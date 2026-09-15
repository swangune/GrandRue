package mainstreet.surface;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Generic, value-blind E4 Exposure resolver for one admitted observation
 * invocation and one exact Semantic Registry Release.
 */
final class ExposureResolver {

    List<ResolvedExposedElement> resolve(
            AudienceObservationContext context,
            AudienceObservationAdmissionResult admission,
            List<ExposureCandidateObservation> candidates,
            ExposureElementContractRegistrySnapshot contractRegistry,
            ExposureRequirementEvaluatorBindingSnapshot requirementEvaluators,
            MerchantExposureChoiceEvaluatorBindingSnapshot choiceEvaluators
    ) {
        Objects.requireNonNull(context, "context");
        Objects.requireNonNull(admission, "admission");
        candidates = List.copyOf(Objects.requireNonNull(candidates, "candidates"));
        Objects.requireNonNull(contractRegistry, "contractRegistry");
        Objects.requireNonNull(requirementEvaluators, "requirementEvaluators");
        Objects.requireNonNull(choiceEvaluators, "choiceEvaluators");

        EstablishedObservationRequest request = validateInvocation(context, admission);
        String release = EstablishedObservationRequestDetails
                .semanticRegistryReleaseIdentifier(request);
        validateReleaseAffinity(
                release,
                contractRegistry,
                requirementEvaluators,
                choiceEvaluators
        );
        SurfaceAudience audience = AudienceObservationContextDetails.audience(context);

        List<CandidateState> states = establishCandidateStates(
                candidates,
                release,
                audience,
                contractRegistry
        );

        evaluateRequirements(
                states,
                context,
                admission,
                requirementEvaluators
        );
        applyRequirementGate(states);

        evaluateMerchantChoices(
                states,
                context,
                admission,
                choiceEvaluators
        );

        return materialisePositiveMembership(states);
    }

    private static EstablishedObservationRequest validateInvocation(
            AudienceObservationContext context,
            AudienceObservationAdmissionResult admission
    ) {
        if (!admission.admitted()) {
            throw structural("Exposure resolution requires admitted audience observation");
        }

        EstablishedObservationRequest request =
                AudienceObservationContextDetails.request(context);
        ObservationRequestBinding expected =
                EstablishedObservationRequestDetails.requestBinding(request);
        ObservationRequestBinding admitted =
                AudienceObservationInvocationBindingDetails.requestBinding(
                        admission.invocationBinding()
                );
        if (!expected.equals(admitted)) {
            throw structural(
                    "Audience admission must belong to the same established observation request"
            );
        }
        return request;
    }

    private static void validateReleaseAffinity(
            String release,
            ExposureElementContractRegistrySnapshot contractRegistry,
            ExposureRequirementEvaluatorBindingSnapshot requirementEvaluators,
            MerchantExposureChoiceEvaluatorBindingSnapshot choiceEvaluators
    ) {
        if (!release.equals(contractRegistry.semanticRegistryReleaseIdentifier())
                || !release.equals(
                        requirementEvaluators.semanticRegistryReleaseIdentifier()
                )
                || !release.equals(choiceEvaluators.semanticRegistryReleaseIdentifier())) {
            throw structural(
                    "Exposure registries must match the exact established semantic release"
            );
        }
    }

    private static List<CandidateState> establishCandidateStates(
            List<ExposureCandidateObservation> candidates,
            String release,
            SurfaceAudience audience,
            ExposureElementContractRegistrySnapshot contractRegistry
    ) {
        List<CandidateState> states = new ArrayList<>(candidates.size());
        Set<ExposedElementMembership> ingressMemberships = new HashSet<>();

        for (ExposureCandidateObservation candidate : candidates) {
            Objects.requireNonNull(candidate, "Exposure candidate observation");
            ExposableElementReference element = candidate.elementReference();
            if (!contractRegistry.containsElement(release, element)) {
                throw structural(
                        "Exposure candidate element is absent from the exact release registry: "
                                + element
                );
            }

            ExposureMemberIdentitySpecification identitySpecification =
                    memberIdentitySpecification(contractRegistry, element);
            ExposedElementMembership ingressMembership = validateIngressMembership(
                    candidate,
                    identitySpecification
            );
            if (!ingressMemberships.add(ingressMembership)) {
                throw structural(
                        "Duplicate Exposure candidate membership at resolver ingress: "
                                + ingressMembership
                );
            }

            Optional<ExposureElementContract> contract =
                    contractRegistry.contract(release, element, audience);
            states.add(new CandidateState(candidate, contract.orElse(null)));
        }
        return states;
    }

