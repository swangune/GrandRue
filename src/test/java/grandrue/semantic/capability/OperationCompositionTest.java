package grandrue.semantic.capability;

import grandrue.semantic.Operation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class OperationCompositionTest {

    @Test
    void trigger_must_not_be_null() {
        assertThrows(
                NullPointerException.class,
                () -> new OperationComposition(null, operation())
        );
    }

    @Test
    void consequence_must_not_be_null() {
        assertThrows(
                NullPointerException.class,
                () -> new OperationComposition(operation(), null)
        );
    }

    private static Operation operation() {
        Capability capability = new Capability("test");
        return new CapabilityCompiler()
                .compile(
                        capability,
                        java.util.List.of(
                                new CapabilityOperation(
                                        "test_operation",
                                        "requested",
                                        "completed"
                                )
                        )
                )
                .resource()
                .operation("test_operation");
    }
}
