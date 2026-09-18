package grandrue.semantic.release;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable process-local adapter for exact semantic release assembly lookup.
 */
public final class InMemorySemanticReleaseAssemblyRepository
        implements SemanticReleaseAssemblyRepository {

    private final Map<String, SemanticReleaseAssembly> assemblies;

    public InMemorySemanticReleaseAssemblyRepository(
            Collection<SemanticReleaseAssembly> assemblies
    ) {
        Objects.requireNonNull(assemblies, "assemblies");
        Map<String, SemanticReleaseAssembly> indexed = new HashMap<>();
        for (SemanticReleaseAssembly assembly : assemblies) {
            Objects.requireNonNull(assembly, "semantic release assembly");
            String releaseIdentifier = assembly.releaseIdentifier();
            if (indexed.putIfAbsent(releaseIdentifier, assembly) != null) {
                throw new IllegalArgumentException(
                        "Duplicate semantic release assembly: "
                                + releaseIdentifier
                );
            }
        }
        this.assemblies = Map.copyOf(indexed);
    }

    @Override
    public Optional<SemanticReleaseAssembly> release(String releaseIdentifier) {
        if (releaseIdentifier == null || releaseIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Semantic release identifier must not be blank"
            );
        }
        return Optional.ofNullable(assemblies.get(releaseIdentifier));
    }
}
