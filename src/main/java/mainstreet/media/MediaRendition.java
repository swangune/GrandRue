package mainstreet.media;

import mainstreet.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/** Derived technical representation traceable to one source and exact profile. */
public record MediaRendition(
        String renditionIdentity,
        MerchantScope merchantScope,
        String sourceAssetIdentity,
        String profileIdentity,
        int profileVersion,
        String storageReference,
        String renditionDigest,
        Instant generatedAt,
        MediaProcessingOutcome processingOutcome
) {
    public MediaRendition {
        require(renditionIdentity, "Media rendition identity");
        Objects.requireNonNull(merchantScope, "merchantScope");
        require(sourceAssetIdentity, "Source MediaAsset identity");
        require(profileIdentity, "Rendition profile identity");
        if (profileVersion < 1) {
            throw new IllegalArgumentException("Profile version must be positive");
        }
        require(storageReference, "Rendition storage reference");
        if (renditionIdentity.equals(storageReference)) {
            throw new IllegalArgumentException(
                    "Media rendition identity must not equal storage reference"
            );
        }
        require(renditionDigest, "Rendition digest");
        Objects.requireNonNull(generatedAt, "generatedAt");
        Objects.requireNonNull(processingOutcome, "processingOutcome");
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
