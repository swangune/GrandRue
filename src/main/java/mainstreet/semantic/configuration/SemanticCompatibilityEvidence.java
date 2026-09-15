package mainstreet.semantic.configuration;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Immutable authority evidence for one directional, reference-scoped semantic
 * transition. Migration dispositions additionally bind the migration and,
 * where required, explicit decision evidence that produced the target
 * candidate. The candidate still passes through the ordinary configuration
 * approval and activation lifecycle.
 */
public record SemanticCompatibilityEvidence(
        String merchantIdentifier,
        String sourceReleaseIdentifier,
        String targetReleaseIdentifier,
        String sourceSemanticRegistryVersion,
        String targetSemanticRegistryVersion,
        Set<String> referenceScope,
        SemanticCompatibilityDisposition disposition,
        Optional<String> migrationEvidenceIdentifier,
        Optional<String> decisionEvidenceIdentifier
) {

    public SemanticCompatibilityEvidence {
        requireIdentifier(merchantIdentifier, "Merchant identifier");
        requireIdentifier(sourceReleaseIdentifier, "Source release identifier");
        requireIdentifier(targetReleaseIdentifier, "Target release identifier");
        requireIdentifier(
                sourceSemanticRegistryVersion,
                "Source semantic registry version"
        );
        requireIdentifier(
                targetSemanticRegistryVersion,
                "Target semantic registry version"
        );
        if (sourceReleaseIdentifier.equals(targetReleaseIdentifier)) {
            throw new IllegalArgumentException(
                    "Semantic compatibility evidence requires distinct releases"
            );
        }
        if (sourceSemanticRegistryVersion.equals(targetSemanticRegistryVersion)) {
            throw new IllegalArgumentException(
                    "Semantic compatibility evidence requires a registry transition"
            );
        }
        referenceScope = Set.copyOf(Objects.requireNonNull(
                referenceScope,
                "referenceScope"
        ));
        disposition = Objects.requireNonNull(disposition, "disposition");
        migrationEvidenceIdentifier = Objects.requireNonNull(
                migrationEvidenceIdentifier,
                "migrationEvidenceIdentifier"
        );
        decisionEvidenceIdentifier = Objects.requireNonNull(
                decisionEvidenceIdentifier,
                "decisionEvidenceIdentifier"
        );
        migrationEvidenceIdentifier.ifPresent(
                identifier -> requireIdentifier(identifier, "Migration evidence identifier")
        );
        decisionEvidenceIdentifier.ifPresent(
                identifier -> requireIdentifier(identifier, "Decision evidence identifier")
        );

        if (disposition == SemanticCompatibilityDisposition.MIGRATABLE_PRESERVING_INTENT
                && migrationEvidenceIdentifier.isEmpty()) {
            throw new IllegalArgumentException(
                    "Intent-preserving migration requires migration evidence"
            );
        }
        if (disposition == SemanticCompatibilityDisposition.MIGRATION_REQUIRES_DECISION
                && (migrationEvidenceIdentifier.isEmpty()
                || decisionEvidenceIdentifier.isEmpty())) {
            throw new IllegalArgumentException(
                    "Decision-requiring migration requires migration and decision evidence"
            );
        }
    }

    public boolean matches(
            ConfigurationRelease source,
            ConfigurationRelease target
    ) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(target, "target");
        return merchantIdentifier.equals(source.merchantIdentifier())
                && merchantIdentifier.equals(target.merchantIdentifier())
                && sourceReleaseIdentifier.equals(source.releaseIdentifier())
                && targetReleaseIdentifier.equals(target.releaseIdentifier())
                && sourceSemanticRegistryVersion.equals(
                        source.semanticRegistryVersion()
                )
                && targetSemanticRegistryVersion.equals(
                        target.semanticRegistryVersion()
                )
                && referenceScope.equals(
                        SemanticCompatibilityReferenceScope.forSource(source)
                );
    }

    public boolean permitsActivation() {
        return switch (disposition) {
            case UNAFFECTED, SEMANTICALLY_EQUIVALENT -> true;
            case MIGRATABLE_PRESERVING_INTENT ->
                    migrationEvidenceIdentifier.isPresent();
            case MIGRATION_REQUIRES_DECISION ->
                    migrationEvidenceIdentifier.isPresent()
                            && decisionEvidenceIdentifier.isPresent();
            case INCOMPATIBLE -> false;
        };
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
