package grandrue.semantic.configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Process-local copy-on-write configuration history adapter. A rejected
 * publication cannot change the currently visible merchant version.
 */
public final class InMemoryConfigurationPublication
        implements ConfigurationPublication {

    private State state = State.empty();

    @Override
    public synchronized void publish(ConfigurationRelease release) {
        Objects.requireNonNull(release);

        if (state.releaseByIdentifier().containsKey(
                release.releaseIdentifier()
        )) {
            throw new IllegalArgumentException(
                    "Release identifier already published: "
                            + release.releaseIdentifier()
            );
        }

        NavigableMap<Long, ConfigurationRelease> existing =
                state.historyByMerchant().getOrDefault(
                        release.merchantIdentifier(),
                        Collections.emptyNavigableMap()
                );
        long expectedVersion = existing.isEmpty()
                ? 1
                : existing.lastKey() + 1;

        if (release.version() != expectedVersion) {
            throw new IllegalArgumentException(
                    "Expected configuration version "
                            + expectedVersion
                            + " for merchant "
                            + release.merchantIdentifier()
            );
        }

        NavigableMap<Long, ConfigurationRelease> updatedHistory =
                new TreeMap<>(existing);
        updatedHistory.put(release.version(), release);

        Map<String, NavigableMap<Long, ConfigurationRelease>>
                updatedByMerchant = new HashMap<>(
                        state.historyByMerchant()
                );
        updatedByMerchant.put(
                release.merchantIdentifier(),
                Collections.unmodifiableNavigableMap(updatedHistory)
        );

        Map<String, ConfigurationRelease> updatedByIdentifier =
                new HashMap<>(state.releaseByIdentifier());
        updatedByIdentifier.put(release.releaseIdentifier(), release);

        state = new State(
                Map.copyOf(updatedByMerchant),
                Map.copyOf(updatedByIdentifier)
        );
    }

    @Override
    public synchronized Optional<ConfigurationRelease> latest(
            String merchantIdentifier
    ) {
        requireIdentifier(merchantIdentifier);
        NavigableMap<Long, ConfigurationRelease> history =
                state.historyByMerchant().get(merchantIdentifier);
        return history == null || history.isEmpty()
                ? Optional.empty()
                : Optional.of(history.lastEntry().getValue());
    }

    @Override
    public synchronized Optional<ConfigurationRelease> version(
            String merchantIdentifier,
            long version
    ) {
        requireIdentifier(merchantIdentifier);
        if (version < 1) {
            throw new IllegalArgumentException(
                    "Configuration version must be positive"
            );
        }

        NavigableMap<Long, ConfigurationRelease> history =
                state.historyByMerchant().get(merchantIdentifier);
        return history == null
                ? Optional.empty()
                : Optional.ofNullable(history.get(version));
    }

    @Override
    public synchronized Optional<ConfigurationRelease> release(
            String releaseIdentifier
    ) {
        requireReleaseIdentifier(releaseIdentifier);
        return Optional.ofNullable(
                state.releaseByIdentifier().get(releaseIdentifier)
        );
    }

    private static void requireIdentifier(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Merchant identifier must not be blank"
            );
        }
    }

    private static void requireReleaseIdentifier(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Release identifier must not be blank"
            );
        }
    }

    private record State(
            Map<String, NavigableMap<Long, ConfigurationRelease>>
                    historyByMerchant,
            Map<String, ConfigurationRelease> releaseByIdentifier
    ) {

        private static State empty() {
            return new State(Map.of(), Map.of());
        }
    }
}
