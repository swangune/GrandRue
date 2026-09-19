package grandrue.semantic.capability;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CapabilityCompositionTest {

    @Test
    void capabilities_can_compile_into_independent_semantic_models() {

        Capability booking =
                new Capability("booking");

        Capability payment =
                new Capability("payment");

        CapabilityCompiler compiler =
                new CapabilityCompiler();

        CapabilityCompiler.CompiledCapability bookingResult =
                compiler.compile(
                        booking,
                        List.of(
                                new CapabilityOperation(
                                        "confirm_booking",
                                        "requested",
                                        "confirmed"
                                )
                        )
                );

        CapabilityCompiler.CompiledCapability paymentResult =
                compiler.compile(
                        payment,
                        List.of(
                                new CapabilityOperation(
                                        "request_payment",
                                        "pending",
                                        "paid"
                                )
                        )
                );

        assertEquals(
                "booking",
                bookingResult.resource().identifier()
        );

        assertEquals(
                "payment",
                paymentResult.resource().identifier()
        );
    }

    @Test
    void capability_composition_can_reference_operations_owned_by_different_capabilities() {

        Capability booking =
                new Capability("booking");

        Capability payment =
                new Capability("payment");

        CapabilityCompiler compiler =
                new CapabilityCompiler();

        CapabilityCompiler.CompiledCapability bookingResult =
                compiler.compile(
                        booking,
                        List.of(
                                new CapabilityOperation(
                                        "confirm_booking",
                                        "requested",
                                        "confirmed"
                                )
                        )
                );

        CapabilityCompiler.CompiledCapability paymentResult =
                compiler.compile(
                        payment,
                        List.of(
                                new CapabilityOperation(
                                        "request_payment",
                                        "pending",
                                        "paid"
                                )
                        )
                );

        OperationComposition composition =
                new OperationComposition(
                        bookingResult.resource().operation("confirm_booking"),
                        paymentResult.resource().operation("request_payment")
                );

        assertEquals(
                bookingResult.resource().operation("confirm_booking"),
                composition.trigger()
        );

        assertEquals(
                paymentResult.resource().operation("request_payment"),
                composition.consequence()
        );
    }
}
