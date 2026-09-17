package grandrue.infrastructure.persistence.background;

import mainstreet.application.MerchantScope;
import grandrue.background.BackgroundExecutionScope;
import grandrue.background.BackgroundWorkContractAffinity;
import grandrue.background.BackgroundWorkContractIdentity;
import grandrue.background.BackgroundWorkResultClassification;
import grandrue.background.ClaimedWork;
import grandrue.background.DurableWorkInstruction;
import grandrue.background.DurableWorkStore;
import grandrue.background.OverdueHandling;
import grandrue.background.WorkAttempt;
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
 * PostgreSQL/jOOQ infrastructure for durable scheduling, technical claims and
 * attempt evidence under MS-PROT-065.
 *
 * <p>This adapter never decides that the referenced business responsibility is
 * authorised or still applicable. It persists only the owning contract's
 * instruction and the technical processing state.</p>
 */
public final class JooqDurableWorkStore implements DurableWorkStore {

    private static final Table<?> WORK =
            DSL.table(DSL.name("durable_work_instruction"));

    private static final Table<?> ATTEMPT =
            DSL.table(DSL.name("durable_work_attempt"));

    private static final Field<String> WORK_IDENTIFIER =
            DSL.field(
                    DSL.name("work_identifier"),
                    String.class
            );

    private static final Field<String> OWNER_CONTEXT_IDENTIFIER =
            DSL.field(
                    DSL.name("owner_context_identifier"),
                    String.class
            );

    private static final Field<String> EXECUTION_SCOPE =
            DSL.field(
                    DSL.name("execution_scope"),
                    String.class
            );

    private static final Field<String> MERCHANT_IDENTIFIER =
            DSL.field(
                    DSL.name("merchant_identifier"),
                    String.class
            );

    private static final Field<Instant> DUE_AT =
            DSL.field(
                    DSL.name("due_at"),
                    Instant.class
            );

    private static final Field<Instant> NEXT_ATTEMPT_AT =
            DSL.field(
                    DSL.name("next_attempt_at"),
                    Instant.class
            );

    private static final Field<String> RESPONSIBILITY_IDENTIFIER =
            DSL.field(
                    DSL.name("responsibility_identifier"),
                    String.class
            );

    private static final Field<String> CORRELATION_IDENTIFIER =
            DSL.field(
                    DSL.name("correlation_identifier"),
                    String.class
            );

    private static final Field<String> CAUSATION_IDENTIFIER =
            DSL.field(
                    DSL.name("causation_identifier"),
                    String.class
            );

    private static final Field<String> SEMANTIC_PROVENANCE_REFERENCE =
            DSL.field(
                    DSL.name("semantic_provenance_reference"),
                    String.class
            );

    private static final Field<String> CONTRACT_OWNER_IDENTIFIER =
            DSL.field(
                    DSL.name("contract_owner_identifier"),
                    String.class
            );

    private static final Field<String> CONTRACT_IDENTIFIER =
            DSL.field(
                    DSL.name("contract_identifier"),
                    String.class
            );

    private static final Field<String> CONTRACT_SEMANTIC_RELEASE =
            DSL.field(
                    DSL.name("contract_semantic_release"),
                    String.class
            );

    private static final Field<String> RETRY_POLICY_REFERENCE =
            DSL.field(
                    DSL.name("retry_policy_reference"),
                    String.class
            );

    private static final Field<String> OVERDUE_HANDLING =
            DSL.field(
                    DSL.name("overdue_handling"),
                    String.class
            );

    private static final Field<Instant> CREATED_AT =
            DSL.field(
                    DSL.name("created_at"),
                    Instant.class
            );

    private static final Field<String> CLAIMED_BY =
            DSL.field(
                    DSL.name("claimed_by"),
                    String.class
            );

    private static final Field<Instant> CLAIM_EXPIRES_AT =
            DSL.field(
                    DSL.name("claim_expires_at"),
                    Instant.class
            );

    private static final Field<String> FINAL_CLASSIFICATION =
            DSL.field(
                    DSL.name("final_classification"),
                    String.class
            );

    private static final Field<Instant> FINALISED_AT =
            DSL.field(
                    DSL.name("finalised_at"),
                    Instant.class
            );

