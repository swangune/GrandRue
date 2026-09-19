package grandrue.semantic.capability;

import grandrue.semantic.policy.PolicyDefinition;
import grandrue.semantic.policy.PolicyValue;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CapabilityPolicySelectionTest {

    @Test
    void policy_selection_rejects_value_not_allowed_by_definition() {

        Capability booking = new Capability("booking");
        PolicyValue optional = new PolicyValue(false);

        PolicyDefinition paymentRequired =
                new PolicyDefinition(
                        booking,
                        "payment_required",
                        optional,
                        Set.of(optional)
                );

        CapabilityConfiguration configuration =
                new CapabilityConfiguration(booking);

        assertThrows(
                IllegalArgumentException.class,
                () -> configuration.setPolicy(
                        paymentRequired,
                        new PolicyValue(true)
                )
        );
    }

    @Test
    void policy_selection_accepts_value_allowed_by_definition() {

        Capability booking = new Capability("booking");
        PolicyValue optional = new PolicyValue(false);
        PolicyValue required = new PolicyValue(true);

        PolicyDefinition paymentRequired =
                new PolicyDefinition(
                        booking,
                        "payment_required",
                        optional,
                        Set.of(optional, required)
                );

        CapabilityConfiguration configuration =
                new CapabilityConfiguration(booking);

        configuration.setPolicy(
                paymentRequired,
                required
        );

        assertEquals(
                required,
                configuration.policy("payment_required")
        );
    }
}
