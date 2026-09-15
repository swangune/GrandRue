package mainstreet.infrastructure.persistence.onboarding;

import mainstreet.application.MerchantScope;
import mainstreet.onboarding.AcknowledgeInitialConfigurationIntentCommand;
import mainstreet.onboarding.DeterministicOnboardingRecomputation;
import mainstreet.onboarding.InitialConfigurationIntent;
import mainstreet.onboarding.InitialConfigurationIntentIdentity;
import mainstreet.onboarding.InitialOnboardingPromptCatalogue;
import mainstreet.onboarding.OnboardingAnswerEvidence;
import mainstreet.onboarding.OnboardingAnswerEvidenceRevision;
import mainstreet.onboarding.OnboardingAnswerForm;
import mainstreet.onboarding.OnboardingAnswerMutationResult;
import mainstreet.onboarding.OnboardingAnswerOrigin;
import mainstreet.onboarding.OnboardingCase;
import mainstreet.onboarding.OnboardingCaseEvidenceSnapshot;
import mainstreet.onboarding.OnboardingCaseEvidenceStore;
import mainstreet.onboarding.OnboardingCaseIdentity;
import mainstreet.onboarding.OnboardingCaseLifecycle;
import mainstreet.onboarding.OnboardingCasePersistenceException;
import mainstreet.onboarding.OnboardingCasePurpose;
import mainstreet.onboarding.OnboardingCaseReview;
import mainstreet.onboarding.OnboardingCaseRevision;
import mainstreet.onboarding.OnboardingCompletionPolicy;
import mainstreet.onboarding.OnboardingFinalReview;
import mainstreet.onboarding.OnboardingFinalReviewFactory;
import mainstreet.onboarding.OnboardingPersistenceFailureCategory;
import mainstreet.onboarding.OnboardingQuestionDefinitionVersion;
import mainstreet.onboarding.OnboardingQuestionIdentity;
import mainstreet.onboarding.OnboardingPromptKey;
import mainstreet.onboarding.OnboardingRecomputation;
import mainstreet.onboarding.OnboardingSemanticSeed;
import mainstreet.onboarding.OnboardingSubmissionAuthority;
import mainstreet.onboarding.OnboardingSubmissionReadiness;
import mainstreet.onboarding.OnboardingSubmissionReadinessEvaluator;
import mainstreet.onboarding.OnboardingSubmissionRejectedException;
import mainstreet.onboarding.RecordOnboardingAnswerCommand;
import mainstreet.onboarding.StartInitialOnboardingCaseCommand;
import mainstreet.onboarding.SubmitInitialConfigurationIntentCommand;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * PostgreSQL/jOOQ durable ordinary-initial Onboarding Case and answer-evidence
 * authority under MS-PROT-052 v1.2.
 */