    private static final Field<String> ATTEMPT_IDENTIFIER =
            DSL.field(
                    DSL.name("attempt_identifier"),
                    String.class
            );

    private static final Field<Instant> ATTEMPTED_AT =
            DSL.field(
                    DSL.name("attempted_at"),
                    Instant.class
            );

    private static final Field<String> PRINCIPAL_REFERENCE =
            DSL.field(
                    DSL.name("principal_reference"),
                    String.class
            );

    private static final Field<String> RESULT_CLASSIFICATION =
            DSL.field(
                    DSL.name("result_classification"),
                    String.class
            );

    private static final Field<String> EVIDENCE_REFERENCE =
            DSL.field(
                    DSL.name("evidence_reference"),
                    String.class
            );

    private final DSLContext dsl;
    private final TransactionTemplate transactionTemplate;

    public JooqDurableWorkStore(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl =
                Objects.requireNonNull(
                        dsl,
                        "dsl"
                );

        this.transactionTemplate =
                new TransactionTemplate(
                        Objects.requireNonNull(
                                transactionManager,
                                "transactionManager"
                        )
                );
    }

    @Override
    public DurableWorkInstruction schedule(
            DurableWorkInstruction candidate
    ) {
        Objects.requireNonNull(
                candidate,
                "candidate"
        );

        DurableWorkInstruction result =
                transactionTemplate.execute(status -> {
                    lockIdentity(
                            "work",
                            candidate.workIdentity()
                    );

                    Optional<DurableWorkInstruction> existing =
                            instruction(
                                    candidate.workIdentity()
                            );

                    if (existing.isPresent()) {
                        DurableWorkInstruction committed =
                                existing.orElseThrow();

                        if (!committed.equals(candidate)) {
                            throw new IllegalStateException(
                                    "Work identity already exists "
                                            + "with a different instruction"
                            );
                        }

                        return committed;
                    }

                    dsl.insertInto(WORK)
                            .columns(
                                    WORK_IDENTIFIER,
                                    OWNER_CONTEXT_IDENTIFIER,
                                    EXECUTION_SCOPE,
                                    MERCHANT_IDENTIFIER,
                                    DUE_AT,
                                    NEXT_ATTEMPT_AT,
                                    RESPONSIBILITY_IDENTIFIER,
                                    CORRELATION_IDENTIFIER,
                                    CAUSATION_IDENTIFIER,
                                    SEMANTIC_PROVENANCE_REFERENCE,
                                    CONTRACT_OWNER_IDENTIFIER,
                                    CONTRACT_IDENTIFIER,
                                    CONTRACT_SEMANTIC_RELEASE,
                                    RETRY_POLICY_REFERENCE,
                                    OVERDUE_HANDLING,
                                    CREATED_AT
                            )
                            .values(
                                    candidate.workIdentity(),
                                    candidate.ownerContextIdentifier(),
                                    candidate.executionScope()
                                            .name(),
                                    candidate.merchantScope()
                                            .map(
                                                    MerchantScope
                                                            ::merchantIdentifier
                                            )
                                            .orElse(null),
                                    candidate.dueAt(),
                                    candidate.dueAt(),
                                    candidate.responsibilityIdentifier(),
                                    candidate.correlationIdentifier(),
                                    candidate.causationIdentifier()
                                            .orElse(null),
                                    candidate.semanticProvenanceReference()
                                            .orElse(null),
                                    candidate.contractAffinity()
                                            .map(affinity ->
                                                    affinity
                                                            .contractIdentity()
                                                            .ownerIdentifier()
                                            )
                                            .orElse(null),
                                    candidate.contractAffinity()
                                            .map(affinity ->
                                                    affinity
                                                            .contractIdentity()
                                                            .contractIdentifier()
                                            )
                                            .orElse(null),
                                    candidate.contractAffinity()
                                            .map(
                                                    BackgroundWorkContractAffinity
                                                            ::semanticRegistryReleaseIdentifier
                                            )
                                            .orElse(null),
                                    candidate.retryPolicyReference(),
                                    candidate.overdueHandling()
                                            .name(),
                                    candidate.createdAt()
                            )
                            .execute();

                    return candidate;
                });

        return Objects.requireNonNull(
                result,
                "Work scheduling transaction returned no result"
        );
    }

