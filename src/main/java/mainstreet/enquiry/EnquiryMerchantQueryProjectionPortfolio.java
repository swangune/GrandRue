package mainstreet.enquiry;

import mainstreet.surface.*;
import java.util.*;

/** Request-scoped M2 serviceability: complete exact immutable submission, no stale fallback. */
public final class EnquiryMerchantQueryProjectionPortfolio {
    private EnquiryMerchantQueryProjectionPortfolio() { }
    private static ProjectionPolicyReference policy(String name) {
        return new ProjectionPolicyReference("enquiry", name);
    }
    private static final Map<ProjectionPolicyCategory, ProjectionPolicyReference> POLICIES = Map.of(
            ProjectionPolicyCategory.FRESHNESS, policy("merchant-enquiry-current-submission"),
            ProjectionPolicyCategory.SERVICEABILITY, policy("merchant-enquiry-submission-serviceability"),
            ProjectionPolicyCategory.MISSING_EVIDENCE, policy("merchant-enquiry-absent-or-unserviceable"),
            ProjectionPolicyCategory.STALE_SERVING, policy("merchant-enquiry-no-stale-material"));

    public static ProjectionContractRegistrySnapshot contracts(String release) {
        return new ProjectionContractRegistrySnapshot(release, Set.of(new ProjectionContractDefinition(
                EnquiryMerchantRepresentationProjectionReferences.PROJECTION_CONTRACT,
                Set.of(ProjectionContractApplicabilityTrigger.REGISTERED_DEPENDENCY),
                Set.of(EnquiryMerchantRepresentationProjectionReferences.SUBMISSION_SOURCE),
                ProjectionMaterialisationKind.REQUEST_SCOPED, POLICIES.get(ProjectionPolicyCategory.FRESHNESS),
                Set.of(new ProjectionReadUseContract(EnquiryMerchantRepresentationProjectionReferences.READ_USE,
                        POLICIES.get(ProjectionPolicyCategory.SERVICEABILITY),
                        POLICIES.get(ProjectionPolicyCategory.MISSING_EVIDENCE),
                        POLICIES.get(ProjectionPolicyCategory.STALE_SERVING))), Optional.empty(), Optional.empty())));
    }

    public static ProjectionPolicyEvaluatorRegistrySnapshot evaluators(String release) {
        Set<ProjectionPolicyEvaluatorBinding> bindings = new HashSet<>();
        POLICIES.forEach((category, reference) -> bindings.add(new ProjectionPolicyEvaluatorBinding(category,
                reference, new ProjectionPolicyEvaluatorIdentity("enquiry", "exact-submission", "1"),
                context -> context.request().evidenceFor(
                        EnquiryMerchantRepresentationProjectionReferences.SUBMISSION_SOURCE).isCurrent()
                        ? ProjectionPolicyAssessment.fullyServiceable()
                        : ProjectionPolicyAssessment.notServiceable(
                                Set.of(ProjectionServiceabilityReasonCode.REQUIRED_SOURCE_NOT_CURRENT), Set.of()))));
        return new ProjectionPolicyEvaluatorRegistrySnapshot(release, bindings);
    }
}
