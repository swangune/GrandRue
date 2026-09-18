package grandrue.onboarding;

import grandrue.onboarding.InitialConfigurationIntentIdentity;
import grandrue.onboarding.OnboardingCaseRevision;
import grandrue.onboarding.OnboardingCaseIdentity;
import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable non-executable handoff from onboarding to configuration bootstrap.
 *
 * <p>This intent does not pin a Semantic Registry Release, create Merchant
 * Configuration, activate capabilities or establish runtime authority.</p>
 */
public record InitialConfigurationIntent(
        InitialConfigurationIntentIdentity identity,
        MerchantScope merchantScope,
        OnboardingFinalReview merchantReviewEvidence,
        String submittedBy,
        Optional<String> originIdentifier,
        Instant submittedAt
) {

    public InitialConfigurationIntent {
        Objects.requireNonNull(identity, "identity");
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(
                merchantReviewEvidence,
                "merchantReviewEvidence"
        );
        if (submittedBy == null || submittedBy.isBlank()) {
            throw new IllegalArgumentException(
                    "Submitting principal must not be blank"
            );
        }
        originIdentifier = Objects.requireNonNull(
                originIdentifier,
                "originIdentifier"
        );
        originIdentifier.ifPresent(origin -> {
            if (origin.isBlank()) {
                throw new IllegalArgumentException(
                        "Origin identifier must not be blank"
                );
            }
        });
        Objects.requireNonNull(submittedAt, "submittedAt");
    }

    public OnboardingCaseIdentity sourceOnboardingCaseIdentity() {
        return merchantReviewEvidence.affinity().onboardingCaseIdentity();
    }

    public OnboardingCaseRevision sourceOnboardingCaseRevision() {
        return merchantReviewEvidence.affinity().reviewedRevision();
    }
}
