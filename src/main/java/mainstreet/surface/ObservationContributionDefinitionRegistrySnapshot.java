package mainstreet.surface;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Immutable contribution definitions for one exact Semantic Registry Release. */
public final class ObservationContributionDefinitionRegistrySnapshot {

    private final String semanticRegistryReleaseIdentifier;
    private final Map<ObservationContributionKind,
            ObservationContributionDefinition> definitions;

    public ObservationContributionDefinitionRegistrySnapshot(
            String semanticRegistryReleaseIdentifier,
            Collection<ObservationContributionDefinition> definitions
    ) {
        requireIdentifier(
                semanticRegistryReleaseIdentifier,
                "Semantic registry release identifier"
        );
        this.semanticRegistryReleaseIdentifier =
                semanticRegistryReleaseIdentifier;
        this.definitions = index(
                Objects.requireNonNull(definitions, "definitions")
        );
    }

    public String semanticRegistryReleaseIdentifier() {
        return semanticRegistryReleaseIdentifier;
    }

    public Set<ObservationContributionDefinition> definitions() {
        return Set.copyOf(definitions.values());
    }

    public Optional<ObservationContributionDefinition> definition(
            ObservationContributionKind kind
    ) {
        return Optional.ofNullable(
                definitions.get(Objects.requireNonNull(kind, "kind"))
        );
    }

    private static Map<ObservationContributionKind,
            ObservationContributionDefinition> index(
            Collection<ObservationContributionDefinition> definitions
    ) {
        Map<ObservationContributionKind,
                ObservationContributionDefinition> indexed = new HashMap<>();
        for (ObservationContributionDefinition definition : definitions) {
            Objects.requireNonNull(definition, "definition");
            if (indexed.putIfAbsent(
                    definition.kind(),
                    definition
            ) != null) {
                throw new IllegalArgumentException(
                        "Duplicate observation contribution kind: "
                                + definition.kind()
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
}
