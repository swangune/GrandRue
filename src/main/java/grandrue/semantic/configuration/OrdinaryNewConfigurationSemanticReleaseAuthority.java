package grandrue.semantic.configuration;

/**
 * Platform-owned exact release selector for ordinary new configuration.
 * Merchant, onboarding, AI and transport callers do not supply this value.
 */
@FunctionalInterface
public interface OrdinaryNewConfigurationSemanticReleaseAuthority {

    String currentSemanticRegistryReleaseIdentifier();
}