    private static ExposureMemberIdentitySpecification memberIdentitySpecification(
            ExposureElementContractRegistrySnapshot contractRegistry,
            ExposableElementReference element
    ) {
        ExposureMemberIdentitySpecification found = null;
        for (ExposureElementContract contract : contractRegistry.contracts()) {
            if (!contract.exposableElementReference().equals(element)) {
                continue;
            }
            if (found == null) {
                found = contract.memberIdentitySpecification();
            } else if (!found.equals(contract.memberIdentitySpecification())) {
                throw structural(
                        "Exposure element registry contains inconsistent member identity semantics: "
                                + element
                );
            }
        }
        if (found == null) {
            throw structural(
                    "Exposure candidate element has no exact release contract definition: "
                            + element
            );
        }
        return found;
    }

    private static ExposedElementMembership validateIngressMembership(
            ExposureCandidateObservation candidate,
            ExposureMemberIdentitySpecification identitySpecification
    ) {
        if (identitySpecification
                instanceof ExposureMemberIdentitySpecification.Singleton) {
            return new ExposedElementMembership(
                    candidate.elementReference(),
                    Optional.empty()
            );
        }
        if (identitySpecification
                instanceof ExposureMemberIdentitySpecification.InstanceQualified qualified) {
            ExposureCandidateInstanceReference instance = candidate.instanceReference()
                    .orElseThrow(() -> structural(
                            "Instance-qualified Exposure candidate requires a stable instance"
                    ));
            ExposureCandidateInstanceKindReference required =
                    qualified.instanceKindReference();
            if (!required.ownerIdentifier().equals(instance.ownerIdentifier())
                    || !required.instanceKindIdentifier().equals(
                            instance.instanceKindIdentifier()
                    )) {
                throw structural(
                        "Exposure candidate stable instance does not match required member kind"
                );
            }
            return new ExposedElementMembership(
                    candidate.elementReference(),
                    Optional.of(instance)
            );
        }
        throw structural("Unsupported Exposure member identity specification");
    }

    private static void evaluateRequirements(
            List<CandidateState> states,
            AudienceObservationContext context,
            AudienceObservationAdmissionResult admission,
            ExposureRequirementEvaluatorBindingSnapshot requirementEvaluators
    ) {
        List<ExposureRequirementReference> references = states.stream()
                .filter(CandidateState::hasAudienceContract)
                .flatMap(state -> state.contract.requirementReferences().stream())
                .distinct()
                .sorted(Comparator
                        .comparing(ExposureRequirementReference::ownerIdentifier)
                        .thenComparing(
                                ExposureRequirementReference::requirementIdentifier
                        ))
                .toList();

        for (ExposureRequirementReference reference : references) {
            List<CandidateState> group = states.stream()
                    .filter(CandidateState::hasAudienceContract)
                    .filter(state -> state.contract.requirementReferences()
                            .contains(reference))
                    .toList();

            Optional<ExposureRequirementEvaluator> evaluator =
                    requirementEvaluators.evaluator(reference);
            if (evaluator.isEmpty()) {
                group.forEach(state -> state.requirementDecisions.put(
                        reference,
                        ExposureRequirementEvaluationDecision.UNRESOLVED
                ));
                continue;
            }

            OwnerExposureEvaluationContext ownerContext = ownerContext(
                    reference.ownerIdentifier(),
                    context,
                    admission
            );
            EvaluationSubmissions submitted = issueSubmissions(group);
            ExposureRequirementBatchEvaluation batch;
            try {
                batch = evaluator.get().evaluateBatch(
                        reference,
                        ownerContext,
                        submitted.submissions()
                );
            } catch (RuntimeException failure) {
                throw structural(
                        "Unexpected Exposure requirement evaluator failure for " + reference,
                        failure
                );
            }

            Map<ExposureCandidateObservation, ExposureRequirementEvaluationDecision>
                    decisions = validateRequirementBatch(reference, submitted, batch);
            group.forEach(state -> state.requirementDecisions.put(
                    reference,
                    decisions.get(state.candidate)
            ));
        }
    }

    private static void applyRequirementGate(List<CandidateState> states) {
        for (CandidateState state : states) {
            if (!state.hasAudienceContract()) {
                state.withheld = true;
                continue;
            }
            for (ExposureRequirementReference reference
                    : state.contract.requirementReferences()) {
                ExposureRequirementEvaluationDecision decision =
                        state.requirementDecisions.get(reference);
                if (decision == null) {
                    throw structural(
                            "Exposure requirement evaluation is incomplete for " + reference
                    );
                }
                if (decision != ExposureRequirementEvaluationDecision.SATISFIED) {
                    state.withheld = true;
                }
            }
        }
    }

