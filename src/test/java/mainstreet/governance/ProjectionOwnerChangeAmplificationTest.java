package mainstreet.governance;

import mainstreet.surface.ProjectionContractApplicabilityTrigger;
import mainstreet.surface.ProjectionContractDefinition;
import mainstreet.surface.ProjectionContractIdentity;
import mainstreet.surface.ProjectionContractRegistrySnapshot;
import mainstreet.surface.ProjectionMaterialisationKind;
import mainstreet.surface.ProjectionPolicyAssessment;
import mainstreet.surface.ProjectionPolicyCategory;
import mainstreet.surface.ProjectionPolicyEvaluatorBinding;
import mainstreet.surface.ProjectionPolicyEvaluatorIdentity;
import mainstreet.surface.ProjectionPolicyEvaluatorRegistrySnapshot;
import mainstreet.surface.ProjectionPolicyReference;
import mainstreet.surface.ProjectionReadUseContract;
import mainstreet.surface.ProjectionReadUseIdentity;
import mainstreet.surface.ProjectionServiceabilityEvaluationEngine;
import mainstreet.surface.ProjectionServiceabilityEvaluationRequest;
import mainstreet.surface.ProjectionServiceabilityOutcome;
import mainstreet.surface.ProjectionServiceabilityResult;
import mainstreet.surface.ProjectionSourceAvailability;
import mainstreet.surface.ProjectionSourceCompleteness;
import mainstreet.surface.ProjectionSourceDependencyReference;
import mainstreet.surface.ProjectionSourceEvidence;
import mainstreet.surface.ProjectionSourceRevocationState;
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
