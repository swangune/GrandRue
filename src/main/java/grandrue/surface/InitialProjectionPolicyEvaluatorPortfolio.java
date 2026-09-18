package grandrue.surface;

import java.util.HashSet;
import java.util.Set;

/** Exact release composition of owner-supplied initial policy evaluators. */
public final class InitialProjectionPolicyEvaluatorPortfolio {

    private InitialProjectionPolicyEvaluatorPortfolio() {
    }

    public static ProjectionPolicyEvaluatorRegistrySnapshot forRelease(
            String semanticRegistryReleaseIdentifier
    ) {
        Set<ProjectionPolicyEvaluatorBinding> bindings = new HashSet<>();
        bindings.addAll(PlatformProjectionPolicyEvaluators.bindings());
        bindings.addAll(ExposureProjectionPolicyEvaluators.bindings());
        bindings.addAll(CalendarProjectionPolicyEvaluators.bindings());
        bindings.addAll(SchedulingProjectionPolicyEvaluators.bindings());
        return new ProjectionPolicyEvaluatorRegistrySnapshot(
                semanticRegistryReleaseIdentifier,
                Set.copyOf(bindings)
        );
    }
}
