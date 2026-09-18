package grandrue.media;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/**
 * Main Street media identity plus canonical-source provenance. Storage reference
 * remains infrastructure metadata and is never the MediaAsset identity.
 */
public record MediaAsset(
        String assetIdentity,
        MerchantScope merchantScope,
        MediaKind mediaKind,
        String canonicalSourceReference,
        String sourceDigest,
        Instant createdAt,
        MediaValidationState validationState
) {
    public MediaAsset {
        require(assetIdentity, "MediaAsset identity");
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(mediaKind, "mediaKind");
        require(canonicalSourceReference, "Canonical source reference");
        if (assetIdentity.equals(canonicalSourceReference)) {
            throw new IllegalArgumentException(
                    "MediaAsset identity must not equal storage/source reference"
            );
        }
        require(sourceDigest, "Canonical source digest");
        Objects.requireNonNull(createdAt, "createdAt");
        Objects.requireNonNull(validationState, "validationState");
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