    @Override
    public Optional<DurableWorkInstruction> instruction(
            String workIdentity
    ) {
        requireIdentifier(
                workIdentity,
                "workIdentity"
        );

        Record row =
                selectInstructionFields()
                        .from(WORK)
                        .where(
                                WORK_IDENTIFIER.eq(
                                        workIdentity
                                )
                        )
                        .fetchOne();

        return Optional.ofNullable(row)
                .map(this::toInstruction);
    }

    @Override
    public List<ClaimedWork> claimDue(
            String workerIdentity,
            Instant now,
            Instant claimExpiresAt,
            int limit
    ) {
        requireIdentifier(
                workerIdentity,
                "workerIdentity"
        );

        Objects.requireNonNull(
                now,
                "now"
        );

        Objects.requireNonNull(
                claimExpiresAt,
                "claimExpiresAt"
        );

        if (!claimExpiresAt.isAfter(now)) {
            throw new IllegalArgumentException(
                    "Claim expiry must be after claim time"
            );
        }

        if (limit < 1) {
            throw new IllegalArgumentException(
                    "Claim limit must be positive"
            );
        }

        List<ClaimedWork> claimed =
                transactionTemplate.execute(status -> {
                    var rows =
                            selectInstructionFields()
                                    .from(WORK)
                                    .where(
                                            FINALISED_AT.isNull()
                                    )
                                    .and(
                                            NEXT_ATTEMPT_AT.le(now)
                                    )
                                    .and(
                                            CLAIMED_BY.isNull()
                                                    .or(
                                                            CLAIM_EXPIRES_AT
                                                                    .le(now)
                                                    )
                                    )
                                    .orderBy(
                                            NEXT_ATTEMPT_AT,
                                            WORK_IDENTIFIER
                                    )
                                    .limit(limit)
                                    .forUpdate()
                                    .skipLocked()
                                    .fetch();

                    List<ClaimedWork> results =
                            new ArrayList<>(
                                    rows.size()
                            );

                    for (Record row : rows) {
                        String workIdentity =
                                row.get(
                                        WORK_IDENTIFIER
                                );

                        int updated =
                                dsl.update(WORK)
                                        .set(
                                                CLAIMED_BY,
                                                workerIdentity
                                        )
                                        .set(
                                                CLAIM_EXPIRES_AT,
                                                claimExpiresAt
                                        )
                                        .where(
                                                WORK_IDENTIFIER.eq(
                                                        workIdentity
                                                )
                                        )
                                        .and(
                                                FINALISED_AT.isNull()
                                        )
                                        .execute();

                        if (updated != 1) {
                            throw new IllegalStateException(
                                    "Could not establish "
                                            + "technical claim for work: "
                                            + workIdentity
                            );
                        }

                        results.add(
                                new ClaimedWork(
                                        toInstruction(row),
                                        workerIdentity,
                                        claimExpiresAt
                                )
                        );
                    }

                    return List.copyOf(
                            results
                    );
                });

        return Objects.requireNonNull(
                claimed,
                "Work claim transaction returned no result"
        );
    }

