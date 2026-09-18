package grandrue.surface;

import java.lang.reflect.Modifier;
import java.util.Objects;

/** Exact owner-qualified proof-kind to Java implementation binding. */
public record ContextualAccessProofRuntimeBinding(
        ContextualAccessProofKind kind,
        Class<? extends EstablishedContextualAccessProof> contractClass
) {
    public ContextualAccessProofRuntimeBinding {
        Objects.requireNonNull(kind, "kind");
        Objects.requireNonNull(contractClass, "contractClass");
        int modifiers = contractClass.getModifiers();
        if (!Modifier.isFinal(modifiers)
                || Modifier.isPublic(modifiers)
                || Modifier.isProtected(modifiers)
                || Modifier.isPrivate(modifiers)) {
            throw new IllegalArgumentException(
                    "Contextual proof implementation must be final and package-private"
            );
        }
    }
}
