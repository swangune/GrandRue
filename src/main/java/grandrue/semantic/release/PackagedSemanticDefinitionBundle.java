package grandrue.semantic.release;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Locale;
import java.util.Objects;

/**
 * Immutable packaged evidence for one exact published Semantic Registry
 * Release.
 *
 * <p>Definition sections remain opaque at this boundary so their owning
 * registries retain definition-language ownership. This envelope owns only
 * exact release identity, publication provenance and content integrity.</p>
 */
public final class PackagedSemanticDefinitionBundle {

    public static final String V1_FORMAT = "mainstreet-semantic-bundle-v1";
    public static final String CURRENT_FORMAT = "mainstreet-semantic-bundle-v2";

    private final String formatVersion;
    private final String releaseIdentifier;
    private final String publicationProvenance;
    private final String contentDigest;
    private final byte[] semanticDefinitions;
    private final byte[] surfaceDefinitions;
    private final byte[] fulfilmentDefinitions;
    private final byte[] exposureDefinitions;

    /**
     * Legacy three-section representation. It deliberately cannot be labelled
     * as the current v2 format because v2 requires explicit Exposure evidence.
     */
    public PackagedSemanticDefinitionBundle(
            String formatVersion,
            String releaseIdentifier,
            String publicationProvenance,
            String contentDigest,
            byte[] semanticDefinitions,
            byte[] surfaceDefinitions,
            byte[] fulfilmentDefinitions
    ) {
        this.formatVersion = requireText(formatVersion, "formatVersion");
        if (CURRENT_FORMAT.equals(this.formatVersion)) {
            throw new IllegalArgumentException(
                    "Semantic bundle v2 requires explicit Exposure definitions"
            );
        }
        this.releaseIdentifier = requireText(
                releaseIdentifier,
                "releaseIdentifier"
        );
        this.publicationProvenance = requireText(
                publicationProvenance,
                "publicationProvenance"
        );
        this.contentDigest = requireText(contentDigest, "contentDigest")
                .toLowerCase(Locale.ROOT);
        this.semanticDefinitions = copy(
                semanticDefinitions,
                "semanticDefinitions"
        );
        this.surfaceDefinitions = copy(surfaceDefinitions, "surfaceDefinitions");
        this.fulfilmentDefinitions = copy(
                fulfilmentDefinitions,
                "fulfilmentDefinitions"
        );
        this.exposureDefinitions = null;

        if (!integrityVerified()) {
            throw integrityFailure();
        }
    }

    /** Current four-section representation including exact Exposure evidence. */
    public PackagedSemanticDefinitionBundle(
            String formatVersion,
            String releaseIdentifier,
            String publicationProvenance,
            String contentDigest,
            byte[] semanticDefinitions,
            byte[] surfaceDefinitions,
            byte[] fulfilmentDefinitions,
            byte[] exposureDefinitions
    ) {
        this.formatVersion = requireText(formatVersion, "formatVersion");
        if (V1_FORMAT.equals(this.formatVersion)) {
            throw new IllegalArgumentException(
                    "Semantic bundle v1 cannot contain Exposure definitions"
            );
        }
        this.releaseIdentifier = requireText(
                releaseIdentifier,
                "releaseIdentifier"
        );
        this.publicationProvenance = requireText(
                publicationProvenance,
                "publicationProvenance"
        );
        this.contentDigest = requireText(contentDigest, "contentDigest")
                .toLowerCase(Locale.ROOT);
        this.semanticDefinitions = copy(
                semanticDefinitions,
                "semanticDefinitions"
        );
        this.surfaceDefinitions = copy(surfaceDefinitions, "surfaceDefinitions");
        this.fulfilmentDefinitions = copy(
                fulfilmentDefinitions,
                "fulfilmentDefinitions"
        );
        this.exposureDefinitions = copy(
                exposureDefinitions,
                "exposureDefinitions"
        );

        if (!integrityVerified()) {
            throw integrityFailure();
        }
    }

    public String formatVersion() {
        return formatVersion;
    }

    public String releaseIdentifier() {
        return releaseIdentifier;
    }

    public String publicationProvenance() {
        return publicationProvenance;
    }

