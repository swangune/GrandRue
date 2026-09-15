package mainstreet.semantic.release;

import java.util.Objects;

public final class SemanticReleaseAdmissionPersistenceException extends RuntimeException {
    private final SemanticReleaseAdmissionFailureCategory category;

    public SemanticReleaseAdmissionPersistenceException(
            SemanticReleaseAdmissionFailureCategory category,
            String message
    ) {
        super(message);
        this.category = Objects.requireNonNull(category, "category");
    }

    public SemanticReleaseAdmissionPersistenceException(
            SemanticReleaseAdmissionFailureCategory category,
            String message,
            Throwable cause
    ) {
        super(message, cause);
        this.category = Objects.requireNonNull(category, "category");
    }

    public SemanticReleaseAdmissionFailureCategory category() {
        return category;
    }
}
