package mainstreet.media;

import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MediaSemanticModelTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-media-1");
    private static final Instant T0 = Instant.parse("2026-08-24T08:00:00Z");

    @Test
    void media_asset_identity_is_distinct_from_storage_reference() {
        MediaAsset asset = asset();
        assertEquals("asset-1", asset.assetIdentity());
        assertEquals("storage-source-ref-1", asset.canonicalSourceReference());
        assertFalse(asset.assetIdentity().equals(asset.canonicalSourceReference()));
    }

    @Test
    void rendition_profile_is_versioned_and_declares_delivery_fidelity() {
        RenditionProfile profile = new RenditionProfile(
                "public-hero-photo",
                3,
                MediaRole.HERO_IMAGE,
                MediaKind.IMAGE,
                DeliveryFidelity.CONTROLLED_LOSSY,
                Optional.empty()
        );
        assertEquals(3, profile.version());
        assertEquals(DeliveryFidelity.CONTROLLED_LOSSY, profile.deliveryFidelity());
    }

    @Test
    void short_product_video_profile_can_limit_duration_without_universal_video_limit() {
        RenditionProfile shortProductVideo = new RenditionProfile(
                "product-video-short",
                1,
                MediaRole.PRODUCT_VIDEO,
                MediaKind.VIDEO,
                DeliveryFidelity.CONTROLLED_LOSSY,
                Optional.of(Duration.ofSeconds(30))
        );
        RenditionProfile informationVideo = new RenditionProfile(
                "information-video-long-form",
                1,
                MediaRole.INFORMATION_VIDEO,
                MediaKind.VIDEO,
                DeliveryFidelity.CONTROLLED_LOSSY,
                Optional.empty()
        );

        assertTrue(shortProductVideo.acceptsDuration(Duration.ofSeconds(30)));
        assertFalse(shortProductVideo.acceptsDuration(Duration.ofSeconds(31)));
        assertTrue(informationVideo.acceptsDuration(Duration.ofHours(1)));
    }

    @Test
    void logo_profile_requires_lossless_delivery() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new RenditionProfile(
                        "merchant-logo",
                        1,
                        MediaRole.LOGO,
                        MediaKind.IMAGE,
                        DeliveryFidelity.CONTROLLED_LOSSY,
                        Optional.empty()
                )
        );
    }

    @Test
    void rendition_keeps_exact_source_and_profile_provenance() {
        MediaRendition rendition = new MediaRendition(
                "rendition-1",
                MERCHANT,
                "asset-1",
                "public-hero-photo",
                3,
                "storage-rendition-ref-1",
                "sha256:rendition",
                T0,
                MediaProcessingOutcome.SUCCEEDED
        );
        assertEquals("asset-1", rendition.sourceAssetIdentity());
        assertEquals("public-hero-photo", rendition.profileIdentity());
        assertEquals(3, rendition.profileVersion());
    }

    @Test
    void profile_registry_does_not_allow_same_identity_version_to_be_redefined() {
        RenditionProfileRegistry registry = new RenditionProfileRegistry();
        RenditionProfile profile = new RenditionProfile(
                "public-thumbnail",
                1,
                MediaRole.THUMBNAIL,
                MediaKind.IMAGE,
                DeliveryFidelity.CONTROLLED_LOSSY,
                Optional.empty()
        );
        registry.register(profile);
        assertEquals(profile, registry.require("public-thumbnail", 1));
        assertThrows(IllegalArgumentException.class, () -> registry.register(profile));
    }

    private static MediaAsset asset() {
        return new MediaAsset(
                "asset-1",
                MERCHANT,
                MediaKind.IMAGE,
                "storage-source-ref-1",
                "sha256:source",
                T0,
                MediaValidationState.VALIDATED
        );
    }
}
