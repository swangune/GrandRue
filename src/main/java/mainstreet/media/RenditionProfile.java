package mainstreet.media;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

/**
 * Versioned deterministic technical processing profile. It intentionally does
 * not carry arbitrary crop/editorial instructions or provider-specific codec
 * choices.
 */
public record RenditionProfile(
        String profileIdentity,
        int version,
        MediaRole mediaRole,
        MediaKind mediaKind,
        DeliveryFidelity deliveryFidelity,
        Optional<Duration> maximumSourceDuration
) {
    public RenditionProfile {
        require(profileIdentity, "Rendition profile identity");
        if (version < 1) {
            throw new IllegalArgumentException("Rendition profile version must be positive");
        }
        Objects.requireNonNull(mediaRole, "mediaRole");
        Objects.requireNonNull(mediaKind, "mediaKind");
        Objects.requireNonNull(deliveryFidelity, "deliveryFidelity");
        maximumSourceDuration = Objects.requireNonNull(
                maximumSourceDuration,
                "maximumSourceDuration"
        );
        maximumSourceDuration.ifPresent(duration -> {
            if (duration.isZero() || duration.isNegative()) {
                throw new IllegalArgumentException("Maximum duration must be positive");
            }
            if (mediaKind != MediaKind.VIDEO) {
                throw new IllegalArgumentException(
                        "Duration constraints apply only to video profiles"
                );
            }
        });
        if (mediaRole == MediaRole.LOGO
                && deliveryFidelity != DeliveryFidelity.LOSSLESS) {
            throw new IllegalArgumentException(
                    "Merchant logo rendition profiles must preserve lossless fidelity"
            );
        }
        requireKindAffinity(mediaRole, mediaKind);
    }

    public boolean acceptsDuration(Duration duration) {
        Objects.requireNonNull(duration, "duration");
        return maximumSourceDuration
                .map(maximum -> duration.compareTo(maximum) <= 0)
                .orElse(true);
    }

    private static void requireKindAffinity(MediaRole role, MediaKind kind) {
        switch (role) {
            case PRODUCT_VIDEO, PROFILE_VIDEO, INFORMATION_VIDEO -> {
                if (kind != MediaKind.VIDEO) {
                    throw new IllegalArgumentException("Video role requires VIDEO media kind");
                }
            }
            case DOCUMENT -> {
                if (kind != MediaKind.DOCUMENT) {
                    throw new IllegalArgumentException("Document role requires DOCUMENT media kind");
                }
            }
            default -> {
                if (kind != MediaKind.IMAGE) {
                    throw new IllegalArgumentException("Image role requires IMAGE media kind");
                }
            }
        }
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
