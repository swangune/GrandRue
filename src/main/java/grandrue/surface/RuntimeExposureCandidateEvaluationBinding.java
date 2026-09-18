package grandrue.surface;

import java.util.Objects;
import java.util.UUID;

/** Surface-owned concrete identity; construction is deliberately package-private. */
final class RuntimeExposureCandidateEvaluationBinding
        implements ExposureCandidateEvaluationBinding {

    private final UUID identity;

    RuntimeExposureCandidateEvaluationBinding() {
        this.identity = UUID.randomUUID();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || other instanceof RuntimeExposureCandidateEvaluationBinding that
                && identity.equals(that.identity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identity);
    }
}
