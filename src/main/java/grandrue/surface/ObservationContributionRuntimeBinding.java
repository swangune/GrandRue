package grandrue.surface;

import java.lang.reflect.Modifier;
import java.util.Objects;

/** Exact Java implementation binding for one contribution kind. */
public record ObservationContributionRuntimeBinding(
        ObservationContributionKind kind,
        Class<? extends EstablishedObservationContribution> contractClass
) {
    public ObservationContributionRuntimeBinding {
        Objects.requireNonNull(kind, "kind");
        Objects.requireNonNull(contractClass, "contractClass");
        int modifiers = contractClass.getModifiers();
        if (!Modifier.isFinal(modifiers)
                || Modifier.isPublic(modifiers)
                || Modifier.isProtected(modifiers)
                || Modifier.isPrivate(modifiers)) {
            throw new IllegalArgumentException(
                    "Contribution implementation must be final and package-private"
            );
        }
    }
}
