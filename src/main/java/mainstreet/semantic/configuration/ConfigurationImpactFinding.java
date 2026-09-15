package mainstreet.semantic.configuration;

import java.util.Objects;

/** One classified finding expressed in intelligible business language. */
public record ConfigurationImpactFinding(
        ConfigurationImpactClassification classification,
        String businessFacingDescription
) {

    public ConfigurationImpactFinding {
        Objects.requireNonNull(classification, "classification");
        if (businessFacingDescription == null
                || businessFacingDescription.isBlank()) {
            throw new IllegalArgumentException(
                    "Business-facing finding must not be blank"
            );
        }
    }
}
