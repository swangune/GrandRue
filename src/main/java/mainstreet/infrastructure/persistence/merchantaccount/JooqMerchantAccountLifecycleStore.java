package mainstreet.infrastructure.persistence.merchantaccount;

import mainstreet.application.MerchantScope;
import grandrue.merchantaccount.BeginMerchantAccountClosureCommand;
import grandrue.merchantaccount.FinalizeMerchantAccountClosureCommand;
import grandrue.merchantaccount.MerchantAccountLifecycle;
import grandrue.merchantaccount.MerchantAccountLifecycleConflictException;
import grandrue.merchantaccount.MerchantAccountLifecycleStore;
import grandrue.merchantaccount.MerchantAccountSuspendedException;
import grandrue.merchantaccount.MerchantAccountSuspension;
import grandrue.merchantaccount.MerchantAccountSuspensionCommand;
import grandrue.merchantaccount.MerchantAccountSuspensionReleaseCommand;
import grandrue.merchantaccount.MerchantControllerRelationship;
import grandrue.merchantaccount.MerchantControllerRelationshipLifecycle;
import grandrue.merchantaccount.MerchantControllerTransferCommand;
import grandrue.merchantaccount.MerchantControllerTransferConflictException;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * PostgreSQL/jOOQ consistency adapter for MS-PROT-076 Merchant Account-owned
 * lifecycle, Controller relationship, and merchant-wide suspension facts.
 *
 * <p>Security/authentication, recipient acceptance, closure-readiness, Audit,
 * Commercial, Provider and capability commitment truth remain external
 * authorities. Their evidence identifiers are preserved here only as required
 * transition provenance.</p>
 */
