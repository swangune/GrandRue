package grandrue.publication;

import grandrue.semantic.compiler.ConfigurationCompiler;
import grandrue.semantic.configuration.ConfigurationImpactAssessment;
import grandrue.semantic.configuration.ConfigurationImpactClassification;
import grandrue.semantic.configuration.ConfigurationImpactContext;
import grandrue.semantic.configuration.ConfigurationImpactContribution;
import grandrue.semantic.configuration.ConfigurationImpactFinding;

import java.util.List;
import java.util.Objects;

/**
 * Publication-owned new-activity impact; membership does not publish or withdraw content
 * and does not determine public Exposure or Enquiry participation.
 * Authority: MS-PROT-040 v1.0 — Merchant Configuration Review, Approval, Activation & Change Model,
 * §21 Impact analysis, §22 Impact classifications, §26 Capability deactivation;
 * MS-PROT-040 v1.1 — Configuration Revision, Resolved Package & Atomic Activation Amendment,
 * §4 Semantic-registry affinity;
 * MS-PROT-046 v1.2 — Production Publication Revision, Exposure & Public Interaction Amendment,
 * §1 Publication ownership remains unchanged, §8 Publication state and Exposure remain distinct.
 */
public final class PublicationAvailabilityImpactAssessment implements ConfigurationImpactAssessment {
    private static final String ENABLED_EFFECT =
            "New publishing activity can use GrandRue Publication where applicable authority permits it.";
    private static final String DISABLED_EFFECT =
            "New publishing activity will no longer be initiated through GrandRue Publication.";

    private final ConfigurationCompiler compiler;

    public PublicationAvailabilityImpactAssessment(ConfigurationCompiler compiler) {
        this.compiler = Objects.requireNonNull(compiler, "compiler");
    }

    @Override
    public ConfigurationImpactContribution assess(ConfigurationImpactContext context) {
        Objects.requireNonNull(context, "context");
        // Historical dependency closure can differ even when direct selections are unchanged.
        boolean previouslyEnabled = context.base().map(compiler::compile)
                .map(model -> model.capabilityIdentifiers().contains("publication"))
                .orElse(false);
        boolean proposedEnabled = context.resolvedPackage().executableSemanticModel()
                .capabilityIdentifiers().contains("publication");
        if (previouslyEnabled == proposedEnabled) {
            return new ConfigurationImpactContribution(List.of(), List.of());
        }

        String effect = proposedEnabled ? ENABLED_EFFECT : DISABLED_EFFECT;
        return new ConfigurationImpactContribution(
                List.of(effect),
                List.of(new ConfigurationImpactFinding(ConfigurationImpactClassification.CONSEQUENTIAL, effect)));
    }
}
