package grandrue.governance;

import grandrue.surface.ProjectionContractApplicabilityTrigger;
import grandrue.surface.ProjectionContractDefinition;
import grandrue.surface.ProjectionContractIdentity;
import grandrue.surface.ProjectionContractRegistrySnapshot;
import grandrue.surface.ProjectionMaterialisationKind;
import grandrue.surface.ProjectionPolicyAssessment;
import grandrue.surface.ProjectionPolicyCategory;
import grandrue.surface.ProjectionPolicyEvaluatorBinding;
import grandrue.surface.ProjectionPolicyEvaluatorIdentity;
import grandrue.surface.ProjectionPolicyEvaluatorRegistrySnapshot;
import grandrue.surface.ProjectionPolicyReference;
import grandrue.surface.ProjectionReadUseContract;
import grandrue.surface.ProjectionReadUseIdentity;
import grandrue.surface.ProjectionServiceabilityEvaluationEngine;
import grandrue.surface.ProjectionServiceabilityEvaluationRequest;
import grandrue.surface.ProjectionServiceabilityOutcome;
import grandrue.surface.ProjectionServiceabilityResult;
import grandrue.surface.ProjectionSourceAvailability;
import grandrue.surface.ProjectionSourceCompleteness;
import grandrue.surface.ProjectionSourceDependencyReference;
import grandrue.surface.ProjectionSourceEvidence;
import grandrue.surface.ProjectionSourceRevocationState;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Falsifies Projection Serviceability change amplification from outside the
 * surface package. A synthetic owner must be able to supply exact contracts
 * and executable policies through public architecture contracts without any
 * existing shared component learning that owner's semantics.
 */
class ProjectionOwnerChangeAmplificationTest {

    private static final String RELEASE = "semantic-registry-reviews-test";
    private static final Instant EVALUATED_AT =
            Instant.parse("2026-09-02T12:00:00Z");

    @Test
    void ordinary_new_projection_owner_crosses_only_public_generic_seams() {
        ProjectionSourceDependencyReference source =
                new ProjectionSourceDependencyReference(
                        "reviews",
                        "published-review-summary"
                );
        ProjectionContractIdentity contractIdentity =
                new ProjectionContractIdentity(
                        "reviews",
                        "merchant-review-summary"
                );
        ProjectionReadUseIdentity readUseIdentity =
                new ProjectionReadUseIdentity(
                        "reviews",
                        "public-review-summary"
                );

        ProjectionPolicyReference freshness = policy(
                "reviews",
                "review-summary-currentness"
        );
        ProjectionPolicyReference serviceability = policy(
                "reviews",
                "review-summary-serviceability"
        );
        ProjectionPolicyReference missingEvidence = policy(
                "reviews",
                "review-summary-missing-evidence"
        );
        ProjectionPolicyReference staleServing = policy(
                "reviews",
                "review-summary-stale-serving"
        );

        ProjectionContractDefinition contract =
                new ProjectionContractDefinition(
                        contractIdentity,
                        Set.of(ProjectionContractApplicabilityTrigger
                                .REGISTERED_DEPENDENCY),
                        Set.of(source),
                        ProjectionMaterialisationKind.REQUEST_SCOPED,
                        freshness,
                        Set.of(new ProjectionReadUseContract(
                                readUseIdentity,
                                serviceability,
                                missingEvidence,
                                staleServing
                        )),
                        Optional.empty(),
                        Optional.empty()
                );

        ProjectionPolicyEvaluatorRegistrySnapshot evaluators =
                new ProjectionPolicyEvaluatorRegistrySnapshot(
                        RELEASE,
                        Set.of(
                                binding(
                                        ProjectionPolicyCategory.FRESHNESS,
                                        freshness
                                ),
                                binding(
                                        ProjectionPolicyCategory.SERVICEABILITY,
                                        serviceability
                                ),
                                binding(
                                        ProjectionPolicyCategory.MISSING_EVIDENCE,
                                        missingEvidence
                                ),
                                binding(
                                        ProjectionPolicyCategory.STALE_SERVING,
                                        staleServing
                                )
                        )
                );

        ProjectionServiceabilityEvaluationEngine engine =
                new ProjectionServiceabilityEvaluationEngine(
                        new ProjectionContractRegistrySnapshot(
                                RELEASE,
                                Set.of(contract)
                        ),
                        evaluators
                );

        ProjectionSourceEvidence evidence = new ProjectionSourceEvidence(
                source,
                "reviews-evidence-1",
                Optional.of("reviews-revision-7"),
                Optional.of("reviews-revision-7"),
                EVALUATED_AT.minusSeconds(15),
                ProjectionSourceAvailability.AVAILABLE,
                ProjectionSourceCompleteness.COMPLETE,
                ProjectionSourceRevocationState.CLEAR
        );

        ProjectionServiceabilityResult result = engine.evaluate(
                new ProjectionServiceabilityEvaluationRequest(
                        RELEASE,
                        contractIdentity,
                        readUseIdentity,
                        EVALUATED_AT,
                        Set.of(evidence)
                )
        );

        assertEquals(
                ProjectionServiceabilityOutcome.FULLY_SERVICEABLE,
                result.outcome()
        );
        assertEquals(4, result.consumedPolicyEvaluators().size());
        assertTrue(result.consumedPolicyEvaluators().stream().allMatch(
                consumed -> consumed.policyReference()
                        .ownerIdentifier().equals("reviews")
                        && consumed.evaluatorIdentity()
                        .ownerIdentifier().equals("reviews")
        ));
    }

    private static ProjectionPolicyEvaluatorBinding binding(
            ProjectionPolicyCategory category,
            ProjectionPolicyReference policyReference
    ) {
        return new ProjectionPolicyEvaluatorBinding(
                category,
                policyReference,
                new ProjectionPolicyEvaluatorIdentity(
                        "reviews",
                        policyReference.policyIdentifier() + "-evaluator",
                        "v1"
                ),
                context -> ProjectionPolicyAssessment.fullyServiceable()
        );
    }

    private static ProjectionPolicyReference policy(
            String owner,
            String identifier
    ) {
        return new ProjectionPolicyReference(owner, identifier);
    }
}
