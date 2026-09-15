package mainstreet.surface;

import java.util.Objects;

/** Exact source-progress affinity retained for one Projection material fragment. */
public record ProjectionMaterialSourceAffinity(
        ProjectionSourceDependencyReference sourceReference,
        String observedProgressIdentifier
) {
    public ProjectionMaterialSourceAffinity {
        Objects.requireNonNull(sourceReference, "sourceReference");
        if (observedProgressIdentifier == null
                || observedProgressIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Observed progress identifier must not be blank"
            );
        }
    }
}