    private static void evaluateMerchantChoices(
            List<CandidateState> states,
            AudienceObservationContext context,
            AudienceObservationAdmissionResult admission,
            MerchantExposureChoiceEvaluatorBindingSnapshot choiceEvaluators
    ) {
        List<MerchantExposureChoiceSourceReference> references = states.stream()
                .filter(state -> !state.withheld)
                .map(state -> state.contract.merchantChoiceSource())
                .flatMap(Optional::stream)
                .distinct()
                .sorted(Comparator
                        .comparing(MerchantExposureChoiceSourceReference::ownerIdentifier)
                        .thenComparing(
                                MerchantExposureChoiceSourceReference::sourceIdentifier
                        ))
                .toList();

        for (MerchantExposureChoiceSourceReference reference : references) {
            List<CandidateState> group = states.stream()
                    .filter(state -> !state.withheld)
                    .filter(state -> state.contract.merchantChoiceSource()
                            .filter(reference::equals)
                            .isPresent())
                    .toList();

            Optional<MerchantExposureChoiceEvaluator> evaluator =
                    choiceEvaluators.evaluator(reference);
            if (evaluator.isEmpty()) {
                group.forEach(state -> state.withheld = true);
                continue;
            }

            OwnerExposureEvaluationContext ownerContext = ownerContext(
                    reference.ownerIdentifier(),
                    context,
                    admission
            );
            EvaluationSubmissions submitted = issueSubmissions(group);
            MerchantExposureChoiceBatchEvaluation batch;
            try {
                batch = evaluator.get().evaluateBatch(
                        reference,
                        ownerContext,
                        submitted.submissions()
                );
            } catch (RuntimeException failure) {
                throw structural(
                        "Unexpected merchant Exposure-choice evaluator failure for "
                                + reference,
                        failure
                );
            }

            Map<ExposureCandidateObservation, MerchantExposureChoiceEvaluationDecision>
                    decisions = validateChoiceBatch(reference, submitted, batch);
            for (CandidateState state : group) {
                MerchantExposureChoiceEvaluationDecision decision =
                        decisions.get(state.candidate);
                if (decision == MerchantExposureChoiceEvaluationDecision.EXPOSE) {
                    state.choiceExposed = true;
                } else {
                    state.withheld = true;
                }
            }
        }
    }

    private static EvaluationSubmissions issueSubmissions(List<CandidateState> group) {
        List<ExposureCandidateEvaluationSubmission> submissions =
                new ArrayList<>(group.size());
        Map<ExposureCandidateEvaluationBinding, ExposureCandidateObservation>
                candidatesByBinding = new LinkedHashMap<>();

        for (CandidateState state : group) {
            ExposureCandidateEvaluationBinding binding =
                    ExposureCandidateEvaluationBindings.issue();
            if (candidatesByBinding.putIfAbsent(binding, state.candidate) != null) {
                throw structural("E4 reused a candidate-evaluation binding");
            }
            submissions.add(new ExposureCandidateEvaluationSubmission(
                    binding,
                    state.candidate
            ));
        }

        return new EvaluationSubmissions(
                List.copyOf(submissions),
                Map.copyOf(candidatesByBinding)
        );
    }

    private static OwnerExposureEvaluationContext ownerContext(
            String ownerIdentifier,
            AudienceObservationContext context,
            AudienceObservationAdmissionResult admission
    ) {
        try {
            return OwnerExposureEvaluationContexts.forOwner(
                    ownerIdentifier,
                    context,
                    admission
            );
        } catch (RuntimeException failure) {
            throw structural(
                    "Unable to establish owner-scoped Exposure evaluation context for "
                            + ownerIdentifier,
                    failure
            );
        }
    }

    private static Map<ExposureCandidateObservation,
            ExposureRequirementEvaluationDecision> validateRequirementBatch(
            ExposureRequirementReference reference,
            EvaluationSubmissions submitted,
            ExposureRequirementBatchEvaluation batch
    ) {
        if (batch == null || !reference.equals(batch.requirementReference())) {
            throw structural(
                    "Exposure requirement evaluator returned mismatched batch binding"
            );
        }
        Map<ExposureCandidateEvaluationBinding, ExposureCandidateObservation> expected =
                submitted.candidatesByBinding();
        Set<ExposureCandidateEvaluationBinding> returned = new HashSet<>();
        Map<ExposureCandidateObservation, ExposureRequirementEvaluationDecision>
                decisions = new LinkedHashMap<>();
        for (ExposureRequirementCandidateEvaluation result : batch.results()) {
            if (result == null || !expected.containsKey(result.evaluationBinding())) {
                throw structural(
                        "Exposure requirement evaluator returned a foreign submission binding"
                );
            }
            if (!returned.add(result.evaluationBinding())) {
                throw structural(
                        "Exposure requirement evaluator returned a duplicate submission binding"
                );
            }
            ExposureCandidateObservation candidate =
                    expected.get(result.evaluationBinding());
            decisions.put(candidate, result.decision());
        }
        if (returned.size() != expected.size()) {
            throw structural(
                    "Exposure requirement evaluator did not cover the exact submitted batch"
            );
        }
        return Map.copyOf(decisions);
    }

