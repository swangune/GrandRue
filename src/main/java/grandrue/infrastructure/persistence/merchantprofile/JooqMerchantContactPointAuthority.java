package grandrue.infrastructure.persistence.merchantprofile;

import grandrue.application.MerchantScope;
import grandrue.merchantprofile.CreateMerchantContactPointCommand;
import grandrue.merchantprofile.MerchantContactPointAuthority;
import grandrue.merchantprofile.MerchantContactPointExposure;
import grandrue.merchantprofile.MerchantContactPointKind;
import grandrue.merchantprofile.MerchantContactPointLifecycle;
import grandrue.merchantprofile.MerchantContactPointRevision;
import grandrue.merchantprofile.MerchantContactPointScope;
import grandrue.merchantprofile.MerchantContactPointScopeKind;
import grandrue.merchantprofile.MerchantProfileFailureCategory;
import grandrue.merchantprofile.MerchantProfileMutationException;
import grandrue.merchantprofile.RetireMerchantContactPointCommand;
import grandrue.merchantprofile.UpdateMerchantContactPointCommand;
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

/** PostgreSQL authority for immutable Merchant Contact Point revisions. */
public final class JooqMerchantContactPointAuthority
        implements MerchantContactPointAuthority {
    private final DSLContext dsl;
    private final TransactionTemplate transactions;

    public JooqMerchantContactPointAuthority(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactions = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
    }

    @Override
    public MerchantContactPointRevision create(
            CreateMerchantContactPointCommand command,
            TrustedExecutionContext trustedContext
    ) {
        Objects.requireNonNull(command, "command");
        requireAuthenticated(
                command.scope().merchantScope(),
                command.actingPrincipalIdentity(),
                trustedContext
        );
        return mutate(new MutationIntent(
                OperationKind.CREATE,
                command.scope(),
                command.contactPointIdentity(),
                Optional.empty(),
                Optional.of(command.kind()),
                Optional.of(command.value()),
                Optional.of(command.exposure()),
                command.label(),
                command.logicalRequestIdentity(),
                command.provenanceReference(),
                command.actingPrincipalIdentity(),
                command.committedAt()
        ));
    }

    @Override
    public MerchantContactPointRevision update(
            UpdateMerchantContactPointCommand command,
            TrustedExecutionContext trustedContext
    ) {
        Objects.requireNonNull(command, "command");
        requireAuthenticated(
                command.scope().merchantScope(),
                command.actingPrincipalIdentity(),
                trustedContext
        );
        return mutate(new MutationIntent(
                OperationKind.UPDATE,
                command.scope(),
                command.contactPointIdentity(),
                Optional.of(command.expectedCurrentRevisionIdentity()),
                Optional.of(command.kind()),
                Optional.of(command.value()),
                Optional.of(command.exposure()),
                command.label(),
                command.logicalRequestIdentity(),
                command.provenanceReference(),
                command.actingPrincipalIdentity(),
                command.committedAt()
        ));
    }

    @Override
    public MerchantContactPointRevision retire(
            RetireMerchantContactPointCommand command,
            TrustedExecutionContext trustedContext
    ) {
        Objects.requireNonNull(command, "command");
        requireAuthenticated(
                command.scope().merchantScope(),
                command.actingPrincipalIdentity(),
                trustedContext
        );
        return mutate(new MutationIntent(
                OperationKind.RETIRE,
                command.scope(),
                command.contactPointIdentity(),
                Optional.of(command.expectedCurrentRevisionIdentity()),
                Optional.empty(),
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
    public Optional<MerchantContactPointRevision> current(
            MerchantScope merchantScope,
            String contactPointIdentity
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(contactPointIdentity, "contactPointIdentity");
        Record pointer = dsl.fetchOne(
                "select revision_identifier from current_merchant_contact_point "
                        + "where merchant_identifier = ? and contact_point_identifier = ?",
                merchantScope.merchantIdentifier(),
                contactPointIdentity
        );
        return pointer == null
                ? Optional.empty()
                : revision(pointer.get("revision_identifier", String.class));
    }

    @Override
    public Optional<MerchantContactPointRevision> revision(
            String revisionIdentity
    ) {
        requireIdentifier(revisionIdentity, "revisionIdentity");
        Record row = dsl.fetchOne(
                "select * from merchant_contact_point_revision "
                        + "where revision_identifier = ?",
                revisionIdentity
        );
        return Optional.ofNullable(row).map(this::toRevision);
    }

    private MerchantContactPointRevision mutate(MutationIntent intent) {
        MerchantContactPointRevision result = transactions.execute(status -> {
            lock("merchant-contact-request|" + intent.requestIdentity(), 515);
            Optional<MerchantContactPointRevision> replay = byRequest(
                    intent.requestIdentity()
            );
            if (replay.isPresent()) {
                return requireSameIntent(intent, replay.orElseThrow());
            }

            String merchant = intent.scope().merchantScope()
                    .merchantIdentifier();
            lock(merchant, 76);
            requireOpenUnsuspendedAccount(intent.scope().merchantScope());
            String controllerRelationship = requireCurrentController(
                    intent.scope().merchantScope(),
                    intent.actorIdentity()
            );
            lock(merchant + "|" + intent.contactPointIdentity(), 511);

            Record pointer = currentPointerForUpdate(intent);
            Optional<MerchantContactPointRevision> current = pointer == null
                    ? Optional.empty()
                    : revision(pointer.get(
                            "revision_identifier",
                            String.class
                    ));
            RevisionMaterial material = resolveMaterial(intent, current);
            String revisionIdentity = "merchant-contact-point-revision-"
                    + UUID.randomUUID();
            try {
                insertRevision(
                        revisionIdentity,
                        intent,
                        material,
                        controllerRelationship
                );
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
                        "Merchant Contact Point revision could not commit",
                        failure
                );
            }
            return revision(revisionIdentity).orElseThrow(() ->
                    new IllegalStateException(
                            "Committed Merchant Contact Point revision is missing"
                    )
            );
        });
        if (result == null) {
            throw new IllegalStateException(
                    "Merchant Contact Point mutation returned no revision"
            );
        }
        return result;
    }

    private RevisionMaterial resolveMaterial(
            MutationIntent intent,
            Optional<MerchantContactPointRevision> current
    ) {
        if (intent.operation() == OperationKind.CREATE) {
            if (current.isPresent()) {
                throw failure(
                        MerchantProfileFailureCategory.PROFILE_REVISION_CONFLICT,
                        "Merchant Contact Point identity is already established"
                );
            }
            return new RevisionMaterial(
                    1L,
                    Optional.empty(),
                    MerchantContactPointLifecycle.ACTIVE,
                    intent.kind().orElseThrow(),
                    intent.value().orElseThrow(),
                    intent.exposure().orElseThrow(),
                    intent.label(),
                    requireCurrentActiveLocation(intent.scope())
            );
        }

        MerchantContactPointRevision authoritative = current.orElseThrow(() ->
                failure(
                        MerchantProfileFailureCategory.PROFILE_FACT_NOT_FOUND,
                        "Merchant Contact Point is not established"
                )
        );
        if (!authoritative.scope().equals(intent.scope())) {
            throw failure(
                    MerchantProfileFailureCategory.PROFILE_REVISION_CONFLICT,
                    "Merchant Contact Point scope cannot be replaced"
            );
        }
        if (authoritative.lifecycle()
                == MerchantContactPointLifecycle.RETIRED) {
            throw failure(
                    MerchantProfileFailureCategory.PROFILE_FACT_RETIRED,
                    "Merchant Contact Point identity is retired"
            );
        }
        if (!intent.expectedRevision().orElseThrow().equals(
                authoritative.revisionIdentity()
        )) {
            throw failure(
                    MerchantProfileFailureCategory.PROFILE_REVISION_CONFLICT,
                    "Merchant Contact Point current revision changed"
            );
        }
        long next = Math.addExact(authoritative.revisionNumber(), 1L);
        if (intent.operation() == OperationKind.RETIRE) {
            return new RevisionMaterial(
                    next,
                    Optional.of(authoritative.revisionIdentity()),
                    MerchantContactPointLifecycle.RETIRED,
                    authoritative.kind(),
                    authoritative.value(),
                    authoritative.exposure(),
                    authoritative.label(),
                    authoritative.merchantLocationRevisionIdentity()
            );
        }
        return new RevisionMaterial(
                next,
                Optional.of(authoritative.revisionIdentity()),
                MerchantContactPointLifecycle.ACTIVE,
                intent.kind().orElseThrow(),
                intent.value().orElseThrow(),
                intent.exposure().orElseThrow(),
                intent.label(),
                requireCurrentActiveLocation(intent.scope())
        );
    }

    private Optional<MerchantContactPointRevision> byRequest(String request) {
        Record row = dsl.fetchOne(
                "select * from merchant_contact_point_revision "
                        + "where logical_request_identifier = ?",
                request
        );
        return Optional.ofNullable(row).map(this::toRevision);
    }

    private Record currentPointerForUpdate(MutationIntent intent) {
        return dsl.fetchOne(
                "select * from current_merchant_contact_point "
                        + "where merchant_identifier = ? "
                        + "and contact_point_identifier = ? for update",
                intent.scope().merchantScope().merchantIdentifier(),
                intent.contactPointIdentity()
        );
    }

    private void insertRevision(
            String revisionIdentity,
            MutationIntent intent,
            RevisionMaterial material,
            String controllerRelationship
    ) {
        dsl.execute(
                "insert into merchant_contact_point_revision "
                        + "(revision_identifier, merchant_identifier, "
                        + "contact_point_identifier, scope_kind, "
                        + "merchant_location_identifier, revision_number, "
                        + "predecessor_revision_identifier, operation_kind, "
                        + "lifecycle, contact_kind, contact_value, exposure_choice, "
                        + "contact_label, merchant_location_revision_identifier, "
                        + "logical_request_identifier, provenance_reference, "
                        + "acting_principal_identifier, controller_relationship_identifier, "
                        + "committed_at) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                        + "?, ?, ?, ?, ?, ?, cast(? as timestamptz))",
                revisionIdentity,
                intent.scope().merchantScope().merchantIdentifier(),
                intent.contactPointIdentity(),
                intent.scope().kind().name(),
                intent.scope().merchantLocationIdentity().orElse(null),
                material.revisionNumber(),
                material.predecessorRevisionIdentity().orElse(null),
                intent.operation().name(),
                material.lifecycle().name(),
                material.kind().name(),
                material.value(),
                material.exposure().name(),
                material.label().orElse(null),
                material.merchantLocationRevisionIdentity().orElse(null),
                intent.requestIdentity(),
                intent.provenanceReference(),
                intent.actorIdentity(),
                controllerRelationship,
                intent.committedAt().toString()
        );
    }

    private void advancePointer(
            Record pointer,
            String revisionIdentity,
            MutationIntent intent,
            RevisionMaterial material
    ) {
        if (pointer == null) {
            dsl.execute(
                    "insert into current_merchant_contact_point "
                            + "(current_pointer_identifier, merchant_identifier, "
                            + "contact_point_identifier, revision_identifier, "
                            + "revision_number, lifecycle) values (?, ?, ?, ?, ?, ?)",
                    "merchant-contact-point-current-" + UUID.randomUUID(),
                    intent.scope().merchantScope().merchantIdentifier(),
                    intent.contactPointIdentity(),
                    revisionIdentity,
                    material.revisionNumber(),
                    material.lifecycle().name()
            );
            return;
        }
        int updated = dsl.execute(
                "update current_merchant_contact_point "
                        + "set revision_identifier = ?, revision_number = ?, lifecycle = ? "
                        + "where current_pointer_identifier = ?",
                revisionIdentity,
                material.revisionNumber(),
                material.lifecycle().name(),
                pointer.get("current_pointer_identifier", String.class)
        );
        if (updated != 1) {
            throw failure(
                    MerchantProfileFailureCategory.PROFILE_REVISION_CONFLICT,
                    "Merchant Contact Point current pointer changed"
            );
        }
    }

    private MerchantContactPointRevision toRevision(Record row) {
        MerchantScope merchant = new MerchantScope(
                row.get("merchant_identifier", String.class)
        );
        MerchantContactPointScopeKind scopeKind =
                MerchantContactPointScopeKind.valueOf(
                        row.get("scope_kind", String.class)
                );
        MerchantContactPointScope scope = scopeKind
                == MerchantContactPointScopeKind.MERCHANT
                ? MerchantContactPointScope.merchant(merchant)
                : MerchantContactPointScope.merchantLocation(
                        merchant,
                        row.get("merchant_location_identifier", String.class)
                );
        return new MerchantContactPointRevision(
                row.get("revision_identifier", String.class),
                scope,
                row.get("contact_point_identifier", String.class),
                row.get("revision_number", Long.class),
                Optional.ofNullable(row.get(
                        "predecessor_revision_identifier",
                        String.class
                )),
                MerchantContactPointLifecycle.valueOf(
                        row.get("lifecycle", String.class)
                ),
                MerchantContactPointKind.valueOf(
                        row.get("contact_kind", String.class)
                ),
                row.get("contact_value", String.class),
                MerchantContactPointExposure.valueOf(
                        row.get("exposure_choice", String.class)
                ),
                Optional.ofNullable(row.get("contact_label", String.class)),
                Optional.ofNullable(row.get(
                        "merchant_location_revision_identifier",
                        String.class
                )),
                row.get("logical_request_identifier", String.class),
                row.get("provenance_reference", String.class),
                row.get("acting_principal_identifier", String.class),
                row.get("controller_relationship_identifier", String.class),
                row.get("committed_at", Instant.class)
        );
    }

    private Optional<String> requireCurrentActiveLocation(
            MerchantContactPointScope scope
    ) {
        if (scope.kind() == MerchantContactPointScopeKind.MERCHANT) {
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
        return Optional.of(pointer.get("revision_identifier", String.class));
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

    private static MerchantContactPointRevision requireSameIntent(
            MutationIntent intent,
            MerchantContactPointRevision replay
    ) {
        OperationKind replayOperation = replay.revisionNumber() == 1
                ? OperationKind.CREATE
                : replay.lifecycle() == MerchantContactPointLifecycle.RETIRED
                ? OperationKind.RETIRE
                : OperationKind.UPDATE;
        boolean sameMaterial = intent.operation() == OperationKind.RETIRE
                || (intent.kind().equals(Optional.of(replay.kind()))
                && intent.value().equals(Optional.of(replay.value()))
                && intent.exposure().equals(Optional.of(replay.exposure()))
                && intent.label().equals(replay.label()));
        if (intent.operation() != replayOperation
                || !intent.scope().equals(replay.scope())
                || !intent.contactPointIdentity().equals(
                        replay.contactPointIdentity()
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
                    "Contact Point request identity is bound to different intent"
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
            MerchantContactPointScope scope,
            String contactPointIdentity,
            Optional<String> expectedRevision,
            Optional<MerchantContactPointKind> kind,
            Optional<String> value,
            Optional<MerchantContactPointExposure> exposure,
            Optional<String> label,
            String requestIdentity,
            String provenanceReference,
            String actorIdentity,
            Instant committedAt
    ) {
    }

    private record RevisionMaterial(
            long revisionNumber,
            Optional<String> predecessorRevisionIdentity,
            MerchantContactPointLifecycle lifecycle,
            MerchantContactPointKind kind,
            String value,
            MerchantContactPointExposure exposure,
            Optional<String> label,
            Optional<String> merchantLocationRevisionIdentity
    ) {
    }
}
