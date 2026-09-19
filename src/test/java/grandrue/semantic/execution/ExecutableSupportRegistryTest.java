package grandrue.semantic.execution;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExecutableSupportRegistryTest {

    @Test
    void matching_operation_identifier_does_not_override_release_affinity() {
        ExecutableSupportRegistry registry = registry(manifest(
                "current-path",
                reference("R9", "booking.confirm")
        ));
        ExecutableSupportRequirement historical = requirement(
                "R8",
                "booking.confirm"
        );

        assertTrue(registry.eligiblePaths(historical).isEmpty());
        assertThrows(
                UnsupportedExecutableSupportException.class,
                () -> registry.requirePath("current-path", historical)
        );
    }

    @Test
    void one_path_may_prove_support_for_multiple_exact_semantic_contexts() {
        ExecutableSupportRegistry registry = registry(manifest(
                "generic-booking-path",
                reference("R8", "booking.confirm"),
                reference("R9", "booking.confirm")
        ));

        assertEquals(
                List.of("generic-booking-path"),
                pathIdentifiers(registry, requirement("R8", "booking.confirm"))
        );
        assertEquals(
                List.of("generic-booking-path"),
                pathIdentifiers(registry, requirement("R9", "booking.confirm"))
        );
    }

    @Test
    void materially_distinct_historical_contract_can_use_a_distinct_path() {
        ExecutableSupportRegistry registry = registry(
                manifest(
                        "current-path",
                        reference("R9", "booking.confirm")
                ),
                manifest(
                        "historical-r8-path",
                        reference("R8", "booking.confirm")
                )
        );

        assertEquals(
                List.of("historical-r8-path"),
                pathIdentifiers(registry, requirement("R8", "booking.confirm"))
        );
        assertEquals(
                List.of("current-path"),
                pathIdentifiers(registry, requirement("R9", "booking.confirm"))
        );
    }

    @Test
    void participant_and_effect_contracts_are_required_for_path_admission() {
        ExecutableSupportRequirement ordering = new ExecutableSupportRequirement(
                reference("R8", "ordering.commit"),
                Set.of(
                        reference("R8", "inventory.claim"),
                        reference("R8", "money.obligation")
                )
        );
        ExecutableSupportRegistry incomplete = registry(manifest(
                "ordering-path",
                reference("R8", "ordering.commit"),
                reference("R8", "inventory.claim")
        ));

        assertThrows(
                UnsupportedExecutableSupportException.class,
                () -> incomplete.requirePath("ordering-path", ordering)
        );

        ExecutableSupportRegistry complete = registry(manifest(
                "ordering-path",
                reference("R8", "ordering.commit"),
                reference("R8", "inventory.claim"),
                reference("R8", "money.obligation")
        ));
        assertDoesNotThrow(() -> complete.requirePath("ordering-path", ordering));
    }

    @Test
    void process_rejects_a_context_absent_from_its_manifest() {
        ExecutableSupportRegistry registry = registry(manifest(
                "process-a",
                reference("R10", "publication.publish")
        ));

        assertThrows(
                UnsupportedExecutableSupportException.class,
                () -> registry.requirePath(
                        "process-a",
                        requirement("R10", "booking.confirm")
                )
        );
    }

    @Test
    void deployment_coverage_rejects_orphaning_the_last_safe_path() {
        ExecutableSupportRequirement requiredHistoricalContract = requirement(
                "R8",
                "booking.confirm"
        );
        ExecutableSupportRegistry currentOnly = registry(manifest(
                "current-path",
                reference("R9", "booking.confirm")
        ));

        ExecutableSupportCoverageException failure = assertThrows(
                ExecutableSupportCoverageException.class,
                () -> currentOnly.requireDeploymentCoverage(
                        Set.of(requiredHistoricalContract)
                )
        );
        assertEquals(
                Set.of(requiredHistoricalContract),
                failure.uncoveredRequirements()
        );

        ExecutableSupportRegistry withHistoricalPath = registry(
                manifest(
                        "current-path",
                        reference("R9", "booking.confirm")
                ),
                manifest(
                        "historical-r8-path",
                        reference("R8", "booking.confirm")
                )
        );
        assertDoesNotThrow(() -> withHistoricalPath.requireDeploymentCoverage(
                Set.of(requiredHistoricalContract)
        ));
    }

    @Test
    void unexpected_support_gap_remains_scoped_to_uncovered_requirements() {
        ExecutableSupportRequirement unsupported = requirement(
                "R8",
                "booking.confirm"
        );
        ExecutableSupportRequirement supported = requirement(
                "R9",
                "publication.publish"
        );
        ExecutableSupportRegistry registry = registry(manifest(
                "current-path",
                reference("R9", "publication.publish")
        ));

        assertEquals(
                Set.of(unsupported),
                registry.uncoveredRequirements(Set.of(unsupported, supported))
        );
    }

    private static ExecutableSupportRegistry registry(
            ExecutableSupportManifest... manifests
    ) {
        return new ExecutableSupportRegistry(List.of(manifests));
    }

    private static ExecutableSupportManifest manifest(
            String pathIdentifier,
            SemanticExecutionContractReference... contracts
    ) {
        return new ExecutableSupportManifest(
                pathIdentifier,
                Set.of(contracts)
        );
    }

    private static ExecutableSupportRequirement requirement(
            String releaseIdentifier,
            String contractIdentifier
    ) {
        return new ExecutableSupportRequirement(reference(
                releaseIdentifier,
                contractIdentifier
        ));
    }

    private static SemanticExecutionContractReference reference(
            String releaseIdentifier,
            String contractIdentifier
    ) {
        return new SemanticExecutionContractReference(
                releaseIdentifier,
                contractIdentifier
        );
    }

    private static List<String> pathIdentifiers(
            ExecutableSupportRegistry registry,
            ExecutableSupportRequirement requirement
    ) {
        return registry.eligiblePaths(requirement).stream()
                .map(ExecutableSupportManifest::implementationPathIdentifier)
                .toList();
    }
}
