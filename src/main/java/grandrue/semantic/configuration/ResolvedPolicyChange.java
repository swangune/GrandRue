package grandrue.semantic.configuration;

import java.util.Objects;
import java.util.Optional;

/**
 * A change in applicable value or governing release, scoped to the policy's owner.
 * Absence means no applicable value; it is not an inferred default.
 * Authority: MS-PROT-047 — Capability Configuration Contract, v1.0 §4 Semantic ownership,
 * §11 Resolution states and provenance;
 * MS-PROT-040 v1.1 — Configuration Revision, Resolved Package & Atomic Activation Amendment,
 * §4 Semantic-registry affinity.
 */
public record ResolvedPolicyChange(
        CapabilityConfigurationDecisionIdentity identity,
        Optional<EffectiveValue> before,
        Optional<EffectiveValue> after
) {
    public ResolvedPolicyChange {
        Objects.requireNonNull(identity, "identity");
        Objects.requireNonNull(before, "before");
        Objects.requireNonNull(after, "after");
        if (before.equals(after)) {
            throw new IllegalArgumentException("A policy change requires different applicable values or releases");
        }
    }

    public record EffectiveValue(String semanticRegistryReleaseIdentifier, String value) {
        public EffectiveValue {
            if (semanticRegistryReleaseIdentifier == null || semanticRegistryReleaseIdentifier.isBlank()
                    || value == null || value.isBlank()) {
                throw new IllegalArgumentException("Effective policy value and governing release must not be blank");
            }
        }
    }
}