    @Override
    public WorkAttempt startAttempt(
            String attemptIdentity,
            String workIdentity,
            String workerIdentity,
            Instant attemptedAt,
            String principalReference
    ) {
        requireIdentifier(
                attemptIdentity,
                "attemptIdentity"
        );

        requireIdentifier(
                workIdentity,
                "workIdentity"
        );

        requireIdentifier(
                workerIdentity,
                "workerIdentity"
        );

        Objects.requireNonNull(
                attemptedAt,
                "attemptedAt"
        );

        requireIdentifier(
                principalReference,
                "principalReference"
        );

        WorkAttempt result =
                transactionTemplate.execute(status -> {
                    lockIdentity(
                            "attempt",
                            attemptIdentity
                    );

                    Record claim = requireCurrentClaim(
                            workIdentity,
                            workerIdentity
                    );

                    Instant claimExpiresAt = claim.get(
                            CLAIM_EXPIRES_AT
                    );
                    if (claimExpiresAt == null
                            || !attemptedAt.isBefore(
                                    claimExpiresAt
                            )) {
                        throw new IllegalStateException(
                                "Worker claim expired before "
                                        + "the attempt could start"
                        );
                    }

                    Optional<WorkAttempt> existing =
                            attempt(
                                    attemptIdentity
                            );

                    if (existing.isPresent()) {
                        WorkAttempt committed =
                                existing.orElseThrow();

                        if (!sameAttemptStart(
                                committed,
                                workIdentity,
                                attemptedAt,
                                principalReference
                        )) {
                            throw new IllegalStateException(
                                    "Attempt identity already exists "
                                            + "with different start evidence"
                            );
                        }

                        return committed;
                    }

                    Optional<WorkAttempt> latest =
                            latestAttempt(
                                    workIdentity
                            );

                    if (latest.isPresent()) {
                        WorkAttempt previous =
                                latest.orElseThrow();

                        if (previous
                                .resultClassification()
                                .isEmpty()) {
                            throw new IllegalStateException(
                                    "Cannot start another attempt "
                                            + "while the prior attempt "
                                            + "outcome is unresolved"
                            );
                        }

                        if (!previous
                                .resultClassification()
                                .equals(
                                        Optional.of(
                                                BackgroundWorkResultClassification
                                                        .RETRY_SAFE
                                        )
                                )) {
                            throw new IllegalStateException(
                                    "Another attempt requires "
                                            + "a RETRY_SAFE prior outcome"
                            );
                        }

                        if (attemptedAt.isBefore(
                                previous.attemptedAt()
                        )) {
                            throw new IllegalArgumentException(
                                    "New attempt cannot precede "
                                            + "the prior attempt"
                            );
                        }
                    }

                    WorkAttempt candidate =
                            new WorkAttempt(
                                    attemptIdentity,
                                    workIdentity,
                                    attemptedAt,
                                    principalReference,
                                    Optional.empty(),
                                    Optional.empty()
                            );

                    dsl.insertInto(ATTEMPT)
                            .columns(
                                    ATTEMPT_IDENTIFIER,
                                    WORK_IDENTIFIER,
                                    ATTEMPTED_AT,
                                    PRINCIPAL_REFERENCE,
                                    RESULT_CLASSIFICATION,
                                    EVIDENCE_REFERENCE
                            )
                            .values(
                                    candidate.attemptIdentity(),
                                    candidate.workIdentity(),
                                    candidate.attemptedAt(),
                                    candidate.principalReference(),
                                    (String) null,
                                    (String) null
                            )
                            .execute();

                    return candidate;
                });

        return Objects.requireNonNull(
                result,
                "Attempt start transaction returned no result"
        );
    }

    @Override
    public WorkAttempt recordAttemptOutcome(
            String attemptIdentity,
            String workerIdentity,
            BackgroundWorkResultClassification classification,
            Optional<String> evidenceReference
    ) {
        requireIdentifier(
                attemptIdentity,
                "attemptIdentity"
        );

        requireIdentifier(
                workerIdentity,
                "workerIdentity"
        );

        Objects.requireNonNull(
                classification,
                "classification"
        );

        Objects.requireNonNull(
                evidenceReference,
                "evidenceReference"
        );

        evidenceReference.ifPresent(value ->
                requireIdentifier(
                        value,
                        "evidenceReference"
                )
        );

        WorkAttempt result =
                transactionTemplate.execute(status -> {
                    lockIdentity(
                            "attempt",
                            attemptIdentity
                    );

                    WorkAttempt existing =
                            attempt(
                                    attemptIdentity
                            ).orElseThrow(() ->
                                    new IllegalStateException(
                                            "Unknown work attempt: "
                                                    + attemptIdentity
                                    )
                            );

                    requireCurrentClaim(
                            existing.workIdentity(),
                            workerIdentity
                    );

                    if (existing
                            .resultClassification()
                            .isPresent()) {
                        if (!existing
                                .resultClassification()
                                .equals(
                                        Optional.of(
                                                classification
                                        )
                                )
                                || !existing
                                .evidenceReference()
                                .equals(
                                        evidenceReference
                                )) {
                            throw new IllegalStateException(
                                    "Attempt outcome is already "
                                            + "classified differently"
                            );
                        }

                        return existing;
                    }

                    int updated =
                            dsl.update(ATTEMPT)
                                    .set(
                                            RESULT_CLASSIFICATION,
                                            classification.name()
                                    )
                                    .set(
                                            EVIDENCE_REFERENCE,
                                            evidenceReference
                                                    .orElse(null)
                                    )
                                    .where(
                                            ATTEMPT_IDENTIFIER.eq(
                                                    attemptIdentity
                                            )
                                    )
                                    .and(
                                            RESULT_CLASSIFICATION
                                                    .isNull()
                                    )
                                    .execute();

                    if (updated != 1) {
                        throw new IllegalStateException(
                                "Attempt outcome could not be recorded"
                        );
                    }

                    return new WorkAttempt(
                            existing.attemptIdentity(),
                            existing.workIdentity(),
                            existing.attemptedAt(),
                            existing.principalReference(),
                            Optional.of(
                                    classification
                            ),
                            evidenceReference
                    );
                });

        return Objects.requireNonNull(
                result,
                "Attempt outcome transaction returned no result"
        );
    }

