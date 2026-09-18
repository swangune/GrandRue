package grandrue.semantic.configuration;

import grandrue.semantic.execution.ExecutableSupportRequirement;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

/** Immutable exact RCP-affined new-activity execution requirement evidence. */
public record ConfigurationNewActivityRequirementSet(
        String merchantIdentifier,
        String configurationRevisionIdentifier,
        String semanticRegistryReleaseIdentifier,
        String resolvedPackageEvidenceIdentifier,
        int canonicalizationVersion,
        String requirementSetIdentifier,
        Set<ExecutableSupportRequirement> requirements,
        Instant evidenceProducedAt
) {
    public ConfigurationNewActivityRequirementSet {
        requireIdentifier(merchantIdentifier, "Merchant identifier");
        requireIdentifier(
                configurationRevisionIdentifier,
                "Configuration Revision identifier"
        );
        requireIdentifier(
                semanticRegistryReleaseIdentifier,
                "Semantic Registry Release identifier"
        );
        requireIdentifier(
                resolvedPackageEvidenceIdentifier,
                "Resolved package evidence identifier"
        );
        if (canonicalizationVersion
                != ConfigurationNewActivityRequirementSetIdentity
                        .CANONICALIZATION_VERSION) {
            throw new IllegalArgumentException(
                    "Unknown requirement-set canonicalization version"
            );
        }
        requireIdentifier(
                requirementSetIdentifier,
                "Requirement-set identifier"
        );
        requirements = Set.copyOf(
                Objects.requireNonNull(requirements, "requirements")
        );
        requireReleaseAffinity(
                semanticRegistryReleaseIdentifier,
                requirements
        );
        if (!requirementSetIdentifier.equals(
                ConfigurationNewActivityRequirementSetIdentity.derive(
                        requirements
                ))) {
            throw new IllegalArgumentException(
                    "Requirement-set identifier does not match its content"
            );
        }
        Objects.requireNonNull(evidenceProducedAt, "evidenceProducedAt");
    }

    private static void requireReleaseAffinity(
            String release,
            Set<ExecutableSupportRequirement> requirements
    ) {
        requirements.forEach(requirement -> {
            requireRelease(
                    release,
                    requirement.affectedExecutionContract()
                            .semanticRegistryReleaseIdentifier()
            );
            requirement.requiredParticipantAndEffectContracts().forEach(
                    contract -> requireRelease(
                            release,
                            contract.semanticRegistryReleaseIdentifier()
                    )
            );
        });
    }

    private static void requireRelease(String expected, String actual) {
        if (!expected.equals(actual)) {
            throw new IllegalArgumentException(
                    "Requirement contract is not affined to the evidence release"
            );
        }
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
