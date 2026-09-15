package mainstreet.onboarding;

import java.util.Objects;
import java.util.Set;

/**
 * Determines whether an initial customer-interaction discovery selection is
 * internally consistent under MS-PROT-052 v1.1.
 *
 * <p>This is only the control-outcome contradiction rule. It does not decide
 * answer completeness, semantic seed mapping, configuration gaps or whether
 * an otherwise consistent selection is sufficient for onboarding progress.</p>
 */
public final class InitialCustomerInteractionDiscoverySelectionConsistency {

    public boolean isConsistent(Set<InitialCustomerInteractionDiscoveryOption> selections) {
        Objects.requireNonNull(selections, "selections");

        return !selections.contains(InitialCustomerInteractionDiscoveryOption.NOTHING_ELSE_FOR_NOW)
                || selections.size() == 1;
    }
}
