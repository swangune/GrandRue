package mainstreet.prototype;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PrototypeDiscoverySeedResolverTest {

    private final PrototypeDiscoverySeedResolver resolver =
            PrototypeDiscoverySeedResolver.standard();

    @Test
    void gardener_showcase_and_enquiry_choices_propose_only_publication_and_enquiry() {
        assertEquals(
                Set.of("publication", "enquiry"),
                resolver.resolve(Set.of(
                        PrototypeCustomerInteractionChoice.PUBLISH_INFORMATION,
                        PrototypeCustomerInteractionChoice.SEND_ENQUIRY
                ))
        );
    }

    @Test
    void appointment_choice_adds_appointment_scheduling_and_customer_without_booking() {
        assertEquals(
                Set.of(
                        "publication",
                        "enquiry",
                        "appointment",
                        "scheduling",
                        "customer"
                ),
                resolver.resolve(Set.of(
                        PrototypeCustomerInteractionChoice.PUBLISH_INFORMATION,
                        PrototypeCustomerInteractionChoice.SEND_ENQUIRY,
                        PrototypeCustomerInteractionChoice.ARRANGE_APPOINTMENT
                ))
        );
    }

    @Test
    void place_order_remains_a_candidate_seed_even_before_choice_driven_ordering_assembly() {
        assertEquals(
                Set.of("ordering"),
                resolver.resolve(Set.of(
                        PrototypeCustomerInteractionChoice.PLACE_ORDER
                ))
        );
    }

    @Test
    void nothing_else_for_now_proposes_no_additional_semantic_seed() {
        assertEquals(
                Set.of(),
                resolver.resolve(Set.of(
                        PrototypeCustomerInteractionChoice.NOTHING_ELSE_FOR_NOW
                ))
        );
    }

    @Test
    void nothing_else_for_now_cannot_be_combined_with_positive_interaction_intent() {
        assertThrows(
                IllegalArgumentException.class,
                () -> resolver.resolve(Set.of(
                        PrototypeCustomerInteractionChoice.NOTHING_ELSE_FOR_NOW,
                        PrototypeCustomerInteractionChoice.SEND_ENQUIRY
                ))
        );
    }

    @Test
    void unsupported_other_intent_is_exposed_as_configuration_gap() {
        assertThrows(
                PrototypeConfigurationGapException.class,
                () -> resolver.resolve(Set.of(
                        PrototypeCustomerInteractionChoice.OTHER
                ))
        );
    }
}