public final class JooqMerchantAccountLifecycleStore
        implements MerchantAccountLifecycleStore {

    private static final Table<?> ACCOUNT =
            DSL.table(DSL.name("merchant_account"));
    private static final Table<?> CONTROLLER =
            DSL.table(DSL.name("merchant_controller_relationship"));
    private static final Table<?> TRANSFER_REQUEST =
            DSL.table(DSL.name("merchant_controller_transfer_request"));
    private static final Table<?> SUSPENSION =
            DSL.table(DSL.name("merchant_account_suspension"));
    private static final Table<?> SUSPENSION_REQUEST =
            DSL.table(DSL.name("merchant_account_suspension_request"));
    private static final Table<?> SUSPENSION_RELEASE_REQUEST =
            DSL.table(DSL.name("merchant_account_suspension_release_request"));
    private static final Table<?> CLOSURE_BEGIN_REQUEST =
            DSL.table(DSL.name("merchant_account_closure_begin_request"));
    private static final Table<?> CLOSURE_FINALIZE_REQUEST =
            DSL.table(DSL.name("merchant_account_closure_finalize_request"));

    private static final Field<String> MERCHANT_ID =
            DSL.field(DSL.name("merchant_identifier"), String.class);
    private static final Field<String> ACCOUNT_LIFECYCLE =
            DSL.field(DSL.name("lifecycle"), String.class);

    private static final Field<String> CONTROLLER_REL_ID =
            DSL.field(DSL.name("controller_relationship_identifier"), String.class);
    private static final Field<String> IDENTITY_ID =
            DSL.field(DSL.name("identity_identifier"), String.class);
    private static final Field<String> RELATIONSHIP_LIFECYCLE =
            DSL.field(DSL.name("lifecycle"), String.class);

    private static final Field<String> LOGICAL_REQUEST_ID =
            DSL.field(DSL.name("logical_request_identity"), String.class);
    private static final Field<String> EXPECTED_CONTROLLER_REL_ID =
            DSL.field(
                    DSL.name("expected_controller_relationship_identifier"),
                    String.class
            );
    private static final Field<String> INITIATING_CONTROLLER_ID =
            DSL.field(DSL.name("initiating_controller_identity"), String.class);
    private static final Field<String> RECEIVING_ID =
            DSL.field(DSL.name("receiving_identity"), String.class);
    private static final Field<String> REPLACEMENT_CONTROLLER_REL_ID =
            DSL.field(
                    DSL.name("replacement_controller_relationship_identifier"),
                    String.class
            );
    private static final Field<String> AUTH_ASSURANCE_EVIDENCE_ID =
            DSL.field(
                    DSL.name("authentication_assurance_evidence_identity"),
                    String.class
            );
    private static final Field<String> RECIPIENT_ACCEPTANCE_EVIDENCE_ID =
            DSL.field(
                    DSL.name("recipient_acceptance_evidence_identity"),
                    String.class
            );
    private static final Field<Instant> TRANSFERRED_AT =
            DSL.field(DSL.name("transferred_at"), Instant.class);

    private static final Field<String> SUSPENSION_ID =
            DSL.field(DSL.name("suspension_identity"), String.class);
    private static final Field<String> SOURCE_AUTHORITY_ID =
            DSL.field(DSL.name("source_authority_identifier"), String.class);
    private static final Field<String> REASON_CLASS_ID =
            DSL.field(DSL.name("reason_class_identifier"), String.class);
    private static final Field<Instant> ESTABLISHED_AT =
            DSL.field(DSL.name("established_at"), Instant.class);
    private static final Field<String> ESTABLISHED_BY_ID =
            DSL.field(DSL.name("established_by_identifier"), String.class);
    private static final Field<String> RELEASE_AUTHORITY_ID =
            DSL.field(DSL.name("release_authority_identifier"), String.class);
    private static final Field<String> PROVENANCE_ID =
            DSL.field(DSL.name("provenance_identifier"), String.class);
    private static final Field<Instant> RELEASED_AT =
            DSL.field(DSL.name("released_at"), Instant.class);
    private static final Field<String> RELEASED_BY_ID =
            DSL.field(DSL.name("released_by_identifier"), String.class);
    private static final Field<String> RELEASE_EVIDENCE_ID =
            DSL.field(DSL.name("release_evidence_identifier"), String.class);
    private static final Field<String> RELEASING_AUTHORITY_ID =
            DSL.field(DSL.name("releasing_authority_identifier"), String.class);

    private static final Field<Instant> REQUESTED_AT =
            DSL.field(DSL.name("requested_at"), Instant.class);
    private static final Field<String> CLOSURE_READINESS_EVIDENCE_ID =
            DSL.field(
                    DSL.name("closure_readiness_evidence_identity"),
                    String.class
            );
    private static final Field<String> ACTING_AUTHORITY_ID =
            DSL.field(DSL.name("acting_authority_identifier"), String.class);
    private static final Field<Instant> CLOSED_AT =
            DSL.field(DSL.name("closed_at"), Instant.class);

    private final DSLContext dsl;
    private final TransactionTemplate transactions;

    public JooqMerchantAccountLifecycleStore(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactions = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
    }

    @Override
    public MerchantControllerRelationship transferController(
            MerchantControllerTransferCommand command
    ) {
        Objects.requireNonNull(command, "command");
        MerchantControllerRelationship result = transactions.execute(status -> {
            lockRequest("controller-transfer", command.logicalRequestIdentity(), 7601);
            Record replay = transferRequest(command.logicalRequestIdentity());
            if (replay != null) {
                requireSameTransferIntent(replay, command);
                return requireControllerRelationship(
                        command.merchantScope(),
                        replay.get(REPLACEMENT_CONTROLLER_REL_ID)
                );
            }

            lockMerchant(command.merchantScope());
            MerchantAccountLifecycle accountLifecycle = lifecycleForUpdate(
                    command.merchantScope()
            );
            if (accountLifecycle == MerchantAccountLifecycle.CLOSED) {
                throw new MerchantControllerTransferConflictException(
                        "Cannot transfer Controller of CLOSED Merchant Account"
                );
            }
            requireNotSuspended(command.merchantScope());

            MerchantControllerRelationship current = requireActiveControllerForUpdate(
                    command.merchantScope()
            );
            if (!current.relationshipIdentifier().equals(
                    command.expectedCurrentControllerRelationshipIdentity()
            ) || !current.identityIdentifier().equals(
                    command.initiatingControllerIdentity()
            )) {
                throw new MerchantControllerTransferConflictException(
                        "Controller transfer is based on stale current authority"
                );
            }
            if (controllerRelationshipExists(
                    command.replacementControllerRelationshipIdentity()
            )) {
                throw new IllegalArgumentException(
                        "Replacement Controller relationship identity already exists"
                );
            }

            int ended = dsl.update(CONTROLLER)
                    .set(
                            RELATIONSHIP_LIFECYCLE,
                            MerchantControllerRelationshipLifecycle.ENDED.name()
                    )
                    .where(MERCHANT_ID.eq(command.merchantScope().merchantIdentifier()))
                    .and(CONTROLLER_REL_ID.eq(current.relationshipIdentifier()))
                    .and(RELATIONSHIP_LIFECYCLE.eq(
                            MerchantControllerRelationshipLifecycle.ACTIVE.name()
                    ))
                    .execute();
            if (ended != 1) {
                throw new MerchantControllerTransferConflictException(
                        "Current Controller changed before transfer commit"
                );
            }

            dsl.insertInto(CONTROLLER)
                    .columns(
                            CONTROLLER_REL_ID,
                            MERCHANT_ID,
                            IDENTITY_ID,
                            RELATIONSHIP_LIFECYCLE
                    )
                    .values(
                            command.replacementControllerRelationshipIdentity(),
                            command.merchantScope().merchantIdentifier(),
                            command.receivingIdentity(),
                            MerchantControllerRelationshipLifecycle.ACTIVE.name()
                    )
                    .execute();

            dsl.insertInto(TRANSFER_REQUEST)
                    .columns(
                            LOGICAL_REQUEST_ID,
                            MERCHANT_ID,
                            EXPECTED_CONTROLLER_REL_ID,
                            INITIATING_CONTROLLER_ID,
                            RECEIVING_ID,
                            REPLACEMENT_CONTROLLER_REL_ID,
                            AUTH_ASSURANCE_EVIDENCE_ID,
                            RECIPIENT_ACCEPTANCE_EVIDENCE_ID,
                            TRANSFERRED_AT
                    )
                    .values(
                            command.logicalRequestIdentity(),
                            command.merchantScope().merchantIdentifier(),
                            command.expectedCurrentControllerRelationshipIdentity(),
                            command.initiatingControllerIdentity(),
                            command.receivingIdentity(),
                            command.replacementControllerRelationshipIdentity(),
                            command.authenticationAssuranceEvidenceIdentity(),
                            command.recipientAcceptanceEvidenceIdentity(),
                            command.transferredAt()
                    )
                    .execute();

            return new MerchantControllerRelationship(
                    command.replacementControllerRelationshipIdentity(),
                    command.merchantScope(),
                    command.receivingIdentity(),
                    MerchantControllerRelationshipLifecycle.ACTIVE
            );
        });
        return Objects.requireNonNull(result, "Controller transfer returned no result");
    }

    @Override
    public MerchantAccountSuspension establishSuspension(
            MerchantAccountSuspensionCommand command
    ) {
        Objects.requireNonNull(command, "command");
        MerchantAccountSuspension result = transactions.execute(status -> {
            lockRequest("suspension-establish", command.logicalRequestIdentity(), 7602);
            Record replay = suspensionEstablishmentRequest(
                    command.logicalRequestIdentity()
            );
            if (replay != null) {
                MerchantAccountSuspension existing = requireSuspension(
                        replay.get(SUSPENSION_ID)
                );
                requireSameSuspensionIntent(existing, command);
                return existing;
            }

            lockMerchant(command.merchantScope());
            if (lifecycleForUpdate(command.merchantScope())
                    == MerchantAccountLifecycle.CLOSED) {
                throw new MerchantAccountLifecycleConflictException(
                        command.merchantScope().merchantIdentifier(),
                        MerchantAccountLifecycle.CLOSED,
                        MerchantAccountLifecycle.OPEN
                );
            }
            if (suspensionExists(command.suspensionIdentity())) {
                throw new IllegalArgumentException(
                        "Suspension identity already exists: "
                                + command.suspensionIdentity()
                );
            }

            MerchantAccountSuspension suspension = new MerchantAccountSuspension(
                    command.suspensionIdentity(),
                    command.merchantScope(),
                    command.sourceAuthorityIdentifier(),
                    command.reasonClassIdentifier(),
                    command.establishedAt(),
                    command.establishedByIdentifier(),
                    command.releaseAuthorityIdentifier(),
                    command.provenanceIdentifier(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty()
            );
            insertSuspension(suspension);
            dsl.insertInto(SUSPENSION_REQUEST)
                    .columns(LOGICAL_REQUEST_ID, SUSPENSION_ID, MERCHANT_ID)
                    .values(
                            command.logicalRequestIdentity(),
                            command.suspensionIdentity(),
                            command.merchantScope().merchantIdentifier()
                    )
                    .execute();
            return suspension;
        });
        return Objects.requireNonNull(result, "Suspension establishment returned no result");
    }

    @Override
    public MerchantAccountSuspension releaseSuspension(
            MerchantAccountSuspensionReleaseCommand command
    ) {
        Objects.requireNonNull(command, "command");
        MerchantAccountSuspension result = transactions.execute(status -> {
            lockRequest("suspension-release", command.logicalRequestIdentity(), 7603);
            Record replay = suspensionReleaseRequest(command.logicalRequestIdentity());
            if (replay != null) {
                requireSameSuspensionReleaseIntent(replay, command);
                return requireSuspension(replay.get(SUSPENSION_ID));
            }

            lockMerchant(command.merchantScope());
            MerchantAccountSuspension current = requireSuspensionForUpdate(
                    command.suspensionIdentity()
            );
            if (!current.merchantScope().equals(command.merchantScope())) {
                throw new IllegalArgumentException(
                        "Suspension belongs to another Merchant Scope"
                );
            }
            if (!current.releaseAuthorityIdentifier().equals(
                    command.releasingAuthorityIdentifier()
            )) {
                throw new SecurityException(
                        "Authority is not permitted to release this suspension"
                );
            }
            if (!current.isEffective()) {
                throw new IllegalStateException("Suspension is already released");
            }

            MerchantAccountSuspension released = current.release(
                    command.releasedAt(),
                    command.releasedByIdentifier(),
                    command.releaseEvidenceIdentifier()
            );
            int changed = dsl.update(SUSPENSION)
                    .set(RELEASED_AT, command.releasedAt())
                    .set(RELEASED_BY_ID, command.releasedByIdentifier())
                    .set(RELEASE_EVIDENCE_ID, command.releaseEvidenceIdentifier())
                    .where(SUSPENSION_ID.eq(command.suspensionIdentity()))
                    .and(RELEASED_AT.isNull())
                    .execute();
            if (changed != 1) {
                throw new IllegalStateException(
                        "Suspension release lost its current-state predicate"
                );
            }
            dsl.insertInto(SUSPENSION_RELEASE_REQUEST)
                    .columns(
                            LOGICAL_REQUEST_ID,
                            SUSPENSION_ID,
                            MERCHANT_ID,
                            RELEASING_AUTHORITY_ID,
                            RELEASED_AT,
                            RELEASED_BY_ID,
                            RELEASE_EVIDENCE_ID
                    )
                    .values(
                            command.logicalRequestIdentity(),
                            command.suspensionIdentity(),
                            command.merchantScope().merchantIdentifier(),
                            command.releasingAuthorityIdentifier(),
                            command.releasedAt(),
                            command.releasedByIdentifier(),
                            command.releaseEvidenceIdentifier()
                    )
                    .execute();
            return released;
        });
        return Objects.requireNonNull(result, "Suspension release returned no result");
    }

    @Override
    public List<MerchantAccountSuspension> effectiveSuspensions(
            MerchantScope merchantScope
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        var records = dsl.select(
                        SUSPENSION_ID,
                        MERCHANT_ID,
                        SOURCE_AUTHORITY_ID,
                        REASON_CLASS_ID,
                        ESTABLISHED_AT,
                        ESTABLISHED_BY_ID,
                        RELEASE_AUTHORITY_ID,
                        PROVENANCE_ID,
                        RELEASED_AT,
                        RELEASED_BY_ID,
                        RELEASE_EVIDENCE_ID
                )
                .from(SUSPENSION)
                .where(MERCHANT_ID.eq(merchantScope.merchantIdentifier()))
                .and(RELEASED_AT.isNull())
                .orderBy(ESTABLISHED_AT, SUSPENSION_ID)
                .fetch();
        List<MerchantAccountSuspension> suspensions = new ArrayList<>();
        records.forEach(record -> suspensions.add(toSuspension(record)));
        return List.copyOf(suspensions);
    }

    @Override
    public MerchantAccountLifecycle beginClosure(
            BeginMerchantAccountClosureCommand command
    ) {
        Objects.requireNonNull(command, "command");
        MerchantAccountLifecycle result = transactions.execute(status -> {
            lockRequest("closure-begin", command.logicalRequestIdentity(), 7604);
            Record replay = closureBeginRequest(command.logicalRequestIdentity());
            if (replay != null) {
                requireSameClosureBeginIntent(replay, command);
                return MerchantAccountLifecycle.CLOSING;
            }

            lockMerchant(command.merchantScope());
            MerchantAccountLifecycle currentLifecycle = lifecycleForUpdate(
                    command.merchantScope()
            );
            if (currentLifecycle != MerchantAccountLifecycle.OPEN) {
                throw new MerchantAccountLifecycleConflictException(
                        command.merchantScope().merchantIdentifier(),
                        currentLifecycle,
                        MerchantAccountLifecycle.OPEN
                );
            }
            requireNotSuspended(command.merchantScope());
            MerchantControllerRelationship current = requireActiveControllerForUpdate(
                    command.merchantScope()
            );
            requireCurrentController(
                    current,
                    command.expectedCurrentControllerRelationshipIdentity(),
                    command.initiatingControllerIdentity()
            );

            int changed = dsl.update(ACCOUNT)
                    .set(ACCOUNT_LIFECYCLE, MerchantAccountLifecycle.CLOSING.name())
                    .where(MERCHANT_ID.eq(command.merchantScope().merchantIdentifier()))
                    .and(ACCOUNT_LIFECYCLE.eq(MerchantAccountLifecycle.OPEN.name()))
                    .execute();
            if (changed != 1) {
                throw new MerchantAccountLifecycleConflictException(
                        command.merchantScope().merchantIdentifier(),
                        lifecycle(command.merchantScope()),
                        MerchantAccountLifecycle.OPEN
                );
            }
            dsl.insertInto(CLOSURE_BEGIN_REQUEST)
                    .columns(
                            LOGICAL_REQUEST_ID,
                            MERCHANT_ID,
                            EXPECTED_CONTROLLER_REL_ID,
                            INITIATING_CONTROLLER_ID,
                            AUTH_ASSURANCE_EVIDENCE_ID,
                            REQUESTED_AT
                    )
                    .values(
                            command.logicalRequestIdentity(),
                            command.merchantScope().merchantIdentifier(),
                            command.expectedCurrentControllerRelationshipIdentity(),
                            command.initiatingControllerIdentity(),
                            command.authenticationAssuranceEvidenceIdentity(),
                            command.requestedAt()
                    )
                    .execute();
            return MerchantAccountLifecycle.CLOSING;
        });
        return Objects.requireNonNull(result, "Closure initiation returned no result");
    }

    @Override
    public MerchantAccountLifecycle finalizeClosure(
            FinalizeMerchantAccountClosureCommand command
    ) {
        Objects.requireNonNull(command, "command");
        MerchantAccountLifecycle result = transactions.execute(status -> {
            lockRequest("closure-finalize", command.logicalRequestIdentity(), 7605);
            Record replay = closureFinalizeRequest(command.logicalRequestIdentity());
            if (replay != null) {
                requireSameClosureFinalizeIntent(replay, command);
                return MerchantAccountLifecycle.CLOSED;
            }

            lockMerchant(command.merchantScope());
            MerchantAccountLifecycle currentLifecycle = lifecycleForUpdate(
                    command.merchantScope()
            );
            if (currentLifecycle != MerchantAccountLifecycle.CLOSING) {
                throw new MerchantAccountLifecycleConflictException(
                        command.merchantScope().merchantIdentifier(),
                        currentLifecycle,
                        MerchantAccountLifecycle.CLOSING
                );
            }
            MerchantControllerRelationship current = requireActiveControllerForUpdate(
                    command.merchantScope()
            );
            if (!current.relationshipIdentifier().equals(
                    command.expectedCurrentControllerRelationshipIdentity()
            )) {
                throw new MerchantControllerTransferConflictException(
                        "Final closure is based on a stale Controller relationship"
                );
            }

            int ended = dsl.update(CONTROLLER)
                    .set(
                            RELATIONSHIP_LIFECYCLE,
                            MerchantControllerRelationshipLifecycle.ENDED.name()
                    )
                    .where(MERCHANT_ID.eq(command.merchantScope().merchantIdentifier()))
                    .and(CONTROLLER_REL_ID.eq(current.relationshipIdentifier()))
                    .and(RELATIONSHIP_LIFECYCLE.eq(
                            MerchantControllerRelationshipLifecycle.ACTIVE.name()
                    ))
                    .execute();
            if (ended != 1) {
                throw new MerchantControllerTransferConflictException(
                        "Controller changed before final closure commit"
                );
            }

            int closed = dsl.update(ACCOUNT)
                    .set(ACCOUNT_LIFECYCLE, MerchantAccountLifecycle.CLOSED.name())
                    .where(MERCHANT_ID.eq(command.merchantScope().merchantIdentifier()))
                    .and(ACCOUNT_LIFECYCLE.eq(MerchantAccountLifecycle.CLOSING.name()))
                    .execute();
            if (closed != 1) {
                throw new MerchantAccountLifecycleConflictException(
                        command.merchantScope().merchantIdentifier(),
                        lifecycle(command.merchantScope()),
                        MerchantAccountLifecycle.CLOSING
                );
            }

            dsl.insertInto(CLOSURE_FINALIZE_REQUEST)
                    .columns(
                            LOGICAL_REQUEST_ID,
                            MERCHANT_ID,
                            EXPECTED_CONTROLLER_REL_ID,
                            CLOSURE_READINESS_EVIDENCE_ID,
                            ACTING_AUTHORITY_ID,
                            CLOSED_AT
                    )
                    .values(
                            command.logicalRequestIdentity(),
                            command.merchantScope().merchantIdentifier(),
                            command.expectedCurrentControllerRelationshipIdentity(),
                            command.closureReadinessEvidenceIdentity(),
                            command.actingAuthorityIdentifier(),
                            command.closedAt()
                    )
                    .execute();
            return MerchantAccountLifecycle.CLOSED;
        });
        return Objects.requireNonNull(result, "Final closure returned no result");
    }

    @Override
    public MerchantAccountLifecycle lifecycle(MerchantScope merchantScope) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        String value = dsl.select(ACCOUNT_LIFECYCLE)
                .from(ACCOUNT)
                .where(MERCHANT_ID.eq(merchantScope.merchantIdentifier()))
                .fetchOne(ACCOUNT_LIFECYCLE);
        if (value == null) {
            throw new IllegalArgumentException(
                    "Unknown Merchant Account: " + merchantScope.merchantIdentifier()
            );
        }
        return MerchantAccountLifecycle.valueOf(value);
    }

    @Override
    public Optional<MerchantControllerRelationship> activeController(
            MerchantScope merchantScope
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Record record = dsl.select(
                        CONTROLLER_REL_ID,
                        MERCHANT_ID,
                        IDENTITY_ID,
                        RELATIONSHIP_LIFECYCLE
                )
                .from(CONTROLLER)
                .where(MERCHANT_ID.eq(merchantScope.merchantIdentifier()))
                .and(RELATIONSHIP_LIFECYCLE.eq(
                        MerchantControllerRelationshipLifecycle.ACTIVE.name()
                ))
                .fetchOne();
        return Optional.ofNullable(record).map(this::toControllerRelationship);
    }

    private MerchantAccountLifecycle lifecycleForUpdate(MerchantScope merchantScope) {
        String value = dsl.select(ACCOUNT_LIFECYCLE)
                .from(ACCOUNT)
                .where(MERCHANT_ID.eq(merchantScope.merchantIdentifier()))
                .forUpdate()
                .fetchOne(ACCOUNT_LIFECYCLE);
        if (value == null) {
            throw new IllegalArgumentException(
                    "Unknown Merchant Account: " + merchantScope.merchantIdentifier()
            );
        }
        return MerchantAccountLifecycle.valueOf(value);
    }

    private MerchantControllerRelationship requireActiveControllerForUpdate(
            MerchantScope merchantScope
    ) {
        Record record = dsl.select(
                        CONTROLLER_REL_ID,
                        MERCHANT_ID,
                        IDENTITY_ID,
                        RELATIONSHIP_LIFECYCLE
                )
                .from(CONTROLLER)
                .where(MERCHANT_ID.eq(merchantScope.merchantIdentifier()))
                .and(RELATIONSHIP_LIFECYCLE.eq(
                        MerchantControllerRelationshipLifecycle.ACTIVE.name()
                ))
                .forUpdate()
                .fetchOne();
        if (record == null) {
            throw new MerchantControllerTransferConflictException(
                    "Non-closed Merchant Account has no ACTIVE Controller"
            );
        }
        return toControllerRelationship(record);
    }

    private MerchantControllerRelationship requireControllerRelationship(
            MerchantScope merchantScope,
            String relationshipIdentity
    ) {
        Record record = dsl.select(
                        CONTROLLER_REL_ID,
                        MERCHANT_ID,
                        IDENTITY_ID,
                        RELATIONSHIP_LIFECYCLE
                )
                .from(CONTROLLER)
                .where(MERCHANT_ID.eq(merchantScope.merchantIdentifier()))
                .and(CONTROLLER_REL_ID.eq(relationshipIdentity))
                .fetchOne();
        if (record == null) {
            throw new IllegalStateException(
                    "Committed Controller relationship is unavailable"
            );
        }
        return toControllerRelationship(record);
    }

    private MerchantControllerRelationship toControllerRelationship(Record record) {
        return new MerchantControllerRelationship(
                record.get(CONTROLLER_REL_ID),
                new MerchantScope(record.get(MERCHANT_ID)),
                record.get(IDENTITY_ID),
                MerchantControllerRelationshipLifecycle.valueOf(
                        record.get(RELATIONSHIP_LIFECYCLE)
                )
        );
    }

    private void requireCurrentController(
            MerchantControllerRelationship current,
            String expectedRelationship,
            String expectedIdentity
    ) {
        if (!current.relationshipIdentifier().equals(expectedRelationship)
                || !current.identityIdentifier().equals(expectedIdentity)) {
            throw new MerchantControllerTransferConflictException(
                    "Operation is based on stale Merchant Controller authority"
            );
        }
    }

    private void requireNotSuspended(MerchantScope merchantScope) {
        if (dsl.fetchExists(
                dsl.selectOne()
                        .from(SUSPENSION)
                        .where(MERCHANT_ID.eq(merchantScope.merchantIdentifier()))
                        .and(RELEASED_AT.isNull())
        )) {
            throw new MerchantAccountSuspendedException(
                    merchantScope.merchantIdentifier()
            );
        }
    }

    private void insertSuspension(MerchantAccountSuspension suspension) {
        dsl.insertInto(SUSPENSION)
                .columns(
                        SUSPENSION_ID,
                        MERCHANT_ID,
                        SOURCE_AUTHORITY_ID,
                        REASON_CLASS_ID,
                        ESTABLISHED_AT,
                        ESTABLISHED_BY_ID,
                        RELEASE_AUTHORITY_ID,
                        PROVENANCE_ID,
                        RELEASED_AT,
                        RELEASED_BY_ID,
                        RELEASE_EVIDENCE_ID
                )
                .values(
                        suspension.suspensionIdentity(),
                        suspension.merchantScope().merchantIdentifier(),
                        suspension.sourceAuthorityIdentifier(),
                        suspension.reasonClassIdentifier(),
                        suspension.establishedAt(),
                        suspension.establishedByIdentifier(),
                        suspension.releaseAuthorityIdentifier(),
                        suspension.provenanceIdentifier(),
                        null,
                        null,
                        null
                )
                .execute();
    }

    private MerchantAccountSuspension requireSuspension(String suspensionIdentity) {
        Record record = suspensionRecord(suspensionIdentity, false);
        if (record == null) {
            throw new IllegalArgumentException(
                    "Unknown Merchant Account Suspension: " + suspensionIdentity
            );
        }
        return toSuspension(record);
    }

    private MerchantAccountSuspension requireSuspensionForUpdate(
            String suspensionIdentity
    ) {
        Record record = suspensionRecord(suspensionIdentity, true);
        if (record == null) {
            throw new IllegalArgumentException(
                    "Unknown Merchant Account Suspension: " + suspensionIdentity
            );
        }
        return toSuspension(record);
    }

    private Record suspensionRecord(String suspensionIdentity, boolean forUpdate) {
        var query = dsl.select(
                        SUSPENSION_ID,
                        MERCHANT_ID,
                        SOURCE_AUTHORITY_ID,
                        REASON_CLASS_ID,
                        ESTABLISHED_AT,
                        ESTABLISHED_BY_ID,
                        RELEASE_AUTHORITY_ID,
                        PROVENANCE_ID,
                        RELEASED_AT,
                        RELEASED_BY_ID,
                        RELEASE_EVIDENCE_ID
                )
                .from(SUSPENSION)
                .where(SUSPENSION_ID.eq(suspensionIdentity));
        return forUpdate ? query.forUpdate().fetchOne() : query.fetchOne();
    }

    private MerchantAccountSuspension toSuspension(Record record) {
        return new MerchantAccountSuspension(
                record.get(SUSPENSION_ID),
                new MerchantScope(record.get(MERCHANT_ID)),
                record.get(SOURCE_AUTHORITY_ID),
                record.get(REASON_CLASS_ID),
                record.get(ESTABLISHED_AT),
                record.get(ESTABLISHED_BY_ID),
                record.get(RELEASE_AUTHORITY_ID),
                record.get(PROVENANCE_ID),
                Optional.ofNullable(record.get(RELEASED_AT)),
                Optional.ofNullable(record.get(RELEASED_BY_ID)),
                Optional.ofNullable(record.get(RELEASE_EVIDENCE_ID))
        );
    }

    private boolean controllerRelationshipExists(String relationshipIdentity) {
        return dsl.fetchExists(
                dsl.selectOne()
                        .from(CONTROLLER)
                        .where(CONTROLLER_REL_ID.eq(relationshipIdentity))
        );
    }

    private boolean suspensionExists(String suspensionIdentity) {
        return dsl.fetchExists(
                dsl.selectOne()
                        .from(SUSPENSION)
                        .where(SUSPENSION_ID.eq(suspensionIdentity))
        );
    }

    private Record transferRequest(String requestIdentity) {
        return dsl.select(
                        LOGICAL_REQUEST_ID,
                        MERCHANT_ID,
                        EXPECTED_CONTROLLER_REL_ID,
                        INITIATING_CONTROLLER_ID,
                        RECEIVING_ID,
                        REPLACEMENT_CONTROLLER_REL_ID,
                        AUTH_ASSURANCE_EVIDENCE_ID,
                        RECIPIENT_ACCEPTANCE_EVIDENCE_ID,
                        TRANSFERRED_AT
                )
                .from(TRANSFER_REQUEST)
                .where(LOGICAL_REQUEST_ID.eq(requestIdentity))
                .fetchOne();
    }

    private Record suspensionEstablishmentRequest(String requestIdentity) {
        return dsl.select(LOGICAL_REQUEST_ID, SUSPENSION_ID, MERCHANT_ID)
                .from(SUSPENSION_REQUEST)
                .where(LOGICAL_REQUEST_ID.eq(requestIdentity))
                .fetchOne();
    }

    private Record suspensionReleaseRequest(String requestIdentity) {
        return dsl.select(
                        LOGICAL_REQUEST_ID,
                        SUSPENSION_ID,
                        MERCHANT_ID,
                        RELEASING_AUTHORITY_ID,
                        RELEASED_AT,
                        RELEASED_BY_ID,
                        RELEASE_EVIDENCE_ID
                )
                .from(SUSPENSION_RELEASE_REQUEST)
                .where(LOGICAL_REQUEST_ID.eq(requestIdentity))
                .fetchOne();
    }

    private Record closureBeginRequest(String requestIdentity) {
        return dsl.select(
                        LOGICAL_REQUEST_ID,
                        MERCHANT_ID,
                        EXPECTED_CONTROLLER_REL_ID,
                        INITIATING_CONTROLLER_ID,
                        AUTH_ASSURANCE_EVIDENCE_ID,
                        REQUESTED_AT
                )
                .from(CLOSURE_BEGIN_REQUEST)
                .where(LOGICAL_REQUEST_ID.eq(requestIdentity))
                .fetchOne();
    }

    private Record closureFinalizeRequest(String requestIdentity) {
        return dsl.select(
                        LOGICAL_REQUEST_ID,
                        MERCHANT_ID,
                        EXPECTED_CONTROLLER_REL_ID,
                        CLOSURE_READINESS_EVIDENCE_ID,
                        ACTING_AUTHORITY_ID,
                        CLOSED_AT
                )
                .from(CLOSURE_FINALIZE_REQUEST)
                .where(LOGICAL_REQUEST_ID.eq(requestIdentity))
                .fetchOne();
    }

    private void requireSameTransferIntent(
            Record record,
            MerchantControllerTransferCommand command
    ) {
        requireEqual(record.get(MERCHANT_ID), command.merchantScope().merchantIdentifier(), "merchant");
        requireEqual(record.get(EXPECTED_CONTROLLER_REL_ID), command.expectedCurrentControllerRelationshipIdentity(), "expected Controller");
        requireEqual(record.get(INITIATING_CONTROLLER_ID), command.initiatingControllerIdentity(), "initiating Controller");
        requireEqual(record.get(RECEIVING_ID), command.receivingIdentity(), "receiving identity");
        requireEqual(record.get(REPLACEMENT_CONTROLLER_REL_ID), command.replacementControllerRelationshipIdentity(), "replacement relationship");
        requireEqual(record.get(AUTH_ASSURANCE_EVIDENCE_ID), command.authenticationAssuranceEvidenceIdentity(), "authentication evidence");
        requireEqual(record.get(RECIPIENT_ACCEPTANCE_EVIDENCE_ID), command.recipientAcceptanceEvidenceIdentity(), "recipient acceptance");
        requireEqual(record.get(TRANSFERRED_AT), command.transferredAt(), "transfer time");
    }

    private void requireSameSuspensionIntent(
            MerchantAccountSuspension suspension,
            MerchantAccountSuspensionCommand command
    ) {
        requireEqual(suspension.merchantScope(), command.merchantScope(), "merchant");
        requireEqual(suspension.suspensionIdentity(), command.suspensionIdentity(), "suspension identity");
        requireEqual(suspension.sourceAuthorityIdentifier(), command.sourceAuthorityIdentifier(), "source authority");
        requireEqual(suspension.reasonClassIdentifier(), command.reasonClassIdentifier(), "reason class");
        requireEqual(suspension.establishedAt(), command.establishedAt(), "establishment time");
        requireEqual(suspension.establishedByIdentifier(), command.establishedByIdentifier(), "established by");
        requireEqual(suspension.releaseAuthorityIdentifier(), command.releaseAuthorityIdentifier(), "release authority");
        requireEqual(suspension.provenanceIdentifier(), command.provenanceIdentifier(), "provenance");
    }

    private void requireSameSuspensionReleaseIntent(
            Record record,
            MerchantAccountSuspensionReleaseCommand command
    ) {
        requireEqual(record.get(SUSPENSION_ID), command.suspensionIdentity(), "suspension identity");
        requireEqual(record.get(MERCHANT_ID), command.merchantScope().merchantIdentifier(), "merchant");
        requireEqual(record.get(RELEASING_AUTHORITY_ID), command.releasingAuthorityIdentifier(), "releasing authority");
        requireEqual(record.get(RELEASED_AT), command.releasedAt(), "release time");
        requireEqual(record.get(RELEASED_BY_ID), command.releasedByIdentifier(), "released by");
        requireEqual(record.get(RELEASE_EVIDENCE_ID), command.releaseEvidenceIdentifier(), "release evidence");
    }

    private void requireSameClosureBeginIntent(
            Record record,
            BeginMerchantAccountClosureCommand command
    ) {
        requireEqual(record.get(MERCHANT_ID), command.merchantScope().merchantIdentifier(), "merchant");
        requireEqual(record.get(EXPECTED_CONTROLLER_REL_ID), command.expectedCurrentControllerRelationshipIdentity(), "expected Controller");
        requireEqual(record.get(INITIATING_CONTROLLER_ID), command.initiatingControllerIdentity(), "initiating Controller");
        requireEqual(record.get(AUTH_ASSURANCE_EVIDENCE_ID), command.authenticationAssuranceEvidenceIdentity(), "authentication evidence");
        requireEqual(record.get(REQUESTED_AT), command.requestedAt(), "request time");
    }

    private void requireSameClosureFinalizeIntent(
            Record record,
            FinalizeMerchantAccountClosureCommand command
    ) {
        requireEqual(record.get(MERCHANT_ID), command.merchantScope().merchantIdentifier(), "merchant");
        requireEqual(record.get(EXPECTED_CONTROLLER_REL_ID), command.expectedCurrentControllerRelationshipIdentity(), "expected Controller");
        requireEqual(record.get(CLOSURE_READINESS_EVIDENCE_ID), command.closureReadinessEvidenceIdentity(), "closure readiness evidence");
        requireEqual(record.get(ACTING_AUTHORITY_ID), command.actingAuthorityIdentifier(), "acting authority");
        requireEqual(record.get(CLOSED_AT), command.closedAt(), "closure time");
    }

    private static void requireEqual(Object actual, Object expected, String field) {
        if (!Objects.equals(actual, expected)) {
            throw new IllegalArgumentException(
                    "Logical request identity was reused with different " + field
            );
        }
    }

    private void lockMerchant(MerchantScope merchantScope) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 76))",
                merchantScope.merchantIdentifier()
        );
    }

    private void lockRequest(String kind, String requestIdentity, int seed) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), ?))",
                kind + "\u001f" + requestIdentity,
                seed
        );
    }
}
