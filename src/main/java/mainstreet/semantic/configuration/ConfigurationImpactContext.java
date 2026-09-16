package mainstreet.semantic.configuration;

import java.util.Objects;
import java.util.Optional;

/**
 * Exact immutable candidate, historical base and validated package presented for analysis.
 * Authority: MS-PROT-040 v1.0 — Merchant Configuration Review, Approval, Activation & Change Model,
 * §20 Configuration diff; §24 Existing commitments retain semantic affinity;
 * MS-PROT-040 v1.3 — Configuration Validation, Impact Review & Approval Evidence Amendment,
 * §11 Impact-Evidence Production Predicate.
 */
public record ConfigurationImpactContext(
        MerchantConfiguration candidate,
        Optional<MerchantConfiguration> base,
        ConfigurationValidationEvidence validation,
        ResolvedConfigurationPackage resolvedPackage
) {
    public ConfigurationImpactContext {
        Objects.requireNonNull(candidate, "candidate");
        Objects.requireNonNull(base, "base");
        Objects.requireNonNull(validation, "validation");
        Objects.requireNonNull(resolvedPackage, "resolvedPackage");
        if (!candidate.merchantIdentifier().equals(validation.merchantIdentifier())
                || !candidate.configurationIdentifier().equals(validation.configurationRevisionIdentifier())
                || !candidate.semanticRegistryVersion().equals(validation.semanticRegistryReleaseIdentifier())
                || !resolvedPackage.provenance().compilerIdentifier().equals(validation.compilerIdentifier())
                || !resolvedPackage.provenance().generatedAt().equals(validation.packageGeneratedAt())) {
            throw new IllegalArgumentException("Impact analysis requires exact validated revision/package affinity");
        }
        resolvedPackage.requireSourceConfiguration(candidate);
        if (!candidate.baseConfigurationIdentifier().equals(base.map(MerchantConfiguration::configurationIdentifier))
                || base.filter(value -> !value.merchantIdentifier().equals(candidate.merchantIdentifier())).isPresent()) {
            throw new IllegalArgumentException("Impact analysis requires the exact merchant-owned base revision");
        }
    }
}
