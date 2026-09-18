package grandrue.semantic.release;

import java.util.Optional;

/**
 * Read port for resolving one exact immutable semantic release assembly.
 */
public interface SemanticReleaseAssemblyRepository {

    /**
     * Resolves the exact requested release without substituting another one.
     */
    Optional<SemanticReleaseAssembly> release(String releaseIdentifier);
}
