package grandrue.surface;

import java.util.List;
import java.util.Objects;

/**
 * Deterministic static composition result. This remains a declarative
 * composition input and is not a route tree, navigation model or authority
 * decision.
 */
public record StaticSurfaceComposition(
        List<StaticSurfaceCompositionGroup> groups
) {
    public StaticSurfaceComposition {
        groups = List.copyOf(Objects.requireNonNull(groups, "groups"));
    }

    public static StaticSurfaceComposition empty() {
        return new StaticSurfaceComposition(List.of());
    }
}
