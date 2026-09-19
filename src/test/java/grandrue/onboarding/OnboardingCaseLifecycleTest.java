package grandrue.onboarding;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MS-PROT-052 v1.2 initial Onboarding Case lifecycle contract.
 */
class OnboardingCaseLifecycleTest {

    @Test
    void exposes_exactly_the_accepted_initial_case_lifecycle_states() {
        Set<String> states = Arrays.stream(OnboardingCaseLifecycle.values())
                .map(Enum::name)
                .collect(Collectors.toUnmodifiableSet());

        assertEquals(Set.of("IN_PROGRESS", "SUBMITTED", "COMPLETED", "ABANDONED"), states);
    }

    @Test
    void only_completed_and_abandoned_are_terminal() {
        assertFalse(OnboardingCaseLifecycle.IN_PROGRESS.isTerminal());
        assertFalse(OnboardingCaseLifecycle.SUBMITTED.isTerminal());
        assertTrue(OnboardingCaseLifecycle.COMPLETED.isTerminal());
        assertTrue(OnboardingCaseLifecycle.ABANDONED.isTerminal());
    }
}
