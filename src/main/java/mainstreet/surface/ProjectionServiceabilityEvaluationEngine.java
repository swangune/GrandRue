package mainstreet.surface;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Deterministic exact-release Projection Serviceability evaluation. This
 * derived read decision performs no source query or mutation.
 */
public final class ProjectionServiceabilityEvaluationEngine {

    private final ProjectionContractRegistrySnapshot contractRegistry;
    private final ProjectionPolicyEvaluatorRegistrySnapshot evaluatorRegistry;

    public ProjectionServiceabilityEvaluationEngine(
            ProjectionContractRegistrySnapshot contractRegistry,
            ProjectionPolicyEvaluatorRegistrySnapshot evaluatorRegistry
    ) {
        this.contractRegistry = Objects.requireNonNull(
                contractRegistry,
                "contractRegistry"
        );
        this.evaluatorRegistry = Objects.requireNonNull(
                evaluatorRegistry,
                "evaluatorRegistry"
        );
    }

    public ProjectionServiceabilityResult evaluate(
            ProjectionServiceabilityEvaluationRequest request
    ) {
        Objects.requireNonNull(request, "request");
        Optional<ProjectionContractDefinition> resolvedContract =
                contractRegistry.contract(
                        request.semanticRegistryReleaseIdentifier(),
                        request.contractIdentity()
                );
        if (resolvedContract.isEmpty()) {
            return failure(
                    request,
                    ProjectionServiceabilityReasonCode
                            .EXACT_CONTRACT_UNRESOLVED,
                    Set.of(),
                    Set.of(),
                    Set.of()
            );
        }

        ProjectionContractDefinition contract = resolvedContract.orElseThrow();
        Optional<ProjectionReadUseContract> resolvedReadUse =
                contract.readUseContracts().stream()
                        .filter(value -> value.readUseIdentity().equals(
                                request.readUseIdentity()
                        ))
                        .findFirst();
        if (resolvedReadUse.isEmpty()) {
            return failure(
                    request,
                    ProjectionServiceabilityReasonCode
                            .EXACT_READ_USE_UNRESOLVED,
                    Set.of(),
                    Set.of(),
                    Set.of()
            );
        }

        Set<ProjectionSourceDependencyReference> suppliedSources =
                request.sourceEvidence().stream()
                        .map(ProjectionSourceEvidence::sourceReference)
                        .collect(java.util.stream.Collectors.toUnmodifiableSet());
        if (!suppliedSources.equals(
                contract.authoritativeSourceReferences()
        )) {
            return failure(
                    request,
                    ProjectionServiceabilityReasonCode.EVIDENCE_SET_MISMATCH,
                    Set.of(),
                    Set.of(),
                    Set.of()
            );
        }

        ProjectionReadUseContract readUse = resolvedReadUse.orElseThrow();
        Set<PolicyRequirement> requirements = requirements(contract, readUse);
        List<ProjectionPolicyEvaluatorBinding> bindings = new ArrayList<>();
        Set<ProjectionPolicyReference> unresolved = new HashSet<>();
        for (PolicyRequirement requirement : requirements) {
            Optional<ProjectionPolicyEvaluatorBinding> binding =
                    evaluatorRegistry.binding(
                            request.semanticRegistryReleaseIdentifier(),
                            requirement.category(),
                            requirement.reference()
                    );
            if (binding.isPresent()) {
                bindings.add(binding.orElseThrow());
            } else {
                unresolved.add(requirement.reference());
            }
        }
        if (!unresolved.isEmpty()) {
            return failure(
                    request,
                    ProjectionServiceabilityReasonCode
                            .POLICY_EVALUATOR_UNRESOLVED,
                    Set.of(),
                    Set.of(),
                    unresolved
            );
        }

        bindings.sort(Comparator
                .comparing((ProjectionPolicyEvaluatorBinding value) ->
                        value.policyCategory().name())
                .thenComparing(value ->
                        value.policyReference().ownerIdentifier())
                .thenComparing(value ->
                        value.policyReference().policyIdentifier()));

        ProjectionServiceabilityOutcome outcome =
                ProjectionServiceabilityOutcome.FULLY_SERVICEABLE;
        Set<ProjectionServiceabilityReasonCode> reasons = new HashSet<>();
        Set<ProjectionSourceDependencyReference> omitted = new HashSet<>();
        Set<ProjectionConsumedPolicyEvaluator> consumed = new HashSet<>();

        for (ProjectionPolicyEvaluatorBinding binding : bindings) {
            ProjectionPolicyAssessment assessment;
            try {
                assessment = Objects.requireNonNull(
                        binding.evaluator().evaluate(
                                new ProjectionPolicyEvaluationContext(
                                        contract,
                                        readUse,
                                        request,
                                        binding.policyCategory(),
                                        binding.policyReference()
                                )
                        ),
                        "policy assessment"
                );
                if (!contract.authoritativeSourceReferences().containsAll(
                        assessment.omittedSourceReferences()
                )) {
                    throw new IllegalArgumentException(
                            "Policy assessment omitted an undeclared source"
                    );
                }
            } catch (RuntimeException exception) {
                return failure(
                        request,
                        ProjectionServiceabilityReasonCode
                                .POLICY_EVALUATION_FAILED,
                        consumed,
                        omitted,
                        Set.of()
                );
            }

            consumed.add(new ProjectionConsumedPolicyEvaluator(
                    binding.policyCategory(),
                    binding.policyReference(),
                    binding.evaluatorIdentity()
            ));
            outcome = ProjectionServiceabilityOutcome.mostRestrictive(
                    outcome,
                    assessment.outcome()
            );
            reasons.addAll(assessment.reasonCodes());
            omitted.addAll(assessment.omittedSourceReferences());
        }

        if ((outcome == ProjectionServiceabilityOutcome.FULLY_SERVICEABLE
                && !omitted.isEmpty())
                || (outcome
                == ProjectionServiceabilityOutcome.REDUCED_SERVICEABLE
                && omitted.isEmpty())) {
            return failure(
                    request,
                    ProjectionServiceabilityReasonCode
                            .POLICY_EVALUATION_FAILED,
                    consumed,
                    omitted,
                    Set.of()
            );
        }

        return new ProjectionServiceabilityResult(
                request.semanticRegistryReleaseIdentifier(),
                request.contractIdentity(),
                request.readUseIdentity(),
                request.evaluatedAt(),
                outcome,
                reasons,
                consumed,
                request.sourceEvidence(),
                omitted,
                Set.of(),
                request.boundedReadBinding()
        );
    }

