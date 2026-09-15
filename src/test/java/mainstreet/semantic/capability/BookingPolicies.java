package mainstreet.semantic.capability;


import mainstreet.semantic.policy.PolicyDefinition;
import mainstreet.semantic.policy.PolicyValue;

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
