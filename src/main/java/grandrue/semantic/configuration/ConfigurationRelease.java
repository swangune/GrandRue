package grandrue.semantic.configuration;

import grandrue.semantic.executable.ExecutableMerchantModel;

import java.time.Instant;
import java.util.Objects;

/**
 * Immutable release envelope binding one authoritative configuration revision
 * to the exact derived Resolved Configuration Package produced from it.
 */
public record ConfigurationRelease(
        String releaseIdentifier,
        MerchantConfiguration configuration,
        ResolvedConfigurationPackage resolvedPackage
) {

    public ConfigurationRelease {
        requireIdentifier(releaseIdentifier, "Release identifier");
        Objects.requireNonNull(configuration, "configuration");
        Objects.requireNonNull(resolvedPackage, "resolvedPackage");
        resolvedPackage.requireSourceConfiguration(configuration);
    }

    /**
     * Compatibility constructor for existing callers while package production
     * is promoted through the compiler/resolution boundary.
     */
    public ConfigurationRelease(
            String releaseIdentifier,
            MerchantConfiguration configuration,
            ExecutableMerchantModel executableModel,
            String compilerIdentifier,
            Instant generatedAt
    ) {
        this(
                releaseIdentifier,
                configuration,
                ResolvedConfigurationPackage.currentFoundation(
                        configuration,
                        executableModel,
                        new ResolvedConfigurationProvenance(
                                compilerIdentifier,
                                generatedAt
                        )
                )
        );
    }

    public ExecutableMerchantModel executableModel() {
        return resolvedPackage.executableSemanticModel();
    }

    public String compilerIdentifier() {
        return resolvedPackage.provenance().compilerIdentifier();
    }

    public Instant generatedAt() {
        return resolvedPackage.provenance().generatedAt();
    }

    public String merchantIdentifier() {
        return configuration.merchantIdentifier();
    }

    public String configurationIdentifier() {
        return configuration.configurationIdentifier();
    }

    public long version() {
        return configuration.version();
    }

    public String semanticRegistryVersion() {
        return configuration.semanticRegistryVersion();
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
