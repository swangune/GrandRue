package mainstreet.onboarding;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * MS-PROT-052 exact material question-definition version identity for historical answer affinity.
 */
class OnboardingQuestionDefinitionVersionTest {

    @Test
    void definition_version_is_stable_and_exact() {
        OnboardingQuestionDefinitionVersion version = new OnboardingQuestionDefinitionVersion("definition-v2");

        assertEquals("definition-v2", version.value());
        assertEquals(version, new OnboardingQuestionDefinitionVersion("definition-v2"));
        assertNotEquals(version, new OnboardingQuestionDefinitionVersion("definition-v3"));
    }

    @Test
    void definition_version_must_not_be_blank() {
        assertThrows(IllegalArgumentException.class, () -> new OnboardingQuestionDefinitionVersion(null));
        assertThrows(IllegalArgumentException.class, () -> new OnboardingQuestionDefinitionVersion(""));
        assertThrows(IllegalArgumentException.class, () -> new OnboardingQuestionDefinitionVersion("   "));
    }
}
