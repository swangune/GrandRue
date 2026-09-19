package grandrue.semantic.capability;

import grandrue.semantic.Operation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CompiledModelCompositionTest {

    @Test
    void compiled_model_rejects_composition_with_uncompiled_operation() {

        Capability booking = new Capability("booking");
        Capability payment = new Capability("payment");

        CapabilityCompiler compiler = new CapabilityCompiler();

        CapabilityCompiler.CompiledCapability bookingResult =
                compiler.compile(
                        booking,
                        List.of(new CapabilityOperation(
                                "confirm_booking",
                                "requested",
                                "confirmed"
                        ))
                );

        CapabilityCompiler.CompiledCapability paymentResult =
                compiler.compile(
                        payment,
                        List.of(new CapabilityOperation(
                                "request_payment",
                                "pending",
                                "paid"
                        ))
                );

        Operation uncompiled =
                new CapabilityCompiler()
                        .compile(
                                new Capability("other"),
                                List.of(new CapabilityOperation(
                                        "unrelated",
                                        "initial",
                                        "done"
                                ))
                        )
                        .resource()
                        .operation("unrelated");

        OperationComposition composition =
                new OperationComposition(
                        bookingResult.resource().operation("confirm_booking"),
                        uncompiled
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> new CapabilityCompiler.CompiledModel(
                        List.of(bookingResult, paymentResult),
                        List.of(composition)
                )
        );
    }
}
