package mainstreet.onboarding;

import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OnboardingSubmissionReadinessEvaluatorTest {

    private static final OnboardingQuestionDefinitionVersion V1 =
            new OnboardingQuestionDefinitionVersion("v1");
    private static final Instant NOW =
            Instant.parse("2026-08-29T00:00:00Z");

    private final OnboardingFinalReviewFactory reviewFactory =
            new OnboardingFinalReviewFactory();
    private final OnboardingSubmissionReadinessEvaluator evaluator =
            new OnboardingSubmissionReadinessEvaluator(reviewFactory);

    @Test
    void final_review_captures_exact_candidate_provenance_and_frontier() {
        OnboardingPromptDefinition unresolved =
                definition("optional", OnboardingPromptPriority
                        .OPTIONAL_PROFILE_ENRICHMENT);
        OnboardingAnswerEvidenceRevision evidence =
                answer("answer-1", "answered", "YES", "revision-2");
        OnboardingRecomputation recomputation = recomputation(
                "revision-2",
                OnboardingCaseLifecycle.IN_PROGRESS,
                Set.of(new OnboardingSemanticSeed("test", "seed")),
                List.of(unresolved),
                List.of(evidence),
                List.of()
        );

        OnboardingFinalReview review =
                reviewFactory.create(recomputation);

        assertEquals(
                new OnboardingCaseReview(
                        new OnboardingCaseIdentity("case-1"),
                        new OnboardingCaseRevision("revision-2")
                ),
                review.affinity()
        );
        assertEquals(
                Set.of(new OnboardingSemanticSeed("test", "seed")),
                review.candidateRegisteredIntent()
        );
        assertEquals(List.of("answer-1"), review.provenanceReferences());
        assertEquals(
                List.of(unresolved.key()),
                review.unresolvedPromptKeys()
        );
    }

    @Test
    void stale_review_cannot_be_submission_ready() {
        OnboardingRecomputation reviewed = recomputation(
                "revision-2",
                OnboardingCaseLifecycle.IN_PROGRESS,
                Set.of(),
                List.of(),
                List.of(),
                List.of()
        );
        OnboardingFinalReview review = reviewFactory.create(reviewed);
        OnboardingRecomputation current = recomputation(
                "revision-3",
                OnboardingCaseLifecycle.IN_PROGRESS,
                Set.of(),
                List.of(),
                List.of(),
                List.of()
        );

        OnboardingSubmissionReadiness result = evaluator.evaluate(
                review,
                current,
                OnboardingCompletionPolicy.empty(),
                OnboardingSubmissionAuthority.AUTHORISED
        );

        assertFalse(result.isReady());
        assertTrue(result.blockers().contains(
                OnboardingSubmissionBlocker.STALE_REVIEW
        ));
    }

    @Test
    void unresolved_required_prompt_blocks_but_optional_prompt_does_not() {
        OnboardingPromptDefinition required = definition(
                "required",
                OnboardingPromptPriority.MANDATORY_CONFIGURATION_DECISION
        );
        OnboardingPromptDefinition optional = definition(
                "optional",
                OnboardingPromptPriority.OPTIONAL_INTEGRATION
        );
        OnboardingRecomputation recomputation = recomputation(
                "revision-2",
                OnboardingCaseLifecycle.IN_PROGRESS,
                Set.of(),
                List.of(required, optional),
                List.of(),
                List.of()
        );
        OnboardingFinalReview review =
                reviewFactory.create(recomputation);
        OnboardingCompletionPolicy policy =
                new OnboardingCompletionPolicy(
                        Map.of(
                                required.key(),
                                OnboardingPromptCompletionRequirement.REQUIRED,
                                optional.key(),
                                OnboardingPromptCompletionRequirement.OPTIONAL
                        ),
                        List.of()
                );

        OnboardingSubmissionReadiness result = evaluator.evaluate(
                review,
                recomputation,
                policy,
                OnboardingSubmissionAuthority.AUTHORISED
        );

        assertFalse(result.isReady());
        assertEquals(
                Set.of(
                        OnboardingSubmissionBlocker
                                .INCOMPLETE_SUPPORTED_INTENT
                ),
                result.blockers()
        );

        OnboardingCompletionPolicy optionalOnly =
                new OnboardingCompletionPolicy(
                        Map.of(
                                required.key(),
                                OnboardingPromptCompletionRequirement.OPTIONAL,
                                optional.key(),
                                OnboardingPromptCompletionRequirement.OPTIONAL
                        ),
                        List.of()
                );
        assertTrue(evaluator.evaluate(
                review,
                recomputation,
                optionalOnly,
                OnboardingSubmissionAuthority.AUTHORISED
        ).isReady());
    }

    @Test
    void every_active_prompt_requires_an_explicit_completion_policy() {
        OnboardingPromptDefinition prompt = definition(
                "unclassified",
                OnboardingPromptPriority.HIGH_BRANCH_DISCOVERY
        );
        OnboardingRecomputation recomputation = recomputation(
                "revision-2",
                OnboardingCaseLifecycle.IN_PROGRESS,
                Set.of(),
                List.of(prompt),
                List.of(),
                List.of()
        );

        OnboardingSubmissionReadiness result = evaluator.evaluate(
                reviewFactory.create(recomputation),
                recomputation,
                OnboardingCompletionPolicy.empty(),
                OnboardingSubmissionAuthority.AUTHORISED
        );

        assertEquals(
                Set.of(
                        OnboardingSubmissionBlocker
                                .COMPLETION_POLICY_MISSING
                ),
                result.blockers()
        );
    }

    @Test
    void registered_not_sure_varies_and_unsupported_outcomes_block_submission() {
        OnboardingAnswerEvidenceRevision evidence = answer(
                "answer-1",
                "decision",
                "NOT_SURE",
                "revision-2"
        );
        OnboardingRecomputation recomputation = recomputation(
                "revision-2",
                OnboardingCaseLifecycle.IN_PROGRESS,
                Set.of(),
                List.of(),
                List.of(evidence),
                List.of()
        );
        OnboardingPromptKey key = key("decision");
        OnboardingCompletionPolicy policy =
                new OnboardingCompletionPolicy(
                        Map.of(
                                key,
                                OnboardingPromptCompletionRequirement.REQUIRED
                        ),
                        List.of(
                                rule(
                                        "decision",
                                        "NOT_SURE",
                                        OnboardingBlockingAnswerOutcome.NOT_SURE
                                ),
                                rule(
                                        "decision",
                                        "VARIES",
                                        OnboardingBlockingAnswerOutcome.VARIES
                                ),
                                rule(
                                        "decision",
                                        "OTHER",
                                        OnboardingBlockingAnswerOutcome
                                                .UNSUPPORTED_INTENT
                                )
                        )
                );

        assertEquals(
                Set.of(OnboardingSubmissionBlocker.UNRESOLVED_NOT_SURE),
                evaluator.evaluate(
                        reviewFactory.create(recomputation),
                        recomputation,
                        policy,
                        OnboardingSubmissionAuthority.AUTHORISED
                ).blockers()
        );

        assertEquals(
                Set.of(OnboardingSubmissionBlocker.UNRESOLVED_VARIES),
                blockersForReplacementOption(
                        recomputation,
                        policy,
                        "VARIES"
                )
        );
        assertEquals(
                Set.of(OnboardingSubmissionBlocker.UNSUPPORTED_INTENT),
                blockersForReplacementOption(
                        recomputation,
                        policy,
                        "OTHER"
                )
        );
    }

    @Test
    void incompatible_historical_answer_requires_definition_review() {
        OnboardingPromptDefinition current = definition(
                "decision",
                OnboardingPromptPriority.MANDATORY_CONFIGURATION_DECISION
        );
        OnboardingAnswerEvidenceRevision historical =
                new OnboardingAnswerEvidenceRevision(
                        "historical",
                        new OnboardingCaseIdentity("case-1"),
                        new OnboardingCaseRevision("revision-1"),
                        Optional.empty(),
                        new OnboardingAnswerEvidence(
                                current.key().questionIdentity(),
                                new OnboardingQuestionDefinitionVersion("old"),
                                OnboardingAnswerForm.MULTI_SELECT,
                                Set.of("YES"),
                                Optional.empty(),
                                Optional.empty(),
                                OnboardingAnswerOrigin.MERCHANT_SELECTED,
                                "identity-42",
                                NOW,
                                Optional.empty()
                        )
                );
        OnboardingRecomputation recomputation = recomputation(
                "revision-2",
                OnboardingCaseLifecycle.IN_PROGRESS,
                Set.of(),
                List.of(current),
                List.of(),
                List.of(historical)
        );
        OnboardingCompletionPolicy policy =
                new OnboardingCompletionPolicy(
                        Map.of(
                                current.key(),
                                OnboardingPromptCompletionRequirement.REQUIRED
                        ),
                        List.of()
                );

        OnboardingSubmissionReadiness result = evaluator.evaluate(
                reviewFactory.create(recomputation),
                recomputation,
                policy,
                OnboardingSubmissionAuthority.AUTHORISED
        );

        assertTrue(result.blockers().contains(
                OnboardingSubmissionBlocker.DEFINITION_REVIEW_REQUIRED
        ));
        assertTrue(result.blockers().contains(
                OnboardingSubmissionBlocker.INCOMPLETE_SUPPORTED_INTENT
        ));
    }

    @Test
    void current_authority_and_case_lifecycle_fail_closed() {
        OnboardingRecomputation inProgress = recomputation(
                "revision-2",
                OnboardingCaseLifecycle.IN_PROGRESS,
                Set.of(),
                List.of(),
                List.of(),
                List.of()
        );
        OnboardingFinalReview review =
                reviewFactory.create(inProgress);

        assertEquals(
                Set.of(
                        OnboardingSubmissionBlocker.AUTHORISATION_REJECTION
                ),
                evaluator.evaluate(
                        review,
                        inProgress,
                        OnboardingCompletionPolicy.empty(),
                        OnboardingSubmissionAuthority.AUTHORISATION_REJECTED
                ).blockers()
        );

        OnboardingRecomputation submitted = recomputation(
                "revision-2",
                OnboardingCaseLifecycle.SUBMITTED,
                Set.of(),
                List.of(),
                List.of(),
                List.of()
        );
        assertTrue(evaluator.evaluate(
                reviewFactory.create(submitted),
                submitted,
                OnboardingCompletionPolicy.empty(),
                OnboardingSubmissionAuthority.AUTHORISED
        ).blockers().contains(
                OnboardingSubmissionBlocker.CASE_NOT_IN_PROGRESS
        ));
    }

    @Test
    void review_content_must_equal_recomputed_revision_content() {
        OnboardingRecomputation recomputation = recomputation(
                "revision-2",
                OnboardingCaseLifecycle.IN_PROGRESS,
                Set.of(new OnboardingSemanticSeed("test", "actual")),
                List.of(),
                List.of(),
                List.of()
        );
        OnboardingFinalReview tampered = new OnboardingFinalReview(
                new OnboardingCaseReview(
                        new OnboardingCaseIdentity("case-1"),
                        new OnboardingCaseRevision("revision-2")
                ),
                Set.of(new OnboardingSemanticSeed("test", "different")),
                List.of(),
                List.of()
        );

        OnboardingSubmissionReadiness result = evaluator.evaluate(
                tampered,
                recomputation,
                OnboardingCompletionPolicy.empty(),
                OnboardingSubmissionAuthority.AUTHORISED
        );

        assertEquals(
                Set.of(
                        OnboardingSubmissionBlocker.REVIEW_CONTENT_MISMATCH
                ),
                result.blockers()
        );
    }

    private Set<OnboardingSubmissionBlocker> blockersForReplacementOption(
            OnboardingRecomputation original,
            OnboardingCompletionPolicy policy,
            String option
    ) {
        OnboardingRecomputation replacement = recomputation(
                original.caseAtRevision().currentRevision().value(),
                OnboardingCaseLifecycle.IN_PROGRESS,
                Set.of(),
                List.of(),
                List.of(answer(
                        "replacement",
                        "decision",
                        option,
                        "revision-2"
                )),
                List.of()
        );
        return evaluator.evaluate(
                reviewFactory.create(replacement),
                replacement,
                policy,
                OnboardingSubmissionAuthority.AUTHORISED
        ).blockers();
    }

    private static OnboardingAnswerOutcomeRule rule(
            String question,
            String option,
            OnboardingBlockingAnswerOutcome outcome
    ) {
        return new OnboardingAnswerOutcomeRule(
                key(question),
                V1,
                option,
                outcome
        );
    }

    private static OnboardingRecomputation recomputation(
            String revision,
            OnboardingCaseLifecycle lifecycle,
            Set<OnboardingSemanticSeed> seeds,
            List<OnboardingPromptDefinition> frontier,
            List<OnboardingAnswerEvidenceRevision> candidateEvidence,
            List<OnboardingAnswerEvidenceRevision> excludedEvidence
    ) {
        return new OnboardingRecomputation(
                new OnboardingCase(
                        new OnboardingCaseIdentity("case-1"),
                        new MerchantScope("merchant-acme"),
                        lifecycle,
                        new OnboardingCaseRevision(revision)
                ),
                seeds,
                frontier,
                candidateEvidence,
                excludedEvidence
        );
    }

    private static OnboardingPromptDefinition definition(
            String question,
            OnboardingPromptPriority priority
    ) {
        return new OnboardingPromptDefinition(
                key(question),
                V1,
                OnboardingPromptClass.CONFIGURATION_DECISION_QUESTION,
                OnboardingAnswerForm.MULTI_SELECT,
                priority,
                Map.of(V1, Set.of("YES", "NOT_SURE", "VARIES", "OTHER")),
                OnboardingPromptApplicability.always()
        );
    }

    private static OnboardingAnswerEvidenceRevision answer(
            String evidenceIdentity,
            String question,
            String option,
            String revision
    ) {
        return new OnboardingAnswerEvidenceRevision(
                evidenceIdentity,
                new OnboardingCaseIdentity("case-1"),
                new OnboardingCaseRevision(revision),
                Optional.empty(),
                new OnboardingAnswerEvidence(
                        key(question).questionIdentity(),
                        V1,
                        OnboardingAnswerForm.MULTI_SELECT,
                        Set.of(option),
                        Optional.empty(),
                        Optional.empty(),
                        OnboardingAnswerOrigin.MERCHANT_SELECTED,
                        "identity-42",
                        NOW,
                        Optional.empty()
                )
        );
    }

    private static OnboardingPromptKey key(String question) {
        return new OnboardingPromptKey(
                new OnboardingQuestionIdentity("onboarding.test", question),
                Optional.empty()
        );
    }
}
