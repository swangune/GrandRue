package mainstreet.deployment;

import java.util.Collection;
import java.util.Objects;

/** Pure exact target-coverage proof used inside promotion preparation. */
public final class ServingDeploymentPromotionCoverage {

    private ServingDeploymentPromotionCoverage() {
    }

    public static void requireCovered(
            ServingDeploymentAdmissionSnapshot target,
            Collection<ActiveConfigurationServingRequirement> active
    ) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(active, "active");
        for (ActiveConfigurationServingRequirement requirement : active) {
            Objects.requireNonNull(requirement, "active contains null");
            if (!target.materialises(
                    requirement.semanticRegistryReleaseIdentifier(),
                    requirement.packagedBundleContentDigest()
            )) {
                throw new ServingDeploymentPromotionCoverageException(
                        ServingDeploymentPromotionCoverageFailure
                                .SEMANTIC_MATERIALISATION_INCOMPLETE,
                        "Target generation does not materialise active Configuration "
                                + requirement.merchantIdentifier()
                                + "/"
                                + requirement.configurationRevisionIdentifier()
                );
            }
            if (!target.uncoveredRequirements(
                    requirement.requirements()
            ).isEmpty()) {
                throw new ServingDeploymentPromotionCoverageException(
                        ServingDeploymentPromotionCoverageFailure
                                .EXECUTABLE_SUPPORT_INCOMPLETE,
                        "Target generation does not support active Configuration "
                                + requirement.merchantIdentifier()
                                + "/"
                                + requirement.configurationRevisionIdentifier()
                );
            }
        }
    }
}
