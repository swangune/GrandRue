package grandrue.semantic.configuration;

import java.time.Instant;
import java.util.Objects;

/** Trusted request to derive and retain exact requirements for one RCP. */
public record RecordConfigurationNewActivityRequirementSetCommand(
        String resolvedPackageEvidenceIdentifier,
        ResolvedConfigurationPackage resolvedPackage,
        Instant evidenceProducedAt
) {
    public RecordConfigurationNewActivityRequirementSetCommand {
        if (resolvedPackageEvidenceIdentifier == null
                || resolvedPackageEvidenceIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Resolved package evidence identifier must not be blank"
            );
        }
        Objects.requireNonNull(resolvedPackage, "resolvedPackage");
        Objects.requireNonNull(evidenceProducedAt, "evidenceProducedAt");
    }
}
