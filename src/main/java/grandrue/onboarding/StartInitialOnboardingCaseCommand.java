package grandrue.onboarding;

import grandrue.onboarding.OnboardingCaseRevision;
import grandrue.onboarding.OnboardingCaseIdentity;
import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Retry-identifiable request to start or resume ordinary initial onboarding.
 */
public record StartInitialOnboardingCaseCommand(
        OnboardingCaseIdentity requestedCaseIdentity,
        MerchantScope merchantScope,
        OnboardingCaseRevision initialRevision,
        String startRequestIdentifier,
        String principalReference,
        Optional<String> originIdentifier,
        Instant startedAt
) {

    public StartInitialOnboardingCaseCommand {
        Objects.requireNonNull(
                requestedCaseIdentity,
                "requestedCaseIdentity"
        );
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(initialRevision, "initialRevision");
        require(startRequestIdentifier, "startRequestIdentifier");
        require(principalReference, "principalReference");
        originIdentifier = Objects.requireNonNull(
                originIdentifier,
                "originIdentifier"
        );
        originIdentifier.ifPresent(value ->
                require(value, "originIdentifier")
        );
        Objects.requireNonNull(startedAt, "startedAt");
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
