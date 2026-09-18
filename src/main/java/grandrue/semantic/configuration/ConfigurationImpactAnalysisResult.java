package grandrue.semantic.configuration;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Trusted completed impact-analysis result for one exact validation/package.
 * Business-facing content is mandatory; findings may be empty.
 */
public record ConfigurationImpactAnalysisResult(
        String merchantIdentifier,
        String configurationRevisionIdentifier,
        String semanticRegistryReleaseIdentifier,
        String validationEvidenceIdentifier,
        String resolvedPackageEvidenceIdentifier,
        List<String> businessFacingEffects,
        List<ConfigurationImpactFinding> findings,
        Instant completedAt
) {

    public ConfigurationImpactAnalysisResult {
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
                validationEvidenceIdentifier,
                "Validation evidence identifier"
        );
        requireIdentifier(
                resolvedPackageEvidenceIdentifier,
                "Resolved package evidence identifier"
        );
        businessFacingEffects = copyEffects(businessFacingEffects);
        findings = List.copyOf(Objects.requireNonNull(findings, "findings"));
        Objects.requireNonNull(completedAt, "completedAt");
    }

    private static List<String> copyEffects(List<String> effects) {
        List<String> copy = List.copyOf(
                Objects.requireNonNull(effects, "businessFacingEffects")
        );
        if (copy.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one business-facing effect is required"
            );
        }
        copy.forEach(effect -> {
            if (effect.isBlank()) {
                throw new IllegalArgumentException(
                        "Business-facing effect must not be blank"
                );
            }
        });
        return copy;
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
