package grandrue.fulfilment;

import mainstreet.application.MerchantScope;
import mainstreet.semantic.compiler.ConfigurationCompiler;
import mainstreet.semantic.configuration.ConfigurationImpactAssessment;
import mainstreet.semantic.configuration.ConfigurationImpactClassification;
import mainstreet.semantic.configuration.ConfigurationImpactContext;
import mainstreet.semantic.configuration.ConfigurationImpactContribution;
import mainstreet.semantic.configuration.ConfigurationImpactFinding;
import mainstreet.semantic.configuration.MerchantConfiguration;
import mainstreet.semantic.release.SemanticReleaseAssemblyRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Assesses static Fulfilment requirement/binding consequences for new activity.
 *
 * <p>The validated candidate RCP supplies candidate truth. Historical truth is
 * reconstructed from the base configuration's exact semantic release and exact
 * merchant binding-set revision. Mutable current routing, provider health and
 * credentials are deliberately outside this assessment.</p>
 *
 * <p>Authority: MS-PROT-040 v1.0 §§20–24; MS-PROT-040 v1.3 §§9–11;
 * MS-PROT-048 v1.2 §§4–6, 12–18; MS-PROT-048 v1.3 §§1–2, 9–18.</p>
 */
public final class FulfilmentRoutingImpactAssessment
        implements ConfigurationImpactAssessment {

    static final String RESPONSIBILITY_EFFECT =
            "The supporting services required for new activity will change.";
    static final String ROUTING_EFFECT =
            "The provider or internal service used to carry out supporting work for new activity will change.";

    private final ConfigurationCompiler compiler;
    private final SemanticReleaseAssemblyRepository releases;
    private final FulfilmentBindingSetRevisionAuthority bindingSets;

    public FulfilmentRoutingImpactAssessment(
            ConfigurationCompiler compiler,
            SemanticReleaseAssemblyRepository releases,
            FulfilmentBindingSetRevisionAuthority bindingSets
    ) {
        this.compiler = Objects.requireNonNull(compiler, "compiler");
        this.releases = Objects.requireNonNull(releases, "releases");
        this.bindingSets = Objects.requireNonNull(bindingSets, "bindingSets");
    }

    @Override
    public ConfigurationImpactContribution assess(ConfigurationImpactContext context) {
        Objects.requireNonNull(context, "context");

        FulfilmentPlan before = context.base()
                .map(this::resolveHistoricalPlan)
                .orElseGet(FulfilmentPlan::empty);
        FulfilmentPlan after = context.resolvedPackage().fulfilmentPlan();

        Map<BindingScope, BindingMeaning> beforeBindings = index(before);
        Map<BindingScope, BindingMeaning> afterBindings = index(after);

        boolean responsibilityChanged = responsibilityChanged(
                beforeBindings,
                afterBindings
        );
        boolean routingChanged = routingChanged(beforeBindings, afterBindings);

        if (!responsibilityChanged
                && !routingChanged
                && context.base().isPresent()
                && !context.base().orElseThrow().semanticRegistryVersion().equals(
                        context.candidate().semanticRegistryVersion()
                )
                && (!beforeBindings.isEmpty() || !afterBindings.isEmpty())) {
            throw new IllegalStateException(
                    "Fulfilment equivalence across semantic releases is not proven"
            );
        }

        var effects = new ArrayList<String>();
        var findings = new ArrayList<ConfigurationImpactFinding>();
        if (responsibilityChanged) {
            addConsequential(RESPONSIBILITY_EFFECT, effects, findings);
        }
        if (routingChanged) {
            addConsequential(ROUTING_EFFECT, effects, findings);
        }
        return new ConfigurationImpactContribution(effects, findings);
    }

    private FulfilmentPlan resolveHistoricalPlan(MerchantConfiguration base) {
        Optional<FulfilmentBindingSetRevisionReference> reference =
                base.fulfilmentBindingSetRevisionReference();
        if (reference.isEmpty()) {
            return FulfilmentPlan.empty();
        }

        var executableModel = compiler.compile(base);
        var release = releases.release(base.semanticRegistryVersion())
                .orElseThrow(() -> new IllegalStateException(
                        "Historical semantic release assembly is unavailable: "
                                + base.semanticRegistryVersion()
                ));
        MerchantScope merchantScope = new MerchantScope(base.merchantIdentifier());
        FulfilmentBindingSetRevision bindingSet = bindingSets.revision(
                        merchantScope,
                        reference.orElseThrow()
                )
                .orElseThrow(() -> new IllegalStateException(
                        "Historical fulfilment binding-set revision is unavailable: "
                                + reference.orElseThrow().provenanceIdentifier()
                ));

        return new FulfilmentPlanResolver(release.fulfilmentRegistry()).resolve(
                base,
                executableModel,
                bindingSet
        );
    }

    private static Map<BindingScope, BindingMeaning> index(FulfilmentPlan plan) {
        Map<BindingScope, BindingMeaning> indexed = new HashMap<>();
        for (ResolvedFulfilmentBinding binding : plan.bindings()) {
            BindingScope scope = new BindingScope(
                    binding.roleIdentity(),
                    binding.semanticContextIdentifier()
            );
            BindingMeaning meaning = new BindingMeaning(
                    binding.requiredObligations(),
                    binding.fulfillerKind(),
                    binding.fulfillerIdentity(),
                    binding.providerConnectionIdentity()
            );
            if (indexed.putIfAbsent(scope, meaning) != null) {
                throw new IllegalStateException(
                        "Resolved fulfilment plan contains duplicate role/context scope"
                );
            }
        }
        return Map.copyOf(indexed);
    }

    private static boolean responsibilityChanged(
            Map<BindingScope, BindingMeaning> before,
            Map<BindingScope, BindingMeaning> after
    ) {
        if (!before.keySet().equals(after.keySet())) {
            return true;
        }
        return before.keySet().stream().anyMatch(scope ->
                !before.get(scope).requiredObligations().equals(
                        after.get(scope).requiredObligations()
                ));
    }

    private static boolean routingChanged(
            Map<BindingScope, BindingMeaning> before,
            Map<BindingScope, BindingMeaning> after
    ) {
        Set<BindingScope> common = new HashSet<>(before.keySet());
        common.retainAll(after.keySet());
        return common.stream().anyMatch(scope ->
                !before.get(scope).routing().equals(after.get(scope).routing()));
    }

    private static void addConsequential(
            String effect,
            ArrayList<String> effects,
            ArrayList<ConfigurationImpactFinding> findings
    ) {
        effects.add(effect);
        findings.add(new ConfigurationImpactFinding(
                ConfigurationImpactClassification.CONSEQUENTIAL,
                effect
        ));
    }

    private record BindingScope(
            FulfilmentRoleIdentity roleIdentity,
            Optional<String> semanticContextIdentifier
    ) {
        private BindingScope {
            Objects.requireNonNull(roleIdentity, "roleIdentity");
            Objects.requireNonNull(
                    semanticContextIdentifier,
                    "semanticContextIdentifier"
            );
        }
    }

    private record BindingMeaning(
            Set<String> requiredObligations,
            FulfillerKind fulfillerKind,
            String fulfillerIdentity,
            Optional<String> providerConnectionIdentity
    ) {
        private BindingMeaning {
            requiredObligations = Set.copyOf(Objects.requireNonNull(
                    requiredObligations,
                    "requiredObligations"
            ));
            Objects.requireNonNull(fulfillerKind, "fulfillerKind");
            Objects.requireNonNull(fulfillerIdentity, "fulfillerIdentity");
            Objects.requireNonNull(
                    providerConnectionIdentity,
                    "providerConnectionIdentity"
            );
        }

        private Routing routing() {
            return new Routing(
                    fulfillerKind,
                    fulfillerIdentity,
                    providerConnectionIdentity
            );
        }
    }

    private record Routing(
            FulfillerKind fulfillerKind,
            String fulfillerIdentity,
            Optional<String> providerConnectionIdentity
    ) {
    }
}