    public String contentDigest() {
        return contentDigest;
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
                    "Legacy semantic bundle has no Exposure definition section"
            );
        }
        return exposureDefinitions.clone();
    }

    public boolean integrityVerified() {
        String computed = exposureDefinitions == null
                ? computeDigest(
                        formatVersion,
                        releaseIdentifier,
                        publicationProvenance,
                        semanticDefinitions,
                        surfaceDefinitions,
                        fulfilmentDefinitions
                )
                : computeDigest(
                        formatVersion,
                        releaseIdentifier,
                        publicationProvenance,
                        semanticDefinitions,
                        surfaceDefinitions,
                        fulfilmentDefinitions,
                        exposureDefinitions
                );
        return MessageDigest.isEqual(
                contentDigest.getBytes(StandardCharsets.US_ASCII),
                computed.getBytes(StandardCharsets.US_ASCII)
        );
    }

    /** Legacy v1-compatible digest over the original three definition sections. */
    public static String computeDigest(
            String formatVersion,
            String releaseIdentifier,
            String publicationProvenance,
            byte[] semanticDefinitions,
            byte[] surfaceDefinitions,
            byte[] fulfilmentDefinitions
    ) {
        return digest(canonicalBytes(
                requireText(formatVersion, "formatVersion"),
                requireText(releaseIdentifier, "releaseIdentifier"),
                requireText(publicationProvenance, "publicationProvenance"),
                copy(semanticDefinitions, "semanticDefinitions"),
                copy(surfaceDefinitions, "surfaceDefinitions"),
                copy(fulfilmentDefinitions, "fulfilmentDefinitions")
        ));
    }

    /** v2 digest including the dedicated Exposure definition section. */
    public static String computeDigest(
            String formatVersion,
            String releaseIdentifier,
            String publicationProvenance,
            byte[] semanticDefinitions,
            byte[] surfaceDefinitions,
            byte[] fulfilmentDefinitions,
            byte[] exposureDefinitions
    ) {
        return digest(canonicalBytes(
                requireText(formatVersion, "formatVersion"),
                requireText(releaseIdentifier, "releaseIdentifier"),
                requireText(publicationProvenance, "publicationProvenance"),
                copy(semanticDefinitions, "semanticDefinitions"),
                copy(surfaceDefinitions, "surfaceDefinitions"),
                copy(fulfilmentDefinitions, "fulfilmentDefinitions"),
                copy(exposureDefinitions, "exposureDefinitions")
        ));
    }

    private static String digest(byte[] canonicalBytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(canonicalBytes));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(
                    "Required SHA-256 digest algorithm is unavailable",
                    exception
            );
        }
    }

    private static byte[] canonicalBytes(
            String formatVersion,
            String releaseIdentifier,
            String publicationProvenance,
            byte[] semanticDefinitions,
            byte[] surfaceDefinitions,
            byte[] fulfilmentDefinitions,
            byte[]... additionalSections
    ) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try (DataOutputStream output = new DataOutputStream(buffer)) {
                writeBytes(output, formatVersion.getBytes(StandardCharsets.UTF_8));
                writeBytes(output, releaseIdentifier.getBytes(StandardCharsets.UTF_8));
                writeBytes(output, publicationProvenance.getBytes(StandardCharsets.UTF_8));
                writeBytes(output, semanticDefinitions);
                writeBytes(output, surfaceDefinitions);
                writeBytes(output, fulfilmentDefinitions);
                for (byte[] section : additionalSections) {
                    writeBytes(output, section);
                }
            }
            return buffer.toByteArray();
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Unexpected in-memory semantic digest encoding failure",
                    exception
            );
        }
    }

    private SemanticMaterialisationException integrityFailure() {
        return new SemanticMaterialisationException(
                SemanticMaterialisationFailure.CONTENT_INTEGRITY_FAILURE,
                "Semantic definition bundle content digest does not match "
                        + "published evidence for release: "
                        + releaseIdentifier
        );
    }

    private static void writeBytes(DataOutputStream output, byte[] value)
            throws IOException {
        output.writeInt(value.length);
        output.write(value);
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
