package mainstreet.money;

import mainstreet.semantic.compiler.ConfigurationCompiler;
import mainstreet.semantic.configuration.ConfigurationImpactAssessment;
import mainstreet.semantic.configuration.ConfigurationImpactClassification;
import mainstreet.semantic.configuration.ConfigurationImpactContext;
import mainstreet.semantic.configuration.ConfigurationImpactContribution;
import mainstreet.semantic.configuration.ConfigurationImpactFinding;

import java.util.List;
import java.util.Objects;

/**
 * Assesses Payment capability membership for new activity only.
 * Existing obligations, provider evidence, payment applications and refunds remain independently owned Payment truth.
 *
 * Authority: designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md,
 * v1.0 §§20–28 impact analysis, existing-commitment protection and capability deactivation;
 * designs/MS-PROT-055 — Money, Commercial Terms, Payment Obligations & Payment Evidence Model.md,
 * v1.0 §§18–26 committed commercial truth, Payment Obligation and provider-payment boundaries;
 * designs/MS-PROT-055 v1.1 — Payment Obligation, Provider Execution, Reconciliation & Refund Execution Contract Amendment.md,
 * §§1–5 and §§14–25 Payment ownership, immutable obligation truth and execution boundaries.
 */
public final class PaymentAvailabilityImpactAssessment implements ConfigurationImpactAssessment {
    private static final String ENABLED_EFFECT =
            "New payment activity can use Main Street Payment where applicable payment semantics and authority permit it.";
    private static final String DISABLED_EFFECT =
            "New payment activity will no longer be initiated through Main Street Payment.";

    private final ConfigurationCompiler compiler;

    public PaymentAvailabilityImpactAssessment(ConfigurationCompiler compiler) {
        this.compiler = Objects.requireNonNull(compiler, "compiler");
    }

    @Override
    public ConfigurationImpactContribution assess(ConfigurationImpactContext context) {
        Objects.requireNonNull(context, "context");
        // Historical dependency closure can differ even when direct selections are unchanged.
        boolean previouslyEnabled = context.base().map(compiler::compile)
                .map(model -> model.capabilityIdentifiers().contains("payment"))
                .orElse(false);
        boolean proposedEnabled = context.resolvedPackage().executableSemanticModel()
                .capabilityIdentifiers().contains("payment");
        if (previouslyEnabled == proposedEnabled) {
            return new ConfigurationImpactContribution(List.of(), List.of());
        }

        String effect = proposedEnabled ? ENABLED_EFFECT : DISABLED_EFFECT;
        return new ConfigurationImpactContribution(
                List.of(effect),
                List.of(new ConfigurationImpactFinding(ConfigurationImpactClassification.CONSEQUENTIAL, effect)));
    }
}
