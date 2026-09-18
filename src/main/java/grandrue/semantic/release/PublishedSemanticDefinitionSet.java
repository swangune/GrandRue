package grandrue.semantic.release;

import java.util.Objects;

/**
 * Immutable build input representing the accepted Published Semantic Definition
 * Set established by ADR-011 for one exact Semantic Registry Release.
 *
 * <p>This type preserves exact release identity, publication provenance and the
 * release-affined definition bytes. Construction belongs to the trusted
 * publication/build boundary; this value object does not independently
 * authenticate who produced that evidence.</p>
 */
public final class PublishedSemanticDefinitionSet {

    private final String releaseIdentifier;
    private final String publicationProvenance;
    private final byte[] semanticDefinitions;
    private final byte[] surfaceDefinitions;
    private final byte[] fulfilmentDefinitions;
    private final byte[] exposureDefinitions;

    /**
     * Legacy published evidence lacking an explicit Exposure definition
     * constituent. It remains representable for historical evidence handling,
     * but cannot be packaged as a complete v2 bundle.
     */
    public PublishedSemanticDefinitionSet(
            String releaseIdentifier,
            String publicationProvenance,
            byte[] semanticDefinitions,
            byte[] surfaceDefinitions,
            byte[] fulfilmentDefinitions
    ) {
        this(
                releaseIdentifier,
                publicationProvenance,
                semanticDefinitions,
                surfaceDefinitions,
                fulfilmentDefinitions,
                null
        );
    }

    /** Current published evidence including explicit Exposure definitions. */
    public PublishedSemanticDefinitionSet(
            String releaseIdentifier,
            String publicationProvenance,
            byte[] semanticDefinitions,
            byte[] surfaceDefinitions,
            byte[] fulfilmentDefinitions,
            byte[] exposureDefinitions
    ) {
        this.releaseIdentifier = requireText(
                releaseIdentifier,
                "releaseIdentifier"
        );
        this.publicationProvenance = requireText(
                publicationProvenance,
                "publicationProvenance"
        );
        this.semanticDefinitions = copy(
                semanticDefinitions,
                "semanticDefinitions"
        );
        this.surfaceDefinitions = copy(
                surfaceDefinitions,
                "surfaceDefinitions"
        );
        this.fulfilmentDefinitions = copy(
                fulfilmentDefinitions,
                "fulfilmentDefinitions"
        );
        this.exposureDefinitions = exposureDefinitions == null
                ? null
                : exposureDefinitions.clone();
    }

    public String releaseIdentifier() {
        return releaseIdentifier;
    }

    public String publicationProvenance() {
        return publicationProvenance;
    }

    public byte[] semanticDefinitions() {
        return semanticDefinitions.clone();
    }

    public byte[] surfaceDefinitions() {
        return surfaceDefinitions.clone();
    }

    public byte[] fulfilmentDefinitions() {
        return fulfilmentDefinitions.clone();
    }

    public boolean hasExposureDefinitions() {
        return exposureDefinitions != null;
    }

    public byte[] exposureDefinitions() {
        if (exposureDefinitions == null) {
            throw new IllegalStateException(
                    "Published evidence has no Exposure definition constituent"
            );
        }
        return exposureDefinitions.clone();
    }

    private static byte[] copy(byte[] value, String field) {
        return Objects.requireNonNull(value, field).clone();
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value;
    }
}
