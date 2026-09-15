package mainstreet.semantic.execution;

import java.util.Set;

/**
 * Raised when a serving/deployment plan would leave a still-required semantic
 * execution contract without any proven executable path.
 */
public final class ExecutableSupportCoverageException
        extends IllegalStateException {

    private final Set<ExecutableSupportRequirement> uncoveredRequirements;

    public ExecutableSupportCoverageException(
            Set<ExecutableSupportRequirement> uncoveredRequirements
    ) {
        super("Executable support coverage is incomplete for "
                + uncoveredRequirements.size() + " requirement(s)");
        this.uncoveredRequirements = Set.copyOf(uncoveredRequirements);
    }

    public Set<ExecutableSupportRequirement> uncoveredRequirements() {
        return uncoveredRequirements;
    }
}
