package grandrue.infrastructure.persistence.merchantprofile;

import grandrue.application.MerchantScope;
import mainstreet.merchantprofile.CreateMerchantClassificationEntryCommand;
import mainstreet.merchantprofile.MerchantClassificationEntryAuthority;
import mainstreet.merchantprofile.MerchantClassificationEntryRevision;
import mainstreet.merchantprofile.MerchantClassificationEntryV1;
import mainstreet.merchantprofile.MerchantClassificationExposure;
import grandrue.merchantprofile.MerchantClassificationLifecycle;
import mainstreet.merchantprofile.MerchantProfileFailureCategory;
import mainstreet.merchantprofile.MerchantProfileMutationException;
import mainstreet.merchantprofile.RetireMerchantClassificationEntryCommand;
import mainstreet.merchantprofile.UpdateMerchantClassificationEntryCommand;
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

/** PostgreSQL authority for immutable Merchant Classification Entry revisions. */
public final class JooqMerchantClassificationEntryAuthority
        implements MerchantClassificationEntryAuthority {
    private final DSLContext dsl;
    private final TransactionTemplate transactions;

    public JooqMerchantClassificationEntryAuthority(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactions = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
    }

    @Override
    public MerchantClassificationEntryRevision create(
            CreateMerchantClassificationEntryCommand command,
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
                command.classificationIdentity(),
                Optional.empty(),
                Optional.of(command.entry()),
                Optional.empty(),
                Optional.empty(),
                command.logicalRequestIdentity(),
                command.provenanceReference(),
                command.actingPrincipalIdentity(),
                command.committedAt()
        ));
    }

    @Override
    public MerchantClassificationEntryRevision update(
            UpdateMerchantClassificationEntryCommand command,
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
                command.classificationIdentity(),
                Optional.of(command.expectedCurrentRevisionIdentity()),
                Optional.empty(),
                Optional.of(command.merchantApprovedLabel()),
                Optional.of(command.exposure()),
                command.logicalRequestIdentity(),
                command.provenanceReference(),
                command.actingPrincipalIdentity(),
                command.committedAt()
        ));
    }

    @Override
    public MerchantClassificationEntryRevision retire(
            RetireMerchantClassificationEntryCommand command,
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
                command.classificationIdentity(),
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
    public Optional<MerchantClassificationEntryRevision> current(
            MerchantScope merchantScope,
            String classificationIdentity
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(classificationIdentity, "classificationIdentity");
        Record pointer = dsl.fetchOne(
                "select revision_identifier "
                        + "from current_merchant_classification_entry "
                        + "where merchant_identifier = ? "
                        + "and classification_identifier = ?",
                merchantScope.merchantIdentifier(),
                classificationIdentity
        );
        return pointer == null
                ? Optional.empty()
                : revision(pointer.get("revision_identifier", String.class));
    }

    @Override
    public Optional<MerchantClassificationEntryRevision> revision(
            String revisionIdentity
    ) {
        requireIdentifier(revisionIdentity, "revisionIdentity");
        Record row = dsl.fetchOne(
                "select * from merchant_classification_entry_revision "
                        + "where revision_identifier = ?",
                revisionIdentity
        );
        return Optional.ofNullable(row).map(this::toRevision);
    }

    private MerchantClassificationEntryRevision mutate(MutationIntent intent) {
        MerchantClassificationEntryRevision result = transactions.execute(
                status -> {
                    lock("merchant-classification-request|"
                            + intent.requestIdentity(), 537);
                    Optional<MerchantClassificationEntryRevision> replay =
                            byRequest(intent.requestIdentity());
                    if (replay.isPresent()) {
                        return requireSameIntent(intent, replay.orElseThrow());
                    }

                    String merchant = intent.merchantScope()
                            .merchantIdentifier();
                    lock(merchant, 76);
                    requireOpenUnsuspendedAccount(intent.merchantScope());
                    String controllerRelationship = requireCurrentController(
                            intent.merchantScope(),
                            intent.actorIdentity()
                    );
                    lock(merchant + "|" + intent.classificationIdentity(), 541);

                    Record pointer = currentPointerForUpdate(intent);
                    Optional<MerchantClassificationEntryRevision> current =
                            pointer == null
                                    ? Optional.empty()
                                    : revision(pointer.get(
                                            "revision_identifier",
                                            String.class
                                    ));
                    RevisionMaterial material = resolveMaterial(intent, current);
                    String revisionIdentity =
                            "merchant-classification-entry-revision-"
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
                                "Classification Entry revision could not commit",
                                failure
                        );
                    }
                    return revision(revisionIdentity).orElseThrow(() ->
                            new IllegalStateException(
                                    "Committed Classification Entry revision is missing"
                            )
                    );
                }
        );
        if (result == null) {
            throw new IllegalStateException(
                    "Classification Entry mutation returned no revision"
            );
        }
        return result;
    }

    private RevisionMaterial resolveMaterial(
            MutationIntent intent,
            Optional<MerchantClassificationEntryRevision> current
    ) {
        if (intent.operation() == OperationKind.CREATE) {
            if (current.isPresent()) {
                throw failure(
                        MerchantProfileFailureCategory.PROFILE_REVISION_CONFLICT,
                        "Classification Entry identity is already established"
                );
            }
            return new RevisionMaterial(
                    1L,
                    Optional.empty(),
                    MerchantClassificationLifecycle.ACTIVE,
                    intent.entry().orElseThrow()
            );
        }

        MerchantClassificationEntryRevision authoritative =
                current.orElseThrow(() -> failure(
                        MerchantProfileFailureCategory.PROFILE_FACT_NOT_FOUND,
                        "Classification Entry identity is not established"
                ));
        if (authoritative.lifecycle()
                == MerchantClassificationLifecycle.RETIRED) {
            throw failure(
                    MerchantProfileFailureCategory.PROFILE_FACT_RETIRED,
                    "Classification Entry identity is retired"
            );
        }
        if (!intent.expectedRevision().orElseThrow().equals(
                authoritative.revisionIdentity()
        )) {
            throw failure(
                    MerchantProfileFailureCategory.PROFILE_REVISION_CONFLICT,
                    "Classification Entry current revision changed"
            );
        }
        long next = Math.addExact(authoritative.revisionNumber(), 1L);
        if (intent.operation() == OperationKind.RETIRE) {
            return new RevisionMaterial(
                    next,
                    Optional.of(authoritative.revisionIdentity()),
                    MerchantClassificationLifecycle.RETIRED,
                    authoritative.entry()
            );
        }
        return new RevisionMaterial(
                next,
                Optional.of(authoritative.revisionIdentity()),
                MerchantClassificationLifecycle.ACTIVE,
                new MerchantClassificationEntryV1(
                        authoritative.entry().kind(),
                        intent.label().orElseThrow(),
                        intent.exposure().orElseThrow()
                )
        );
    }

    private Optional<MerchantClassificationEntryRevision> byRequest(
            String requestIdentity
    ) {
        Record row = dsl.fetchOne(
                "select * from merchant_classification_entry_revision "
                        + "where logical_request_identifier = ?",
                requestIdentity
        );
        return Optional.ofNullable(row).map(this::toRevision);
    }

    private Record currentPointerForUpdate(MutationIntent intent) {
        return dsl.fetchOne(
                "select * from current_merchant_classification_entry "
                        + "where merchant_identifier = ? "
                        + "and classification_identifier = ? for update",
                intent.merchantScope().merchantIdentifier(),
                intent.classificationIdentity()
        );
    }

    private void insertRevision(
            String revisionIdentity,
            MutationIntent intent,
            RevisionMaterial material,
            String controllerRelationship
    ) {
        dsl.execute(
                "insert into merchant_classification_entry_revision "
                        + "(revision_identifier, merchant_identifier, "
                        + "classification_identifier, revision_number, "
                        + "predecessor_revision_identifier, operation_kind, "
                        + "lifecycle, value_schema_identifier, "
                        + "classification_kind, merchant_approved_label, "
                        + "exposure_choice, logical_request_identifier, "
                        + "provenance_reference, acting_principal_identifier, "
                        + "controller_relationship_identifier, committed_at) "
                        + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                        + "?, cast(? as timestamptz))",
                revisionIdentity,
                intent.merchantScope().merchantIdentifier(),
                intent.classificationIdentity(),
                material.revisionNumber(),
                material.predecessorRevisionIdentity().orElse(null),
                intent.operation().name(),
                material.lifecycle().name(),
                material.entry().schemaIdentity(),
                material.entry().kind().name(),
                material.entry().merchantApprovedLabel(),
                material.entry().exposure().name(),
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
                    "insert into current_merchant_classification_entry "
                            + "(current_pointer_identifier, merchant_identifier, "
                            + "classification_identifier, revision_identifier, "
                            + "revision_number, lifecycle) "
                            + "values (?, ?, ?, ?, ?, ?)",
                    "merchant-classification-entry-current-" + UUID.randomUUID(),
                    intent.merchantScope().merchantIdentifier(),
                    intent.classificationIdentity(),
                    revisionIdentity,
                    material.revisionNumber(),
                    material.lifecycle().name()
            );
            return;
        }
        int updated = dsl.execute(
                "update current_merchant_classification_entry "
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
                    "Classification Entry current pointer changed"
            );
        }
    }

    private MerchantClassificationEntryRevision toRevision(Record row) {
        String schema = row.get("value_schema_identifier", String.class);
        if (!MerchantClassificationEntryV1.SCHEMA_IDENTITY.equals(schema)) {
            throw new IllegalStateException(
                    "Unsupported Classification Entry schema: " + schema
            );
        }
        return new MerchantClassificationEntryRevision(
                row.get("revision_identifier", String.class),
                new MerchantScope(row.get("merchant_identifier", String.class)),
                row.get("classification_identifier", String.class),
                row.get("revision_number", Long.class),
                Optional.ofNullable(row.get(
                        "predecessor_revision_identifier",
                        String.class
                )),
                MerchantClassificationLifecycle.valueOf(
                        row.get("lifecycle", String.class)
                ),
                new MerchantClassificationEntryV1(
                        grandrue.merchantprofile.MerchantClassificationKind
                                .valueOf(row.get(
                                        "classification_kind",
                                        String.class
                                )),
                        row.get("merchant_approved_label", String.class),
                        MerchantClassificationExposure.valueOf(
                                row.get("exposure_choice", String.class)
                        )
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
                    MerchantProfileFailureCategory
                            .MERCHANT_ACCOUNT_OPERATION_RESTRICTED,
                    "Merchant Account does not permit classification mutation"
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

    private static MerchantClassificationEntryRevision requireSameIntent(
            MutationIntent intent,
            MerchantClassificationEntryRevision replay
    ) {
        OperationKind replayOperation = replay.revisionNumber() == 1
                ? OperationKind.CREATE
                : replay.lifecycle() == MerchantClassificationLifecycle.RETIRED
                ? OperationKind.RETIRE
                : OperationKind.UPDATE;
        boolean sameMaterial = switch (intent.operation()) {
            case CREATE -> intent.entry().equals(Optional.of(replay.entry()));
            case UPDATE -> intent.label().equals(Optional.of(
                    replay.entry().merchantApprovedLabel()
            )) && intent.exposure().equals(Optional.of(
                    replay.entry().exposure()
            ));
            case RETIRE -> true;
        };
        if (intent.operation() != replayOperation
                || !intent.merchantScope().equals(replay.merchantScope())
                || !intent.classificationIdentity().equals(
                        replay.classificationIdentity()
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
                    "Classification Entry request identity has different intent"
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
            String classificationIdentity,
            Optional<String> expectedRevision,
            Optional<MerchantClassificationEntryV1> entry,
            Optional<String> label,
            Optional<MerchantClassificationExposure> exposure,
            String requestIdentity,
            String provenanceReference,
            String actorIdentity,
            Instant committedAt
    ) {
    }

    private record RevisionMaterial(
            long revisionNumber,
            Optional<String> predecessorRevisionIdentity,
            MerchantClassificationLifecycle lifecycle,
            MerchantClassificationEntryV1 entry
    ) {
    }
}
