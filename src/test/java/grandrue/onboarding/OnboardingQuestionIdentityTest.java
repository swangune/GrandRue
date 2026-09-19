package grandrue.onboarding;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * MS-PROT-052 stable onboarding question identity contract.
 */
class OnboardingQuestionIdentityTest {

    @Test
    void identity_is_the_namespace_and_identifier_pair() {
        OnboardingQuestionIdentity identity = new OnboardingQuestionIdentity(
                "onboarding.discovery",
                "customer-interactions"
        );

        assertEquals("onboarding.discovery", identity.namespace());
        assertEquals("customer-interactions", identity.identifier());
        assertEquals(
                new OnboardingQuestionIdentity("onboarding.discovery", "customer-interactions"),
                identity
        );
        assertNotEquals(
                new OnboardingQuestionIdentity("onboarding.configuration", "customer-interactions"),
                identity
        );
    }

    @Test
    void namespace_and_identifier_are_required_non_blank_identity_parts() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new OnboardingQuestionIdentity("   ", "customer-interactions")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new OnboardingQuestionIdentity("onboarding.discovery", "   ")
        );
    }
}
