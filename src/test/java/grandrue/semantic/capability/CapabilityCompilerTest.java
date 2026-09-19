package grandrue.semantic.capability;

import grandrue.semantic.Operation;
import grandrue.semantic.Resource;
import grandrue.semantic.policy.PolicyDefinition;
import grandrue.semantic.policy.PolicyValue;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CapabilityCompilerTest {

    @Test
    void simple_booking_capability_generates_semantic_model() {

        Capability booking =
                new Capability("booking");

        CapabilityCompiler compiler =
                new CapabilityCompiler();

        CapabilityCompiler.CompiledCapability result =
                compiler.compile(
                        booking,
                        List.of(
                                new CapabilityOperation(
                                        "confirm_booking",
                                        "requested",
                                        "confirmed"
                                ),

                                new CapabilityOperation(
                                        "cancel_booking",
                                        "confirmed",
                                        "cancelled"
                                )
                        )
                );

        Resource resource =
                result.resource();

        assertEquals(
                "booking",
                resource.identifier()
        );

        Operation confirm =
                resource.operation(
                        "confirm_booking"
                );

        assertNotNull(confirm);

        assertEquals(
                "requested",
                confirm.transition()
                        .source()
                        .identifier()
        );

        assertEquals(
                "confirmed",
                confirm.transition()
                        .target()
                        .identifier()
        );

        assertEquals(
                2,
                result.operations().size()
        );
    }

    @Test
    void compiled_model_preserves_cross_capability_composition() {

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

        CapabilityCompiler.CompiledModel result =
                compiler.compile(
                        List.of(bookingResult, paymentResult),
                        List.of(composition)
                );

        assertEquals(
                List.of(bookingResult, paymentResult),
                result.capabilities()
        );

        assertEquals(
                List.of(composition),
                result.compositions()
        );
    }

    @Test
    void policy_selection_does_not_create_undeclared_semantics() {

        Capability booking =
                new Capability("booking");

        CapabilityConfiguration configuration =
                new CapabilityConfiguration(booking);

        configuration.setPolicy(
                new PolicyDefinition(
                        booking,
                        "payment_required",
                        new PolicyValue(false),
                        Set.of(
                                new PolicyValue(false),
                                new PolicyValue(true)
                        )
                ),
                new PolicyValue(true)
        );

        CapabilityCompiler compiler =
                new CapabilityCompiler();

        CapabilityCompiler.CompiledCapability result =
                compiler.compile(
                        configuration,
                        List.of(
                                new CapabilityOperation(
                                        "confirm_booking",
                                        "requested",
                                        "confirmed"
                                )
                        )
                );

        assertEquals(
                1,
                result.operations().size()
        );

        assertNotNull(
                result.resource().operation("confirm_booking")
        );

        assertNull(
                result.resource().operation("request_payment")
        );

        assertNull(
                result.resource().state("payment_pending")
        );
    }
}
