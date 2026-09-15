package mainstreet.onboarding;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * MS-PROT-052 v1.1 authoritative initial customer-interaction discovery option identities.
 */
class InitialCustomerInteractionDiscoveryOptionTest {

    @Test
    void exposes_exactly_the_v1_1_authoritative_option_identities() {
        Set<String> identities = Arrays.stream(InitialCustomerInteractionDiscoveryOption.values())
                .map(Enum::name)
                .collect(Collectors.toUnmodifiableSet());

        assertEquals(Set.of(
                "PUBLISH_INFORMATION",
                "SEND_ENQUIRY",
                "ARRANGE_APPOINTMENT",
                "RESERVE_SUBJECT",
                "PLACE_ORDER",
                "SUBSCRIBE_UPDATES",
                "NOTHING_ELSE_FOR_NOW",
                "OTHER"
        ), identities);
        assertFalse(identities.contains("VIEW_BUSINESS_INFORMATION"));
    }
}
