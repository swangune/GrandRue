package grandrue.infrastructure.persistence.onboarding;

import grandrue.application.MerchantScope;
import grandrue.onboarding.DeterministicOnboardingRecomputation;
import grandrue.onboarding.InitialCustomerInteractionDiscoveryQuestion;
import grandrue.onboarding.InitialConfigurationIntent;
import grandrue.onboarding.InitialConfigurationIntentIdentity;
import grandrue.onboarding.InitialOnboardingPromptCatalogue;
import grandrue.onboarding.OnboardingAnswerEvidence;
import grandrue.onboarding.OnboardingAnswerRecomputationResult;
import grandrue.onboarding.OnboardingAnswerEvidenceRevision;
import grandrue.onboarding.OnboardingAnswerForm;
import grandrue.onboarding.OnboardingAnswerMutationResult;
import grandrue.onboarding.OnboardingAnswerOrigin;
import grandrue.onboarding.OnboardingCase;
import grandrue.onboarding.OnboardingCaseEvidenceSnapshot;
import grandrue.onboarding.OnboardingCaseIdentity;
import grandrue.onboarding.OnboardingCaseLifecycle;
import grandrue.onboarding.OnboardingCasePersistenceException;
import grandrue.onboarding.OnboardingCaseRecomputationService;
import grandrue.onboarding.OnboardingCompletionPolicy;
import grandrue.onboarding.OnboardingFinalReview;
import grandrue.onboarding.OnboardingFinalReviewFactory;
import grandrue.onboarding.OnboardingFinalReviewService;
import grandrue.onboarding.OnboardingCaseRevision;
import grandrue.onboarding.OnboardingPersistenceFailureCategory;
import grandrue.onboarding.OnboardingPromptCompletionRequirement;
import grandrue.onboarding.OnboardingPromptKey;
import grandrue.onboarding.OnboardingSubmissionAuthority;
import grandrue.onboarding.OnboardingSubmissionBlocker;
import grandrue.onboarding.OnboardingSubmissionRejectedException;
import grandrue.onboarding.OnboardingSubmissionReadinessEvaluator;
import grandrue.onboarding.OnboardingQuestionDefinitionVersion;
import grandrue.onboarding.OnboardingQuestionIdentity;
import grandrue.onboarding.RecordOnboardingAnswerCommand;
import grandrue.onboarding.StartInitialOnboardingCaseCommand;
import grandrue.onboarding.SubmitInitialConfigurationIntentCommand;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqOnboardingCaseEvidenceStoreIT {

    private static final Instant NOW =
            Instant.parse("2026-08-28T23:00:00Z");

    private DSLContext dsl;
    private PlatformTransactionManager transactionManager;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new DriverManagerDataSource(
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_URL"),
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_USER"),
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_PASSWORD")
        );
        transactionManager = new DataSourceTransactionManager(dataSource);
        Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load()
                .migrate();
        dsl = DSL.using(
                new TransactionAwareDataSourceProxy(dataSource),
                SQLDialect.POSTGRES
        );
        dsl.execute(
                "truncate table "
                        + "configuration_new_activity_required_contract, "
                        + "configuration_new_activity_requirement, "
                        + "configuration_new_activity_requirement_set, "
                        + "configuration_revision_approval, "
                        + "configuration_impact_review_finding, "
                        + "configuration_impact_review_effect, "
                        + "configuration_impact_review_evidence, "
                        + "configuration_validation_evidence, "
                        + "merchant_configuration_revision_policy, "
                        + "merchant_configuration_revision_capability, "
                        + "merchant_configuration_revision, "
                        + "initial_configuration_intent_unresolved_prompt, "
                        + "initial_configuration_intent_provenance, "
                        + "initial_configuration_intent_semantic_seed, "
                        + "initial_configuration_intent, "
                        + "onboarding_effective_answer, "
                        + "onboarding_answer_evidence, "
                        + "onboarding_case_revision, "
                        + "onboarding_start_request, "
                        + "onboarding_case"
        );
    }

    @Test
    void repeated_start_resolves_the_same_durable_merchant_case() {
        JooqOnboardingCaseEvidenceStore store = store();
        OnboardingCase first = store.startInitial(start(
                "case-1",
                "merchant-acme",
                "revision-1",
                "start-1"
        ));
        OnboardingCase repeated = store.startInitial(start(
                "case-2",
                "merchant-acme",
                "other-initial-revision",
                "start-2"
        ));

        assertEquals(first, repeated);
        assertEquals(
                first,
                store().currentInitial(
                        new MerchantScope("merchant-acme")
                ).orElseThrow()
        );
        assertEquals(1, dsl.fetchCount(
                DSL.table(DSL.name("onboarding_case"))
        ));
        assertEquals(1, dsl.fetchCount(
                DSL.table(DSL.name("onboarding_case_revision"))
        ));
        assertEquals(2, dsl.fetchCount(
                DSL.table(DSL.name("onboarding_start_request"))
        ));
    }

    @Test
    void answer_advances_revision_and_correction_preserves_history() {
        JooqOnboardingCaseEvidenceStore store = store();
        store.startInitial(start(
                "case-1",
                "merchant-acme",
                "revision-1",
                "start-1"
        ));

        OnboardingAnswerMutationResult first = store.recordAnswer(answer(
                "revision-1",
                "revision-2",
                "answer-request-1",
                "answer-evidence-1",
                "SEND_ENQUIRY"
        ));
        OnboardingAnswerMutationResult corrected = store.recordAnswer(answer(
                "revision-2",
                "revision-3",
                "answer-request-2",
                "answer-evidence-2",
                "PUBLISH_INFORMATION"
        ));

        assertEquals(
                new OnboardingCaseRevision("revision-2"),
                first.caseAtCommit().currentRevision()
        );
        assertEquals(
                Optional.of("answer-evidence-1"),
                corrected.answerEvidence().supersedesAnswerEvidenceIdentifier()
        );
        assertEquals(
                new OnboardingCaseRevision("revision-3"),
                store.currentInitial(
                        new MerchantScope("merchant-acme")
                ).orElseThrow().currentRevision()
        );

        List<OnboardingAnswerEvidenceRevision> history =
                store.answerHistory(
                        new OnboardingCaseIdentity("case-1"),
                        question(),
                        Optional.empty()
                );
        assertEquals(2, history.size());
        assertEquals(
                List.of("answer-evidence-1", "answer-evidence-2"),
                history.stream()
                        .map(OnboardingAnswerEvidenceRevision::answerEvidenceIdentifier)
                        .toList()
        );
        assertEquals(
                List.of("answer-evidence-2"),
                store.effectiveAnswers(
                                new OnboardingCaseIdentity("case-1")
                        ).stream()
                        .map(OnboardingAnswerEvidenceRevision::answerEvidenceIdentifier)
                        .toList()
        );
    }

    @Test
    void reconstructs_exact_effective_evidence_at_historical_revision() {
        JooqOnboardingCaseEvidenceStore store = store();
        store.startInitial(start(
                "case-1",
                "merchant-acme",
                "revision-1",
                "start-1"
        ));
        store.recordAnswer(answer(
                "revision-1",
                "revision-2",
                "answer-request-1",
                "answer-evidence-1",
                "SEND_ENQUIRY"
        ));
        store.recordAnswer(answer(
                "revision-2",
                "revision-3",
                "answer-request-2",
                "answer-evidence-2",
                "PUBLISH_INFORMATION"
        ));

        OnboardingCaseEvidenceSnapshot first =
                store.evidenceSnapshotAtRevision(
                        new OnboardingCaseIdentity("case-1"),
                        new OnboardingCaseRevision("revision-2")
                );
        OnboardingCaseEvidenceSnapshot corrected =
                store.evidenceSnapshotAtRevision(
                        new OnboardingCaseIdentity("case-1"),
                        new OnboardingCaseRevision("revision-3")
                );

        assertEquals(
                new OnboardingCaseRevision("revision-2"),
                first.caseAtRevision().currentRevision()
        );
        assertEquals(
                List.of("answer-evidence-1"),
                first.effectiveAnswers().stream()
                        .map(OnboardingAnswerEvidenceRevision
                                ::answerEvidenceIdentifier)
                        .toList()
        );
        assertEquals(
                List.of("answer-evidence-2"),
                corrected.effectiveAnswers().stream()
                        .map(OnboardingAnswerEvidenceRevision
                                ::answerEvidenceIdentifier)
                        .toList()
        );
    }

    @Test
    void answer_application_path_recomputes_the_exact_committed_revision() {
        JooqOnboardingCaseEvidenceStore store = store();
        store.startInitial(start(
                "case-1",
                "merchant-acme",
                "revision-1",
                "start-1"
        ));
        OnboardingCaseRecomputationService service =
                new OnboardingCaseRecomputationService(
                        store,
                        new DeterministicOnboardingRecomputation(
                                InitialOnboardingPromptCatalogue.registry()
                        )
                );

        OnboardingAnswerRecomputationResult result =
                service.recordAnswerAndRecompute(
                        new RecordOnboardingAnswerCommand(
                                new OnboardingCaseIdentity("case-1"),
                                new OnboardingCaseRevision("revision-1"),
                                new OnboardingCaseRevision("revision-2"),
                                "answer-request-1",
                                "answer-evidence-1",
                                evidenceV2("SEND_ENQUIRY"),
                                "identity-42",
                                Optional.of("privileged-browser"),
                                NOW
                        )
                );

        assertEquals(
                new OnboardingCaseRevision("revision-2"),
                result.answerMutation()
                        .caseAtCommit()
                        .currentRevision()
        );
        assertEquals(
                new OnboardingCaseRevision("revision-2"),
                result.recomputation()
                        .caseAtRevision()
                        .currentRevision()
        );
        assertEquals(
                Set.of("enquiry"),
                result.recomputation().candidateSemanticSeeds().stream()
                        .map(seed -> seed.identifier())
                        .collect(java.util.stream.Collectors.toSet())
        );
    }

    @Test
    void durable_final_review_becomes_stale_after_case_correction() {
        JooqOnboardingCaseEvidenceStore store = store();
        store.startInitial(start(
                "case-1",
                "merchant-acme",
                "revision-1",
                "start-1"
        ));
        OnboardingCaseRecomputationService recomputationService =
                new OnboardingCaseRecomputationService(
                        store,
                        new DeterministicOnboardingRecomputation(
                                InitialOnboardingPromptCatalogue.registry()
                        )
                );
        recomputationService.recordAnswerAndRecompute(
                new RecordOnboardingAnswerCommand(
                        new OnboardingCaseIdentity("case-1"),
                        new OnboardingCaseRevision("revision-1"),
                        new OnboardingCaseRevision("revision-2"),
                        "answer-request-1",
                        "answer-evidence-1",
                        evidenceV2("SEND_ENQUIRY"),
                        "identity-42",
                        Optional.of("privileged-browser"),
                        NOW
                )
        );
        OnboardingFinalReviewService reviewService =
                new OnboardingFinalReviewService(
                        store,
                        recomputationService,
                        new OnboardingFinalReviewFactory(),
                        new OnboardingSubmissionReadinessEvaluator(
                                new OnboardingFinalReviewFactory()
                        )
                );
        OnboardingFinalReview review = reviewService.createReview(
                new OnboardingCaseIdentity("case-1"),
                new OnboardingCaseRevision("revision-2")
        );
        OnboardingPromptKey root = new OnboardingPromptKey(
                InitialCustomerInteractionDiscoveryQuestion.identity(),
                Optional.empty()
        );
        OnboardingCompletionPolicy policy =
                new OnboardingCompletionPolicy(
                        Map.of(
                                root,
                                OnboardingPromptCompletionRequirement.REQUIRED
                        ),
                        List.of()
                );

        assertEquals(
                true,
                reviewService.assessCurrent(
                        review,
                        policy,
                        OnboardingSubmissionAuthority.AUTHORISED
                ).isReady()
        );

        store.recordAnswer(new RecordOnboardingAnswerCommand(
                new OnboardingCaseIdentity("case-1"),
                new OnboardingCaseRevision("revision-2"),
                new OnboardingCaseRevision("revision-3"),
                "answer-request-2",
                "answer-evidence-2",
                evidenceV2("NOTHING_ELSE_FOR_NOW"),
                "identity-42",
                Optional.of("privileged-browser"),
                NOW
        ));

        assertEquals(
                false,
                reviewService.assessCurrent(
                        review,
                        policy,
                        OnboardingSubmissionAuthority.AUTHORISED
                ).isReady()
        );
    }

    @Test
    void submission_atomically_persists_exact_intent_and_transitions_case() {
        JooqOnboardingCaseEvidenceStore store = readyCase();
        OnboardingFinalReview review = reviewService(store).createReview(
                new OnboardingCaseIdentity("case-1"),
                new OnboardingCaseRevision("revision-2")
        );
        SubmitInitialConfigurationIntentCommand command = submit(
                review,
                "submit-request-1",
                "intent-1",
                "revision-3"
        );

        InitialConfigurationIntent first =
                store.submitInitialConfigurationIntent(
                        command,
                        completionPolicy(),
                        OnboardingSubmissionAuthority.AUTHORISED
                );
        InitialConfigurationIntent retry =
                store.submitInitialConfigurationIntent(
                        command,
                        completionPolicy(),
                        OnboardingSubmissionAuthority.AUTHORISED
                );

        assertEquals(first, retry);
        assertEquals(
                new InitialConfigurationIntentIdentity("intent-1"),
                first.identity()
        );
        assertEquals(review, first.merchantReviewEvidence());
        assertEquals(
                new MerchantScope("merchant-acme"),
                first.merchantScope()
        );
        assertEquals(
                OnboardingCaseLifecycle.SUBMITTED,
                store.caseByIdentity(
                        new OnboardingCaseIdentity("case-1")
                ).orElseThrow().lifecycle()
        );
        assertEquals(
                new OnboardingCaseRevision("revision-3"),
                store.caseByIdentity(
                        new OnboardingCaseIdentity("case-1")
                ).orElseThrow().currentRevision()
        );
        assertEquals(
                first,
                store.initialConfigurationIntentByIdentity(
                        new InitialConfigurationIntentIdentity("intent-1")
                ).orElseThrow()
        );
        assertEquals(1, dsl.fetchCount(
                DSL.table(DSL.name("initial_configuration_intent"))
        ));
        assertEquals(1, dsl.fetchCount(
                DSL.table(DSL.name(
                        "initial_configuration_intent_semantic_seed"
                ))
        ));
        assertEquals(1, dsl.fetchCount(
                DSL.table(DSL.name(
                        "initial_configuration_intent_provenance"
                ))
        ));
        assertEquals(3, dsl.fetchCount(
                DSL.table(DSL.name("onboarding_case_revision"))
        ));
    }

    @Test
    void stale_review_rejection_rolls_back_submission_authority() {
        JooqOnboardingCaseEvidenceStore store = readyCase();
        OnboardingFinalReview review = reviewService(store).createReview(
                new OnboardingCaseIdentity("case-1"),
                new OnboardingCaseRevision("revision-2")
        );
        store.recordAnswer(new RecordOnboardingAnswerCommand(
                new OnboardingCaseIdentity("case-1"),
                new OnboardingCaseRevision("revision-2"),
                new OnboardingCaseRevision("revision-current"),
                "answer-request-current",
                "answer-evidence-current",
                evidenceV2("NOTHING_ELSE_FOR_NOW"),
                "identity-42",
                Optional.of("privileged-browser"),
                NOW
        ));

        OnboardingSubmissionRejectedException failure = assertThrows(
                OnboardingSubmissionRejectedException.class,
                () -> store.submitInitialConfigurationIntent(
                        submit(
                                review,
                                "submit-request-stale",
                                "intent-stale",
                                "revision-submitted"
                        ),
                        completionPolicy(),
                        OnboardingSubmissionAuthority.AUTHORISED
                )
        );

        assertTrue(failure.blockers().contains(
                OnboardingSubmissionBlocker.STALE_REVIEW
        ));
        assertEquals(
                OnboardingCaseLifecycle.IN_PROGRESS,
                store.caseByIdentity(
                        new OnboardingCaseIdentity("case-1")
                ).orElseThrow().lifecycle()
        );
        assertEquals(0, dsl.fetchCount(
                DSL.table(DSL.name("initial_configuration_intent"))
        ));
    }

    @Test
    void submission_revalidates_current_authority_before_commit() {
        JooqOnboardingCaseEvidenceStore store = readyCase();
        OnboardingFinalReview review = reviewService(store).createReview(
                new OnboardingCaseIdentity("case-1"),
                new OnboardingCaseRevision("revision-2")
        );

        OnboardingSubmissionRejectedException failure = assertThrows(
                OnboardingSubmissionRejectedException.class,
                () -> store.submitInitialConfigurationIntent(
                        submit(
                                review,
                                "submit-request-restricted",
                                "intent-restricted",
                                "revision-3"
                        ),
                        completionPolicy(),
                        OnboardingSubmissionAuthority
                                .ACCOUNT_OPERATION_RESTRICTED
                )
        );

        assertEquals(
                Set.of(
                        OnboardingSubmissionBlocker
                                .ACCOUNT_OPERATION_RESTRICTED
                ),
                failure.blockers()
        );
        assertEquals(0, dsl.fetchCount(
                DSL.table(DSL.name("initial_configuration_intent"))
        ));
        assertEquals(
                OnboardingCaseLifecycle.IN_PROGRESS,
                store.caseByIdentity(
                        new OnboardingCaseIdentity("case-1")
                ).orElseThrow().lifecycle()
        );
    }

    @Test
    void submission_request_identity_cannot_be_reused_for_new_intent() {
        JooqOnboardingCaseEvidenceStore store = readyCase();
        OnboardingFinalReview review = reviewService(store).createReview(
                new OnboardingCaseIdentity("case-1"),
                new OnboardingCaseRevision("revision-2")
        );
        SubmitInitialConfigurationIntentCommand first = submit(
                review,
                "submit-request-1",
                "intent-1",
                "revision-3"
        );
        store.submitInitialConfigurationIntent(
                first,
                completionPolicy(),
                OnboardingSubmissionAuthority.AUTHORISED
        );

        OnboardingCasePersistenceException conflict = assertThrows(
                OnboardingCasePersistenceException.class,
                () -> store.submitInitialConfigurationIntent(
                        submit(
                                review,
                                "submit-request-1",
                                "intent-different",
                                "revision-3"
                        ),
                        completionPolicy(),
                        OnboardingSubmissionAuthority.AUTHORISED
                )
        );

        assertEquals(
                OnboardingPersistenceFailureCategory.REQUEST_IDENTITY_CONFLICT,
                conflict.category()
        );
        assertEquals(1, dsl.fetchCount(
                DSL.table(DSL.name("initial_configuration_intent"))
        ));
    }

    @Test
    void concurrent_submissions_from_one_review_commit_at_most_once()
            throws Exception {
        JooqOnboardingCaseEvidenceStore store = readyCase();
        OnboardingFinalReview review = reviewService(store).createReview(
                new OnboardingCaseIdentity("case-1"),
                new OnboardingCaseRevision("revision-2")
        );
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<Object> first = executor.submit(() ->
                    submitAfterBarrier(
                            ready,
                            start,
                            submit(
                                    review,
                                    "submit-request-a",
                                    "intent-a",
                                    "revision-a"
                            )
                    )
            );
            Future<Object> second = executor.submit(() ->
                    submitAfterBarrier(
                            ready,
                            start,
                            submit(
                                    review,
                                    "submit-request-b",
                                    "intent-b",
                                    "revision-b"
                            )
                    )
            );
            ready.await();
            start.countDown();

            List<Object> outcomes = List.of(first.get(), second.get());
            assertEquals(
                    1,
                    outcomes.stream()
                            .filter(InitialConfigurationIntent.class::isInstance)
                            .count()
            );
            assertEquals(
                    1,
                    outcomes.stream()
                            .filter(OnboardingSubmissionRejectedException
                                    .class::isInstance)
                            .map(OnboardingSubmissionRejectedException
                                    .class::cast)
                            .filter(failure -> failure.blockers().contains(
                                    OnboardingSubmissionBlocker
                                            .CASE_NOT_IN_PROGRESS
                            ))
                            .count()
            );
        }

        assertEquals(1, dsl.fetchCount(
                DSL.table(DSL.name("initial_configuration_intent"))
        ));
        assertEquals(3, dsl.fetchCount(
                DSL.table(DSL.name("onboarding_case_revision"))
        ));
    }

    @Test
    void stale_revision_conflicts_without_persisting_answer() {
        JooqOnboardingCaseEvidenceStore store = store();
        store.startInitial(start(
                "case-1",
                "merchant-acme",
                "revision-1",
                "start-1"
        ));
        store.recordAnswer(answer(
                "revision-1",
                "revision-2",
                "answer-request-1",
                "answer-evidence-1",
                "SEND_ENQUIRY"
        ));

        OnboardingCasePersistenceException conflict = assertThrows(
                OnboardingCasePersistenceException.class,
                () -> store.recordAnswer(answer(
                        "revision-1",
                        "revision-stale",
                        "answer-request-stale",
                        "answer-evidence-stale",
                        "PUBLISH_INFORMATION"
                ))
        );

        assertEquals(
                OnboardingPersistenceFailureCategory.CASE_REVISION_CONFLICT,
                conflict.category()
        );
        assertEquals(1, dsl.fetchCount(
                DSL.table(DSL.name("onboarding_answer_evidence"))
        ));
        assertEquals(
                new OnboardingCaseRevision("revision-2"),
                store.currentInitial(
                        new MerchantScope("merchant-acme")
                ).orElseThrow().currentRevision()
        );
    }

    @Test
    void retry_returns_exact_committed_answer_after_later_correction() {
        JooqOnboardingCaseEvidenceStore store = store();
        store.startInitial(start(
                "case-1",
                "merchant-acme",
                "revision-1",
                "start-1"
        ));
        RecordOnboardingAnswerCommand firstCommand = answer(
                "revision-1",
                "revision-2",
                "answer-request-1",
                "answer-evidence-1",
                "SEND_ENQUIRY"
        );
        OnboardingAnswerMutationResult first =
                store.recordAnswer(firstCommand);
        store.recordAnswer(answer(
                "revision-2",
                "revision-3",
                "answer-request-2",
                "answer-evidence-2",
                "PUBLISH_INFORMATION"
        ));

        assertEquals(first, store.recordAnswer(firstCommand));
        assertEquals(
                new OnboardingCaseRevision("revision-2"),
                first.caseAtCommit().currentRevision()
        );
        assertEquals(2, dsl.fetchCount(
                DSL.table(DSL.name("onboarding_answer_evidence"))
        ));
    }

    @Test
    void request_identity_reuse_with_different_intent_is_rejected() {
        JooqOnboardingCaseEvidenceStore store = store();
        store.startInitial(start(
                "case-1",
                "merchant-acme",
                "revision-1",
                "start-1"
        ));
        store.recordAnswer(answer(
                "revision-1",
                "revision-2",
                "answer-request-1",
                "answer-evidence-1",
                "SEND_ENQUIRY"
        ));

        OnboardingCasePersistenceException conflict = assertThrows(
                OnboardingCasePersistenceException.class,
                () -> store.recordAnswer(new RecordOnboardingAnswerCommand(
                        new OnboardingCaseIdentity("case-1"),
                        new OnboardingCaseRevision("revision-1"),
                        new OnboardingCaseRevision("revision-2"),
                        "answer-request-1",
                        "answer-evidence-different",
                        evidence("PUBLISH_INFORMATION"),
                        "identity-42",
                        Optional.of("privileged-browser"),
                        NOW
                ))
        );

        assertEquals(
                OnboardingPersistenceFailureCategory.REQUEST_IDENTITY_CONFLICT,
                conflict.category()
        );
    }

    @Test
    void concurrent_answers_from_one_revision_commit_at_most_once()
            throws Exception {
        JooqOnboardingCaseEvidenceStore store = store();
        store.startInitial(start(
                "case-1",
                "merchant-acme",
                "revision-1",
                "start-1"
        ));

        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<Object> first = executor.submit(() -> answerAfterBarrier(
                    ready,
                    start,
                    answer(
                            "revision-1",
                            "revision-a",
                            "answer-request-a",
                            "answer-evidence-a",
                            "SEND_ENQUIRY"
                    )
            ));
            Future<Object> second = executor.submit(() -> answerAfterBarrier(
                    ready,
                    start,
                    answer(
                            "revision-1",
                            "revision-b",
                            "answer-request-b",
                            "answer-evidence-b",
                            "PUBLISH_INFORMATION"
                    )
            ));
            ready.await();
            start.countDown();

            List<Object> outcomes = List.of(first.get(), second.get());
            assertEquals(
                    1,
                    outcomes.stream()
                            .filter(OnboardingAnswerMutationResult.class::isInstance)
                            .count()
            );
            assertEquals(
                    1,
                    outcomes.stream()
                            .filter(OnboardingCasePersistenceException.class::isInstance)
                            .map(OnboardingCasePersistenceException.class::cast)
                            .filter(failure ->
                                    failure.category()
                                            == OnboardingPersistenceFailureCategory
                                                    .CASE_REVISION_CONFLICT)
                            .count()
            );
        }

        assertEquals(1, dsl.fetchCount(
                DSL.table(DSL.name("onboarding_answer_evidence"))
        ));
        assertEquals(2, dsl.fetchCount(
                DSL.table(DSL.name("onboarding_case_revision"))
        ));
    }

    private Object answerAfterBarrier(
            CountDownLatch ready,
            CountDownLatch start,
            RecordOnboardingAnswerCommand command
    ) throws InterruptedException {
        ready.countDown();
        start.await();
        try {
            return store().recordAnswer(command);
        } catch (OnboardingCasePersistenceException failure) {
            return failure;
        }
    }

    private Object submitAfterBarrier(
            CountDownLatch ready,
            CountDownLatch start,
            SubmitInitialConfigurationIntentCommand command
    ) throws InterruptedException {
        ready.countDown();
        start.await();
        try {
            return store().submitInitialConfigurationIntent(
                    command,
                    completionPolicy(),
                    OnboardingSubmissionAuthority.AUTHORISED
            );
        } catch (OnboardingSubmissionRejectedException failure) {
            return failure;
        }
    }

    private JooqOnboardingCaseEvidenceStore store() {
        return new JooqOnboardingCaseEvidenceStore(
                dsl,
                transactionManager
        );
    }

    private JooqOnboardingCaseEvidenceStore readyCase() {
        JooqOnboardingCaseEvidenceStore store = store();
        store.startInitial(start(
                "case-1",
                "merchant-acme",
                "revision-1",
                "start-1"
        ));
        store.recordAnswer(new RecordOnboardingAnswerCommand(
                new OnboardingCaseIdentity("case-1"),
                new OnboardingCaseRevision("revision-1"),
                new OnboardingCaseRevision("revision-2"),
                "answer-request-1",
                "answer-evidence-1",
                evidenceV2("SEND_ENQUIRY"),
                "identity-42",
                Optional.of("privileged-browser"),
                NOW
        ));
        return store;
    }

    private OnboardingFinalReviewService reviewService(
            JooqOnboardingCaseEvidenceStore store
    ) {
        OnboardingFinalReviewFactory factory =
                new OnboardingFinalReviewFactory();
        return new OnboardingFinalReviewService(
                store,
                new OnboardingCaseRecomputationService(
                        store,
                        new DeterministicOnboardingRecomputation(
                                InitialOnboardingPromptCatalogue.registry()
                        )
                ),
                factory,
                new OnboardingSubmissionReadinessEvaluator(factory)
        );
    }

    private static OnboardingCompletionPolicy completionPolicy() {
        return new OnboardingCompletionPolicy(
                Map.of(
                        new OnboardingPromptKey(
                                InitialCustomerInteractionDiscoveryQuestion
                                        .identity(),
                                Optional.empty()
                        ),
                        OnboardingPromptCompletionRequirement.REQUIRED
                ),
                List.of()
        );
    }

    private static SubmitInitialConfigurationIntentCommand submit(
            OnboardingFinalReview review,
            String requestIdentity,
            String intentIdentity,
            String submittedRevision
    ) {
        return new SubmitInitialConfigurationIntentCommand(
                requestIdentity,
                new InitialConfigurationIntentIdentity(intentIdentity),
                review,
                new OnboardingCaseRevision(submittedRevision),
                "identity-42",
                Optional.of("privileged-browser"),
                NOW
        );
    }

    private static StartInitialOnboardingCaseCommand start(
            String caseIdentity,
            String merchant,
            String revision,
            String request
    ) {
        return new StartInitialOnboardingCaseCommand(
                new OnboardingCaseIdentity(caseIdentity),
                new MerchantScope(merchant),
                new OnboardingCaseRevision(revision),
                request,
                "identity-42",
                Optional.of("privileged-browser"),
                NOW
        );
    }

    private static RecordOnboardingAnswerCommand answer(
            String expectedRevision,
            String replacementRevision,
            String request,
            String evidenceIdentifier,
            String option
    ) {
        return new RecordOnboardingAnswerCommand(
                new OnboardingCaseIdentity("case-1"),
                new OnboardingCaseRevision(expectedRevision),
                new OnboardingCaseRevision(replacementRevision),
                request,
                evidenceIdentifier,
                evidence(option),
                "identity-42",
                Optional.of("privileged-browser"),
                NOW
        );
    }

    private static OnboardingAnswerEvidence evidence(String option) {
        return new OnboardingAnswerEvidence(
                question(),
                new OnboardingQuestionDefinitionVersion("v1"),
                OnboardingAnswerForm.MULTI_SELECT,
                Set.of(option),
                Optional.empty(),
                Optional.empty(),
                OnboardingAnswerOrigin.MERCHANT_SELECTED,
                "identity-42",
                NOW,
                Optional.of("semantic-release-7")
        );
    }

    private static OnboardingAnswerEvidence evidenceV2(String option) {
        return new OnboardingAnswerEvidence(
                question(),
                new OnboardingQuestionDefinitionVersion("v2"),
                OnboardingAnswerForm.MULTI_SELECT,
                Set.of(option),
                Optional.empty(),
                Optional.empty(),
                OnboardingAnswerOrigin.MERCHANT_SELECTED,
                "identity-42",
                NOW,
                Optional.of("semantic-release-7")
        );
    }

    private static OnboardingQuestionIdentity question() {
        return new OnboardingQuestionIdentity(
                "onboarding.discovery",
                "customer-interactions"
        );
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Required environment variable is missing: " + name
            );
        }
        return value;
    }
}
