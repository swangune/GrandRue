package grandrue.scheduling;

import grandrue.semantic.compiler.ConfigurationCompiler;
import grandrue.semantic.configuration.ConfigurationImpactAssessment;
import grandrue.semantic.configuration.ConfigurationImpactClassification;
import grandrue.semantic.configuration.ConfigurationImpactContext;
import grandrue.semantic.configuration.ConfigurationImpactContribution;
import grandrue.semantic.configuration.ConfigurationImpactFinding;

import java.util.List;
import java.util.Objects;

/**
 * Assesses Scheduling capability membership for new appointment-scheduling activity only.
 * Current schedulability, Business Hours, Scheduling constraints, staff/resource availability,
 * capacity and existing Appointment truth remain independently owned runtime/domain concerns.
 *
 * Authority: MS-PROT-040 v1.0 — Merchant Configuration Review, Approval, Activation & Change Model,
 * §§20–30 configuration impact, existing-commitment protection and Scheduling change semantics;
 * MS-PROT-042 v1.6 — Production Scheduling Evidence, Appointment Admission & Customer Surface Requirement Amendment,
 * §§1–4 Scheduling-owned current Appointment Scheduling Evaluation and non-persistent availability boundary;
 * MS-PROT-050 v1.1 — Business Operating Hours, Scheduling-Time & Digital Contactability Amendment,
 * §§1–4 separation of Business Hours, Scheduling constraints, staff/resource availability and Enquiry.
 */
public final class SchedulingAvailabilityImpactAssessment implements ConfigurationImpactAssessment {
    private static final String ENABLED_EFFECT =
            "New appointment scheduling activity can use Main Street Scheduling where applicable scheduling semantics and authority permit it.";
    private static final String DISABLED_EFFECT =
            "New appointment scheduling activity will no longer be initiated through Main Street Scheduling.";

    private final ConfigurationCompiler compiler;

    public SchedulingAvailabilityImpactAssessment(ConfigurationCompiler compiler) {
        this.compiler = Objects.requireNonNull(compiler, "compiler");
    }

    @Override
    public ConfigurationImpactContribution assess(ConfigurationImpactContext context) {
        Objects.requireNonNull(context, "context");
        // Historical dependency closure can differ even when direct selections are unchanged.
        boolean previouslyEnabled = context.base().map(compiler::compile)
                .map(model -> model.capabilityIdentifiers().contains("scheduling"))
                .orElse(false);
        boolean proposedEnabled = context.resolvedPackage().executableSemanticModel()
                .capabilityIdentifiers().contains("scheduling");
        if (previouslyEnabled == proposedEnabled) {
            return new ConfigurationImpactContribution(List.of(), List.of());
        }

        String effect = proposedEnabled ? ENABLED_EFFECT : DISABLED_EFFECT;
        return new ConfigurationImpactContribution(
                List.of(effect),
                List.of(new ConfigurationImpactFinding(ConfigurationImpactClassification.CONSEQUENTIAL, effect)));
    }
}
