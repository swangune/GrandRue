package mainstreet.deployment;

import mainstreet.semantic.execution.ExecutableSupportManifest;
import mainstreet.semantic.execution.ExecutableSupportRequirement;
import mainstreet.semantic.execution.SemanticExecutionContractReference;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServingDeploymentAdmissionSnapshotTest {

    @Test
    void retains_exact_materialisation_and_complete_path_support() {
        SemanticExecutionContractReference affected = reference(
                "semantic-release-21",
                "booking.create"
        );
        SemanticExecutionContractReference participant = reference(
                "semantic-release-21",
                "notification.intent"
        );
        ServingDeploymentAdmissionSnapshot snapshot =
                new ServingDeploymentAdmissionSnapshot(
                        "generation-42",
                        ServingDeploymentCohort.ORDINARY,
                        Map.of("semantic-release-21", "bundle-digest-21"),
                        Set.of(new ExecutableSupportManifest(
                                "booking-path",
                                Set.of(affected, participant)
                        )),
                        Instant.parse("2026-08-29T18:00:00Z")
                );

        assertTrue(snapshot.materialises(
                "semantic-release-21",
                "bundle-digest-21"
        ));
        assertFalse(snapshot.materialises(
                "semantic-release-21",
                "different-digest"
        ));
        assertEquals(
                Set.of(),
                snapshot.uncoveredRequirements(Set.of(
                        new ExecutableSupportRequirement(
                                affected,
                                Set.of(participant)
                        )
                ))
        );
    }

    private static SemanticExecutionContractReference reference(
            String release,
            String contract
    ) {
        return new SemanticExecutionContractReference(release, contract);
    }
}
