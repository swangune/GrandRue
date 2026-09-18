package grandrue.runtime;

import java.util.Objects;
import java.util.Set;

/**
 * Rejects dispatch when one or more applicable operation requirements lack
 * satisfaction evidence in the current command context.
 */
public final class UnsatisfiedOperationRequirementsException
        extends RuntimeException {

    private final Set<String> requirementIdentifiers;

    public UnsatisfiedOperationRequirementsException(
            Set<String> requirementIdentifiers
    ) {
        super("Unsatisfied operation requirements: "
                + Objects.requireNonNull(requirementIdentifiers));
        this.requirementIdentifiers = Set.copyOf(requirementIdentifiers);
        if (this.requirementIdentifiers.isEmpty()) {
            throw new IllegalArgumentException(
                    "Unsatisfied requirements must not be empty"
            );
        }
    }

    public Set<String> requirementIdentifiers() {
        return requirementIdentifiers;
    }
}
