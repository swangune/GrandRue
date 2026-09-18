package grandrue.enquiry;

import grandrue.semantic.compiler.ConfigurationCompiler;
import grandrue.semantic.configuration.ConfigurationImpactAssessment;
import grandrue.semantic.configuration.ConfigurationImpactClassification;
import grandrue.semantic.configuration.ConfigurationImpactContext;
import grandrue.semantic.configuration.ConfigurationImpactContribution;
import grandrue.semantic.configuration.ConfigurationImpactFinding;

import java.util.List;
import java.util.Objects;

/**
 * Assesses Enquiry capability membership for new activity only.
 * Subject participation, public binding, current availability and existing Enquiry history remain independently owned.
 *
 * Authority: MS-PROT-040 v1.0 — Merchant Configuration Review, Approval, Activation & Change Model,
 * §§20–22 impact analysis/classification and §26 capability deactivation;
 * MS-PROT-043 v1.4 — Production Enquiry Submission, Provenance & Merchant Observation Amendment,
 * §§2–6 canonical submission, merchant-general/subject-specific Enquiry and current-participation revalidation;
 * MS-PROT-049 v1.2 — Public Interaction Subject Participation & Binding Amendment,
 * §§4, 17–18 interaction applicability, execution revalidation and stale-binding behaviour.
 */
public final class EnquiryAvailabilityImpactAssessment implements ConfigurationImpactAssessment {
    private static final String ENABLED_EFFECT =
            "New customer enquiries can be accepted through GrandRue where an applicable enquiry interaction is available.";
    private static final String DISABLED_EFFECT =
            "New customer enquiries will no longer be accepted through GrandRue.";

    private final ConfigurationCompiler compiler;

    public EnquiryAvailabilityImpactAssessment(ConfigurationCompiler compiler) {
        this.compiler = Objects.requireNonNull(compiler, "compiler");
    }

    @Override
    public ConfigurationImpactContribution assess(ConfigurationImpactContext context) {
        Objects.requireNonNull(context, "context");
        // Historical dependency closure can differ even when direct selections are unchanged.
        boolean previouslyEnabled = context.base().map(compiler::compile)
                .map(model -> model.capabilityIdentifiers().contains("enquiry"))
                .orElse(false);
        boolean proposedEnabled = context.resolvedPackage().executableSemanticModel()
                .capabilityIdentifiers().contains("enquiry");
        if (previouslyEnabled == proposedEnabled) {
            return new ConfigurationImpactContribution(List.of(), List.of());
        }

        String effect = proposedEnabled ? ENABLED_EFFECT : DISABLED_EFFECT;
        return new ConfigurationImpactContribution(
                List.of(effect),
                List.of(new ConfigurationImpactFinding(ConfigurationImpactClassification.CONSEQUENTIAL, effect)));
    }
}
