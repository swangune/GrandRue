package mainstreet.onboarding;

import grandrue.onboarding.OnboardingCaseRevision;
import grandrue.onboarding.OnboardingCaseIdentity;
import grandrue.onboarding.OnboardingCaseLifecycle;
import grandrue.onboarding.OnboardingCasePurpose;
import grandrue.application.MerchantScope;

import java.util.Objects;

/**
 * Merchant-owned ordinary initial Onboarding Case identity shell.
 *
 * <p>The case belongs to Merchant Scope, not to the Controller who happened to begin it.</p>
 */
public record OnboardingCase(
        OnboardingCaseIdentity identity,
        MerchantScope merchantScope,
        OnboardingCaseLifecycle lifecycle,
        OnboardingCaseRevision currentRevision
) {

    public OnboardingCase {
        Objects.requireNonNull(identity, "identity");
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(lifecycle, "lifecycle");
        Objects.requireNonNull(currentRevision, "currentRevision");
    }

    public OnboardingCasePurpose purpose() {
        return OnboardingCasePurpose.INITIAL_CONFIGURATION;
    }
}
