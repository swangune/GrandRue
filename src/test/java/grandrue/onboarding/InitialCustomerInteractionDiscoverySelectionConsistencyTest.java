package grandrue.onboarding;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MS-PROT-052 v1.1 initial discovery control-outcome consistency.
 */
class InitialCustomerInteractionDiscoverySelectionConsistencyTest {

    private final InitialCustomerInteractionDiscoverySelectionConsistency consistency =
            new InitialCustomerInteractionDiscoverySelectionConsistency();

    @Test
    void nothing_else_for_now_is_consistent_when_selected_alone() {
        assertTrue(consistency.isConsistent(Set.of(
                InitialCustomerInteractionDiscoveryOption.NOTHING_ELSE_FOR_NOW
        )));
    }

    @Test
    void nothing_else_for_now_conflicts_with_positive_additional_interaction() {
        assertFalse(consistency.isConsistent(Set.of(
                InitialCustomerInteractionDiscoveryOption.NOTHING_ELSE_FOR_NOW,
                InitialCustomerInteractionDiscoveryOption.SEND_ENQUIRY
        )));
    }

    @Test
    void nothing_else_for_now_conflicts_with_other() {
        assertFalse(consistency.isConsistent(Set.of(
                InitialCustomerInteractionDiscoveryOption.NOTHING_ELSE_FOR_NOW,
                InitialCustomerInteractionDiscoveryOption.OTHER
        )));
    }

    @Test
    void multiple_positive_additional_interactions_are_consistent() {
        assertTrue(consistency.isConsistent(Set.of(
                InitialCustomerInteractionDiscoveryOption.PUBLISH_INFORMATION,
                InitialCustomerInteractionDiscoveryOption.SEND_ENQUIRY,
                InitialCustomerInteractionDiscoveryOption.SUBSCRIBE_UPDATES
        )));
    }

    @Test
    void other_may_coexist_with_a_positive_selection_without_creating_the_control_conflict() {
        assertTrue(consistency.isConsistent(Set.of(
                InitialCustomerInteractionDiscoveryOption.OTHER,
                InitialCustomerInteractionDiscoveryOption.ARRANGE_APPOINTMENT
        )));
    }
}
