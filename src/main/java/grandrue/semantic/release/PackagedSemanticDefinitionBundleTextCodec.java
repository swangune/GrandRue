package grandrue.semantic.release;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Deterministic UTF-8 resource envelope for packaged semantic definition
 * bundles. Definition sections remain opaque bytes owned by their registries.
 */
public final class PackagedSemanticDefinitionBundleTextCodec {

    private static final String[] BASE_REQUIRED_KEYS = {
            "format",
            "release",
            "provenance",
            "digest",
            "semantic",
            "surface",
            "fulfilment"
    };
    private static final String EXPOSURE_KEY = "exposure";

    public byte[] encode(PackagedSemanticDefinitionBundle bundle) {
        if (bundle == null) {
            throw new IllegalArgumentException("bundle must not be null");
        }
        StringBuilder text = new StringBuilder();
        append(text, "format", encodeText(bundle.formatVersion()));
        append(text, "release", encodeText(bundle.releaseIdentifier()));
        append(text, "provenance", encodeText(bundle.publicationProvenance()));
        append(text, "digest", bundle.contentDigest());
        append(text, "semantic", encodeBytes(bundle.semanticDefinitions()));
        append(text, "surface", encodeBytes(bundle.surfaceDefinitions()));
        append(text, "fulfilment", encodeBytes(bundle.fulfilmentDefinitions()));
        if (bundle.hasExposureDefinitions()) {
            append(text, EXPOSURE_KEY, encodeBytes(bundle.exposureDefinitions()));
        }
        return text.toString().getBytes(StandardCharsets.UTF_8);
    }

    public PackagedSemanticDefinitionBundle decode(byte[] encodedBundle) {
        if (encodedBundle == null) {
            throw new IllegalArgumentException("encodedBundle must not be null");
        }
        try {
            return decodeResource(encodedBundle);
        } catch (SemanticMaterialisationException exception) {
            throw exception;
        } catch (IllegalArgumentException exception) {
            throw new SemanticMaterialisationException(
                    SemanticMaterialisationFailure.UNSUPPORTED_BUNDLE_FORMAT,
                    "Unable to deterministically interpret semantic bundle resource",
                    exception
            );
        }
    }

    private static PackagedSemanticDefinitionBundle decodeResource(
            byte[] encodedBundle
    ) {
        String text = new String(encodedBundle, StandardCharsets.UTF_8);
        Map<String, String> values = new LinkedHashMap<>();
        String[] lines = text.split("\\n", -1);
        for (String line : lines) {
            if (line.isEmpty()) {
                continue;
            }
            int separator = line.indexOf('=');
            if (separator <= 0) {
                throw new IllegalArgumentException(
                        "Invalid semantic bundle resource line"
                );
            }
            String key = line.substring(0, separator);
            String value = line.substring(separator + 1);
            if (!isKnownKey(key)) {
                throw new IllegalArgumentException(
                        "Unknown semantic bundle resource field: " + key
                );
            }
            if (values.putIfAbsent(key, value) != null) {
                throw new IllegalArgumentException(
                        "Duplicate semantic bundle resource field: " + key
                );
            }
        }

        for (String key : BASE_REQUIRED_KEYS) {
            if (!values.containsKey(key)) {
                throw new IllegalArgumentException(
                        "Missing semantic bundle resource field: " + key
                );
            }
        }

        String format = decodeText(values.get("format"));
        boolean hasExposure = values.containsKey(EXPOSURE_KEY);
        if (PackagedSemanticDefinitionBundle.V1_FORMAT.equals(format)
                && hasExposure) {
            throw new IllegalArgumentException(
                    "Semantic bundle v1 must not contain Exposure definitions"
            );
        }
        if (PackagedSemanticDefinitionBundle.CURRENT_FORMAT.equals(format)
                && !hasExposure) {
            throw new IllegalArgumentException(
                    "Semantic bundle v2 requires Exposure definitions"
            );
        }

        if (!hasExposure) {
            return new PackagedSemanticDefinitionBundle(
                    format,
                    decodeText(values.get("release")),
                    decodeText(values.get("provenance")),
                    values.get("digest"),
                    decodeBytes(values.get("semantic")),
                    decodeBytes(values.get("surface")),
                    decodeBytes(values.get("fulfilment"))
            );
        }

        return new PackagedSemanticDefinitionBundle(
                format,
                decodeText(values.get("release")),
                decodeText(values.get("provenance")),
                values.get("digest"),
                decodeBytes(values.get("semantic")),
                decodeBytes(values.get("surface")),
                decodeBytes(values.get("fulfilment")),
                decodeBytes(values.get(EXPOSURE_KEY))
        );
    }

    private static void append(StringBuilder text, String key, String value) {
        text.append(key).append('=').append(value).append('\n');
    }

    private static String encodeText(String value) {
        return encodeBytes(value.getBytes(StandardCharsets.UTF_8));
    }

    private static String decodeText(String value) {
        return new String(decodeBytes(value), StandardCharsets.UTF_8);
    }

    private static String encodeBytes(byte[] value) {
        return Base64.getEncoder().encodeToString(value);
    }

    private static byte[] decodeBytes(String value) {
        try {
            return Base64.getDecoder().decode(value);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Invalid Base64 semantic bundle resource field",
                    exception
            );
        }
    }

    private static boolean isKnownKey(String candidate) {
        if (EXPOSURE_KEY.equals(candidate)) {
            return true;
        }
        for (String key : BASE_REQUIRED_KEYS) {
            if (key.equals(candidate)) {
                return true;
            }
        }
        return false;
    }
}
