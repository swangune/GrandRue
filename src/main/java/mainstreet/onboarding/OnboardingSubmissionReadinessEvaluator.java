package mainstreet.onboarding;

import grandrue.onboarding.OnboardingAnswerEvidenceRevision;
import grandrue.onboarding.OnboardingCase;
import grandrue.onboarding.OnboardingPromptKey;
import grandrue.onboarding.OnboardingSubmissionReadiness;
import grandrue.onboarding.OnboardingFinalReviewFactory;
import grandrue.onboarding.OnboardingCaseLifecycle;
import grandrue.onboarding.OnboardingCaseReviewCurrentness;
import grandrue.onboarding.OnboardingSubmissionBlocker;
import grandrue.onboarding.OnboardingBlockingAnswerOutcome;
import grandrue.onboarding.OnboardingPromptCompletionRequirement;
import grandrue.onboarding.OnboardingSubmissionAuthority;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Fail-closed deterministic submission-readiness evaluator.
 *
 * <p>Readiness is not submission authority. The durable submission boundary
 * revalidates these predicates while atomically creating the immutable
 * Initial Configuration Intent and transitioning the case.</p>
 */
public final class OnboardingSubmissionReadinessEvaluator {

    private final OnboardingFinalReviewFactory reviewFactory;
    private final OnboardingCaseReviewCurrentness currentness =
            new OnboardingCaseReviewCurrentness();

    public OnboardingSubmissionReadinessEvaluator(
            OnboardingFinalReviewFactory reviewFactory
    ) {
        this.reviewFactory = Objects.requireNonNull(
                reviewFactory,
                "reviewFactory"
        );
    }

    public OnboardingSubmissionReadiness evaluate(
            OnboardingFinalReview review,
            OnboardingRecomputation currentRecomputation,
            OnboardingCompletionPolicy completionPolicy,
            OnboardingSubmissionAuthority authority
    ) {
        Objects.requireNonNull(review, "review");
        Objects.requireNonNull(
                currentRecomputation,
                "currentRecomputation"
        );
        Objects.requireNonNull(completionPolicy, "completionPolicy");
        Objects.requireNonNull(authority, "authority");

        LinkedHashSet<OnboardingSubmissionBlocker> blockers =
                new LinkedHashSet<>();
        OnboardingCase currentCase =
                currentRecomputation.caseAtRevision();

        boolean reviewIsCurrent = currentness.isCurrent(
                review.affinity(),
                currentCase
        );
        if (!reviewIsCurrent) {
            blockers.add(OnboardingSubmissionBlocker.STALE_REVIEW);
        } else if (!review.equals(
                reviewFactory.create(currentRecomputation)
        )) {
            blockers.add(
                    OnboardingSubmissionBlocker.REVIEW_CONTENT_MISMATCH
            );
        }

        if (currentCase.lifecycle()
                != OnboardingCaseLifecycle.IN_PROGRESS) {
            blockers.add(
                    OnboardingSubmissionBlocker.CASE_NOT_IN_PROGRESS
            );
        }
        addAuthorityBlocker(blockers, authority);

        Set<OnboardingPromptKey> activePromptKeys =
                activePromptKeys(currentRecomputation);
        activePromptKeys.forEach(key -> {
            if (completionPolicy.requirementFor(key).isEmpty()) {
                blockers.add(
                        OnboardingSubmissionBlocker
                                .COMPLETION_POLICY_MISSING
                );
            }
        });

        for (OnboardingPromptDefinition unresolved :
                currentRecomputation.unresolvedFrontier()) {
            completionPolicy.requirementFor(unresolved.key())
                    .filter(requirement ->
                            requirement
                                    == OnboardingPromptCompletionRequirement
                                            .REQUIRED)
                    .ifPresent(requirement -> blockers.add(
                            OnboardingSubmissionBlocker
                                    .INCOMPLETE_SUPPORTED_INTENT
                    ));
            addExcludedEvidenceBlocker(
                    blockers,
                    unresolved,
                    currentRecomputation.excludedEvidence()
            );
        }

        currentRecomputation.candidateEvidence().forEach(evidence ->
                completionPolicy.blockingOutcomesFor(evidence)
                        .forEach(outcome ->
                                blockers.add(blockerFor(outcome))
                        )
        );

        return new OnboardingSubmissionReadiness(blockers);
    }

    private static Set<OnboardingPromptKey> activePromptKeys(
            OnboardingRecomputation recomputation
    ) {
        LinkedHashSet<OnboardingPromptKey> keys =
                new LinkedHashSet<>();
        recomputation.unresolvedFrontier().stream()
                .map(OnboardingPromptDefinition::key)
                .forEach(keys::add);
        recomputation.candidateEvidence().stream()
                .map(OnboardingAnswerEvidenceRevision::answer)
                .map(answer -> new OnboardingPromptKey(
                        answer.questionIdentity(),
                        answer.contextScopeReference()
                ))
                .forEach(keys::add);
        return Set.copyOf(keys);
    }

    private static void addExcludedEvidenceBlocker(
            Set<OnboardingSubmissionBlocker> blockers,
            OnboardingPromptDefinition unresolved,
            List<OnboardingAnswerEvidenceRevision> excludedEvidence
    ) {
        excludedEvidence.stream()
                .filter(evidence -> unresolved.key().equals(
                        new OnboardingPromptKey(
                                evidence.answer().questionIdentity(),
                                evidence.answer().contextScopeReference()
                        )
                ))
                .forEach(evidence -> {
                    if (!unresolved
                            .acceptedOptionIdentifiersByVersion()
                            .containsKey(
                                    evidence.answer()
                                            .questionDefinitionVersion()
                            )) {
                        blockers.add(
                                OnboardingSubmissionBlocker
                                        .DEFINITION_REVIEW_REQUIRED
                        );
                    } else if (!unresolved.accepts(evidence.answer())) {
                        blockers.add(
                                OnboardingSubmissionBlocker
                                        .ANSWER_INVALID_FOR_QUESTION_DEFINITION
                        );
                    }
                });
    }

    private static void addAuthorityBlocker(
            Set<OnboardingSubmissionBlocker> blockers,
            OnboardingSubmissionAuthority authority
    ) {
        switch (authority) {
            case AUTHORISED -> {
            }
            case AUTHENTICATION_REQUIRED -> blockers.add(
                    OnboardingSubmissionBlocker.AUTHENTICATION_REQUIRED
            );
            case AUTHORISATION_REJECTED -> blockers.add(
                    OnboardingSubmissionBlocker.AUTHORISATION_REJECTION
            );
            case ACCOUNT_OPERATION_RESTRICTED -> blockers.add(
                    OnboardingSubmissionBlocker
                            .ACCOUNT_OPERATION_RESTRICTED
            );
            case INITIAL_ONBOARDING_NOT_APPLICABLE -> blockers.add(
                    OnboardingSubmissionBlocker
                            .INITIAL_ONBOARDING_NOT_APPLICABLE
            );
        }
    }

    private static OnboardingSubmissionBlocker blockerFor(
            OnboardingBlockingAnswerOutcome outcome
    ) {
        return switch (outcome) {
            case NOT_SURE ->
                    OnboardingSubmissionBlocker.UNRESOLVED_NOT_SURE;
            case VARIES ->
                    OnboardingSubmissionBlocker.UNRESOLVED_VARIES;
            case UNSUPPORTED_INTENT ->
                    OnboardingSubmissionBlocker.UNSUPPORTED_INTENT;
        };
    }
}
