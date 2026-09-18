package grandrue.deployment;

import grandrue.semantic.execution.ExecutableSupportManifest;
import grandrue.semantic.execution.ExecutableSupportRequirement;
import grandrue.semantic.execution.SemanticExecutionContractReference;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ServingDeploymentPromotionCoverageTest {

    @Test
    void target_must_materialise_and_support_every_active_configuration() {
        ActiveConfigurationServingRequirement active = activeRequirement();
        ServingDeploymentAdmissionSnapshot covered = snapshot(
                "target",
                "bundle-21",
                Set.of(reference("booking.create"), reference("notification.intent"))
        );
        ServingDeploymentAdmissionSnapshot uncovered = snapshot(
                "uncovered",
                "bundle-21",
                Set.of(reference("booking.create"))
        );

        assertDoesNotThrow(() -> ServingDeploymentPromotionCoverage
                .requireCovered(covered, List.of(active)));
        ServingDeploymentPromotionCoverageException failure = assertThrows(
                ServingDeploymentPromotionCoverageException.class,
                () -> ServingDeploymentPromotionCoverage.requireCovered(
                        uncovered,
                        List.of(active)
                )
        );
        assertEquals(
                ServingDeploymentPromotionCoverageFailure
                        .EXECUTABLE_SUPPORT_INCOMPLETE,
                failure.failure()
        );
    }

    @Test
    void changed_bundle_digest_does_not_materialise_the_active_context() {
        ServingDeploymentPromotionCoverageException failure = assertThrows(
                ServingDeploymentPromotionCoverageException.class,
                () -> ServingDeploymentPromotionCoverage.requireCovered(
                        snapshot(
                                "target",
                                "different-bundle",
                                Set.of(
                                        reference("booking.create"),
                                        reference("notification.intent")
                                )
                        ),
                        List.of(activeRequirement())
                )
        );

        assertEquals(
                ServingDeploymentPromotionCoverageFailure
                        .SEMANTIC_MATERIALISATION_INCOMPLETE,
                failure.failure()
        );
    }

    private static ActiveConfigurationServingRequirement activeRequirement() {
        return new ActiveConfigurationServingRequirement(
                "merchant-acme",
                "configuration-1",
                "semantic-release-21",
                "bundle-21",
                "ms-reqset-v1:sha256:" + "a".repeat(64),
                Set.of(new ExecutableSupportRequirement(
                        reference("booking.create"),
                        Set.of(reference("notification.intent"))
                ))
        );
    }

    private static ServingDeploymentAdmissionSnapshot snapshot(
            String generation,
            String digest,
            Set<SemanticExecutionContractReference> supported
    ) {
        return new ServingDeploymentAdmissionSnapshot(
                generation,
                ServingDeploymentCohort.ORDINARY,
                Map.of("semantic-release-21", digest),
                Set.of(new ExecutableSupportManifest("path-1", supported)),
                Instant.parse("2026-08-29T18:00:00Z")
        );
    }

    private static SemanticExecutionContractReference reference(String contract) {
        return new SemanticExecutionContractReference(
                "semantic-release-21",
                contract
        );
    }
}
