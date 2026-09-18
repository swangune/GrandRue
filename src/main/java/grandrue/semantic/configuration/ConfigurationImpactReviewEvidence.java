package grandrue.semantic.configuration;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable exact business-facing impact-review evidence.
 *
 * <p>Reinstatement evidence additionally carries the exact Reinstatement
 * Basis Activation required by MS-PROT-040 v1.8 §§5–6.</p>
 */
public record ConfigurationImpactReviewEvidence(
        String impactReviewEvidenceIdentifier,
        String merchantIdentifier,
        String configurationRevisionIdentifier,
        String semanticRegistryReleaseIdentifier,
        String validationEvidenceIdentifier,
        String resolvedPackageEvidenceIdentifier,
        List<String> businessFacingEffects,
        List<ConfigurationImpactFinding> findings,
        Instant impactAnalysisCompletedAt,
        Optional<String> reinstatementBasisActivationRequestIdentifier
) {

    public ConfigurationImpactReviewEvidence {
        requireIdentifier(
                impactReviewEvidenceIdentifier,
                "Impact-review evidence identifier"
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
                validationEvidenceIdentifier,
                "Validation evidence identifier"
        );
        requireIdentifier(
                resolvedPackageEvidenceIdentifier,
                "Resolved package evidence identifier"
        );
        businessFacingEffects = copyEffects(businessFacingEffects);
        findings = List.copyOf(Objects.requireNonNull(findings, "findings"));
        Objects.requireNonNull(
                impactAnalysisCompletedAt,
                "impactAnalysisCompletedAt"
        );
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

    public ConfigurationImpactReviewEvidence(
            String impactReviewEvidenceIdentifier,
            String merchantIdentifier,
            String configurationRevisionIdentifier,
            String semanticRegistryReleaseIdentifier,
            String validationEvidenceIdentifier,
            String resolvedPackageEvidenceIdentifier,
            List<String> businessFacingEffects,
            List<ConfigurationImpactFinding> findings,
            Instant impactAnalysisCompletedAt
    ) {
        this(
                impactReviewEvidenceIdentifier,
                merchantIdentifier,
                configurationRevisionIdentifier,
                semanticRegistryReleaseIdentifier,
                validationEvidenceIdentifier,
                resolvedPackageEvidenceIdentifier,
                businessFacingEffects,
                findings,
                impactAnalysisCompletedAt,
                Optional.empty()
        );
    }

    public boolean hasBlockingFinding() {
        return findings.stream().anyMatch(finding ->
                finding.classification()
                        == ConfigurationImpactClassification.BLOCKING
        );
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
