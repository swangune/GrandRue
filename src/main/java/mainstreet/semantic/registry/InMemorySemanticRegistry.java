package mainstreet.semantic.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Process-local append-only semantic registry adapter for tests and early
 * runtime composition.
 */
public final class InMemorySemanticRegistry implements SemanticRegistry {

    private Map<String, SemanticRegistrySnapshot> snapshots = Map.of();

    /**
     * Publishes a new immutable release and rejects replacement of an existing
     * version.
     */
    public synchronized void publish(SemanticRegistrySnapshot snapshot) {
        Objects.requireNonNull(snapshot);
        if (snapshots.containsKey(snapshot.version())) {
            throw new IllegalArgumentException(
                    "Semantic registry version is already published: "
                            + snapshot.version()
            );
        }

        Map<String, SemanticRegistrySnapshot> updated =
                new HashMap<>(snapshots);
        updated.put(snapshot.version(), snapshot);
        snapshots = Map.copyOf(updated);
    }

    @Override
    public synchronized Optional<SemanticRegistrySnapshot> version(
            String version
    ) {
        return Optional.ofNullable(snapshots.get(version));
    }
}
