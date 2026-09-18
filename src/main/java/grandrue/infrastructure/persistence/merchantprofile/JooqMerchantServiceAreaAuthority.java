package grandrue.infrastructure.persistence.merchantprofile;

import grandrue.application.MerchantScope;
import mainstreet.merchantprofile.CountryWideGeographyV1;
import mainstreet.merchantprofile.CreateMerchantServiceAreaCommand;
import mainstreet.merchantprofile.MerchantLocationRadiusGeographyV1;
import mainstreet.merchantprofile.MerchantProfileFailureCategory;
import mainstreet.merchantprofile.MerchantProfileMutationException;
import mainstreet.merchantprofile.MerchantServiceAreaAuthority;
import mainstreet.merchantprofile.MerchantServiceAreaExposure;
import mainstreet.merchantprofile.MerchantServiceAreaGeographyKind;
import grandrue.merchantprofile.MerchantServiceAreaLifecycle;
import mainstreet.merchantprofile.MerchantServiceAreaRevision;
import mainstreet.merchantprofile.NamedAreaGeographyV1;
import mainstreet.merchantprofile.RemoteCountriesGeographyV1;
import mainstreet.merchantprofile.RetireMerchantServiceAreaCommand;
import mainstreet.merchantprofile.ServiceAreaGeographyV1;
import mainstreet.merchantprofile.UpdateMerchantServiceAreaCommand;
import grandrue.runtime.TrustedExecutionContext;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.exception.DataAccessException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/** PostgreSQL authority for immutable Merchant Service Area revisions. */
public final class JooqMerchantServiceAreaAuthority
        implements MerchantServiceAreaAuthority {
    private final DSLContext dsl;
    private final TransactionTemplate transactions;

    public JooqMerchantServiceAreaAuthority(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactions = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
    }

    @Override
    public MerchantServiceAreaRevision create(
            CreateMerchantServiceAreaCommand command,
            TrustedExecutionContext trustedContext
    ) {
        Objects.requireNonNull(command, "command");
        requireAuthenticated(
                command.merchantScope(),
                command.actingPrincipalIdentity(),
                trustedContext
        );
        return mutate(new MutationIntent(
                OperationKind.CREATE,
                command.merchantScope(),
                command.serviceAreaIdentity(),
                Optional.empty(),
                Optional.of(command.geography()),
                Optional.of(command.publicDescription()),
                Optional.of(command.exposure()),
                command.logicalRequestIdentity(),
                command.provenanceReference(),
                command.actingPrincipalIdentity(),
                command.committedAt()
        ));
    }

    @Override
    public MerchantServiceAreaRevision update(
            UpdateMerchantServiceAreaCommand command,
            TrustedExecutionContext trustedContext
    ) {
        Objects.requireNonNull(command, "command");
        requireAuthenticated(
                command.merchantScope(),
                command.actingPrincipalIdentity(),
                trustedContext
        );
        return mutate(new MutationIntent(
                OperationKind.UPDATE,
                command.merchantScope(),
                command.serviceAreaIdentity(),
                Optional.of(command.expectedCurrentRevisionIdentity()),
                Optional.of(command.geography()),
                Optional.of(command.publicDescription()),
                Optional.of(command.exposure()),
                command.logicalRequestIdentity(),
                command.provenanceReference(),
                command.actingPrincipalIdentity(),
                command.committedAt()
        ));
    }

    @Override
    public MerchantServiceAreaRevision retire(
            RetireMerchantServiceAreaCommand command,
            TrustedExecutionContext trustedContext
    ) {
        Objects.requireNonNull(command, "command");
        requireAuthenticated(
                command.merchantScope(),
                command.actingPrincipalIdentity(),
                trustedContext
        );
        return mutate(new MutationIntent(
                OperationKind.RETIRE,
                command.merchantScope(),
                command.serviceAreaIdentity(),
                Optional.of(command.expectedCurrentRevisionIdentity()),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                command.logicalRequestIdentity(),
                command.provenanceReference(),
                command.actingPrincipalIdentity(),
                command.committedAt()
        ));
    }

    @Override
    public Optional<MerchantServiceAreaRevision> current(
            MerchantScope merchantScope,
            String serviceAreaIdentity
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(serviceAreaIdentity, "serviceAreaIdentity");
        Record pointer = dsl.fetchOne(
                "select revision_identifier from current_merchant_service_area "
                        + "where merchant_identifier = ? "
                        + "and service_area_identifier = ?",
                merchantScope.merchantIdentifier(),
                serviceAreaIdentity
        );
        return pointer == null
                ? Optional.empty()
                : revision(pointer.get("revision_identifier", String.class));
    }

    @Override
    public Optional<MerchantServiceAreaRevision> revision(
            String revisionIdentity
    ) {
        requireIdentifier(revisionIdentity, "revisionIdentity");
        Record row = dsl.fetchOne(
                "select * from merchant_service_area_revision "
                        + "where revision_identifier = ?",
                revisionIdentity
        );
        return Optional.ofNullable(row).map(this::toRevision);
    }

    private MerchantServiceAreaRevision mutate(MutationIntent intent) {
        MerchantServiceAreaRevision result = transactions.execute(status -> {
            lock("merchant-service-area-request|" + intent.requestIdentity(),
                    535);
            Optional<MerchantServiceAreaRevision> replay = byRequest(
                    intent.requestIdentity()
            );
            if (replay.isPresent()) {
                return requireSameIntent(intent, replay.orElseThrow());
            }

            String merchant = intent.merchantScope().merchantIdentifier();
            lock(merchant, 76);
            requireOpenUnsuspendedAccount(intent.merchantScope());
            String controllerRelationship = requireCurrentController(
                    intent.merchantScope(),
                    intent.actorIdentity()
            );
            lock(merchant + "|" + intent.serviceAreaIdentity(), 531);

            Record pointer = currentPointerForUpdate(intent);
            Optional<MerchantServiceAreaRevision> current = pointer == null
                    ? Optional.empty()
                    : revision(pointer.get(
                            "revision_identifier",
                            String.class
                    ));
            RevisionMaterial material = resolveMaterial(intent, current);
            String revisionIdentity = "merchant-service-area-revision-"
                    + UUID.randomUUID();
            try {
                insertRevision(
                        revisionIdentity,
                        intent,
                        material,
                        controllerRelationship
                );
                insertRemoteCountries(revisionIdentity, material.geography());
                advancePointer(
                        pointer,
                        revisionIdentity,
                        intent,
                        material
                );
            } catch (DataAccessException failure) {
                throw new MerchantProfileMutationException(
                        MerchantProfileFailureCategory
                                .TECHNICAL_FAILURE_BEFORE_COMMIT,
                        "Service Area revision could not commit",
                        failure
                );
            }
            return revision(revisionIdentity).orElseThrow(() ->
                    new IllegalStateException(
                            "Committed Service Area revision is missing"
                    )
            );
        });
        if (result == null) {
            throw new IllegalStateException(
                    "Service Area mutation returned no revision"
            );
        }
        return result;
    }

    private RevisionMaterial resolveMaterial(
            MutationIntent intent,
            Optional<MerchantServiceAreaRevision> current
    ) {
        if (intent.operation() == OperationKind.CREATE) {
            if (current.isPresent()) {
                throw failure(
                        MerchantProfileFailureCategory.PROFILE_REVISION_CONFLICT,
                        "Service Area identity is already established"
                );
            }
            ServiceAreaGeographyV1 geography = requireAdmittedGeography(
                    intent.merchantScope(),
                    intent.geography().orElseThrow()
            );
            return new RevisionMaterial(
                    1L,
                    Optional.empty(),
                    MerchantServiceAreaLifecycle.ACTIVE,
                    geography,
                    intent.publicDescription().orElseThrow(),
                    intent.exposure().orElseThrow()
            );
        }

        MerchantServiceAreaRevision authoritative = current.orElseThrow(() ->
                failure(
                        MerchantProfileFailureCategory.PROFILE_FACT_NOT_FOUND,
                        "Service Area identity is not established"
                )
        );
        if (authoritative.lifecycle() == MerchantServiceAreaLifecycle.RETIRED) {
            throw failure(
                    MerchantProfileFailureCategory.PROFILE_FACT_RETIRED,
                    "Service Area identity is retired"
            );
        }
        if (!intent.expectedRevision().orElseThrow().equals(
                authoritative.revisionIdentity()
        )) {
            throw failure(
                    MerchantProfileFailureCategory.PROFILE_REVISION_CONFLICT,
                    "Service Area current revision changed"
            );
        }
        long next = Math.addExact(authoritative.revisionNumber(), 1L);
        if (intent.operation() == OperationKind.RETIRE) {
            return new RevisionMaterial(
                    next,
                    Optional.of(authoritative.revisionIdentity()),
                    MerchantServiceAreaLifecycle.RETIRED,
                    authoritative.geography(),
                    authoritative.publicDescription(),
                    authoritative.exposure()
            );
        }
        ServiceAreaGeographyV1 geography = requireAdmittedGeography(
                intent.merchantScope(),
                intent.geography().orElseThrow()
        );
        return new RevisionMaterial(
                next,
                Optional.of(authoritative.revisionIdentity()),
                MerchantServiceAreaLifecycle.ACTIVE,
                geography,
                intent.publicDescription().orElseThrow(),
                intent.exposure().orElseThrow()
        );
    }

    private ServiceAreaGeographyV1 requireAdmittedGeography(
            MerchantScope merchantScope,
            ServiceAreaGeographyV1 geography
    ) {
        if (!(geography instanceof MerchantLocationRadiusGeographyV1 radius)) {
            return geography;
        }
        String merchant = merchantScope.merchantIdentifier();
        lock(merchant + "|" + radius.merchantLocationIdentity(), 512);
        Record pointer = dsl.fetchOne(
                "select revision_identifier, lifecycle "
                        + "from current_merchant_location "
                        + "where merchant_identifier = ? "
                        + "and location_identifier = ? for share",
                merchant,
                radius.merchantLocationIdentity()
        );
        if (pointer == null) {
            throw failure(
                    MerchantProfileFailureCategory.PROFILE_FACT_NOT_FOUND,
                    "Merchant Location is not established"
            );
        }
        if (!"ACTIVE".equals(pointer.get("lifecycle", String.class))) {
            throw failure(
                    MerchantProfileFailureCategory.PROFILE_FACT_RETIRED,
                    "Merchant Location is retired"
            );
        }
        if (!radius.merchantLocationRevisionIdentity().equals(
                pointer.get("revision_identifier", String.class)
        )) {
            throw failure(
                    MerchantProfileFailureCategory.PROFILE_REVISION_CONFLICT,
                    "Merchant Location current revision changed"
            );
        }
        return geography;
    }

    private Optional<MerchantServiceAreaRevision> byRequest(String request) {
        Record row = dsl.fetchOne(
                "select * from merchant_service_area_revision "
                        + "where logical_request_identifier = ?",
                request
        );
        return Optional.ofNullable(row).map(this::toRevision);
    }

    private Record currentPointerForUpdate(MutationIntent intent) {
        return dsl.fetchOne(
                "select * from current_merchant_service_area "
                        + "where merchant_identifier = ? "
                        + "and service_area_identifier = ? for update",
                intent.merchantScope().merchantIdentifier(),
                intent.serviceAreaIdentity()
        );
    }

    private void insertRevision(
            String revisionIdentity,
            MutationIntent intent,
            RevisionMaterial material,
            String controllerRelationship
    ) {
        GeographyColumns geography = columns(material.geography());
        dsl.execute(
                "insert into merchant_service_area_revision "
                        + "(revision_identifier, merchant_identifier, "
                        + "service_area_identifier, revision_number, "
                        + "predecessor_revision_identifier, operation_kind, "
                        + "lifecycle, geography_schema_identifier, "
                        + "geography_kind, country_code, area_name, "
                        + "merchant_location_identifier, "
                        + "merchant_location_revision_identifier, radius_metres, "
                        + "public_description, exposure_choice, "
                        + "logical_request_identifier, provenance_reference, "
                        + "acting_principal_identifier, "
                        + "controller_relationship_identifier, committed_at) "
                        + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                        + "?, ?, ?, ?, ?, ?, cast(? as timestamptz))",
                revisionIdentity,
                intent.merchantScope().merchantIdentifier(),
                intent.serviceAreaIdentity(),
                material.revisionNumber(),
                material.predecessorRevisionIdentity().orElse(null),
                intent.operation().name(),
                material.lifecycle().name(),
                material.geography().schemaIdentity(),
                material.geography().kind().name(),
                geography.countryCode().orElse(null),
                geography.areaName().orElse(null),
                geography.merchantLocationIdentity().orElse(null),
                geography.merchantLocationRevisionIdentity().orElse(null),
                geography.radiusMetres().orElse(null),
                material.publicDescription(),
                material.exposure().name(),
                intent.requestIdentity(),
                intent.provenanceReference(),
                intent.actorIdentity(),
                controllerRelationship,
                intent.committedAt().toString()
        );
    }

    private void insertRemoteCountries(
            String revisionIdentity,
            ServiceAreaGeographyV1 geography
    ) {
        if (!(geography instanceof RemoteCountriesGeographyV1 remote)) {
            return;
        }
        for (int index = 0; index < remote.countryCodes().size(); index++) {
            dsl.execute(
                    "insert into merchant_service_area_remote_country "
                            + "(revision_identifier, country_ordinal, country_code) "
                            + "values (?, ?, ?)",
                    revisionIdentity,
                    index,
                    remote.countryCodes().get(index)
            );
        }
    }

    private void advancePointer(
            Record pointer,
            String revisionIdentity,
            MutationIntent intent,
            RevisionMaterial material
    ) {
        if (pointer == null) {
            dsl.execute(
                    "insert into current_merchant_service_area "
                            + "(current_pointer_identifier, merchant_identifier, "
                            + "service_area_identifier, revision_identifier, "
                            + "revision_number, lifecycle) values (?, ?, ?, ?, ?, ?)",
                    "merchant-service-area-current-" + UUID.randomUUID(),
                    intent.merchantScope().merchantIdentifier(),
                    intent.serviceAreaIdentity(),
                    revisionIdentity,
                    material.revisionNumber(),
                    material.lifecycle().name()
            );
            return;
        }
        int updated = dsl.execute(
                "update current_merchant_service_area "
                        + "set revision_identifier = ?, revision_number = ?, "
                        + "lifecycle = ? where current_pointer_identifier = ?",
                revisionIdentity,
                material.revisionNumber(),
                material.lifecycle().name(),
                pointer.get("current_pointer_identifier", String.class)
        );
        if (updated != 1) {
            throw failure(
                    MerchantProfileFailureCategory.PROFILE_REVISION_CONFLICT,
                    "Service Area current pointer changed"
            );
        }
    }

    private MerchantServiceAreaRevision toRevision(Record row) {
        return new MerchantServiceAreaRevision(
                row.get("revision_identifier", String.class),
                new MerchantScope(row.get("merchant_identifier", String.class)),
                row.get("service_area_identifier", String.class),
                row.get("revision_number", Long.class),
                Optional.ofNullable(row.get(
                        "predecessor_revision_identifier",
                        String.class
                )),
                MerchantServiceAreaLifecycle.valueOf(
                        row.get("lifecycle", String.class)
                ),
                toGeography(row),
                row.get("public_description", String.class),
                MerchantServiceAreaExposure.valueOf(
                        row.get("exposure_choice", String.class)
                ),
                row.get("logical_request_identifier", String.class),
                row.get("provenance_reference", String.class),
                row.get("acting_principal_identifier", String.class),
                row.get("controller_relationship_identifier", String.class),
                row.get("committed_at", Instant.class)
        );
    }

    private ServiceAreaGeographyV1 toGeography(Record row) {
        MerchantServiceAreaGeographyKind kind =
                MerchantServiceAreaGeographyKind.valueOf(
                        row.get("geography_kind", String.class)
                );
        return switch (kind) {
            case NAMED_AREA -> new NamedAreaGeographyV1(
                    row.get("country_code", String.class),
                    row.get("area_name", String.class)
            );
            case MERCHANT_LOCATION_RADIUS ->
                    new MerchantLocationRadiusGeographyV1(
                            row.get(
                                    "merchant_location_identifier",
                                    String.class
                            ),
                            row.get(
                                    "merchant_location_revision_identifier",
                                    String.class
                            ),
                            row.get("radius_metres", Long.class)
                    );
            case COUNTRY_WIDE -> new CountryWideGeographyV1(
                    row.get("country_code", String.class)
            );
            case REMOTE_COUNTRIES -> new RemoteCountriesGeographyV1(
                    remoteCountries(row.get("revision_identifier", String.class))
            );
        };
    }

    private List<String> remoteCountries(String revisionIdentity) {
        return dsl.fetch(
                "select country_code from merchant_service_area_remote_country "
                        + "where revision_identifier = ? order by country_ordinal",
                revisionIdentity
        ).getValues("country_code", String.class);
    }

    private static GeographyColumns columns(ServiceAreaGeographyV1 geography) {
        return switch (geography) {
            case NamedAreaGeographyV1 named -> new GeographyColumns(
                    Optional.of(named.countryCode()),
                    Optional.of(named.areaName()),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty()
            );
            case MerchantLocationRadiusGeographyV1 radius ->
                    new GeographyColumns(
                            Optional.empty(),
                            Optional.empty(),
                            Optional.of(radius.merchantLocationIdentity()),
                            Optional.of(
                                    radius.merchantLocationRevisionIdentity()
                            ),
                            Optional.of(radius.radiusMetres())
                    );
            case CountryWideGeographyV1 country -> new GeographyColumns(
                    Optional.of(country.countryCode()),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty()
            );
            case RemoteCountriesGeographyV1 ignored -> new GeographyColumns(
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty()
            );
        };
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
                    MerchantProfileFailureCategory
                            .MERCHANT_ACCOUNT_OPERATION_RESTRICTED,
                    "Merchant Account does not permit ordinary profile mutation"
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
                    MerchantProfileFailureCategory.AUTHORISATION_REJECTION,
                    "Current Merchant Controller authority is required"
            );
        }
        return controller.get(
                "controller_relationship_identifier",
                String.class
        );
    }

    private static MerchantServiceAreaRevision requireSameIntent(
            MutationIntent intent,
            MerchantServiceAreaRevision replay
    ) {
        OperationKind replayOperation = replay.revisionNumber() == 1
                ? OperationKind.CREATE
                : replay.lifecycle() == MerchantServiceAreaLifecycle.RETIRED
                ? OperationKind.RETIRE
                : OperationKind.UPDATE;
        boolean sameMaterial = intent.operation() == OperationKind.RETIRE
                || (intent.geography().equals(Optional.of(replay.geography()))
                && intent.publicDescription().equals(Optional.of(
                        replay.publicDescription()
                ))
                && intent.exposure().equals(Optional.of(replay.exposure())));
        if (intent.operation() != replayOperation
                || !intent.merchantScope().equals(replay.merchantScope())
                || !intent.serviceAreaIdentity().equals(
                        replay.serviceAreaIdentity()
                )
                || !intent.expectedRevision().equals(
                        replay.predecessorRevisionIdentity()
                )
                || !sameMaterial
                || !intent.provenanceReference().equals(
                        replay.provenanceReference()
                )
                || !intent.actorIdentity().equals(
                        replay.actingPrincipalIdentity()
                )
                || !intent.committedAt().equals(replay.committedAt())) {
            throw failure(
                    MerchantProfileFailureCategory.REQUEST_IDENTITY_CONFLICT,
                    "Service Area request identity is bound to different intent"
            );
        }
        return replay;
    }

    private static void requireAuthenticated(
            MerchantScope merchantScope,
            String actorIdentity,
            TrustedExecutionContext context
    ) {
        if (context == null
                || context.authentication().isEmpty()
                || !context.merchantScope().equals(merchantScope)
                || !context.principal().identifier().equals(actorIdentity)
                || !context.authentication().orElseThrow()
                        .identityIdentifier().equals(actorIdentity)) {
            throw failure(
                    MerchantProfileFailureCategory.AUTHENTICATION_REQUIRED,
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

    private static MerchantProfileMutationException failure(
            MerchantProfileFailureCategory category,
            String message
    ) {
        return new MerchantProfileMutationException(category, message);
    }

    private enum OperationKind {
        CREATE,
        UPDATE,
        RETIRE
    }

    private record MutationIntent(
            OperationKind operation,
            MerchantScope merchantScope,
            String serviceAreaIdentity,
            Optional<String> expectedRevision,
            Optional<ServiceAreaGeographyV1> geography,
            Optional<String> publicDescription,
            Optional<MerchantServiceAreaExposure> exposure,
            String requestIdentity,
            String provenanceReference,
            String actorIdentity,
            Instant committedAt
    ) {
    }

    private record RevisionMaterial(
            long revisionNumber,
            Optional<String> predecessorRevisionIdentity,
            MerchantServiceAreaLifecycle lifecycle,
            ServiceAreaGeographyV1 geography,
            String publicDescription,
            MerchantServiceAreaExposure exposure
    ) {
    }

    private record GeographyColumns(
            Optional<String> countryCode,
            Optional<String> areaName,
            Optional<String> merchantLocationIdentity,
            Optional<String> merchantLocationRevisionIdentity,
            Optional<Long> radiusMetres
    ) {
    }
}
