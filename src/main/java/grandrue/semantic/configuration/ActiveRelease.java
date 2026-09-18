package grandrue.semantic.configuration;

import java.util.Objects;

/**
 * Captured merchant-scoped pointer to one immutable configuration release.
 * Holding this object keeps execution on the same release even if activation
 * later changes for the merchant.
 */
public record ActiveRelease(ConfigurationRelease release) {

    public ActiveRelease {
        Objects.requireNonNull(release, "release");
    }

    public String merchantIdentifier() {
        return release.merchantIdentifier();
    }

    public String releaseIdentifier() {
        return release.releaseIdentifier();
    }
}
