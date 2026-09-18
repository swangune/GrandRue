package grandrue.semantic.executable;

import java.util.Objects;
import java.util.Set;

/** Capability membership together with every reason that caused it. */
public record ExecutableCapabilityActivation(
        String capabilityIdentifier,
        Set<CapabilityActivationReason> reasons
) {

    public ExecutableCapabilityActivation {
        if (capabilityIdentifier == null || capabilityIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Capability identifier must not be blank"
            );
        }
        reasons = Set.copyOf(Objects.requireNonNull(reasons));
        if (reasons.isEmpty()) {
            throw new IllegalArgumentException(
                    "An active capability requires at least one reason"
            );
        }
    }
}
