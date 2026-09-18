package grandrue.surface;

import java.util.List;
import java.util.Objects;

/** Immutable statically applicable surface-contribution catalogue. */
public record StaticSurfaceContributionCatalogue(
        List<StaticSurfaceContribution> contributions
) {
    public StaticSurfaceContributionCatalogue {
        contributions = List.copyOf(
                Objects.requireNonNull(contributions, "contributions")
        );
    }

    public static StaticSurfaceContributionCatalogue empty() {
        return new StaticSurfaceContributionCatalogue(List.of());
    }
}
