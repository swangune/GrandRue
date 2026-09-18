package mainstreet.semantic.configuration;

import mainstreet.semantic.compiler.ConfigurationCompiler;
import grandrue.semantic.executable.ExecutableMerchantModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;

/**
 * Finds effective policy changes and requires owner coverage before assessment.
 * It does not derive business meaning from policy identifiers or enum spellings.
 * Authority: designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md,
 * v1.0 §§20–22 configuration diff, impact analysis and classifications;
 * designs/MS-PROT-047 — Capability Configuration Contract.md, v1.0 §§4–5 and §11;
 * designs/MS-PROT-040 v1.3 — Configuration Validation, Impact Review & Approval Evidence Amendment.md,
 * §§10–11 business-facing review and completed analysis.
 */
public final class ResolvedPolicyImpactAssessment implements ConfigurationImpactAssessment {
    private final ConfigurationCompiler compiler;
    private final Map<CapabilityConfigurationDecisionIdentity, PolicyImpactAssessment> owners;

    public ResolvedPolicyImpactAssessment(ConfigurationCompiler compiler,
                                         Map<CapabilityConfigurationDecisionIdentity, PolicyImpactAssessment> owners) {
        this.compiler = Objects.requireNonNull(compiler, "compiler");
        this.owners = Map.copyOf(owners);
    }

    @Override
    public ConfigurationImpactContribution assess(ConfigurationImpactContext context) {
        Objects.requireNonNull(context, "context");
        var before = context.base().map(compiler::compile).map(ResolvedPolicyImpactAssessment::effectiveValues)
                .orElseGet(Map::of);
        var after = effectiveValues(context.resolvedPackage().executableSemanticModel());
        var identities = new TreeSet<>(Comparator.comparing(CapabilityConfigurationDecisionIdentity::ownerCapabilityIdentifier)
                .thenComparing(CapabilityConfigurationDecisionIdentity::decisionIdentifier));
        identities.addAll(before.keySet());
        identities.addAll(after.keySet());
        var changes = new ArrayList<ResolvedPolicyChange>();
        for (var identity : identities) {
            if (!Objects.equals(before.get(identity), after.get(identity))) {
                changes.add(new ResolvedPolicyChange(identity, Optional.ofNullable(before.get(identity)),
                        Optional.ofNullable(after.get(identity))));
            }
        }
        // Check the whole set before any owner is invoked; partial registration is not coverage.
        for (var change : changes) {
            if (!owners.containsKey(change.identity())) {
                throw new IllegalStateException("Missing owner impact assessment for policy: " + change.identity());
            }
        }
        var effects = new ArrayList<String>();
        var findings = new ArrayList<ConfigurationImpactFinding>();
        for (var change : changes) {
            var contribution = Objects.requireNonNull(owners.get(change.identity()).assess(context, change),
                    "Policy owner assessment did not complete");
            if (contribution.businessFacingEffects().isEmpty()) {
                throw new IllegalStateException("Changed policy requires business-facing review content: " + change.identity());
            }
            effects.addAll(contribution.businessFacingEffects());
            findings.addAll(contribution.findings());
        }
        return new ConfigurationImpactContribution(effects, findings);
    }

    private static Map<CapabilityConfigurationDecisionIdentity, ResolvedPolicyChange.EffectiveValue> effectiveValues(
            ExecutableMerchantModel model) {
        var values = new HashMap<CapabilityConfigurationDecisionIdentity, ResolvedPolicyChange.EffectiveValue>();
        for (var policy : model.policies()) {
            policy.value().ifPresent(value -> values.put(new CapabilityConfigurationDecisionIdentity(
                    policy.ownerCapabilityIdentifier(), policy.policyIdentifier()),
                    new ResolvedPolicyChange.EffectiveValue(model.semanticRegistryVersion(), value)));
        }
        return Map.copyOf(values);
    }
}
