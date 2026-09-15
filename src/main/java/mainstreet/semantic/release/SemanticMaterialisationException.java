package mainstreet.semantic.release;

import java.util.Objects;

/**
 * Explicit bootstrap/materialisation failure preserving ADR-013 failure class.
 */
public final class SemanticMaterialisationException extends IllegalStateException {

    private final SemanticMaterialisationFailure failure;

    public SemanticMaterialisationException(
            SemanticMaterialisationFailure failure,
            String message
    ) {
        super(message);
        this.failure = Objects.requireNonNull(failure, "failure");
    }

    public SemanticMaterialisationException(
            SemanticMaterialisationFailure failure,
            String message,
            Throwable cause
    ) {
        super(message, cause);
        this.failure = Objects.requireNonNull(failure, "failure");
    }

    public SemanticMaterialisationFailure failure() {
        return failure;
    }
}
