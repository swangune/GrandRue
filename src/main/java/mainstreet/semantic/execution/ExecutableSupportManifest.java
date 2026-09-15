package mainstreet.semantic.execution;

import java.util.Objects;
import java.util.Set;

/**
 * Immutable implementation/conformance evidence for one executable path.
 *
 * <p>The manifest is deliberately not semantic authority. It only states the
 * exact contracts for which the path has separately established conformance.</p>
 */
public record ExecutableSupportManifest(
        String implementationPathIdentifier,
        Set<SemanticExecutionContractReference> supportedContracts
) {
    public ExecutableSupportManifest {
        if (implementationPathIdentifier == null
                || implementationPathIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Implementation path identifier must not be blank"
            );
        }
        supportedContracts = Set.copyOf(
                Objects.requireNonNull(supportedContracts, "supportedContracts")
        );
    }

    public boolean supports(ExecutableSupportRequirement requirement) {
        Objects.requireNonNull(requirement, "requirement");
        return supportedContracts.containsAll(requirement.requiredContracts());
    }
}
