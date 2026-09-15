package mainstreet.surface;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Immutable exact contextual proof runtime bindings; initially empty-capable. */
public final class ContextualAccessProofRuntimeBindingSnapshot {

    private final Map<ContextualAccessProofKind,
            ContextualAccessProofRuntimeBinding> bindings;

    public ContextualAccessProofRuntimeBindingSnapshot(
            Collection<ContextualAccessProofRuntimeBinding> bindings
    ) {
        this.bindings = index(
                Objects.requireNonNull(bindings, "bindings")
        );
    }

    public Set<ContextualAccessProofRuntimeBinding> bindings() {
        return Set.copyOf(bindings.values());
    }

    public Optional<ContextualAccessProofRuntimeBinding> binding(
            ContextualAccessProofKind kind
    ) {
        return Optional.ofNullable(
                bindings.get(Objects.requireNonNull(kind, "kind"))
        );
    }

    public boolean matches(
            ContextualAccessProofKind kind,
            Class<? extends EstablishedContextualAccessProof> contractClass
    ) {
        Objects.requireNonNull(kind, "kind");
        Objects.requireNonNull(contractClass, "contractClass");
        return binding(kind)
                .map(ContextualAccessProofRuntimeBinding::contractClass)
                .filter(contractClass::equals)
                .isPresent();
    }

    private static Map<ContextualAccessProofKind,
            ContextualAccessProofRuntimeBinding> index(
            Collection<ContextualAccessProofRuntimeBinding> bindings
    ) {
        Map<ContextualAccessProofKind,
                ContextualAccessProofRuntimeBinding> indexed =
                new HashMap<>();
        for (ContextualAccessProofRuntimeBinding binding : bindings) {
            Objects.requireNonNull(binding, "binding");
            if (indexed.putIfAbsent(binding.kind(), binding) != null) {
                throw new IllegalArgumentException(
                        "Duplicate contextual access proof runtime binding: "
                                + binding.kind()
                );
            }
        }
        return Map.copyOf(indexed);
    }
}
