package grandrue.ordering;

import grandrue.semantic.compiler.ConfigurationCompiler;
import grandrue.semantic.configuration.ConfigurationImpactAssessment;
import grandrue.semantic.configuration.ConfigurationImpactClassification;
import grandrue.semantic.configuration.ConfigurationImpactContext;
import grandrue.semantic.configuration.ConfigurationImpactContribution;
import grandrue.semantic.configuration.ConfigurationImpactFinding;

import java.util.List;
import java.util.Objects;

/**
 * Assesses Ordering capability membership for new activity only.
 * Existing Order commitment, release, fulfilment and policy truth remain independently owned.
 *
 * Authority: MS-PROT-040 v1.0 — Merchant Configuration Review, Approval, Activation & Change Model,
 * §§20–22 impact analysis/classification and §26 capability deactivation;
 * composite MS-PROT-077 through v1.1 — Order Commitment, Amendment & Lifecycle Model,
 * Ordering ownership, independent capability activation and historical Order-commitment boundaries.
 */
public final class OrderingAvailabilityImpactAssessment implements ConfigurationImpactAssessment {
    private static final String ENABLED_EFFECT =
            "New orders can be accepted through Main Street where applicable ordering requirements are satisfied.";
    private static final String DISABLED_EFFECT =
            "New orders will no longer be accepted through Main Street.";

    private final ConfigurationCompiler compiler;

    public OrderingAvailabilityImpactAssessment(ConfigurationCompiler compiler) {
        this.compiler = Objects.requireNonNull(compiler, "compiler");
    }

    @Override
    public ConfigurationImpactContribution assess(ConfigurationImpactContext context) {
        Objects.requireNonNull(context, "context");
        // Historical dependency closure can differ even when direct selections are unchanged.
        boolean previouslyEnabled = context.base().map(compiler::compile)
                .map(model -> model.capabilityIdentifiers().contains("ordering"))
                .orElse(false);
        boolean proposedEnabled = context.resolvedPackage().executableSemanticModel()
                .capabilityIdentifiers().contains("ordering");
        if (previouslyEnabled == proposedEnabled) {
            return new ConfigurationImpactContribution(List.of(), List.of());
        }

        String effect = proposedEnabled ? ENABLED_EFFECT : DISABLED_EFFECT;
        return new ConfigurationImpactContribution(
                List.of(effect),
                List.of(new ConfigurationImpactFinding(ConfigurationImpactClassification.CONSEQUENTIAL, effect)));
    }
}
