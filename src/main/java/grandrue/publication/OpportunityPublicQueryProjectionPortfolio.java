package grandrue.publication;

import mainstreet.surface.*;
import java.util.*;

/** Request-scoped P5 serviceability: complete exact published material, no stale fallback. */
public final class OpportunityPublicQueryProjectionPortfolio {
    private OpportunityPublicQueryProjectionPortfolio() { }
    private static ProjectionPolicyReference policy(String name) {
        return new ProjectionPolicyReference("publication", name);
    }
    private static final Map<ProjectionPolicyCategory, ProjectionPolicyReference> POLICIES = Map.of(
            ProjectionPolicyCategory.FRESHNESS, policy("public-opportunity-current-published-material"),
            ProjectionPolicyCategory.SERVICEABILITY, policy("public-opportunity-published-material-serviceability"),
            ProjectionPolicyCategory.MISSING_EVIDENCE, policy("public-opportunity-absent-or-unserviceable"),
            ProjectionPolicyCategory.STALE_SERVING, policy("public-opportunity-no-stale-material"));

    public static ProjectionContractRegistrySnapshot contracts(String release) {
        return new ProjectionContractRegistrySnapshot(release, Set.of(new ProjectionContractDefinition(
                OpportunityPublicRepresentationProjectionReferences.PROJECTION_CONTRACT,
                Set.of(ProjectionContractApplicabilityTrigger.REGISTERED_DEPENDENCY),
                Set.of(OpportunityPublicRepresentationProjectionReferences.PUBLISHED_MATERIAL_SOURCE),
                ProjectionMaterialisationKind.REQUEST_SCOPED, POLICIES.get(ProjectionPolicyCategory.FRESHNESS),
                Set.of(new ProjectionReadUseContract(OpportunityPublicRepresentationProjectionReferences.READ_USE,
                        POLICIES.get(ProjectionPolicyCategory.SERVICEABILITY),
                        POLICIES.get(ProjectionPolicyCategory.MISSING_EVIDENCE),
                        POLICIES.get(ProjectionPolicyCategory.STALE_SERVING))), Optional.empty(), Optional.empty())));
    }

    public static ProjectionPolicyEvaluatorRegistrySnapshot evaluators(String release) {
        Set<ProjectionPolicyEvaluatorBinding> bindings = new HashSet<>();
        POLICIES.forEach((category, reference) -> bindings.add(new ProjectionPolicyEvaluatorBinding(category,
                reference, new ProjectionPolicyEvaluatorIdentity("publication", "exact-published-material", "1"),
                context -> context.request().evidenceFor(
                        OpportunityPublicRepresentationProjectionReferences.PUBLISHED_MATERIAL_SOURCE).isCurrent()
                        ? ProjectionPolicyAssessment.fullyServiceable()
                        : ProjectionPolicyAssessment.notServiceable(
                                Set.of(ProjectionServiceabilityReasonCode.REQUIRED_SOURCE_NOT_CURRENT), Set.of()))));
        return new ProjectionPolicyEvaluatorRegistrySnapshot(release, bindings);
    }
}
