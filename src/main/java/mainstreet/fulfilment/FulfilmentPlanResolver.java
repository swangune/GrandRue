package mainstreet.fulfilment;

import mainstreet.application.MerchantScope;
import mainstreet.semantic.configuration.CapabilityConfigurationResolutionStatus;
import mainstreet.semantic.configuration.CapabilityConfigurationValueDomain;
import mainstreet.semantic.configuration.MerchantConfiguration;
import mainstreet.semantic.configuration.ResolvedCapabilityConfiguration;
import mainstreet.semantic.configuration.ResolvedCapabilityConfigurationDecision;
import mainstreet.semantic.configuration.ResolvedEnumCapabilityConfigurationValue;
import mainstreet.semantic.executable.ExecutableMerchantModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Resolves one exact merchant fulfilment-binding-set revision into the static
 * Fulfilment Plan for an already-resolved executable semantic model.
 *
 * <p>MS-PROT-048 v1.3 makes applicability requirement-driven: active
 * capability requirements produce exact obligation sets; a role/context is
 * applicable exactly when that set is non-empty. Provider health, credentials
 * and other live ProviderConnection state remain outside this boundary.</p>
 */
public final class FulfilmentPlanResolver {

    private final FulfilmentContractRegistrySnapshot contractRegistry;

    public FulfilmentPlanResolver(
            FulfilmentContractRegistrySnapshot contractRegistry
    ) {
        this.contractRegistry = Objects.requireNonNull(
                contractRegistry,
                "contractRegistry"
        );
    }

