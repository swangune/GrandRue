package grandrue.semantic.registry;

import java.util.Optional;

/**
 * Read port for resolving immutable semantic registry releases.
 */
public interface SemanticRegistry {

    /**
     * Resolves an exact registry release without substituting a newer one.
     */
    Optional<SemanticRegistrySnapshot> version(String version);
}
