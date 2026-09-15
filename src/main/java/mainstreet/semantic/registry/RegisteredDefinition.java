package mainstreet.semantic.registry;

/**
 * Platform-owned semantic meaning that can be addressed by merchant
 * configuration through a registry release.
 */
public interface RegisteredDefinition {

    /**
     * Returns the stable semantic identity. Release/version information is
     * deliberately owned by the containing registry snapshot.
     */
    SemanticIdentity identity();
}
