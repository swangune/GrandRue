package mainstreet.surface;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * One static presentation-composition group assembled from already applicable
 * contributions. Grouping preserves every contribution as an independent
 * semantic input; it does not merge ownership, operations, projections or
 * configuration authority.
 */
public record StaticSurfaceCompositionGroup(
        SurfaceAudience audience,
        Optional<String> compositionTargetReference,
        List<StaticSurfaceContribution> contributions
) {
    public StaticSurfaceCompositionGroup {
        Objects.requireNonNull(audience, "audience");
        compositionTargetReference = Objects.requireNonNull(
                compositionTargetReference,
                "compositionTargetReference"
        );
        contributions = List.copyOf(
                Objects.requireNonNull(contributions, "contributions")
        );
        if (contributions.isEmpty()) {
            throw new IllegalArgumentException(
                    "A surface composition group requires a contribution"
            );
        }
        for (StaticSurfaceContribution contribution : contributions) {
            Objects.requireNonNull(contribution, "surface contribution");
            if (contribution.audience() != audience) {
                throw new IllegalArgumentException(
                        "A surface composition group cannot mix audiences"
                );
            }
            if (!contribution.compositionTargetReference().equals(
                    compositionTargetReference
            )) {
                throw new IllegalArgumentException(
                        "A surface composition group cannot mix composition targets"
                );
            }
        }
        if (compositionTargetReference.isEmpty() && contributions.size() != 1) {
            throw new IllegalArgumentException(
                    "Untargeted contributions must remain independent"
            );
        }
    }
}
