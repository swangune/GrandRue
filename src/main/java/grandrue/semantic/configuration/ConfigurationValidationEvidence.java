package grandrue.semantic.configuration;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable evidence that one exact revision/release produced one exact
 * deterministic Resolved Configuration Package result.
 *
 * <p>Reinstatement evidence additionally carries the exact Reinstatement
 * Basis Activation required by MS-PROT-040 v1.8 §§5–6.</p>
 */
public record ConfigurationValidationEvidence(
        String validationEvidenceIdentifier,
        String merchantIdentifier,
        String configurationRevisionIdentifier,
        String semanticRegistryReleaseIdentifier,
        String resolvedPackageEvidenceIdentifier,
        ConfigurationValidationOutcome outcome,
        String compilerIdentifier,
        Instant packageGeneratedAt,
        Instant evidenceProducedAt,
        Optional<String> reinstatementBasisActivationRequestIdentifier
) {

    public ConfigurationValidationEvidence {
        requireIdentifier(
                validationEvidenceIdentifier,
                "Validation evidence identifier"
        );
        requireIdentifier(merchantIdentifier, "Merchant identifier");
        requireIdentifier(
                configurationRevisionIdentifier,
                "Configuration revision identifier"
        );
        requireIdentifier(
                semanticRegistryReleaseIdentifier,
                "Semantic registry release identifier"
        );
        requireIdentifier(
                resolvedPackageEvidenceIdentifier,
                "Resolved package evidence identifier"
        );
        Objects.requireNonNull(outcome, "outcome");
        requireIdentifier(compilerIdentifier, "Compiler identifier");
        Objects.requireNonNull(packageGeneratedAt, "packageGeneratedAt");
        Objects.requireNonNull(evidenceProducedAt, "evidenceProducedAt");
        reinstatementBasisActivationRequestIdentifier =
                Objects.requireNonNull(
                        reinstatementBasisActivationRequestIdentifier,
                        "reinstatementBasisActivationRequestIdentifier"
                );
        reinstatementBasisActivationRequestIdentifier.ifPresent(identifier ->
                requireIdentifier(
                        identifier,
                        "Reinstatement Basis Activation identifier"
                )
        );
    }

    public ConfigurationValidationEvidence(
            String validationEvidenceIdentifier,
            String merchantIdentifier,
            String configurationRevisionIdentifier,
            String semanticRegistryReleaseIdentifier,
            String resolvedPackageEvidenceIdentifier,
            ConfigurationValidationOutcome outcome,
            String compilerIdentifier,
            Instant packageGeneratedAt,
            Instant evidenceProducedAt
    ) {
        this(
                validationEvidenceIdentifier,
                merchantIdentifier,
                configurationRevisionIdentifier,
                semanticRegistryReleaseIdentifier,
                resolvedPackageEvidenceIdentifier,
                outcome,
                compilerIdentifier,
                packageGeneratedAt,
                evidenceProducedAt,
                Optional.empty()
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