    private static Map<ExposureCandidateObservation,
            MerchantExposureChoiceEvaluationDecision> validateChoiceBatch(
            MerchantExposureChoiceSourceReference reference,
            EvaluationSubmissions submitted,
            MerchantExposureChoiceBatchEvaluation batch
    ) {
        if (batch == null || !reference.equals(batch.choiceSourceReference())) {
            throw structural(
                    "Merchant Exposure-choice evaluator returned mismatched batch binding"
            );
        }
        Map<ExposureCandidateEvaluationBinding, ExposureCandidateObservation> expected =
                submitted.candidatesByBinding();
        Set<ExposureCandidateEvaluationBinding> returned = new HashSet<>();
        Map<ExposureCandidateObservation, MerchantExposureChoiceEvaluationDecision>
                decisions = new LinkedHashMap<>();
        for (MerchantExposureChoiceCandidateEvaluation result : batch.results()) {
            if (result == null || !expected.containsKey(result.evaluationBinding())) {
                throw structural(
                        "Merchant Exposure-choice evaluator returned a foreign submission binding"
                );
            }
            if (!returned.add(result.evaluationBinding())) {
                throw structural(
                        "Merchant Exposure-choice evaluator returned a duplicate submission binding"
                );
            }
            ExposureCandidateObservation candidate =
                    expected.get(result.evaluationBinding());
            decisions.put(candidate, result.decision());
        }
        if (returned.size() != expected.size()) {
            throw structural(
                    "Merchant Exposure-choice evaluator did not cover the exact submitted batch"
            );
        }
        return Map.copyOf(decisions);
    }

    private static List<ResolvedExposedElement> materialisePositiveMembership(
            List<CandidateState> states
    ) {
        List<ResolvedExposedElement> resolved = new ArrayList<>();
        Set<ExposedElementMembership> memberships = new HashSet<>();

        for (CandidateState state : states) {
            if (state.withheld || !state.hasAudienceContract()) {
                continue;
            }
            boolean expose = state.contract.merchantChoiceSource().isPresent()
                    ? state.choiceExposed
                    : state.contract.baselineDecision() == ExposureDecision.EXPOSE;
            if (!expose) {
                continue;
            }

            ResolvedExposedElement member;
            try {
                member = new ResolvedExposedElement(
                        state.candidate.elementReference(),
                        state.candidate.instanceReference(),
                        state.contract
                );
            } catch (RuntimeException failure) {
                throw structural(
                        "Unable to construct exact positive Exposure membership",
                        failure
                );
            }
            if (!memberships.add(member.membership())) {
                throw structural(
                        "Duplicate positive Exposure membership: " + member.membership()
                );
            }
            resolved.add(member);
        }
        return List.copyOf(resolved);
    }

    private static ExposureResolutionStructuralException structural(String message) {
        return new ExposureResolutionStructuralException(message);
    }

    private static ExposureResolutionStructuralException structural(
            String message,
            Throwable cause
    ) {
        return new ExposureResolutionStructuralException(message, cause);
    }

    private record EvaluationSubmissions(
            List<ExposureCandidateEvaluationSubmission> submissions,
            Map<ExposureCandidateEvaluationBinding, ExposureCandidateObservation>
                    candidatesByBinding
    ) {
        private EvaluationSubmissions {
            submissions = List.copyOf(submissions);
            candidatesByBinding = Map.copyOf(candidatesByBinding);
        }
    }

    private static final class CandidateState {
        private final ExposureCandidateObservation candidate;
        private final ExposureElementContract contract;
        private final Map<ExposureRequirementReference,
                ExposureRequirementEvaluationDecision> requirementDecisions =
                new HashMap<>();
        private boolean withheld;
        private boolean choiceExposed;

        private CandidateState(
                ExposureCandidateObservation candidate,
                ExposureElementContract contract
        ) {
            this.candidate = candidate;
            this.contract = contract;
            this.withheld = contract == null;
        }

        private boolean hasAudienceContract() {
            return contract != null;
        }
    }
}

/** Whole-resolution failure for malformed or structurally incoherent E4 input/output. */
final class ExposureResolutionStructuralException extends RuntimeException {

    ExposureResolutionStructuralException(String message) {
        super(message);
    }

    ExposureResolutionStructuralException(String message, Throwable cause) {
        super(message, cause);
    }
}
