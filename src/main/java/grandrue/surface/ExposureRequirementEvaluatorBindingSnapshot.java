package grandrue.surface;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Immutable exact-release Exposure requirement evaluator bindings. */
public final class ExposureRequirementEvaluatorBindingSnapshot {

    private final String semanticRegistryReleaseIdentifier;
    private final Map<ExposureRequirementReference,
            ExposureRequirementEvaluatorBinding> bindings;

    public ExposureRequirementEvaluatorBindingSnapshot(
            String semanticRegistryReleaseIdentifier,
            Collection<ExposureRequirementEvaluatorBinding> bindings
    ) {
        requireIdentifier(
                semanticRegistryReleaseIdentifier,
                "semanticRegistryReleaseIdentifier"
        );
        this.semanticRegistryReleaseIdentifier =
                semanticRegistryReleaseIdentifier;
        this.bindings = index(Objects.requireNonNull(bindings, "bindings"));
    }

    public String semanticRegistryReleaseIdentifier() {
        return semanticRegistryReleaseIdentifier;
    }

    public Set<ExposureRequirementEvaluatorBinding> bindings() {
        return Set.copyOf(bindings.values());
    }

    public Optional<ExposureRequirementEvaluator> evaluator(
            ExposureRequirementReference reference
    ) {
        Objects.requireNonNull(reference, "reference");
        return Optional.ofNullable(bindings.get(reference))
                .map(ExposureRequirementEvaluatorBinding::evaluator);
    }

    private static Map<ExposureRequirementReference,
            ExposureRequirementEvaluatorBinding> index(
            Collection<ExposureRequirementEvaluatorBinding> source
    ) {
        Map<ExposureRequirementReference,
                ExposureRequirementEvaluatorBinding> indexed =
                new LinkedHashMap<>();
        for (ExposureRequirementEvaluatorBinding binding : source) {
            Objects.requireNonNull(binding, "binding");
            if (indexed.putIfAbsent(binding.reference(), binding) != null) {
                throw new IllegalArgumentException(
                        "Duplicate Exposure requirement evaluator binding: "
                                + binding.reference()
                );
            }
        }
        return Map.copyOf(indexed);
    }

    private static void requireIdentifier(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
    }
}
