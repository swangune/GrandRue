package grandrue.deployment;

import java.util.Objects;

public final class ServingDeploymentAdmissionSnapshotPersistenceException
        extends RuntimeException {

    private final ServingDeploymentAdmissionSnapshotFailureCategory category;

    public ServingDeploymentAdmissionSnapshotPersistenceException(
            ServingDeploymentAdmissionSnapshotFailureCategory category,
            String message
    ) {
        super(message);
        this.category = Objects.requireNonNull(category, "category");
    }

    public ServingDeploymentAdmissionSnapshotPersistenceException(
            ServingDeploymentAdmissionSnapshotFailureCategory category,
            String message,
            Throwable cause
    ) {
        super(message, cause);
        this.category = Objects.requireNonNull(category, "category");
    }

    public ServingDeploymentAdmissionSnapshotFailureCategory category() {
        return category;
    }
}