    @Override
    public Optional<WorkAttempt> latestAttempt(
            String workIdentity
    ) {
        requireIdentifier(
                workIdentity,
                "workIdentity"
        );

        Record row =
                dsl.select(
                                ATTEMPT_IDENTIFIER,
                                WORK_IDENTIFIER,
                                ATTEMPTED_AT,
                                PRINCIPAL_REFERENCE,
                                RESULT_CLASSIFICATION,
                                EVIDENCE_REFERENCE
                        )
                        .from(ATTEMPT)
                        .where(
                                WORK_IDENTIFIER.eq(
                                        workIdentity
                                )
                        )
                        .orderBy(
                                ATTEMPTED_AT.desc(),
                                ATTEMPT_IDENTIFIER.desc()
                        )
                        .limit(1)
                        .fetchOne();

        return Optional.ofNullable(row)
                .map(this::toAttempt);
    }

    @Override
    public void rescheduleRetry(
            String workIdentity,
            String workerIdentity,
            Instant nextAttemptAt
    ) {
        requireIdentifier(
                workIdentity,
                "workIdentity"
        );

        requireIdentifier(
                workerIdentity,
                "workerIdentity"
        );

        Objects.requireNonNull(
                nextAttemptAt,
                "nextAttemptAt"
        );

        transactionTemplate.executeWithoutResult(status -> {
            lockIdentity(
                    "work",
                    workIdentity
            );

            requireCurrentClaim(
                    workIdentity,
                    workerIdentity
            );

            WorkAttempt latest =
                    latestAttempt(
                            workIdentity
                    ).orElseThrow(() ->
                            new IllegalStateException(
                                    "Retry reschedule "
                                            + "requires an attempt"
                            )
                    );

            if (!latest
                    .resultClassification()
                    .equals(
                            Optional.of(
                                    BackgroundWorkResultClassification
                                            .RETRY_SAFE
                            )
                    )) {
                throw new IllegalStateException(
                        "Only RETRY_SAFE work may be "
                                + "technically rescheduled"
                );
            }

            int updated =
                    dsl.update(WORK)
                            .set(
                                    NEXT_ATTEMPT_AT,
                                    nextAttemptAt
                            )
                            .set(
                                    CLAIMED_BY,
                                    (String) null
                            )
                            .set(
                                    CLAIM_EXPIRES_AT,
                                    (Instant) null
                            )
                            .where(
                                    WORK_IDENTIFIER.eq(
                                            workIdentity
                                    )
                            )
                            .and(
                                    FINALISED_AT.isNull()
                            )
                            .and(
                                    CLAIMED_BY.eq(
                                            workerIdentity
                                    )
                            )
                            .execute();

            if (updated != 1) {
                throw new IllegalStateException(
                        "Work retry reschedule "
                                + "lost its technical claim"
                );
            }
        });
    }

