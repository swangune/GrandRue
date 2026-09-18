package grandrue.semantic.configuration;

import mainstreet.semantic.compiler.ConfigurationCompiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * Preflights owner coverage for changed compiled capability membership, then composes
 * owner contributions. Registration proves coverage, not the truth of owner semantics
 * or completeness of policy, routing, resource and commitment assessments.
 * Authority: MS-PROT-040 v1.0,
 * designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md,
 * §20 Configuration diff, §21 Impact analysis, §26 Capability deactivation;
 * MS-PROT-040 v1.1,
 * designs/MS-PROT-040 v1.1 — Configuration Revision, Resolved Package & Atomic Activation Amendment.md,
 * §4 Semantic-registry affinity;
 * MS-PROT-040 v1.3,
 * designs/MS-PROT-040 v1.3 — Configuration Validation, Impact Review & Approval Evidence Amendment.md,
 * §10 Business-Facing Review Boundary, §11 Impact-Evidence Production Predicate.
 */
public final class CapabilityMembershipImpactAssessment implements ConfigurationImpactAssessment {
    private final ConfigurationCompiler compiler;
    private final Map<String, ConfigurationImpactAssessment> owners;
    private final List<String> ownerOrder;

    public CapabilityMembershipImpactAssessment(ConfigurationCompiler compiler,
                                                Map<String, ConfigurationImpactAssessment> owners) {
        this.compiler = Objects.requireNonNull(compiler, "compiler");
        this.owners = Map.copyOf(owners);
        if (this.owners.keySet().stream().anyMatch(String::isBlank)) {
            throw new IllegalArgumentException("Capability owner identifier must not be blank");
        }
        this.ownerOrder = this.owners.keySet().stream().sorted().toList();
    }

    @Override
    public ConfigurationImpactContribution assess(ConfigurationImpactContext context) {
        Objects.requireNonNull(context, "context");
        var before = context.base().map(compiler::compile)
                .map(model -> model.capabilityIdentifiers()).orElseGet(Set::of);
        var after = context.resolvedPackage().executableSemanticModel().capabilityIdentifiers();
        var changed = new TreeSet<>(before);
        changed.addAll(after);
        changed.removeIf(identifier -> before.contains(identifier) == after.contains(identifier));
        for (var identifier : changed) {
            if (!owners.containsKey(identifier)) {
                throw new IllegalStateException("Missing owner impact assessment for capability: " + identifier);
            }
        }

        var effects = new ArrayList<String>();
        var findings = new ArrayList<ConfigurationImpactFinding>();
        // Unchanged/disabled membership can still require owner-backed residual management.
        for (var identifier : ownerOrder) {
            var contribution = Objects.requireNonNull(owners.get(identifier).assess(context),
                    "Capability owner assessment did not complete");
            effects.addAll(contribution.businessFacingEffects());
            findings.addAll(contribution.findings());
        }
        return new ConfigurationImpactContribution(effects, findings);
    }
}
