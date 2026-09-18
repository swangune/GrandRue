package grandrue.surface;

import java.util.Objects;
import java.util.Set;

/**
 * Declarative contribution definition for one exact Semantic Registry Release.
 *
 * <p>It contains no Java implementation, evaluator, relationship value,
 * permission or Exposure verdict.</p>
 */
public record ObservationContributionDefinition(
        ObservationContributionKind kind,
        Set<SurfaceAudience> permittedAudiences,
        ObservationContributionCardinality cardinality
) {
    public ObservationContributionDefinition {
        Objects.requireNonNull(kind, "kind");
        permittedAudiences = Set.copyOf(
                Objects.requireNonNull(
                        permittedAudiences,
                        "permittedAudiences"
                )
        );
        if (permittedAudiences.isEmpty()) {
            throw new IllegalArgumentException(
                    "Permitted audiences must not be empty"
            );
        }
        Objects.requireNonNull(cardinality, "cardinality");
    }
}
