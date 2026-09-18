package grandrue.deployment;

import grandrue.semantic.execution.ExecutableSupportRequirement;

import java.util.Objects;
import java.util.Set;

/** Exact technical serving obligation retained for one active Configuration. */
public record ActiveConfigurationServingRequirement(
        String merchantIdentifier,
        String configurationRevisionIdentifier,
        String semanticRegistryReleaseIdentifier,
        String packagedBundleContentDigest,
        String requirementSetIdentifier,
        Set<ExecutableSupportRequirement> requirements
) {
    public ActiveConfigurationServingRequirement {
        require(merchantIdentifier, "Merchant identifier");
        require(configurationRevisionIdentifier, "Configuration Revision identifier");
        require(semanticRegistryReleaseIdentifier, "Semantic release identifier");
        require(packagedBundleContentDigest, "Bundle content digest");
        require(requirementSetIdentifier, "Requirement-set identifier");
        requirements = Set.copyOf(Objects.requireNonNull(
                requirements,
                "requirements"
        ));
        requirements.forEach(requirement -> requirement.requiredContracts()
                .forEach(contract -> {
                    if (!semanticRegistryReleaseIdentifier.equals(
                            contract.semanticRegistryReleaseIdentifier()
                    )) {
                        throw new IllegalArgumentException(
                                "Active requirement is not affined to its release"
                        );
                    }
                }));
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
