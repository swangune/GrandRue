package grandrue.media;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** Registry keyed by exact profile identity and version. */
public final class RenditionProfileRegistry {
    private final Map<Key, RenditionProfile> profiles = new LinkedHashMap<>();

    public void register(RenditionProfile profile) {
        Objects.requireNonNull(profile, "profile");
        Key key = new Key(profile.profileIdentity(), profile.version());
        if (profiles.putIfAbsent(key, profile) != null) {
            throw new IllegalArgumentException(
                    "Rendition profile already registered: "
                            + profile.profileIdentity() + "@" + profile.version()
            );
        }
    }

    public RenditionProfile require(String identity, int version) {
        RenditionProfile profile = profiles.get(new Key(identity, version));
        if (profile == null) {
            throw new IllegalArgumentException(
                    "Unknown Rendition Profile: " + identity + "@" + version
            );
        }
        return profile;
    }

    private record Key(String identity, int version) {
        private Key {
            if (identity == null || identity.isBlank()) {
                throw new IllegalArgumentException("Profile identity must not be blank");
            }
            if (version < 1) {
                throw new IllegalArgumentException("Profile version must be positive");
            }
        }
    }
}
