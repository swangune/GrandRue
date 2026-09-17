package grandrue.deployment;

import java.util.Objects;

public final class ServingDeploymentPromotionCoverageException
        extends RuntimeException {

    private final ServingDeploymentPromotionCoverageFailure failure;

    public ServingDeploymentPromotionCoverageException(
            ServingDeploymentPromotionCoverageFailure failure,
            String message
    ) {
        super(message);
        this.failure = Objects.requireNonNull(failure, "failure");
    }

    public ServingDeploymentPromotionCoverageFailure failure() {
        return failure;
    }
}
