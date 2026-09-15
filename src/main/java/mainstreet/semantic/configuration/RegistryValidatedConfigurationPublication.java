package mainstreet.semantic.configuration;

import mainstreet.semantic.registry.SemanticDefinitionKind;
import mainstreet.semantic.registry.SemanticRegistry;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;

import java.util.Objects;
import java.util.Optional;

/**
 * Publication guard that rejects releases compiled against unknown registry
 * snapshots or containing unknown capabilities before staging them.
 */
public final class RegistryValidatedConfigurationPublication
        implements ConfigurationPublication {

    private final SemanticRegistry registry;
    private final ConfigurationPublication publication;

    public RegistryValidatedConfigurationPublication(
            SemanticRegistry registry,
            ConfigurationPublication publication
    ) {
        this.registry = Objects.requireNonNull(registry);
        this.publication = Objects.requireNonNull(publication);
    }

    @Override
    public void publish(ConfigurationRelease release) {
        Objects.requireNonNull(release);
        SemanticRegistrySnapshot snapshot = registry.version(
                        release.semanticRegistryVersion()
                )
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown semantic registry version: "
                                + release.semanticRegistryVersion()
                ));

        for (String capability
                : release.executableModel().capabilityIdentifiers()) {
            if (!snapshot.contains(
                    SemanticDefinitionKind.CAPABILITY,
                    capability
            )) {
                throw new IllegalArgumentException(
                        "Capability is not registered in "
                                + snapshot.version()
                                + ": "
                                + capability
                );
            }
        }

        publication.publish(release);
    }

    @Override
    public Optional<ConfigurationRelease> latest(
            String merchantIdentifier
    ) {
        return publication.latest(merchantIdentifier);
    }

    @Override
    public Optional<ConfigurationRelease> version(
            String merchantIdentifier,
            long version
    ) {
        return publication.version(merchantIdentifier, version);
    }

    @Override
    public Optional<ConfigurationRelease> release(
            String releaseIdentifier
    ) {
        return publication.release(releaseIdentifier);
    }
}
