package grandrue.infrastructure.persistence.merchantprofile;

import grandrue.application.MerchantScope;
import mainstreet.merchantprofile.CreateMerchantExternalPresenceLinkCommand;
import mainstreet.merchantprofile.MerchantExternalPresenceExposure;
import grandrue.merchantprofile.MerchantExternalPresenceLifecycle;
import grandrue.merchantprofile.MerchantExternalPresenceLinkAuthority;
import mainstreet.merchantprofile.MerchantExternalPresenceLinkRevision;
import mainstreet.merchantprofile.MerchantProfileFailureCategory;
import mainstreet.merchantprofile.MerchantProfileMutationException;
import mainstreet.merchantprofile.RetireMerchantExternalPresenceLinkCommand;
import mainstreet.merchantprofile.UpdateMerchantExternalPresenceLinkCommand;
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

/** PostgreSQL authority for immutable Merchant External Presence revisions. */
public final class JooqMerchantExternalPresenceLinkAuthority
        implements MerchantExternalPresenceLinkAuthority {
    private final DSLContext dsl;
    private final TransactionTemplate transactions;

    public JooqMerchantExternalPresenceLinkAuthority(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactions = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
    }

    @Override
    public MerchantExternalPresenceLinkRevision create(
            CreateMerchantExternalPresenceLinkCommand command,
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
                command.presenceIdentity(),
                Optional.empty(),
                Optional.of(command.platformKind()),
                Optional.of(command.publicUrl()),
                Optional.of(command.exposure()),
                command.logicalRequestIdentity(),
                command.provenanceReference(),
                command.actingPrincipalIdentity(),
                command.committedAt()
        ));
    }

    @Override
    public MerchantExternalPresenceLinkRevision update(
            UpdateMerchantExternalPresenceLinkCommand command,
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
                command.presenceIdentity(),
                Optional.of(command.expectedCurrentRevisionIdentity()),
                Optional.of(command.platformKind()),
                Optional.of(command.publicUrl()),
                Optional.of(command.exposure()),
                command.logicalRequestIdentity(),
                command.provenanceReference(),
                command.actingPrincipalIdentity(),
                command.committedAt()
        ));
    }

    @Override
    public MerchantExternalPresenceLinkRevision retire(
            RetireMerchantExternalPresenceLinkCommand command,
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
                command.presenceIdentity(),
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
    public Optional<MerchantExternalPresenceLinkRevision> current(
            MerchantScope merchantScope,
            String presenceIdentity
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(presenceIdentity, "presenceIdentity");
        Record pointer = dsl.fetchOne(
                "select revision_identifier "
                        + "from current_merchant_external_presence_link "
                        + "where merchant_identifier = ? "
                        + "and presence_identifier = ?",
                merchantScope.merchantIdentifier(),
                presenceIdentity
        );
        return pointer == null
                ? Optional.empty()
                : revision(pointer.get("revision_identifier", String.class));
    }

    @Override
    public Optional<MerchantExternalPresenceLinkRevision> revision(
            String revisionIdentity
    ) {
        requireIdentifier(revisionIdentity, "revisionIdentity");
        Record row = dsl.fetchOne(
                "select * from merchant_external_presence_link_revision "
                        + "where revision_identifier = ?",
                revisionIdentity
        );
        return Optional.ofNullable(row).map(this::toRevision);
    }

    private MerchantExternalPresenceLinkRevision mutate(MutationIntent intent) {
        MerchantExternalPresenceLinkRevision result = transactions.execute(
                status -> {
                    lock("merchant-presence-request|" + intent.requestIdentity(),
                            525);
                    Optional<MerchantExternalPresenceLinkRevision> replay =
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
                    lock(merchant + "|" + intent.presenceIdentity(), 521);

                    Record pointer = currentPointerForUpdate(intent);
                    Optional<MerchantExternalPresenceLinkRevision> current =
                            pointer == null
                                    ? Optional.empty()
                                    : revision(pointer.get(
                                            "revision_identifier",
                                            String.class
                                    ));
                    RevisionMaterial material = resolveMaterial(intent, current);
                    String revisionIdentity =
                            "merchant-external-presence-revision-"
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
                                "External Presence revision could not commit",
                                failure
                        );
                    }
                    return revision(revisionIdentity).orElseThrow(() ->
                            new IllegalStateException(
                                    "Committed External Presence revision is missing"
                            )
                    );
                }
        );
        if (result == null) {
            throw new IllegalStateException(
                    "External Presence mutation returned no revision"
            );
        }
        return result;
    }

    private RevisionMaterial resolveMaterial(
            MutationIntent intent,
            Optional<MerchantExternalPresenceLinkRevision> current
    ) {
        if (intent.operation() == OperationKind.CREATE) {
            if (current.isPresent()) {
                throw failure(
                        MerchantProfileFailureCategory.PROFILE_REVISION_CONFLICT,
                        "External Presence identity is already established"
                );
            }
            return new RevisionMaterial(
                    1L,
                    Optional.empty(),
                    MerchantExternalPresenceLifecycle.ACTIVE,
                    intent.platformKind().orElseThrow(),
                    intent.publicUrl().orElseThrow(),
                    intent.exposure().orElseThrow()
            );
        }

        MerchantExternalPresenceLinkRevision authoritative =
                current.orElseThrow(() -> failure(
                        MerchantProfileFailureCategory.PROFILE_FACT_NOT_FOUND,
                        "External Presence identity is not established"
                ));
        if (authoritative.lifecycle()
                == MerchantExternalPresenceLifecycle.RETIRED) {
            throw failure(
                    MerchantProfileFailureCategory.PROFILE_FACT_RETIRED,
                    "External Presence identity is retired"
            );
        }
        if (!intent.expectedRevision().orElseThrow().equals(
                authoritative.revisionIdentity()
        )) {
            throw failure(
                    MerchantProfileFailureCategory.PROFILE_REVISION_CONFLICT,
                    "External Presence current revision changed"
            );
        }
        long next = Math.addExact(authoritative.revisionNumber(), 1L);
        if (intent.operation() == OperationKind.RETIRE) {
            return new RevisionMaterial(
                    next,
                    Optional.of(authoritative.revisionIdentity()),
                    MerchantExternalPresenceLifecycle.RETIRED,
                    authoritative.platformKind(),
                    authoritative.publicUrl(),
                    authoritative.exposure()
            );
        }
        return new RevisionMaterial(
                next,
                Optional.of(authoritative.revisionIdentity()),
                MerchantExternalPresenceLifecycle.ACTIVE,
                intent.platformKind().orElseThrow(),
                intent.publicUrl().orElseThrow(),
                intent.exposure().orElseThrow()
        );
    }

    private Optional<MerchantExternalPresenceLinkRevision> byRequest(
            String requestIdentity
    ) {
        Record row = dsl.fetchOne(
                "select * from merchant_external_presence_link_revision "
                        + "where logical_request_identifier = ?",
                requestIdentity
        );
        return Optional.ofNullable(row).map(this::toRevision);
    }

    private Record currentPointerForUpdate(MutationIntent intent) {
        return dsl.fetchOne(
                "select * from current_merchant_external_presence_link "
                        + "where merchant_identifier = ? "
                        + "and presence_identifier = ? for update",
                intent.merchantScope().merchantIdentifier(),
                intent.presenceIdentity()
        );
    }

    private void insertRevision(
            String revisionIdentity,
            MutationIntent intent,
            RevisionMaterial material,
            String controllerRelationship
    ) {
        dsl.execute(
                "insert into merchant_external_presence_link_revision "
                        + "(revision_identifier, merchant_identifier, "
                        + "presence_identifier, revision_number, "
                        + "predecessor_revision_identifier, operation_kind, "
                        + "lifecycle, platform_kind, public_url, "
                        + "exposure_choice, logical_request_identifier, "
                        + "provenance_reference, acting_principal_identifier, "
                        + "controller_relationship_identifier, committed_at) "
                        + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                        + "cast(? as timestamptz))",
                revisionIdentity,
                intent.merchantScope().merchantIdentifier(),
                intent.presenceIdentity(),
                material.revisionNumber(),
                material.predecessorRevisionIdentity().orElse(null),
                intent.operation().name(),
                material.lifecycle().name(),
                material.platformKind(),
                material.publicUrl(),
                material.exposure().name(),
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
                    "insert into current_merchant_external_presence_link "
                            + "(current_pointer_identifier, merchant_identifier, "
                            + "presence_identifier, revision_identifier, "
                            + "revision_number, lifecycle) "
                            + "values (?, ?, ?, ?, ?, ?)",
                    "merchant-external-presence-current-" + UUID.randomUUID(),
                    intent.merchantScope().merchantIdentifier(),
                    intent.presenceIdentity(),
                    revisionIdentity,
                    material.revisionNumber(),
                    material.lifecycle().name()
            );
            return;
        }
        int updated = dsl.execute(
                "update current_merchant_external_presence_link "
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
                    "External Presence current pointer changed"
            );
        }
    }

    private MerchantExternalPresenceLinkRevision toRevision(Record row) {
        return new MerchantExternalPresenceLinkRevision(
                row.get("revision_identifier", String.class),
                new MerchantScope(row.get("merchant_identifier", String.class)),
                row.get("presence_identifier", String.class),
                row.get("revision_number", Long.class),
                Optional.ofNullable(row.get(
                        "predecessor_revision_identifier",
                        String.class
                )),
                MerchantExternalPresenceLifecycle.valueOf(
                        row.get("lifecycle", String.class)
                ),
                row.get("platform_kind", String.class),
                row.get("public_url", String.class),
                MerchantExternalPresenceExposure.valueOf(
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

    private static MerchantExternalPresenceLinkRevision requireSameIntent(
            MutationIntent intent,
            MerchantExternalPresenceLinkRevision replay
    ) {
        OperationKind replayOperation = replay.revisionNumber() == 1
                ? OperationKind.CREATE
                : replay.lifecycle() == MerchantExternalPresenceLifecycle.RETIRED
                ? OperationKind.RETIRE
                : OperationKind.UPDATE;
        boolean sameMaterial = intent.operation() == OperationKind.RETIRE
                || (intent.platformKind().equals(Optional.of(
                        replay.platformKind()
                ))
                && intent.publicUrl().equals(Optional.of(replay.publicUrl()))
                && intent.exposure().equals(Optional.of(replay.exposure())));
        if (intent.operation() != replayOperation
                || !intent.merchantScope().equals(replay.merchantScope())
                || !intent.presenceIdentity().equals(replay.presenceIdentity())
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
                    "External Presence request identity is bound to different intent"
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
            String presenceIdentity,
            Optional<String> expectedRevision,
            Optional<String> platformKind,
            Optional<String> publicUrl,
            Optional<MerchantExternalPresenceExposure> exposure,
            String requestIdentity,
            String provenanceReference,
            String actorIdentity,
            Instant committedAt
    ) {
    }

    private record RevisionMaterial(
            long revisionNumber,
            Optional<String> predecessorRevisionIdentity,
            MerchantExternalPresenceLifecycle lifecycle,
            String platformKind,
            String publicUrl,
            MerchantExternalPresenceExposure exposure
    ) {
    }
}