    private static Set<PolicyRequirement> requirements(
            ProjectionContractDefinition contract,
            ProjectionReadUseContract readUse
    ) {
        Set<PolicyRequirement> requirements = new HashSet<>();
        requirements.add(new PolicyRequirement(
                ProjectionPolicyCategory.FRESHNESS,
                contract.freshnessPolicyReference()
        ));
        requirements.add(new PolicyRequirement(
                ProjectionPolicyCategory.SERVICEABILITY,
                readUse.serviceabilityPolicyReference()
        ));
        requirements.add(new PolicyRequirement(
                ProjectionPolicyCategory.MISSING_EVIDENCE,
                readUse.missingEvidencePolicyReference()
        ));
        requirements.add(new PolicyRequirement(
                ProjectionPolicyCategory.STALE_SERVING,
                readUse.staleServingPolicyReference()
        ));
        contract.revocationPolicyReference().ifPresent(reference ->
                requirements.add(new PolicyRequirement(
                        ProjectionPolicyCategory.REVOCATION,
                        reference
                ))
        );
        return Set.copyOf(requirements);
    }

    private static ProjectionServiceabilityResult failure(
            ProjectionServiceabilityEvaluationRequest request,
            ProjectionServiceabilityReasonCode reason,
            Set<ProjectionConsumedPolicyEvaluator> consumed,
            Set<ProjectionSourceDependencyReference> omitted,
            Set<ProjectionPolicyReference> unresolved
    ) {
        return new ProjectionServiceabilityResult(
                request.semanticRegistryReleaseIdentifier(),
                request.contractIdentity(),
                request.readUseIdentity(),
                request.evaluatedAt(),
                ProjectionServiceabilityOutcome.NOT_SERVICEABLE,
                Set.of(reason),
                consumed,
                request.sourceEvidence(),
                omitted,
                unresolved,
                request.boundedReadBinding()
        );
    }

    private record PolicyRequirement(
            ProjectionPolicyCategory category,
            ProjectionPolicyReference reference
    ) {
    }
}
