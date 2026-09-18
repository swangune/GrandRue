package grandrue.surface;

import java.util.List;
import java.util.Objects;

/** Presentation-neutral result of merchant-context surface eligibility resolution. */
public record ContextualSurfaceComposition(
        List<ContextualSurfaceCompositionGroup> groups
) {
    public ContextualSurfaceComposition {
        groups = List.copyOf(Objects.requireNonNull(groups, "groups"));
    }

    public static ContextualSurfaceComposition empty() {
        return new ContextualSurfaceComposition(List.of());
    }
}
