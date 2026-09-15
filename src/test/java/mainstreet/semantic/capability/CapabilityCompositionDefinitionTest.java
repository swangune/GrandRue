package mainstreet.semantic.capability;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CapabilityCompositionDefinitionTest {

    @Test
    void composition_definition_uses_capability_and_operation_identifiers() {

        CapabilityCompositionDefinition definition =
                new CapabilityCompositionDefinition(
                        "booking",
                        "confirm_booking",
                        "payment",
                        "request_payment"
                );

        assertEquals("booking", definition.triggerCapability());
        assertEquals("confirm_booking", definition.triggerOperation());
        assertEquals("payment", definition.consequenceCapability());
        assertEquals("request_payment", definition.consequenceOperation());
    }

    @Test
    void composition_definition_rejects_null_identifiers() {

        assertThrows(
                NullPointerException.class,
                () -> new CapabilityCompositionDefinition(
                        null,
                        "confirm_booking",
                        "payment",
                        "request_payment"
                )
        );
    }
}