    @Override
    public void finalise(
            String workIdentity,
            String workerIdentity,
            BackgroundWorkResultClassification classification,
            Instant finalisedAt
    ) {
        requireIdentifier(
                workIdentity,
                "workIdentity"
        );

        requireIdentifier(
                workerIdentity,
                "workerIdentity"
        );

        Objects.requireNonNull(
                classification,
                "classification"
        );

        Objects.requireNonNull(
                finalisedAt,
                "finalisedAt"
        );

        if (classification
                == BackgroundWorkResultClassification.RETRY_SAFE) {
            throw new IllegalArgumentException(
                    "RETRY_SAFE is an attempt result, "
                            + "not a terminal work classification"
            );
        }

        if (classification
                == BackgroundWorkResultClassification
                .RECONCILIATION_REQUIRED) {
            throw new IllegalArgumentException(
                    "RECONCILIATION_REQUIRED must be "
                            + "resolved before work finalisation"
            );
        }

        transactionTemplate.executeWithoutResult(status -> {
            lockIdentity(
                    "work",
                    workIdentity
            );

            requireCurrentClaim(
                    workIdentity,
                    workerIdentity
            );

            WorkAttempt latest =
                    latestAttempt(
                            workIdentity
                    ).orElseThrow(() ->
                            new IllegalStateException(
                                    "Work finalisation "
                                            + "requires an attempt"
                            )
                    );

            if (!latest
                    .resultClassification()
                    .equals(
                            Optional.of(
                                    classification
                            )
                    )) {
                throw new IllegalStateException(
                        "Work finalisation must match "
                                + "the latest attempt classification"
                );
            }

            if (finalisedAt.isBefore(
                    latest.attemptedAt()
            )) {
                throw new IllegalArgumentException(
                        "Work cannot be finalised "
                                + "before its latest attempt"
                );
            }

            int updated =
                    dsl.update(WORK)
                            .set(
                                    FINAL_CLASSIFICATION,
                                    classification.name()
                            )
                            .set(
                                    FINALISED_AT,
                                    finalisedAt
                            )
                            .set(
                                    CLAIMED_BY,
                                    (String) null
                            )
                            .set(
                                    CLAIM_EXPIRES_AT,
                                    (Instant) null
                            )
                            .where(
                                    WORK_IDENTIFIER.eq(
                                            workIdentity
                                    )
                            )
                            .and(
                                    FINALISED_AT.isNull()
                            )
                            .and(
                                    CLAIMED_BY.eq(
                                            workerIdentity
                                    )
                            )
                            .execute();

            if (updated != 1) {
                throw new IllegalStateException(
                        "Work finalisation "
                                + "lost its technical claim"
                );
            }
        });
    }

    private Optional<WorkAttempt> attempt(
            String attemptIdentity
    ) {
        Record row =
                dsl.select(
                                ATTEMPT_IDENTIFIER,
                                WORK_IDENTIFIER,
                                ATTEMPTED_AT,
                                PRINCIPAL_REFERENCE,
                                RESULT_CLASSIFICATION,
                                EVIDENCE_REFERENCE
                        )
                        .from(ATTEMPT)
                        .where(
                                ATTEMPT_IDENTIFIER.eq(
                                        attemptIdentity
                                )
                        )
                        .fetchOne();

        return Optional.ofNullable(row)
                .map(this::toAttempt);
    }

    private Record requireCurrentClaim(
            String workIdentity,
            String workerIdentity
    ) {
        Record row =
                dsl.select(
                                CLAIMED_BY,
                                CLAIM_EXPIRES_AT,
                                FINAL_CLASSIFICATION
                        )
                        .from(WORK)
                        .where(
                                WORK_IDENTIFIER.eq(
                                        workIdentity
                                )
                        )
                        .forUpdate()
                        .fetchOne();

        if (row == null) {
            throw new IllegalStateException(
                    "Unknown work instruction: "
                            + workIdentity
            );
        }

        if (row.get(
                FINAL_CLASSIFICATION
        ) != null) {
            throw new IllegalStateException(
                    "Work is already finalised: "
                            + workIdentity
            );
        }

        if (!workerIdentity.equals(
                row.get(
                        CLAIMED_BY
                )
        )) {
            throw new IllegalStateException(
                    "Worker does not hold the current "
                            + "technical claim for work: "
                            + workIdentity
            );
        }

        return row;
    }

