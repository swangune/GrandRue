package mainstreet.semantic.configuration;

import mainstreet.semantic.execution.ExecutableSupportRequirement;

import java.util.Objects;

/** Release-affined Semantic Registry mapping for one executable operation. */
public record ConfigurationOperationExecutionRequirement(
        String semanticRegistryReleaseIdentifier,
        String operationIdentifier,
        ExecutableSupportRequirement requirement
) {
    public ConfigurationOperationExecutionRequirement {
        requireIdentifier(
                semanticRegistryReleaseIdentifier,
                "Semantic Registry Release identifier"
        );
        requireIdentifier(operationIdentifier, "Operation identifier");
        Objects.requireNonNull(requirement, "requirement");
        requireRelease(
                semanticRegistryReleaseIdentifier,
                requirement.affectedExecutionContract()
                        .semanticRegistryReleaseIdentifier()
        );
        requirement.requiredParticipantAndEffectContracts().forEach(
                contract -> requireRelease(
                        semanticRegistryReleaseIdentifier,
                        contract.semanticRegistryReleaseIdentifier()
                )
        );
    }

    private static void requireRelease(String expected, String actual) {
        if (!expected.equals(actual)) {
            throw new IllegalArgumentException(
                    "Execution requirement is not affined to its Semantic Registry Release"
            );
        }
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
