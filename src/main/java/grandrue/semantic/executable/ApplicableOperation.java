package grandrue.semantic.executable;

import grandrue.semantic.configuration.ActiveRelease;

import java.util.Objects;

/**
 * One operation resolved from one captured active operational model. This
 * establishes model applicability and provenance only; it is not an execution
 * authorisation decision.
 */
public final class ApplicableOperation {

    private final ExecutableMerchantModel model;
    private final ExecutableOperationDefinition operation;
    private final String releaseIdentifier;

    ApplicableOperation(
            ActiveRelease activeRelease,
            ExecutableOperationDefinition operation
    ) {
        Objects.requireNonNull(activeRelease);
        this.model = activeRelease.release().executableModel();
        this.operation = Objects.requireNonNull(operation);
        this.releaseIdentifier = activeRelease.releaseIdentifier();
    }

    public ExecutableMerchantModel model() {
        return model;
    }

    public ExecutableOperationDefinition operation() {
        return operation;
    }

    /**
     * Identifies the immutable configuration release governing work begun
     * through this captured operation.
     */
    public String releaseIdentifier() {
        return releaseIdentifier;
    }
}
