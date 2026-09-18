package grandrue.surface;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Immutable exact contribution-kind to Java implementation bindings. */
public final class ObservationContributionRuntimeBindingSnapshot {

    private final Map<ObservationContributionKind,
            ObservationContributionRuntimeBinding> bindings;

    public ObservationContributionRuntimeBindingSnapshot(
            Collection<ObservationContributionRuntimeBinding> bindings
    ) {
        this.bindings = index(
                Objects.requireNonNull(bindings, "bindings")
        );
    }

    public Set<ObservationContributionRuntimeBinding> bindings() {
        return Set.copyOf(bindings.values());
    }

    public Optional<ObservationContributionRuntimeBinding> binding(
            ObservationContributionKind kind
    ) {
        return Optional.ofNullable(
                bindings.get(Objects.requireNonNull(kind, "kind"))
        );
    }

    public boolean matches(
            ObservationContributionKind kind,
            Class<? extends EstablishedObservationContribution> contractClass
    ) {
        Objects.requireNonNull(kind, "kind");
        Objects.requireNonNull(contractClass, "contractClass");
        return binding(kind)
                .map(ObservationContributionRuntimeBinding::contractClass)
                .filter(contractClass::equals)
                .isPresent();
    }

    private static Map<ObservationContributionKind,
            ObservationContributionRuntimeBinding> index(
            Collection<ObservationContributionRuntimeBinding> bindings
    ) {
        Map<ObservationContributionKind,
                ObservationContributionRuntimeBinding> indexed =
                new HashMap<>();
        for (ObservationContributionRuntimeBinding binding : bindings) {
            Objects.requireNonNull(binding, "binding");
            if (indexed.putIfAbsent(binding.kind(), binding) != null) {
                throw new IllegalArgumentException(
                        "Duplicate observation contribution runtime binding: "
                                + binding.kind()
                );
            }
        }
        return Map.copyOf(indexed);
    }
}
