package mainstreet.onboarding;

import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * MS-PROT-052 v1.2 stable Onboarding Case identity, revision and Merchant Account ownership boundary.
 */
class OnboardingCaseOwnershipTest {

    @Test
    void case_identity_is_stable_and_non_blank() {
        OnboardingCaseIdentity identity = new OnboardingCaseIdentity("onboarding-case-1");

        assertEquals("onboarding-case-1", identity.value());
        assertEquals(identity, new OnboardingCaseIdentity("onboarding-case-1"));
        assertNotEquals(identity, new OnboardingCaseIdentity("onboarding-case-2"));
        assertThrows(IllegalArgumentException.class, () -> new OnboardingCaseIdentity(" "));
    }

    @Test
    void case_revision_is_an_exact_non_blank_logical_identity() {
        OnboardingCaseRevision revision = new OnboardingCaseRevision("revision-7");

        assertEquals("revision-7", revision.value());
        assertEquals(revision, new OnboardingCaseRevision("revision-7"));
        assertNotEquals(revision, new OnboardingCaseRevision("revision-8"));
        assertThrows(IllegalArgumentException.class, () -> new OnboardingCaseRevision(" "));
    }

    @Test
    void ordinary_initial_case_is_owned_by_merchant_scope_with_exact_current_revision() {
        MerchantScope merchantScope = new MerchantScope("merchant-1");
        OnboardingCaseRevision revision = new OnboardingCaseRevision("revision-7");
        OnboardingCase onboardingCase = new OnboardingCase(
                new OnboardingCaseIdentity("case-1"),
                merchantScope,
                OnboardingCaseLifecycle.IN_PROGRESS,
                revision
        );

        assertEquals(merchantScope, onboardingCase.merchantScope());
        assertEquals(OnboardingCasePurpose.INITIAL_CONFIGURATION, onboardingCase.purpose());
        assertEquals(OnboardingCaseLifecycle.IN_PROGRESS, onboardingCase.lifecycle());
        assertEquals(revision, onboardingCase.currentRevision());
    }

    @Test
    void case_requires_identity_scope_lifecycle_and_current_revision() {
        MerchantScope merchantScope = new MerchantScope("merchant-1");
        OnboardingCaseIdentity identity = new OnboardingCaseIdentity("case-1");
        OnboardingCaseRevision revision = new OnboardingCaseRevision("revision-1");

        assertThrows(NullPointerException.class,
                () -> new OnboardingCase(null, merchantScope, OnboardingCaseLifecycle.IN_PROGRESS, revision));
        assertThrows(NullPointerException.class,
                () -> new OnboardingCase(identity, null, OnboardingCaseLifecycle.IN_PROGRESS, revision));
        assertThrows(NullPointerException.class,
                () -> new OnboardingCase(identity, merchantScope, null, revision));
        assertThrows(NullPointerException.class,
                () -> new OnboardingCase(identity, merchantScope, OnboardingCaseLifecycle.IN_PROGRESS, null));
    }
}
