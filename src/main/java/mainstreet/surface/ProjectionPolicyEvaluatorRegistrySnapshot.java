package mainstreet.surface;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Immutable exact-release registry of executable projection policy bindings. */
public final class ProjectionPolicyEvaluatorRegistrySnapshot {

    private final String semanticRegistryReleaseIdentifier;
    private final Map<BindingKey, ProjectionPolicyEvaluatorBinding> bindings;

    public ProjectionPolicyEvaluatorRegistrySnapshot(
            String semanticRegistryReleaseIdentifier,
            Set<ProjectionPolicyEvaluatorBinding> bindings
    ) {
        requireIdentifier(
                semanticRegistryReleaseIdentifier,
                "Semantic registry release identifier"
        );
        this.semanticRegistryReleaseIdentifier =
                semanticRegistryReleaseIdentifier;
        this.bindings = index(Objects.requireNonNull(bindings, "bindings"));
    }

    public String semanticRegistryReleaseIdentifier() {
        return semanticRegistryReleaseIdentifier;
    }

    public Set<ProjectionPolicyEvaluatorBinding> bindings() {
        return Set.copyOf(bindings.values());
    }

    public Optional<ProjectionPolicyEvaluatorBinding> binding(
            String requestedSemanticRegistryReleaseIdentifier,
            ProjectionPolicyCategory policyCategory,
            ProjectionPolicyReference policyReference
    ) {
        requireIdentifier(
                requestedSemanticRegistryReleaseIdentifier,
                "Requested semantic registry release identifier"
        );
        Objects.requireNonNull(policyCategory, "policyCategory");
        Objects.requireNonNull(policyReference, "policyReference");
        if (!semanticRegistryReleaseIdentifier.equals(
                requestedSemanticRegistryReleaseIdentifier
        )) {
            return Optional.empty();
        }
        return Optional.ofNullable(bindings.get(new BindingKey(
                policyCategory,
                policyReference
        )));
    }

    private static Map<BindingKey, ProjectionPolicyEvaluatorBinding> index(
            Set<ProjectionPolicyEvaluatorBinding> definitions
    ) {
        Map<BindingKey, ProjectionPolicyEvaluatorBinding> indexed =
                new HashMap<>();
        for (ProjectionPolicyEvaluatorBinding binding : definitions) {
            Objects.requireNonNull(binding, "policy evaluator binding");
            BindingKey key = new BindingKey(
                    binding.policyCategory(),
                    binding.policyReference()
            );
            if (indexed.putIfAbsent(key, binding) != null) {
                throw new IllegalArgumentException(
                        "Duplicate projection policy evaluator binding: "
                                + key
                );
            }
        }
        return Map.copyOf(indexed);
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }

    private record BindingKey(
            ProjectionPolicyCategory category,
            ProjectionPolicyReference reference
    ) {
    }
}
