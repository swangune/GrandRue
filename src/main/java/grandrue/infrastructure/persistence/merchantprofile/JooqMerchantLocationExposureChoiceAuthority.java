package grandrue.infrastructure.persistence.merchantprofile;

import grandrue.application.MerchantScope;
import grandrue.merchantprofile.ExpectedMerchantLocationExposureChoice;
import mainstreet.merchantprofile.MerchantLocationExposure;
import grandrue.merchantprofile.MerchantLocationExposureChoiceAuthority;
import mainstreet.merchantprofile.MerchantLocationExposureChoiceRevision;
import mainstreet.merchantprofile.MerchantProfileFailureCategory;
import mainstreet.merchantprofile.MerchantProfileMutationException;
import mainstreet.merchantprofile.SetMerchantLocationExposureChoiceCommand;
import grandrue.runtime.TrustedExecutionContext;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.exception.DataAccessException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/** PostgreSQL authority for independently revisioned Merchant Location Exposure choice. */
public final class JooqMerchantLocationExposureChoiceAuthority
        implements MerchantLocationExposureChoiceAuthority {
    private static final int LOCATION_LOCK_NAMESPACE = 512;
    private static final int REQUEST_LOCK_NAMESPACE = 520;
    private static final int MERCHANT_LOCK_NAMESPACE = 76;

    private final DSLContext dsl;
    private final TransactionTemplate transactions;

    public JooqMerchantLocationExposureChoiceAuthority(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactions = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
    }

    @Override
    public MerchantLocationExposureChoiceRevision set(
            SetMerchantLocationExposureChoiceCommand command,
            TrustedExecutionContext trustedContext
    ) {
        Objects.requireNonNull(command, "command");
        requireAuthenticated(
                command.merchantScope(),
                command.actingPrincipalIdentity(),
                trustedContext
        );
        MerchantLocationExposureChoiceRevision result = transactions.execute(status -> {
            lock(
                    "merchant-location-exposure-request|"
                            + command.logicalRequestIdentity(),
                    REQUEST_LOCK_NAMESPACE
            );
            Optional<MerchantLocationExposureChoiceRevision> replay = byRequest(
                    command.logicalRequestIdentity()
            );
            if (replay.isPresent()) {
                return requireSameIntent(command, replay.orElseThrow());
            }

            String merchant = command.merchantScope().merchantIdentifier();
            lock(merchant, MERCHANT_LOCK_NAMESPACE);
            requireOpenUnsuspendedAccount(command.merchantScope());
            String controllerRelationship = requireCurrentController(
                    command.merchantScope(),
                    command.actingPrincipalIdentity()
            );

            // Reuse the existing Merchant Location identity lock so retirement and
            // choice establishment cannot cross without an ordered lifecycle check.
            lock(
                    merchant + "|" + command.locationIdentity(),
                    LOCATION_LOCK_NAMESPACE
            );
            requireCurrentActiveLocation(
                    command.merchantScope(),
                    command.locationIdentity()
            );

            Record pointer = currentPointerForUpdate(
                    command.merchantScope(),
                    command.locationIdentity()
            );
            Optional<MerchantLocationExposureChoiceRevision> current = pointer == null
                    ? Optional.empty()
                    : revision(pointer.get("revision_identifier", String.class));
            requireExpectedCurrent(command.expectedCurrentChoice(), current);

            long revisionNumber = current
                    .map(MerchantLocationExposureChoiceRevision::revisionNumber)
                    .map(number -> Math.addExact(number, 1L))
                    .orElse(1L);
            Optional<String> predecessor = current.map(
                    MerchantLocationExposureChoiceRevision::revisionIdentity
            );
            String revisionIdentity = "merchant-location-exposure-choice-revision-"
                    + UUID.randomUUID();

            try {
                insertRevision(
                        revisionIdentity,
                        revisionNumber,
                        predecessor,
                        command,
                        controllerRelationship
                );
                advancePointer(
                        pointer,
                        revisionIdentity,
                        revisionNumber,
                        command
                );
            } catch (DataAccessException failure) {
                throw new MerchantProfileMutationException(
                        MerchantProfileFailureCategory.TECHNICAL_FAILURE_BEFORE_COMMIT,
                        "Merchant Location Exposure choice revision could not commit",
                        failure
                );
            }
            return revision(revisionIdentity).orElseThrow(() ->
                    new IllegalStateException(
                            "Committed Merchant Location Exposure choice revision is missing"
                    )
            );
        });
        if (result == null) {
            throw new IllegalStateException(
                    "Merchant Location Exposure choice mutation returned no revision"
            );
        }
        return result;
    }

    @Override
    public Optional<MerchantLocationExposureChoiceRevision> current(
            MerchantScope merchantScope,
            String locationIdentity
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(locationIdentity, "locationIdentity");
        Record pointer = dsl.fetchOne(
                "select revision_identifier "
                        + "from current_merchant_location_exposure_choice "
                        + "where merchant_identifier = ? and location_identifier = ?",
                merchantScope.merchantIdentifier(),
                locationIdentity
        );
        return pointer == null
                ? Optional.empty()
                : revision(pointer.get("revision_identifier", String.class));
    }

    @Override
    public Optional<MerchantLocationExposureChoiceRevision> revision(
            String revisionIdentity
    ) {
        requireIdentifier(revisionIdentity, "revisionIdentity");
        Record row = dsl.fetchOne(
                "select * from merchant_location_exposure_choice_revision "
                        + "where revision_identifier = ?",
                revisionIdentity
        );
        return Optional.ofNullable(row).map(this::toRevision);
    }

    private Optional<MerchantLocationExposureChoiceRevision> byRequest(
            String logicalRequestIdentity
    ) {
        Record row = dsl.fetchOne(
                "select * from merchant_location_exposure_choice_revision "
                        + "where logical_request_identifier = ?",
                logicalRequestIdentity
        );
        return Optional.ofNullable(row).map(this::toRevision);
    }

    private MerchantLocationExposureChoiceRevision requireSameIntent(
            SetMerchantLocationExposureChoiceCommand command,
            MerchantLocationExposureChoiceRevision replay
    ) {
        if (!replay.merchantScope().equals(command.merchantScope())
                || !replay.locationIdentity().equals(command.locationIdentity())
                || replay.exposure() != command.exposure()
                || !replay.provenanceReference().equals(command.provenanceReference())
                || !replay.actingPrincipalIdentity().equals(
                        command.actingPrincipalIdentity()
                )
                || !replay.committedAt().equals(command.committedAt())) {
            throw failure(
                    MerchantProfileFailureCategory.REQUEST_IDENTITY_CONFLICT,
                    "Logical request identity was already used for different Location Exposure intent"
            );
        }
        return replay;
    }

    private void requireExpectedCurrent(
            ExpectedMerchantLocationExposureChoice expected,
            Optional<MerchantLocationExposureChoiceRevision> current
    ) {
        if (expected instanceof ExpectedMerchantLocationExposureChoice.Absent) {
            if (current.isPresent()) {
                throw failure(
                        MerchantProfileFailureCategory.PROFILE_REVISION_CONFLICT,
                        "Merchant Location Exposure choice is already established"
                );
            }
            return;
        }
        String expectedRevision = ((ExpectedMerchantLocationExposureChoice.Revision) expected)
                .revisionIdentity();
        if (current.isEmpty()
                || !current.orElseThrow().revisionIdentity().equals(expectedRevision)) {
            throw failure(
                    MerchantProfileFailureCategory.PROFILE_REVISION_CONFLICT,
                    "Merchant Location Exposure choice current revision changed"
            );
        }
    }

    private Record currentPointerForUpdate(
            MerchantScope merchantScope,
            String locationIdentity
    ) {
        return dsl.fetchOne(
                "select * from current_merchant_location_exposure_choice "
                        + "where merchant_identifier = ? and location_identifier = ? "
                        + "for update",
                merchantScope.merchantIdentifier(),
                locationIdentity
        );
    }

    private void requireCurrentActiveLocation(
            MerchantScope merchantScope,
            String locationIdentity
    ) {
        Record pointer = dsl.fetchOne(
                "select lifecycle from current_merchant_location "
                        + "where merchant_identifier = ? and location_identifier = ? "
                        + "for share",
                merchantScope.merchantIdentifier(),
                locationIdentity
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
    }

    private void insertRevision(
            String revisionIdentity,
            long revisionNumber,
            Optional<String> predecessor,
            SetMerchantLocationExposureChoiceCommand command,
            String controllerRelationship
    ) {
        dsl.execute(
                "insert into merchant_location_exposure_choice_revision "
                        + "(revision_identifier, merchant_identifier, location_identifier, "
                        + "revision_number, predecessor_revision_identifier, exposure_choice, "
                        + "logical_request_identifier, provenance_reference, "
                        + "acting_principal_identifier, controller_relationship_identifier, "
                        + "committed_at) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, cast(? as timestamptz))",
                revisionIdentity,
                command.merchantScope().merchantIdentifier(),
                command.locationIdentity(),
                revisionNumber,
                predecessor.orElse(null),
                command.exposure().name(),
                command.logicalRequestIdentity(),
                command.provenanceReference(),
                command.actingPrincipalIdentity(),
                controllerRelationship,
                command.committedAt().toString()
        );
    }

    private void advancePointer(
            Record pointer,
            String revisionIdentity,
            long revisionNumber,
            SetMerchantLocationExposureChoiceCommand command
    ) {
        if (pointer == null) {
            dsl.execute(
                    "insert into current_merchant_location_exposure_choice "
                            + "(current_pointer_identifier, merchant_identifier, "
                            + "location_identifier, revision_identifier, revision_number) "
                            + "values (?, ?, ?, ?, ?)",
                    "merchant-location-exposure-choice-current-" + UUID.randomUUID(),
                    command.merchantScope().merchantIdentifier(),
                    command.locationIdentity(),
                    revisionIdentity,
                    revisionNumber
            );
            return;
        }
        int updated = dsl.execute(
                "update current_merchant_location_exposure_choice "
                        + "set revision_identifier = ?, revision_number = ? "
                        + "where current_pointer_identifier = ?",
                revisionIdentity,
                revisionNumber,
                pointer.get("current_pointer_identifier", String.class)
        );
        if (updated != 1) {
            throw failure(
                    MerchantProfileFailureCategory.PROFILE_REVISION_CONFLICT,
                    "Merchant Location Exposure choice current pointer changed"
            );
        }
    }

    private MerchantLocationExposureChoiceRevision toRevision(Record row) {
        return new MerchantLocationExposureChoiceRevision(
                row.get("revision_identifier", String.class),
                new MerchantScope(row.get("merchant_identifier", String.class)),
                row.get("location_identifier", String.class),
                row.get("revision_number", Long.class),
                Optional.ofNullable(row.get(
                        "predecessor_revision_identifier",
                        String.class
                )),
                MerchantLocationExposure.valueOf(
                        row.get("exposure_choice", String.class)
                ),
                row.get("logical_request_identifier", String.class),
                row.get("provenance_reference", String.class),
                row.get("acting_principal_identifier", String.class),
                row.get("controller_relationship_identifier", String.class),
                row.get("committed_at", Instant.class)
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
                    MerchantProfileFailureCategory.MERCHANT_ACCOUNT_OPERATION_RESTRICTED,
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
                        + "where merchant_identifier = ? and lifecycle = 'ACTIVE' for share",
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
        return controller.get("controller_relationship_identifier", String.class);
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
                "select pg_advisory_xact_lock(?, hashtext(?))",
                namespace,
                key
        );
    }

    private static void requireIdentifier(String value, String name) {
        Objects.requireNonNull(value, name);
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
    }

    private static MerchantProfileMutationException failure(
            MerchantProfileFailureCategory category,
            String message
    ) {
        return new MerchantProfileMutationException(category, message);
    }
}
