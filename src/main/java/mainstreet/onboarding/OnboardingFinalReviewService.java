package mainstreet.onboarding;

import grandrue.onboarding.OnboardingSubmissionAuthority;
import java.util.Objects;

/**
 * Final-review projection and advisory current-readiness application boundary.
 *
 * <p>The durable submission boundary independently revalidates these
 * predicates inside the atomic Initial Configuration Intent transaction.</p>
 */
public final class OnboardingFinalReviewService {

    private final OnboardingCaseEvidenceStore evidenceStore;
    private final OnboardingCaseRecomputationService recomputationService;
    private final OnboardingFinalReviewFactory reviewFactory;
    private final OnboardingSubmissionReadinessEvaluator readinessEvaluator;

    public OnboardingFinalReviewService(
            OnboardingCaseEvidenceStore evidenceStore,
            OnboardingCaseRecomputationService recomputationService,
            OnboardingFinalReviewFactory reviewFactory,
            OnboardingSubmissionReadinessEvaluator readinessEvaluator
    ) {
        this.evidenceStore = Objects.requireNonNull(
                evidenceStore,
                "evidenceStore"
        );
        this.recomputationService = Objects.requireNonNull(
                recomputationService,
                "recomputationService"
        );
        this.reviewFactory = Objects.requireNonNull(
                reviewFactory,
                "reviewFactory"
        );
        this.readinessEvaluator = Objects.requireNonNull(
                readinessEvaluator,
                "readinessEvaluator"
        );
    }

    public OnboardingFinalReview createReview(
            OnboardingCaseIdentity caseIdentity,
            OnboardingCaseRevision revision
    ) {
        return reviewFactory.create(
                recomputationService.recomputeAtRevision(
                        Objects.requireNonNull(caseIdentity, "caseIdentity"),
                        Objects.requireNonNull(revision, "revision")
                )
        );
    }

    public OnboardingSubmissionReadiness assessCurrent(
            OnboardingFinalReview review,
            OnboardingCompletionPolicy completionPolicy,
            OnboardingSubmissionAuthority authority
    ) {
        Objects.requireNonNull(review, "review");
        OnboardingCase current = evidenceStore.caseByIdentity(
                review.affinity().onboardingCaseIdentity()
        ).orElse(null);
        if (current == null) {
            return new OnboardingSubmissionReadiness(
                    java.util.Set.of(
                            OnboardingSubmissionBlocker.CASE_NOT_FOUND
                    )
            );
        }
        OnboardingRecomputation recomputation =
                recomputationService.recomputeAtRevision(
                        current.identity(),
                        current.currentRevision()
                );
        return readinessEvaluator.evaluate(
                review,
                recomputation,
                Objects.requireNonNull(
                        completionPolicy,
                        "completionPolicy"
                ),
                Objects.requireNonNull(authority, "authority")
        );
    }
}
