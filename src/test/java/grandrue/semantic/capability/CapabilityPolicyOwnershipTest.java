package grandrue.semantic.capability;

import grandrue.semantic.policy.PolicyDefinition;
import grandrue.semantic.policy.PolicyValue;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CapabilityPolicyOwnershipTest {

    @Test
    void policy_definition_has_explicit_capability_owner() {

        Capability booking = new Capability("booking");

        PolicyDefinition paymentRequired =
                new PolicyDefinition(
                        booking,
                        "payment_required",
                        new PolicyValue(false),
                        Set.of(new PolicyValue(false))
                );

        assertSame(
                booking,
                paymentRequired.owner()
        );
    }

    @Test
    void capability_configuration_rejects_policy_owned_by_another_capability() {

        Capability booking = new Capability("booking");
        Capability payment = new Capability("payment");

        PolicyDefinition paymentRequired =
                new PolicyDefinition(
                        payment,
                        "payment_required",
                        new PolicyValue(false),
                        Set.of(new PolicyValue(false))
                );

        CapabilityConfiguration bookingConfiguration =
                new CapabilityConfiguration(booking);

        assertThrows(
                IllegalArgumentException.class,
                () -> bookingConfiguration.setPolicy(
                        paymentRequired,
                        new PolicyValue(true)
                )
        );
    }
}
