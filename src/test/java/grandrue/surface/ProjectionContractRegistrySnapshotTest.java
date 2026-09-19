package grandrue.surface;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProjectionContractRegistrySnapshotTest {

    private static final String RELEASE = "semantic-registry-17";

    @Test
    void resolves_only_the_exact_contract_from_the_exact_release() {
        ProjectionContractDefinition contract = merchantPresenceContract();
        ProjectionContractRegistrySnapshot registry =
                new ProjectionContractRegistrySnapshot(
                        RELEASE,
                        Set.of(contract)
                );

        assertEquals(
                Optional.of(contract),
                registry.contract(RELEASE, contract.identity())
        );
        assertFalse(registry.contract(
                "semantic-registry-18",
                contract.identity()
        ).isPresent());
        assertFalse(registry.contract(
                RELEASE,
                new ProjectionContractIdentity("catalogue", "public-search")
        ).isPresent());
    }

    @Test
    void rejects_duplicate_contract_and_read_use_identities() {
        ProjectionContractDefinition first = merchantPresenceContract();
        ProjectionContractDefinition conflicting =
                new ProjectionContractDefinition(
                        first.identity(),
                        Set.of(ProjectionContractApplicabilityTrigger
                                .PERSISTED_OR_CACHED),
                        first.authoritativeSourceReferences(),
                        ProjectionMaterialisationKind.MATERIALISED,
                        first.freshnessPolicyReference(),
                        first.readUseContracts(),
                        first.revocationPolicyReference(),
                        Optional.of(policy("platform", "rebuild"))
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> new ProjectionContractRegistrySnapshot(
                        RELEASE,
                        Set.of(first, conflicting)
                )
        );

        ProjectionReadUseContract readUse = readUse(
                "platform",
                "public-merchant-presence",
                "truthful-serviceability"
        );
        ProjectionReadUseContract conflictingReadUse =
                new ProjectionReadUseContract(
                        readUse.readUseIdentity(),
                        policy("platform", "different-serviceability"),
                        readUse.missingEvidencePolicyReference(),
                        readUse.staleServingPolicyReference()
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> definition(Set.of(readUse, conflictingReadUse))
        );
    }

    @Test
    void definitions_and_registry_collections_are_immutable() {
        ProjectionContractDefinition contract = merchantPresenceContract();
        ProjectionContractRegistrySnapshot registry =
                new ProjectionContractRegistrySnapshot(
                        RELEASE,
                        Set.of(contract)
                );

        assertThrows(
                UnsupportedOperationException.class,
                () -> contract.applicabilityTriggers().clear()
        );
        assertThrows(
                UnsupportedOperationException.class,
                () -> contract.authoritativeSourceReferences().clear()
        );
        assertThrows(
                UnsupportedOperationException.class,
                () -> contract.readUseContracts().clear()
        );
        assertThrows(
                UnsupportedOperationException.class,
                () -> registry.contracts().clear()
        );
    }

    @Test
    void rejects_blank_identities_and_structurally_empty_definitions() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ProjectionContractIdentity(" ", "contract")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ProjectionSourceDependencyReference("profile", " ")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ProjectionReadUseIdentity("", "read-use")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ProjectionPolicyReference("owner", null)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ProjectionContractDefinition(
                        new ProjectionContractIdentity("owner", "contract"),
                        Set.of(),
                        Set.of(source("owner", "source")),
                        ProjectionMaterialisationKind.REQUEST_SCOPED,
                        policy("owner", "freshness"),
                        Set.of(readUse("owner", "read", "serviceability")),
                        Optional.empty(),
                        Optional.empty()
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ProjectionContractDefinition(
                        new ProjectionContractIdentity("owner", "contract"),
                        Set.of(ProjectionContractApplicabilityTrigger
                                .DIVERGENT_MULTI_SOURCE),
                        Set.of(),
                        ProjectionMaterialisationKind.REQUEST_SCOPED,
                        policy("owner", "freshness"),
                        Set.of(readUse("owner", "read", "serviceability")),
                        Optional.empty(),
                        Optional.empty()
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ProjectionContractDefinition(
                        new ProjectionContractIdentity("owner", "contract"),
                        Set.of(ProjectionContractApplicabilityTrigger
                                .DIVERGENT_MULTI_SOURCE),
                        Set.of(source("owner", "source")),
                        ProjectionMaterialisationKind.REQUEST_SCOPED,
                        policy("owner", "freshness"),
                        Set.of(),
                        Optional.empty(),
                        Optional.empty()
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ProjectionContractDefinition(
                        new ProjectionContractIdentity("owner", "contract"),
                        Set.of(ProjectionContractApplicabilityTrigger
                                .PERSISTED_OR_CACHED),
                        Set.of(source("owner", "source")),
                        ProjectionMaterialisationKind.MATERIALISED,
                        policy("owner", "freshness"),
                        Set.of(readUse("owner", "read", "serviceability")),
                        Optional.empty(),
                        Optional.empty()
                )
        );
    }

    @Test
    void registers_the_exact_initial_projection_contract_portfolio() {
        ProjectionContractRegistrySnapshot registry =
                InitialProjectionContractPortfolio.forRelease(RELEASE);

        assertEquals(RELEASE, registry.semanticRegistryReleaseIdentifier());
        assertEquals(2, registry.contracts().size());
        assertEquals(
                Set.of(
                        "platform/merchant-presence",
                        "calendar/merchant-calendar"
                ),
                registry.contracts().stream()
                        .map(value -> value.identity().ownerIdentifier()
                                + "/"
                                + value.identity().contractIdentifier())
                        .collect(java.util.stream.Collectors.toUnmodifiableSet())
        );

        ProjectionContractDefinition presence = required(
                registry,
                "platform",
                "merchant-presence"
        );
        assertEquals(
                ProjectionMaterialisationKind.REQUEST_SCOPED,
                presence.materialisationKind()
        );
        assertEquals(
                Set.of(
                        ProjectionContractApplicabilityTrigger
                                .REGISTERED_DEPENDENCY,
                        ProjectionContractApplicabilityTrigger
                                .DIVERGENT_MULTI_SOURCE
                ),
                presence.applicabilityTriggers()
        );
        assertEquals(
                Set.of(
                        source("profile", "merchant-public-descriptor"),
                        source("profile", "contact-points"),
                        source("profile", "merchant-locations"),
                        source("profile", "service-areas"),
                        source("profile", "external-presence-links"),
                        source("business-hours", "public-business-hours")
                ),
                presence.authoritativeSourceReferences()
        );
        assertEquals(
                policy(
                        "platform",
                        "merchant-presence-current-owner-evidence"
                ),
                presence.freshnessPolicyReference()
        );
        assertEquals(
                Set.of(new ProjectionReadUseIdentity(
                        "platform",
                        "public-merchant-presence"
                )),
                readUseIdentities(presence)
        );
        assertEquals(
                Optional.of(policy(
                        "exposure",
                        "current-observation-restrictions"
                )),
                presence.revocationPolicyReference()
        );
        assertEquals(Optional.empty(), presence.rebuildPolicyReference());

        ProjectionContractDefinition calendar = required(
                registry,
                "calendar",
                "merchant-calendar"
        );
        assertEquals(
                Set.of(
                        ProjectionContractApplicabilityTrigger
                                .REGISTERED_DEPENDENCY,
                        ProjectionContractApplicabilityTrigger
                                .SOURCE_OUTAGE_SERVING,
                        ProjectionContractApplicabilityTrigger
                                .DIVERGENT_MULTI_SOURCE
                ),
                calendar.applicabilityTriggers()
        );
        assertEquals(
                Set.of(
                        source("appointment", "commitments"),
                        source("booking", "applicable-timing"),
                        source("calendar", "merchant-schedule-intents"),
                        source("scheduling", "applicable-configuration"),
                        source("business-hours", "operating-windows"),
                        source("scheduling", "resource-capacity"),
                        source(
                                "calendar-integration",
                                "external-busy-constraints"
                        )
                ),
                calendar.authoritativeSourceReferences()
        );
        assertEquals(
                policy(
                        "calendar",
                        "merchant-calendar-independent-source-evidence"
                ),
                calendar.freshnessPolicyReference()
        );
        assertEquals(
                Set.of(
                        new ProjectionReadUseIdentity(
                                "calendar",
                                "committed-work-overview"
                        ),
                        new ProjectionReadUseIdentity(
                                "calendar",
                                "availability-oriented"
                        )
                ),
                readUseIdentities(calendar)
        );
        assertEquals(Optional.empty(), calendar.rebuildPolicyReference());
    }

    private static ProjectionContractDefinition required(
            ProjectionContractRegistrySnapshot registry,
            String owner,
            String identifier
    ) {
        return registry.contract(
                RELEASE,
                new ProjectionContractIdentity(owner, identifier)
        ).orElseThrow();
    }

    private static Set<ProjectionReadUseIdentity> readUseIdentities(
            ProjectionContractDefinition definition
    ) {
        return definition.readUseContracts().stream()
                .map(ProjectionReadUseContract::readUseIdentity)
                .collect(java.util.stream.Collectors.toUnmodifiableSet());
    }

    private static ProjectionContractDefinition merchantPresenceContract() {
        return definition(Set.of(readUse(
                "platform",
                "public-merchant-presence",
                "truthful-serviceability"
        )));
    }

    private static ProjectionContractDefinition definition(
            Set<ProjectionReadUseContract> readUses
    ) {
        return new ProjectionContractDefinition(
                new ProjectionContractIdentity(
                        "platform",
                        "merchant-presence"
                ),
                Set.of(ProjectionContractApplicabilityTrigger
                        .DIVERGENT_MULTI_SOURCE),
                Set.of(source("profile", "merchant-public-descriptor")),
                ProjectionMaterialisationKind.REQUEST_SCOPED,
                policy("platform", "current-owner-evidence"),
                readUses,
                Optional.empty(),
                Optional.empty()
        );
    }

    private static ProjectionReadUseContract readUse(
            String owner,
            String identifier,
            String serviceabilityPolicy
    ) {
        return new ProjectionReadUseContract(
                new ProjectionReadUseIdentity(owner, identifier),
                policy(owner, serviceabilityPolicy),
                policy(owner, "missing-evidence"),
                policy(owner, "stale-serving")
        );
    }

    private static ProjectionSourceDependencyReference source(
            String owner,
            String identifier
    ) {
        return new ProjectionSourceDependencyReference(owner, identifier);
    }

    private static ProjectionPolicyReference policy(
            String owner,
            String identifier
    ) {
        return new ProjectionPolicyReference(owner, identifier);
    }
}
