package mainstreet.infrastructure.persistence.businesshours;

import mainstreet.application.MerchantScope;
import grandrue.businesshours.BusinessHoursFailureCategory;
import grandrue.businesshours.BusinessHoursMutationException;
import grandrue.businesshours.BusinessHoursScope;
import grandrue.businesshours.BusinessHoursScopeKind;
import grandrue.businesshours.ConfigureStandardBusinessHoursCommand;
import grandrue.businesshours.StandardBusinessHours;
import grandrue.businesshours.StandardBusinessHoursAuthority;
import grandrue.businesshours.StandardBusinessHoursRevision;
import grandrue.businesshours.StandardBusinessHoursRevisionDisposition;
import grandrue.businesshours.WeeklyOperatingInterval;
import grandrue.businesshours.WithdrawStandardBusinessHoursCommand;
import mainstreet.runtime.TrustedExecutionContext;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.exception.DataAccessException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/** PostgreSQL authority for merchant-scope stable weekly Business Hours. */
public final class JooqStandardBusinessHoursAuthority
        implements StandardBusinessHoursAuthority {
    private final DSLContext dsl;
    private final TransactionTemplate transactions;

    public JooqStandardBusinessHoursAuthority(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactions = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
    }

    @Override
    public StandardBusinessHoursRevision configure(
            ConfigureStandardBusinessHoursCommand command,
            TrustedExecutionContext trustedContext
    ) {
        Objects.requireNonNull(command, "command");
        BusinessHoursScope scope = command.standardBusinessHours().scope();
        requireAuthenticated(
                scope,
                command.actingPrincipalIdentity(),
                trustedContext
        );
        return mutate(
                scope,
                command.expectedCurrentRevisionIdentity(),
                StandardBusinessHoursRevisionDisposition.CONFIGURED,
                Optional.of(command.standardBusinessHours()),
                command.logicalRequestIdentity(),
                command.actingPrincipalIdentity(),
                command.committedAt()
        );
    }

    @Override
    public StandardBusinessHoursRevision withdraw(
            WithdrawStandardBusinessHoursCommand command,
            TrustedExecutionContext trustedContext
    ) {
        Objects.requireNonNull(command, "command");
        requireAuthenticated(
                command.scope(),
                command.actingPrincipalIdentity(),
                trustedContext
        );
        return mutate(
                command.scope(),
                Optional.of(command.expectedCurrentRevisionIdentity()),
                StandardBusinessHoursRevisionDisposition.WITHDRAWN,
                Optional.empty(),
                command.logicalRequestIdentity(),
                command.actingPrincipalIdentity(),
                command.committedAt()
        );
    }

    @Override
    public Optional<StandardBusinessHoursRevision> current(
            BusinessHoursScope scope
    ) {
        Objects.requireNonNull(scope, "scope");
        Record pointer = dsl.fetchOne(
                "select revision_identifier from current_standard_business_hours "
                        + "where merchant_identifier = ? and scope_kind = ? "
                        + "and merchant_location_identifier is not distinct from ?",
                scope.merchantScope().merchantIdentifier(),
                scope.kind().name(),
                scope.merchantLocationIdentity().orElse(null)
        );
        return pointer == null
                ? Optional.empty()
                : revision(pointer.get("revision_identifier", String.class));
    }

    @Override
    public Optional<StandardBusinessHoursRevision> revision(
            String revisionIdentity
    ) {
        requireIdentifier(revisionIdentity, "revisionIdentity");
        Record row = dsl.fetchOne(
                "select * from standard_business_hours_revision "
                        + "where revision_identifier = ?",
                revisionIdentity
        );
        return Optional.ofNullable(row).map(this::toRevision);
    }

    private StandardBusinessHoursRevision mutate(
            BusinessHoursScope scope,
            Optional<String> expectedRevision,
            StandardBusinessHoursRevisionDisposition disposition,
            Optional<StandardBusinessHours> hours,
            String requestIdentity,
            String actorIdentity,
            Instant committedAt
    ) {
        StandardBusinessHoursRevision result = transactions.execute(status -> {
            lock("standard-business-hours-request|" + requestIdentity, 50401);
            Optional<StandardBusinessHoursRevision> replay = byRequest(
                    requestIdentity
            );
            if (replay.isPresent()) {
                return requireSameIntent(
                        replay.orElseThrow(),
                        scope,
                        expectedRevision,
                        disposition,
                        hours,
                        actorIdentity,
                        committedAt
                );
            }

            MerchantScope merchantScope = scope.merchantScope();
            lock(merchantScope.merchantIdentifier(), 76);
            requireOpenUnsuspendedAccount(merchantScope);
            String controllerRelationship = requireCurrentController(
                    merchantScope,
                    actorIdentity
            );
            Optional<String> merchantLocationRevision =
                    requireCurrentActiveLocation(scope);

            Record pointer = currentPointerForUpdate(scope);
            Optional<String> currentRevision = pointer == null
                    ? Optional.empty()
                    : Optional.of(pointer.get(
                            "revision_identifier",
                            String.class
                    ));
            if (currentRevision.isEmpty()
                    && disposition
                    == StandardBusinessHoursRevisionDisposition.WITHDRAWN) {
                throw failure(
                        BusinessHoursFailureCategory.NOT_CONFIGURED,
                        "Stable Business Hours are not configured"
                );
            }
            if (!currentRevision.equals(expectedRevision)) {
                throw failure(
                        BusinessHoursFailureCategory.REVISION_CONFLICT,
                        "Stable Business Hours current revision changed"
                );
            }

            long nextNumber = pointer == null
                    ? 1L
                    : Math.addExact(
                            pointer.get("revision_number", Long.class),
                            1L
                    );
            String revisionIdentity = "business-hours-revision-"
                    + UUID.randomUUID();
            try {
                insertRevision(
                        revisionIdentity,
                        scope,
                        nextNumber,
                        currentRevision,
                        disposition,
                        hours,
                        requestIdentity,
                        actorIdentity,
                        controllerRelationship,
                        merchantLocationRevision,
                        committedAt
                );
                hours.ifPresent(value -> insertIntervals(
                        revisionIdentity,
                        value.weeklyOperatingIntervals()
                ));
                advancePointer(
                        pointer,
                        revisionIdentity,
                        scope,
                        nextNumber
                );
            } catch (DataAccessException exception) {
                throw new BusinessHoursMutationException(
                        BusinessHoursFailureCategory.PERSISTENCE_CONFLICT,
                        "Stable Business Hours revision could not commit",
                        exception
                );
            }
            return revision(revisionIdentity).orElseThrow(() ->
                    new IllegalStateException(
                            "Committed Business Hours revision is missing"
                    )
            );
        });
        return Objects.requireNonNull(
                result,
                "Business Hours mutation returned no revision"
        );
    }

    private Optional<StandardBusinessHoursRevision> byRequest(String request) {
        Record row = dsl.fetchOne(
                "select * from standard_business_hours_revision "
                        + "where logical_request_identifier = ?",
                request
        );
        return Optional.ofNullable(row).map(this::toRevision);
    }

    private Record currentPointerForUpdate(BusinessHoursScope scope) {
        return dsl.fetchOne(
                "select * from current_standard_business_hours "
                        + "where merchant_identifier = ? and scope_kind = ? "
                        + "and merchant_location_identifier is not distinct from ? "
                        + "for update",
                scope.merchantScope().merchantIdentifier(),
                scope.kind().name(),
                scope.merchantLocationIdentity().orElse(null)
        );
    }

    private void requireOpenUnsuspendedAccount(MerchantScope merchantScope) {
        Record account = dsl.fetchOne(
                "select lifecycle from merchant_account "
                        + "where merchant_identifier = ? for share",
                merchantScope.merchantIdentifier()
        );
        if (account == null
                || !"OPEN".equals(account.get("lifecycle", String.class))
                || dsl.fetchOne(
                        "select 1 from merchant_account_suspension "
                                + "where merchant_identifier = ? "
                                + "and released_at is null limit 1",
                        merchantScope.merchantIdentifier()
                ) != null) {
            throw failure(
                    BusinessHoursFailureCategory
                            .MERCHANT_ACCOUNT_OPERATION_RESTRICTED,
                    "Merchant Account does not permit ordinary mutation"
            );
        }
    }

    private String requireCurrentController(
            MerchantScope merchantScope,
            String actorIdentity
    ) {
        Record controller = dsl.fetchOne(
                "select controller_relationship_identifier, identity_identifier "
                        + "from merchant_controller_relationship "
                        + "where merchant_identifier = ? "
                        + "and lifecycle = 'ACTIVE' for share",
                merchantScope.merchantIdentifier()
        );
        if (controller == null
                || !actorIdentity.equals(controller.get(
                        "identity_identifier",
                        String.class
                ))) {
            throw failure(
                    BusinessHoursFailureCategory.CURRENT_CONTROLLER_REQUIRED,
                    "Current Merchant Controller authority is required"
            );
        }
        return controller.get(
                "controller_relationship_identifier",
                String.class
        );
    }

    private void insertRevision(
            String revisionIdentity,
            BusinessHoursScope scope,
            long revisionNumber,
            Optional<String> predecessor,
            StandardBusinessHoursRevisionDisposition disposition,
            Optional<StandardBusinessHours> hours,
            String requestIdentity,
            String actorIdentity,
            String controllerRelationship,
            Optional<String> merchantLocationRevision,
            Instant committedAt
    ) {
        dsl.execute(
                "insert into standard_business_hours_revision "
                        + "(revision_identifier, merchant_identifier, scope_kind, "
                        + "merchant_location_identifier, revision_number, "
                        + "predecessor_revision_identifier, disposition, "
                        + "time_zone_identifier, logical_request_identifier, "
                        + "acting_principal_identifier, controller_relationship_identifier, "
                        + "merchant_location_revision_identifier, committed_at) values "
                        + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, cast(? as timestamptz))",
                revisionIdentity,
                scope.merchantScope().merchantIdentifier(),
                scope.kind().name(),
                scope.merchantLocationIdentity().orElse(null),
                revisionNumber,
                predecessor.orElse(null),
                disposition.name(),
                hours.map(value -> value.timeZone().getId()).orElse(null),
                requestIdentity,
                actorIdentity,
                controllerRelationship,
                merchantLocationRevision.orElse(null),
                committedAt.toString()
        );
    }

    private void insertIntervals(
            String revisionIdentity,
            Set<WeeklyOperatingInterval> intervals
    ) {
        List<WeeklyOperatingInterval> normalized = new ArrayList<>(intervals);
        normalized.sort(
                Comparator.comparing(WeeklyOperatingInterval::startDay)
                        .thenComparing(WeeklyOperatingInterval::startTime)
                        .thenComparing(WeeklyOperatingInterval::endTime)
        );
        for (int index = 0; index < normalized.size(); index++) {
            WeeklyOperatingInterval interval = normalized.get(index);
            dsl.execute(
                    "insert into standard_business_hours_interval "
                            + "(revision_identifier, interval_sequence, start_day, "
                            + "start_local_time, end_local_time) values (?, ?, ?, ?, ?)",
                    revisionIdentity,
                    index,
                    interval.startDay().getValue(),
                    interval.startTime(),
                    interval.endTime()
            );
        }
    }

    private void advancePointer(
            Record pointer,
            String revisionIdentity,
            BusinessHoursScope scope,
            long revisionNumber
    ) {
        if (pointer == null) {
            dsl.execute(
                    "insert into current_standard_business_hours "
                            + "(current_pointer_identifier, merchant_identifier, "
                            + "scope_kind, merchant_location_identifier, "
                            + "revision_identifier, revision_number) "
                            + "values (?, ?, ?, ?, ?, ?)",
                    "business-hours-current-" + UUID.randomUUID(),
                    scope.merchantScope().merchantIdentifier(),
                    scope.kind().name(),
                    scope.merchantLocationIdentity().orElse(null),
                    revisionIdentity,
                    revisionNumber
            );
            return;
        }
        int updated = dsl.execute(
                "update current_standard_business_hours "
                        + "set revision_identifier = ?, revision_number = ? "
                        + "where current_pointer_identifier = ?",
                revisionIdentity,
                revisionNumber,
                pointer.get("current_pointer_identifier", String.class)
        );
        if (updated != 1) {
            throw failure(
                    BusinessHoursFailureCategory.REVISION_CONFLICT,
                    "Stable Business Hours current pointer changed"
            );
        }
    }

    private StandardBusinessHoursRevision toRevision(Record row) {
        BusinessHoursScope scope = toScope(row);
        StandardBusinessHoursRevisionDisposition disposition =
                StandardBusinessHoursRevisionDisposition.valueOf(
                        row.get("disposition", String.class)
                );
        Optional<StandardBusinessHours> hours = disposition
                == StandardBusinessHoursRevisionDisposition.CONFIGURED
                ? Optional.of(new StandardBusinessHours(
                        scope,
                        ZoneId.of(row.get("time_zone_identifier", String.class)),
                        intervals(row.get("revision_identifier", String.class))
                ))
                : Optional.empty();
        return new StandardBusinessHoursRevision(
                row.get("revision_identifier", String.class),
                scope,
                row.get("revision_number", Long.class),
                Optional.ofNullable(row.get(
                        "predecessor_revision_identifier",
                        String.class
                )),
                disposition,
                hours,
                Optional.ofNullable(row.get(
                        "merchant_location_revision_identifier",
                        String.class
                )),
                row.get("logical_request_identifier", String.class),
                row.get("acting_principal_identifier", String.class),
                row.get("controller_relationship_identifier", String.class),
                row.get("committed_at", Instant.class)
        );
    }

    private BusinessHoursScope toScope(Record row) {
        MerchantScope merchant = new MerchantScope(
                row.get("merchant_identifier", String.class)
        );
        BusinessHoursScopeKind kind = BusinessHoursScopeKind.valueOf(
                row.get("scope_kind", String.class)
        );
        return kind == BusinessHoursScopeKind.MERCHANT
                ? BusinessHoursScope.merchant(merchant)
                : BusinessHoursScope.merchantLocation(
                        merchant,
                        row.get("merchant_location_identifier", String.class)
                );
    }

    private Set<WeeklyOperatingInterval> intervals(String revisionIdentity) {
        LinkedHashSet<WeeklyOperatingInterval> result = new LinkedHashSet<>();
        dsl.fetch(
                "select start_day, start_local_time, end_local_time "
                        + "from standard_business_hours_interval "
                        + "where revision_identifier = ? order by interval_sequence",
                revisionIdentity
        ).forEach(row -> result.add(new WeeklyOperatingInterval(
                DayOfWeek.of(row.get("start_day", Short.class).intValue()),
                row.get("start_local_time", LocalTime.class),
                row.get("end_local_time", LocalTime.class)
        )));
        return Set.copyOf(result);
    }

    private static StandardBusinessHoursRevision requireSameIntent(
            StandardBusinessHoursRevision replay,
            BusinessHoursScope scope,
            Optional<String> expectedRevision,
            StandardBusinessHoursRevisionDisposition disposition,
            Optional<StandardBusinessHours> hours,
            String actorIdentity,
            Instant committedAt
    ) {
        if (!replay.scope().equals(scope)
                || !replay.predecessorRevisionIdentity().equals(expectedRevision)
                || replay.disposition() != disposition
                || !replay.standardBusinessHours().equals(hours)
                || !replay.actingPrincipalIdentity().equals(actorIdentity)
                || !replay.committedAt().equals(committedAt)) {
            throw failure(
                    BusinessHoursFailureCategory.IDEMPOTENCY_CONFLICT,
                    "Business Hours request identity is bound to different intent"
            );
        }
        return replay;
    }

    private Optional<String> requireCurrentActiveLocation(
            BusinessHoursScope scope
    ) {
        if (scope.kind() == BusinessHoursScopeKind.MERCHANT) {
            return Optional.empty();
        }
        String merchant = scope.merchantScope().merchantIdentifier();
        String location = scope.merchantLocationIdentity().orElseThrow();
        lock(merchant + "|" + location, 512);
        Record pointer = dsl.fetchOne(
                "select revision_identifier, lifecycle "
                        + "from current_merchant_location "
                        + "where merchant_identifier = ? and location_identifier = ? "
                        + "for share",
                merchant,
                location
        );
        if (pointer == null) {
            throw failure(
                    BusinessHoursFailureCategory.MERCHANT_LOCATION_NOT_FOUND,
                    "Merchant Location is not established"
            );
        }
        if (!"ACTIVE".equals(pointer.get("lifecycle", String.class))) {
            throw failure(
                    BusinessHoursFailureCategory.MERCHANT_LOCATION_RETIRED,
                    "Merchant Location is retired"
            );
        }
        return Optional.of(pointer.get("revision_identifier", String.class));
    }

    private static void requireAuthenticated(
            BusinessHoursScope scope,
            String actorIdentity,
            TrustedExecutionContext context
    ) {
        if (context == null
                || context.authentication().isEmpty()
                || !context.merchantScope().equals(scope.merchantScope())
                || !context.principal().identifier().equals(actorIdentity)
                || !context.authentication().orElseThrow()
                        .identityIdentifier().equals(actorIdentity)) {
            throw failure(
                    BusinessHoursFailureCategory.AUTHENTICATION_REQUIRED,
                    "Matching authenticated trusted principal is required"
            );
        }
    }

    private void lock(String key, int namespace) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), ?))",
                key,
                namespace
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }

    private static BusinessHoursMutationException failure(
            BusinessHoursFailureCategory category,
            String message
    ) {
        return new BusinessHoursMutationException(category, message);
    }
}
