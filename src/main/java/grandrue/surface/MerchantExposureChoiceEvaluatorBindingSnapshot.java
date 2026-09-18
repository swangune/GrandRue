package grandrue.surface;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Immutable exact-release merchant Exposure-choice evaluator bindings. */
public final class MerchantExposureChoiceEvaluatorBindingSnapshot {

    private final String semanticRegistryReleaseIdentifier;
    private final Map<MerchantExposureChoiceSourceReference,
            MerchantExposureChoiceEvaluatorBinding> bindings;

    public MerchantExposureChoiceEvaluatorBindingSnapshot(
            String semanticRegistryReleaseIdentifier,
            Collection<MerchantExposureChoiceEvaluatorBinding> bindings
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

    public Set<MerchantExposureChoiceEvaluatorBinding> bindings() {
        return Set.copyOf(bindings.values());
    }

    public Optional<MerchantExposureChoiceEvaluator> evaluator(
            MerchantExposureChoiceSourceReference reference
    ) {
        Objects.requireNonNull(reference, "reference");
        return Optional.ofNullable(bindings.get(reference))
                .map(MerchantExposureChoiceEvaluatorBinding::evaluator);
    }

    private static Map<MerchantExposureChoiceSourceReference,
            MerchantExposureChoiceEvaluatorBinding> index(
            Collection<MerchantExposureChoiceEvaluatorBinding> source
    ) {
        Map<MerchantExposureChoiceSourceReference,
                MerchantExposureChoiceEvaluatorBinding> indexed =
                new LinkedHashMap<>();
        for (MerchantExposureChoiceEvaluatorBinding binding : source) {
            Objects.requireNonNull(binding, "binding");
            if (indexed.putIfAbsent(binding.reference(), binding) != null) {
                throw new IllegalArgumentException(
                        "Duplicate merchant Exposure-choice evaluator binding: "
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
