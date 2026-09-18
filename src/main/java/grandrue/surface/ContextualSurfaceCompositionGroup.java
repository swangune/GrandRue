package grandrue.surface;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * One contextually eligible presentation-composition group. Grouping never
 * merges semantic ownership, operation authority or projection ownership.
 */
public record ContextualSurfaceCompositionGroup(
        SurfaceAudience audience,
        Optional<String> compositionTargetReference,
        List<ContextualSurfaceContribution> contributions
) {
    public ContextualSurfaceCompositionGroup {
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
                    "A contextual surface composition group requires a contribution"
            );
        }
        for (ContextualSurfaceContribution contribution : contributions) {
            Objects.requireNonNull(contribution, "surface contribution");
            if (contribution.audience() != audience) {
                throw new IllegalArgumentException(
                        "A contextual surface group cannot mix audiences"
                );
            }
            if (!contribution.compositionTargetReference().equals(
                    compositionTargetReference
            )) {
                throw new IllegalArgumentException(
                        "A contextual surface group cannot mix composition targets"
                );
            }
        }
        if (compositionTargetReference.isEmpty() && contributions.size() != 1) {
            throw new IllegalArgumentException(
                    "Untargeted contextual contributions must remain independent"
            );
        }
    }
}