    private org.jooq.SelectSelectStep<? extends Record>
    selectInstructionFields() {
        return dsl.select(
                WORK_IDENTIFIER,
                OWNER_CONTEXT_IDENTIFIER,
                EXECUTION_SCOPE,
                MERCHANT_IDENTIFIER,
                DUE_AT,
                NEXT_ATTEMPT_AT,
                RESPONSIBILITY_IDENTIFIER,
                CORRELATION_IDENTIFIER,
                CAUSATION_IDENTIFIER,
                SEMANTIC_PROVENANCE_REFERENCE,
                CONTRACT_OWNER_IDENTIFIER,
                CONTRACT_IDENTIFIER,
                CONTRACT_SEMANTIC_RELEASE,
                RETRY_POLICY_REFERENCE,
                OVERDUE_HANDLING,
                CREATED_AT,
                CLAIMED_BY,
                CLAIM_EXPIRES_AT,
                FINAL_CLASSIFICATION,
                FINALISED_AT
        );
    }

    private DurableWorkInstruction toInstruction(
            Record row
    ) {
        String merchantIdentifier =
                row.get(
                        MERCHANT_IDENTIFIER
                );

        return new DurableWorkInstruction(
                row.get(
                        WORK_IDENTIFIER
                ),
                row.get(
                        OWNER_CONTEXT_IDENTIFIER
                ),
                BackgroundExecutionScope.valueOf(
                        row.get(
                                EXECUTION_SCOPE
                        )
                ),
                Optional.ofNullable(
                        merchantIdentifier
                ).map(
                        MerchantScope::new
                ),
                row.get(
                        DUE_AT
                ),
                row.get(
                        RESPONSIBILITY_IDENTIFIER
                ),
                row.get(
                        CORRELATION_IDENTIFIER
                ),
                Optional.ofNullable(
                        row.get(
                                CAUSATION_IDENTIFIER
                        )
                ),
                Optional.ofNullable(
                        row.get(
                                SEMANTIC_PROVENANCE_REFERENCE
                        )
                ),
                row.get(
                        RETRY_POLICY_REFERENCE
                ),
                OverdueHandling.valueOf(
                        row.get(
                                OVERDUE_HANDLING
                        )
                ),
                row.get(
                        CREATED_AT
                ),
                Optional.ofNullable(
                        row.get(
                                CONTRACT_IDENTIFIER
                        )
                ).map(identifier ->
                        new BackgroundWorkContractAffinity(
                                new BackgroundWorkContractIdentity(
                                        row.get(
                                                CONTRACT_OWNER_IDENTIFIER
                                        ),
                                        identifier
                                ),
                                row.get(
                                        CONTRACT_SEMANTIC_RELEASE
                                )
                        )
                )
        );
    }

    private WorkAttempt toAttempt(
            Record row
    ) {
        Optional<BackgroundWorkResultClassification>
                classification =
                Optional.ofNullable(
                        row.get(
                                RESULT_CLASSIFICATION
                        )
                ).map(
                        BackgroundWorkResultClassification
                                ::valueOf
                );

        return new WorkAttempt(
                row.get(
                        ATTEMPT_IDENTIFIER
                ),
                row.get(
                        WORK_IDENTIFIER
                ),
                row.get(
                        ATTEMPTED_AT
                ),
                row.get(
                        PRINCIPAL_REFERENCE
                ),
                classification,
                Optional.ofNullable(
                        row.get(
                                EVIDENCE_REFERENCE
                        )
                )
        );
    }

    private static boolean sameAttemptStart(
            WorkAttempt existing,
            String workIdentity,
            Instant attemptedAt,
            String principalReference
    ) {
        return existing
                .workIdentity()
                .equals(
                        workIdentity
                )
                && existing
                .attemptedAt()
                .equals(
                        attemptedAt
                )
                && existing
                .principalReference()
                .equals(
                        principalReference
                );
    }

    private void lockIdentity(
            String kind,
            String identity
    ) {
        dsl.fetch(
                "select pg_advisory_xact_lock("
                        + "hashtextextended(cast(? as text), 0))",
                "background|"
                        + kind
                        + "|"
                        + identity
        );
    }

    private static void requireIdentifier(
            String value,
            String label
    ) {
        if (value == null
                || value.isBlank()) {
            throw new IllegalArgumentException(
                    label
                            + " must not be blank"
            );
        }
    }
}
