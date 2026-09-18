package grandrue.surface;

import java.util.Optional;
import java.util.Set;

/**
 * Code-owned initial Merchant Presence and Main Street Calendar Projection
 * Contract portfolio accepted by MS-PROT-027 v1.7.
 */
public final class InitialProjectionContractPortfolio {

    private InitialProjectionContractPortfolio() {
    }

    public static ProjectionContractRegistrySnapshot forRelease(
            String semanticRegistryReleaseIdentifier
    ) {
        return new ProjectionContractRegistrySnapshot(
                semanticRegistryReleaseIdentifier,
                Set.of(merchantPresence(), merchantCalendar())
        );
    }

    private static ProjectionContractDefinition merchantPresence() {
        return new ProjectionContractDefinition(
                identity("platform", "merchant-presence"),
                Set.of(
                        ProjectionContractApplicabilityTrigger
                                .REGISTERED_DEPENDENCY,
                        ProjectionContractApplicabilityTrigger
                                .DIVERGENT_MULTI_SOURCE
                ),
                Set.of(
                        source("profile", "merchant-public-descriptor"),
                        source("profile", "contact-points"),
                        source("profile", "merchant-locations"),
                        source("profile", "service-areas"),
                        source("profile", "external-presence-links"),
                        source("business-hours", "public-business-hours")
                ),
                ProjectionMaterialisationKind.REQUEST_SCOPED,
                policy(
                        "platform",
                        "merchant-presence-current-owner-evidence"
                ),
                Set.of(new ProjectionReadUseContract(
                        readUse("platform", "public-merchant-presence"),
                        policy(
                                "platform",
                                "merchant-presence-truthful-serviceability"
                        ),
                        policy(
                                "platform",
                                "merchant-presence-reduced-or-unserviceable"
                        ),
                        policy(
                                "platform",
                                "no-known-stale-profile-or-location"
                        )
                )),
                Optional.of(policy(
                        "exposure",
                        "current-observation-restrictions"
                )),
                Optional.empty()
        );
    }

    private static ProjectionContractDefinition merchantCalendar() {
        return new ProjectionContractDefinition(
                identity("calendar", "merchant-calendar"),
                Set.of(
                        ProjectionContractApplicabilityTrigger
                                .REGISTERED_DEPENDENCY,
                        ProjectionContractApplicabilityTrigger
                                .SOURCE_OUTAGE_SERVING,
                        ProjectionContractApplicabilityTrigger
                                .DIVERGENT_MULTI_SOURCE
                ),
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
                ProjectionMaterialisationKind.REQUEST_SCOPED,
                policy(
                        "calendar",
                        "merchant-calendar-independent-source-evidence"
                ),
                Set.of(
                        new ProjectionReadUseContract(
                                readUse(
                                        "calendar",
                                        "committed-work-overview"
                                ),
                                policy(
                                        "calendar",
                                        "current-main-street-commitments"
                                ),
                                policy(
                                        "calendar",
                                        "independently-established-commitments-only"
                                ),
                                policy(
                                        "calendar",
                                        "no-unproven-commitment-currentness"
                                )
                        ),
                        new ProjectionReadUseContract(
                                readUse(
                                        "calendar",
                                        "availability-oriented"
                                ),
                                policy(
                                        "scheduling",
                                        "all-required-availability-inputs-current"
                                ),
                                policy(
                                        "scheduling",
                                        "no-current-availability-claim"
                                ),
                                policy(
                                        "scheduling",
                                        "no-stale-availability-claim"
                                )
                        )
                ),
                Optional.empty(),
                Optional.empty()
        );
    }

    private static ProjectionContractIdentity identity(
            String owner,
            String identifier
    ) {
        return new ProjectionContractIdentity(owner, identifier);
    }

    private static ProjectionSourceDependencyReference source(
            String owner,
            String identifier
    ) {
        return new ProjectionSourceDependencyReference(owner, identifier);
    }

    private static ProjectionReadUseIdentity readUse(
            String owner,
            String identifier
    ) {
        return new ProjectionReadUseIdentity(owner, identifier);
    }

    private static ProjectionPolicyReference policy(
            String owner,
            String identifier
    ) {
        return new ProjectionPolicyReference(owner, identifier);
    }
}
