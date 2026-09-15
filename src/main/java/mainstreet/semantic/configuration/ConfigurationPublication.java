package mainstreet.semantic.configuration;

import java.util.Optional;

/**
 * Storage-neutral port for staging and resolving immutable configuration
 * release history. Publication does not activate a release.
 */
public interface ConfigurationPublication {

    void publish(ConfigurationRelease release);

    Optional<ConfigurationRelease> latest(String merchantIdentifier);

    Optional<ConfigurationRelease> version(
            String merchantIdentifier,
            long version
    );

    Optional<ConfigurationRelease> release(String releaseIdentifier);
}