public final class JooqOnboardingCaseEvidenceStore
        implements OnboardingCaseEvidenceStore {

    private static final Table<?> CASE =
            DSL.table(DSL.name("onboarding_case"));
    private static final Table<?> START_REQUEST =
            DSL.table(DSL.name("onboarding_start_request"));
    private static final Table<?> CASE_REVISION =
            DSL.table(DSL.name("onboarding_case_revision"));
    private static final Table<?> ANSWER =
            DSL.table(DSL.name("onboarding_answer_evidence"));
    private static final Table<?> EFFECTIVE =
            DSL.table(DSL.name("onboarding_effective_answer"));
    private static final Table<?> INITIAL_INTENT =
            DSL.table(DSL.name("initial_configuration_intent"));
    private static final Table<?> INTENT_SEED = DSL.table(
            DSL.name("initial_configuration_intent_semantic_seed")
    );
    private static final Table<?> INTENT_PROVENANCE = DSL.table(
            DSL.name("initial_configuration_intent_provenance")
    );
    private static final Table<?> INTENT_UNRESOLVED_PROMPT = DSL.table(
            DSL.name("initial_configuration_intent_unresolved_prompt")
    );

    private static final Field<String> CASE_ID =
            DSL.field(DSL.name("onboarding_case_identity"), String.class);
    private static final Field<String> MERCHANT_ID =
            DSL.field(DSL.name("merchant_identifier"), String.class);
    private static final Field<String> PURPOSE =
            DSL.field(DSL.name("purpose"), String.class);
    private static final Field<String> LIFECYCLE =
            DSL.field(DSL.name("lifecycle"), String.class);
    private static final Field<String> CURRENT_REVISION =
            DSL.field(DSL.name("current_revision"), String.class);
    private static final Field<Instant> STARTED_AT =
            DSL.field(DSL.name("started_at"), Instant.class);

    private static final Field<String> START_REQUEST_ID =
            DSL.field(DSL.name("start_request_identifier"), String.class);
    private static final Field<String> REQUESTED_CASE_ID =
            DSL.field(DSL.name("requested_case_identity"), String.class);
    private static final Field<String> INITIAL_REVISION =
            DSL.field(DSL.name("initial_revision"), String.class);
    private static final Field<String> PRINCIPAL =
            DSL.field(DSL.name("principal_reference"), String.class);
    private static final Field<String> ORIGIN =
            DSL.field(DSL.name("origin_identifier"), String.class);
    private static final Field<String> RESOLVED_CASE_ID =
            DSL.field(DSL.name("resolved_case_identity"), String.class);

    private static final Field<String> REVISION_ID =
            DSL.field(DSL.name("revision_identity"), String.class);
    private static final Field<String> PRIOR_REVISION =
            DSL.field(DSL.name("prior_revision_identity"), String.class);
    private static final Field<String> MUTATION_REQUEST_ID =
            DSL.field(DSL.name("mutation_request_identifier"), String.class);
    private static final Field<String> MUTATION_KIND =
            DSL.field(DSL.name("mutation_kind"), String.class);
    private static final Field<String> RESULTING_LIFECYCLE =
            DSL.field(DSL.name("resulting_lifecycle"), String.class);
    private static final Field<Instant> COMMITTED_AT =
            DSL.field(DSL.name("committed_at"), Instant.class);

    private static final Field<Long> ANSWER_SEQUENCE =
            DSL.field(DSL.name("answer_sequence"), Long.class);
    private static final Field<String> ANSWER_EVIDENCE_ID =
            DSL.field(DSL.name("answer_evidence_identifier"), String.class);
    private static final Field<String> QUESTION_NAMESPACE =
            DSL.field(DSL.name("question_namespace"), String.class);
    private static final Field<String> QUESTION_IDENTIFIER =
            DSL.field(DSL.name("question_identifier"), String.class);
    private static final Field<String> QUESTION_VERSION =
            DSL.field(DSL.name("question_definition_version"), String.class);
    private static final Field<String> ANSWER_FORM =
            DSL.field(DSL.name("answer_form"), String.class);
    private static final Field<String[]> ANSWER_OPTIONS =
            DSL.field(DSL.name("answer_option_identifiers"), String[].class);
    private static final Field<String> STRUCTURED_VALUE_REFERENCE =
            DSL.field(DSL.name("structured_value_reference"), String.class);
    private static final Field<String> CONTEXT_SCOPE_REFERENCE =
            DSL.field(DSL.name("context_scope_reference"), String.class);
    private static final Field<String> ANSWER_ORIGIN =
            DSL.field(DSL.name("answer_origin"), String.class);
    private static final Field<String> ANSWERED_BY =
            DSL.field(DSL.name("answered_by"), String.class);
    private static final Field<Instant> ANSWERED_AT =
            DSL.field(DSL.name("answered_at"), Instant.class);
    private static final Field<String> SEMANTIC_REGISTRY_RELEASE =
            DSL.field(DSL.name("semantic_registry_release"), String.class);
    private static final Field<String> SUPERSEDES_ANSWER_ID =
            DSL.field(
                    DSL.name("supersedes_answer_evidence_identifier"),
                    String.class
            );

    private static final Field<Long> EFFECTIVE_POINTER_ID =
            DSL.field(DSL.name("effective_answer_pointer_identity"), Long.class);
    private static final Field<String> INTENT_ID =
            DSL.field(DSL.name("intent_identity"), String.class);
    private static final Field<String> SUBMISSION_REQUEST_ID = DSL.field(
            DSL.name("submission_request_identifier"),
            String.class
    );
    private static final Field<String> SOURCE_CASE_ID = DSL.field(
            DSL.name("source_onboarding_case_identity"),
            String.class
    );
    private static final Field<String> SOURCE_CASE_REVISION = DSL.field(
            DSL.name("source_onboarding_case_revision"),
            String.class
    );
    private static final Field<String> SUBMITTED_CASE_REVISION = DSL.field(
            DSL.name("submitted_case_revision"),
            String.class
    );
    private static final Field<String> SUBMITTED_BY =
            DSL.field(DSL.name("submitted_by"), String.class);
    private static final Field<Instant> SUBMITTED_AT =
            DSL.field(DSL.name("submitted_at"), Instant.class);
    private static final Field<String> SEED_NAMESPACE =
            DSL.field(DSL.name("seed_namespace"), String.class);
    private static final Field<String> SEED_IDENTIFIER =
            DSL.field(DSL.name("seed_identifier"), String.class);
    private static final Field<Integer> PROVENANCE_SEQUENCE = DSL.field(
            DSL.name("provenance_sequence"),
            Integer.class
    );
    private static final Field<String> PROVENANCE_REFERENCE = DSL.field(
            DSL.name("provenance_reference"),
            String.class
    );
    private static final Field<Integer> PROMPT_SEQUENCE =
            DSL.field(DSL.name("prompt_sequence"), Integer.class);

    private final DSLContext dsl;
    private final TransactionTemplate transactionTemplate;
    private final DeterministicOnboardingRecomputation recomputation;
    private final OnboardingSubmissionReadinessEvaluator readinessEvaluator;

    public JooqOnboardingCaseEvidenceStore(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactionTemplate = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
        this.recomputation = new DeterministicOnboardingRecomputation(
                InitialOnboardingPromptCatalogue.registry()
        );
        this.readinessEvaluator = new OnboardingSubmissionReadinessEvaluator(
                new OnboardingFinalReviewFactory()
        );
    }

    @Override
    public OnboardingCase startInitial(
            StartInitialOnboardingCaseCommand command
    ) {
        Objects.requireNonNull(command, "command");
        OnboardingCase result = transactionTemplate.execute(status -> {
            lockStartRequest(command.startRequestIdentifier());
            Optional<StartReceipt> receipt = startReceipt(
                    command.startRequestIdentifier()
            );
            if (receipt.isPresent()) {
                StartReceipt committed = receipt.orElseThrow();
                requireSameStartIntent(command, committed);
                return caseByIdentity(committed.resolvedCaseIdentity())
                        .orElseThrow(() -> new IllegalStateException(
                                "Committed start request references a missing case"
                        ));
            }

            lockMerchant(command.merchantScope().merchantIdentifier());
            OnboardingCase resolved = currentInitial(command.merchantScope())
                    .orElseGet(() -> createInitialCase(command));
            insertStartReceipt(command, resolved.identity());
            return resolved;
        });
        return Objects.requireNonNull(result, "Onboarding start returned no case");
    }

    @Override
    public Optional<OnboardingCase> currentInitial(
            MerchantScope merchantScope
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Record row = selectCaseFields()
                .from(CASE)
                .where(MERCHANT_ID.eq(merchantScope.merchantIdentifier()))
                .and(PURPOSE.eq(OnboardingCasePurpose.INITIAL_CONFIGURATION.name()))
                .and(LIFECYCLE.in(
                        OnboardingCaseLifecycle.IN_PROGRESS.name(),
                        OnboardingCaseLifecycle.SUBMITTED.name()
                ))
                .fetchOne();
        return Optional.ofNullable(row).map(this::toCase);
    }

    @Override
    public Optional<OnboardingCase> caseByIdentity(
            OnboardingCaseIdentity onboardingCaseIdentity
    ) {
        Objects.requireNonNull(onboardingCaseIdentity, "onboardingCaseIdentity");
        Record row = selectCaseFields()
                .from(CASE)
                .where(CASE_ID.eq(onboardingCaseIdentity.value()))
                .fetchOne();
        return Optional.ofNullable(row).map(this::toCase);
    }

    @Override
    public OnboardingAnswerMutationResult recordAnswer(
            RecordOnboardingAnswerCommand command
    ) {
        Objects.requireNonNull(command, "command");
        OnboardingAnswerMutationResult result =
                transactionTemplate.execute(status -> {
                    lockMutationRequest(command.mutationRequestIdentifier());
                    Optional<PersistedAnswerMutation> committed =
                            persistedAnswerMutationByRequest(
                                    command.mutationRequestIdentifier()
                            );
                    if (committed.isPresent()) {
                        PersistedAnswerMutation existing =
                                committed.orElseThrow();
                        requireSameAnswerIntent(command, existing);
                        return existing.result();
                    }

                    lockCase(command.onboardingCaseIdentity().value());
                    OnboardingCase current = caseByIdentity(
                            command.onboardingCaseIdentity()
                    ).orElseThrow(() -> failure(
                            OnboardingPersistenceFailureCategory.CASE_NOT_FOUND,
                            "Onboarding Case does not exist"
                    ));
                    if (current.lifecycle()
                            != OnboardingCaseLifecycle.IN_PROGRESS) {
                        throw failure(
                                OnboardingPersistenceFailureCategory
                                        .CASE_NOT_IN_PROGRESS,
                                "Onboarding Case is not in progress"
                        );
                    }
                    if (!current.currentRevision().equals(
                            command.expectedRevision()
                    )) {
                        throw failure(
                                OnboardingPersistenceFailureCategory
                                        .CASE_REVISION_CONFLICT,
                                "Onboarding Case changed concurrently"
                        );
                    }
                    if (caseRevisionExists(
                            command.onboardingCaseIdentity(),
                            command.replacementRevision()
                    )) {
                        throw failure(
                                OnboardingPersistenceFailureCategory
                                        .REVISION_IDENTITY_CONFLICT,
                                "Replacement Onboarding Case revision already exists"
                        );
                    }
                    if (answerByEvidenceIdentifier(
                            command.answerEvidenceIdentifier()
                    ).isPresent()) {
                        throw failure(
                                OnboardingPersistenceFailureCategory
                                        .ANSWER_EVIDENCE_IDENTITY_CONFLICT,
                                "Answer evidence identity already exists"
                        );
                    }

                    Optional<EffectivePointer> effective =
                            effectivePointer(
                                    command.onboardingCaseIdentity(),
                                    command.answer().questionIdentity(),
                                    command.answer().contextScopeReference()
                            );

                    insertCaseRevision(command);
                    insertAnswer(command, effective.map(
                            EffectivePointer::answerEvidenceIdentifier
                    ));
                    moveEffectivePointer(command, effective);
                    moveCurrentRevision(command);

                    return persistedAnswerMutationByRequest(
                            command.mutationRequestIdentifier()
                    ).orElseThrow(() -> new IllegalStateException(
                            "Onboarding answer mutation was not committed"
                    )).result();
                });
        return Objects.requireNonNull(
                result,
                "Onboarding answer mutation returned no result"
        );
    }

    @Override
    public List<OnboardingAnswerEvidenceRevision> answerHistory(
            OnboardingCaseIdentity onboardingCaseIdentity,
            OnboardingQuestionIdentity questionIdentity,
            Optional<String> contextScopeReference
    ) {
        Objects.requireNonNull(onboardingCaseIdentity, "onboardingCaseIdentity");
        Objects.requireNonNull(questionIdentity, "questionIdentity");
        Objects.requireNonNull(
                contextScopeReference,
                "contextScopeReference"
        );
        return selectAnswerFields()
                .from(ANSWER)
                .where(CASE_ID.eq(onboardingCaseIdentity.value()))
                .and(QUESTION_NAMESPACE.eq(questionIdentity.namespace()))
                .and(QUESTION_IDENTIFIER.eq(questionIdentity.identifier()))
                .and(contextCondition(contextScopeReference))
                .orderBy(ANSWER_SEQUENCE.asc())
                .fetch(this::toAnswerRevision);
    }

    @Override
    public List<OnboardingAnswerEvidenceRevision> effectiveAnswers(
            OnboardingCaseIdentity onboardingCaseIdentity
    ) {
        Objects.requireNonNull(onboardingCaseIdentity, "onboardingCaseIdentity");
        return dsl.select(ANSWER_EVIDENCE_ID)
                .from(EFFECTIVE)
                .where(CASE_ID.eq(onboardingCaseIdentity.value()))
                .orderBy(EFFECTIVE_POINTER_ID.asc())
                .fetch(ANSWER_EVIDENCE_ID)
                .stream()
                .map(identifier -> answerByEvidenceIdentifier(identifier)
                        .orElseThrow(() -> new IllegalStateException(
                                "Effective answer pointer references missing evidence"
                        )))
                .toList();
    }

    @Override
    public OnboardingCaseEvidenceSnapshot evidenceSnapshotAtRevision(
            OnboardingCaseIdentity onboardingCaseIdentity,
            OnboardingCaseRevision revision
    ) {
        Objects.requireNonNull(onboardingCaseIdentity, "onboardingCaseIdentity");
        Objects.requireNonNull(revision, "revision");

        Record caseRow = dsl.select(MERCHANT_ID)
                .from(CASE)
                .where(CASE_ID.eq(onboardingCaseIdentity.value()))
                .fetchOne();
        if (caseRow == null) {
            throw failure(
                    OnboardingPersistenceFailureCategory.CASE_NOT_FOUND,
                    "Onboarding Case does not exist"
            );
        }

        Record revisionRow = dsl.select(RESULTING_LIFECYCLE)
                .from(CASE_REVISION)
                .where(CASE_ID.eq(onboardingCaseIdentity.value()))
                .and(REVISION_ID.eq(revision.value()))
                .fetchOne();
        if (revisionRow == null) {
            throw failure(
                    OnboardingPersistenceFailureCategory
                            .CASE_REVISION_NOT_FOUND,
                    "Onboarding Case revision does not exist"
            );
        }

        List<String> evidenceIdentifiers = dsl.fetch(
                """
                with recursive revision_path as (
                    select revision_identity,
                           prior_revision_identity,
                           0 as revision_depth
                    from onboarding_case_revision
                    where onboarding_case_identity = ?
                      and revision_identity = ?

                    union all

                    select prior.revision_identity,
                           prior.prior_revision_identity,
                           path.revision_depth + 1
                    from onboarding_case_revision prior
                    join revision_path path
                      on prior.onboarding_case_identity = ?
                     and prior.revision_identity =
                             path.prior_revision_identity
                ),
                effective_at_revision as (
                    select distinct on (
                               answer.question_namespace,
                               answer.question_identifier,
                               answer.context_scope_reference
                           )
                           answer.answer_evidence_identifier,
                           answer.question_namespace,
                           answer.question_identifier,
                           answer.context_scope_reference,
                           path.revision_depth
                    from revision_path path
                    join onboarding_answer_evidence answer
                      on answer.onboarding_case_identity = ?
                     and answer.revision_identity =
                             path.revision_identity
                    order by answer.question_namespace,
                             answer.question_identifier,
                             answer.context_scope_reference
                                 nulls first,
                             path.revision_depth
                )
                select answer_evidence_identifier
                from effective_at_revision
                order by question_namespace,
                         question_identifier,
                         context_scope_reference nulls first
                """,
                onboardingCaseIdentity.value(),
                revision.value(),
                onboardingCaseIdentity.value(),
                onboardingCaseIdentity.value()
        ).getValues("answer_evidence_identifier", String.class);

        List<OnboardingAnswerEvidenceRevision> answers =
                evidenceIdentifiers.stream()
                        .map(identifier -> answerByEvidenceIdentifier(
                                identifier
                        ).orElseThrow(() -> new IllegalStateException(
                                "Historical snapshot references missing evidence"
                        )))
                        .toList();

        OnboardingCase caseAtRevision = new OnboardingCase(
                onboardingCaseIdentity,
                new MerchantScope(caseRow.get(MERCHANT_ID)),
                OnboardingCaseLifecycle.valueOf(
                        revisionRow.get(RESULTING_LIFECYCLE)
                ),
                revision
        );
        return new OnboardingCaseEvidenceSnapshot(
                caseAtRevision,
                answers
        );
    }

    @Override
    public InitialConfigurationIntent submitInitialConfigurationIntent(
            SubmitInitialConfigurationIntentCommand command,
            OnboardingCompletionPolicy completionPolicy,
            OnboardingSubmissionAuthority authority
    ) {
        Objects.requireNonNull(command, "command");
        Objects.requireNonNull(completionPolicy, "completionPolicy");
        Objects.requireNonNull(authority, "authority");

        InitialConfigurationIntent result = transactionTemplate.execute(
                status -> {
                    lockMutationRequest(
                            command.submissionRequestIdentifier()
                    );
                    Optional<PersistedSubmission> committed =
                            persistedSubmissionByRequest(
                                    command.submissionRequestIdentifier()
                            );
                    if (committed.isPresent()) {
                        PersistedSubmission existing =
                                committed.orElseThrow();
                        requireSameSubmissionIntent(command, existing);
                        return existing.intent();
                    }
                    if (mutationRequestExists(
                            command.submissionRequestIdentifier()
                    )) {
                        throw failure(
                                OnboardingPersistenceFailureCategory
                                        .REQUEST_IDENTITY_CONFLICT,
                                "Onboarding request identity is already used"
                        );
                    }

                    lockIntent(command.intentIdentity().value());
                    if (initialConfigurationIntentByIdentity(
                            command.intentIdentity()
                    ).isPresent()) {
                        throw failure(
                                OnboardingPersistenceFailureCategory
                                        .INITIAL_CONFIGURATION_INTENT_IDENTITY_CONFLICT,
                                "Initial Configuration Intent identity already exists"
                        );
                    }

                    OnboardingCaseIdentity caseIdentity =
                            command.reviewedIntent()
                                    .affinity()
                                    .onboardingCaseIdentity();
                    lockCase(caseIdentity.value());
                    OnboardingCase current = caseByIdentity(caseIdentity)
                            .orElseThrow(() -> failure(
                                    OnboardingPersistenceFailureCategory
                                            .CASE_NOT_FOUND,
                                    "Onboarding Case does not exist"
                            ));
                    OnboardingCaseEvidenceSnapshot snapshot =
                            evidenceSnapshotAtRevision(
                                    current.identity(),
                                    current.currentRevision()
                            );
                    OnboardingRecomputation currentRecomputation =
                            recomputation.recompute(
                                    snapshot.caseAtRevision(),
                                    snapshot.effectiveAnswers()
                            );
                    OnboardingSubmissionReadiness readiness =
                            readinessEvaluator.evaluate(
                                    command.reviewedIntent(),
                                    currentRecomputation,
                                    completionPolicy,
                                    authority
                            );
                    if (!readiness.isReady()) {
                        throw new OnboardingSubmissionRejectedException(
                                readiness.blockers()
                        );
                    }
                    if (caseRevisionExists(
                            caseIdentity,
                            command.submittedCaseRevision()
                    )) {
                        throw failure(
                                OnboardingPersistenceFailureCategory
                                        .REVISION_IDENTITY_CONFLICT,
                                "Submitted Onboarding Case revision already exists"
                        );
                    }

                    insertSubmissionCaseRevision(command);
                    InitialConfigurationIntent intent =
                            new InitialConfigurationIntent(
                                    command.intentIdentity(),
                                    current.merchantScope(),
                                    command.reviewedIntent(),
                                    command.principalReference(),
                                    command.originIdentifier(),
                                    command.submittedAt()
                            );
                    insertInitialConfigurationIntent(
                            command,
                            intent
                    );
                    moveCaseToSubmitted(command);
                    return intent;
                }
        );
        return Objects.requireNonNull(
                result,
                "Onboarding submission returned no intent"
        );
    }

    @Override
    public Optional<InitialConfigurationIntent>
            initialConfigurationIntentByIdentity(
                    InitialConfigurationIntentIdentity intentIdentity
            ) {
        Objects.requireNonNull(intentIdentity, "intentIdentity");
        Record row = selectInitialIntentFields()
                .from(INITIAL_INTENT)
                .where(INTENT_ID.eq(intentIdentity.value()))
                .fetchOne();
        return Optional.ofNullable(row)
                .map(this::toInitialConfigurationIntent);
    }

    @Override
    public OnboardingCase acknowledgeInitialConfigurationIntent(
            AcknowledgeInitialConfigurationIntentCommand command
    ) {
        Objects.requireNonNull(command, "command");
        OnboardingCase result = transactionTemplate.execute(status -> {
            lockMutationRequest(
                    command.acknowledgementRequestIdentifier()
            );
            lockIntent(command.intentIdentity().value());
            Record intentRow = selectInitialIntentFields()
                    .from(INITIAL_INTENT)
                    .where(INTENT_ID.eq(command.intentIdentity().value()))
                    .fetchOne();
            if (intentRow == null) {
                throw failure(
                        OnboardingPersistenceFailureCategory
                                .INITIAL_CONFIGURATION_INTENT_NOT_FOUND,
                        "Initial Configuration Intent does not exist"
                );
            }

            OnboardingCaseIdentity caseIdentity =
                    new OnboardingCaseIdentity(intentRow.get(SOURCE_CASE_ID));
            OnboardingCaseRevision submittedRevision =
                    new OnboardingCaseRevision(
                            intentRow.get(SUBMITTED_CASE_REVISION)
                    );
            lockCase(caseIdentity.value());
            OnboardingCase current = caseByIdentity(caseIdentity)
                    .orElseThrow(() -> failure(
                            OnboardingPersistenceFailureCategory.CASE_NOT_FOUND,
                            "Source Onboarding Case does not exist"
                    ));

            if (current.lifecycle() == OnboardingCaseLifecycle.COMPLETED) {
                requireSameAcknowledgement(
                        command,
                        caseIdentity,
                        submittedRevision,
                        current
                );
                return current;
            }
            if (mutationRequestExists(
                    command.acknowledgementRequestIdentifier()
            )) {
                throw failure(
                        OnboardingPersistenceFailureCategory
                                .REQUEST_IDENTITY_CONFLICT,
                        "Onboarding acknowledgement request is already used"
                );
            }
            if (current.lifecycle() != OnboardingCaseLifecycle.SUBMITTED
                    || !current.currentRevision().equals(submittedRevision)) {
                throw failure(
                        OnboardingPersistenceFailureCategory
                                .CASE_REVISION_CONFLICT,
                        "Onboarding Case is not at the submitted intent revision"
                );
            }
            if (command.completionCaseRevision().equals(submittedRevision)
                    || caseRevisionExists(
                            caseIdentity,
                            command.completionCaseRevision()
                    )) {
                throw failure(
                        OnboardingPersistenceFailureCategory
                                .REVISION_IDENTITY_CONFLICT,
                        "Completion Onboarding Case revision already exists"
                );
            }

            insertCompletionCaseRevision(
                    command,
                    caseIdentity,
                    submittedRevision
            );
            moveCaseToCompleted(
                    command,
                    caseIdentity,
                    submittedRevision
            );
            return caseByIdentity(caseIdentity).orElseThrow();
        });
        return Objects.requireNonNull(
                result,
                "Onboarding acknowledgement returned no case"
        );
    }

    private OnboardingCase createInitialCase(
            StartInitialOnboardingCaseCommand command
    ) {
        if (caseByIdentity(command.requestedCaseIdentity()).isPresent()) {
            throw failure(
                    OnboardingPersistenceFailureCategory.CASE_IDENTITY_CONFLICT,
                    "Requested Onboarding Case identity already exists"
            );
        }
        dsl.insertInto(CASE)
                .columns(
                        CASE_ID,
                        MERCHANT_ID,
                        PURPOSE,
                        LIFECYCLE,
                        CURRENT_REVISION,
                        STARTED_AT
                )
                .values(
                        command.requestedCaseIdentity().value(),
                        command.merchantScope().merchantIdentifier(),
                        OnboardingCasePurpose.INITIAL_CONFIGURATION.name(),
                        OnboardingCaseLifecycle.IN_PROGRESS.name(),
                        command.initialRevision().value(),
                        command.startedAt()
                )
                .execute();
        dsl.insertInto(CASE_REVISION)
                .columns(
                        CASE_ID,
                        REVISION_ID,
                        PRIOR_REVISION,
                        MUTATION_REQUEST_ID,
                        MUTATION_KIND,
                        RESULTING_LIFECYCLE,
                        PRINCIPAL,
                        ORIGIN,
                        COMMITTED_AT
                )
                .values(
                        command.requestedCaseIdentity().value(),
                        command.initialRevision().value(),
                        null,
                        command.startRequestIdentifier(),
                        "START",
                        OnboardingCaseLifecycle.IN_PROGRESS.name(),
                        command.principalReference(),
                        command.originIdentifier().orElse(null),
                        command.startedAt()
                )
                .execute();
        return caseByIdentity(command.requestedCaseIdentity()).orElseThrow();
    }

    private void insertStartReceipt(
            StartInitialOnboardingCaseCommand command,
            OnboardingCaseIdentity resolvedCaseIdentity
    ) {
        dsl.insertInto(START_REQUEST)
                .columns(
                        START_REQUEST_ID,
                        REQUESTED_CASE_ID,
                        MERCHANT_ID,
                        INITIAL_REVISION,
                        PRINCIPAL,
                        ORIGIN,
                        STARTED_AT,
                        RESOLVED_CASE_ID
                )
                .values(
                        command.startRequestIdentifier(),
                        command.requestedCaseIdentity().value(),
                        command.merchantScope().merchantIdentifier(),
                        command.initialRevision().value(),
                        command.principalReference(),
                        command.originIdentifier().orElse(null),
                        command.startedAt(),
                        resolvedCaseIdentity.value()
                )
                .execute();
    }

    private Optional<StartReceipt> startReceipt(String requestIdentifier) {
        Record row = dsl.select(
                        START_REQUEST_ID,
                        REQUESTED_CASE_ID,
                        MERCHANT_ID,
                        INITIAL_REVISION,
                        PRINCIPAL,
                        ORIGIN,
                        STARTED_AT,
                        RESOLVED_CASE_ID
                )
                .from(START_REQUEST)
                .where(START_REQUEST_ID.eq(requestIdentifier))
                .fetchOne();
        return Optional.ofNullable(row).map(record -> new StartReceipt(
                record.get(START_REQUEST_ID),
                new OnboardingCaseIdentity(record.get(REQUESTED_CASE_ID)),
                new MerchantScope(record.get(MERCHANT_ID)),
                new OnboardingCaseRevision(record.get(INITIAL_REVISION)),
                record.get(PRINCIPAL),
                Optional.ofNullable(record.get(ORIGIN)),
                record.get(STARTED_AT),
                new OnboardingCaseIdentity(record.get(RESOLVED_CASE_ID))
        ));
    }

    private Optional<PersistedAnswerMutation>
            persistedAnswerMutationByRequest(String requestIdentifier) {
        Record revision = dsl.select(
                        CASE_ID,
                        REVISION_ID,
                        PRIOR_REVISION,
                        MUTATION_REQUEST_ID,
                        MUTATION_KIND,
                        RESULTING_LIFECYCLE,
                        PRINCIPAL,
                        ORIGIN,
                        COMMITTED_AT
                )
                .from(CASE_REVISION)
                .where(MUTATION_REQUEST_ID.eq(requestIdentifier))
                .and(MUTATION_KIND.eq("ANSWER"))
                .fetchOne();
        if (revision == null) {
            return Optional.empty();
        }
        OnboardingCaseIdentity caseIdentity = new OnboardingCaseIdentity(
                revision.get(CASE_ID)
        );
        Record caseRow = dsl.select(MERCHANT_ID)
                .from(CASE)
                .where(CASE_ID.eq(caseIdentity.value()))
                .fetchOne();
        if (caseRow == null) {
            throw new IllegalStateException(
                    "Answer revision references a missing Onboarding Case"
            );
        }
        OnboardingCase caseAtCommit = new OnboardingCase(
                caseIdentity,
                new MerchantScope(caseRow.get(MERCHANT_ID)),
                OnboardingCaseLifecycle.valueOf(
                        revision.get(RESULTING_LIFECYCLE)
                ),
                new OnboardingCaseRevision(revision.get(REVISION_ID))
        );
        OnboardingAnswerEvidenceRevision answer =
                answerByCaseRevision(
                        caseIdentity,
                        caseAtCommit.currentRevision()
                ).orElseThrow(() -> new IllegalStateException(
                        "Answer mutation revision has no answer evidence"
                ));
        OnboardingAnswerMutationResult result =
                new OnboardingAnswerMutationResult(caseAtCommit, answer);
        return Optional.of(new PersistedAnswerMutation(
                result,
                new OnboardingCaseRevision(revision.get(PRIOR_REVISION)),
                revision.get(PRINCIPAL),
                Optional.ofNullable(revision.get(ORIGIN)),
                revision.get(COMMITTED_AT)
        ));
    }

    private Optional<OnboardingAnswerEvidenceRevision> answerByCaseRevision(
            OnboardingCaseIdentity caseIdentity,
            OnboardingCaseRevision revision
    ) {
        Record row = selectAnswerFields()
                .from(ANSWER)
                .where(CASE_ID.eq(caseIdentity.value()))
                .and(REVISION_ID.eq(revision.value()))
                .fetchOne();
        return Optional.ofNullable(row).map(this::toAnswerRevision);
    }

    private Optional<OnboardingAnswerEvidenceRevision>
            answerByEvidenceIdentifier(String evidenceIdentifier) {
        Record row = selectAnswerFields()
                .from(ANSWER)
                .where(ANSWER_EVIDENCE_ID.eq(evidenceIdentifier))
                .fetchOne();
        return Optional.ofNullable(row).map(this::toAnswerRevision);
    }

    private Optional<EffectivePointer> effectivePointer(
            OnboardingCaseIdentity caseIdentity,
            OnboardingQuestionIdentity questionIdentity,
            Optional<String> contextScopeReference
    ) {
        Record row = dsl.select(
                        EFFECTIVE_POINTER_ID,
                        ANSWER_EVIDENCE_ID
                )
                .from(EFFECTIVE)
                .where(CASE_ID.eq(caseIdentity.value()))
                .and(QUESTION_NAMESPACE.eq(questionIdentity.namespace()))
                .and(QUESTION_IDENTIFIER.eq(questionIdentity.identifier()))
                .and(contextCondition(contextScopeReference))
                .fetchOne();
        return Optional.ofNullable(row).map(record -> new EffectivePointer(
                record.get(EFFECTIVE_POINTER_ID),
                record.get(ANSWER_EVIDENCE_ID)
        ));
    }

    private void insertCaseRevision(
            RecordOnboardingAnswerCommand command
    ) {
        dsl.insertInto(CASE_REVISION)
                .columns(
                        CASE_ID,
                        REVISION_ID,
                        PRIOR_REVISION,
                        MUTATION_REQUEST_ID,
                        MUTATION_KIND,
                        RESULTING_LIFECYCLE,
                        PRINCIPAL,
                        ORIGIN,
                        COMMITTED_AT
                )
                .values(
                        command.onboardingCaseIdentity().value(),
                        command.replacementRevision().value(),
                        command.expectedRevision().value(),
                        command.mutationRequestIdentifier(),
                        "ANSWER",
                        OnboardingCaseLifecycle.IN_PROGRESS.name(),
                        command.principalReference(),
                        command.originIdentifier().orElse(null),
                        command.committedAt()
                )
                .execute();
    }

    private void insertAnswer(
            RecordOnboardingAnswerCommand command,
            Optional<String> supersededEvidenceIdentifier
    ) {
        OnboardingAnswerEvidence answer = command.answer();
        dsl.insertInto(ANSWER)
                .columns(
                        ANSWER_EVIDENCE_ID,
                        CASE_ID,
                        REVISION_ID,
                        QUESTION_NAMESPACE,
                        QUESTION_IDENTIFIER,
                        QUESTION_VERSION,
                        ANSWER_FORM,
                        ANSWER_OPTIONS,
                        STRUCTURED_VALUE_REFERENCE,
                        CONTEXT_SCOPE_REFERENCE,
                        ANSWER_ORIGIN,
                        ANSWERED_BY,
                        ANSWERED_AT,
                        SEMANTIC_REGISTRY_RELEASE,
                        SUPERSEDES_ANSWER_ID
                )
                .values(
                        command.answerEvidenceIdentifier(),
                        command.onboardingCaseIdentity().value(),
                        command.replacementRevision().value(),
                        answer.questionIdentity().namespace(),
                        answer.questionIdentity().identifier(),
                        answer.questionDefinitionVersion().value(),
                        answer.answerForm().name(),
                        answer.answerOptionIdentifiers().toArray(String[]::new),
                        answer.structuredValueReference().orElse(null),
                        answer.contextScopeReference().orElse(null),
                        answer.answerOrigin().name(),
                        answer.answeredBy(),
                        answer.answeredAt(),
                        answer.semanticRegistryRelease().orElse(null),
                        supersededEvidenceIdentifier.orElse(null)
                )
                .execute();
    }

    private void moveEffectivePointer(
            RecordOnboardingAnswerCommand command,
            Optional<EffectivePointer> effective
    ) {
        OnboardingAnswerEvidence answer = command.answer();
        if (effective.isPresent()) {
            int updated = dsl.update(EFFECTIVE)
                    .set(
                            ANSWER_EVIDENCE_ID,
                            command.answerEvidenceIdentifier()
                    )
                    .where(EFFECTIVE_POINTER_ID.eq(
                            effective.orElseThrow().pointerIdentity()
                    ))
                    .execute();
            if (updated != 1) {
                throw new IllegalStateException(
                        "Effective onboarding answer pointer changed concurrently"
                );
            }
            return;
        }

        dsl.insertInto(EFFECTIVE)
                .columns(
                        CASE_ID,
                        QUESTION_NAMESPACE,
                        QUESTION_IDENTIFIER,
                        CONTEXT_SCOPE_REFERENCE,
                        ANSWER_EVIDENCE_ID
                )
                .values(
                        command.onboardingCaseIdentity().value(),
                        answer.questionIdentity().namespace(),
                        answer.questionIdentity().identifier(),
                        answer.contextScopeReference().orElse(null),
                        command.answerEvidenceIdentifier()
                )
                .execute();
    }

    private void moveCurrentRevision(
            RecordOnboardingAnswerCommand command
    ) {
        int updated = dsl.update(CASE)
                .set(
                        CURRENT_REVISION,
                        command.replacementRevision().value()
                )
                .where(CASE_ID.eq(command.onboardingCaseIdentity().value()))
                .and(CURRENT_REVISION.eq(
                        command.expectedRevision().value()
                ))
                .execute();
        if (updated != 1) {
            throw failure(
                    OnboardingPersistenceFailureCategory
                            .CASE_REVISION_CONFLICT,
                    "Onboarding Case changed concurrently"
            );
        }
    }

    private Optional<PersistedSubmission> persistedSubmissionByRequest(
            String requestIdentifier
    ) {
        Record row = selectInitialIntentFields()
                .from(INITIAL_INTENT)
                .where(SUBMISSION_REQUEST_ID.eq(requestIdentifier))
                .fetchOne();
        if (row == null) {
            return Optional.empty();
        }
        return Optional.of(new PersistedSubmission(
                toInitialConfigurationIntent(row),
                row.get(SUBMISSION_REQUEST_ID),
                new OnboardingCaseRevision(
                        row.get(SUBMITTED_CASE_REVISION)
                )
        ));
    }

    private boolean mutationRequestExists(String requestIdentifier) {
        return dsl.fetchExists(
                DSL.selectOne()
                        .from(CASE_REVISION)
                        .where(MUTATION_REQUEST_ID.eq(requestIdentifier))
        );
    }

    private void insertSubmissionCaseRevision(
            SubmitInitialConfigurationIntentCommand command
    ) {
        OnboardingCaseReview review =
                command.reviewedIntent().affinity();
        dsl.insertInto(CASE_REVISION)
                .columns(
                        CASE_ID,
                        REVISION_ID,
                        PRIOR_REVISION,
                        MUTATION_REQUEST_ID,
                        MUTATION_KIND,
                        RESULTING_LIFECYCLE,
                        PRINCIPAL,
                        ORIGIN,
                        COMMITTED_AT
                )
                .values(
                        review.onboardingCaseIdentity().value(),
                        command.submittedCaseRevision().value(),
                        review.reviewedRevision().value(),
                        command.submissionRequestIdentifier(),
                        "SUBMIT",
                        OnboardingCaseLifecycle.SUBMITTED.name(),
                        command.principalReference(),
                        command.originIdentifier().orElse(null),
                        command.submittedAt()
                )
                .execute();
    }

    private void insertInitialConfigurationIntent(
            SubmitInitialConfigurationIntentCommand command,
            InitialConfigurationIntent intent
    ) {
        OnboardingFinalReview review = intent.merchantReviewEvidence();
        dsl.insertInto(INITIAL_INTENT)
                .columns(
                        INTENT_ID,
                        SUBMISSION_REQUEST_ID,
                        MERCHANT_ID,
                        SOURCE_CASE_ID,
                        SOURCE_CASE_REVISION,
                        SUBMITTED_CASE_REVISION,
                        SUBMITTED_BY,
                        ORIGIN,
                        SUBMITTED_AT
                )
                .values(
                        intent.identity().value(),
                        command.submissionRequestIdentifier(),
                        intent.merchantScope().merchantIdentifier(),
                        intent.sourceOnboardingCaseIdentity().value(),
                        intent.sourceOnboardingCaseRevision().value(),
                        command.submittedCaseRevision().value(),
                        intent.submittedBy(),
                        intent.originIdentifier().orElse(null),
                        intent.submittedAt()
                )
                .execute();

        review.candidateRegisteredIntent().stream()
                .sorted(Comparator
                        .comparing(OnboardingSemanticSeed::namespace)
                        .thenComparing(OnboardingSemanticSeed::identifier))
                .forEach(seed -> dsl.insertInto(INTENT_SEED)
                        .columns(INTENT_ID, SEED_NAMESPACE, SEED_IDENTIFIER)
                        .values(
                                intent.identity().value(),
                                seed.namespace(),
                                seed.identifier()
                        )
                        .execute());

        for (int index = 0;
                index < review.provenanceReferences().size();
                index++) {
            dsl.insertInto(INTENT_PROVENANCE)
                    .columns(
                            INTENT_ID,
                            PROVENANCE_SEQUENCE,
                            PROVENANCE_REFERENCE
                    )
                    .values(
                            intent.identity().value(),
                            index,
                            review.provenanceReferences().get(index)
                    )
                    .execute();
        }

        for (int index = 0;
                index < review.unresolvedPromptKeys().size();
                index++) {
            OnboardingPromptKey key =
                    review.unresolvedPromptKeys().get(index);
            dsl.insertInto(INTENT_UNRESOLVED_PROMPT)
                    .columns(
                            INTENT_ID,
                            PROMPT_SEQUENCE,
                            QUESTION_NAMESPACE,
                            QUESTION_IDENTIFIER,
                            CONTEXT_SCOPE_REFERENCE
                    )
                    .values(
                            intent.identity().value(),
                            index,
                            key.questionIdentity().namespace(),
                            key.questionIdentity().identifier(),
                            key.contextScopeReference().orElse(null)
                    )
                    .execute();
        }
    }

    private void moveCaseToSubmitted(
            SubmitInitialConfigurationIntentCommand command
    ) {
        OnboardingCaseReview review =
                command.reviewedIntent().affinity();
        int updated = dsl.update(CASE)
                .set(LIFECYCLE, OnboardingCaseLifecycle.SUBMITTED.name())
                .set(
                        CURRENT_REVISION,
                        command.submittedCaseRevision().value()
                )
                .where(CASE_ID.eq(
                        review.onboardingCaseIdentity().value()
                ))
                .and(CURRENT_REVISION.eq(
                        review.reviewedRevision().value()
                ))
                .and(LIFECYCLE.eq(
                        OnboardingCaseLifecycle.IN_PROGRESS.name()
                ))
                .execute();
        if (updated != 1) {
            throw failure(
                    OnboardingPersistenceFailureCategory
                            .CASE_REVISION_CONFLICT,
                    "Onboarding Case changed during submission"
            );
        }
    }

    private void insertCompletionCaseRevision(
            AcknowledgeInitialConfigurationIntentCommand command,
            OnboardingCaseIdentity caseIdentity,
            OnboardingCaseRevision submittedRevision
    ) {
        dsl.insertInto(CASE_REVISION)
                .columns(
                        CASE_ID,
                        REVISION_ID,
                        PRIOR_REVISION,
                        MUTATION_REQUEST_ID,
                        MUTATION_KIND,
                        RESULTING_LIFECYCLE,
                        PRINCIPAL,
                        ORIGIN,
                        COMMITTED_AT
                )
                .values(
                        caseIdentity.value(),
                        command.completionCaseRevision().value(),
                        submittedRevision.value(),
                        command.acknowledgementRequestIdentifier(),
                        "COMPLETE",
                        OnboardingCaseLifecycle.COMPLETED.name(),
                        command.principalReference(),
                        command.originIdentifier().orElse(null),
                        command.acknowledgedAt()
                )
                .execute();
    }

    private void moveCaseToCompleted(
            AcknowledgeInitialConfigurationIntentCommand command,
            OnboardingCaseIdentity caseIdentity,
            OnboardingCaseRevision submittedRevision
    ) {
        int updated = dsl.update(CASE)
                .set(LIFECYCLE, OnboardingCaseLifecycle.COMPLETED.name())
                .set(
                        CURRENT_REVISION,
                        command.completionCaseRevision().value()
                )
                .where(CASE_ID.eq(caseIdentity.value()))
                .and(CURRENT_REVISION.eq(submittedRevision.value()))
                .and(LIFECYCLE.eq(
                        OnboardingCaseLifecycle.SUBMITTED.name()
                ))
                .execute();
        if (updated != 1) {
            throw failure(
                    OnboardingPersistenceFailureCategory
                            .CASE_REVISION_CONFLICT,
                    "Onboarding Case changed during handoff acknowledgement"
            );
        }
    }

    private boolean caseRevisionExists(
            OnboardingCaseIdentity caseIdentity,
            OnboardingCaseRevision revision
    ) {
        return dsl.fetchExists(
                DSL.selectOne()
                        .from(CASE_REVISION)
                        .where(CASE_ID.eq(caseIdentity.value()))
                        .and(REVISION_ID.eq(revision.value()))
        );
    }

    private org.jooq.SelectSelectStep<? extends Record> selectCaseFields() {
        return dsl.select(
                CASE_ID,
                MERCHANT_ID,
                PURPOSE,
                LIFECYCLE,
                CURRENT_REVISION
        );
    }

    private org.jooq.SelectSelectStep<? extends Record> selectAnswerFields() {
        return dsl.select(
                ANSWER_SEQUENCE,
                ANSWER_EVIDENCE_ID,
                CASE_ID,
                REVISION_ID,
                QUESTION_NAMESPACE,
                QUESTION_IDENTIFIER,
                QUESTION_VERSION,
                ANSWER_FORM,
                ANSWER_OPTIONS,
                STRUCTURED_VALUE_REFERENCE,
                CONTEXT_SCOPE_REFERENCE,
                ANSWER_ORIGIN,
                ANSWERED_BY,
                ANSWERED_AT,
                SEMANTIC_REGISTRY_RELEASE,
                SUPERSEDES_ANSWER_ID
        );
    }

    private org.jooq.SelectSelectStep<? extends Record>
            selectInitialIntentFields() {
        return dsl.select(
                INTENT_ID,
                SUBMISSION_REQUEST_ID,
                MERCHANT_ID,
                SOURCE_CASE_ID,
                SOURCE_CASE_REVISION,
                SUBMITTED_CASE_REVISION,
                SUBMITTED_BY,
                ORIGIN,
                SUBMITTED_AT
        );
    }

    private OnboardingCase toCase(Record row) {
        return new OnboardingCase(
                new OnboardingCaseIdentity(row.get(CASE_ID)),
                new MerchantScope(row.get(MERCHANT_ID)),
                OnboardingCaseLifecycle.valueOf(row.get(LIFECYCLE)),
                new OnboardingCaseRevision(row.get(CURRENT_REVISION))
        );
    }

    private OnboardingAnswerEvidenceRevision toAnswerRevision(Record row) {
        String[] options = row.get(ANSWER_OPTIONS);
        OnboardingAnswerEvidence answer = new OnboardingAnswerEvidence(
                new OnboardingQuestionIdentity(
                        row.get(QUESTION_NAMESPACE),
                        row.get(QUESTION_IDENTIFIER)
                ),
                new OnboardingQuestionDefinitionVersion(
                        row.get(QUESTION_VERSION)
                ),
                OnboardingAnswerForm.valueOf(row.get(ANSWER_FORM)),
                Set.copyOf(Arrays.asList(options)),
                Optional.ofNullable(row.get(STRUCTURED_VALUE_REFERENCE)),
                Optional.ofNullable(row.get(CONTEXT_SCOPE_REFERENCE)),
                OnboardingAnswerOrigin.valueOf(row.get(ANSWER_ORIGIN)),
                row.get(ANSWERED_BY),
                row.get(ANSWERED_AT),
                Optional.ofNullable(row.get(SEMANTIC_REGISTRY_RELEASE))
        );
        return new OnboardingAnswerEvidenceRevision(
                row.get(ANSWER_EVIDENCE_ID),
                new OnboardingCaseIdentity(row.get(CASE_ID)),
                new OnboardingCaseRevision(row.get(REVISION_ID)),
                Optional.ofNullable(row.get(SUPERSEDES_ANSWER_ID)),
                answer
        );
    }

    private InitialConfigurationIntent toInitialConfigurationIntent(
            Record row
    ) {
        String intentIdentity = row.get(INTENT_ID);
        Set<OnboardingSemanticSeed> seeds = new LinkedHashSet<>(
                dsl.select(SEED_NAMESPACE, SEED_IDENTIFIER)
                        .from(INTENT_SEED)
                        .where(INTENT_ID.eq(intentIdentity))
                        .orderBy(SEED_NAMESPACE.asc(), SEED_IDENTIFIER.asc())
                        .fetch(record -> new OnboardingSemanticSeed(
                                record.get(SEED_NAMESPACE),
                                record.get(SEED_IDENTIFIER)
                        ))
        );
        List<String> provenance = dsl.select(PROVENANCE_REFERENCE)
                .from(INTENT_PROVENANCE)
                .where(INTENT_ID.eq(intentIdentity))
                .orderBy(PROVENANCE_SEQUENCE.asc())
                .fetch(PROVENANCE_REFERENCE);
        List<OnboardingPromptKey> unresolved = dsl.select(
                        QUESTION_NAMESPACE,
                        QUESTION_IDENTIFIER,
                        CONTEXT_SCOPE_REFERENCE
                )
                .from(INTENT_UNRESOLVED_PROMPT)
                .where(INTENT_ID.eq(intentIdentity))
                .orderBy(PROMPT_SEQUENCE.asc())
                .fetch(record -> new OnboardingPromptKey(
                        new OnboardingQuestionIdentity(
                                record.get(QUESTION_NAMESPACE),
                                record.get(QUESTION_IDENTIFIER)
                        ),
                        Optional.ofNullable(
                                record.get(CONTEXT_SCOPE_REFERENCE)
                        )
                ));
        OnboardingFinalReview review = new OnboardingFinalReview(
                new OnboardingCaseReview(
                        new OnboardingCaseIdentity(row.get(SOURCE_CASE_ID)),
                        new OnboardingCaseRevision(
                                row.get(SOURCE_CASE_REVISION)
                        )
                ),
                seeds,
                provenance,
                unresolved
        );
        return new InitialConfigurationIntent(
                new InitialConfigurationIntentIdentity(intentIdentity),
                new MerchantScope(row.get(MERCHANT_ID)),
                review,
                row.get(SUBMITTED_BY),
                Optional.ofNullable(row.get(ORIGIN)),
                row.get(SUBMITTED_AT)
        );
    }

    private Condition contextCondition(Optional<String> context) {
        return context
                .<Condition>map(CONTEXT_SCOPE_REFERENCE::eq)
                .orElseGet(CONTEXT_SCOPE_REFERENCE::isNull);
    }

    private void lockStartRequest(String requestIdentifier) {
        advisoryLock("onboarding-start-request|" + requestIdentifier, 61);
    }

    private void lockMutationRequest(String requestIdentifier) {
        advisoryLock("onboarding-mutation-request|" + requestIdentifier, 62);
    }

    private void lockMerchant(String merchantIdentifier) {
        advisoryLock("onboarding-merchant|" + merchantIdentifier, 63);
    }

    private void lockCase(String caseIdentity) {
        advisoryLock("onboarding-case|" + caseIdentity, 64);
    }

    private void lockIntent(String intentIdentity) {
        advisoryLock("onboarding-initial-intent|" + intentIdentity, 65);
    }

    private void advisoryLock(String value, int seed) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), ?))",
                value,
                seed
        );
    }

    private static void requireSameStartIntent(
            StartInitialOnboardingCaseCommand command,
            StartReceipt receipt
    ) {
        boolean same = command.startRequestIdentifier().equals(
                        receipt.requestIdentifier()
                )
                && command.requestedCaseIdentity().equals(
                        receipt.requestedCaseIdentity()
                )
                && command.merchantScope().equals(receipt.merchantScope())
                && command.initialRevision().equals(receipt.initialRevision())
                && command.principalReference().equals(
                        receipt.principalReference()
                )
                && command.originIdentifier().equals(
                        receipt.originIdentifier()
                )
                && command.startedAt().equals(receipt.startedAt());
        if (!same) {
            throw failure(
                    OnboardingPersistenceFailureCategory
                            .REQUEST_IDENTITY_CONFLICT,
                    "Onboarding start request identity is bound to different intent"
            );
        }
    }

    private static void requireSameAnswerIntent(
            RecordOnboardingAnswerCommand command,
            PersistedAnswerMutation committed
    ) {
        OnboardingCase caseAtCommit = committed.result().caseAtCommit();
        OnboardingAnswerEvidenceRevision evidence =
                committed.result().answerEvidence();
        boolean same = command.onboardingCaseIdentity().equals(
                        caseAtCommit.identity()
                )
                && command.expectedRevision().equals(
                        committed.priorRevision()
                )
                && command.replacementRevision().equals(
                        caseAtCommit.currentRevision()
                )
                && command.answerEvidenceIdentifier().equals(
                        evidence.answerEvidenceIdentifier()
                )
                && command.answer().equals(evidence.answer())
                && command.principalReference().equals(
                        committed.principalReference()
                )
                && command.originIdentifier().equals(
                        committed.originIdentifier()
                )
                && command.committedAt().equals(committed.committedAt());

        if (!same) {
            throw failure(
                    OnboardingPersistenceFailureCategory
                            .REQUEST_IDENTITY_CONFLICT,
                    "Onboarding mutation request identity is bound to different intent"
            );
        }
    }

    private static void requireSameSubmissionIntent(
            SubmitInitialConfigurationIntentCommand command,
            PersistedSubmission committed
    ) {
        InitialConfigurationIntent intent = committed.intent();
        boolean same = command.submissionRequestIdentifier().equals(
                        committed.requestIdentifier()
                )
                && command.intentIdentity().equals(intent.identity())
                && command.reviewedIntent().equals(
                        intent.merchantReviewEvidence()
                )
                && command.submittedCaseRevision().equals(
                        committed.submittedCaseRevision()
                )
                && command.principalReference().equals(
                        intent.submittedBy()
                )
                && command.originIdentifier().equals(
                        intent.originIdentifier()
                )
                && command.submittedAt().equals(intent.submittedAt());
        if (!same) {
            throw failure(
                    OnboardingPersistenceFailureCategory
                            .REQUEST_IDENTITY_CONFLICT,
                    "Onboarding submission request identity is bound to different intent"
            );
        }
    }

    private void requireSameAcknowledgement(
            AcknowledgeInitialConfigurationIntentCommand command,
            OnboardingCaseIdentity caseIdentity,
            OnboardingCaseRevision submittedRevision,
            OnboardingCase current
    ) {
        Record row = dsl.select(
                        PRIOR_REVISION,
                        MUTATION_REQUEST_ID,
                        MUTATION_KIND,
                        RESULTING_LIFECYCLE,
                        PRINCIPAL,
                        ORIGIN,
                        COMMITTED_AT
                )
                .from(CASE_REVISION)
                .where(CASE_ID.eq(caseIdentity.value()))
                .and(REVISION_ID.eq(
                        command.completionCaseRevision().value()
                ))
                .fetchOne();
        boolean same = current.currentRevision().equals(
                        command.completionCaseRevision()
                )
                && row != null
                && submittedRevision.value().equals(row.get(PRIOR_REVISION))
                && command.acknowledgementRequestIdentifier().equals(
                        row.get(MUTATION_REQUEST_ID)
                )
                && "COMPLETE".equals(row.get(MUTATION_KIND))
                && OnboardingCaseLifecycle.COMPLETED.name().equals(
                        row.get(RESULTING_LIFECYCLE)
                )
                && command.principalReference().equals(row.get(PRINCIPAL))
                && command.originIdentifier().equals(
                        Optional.ofNullable(row.get(ORIGIN))
                )
                && command.acknowledgedAt().equals(row.get(COMMITTED_AT));
        if (!same) {
            throw failure(
                    OnboardingPersistenceFailureCategory
                            .REQUEST_IDENTITY_CONFLICT,
                    "Initial Configuration Intent acknowledgement differs from committed outcome"
            );
        }
    }

    private static OnboardingCasePersistenceException failure(
            OnboardingPersistenceFailureCategory category,
            String message
    ) {
        return new OnboardingCasePersistenceException(category, message);
    }

    private record PersistedAnswerMutation(
            OnboardingAnswerMutationResult result,
            OnboardingCaseRevision priorRevision,
            String principalReference,
            Optional<String> originIdentifier,
            Instant committedAt
    ) {
    }

    private record StartReceipt(
            String requestIdentifier,
            OnboardingCaseIdentity requestedCaseIdentity,
            MerchantScope merchantScope,
            OnboardingCaseRevision initialRevision,
            String principalReference,
            Optional<String> originIdentifier,
            Instant startedAt,
            OnboardingCaseIdentity resolvedCaseIdentity
    ) {
    }

    private record PersistedSubmission(
            InitialConfigurationIntent intent,
            String requestIdentifier,
            OnboardingCaseRevision submittedCaseRevision
    ) {
    }

    private record EffectivePointer(
            long pointerIdentity,
            String answerEvidenceIdentifier
    ) {
    }
}
