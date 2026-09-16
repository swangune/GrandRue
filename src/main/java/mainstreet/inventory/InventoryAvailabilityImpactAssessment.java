package mainstreet.inventory;

import mainstreet.semantic.compiler.ConfigurationCompiler;
import mainstreet.semantic.configuration.ConfigurationImpactAssessment;
import mainstreet.semantic.configuration.ConfigurationImpactClassification;
import mainstreet.semantic.configuration.ConfigurationImpactContext;
import mainstreet.semantic.configuration.ConfigurationImpactContribution;
import mainstreet.semantic.configuration.ConfigurationImpactFinding;

import java.util.List;
import java.util.Objects;

/**
 * Assesses Inventory capability membership for new activity only.
 * Existing stock positions, claims, resolutions and movements remain independently owned Inventory truth.
 *
 * Authority: MS-PROT-040 v1.0 — Merchant Configuration Review, Approval, Activation & Change Model,
 * §§20–23 and §26 impact analysis, existing-commitment protection and capability deactivation;
 * MS-PROT-058 v1.0 — Inventory Authority, Stock Claims & Availability Model, §§1–9;
 * MS-PROT-058 v1.1 — Inventory Position, Mutation & Quantity Claim Execution Contract Amendment,
 * §§1–3 Inventory ownership, optionality and surviving authority boundaries.
 */
public final class InventoryAvailabilityImpactAssessment implements ConfigurationImpactAssessment {
    private static final String ENABLED_EFFECT =
            "New inventory-tracked activity can use Main Street Inventory where applicable inventory requirements are satisfied.";
    private static final String DISABLED_EFFECT =
            "New inventory-tracked activity will no longer be initiated through Main Street Inventory.";

    private final ConfigurationCompiler compiler;

    public InventoryAvailabilityImpactAssessment(ConfigurationCompiler compiler) {
        this.compiler = Objects.requireNonNull(compiler, "compiler");
    }

    @Override
    public ConfigurationImpactContribution assess(ConfigurationImpactContext context) {
        Objects.requireNonNull(context, "context");
        // Historical dependency closure can differ even when direct selections are unchanged.
        boolean previouslyEnabled = context.base().map(compiler::compile)
                .map(model -> model.capabilityIdentifiers().contains("inventory"))
                .orElse(false);
        boolean proposedEnabled = context.resolvedPackage().executableSemanticModel()
                .capabilityIdentifiers().contains("inventory");
        if (previouslyEnabled == proposedEnabled) {
            return new ConfigurationImpactContribution(List.of(), List.of());
        }

        String effect = proposedEnabled ? ENABLED_EFFECT : DISABLED_EFFECT;
        return new ConfigurationImpactContribution(
                List.of(effect),
                List.of(new ConfigurationImpactFinding(ConfigurationImpactClassification.CONSEQUENTIAL, effect)));
    }
}