    public FulfilmentPlan resolve(
            MerchantConfiguration configuration,
            ExecutableMerchantModel executableModel,
            FulfilmentBindingSetRevision bindingSetRevision
    ) {
        Objects.requireNonNull(configuration, "configuration");
        Objects.requireNonNull(executableModel, "executableModel");
        Objects.requireNonNull(bindingSetRevision, "bindingSetRevision");

        FulfilmentBindingSetRevisionReference expectedReference =
                configuration.fulfilmentBindingSetRevisionReference()
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Merchant configuration does not pin a fulfilment binding-set revision"
                        ));

        if (!expectedReference.equals(bindingSetRevision.reference())) {
            throw new IllegalArgumentException(
                    "Supplied fulfilment binding-set revision does not match configuration reference"
            );
        }
        MerchantScope expectedMerchantScope = new MerchantScope(
                configuration.merchantIdentifier()
        );
        if (!expectedMerchantScope.equals(bindingSetRevision.merchantScope())) {
            throw new IllegalArgumentException(
                    "Fulfilment binding-set revision belongs to another merchant"
            );
        }
        if (!configuration.semanticRegistryVersion().equals(
                bindingSetRevision.semanticRegistryReleaseIdentifier()
        )) {
            throw new IllegalArgumentException(
                    "Fulfilment binding-set semantic release does not match configuration"
            );
        }
        if (!configuration.semanticRegistryVersion().equals(
                contractRegistry.semanticRegistryReleaseIdentifier()
        )) {
            throw new IllegalArgumentException(
                    "Fulfilment contract registry release does not match configuration"
            );
        }

        ResolvedCapabilityConfiguration resolvedConfiguration =
                new ResolvedCapabilityConfiguration(executableModel.policies());
        Map<BindingKey, Set<String>> applicableObligations =
                resolveApplicableObligations(
                        executableModel,
                        resolvedConfiguration
                );
        Map<BindingKey, FulfilmentBindingSelection> selections =
                indexSelections(bindingSetRevision.bindings());

        for (BindingKey selectedKey : selections.keySet()) {
            if (!applicableObligations.containsKey(selectedKey)) {
                throw new IllegalArgumentException(
                        "Fulfilment binding has no applicable registered requirement"
                );
            }
        }

        List<ResolvedFulfilmentBinding> bindings = new ArrayList<>();
        for (Map.Entry<BindingKey, Set<String>> entry :
                applicableObligations.entrySet()) {
            BindingKey key = entry.getKey();
            Set<String> requiredObligations = entry.getValue();
            FulfilmentBindingSelection selection = selections.get(key);
            if (selection == null) {
                throw new IllegalArgumentException(
                        "Applicable fulfilment role/context has no binding selection"
                );
            }

            FulfilmentRoleDefinition role = contractRegistry
                    .role(key.roleIdentity())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Applicable fulfilment requirement references an unregistered role"
                    ));

            if (selection.fulfillerKind() == FulfillerKind.EXTERNAL_PROVIDER) {
                ProviderDefinition provider = contractRegistry
                        .provider(selection.fulfillerIdentity())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Fulfilment binding references an unregistered provider"
                        ));
                if (!contractRegistry.providerSupports(
                        provider.providerIdentifier(),
                        role.identity(),
                        requiredObligations
                )) {
                    throw new IllegalArgumentException(
                            "External provider does not satisfy the applicable fulfilment obligations"
                    );
                }
            }

            bindings.add(new ResolvedFulfilmentBinding(
                    expectedMerchantScope,
                    selection.roleIdentity(),
                    selection.semanticContextIdentifier(),
                    requiredObligations,
                    selection.fulfillerKind(),
                    selection.fulfillerIdentity(),
                    selection.providerConnectionIdentity(),
                    expectedReference.provenanceIdentifier()
            ));
        }

        bindings.sort(Comparator
                .comparing((ResolvedFulfilmentBinding binding) ->
                        binding.roleIdentity().ownerContextIdentifier())
                .thenComparing(binding ->
                        binding.roleIdentity().roleIdentifier())
                .thenComparing(binding ->
                        binding.semanticContextIdentifier().orElse("")));

        return FulfilmentPlan.fromBindingSet(
                expectedReference,
                List.copyOf(bindings)
        );
    }

    private Map<BindingKey, Set<String>> resolveApplicableObligations(
            ExecutableMerchantModel executableModel,
            ResolvedCapabilityConfiguration resolvedConfiguration
    ) {
        List<FulfilmentRequirementDefinition> requirements =
                contractRegistry.requirements().stream()
                        .sorted(Comparator
                                .comparing((FulfilmentRequirementDefinition requirement) ->
                                        requirement.identity().ownerCapabilityIdentifier())
                                .thenComparing(requirement ->
                                        requirement.identity().requirementIdentifier()))
                        .toList();

        Map<BindingKey, Set<String>> accumulated = new HashMap<>();
        for (FulfilmentRequirementDefinition requirement : requirements) {
            if (!executableModel.capabilityIdentifiers().contains(
                    requirement.identity().ownerCapabilityIdentifier()
            )) {
                continue;
            }
            if (!isApplicable(requirement, resolvedConfiguration)) {
                continue;
            }

            BindingKey key = new BindingKey(
                    requirement.roleIdentity(),
                    requirement.semanticContextIdentifier()
            );
            Set<String> obligations = new HashSet<>(
                    accumulated.getOrDefault(key, Set.of())
            );
            obligations.addAll(requirement.obligationIdentifiers());
            accumulated.put(key, Set.copyOf(obligations));
        }
        return Map.copyOf(accumulated);
    }

    private static boolean isApplicable(
            FulfilmentRequirementDefinition requirement,
            ResolvedCapabilityConfiguration resolvedConfiguration
    ) {
        if (requirement.applicability()
                instanceof AlwaysFulfilmentRequirementApplicability) {
            return true;
        }
        if (requirement.applicability()
                instanceof EnumDecisionValueFulfilmentRequirementApplicability conditional) {
            Optional<ResolvedCapabilityConfigurationDecision> decision =
                    resolvedConfiguration.decisions().stream()
                            .filter(candidate -> candidate.identity().equals(
                                    conditional.decisionIdentity()
                            ))
                            .findFirst();
            if (decision.isEmpty()) {
                return false;
            }
            ResolvedCapabilityConfigurationDecision resolved =
                    decision.orElseThrow();
            if (resolved.status()
                    == CapabilityConfigurationResolutionStatus.NOT_APPLICABLE
                    || resolved.valueDomain()
                    != CapabilityConfigurationValueDomain.ENUM) {
                return false;
            }
            return resolved.value()
                    .filter(ResolvedEnumCapabilityConfigurationValue.class::isInstance)
                    .map(ResolvedEnumCapabilityConfigurationValue.class::cast)
                    .map(ResolvedEnumCapabilityConfigurationValue::value)
                    .map(conditional.acceptedValues()::contains)
                    .orElse(false);
        }
        throw new IllegalStateException(
                "Unsupported registered fulfilment requirement applicability"
        );
    }

    private static Map<BindingKey, FulfilmentBindingSelection> indexSelections(
            Set<FulfilmentBindingSelection> selections
    ) {
        Map<BindingKey, FulfilmentBindingSelection> indexed = new HashMap<>();
        for (FulfilmentBindingSelection selection : selections) {
            BindingKey key = new BindingKey(
                    selection.roleIdentity(),
                    selection.semanticContextIdentifier()
            );
            if (indexed.putIfAbsent(key, selection) != null) {
                throw new IllegalArgumentException(
                        "Fulfilment binding set selects one role/context more than once"
                );
            }
        }
        return Map.copyOf(indexed);
    }

    private record BindingKey(
            FulfilmentRoleIdentity roleIdentity,
            Optional<String> semanticContextIdentifier
    ) {
        private BindingKey {
            Objects.requireNonNull(roleIdentity, "roleIdentity");
            Objects.requireNonNull(
                    semanticContextIdentifier,
                    "semanticContextIdentifier"
            );
        }
    }
}
