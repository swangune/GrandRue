package grandrue.semantic.capability;

import java.util.Objects;

/**
 * Declarative relationship between operations owned by two capabilities.
 * It contains semantic identifiers only; compiled runtime operations are
 * resolved by the compiler.
 */
public record CapabilityCompositionDefinition(
        String triggerCapability,
        String triggerOperation,
        String consequenceCapability,
        String consequenceOperation) {

    public CapabilityCompositionDefinition {
        Objects.requireNonNull(triggerCapability);
        Objects.requireNonNull(triggerOperation);
        Objects.requireNonNull(consequenceCapability);
        Objects.requireNonNull(consequenceOperation);
    }
}
