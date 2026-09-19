package grandrue.semantic.capability;


import grandrue.semantic.policy.PolicyDefinition;
import grandrue.semantic.policy.PolicyValue;

import java.util.Set;


public final class BookingPolicies {


    private BookingPolicies() {}


    public static PolicyDefinition paymentRequired(
            Capability owner
    ) {

        return new PolicyDefinition(
                owner,
                "payment_required",
                new PolicyValue(false),
                Set.of(
                        new PolicyValue(false),
                        new PolicyValue(true)
                )
        );
    }
}
