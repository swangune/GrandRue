package mainstreet.semantic.configuration;

import java.time.Instant;
import java.util.Objects;

/**
 * Immutable provenance for one deterministic Resolved Configuration Package.
 */
public record ResolvedConfigurationProvenance(
        String compilerIdentifier,
        Instant generatedAt
) {
    public ResolvedConfigurationProvenance {
        if (compilerIdentifier == null || compilerIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Compiler identifier must not be blank"
            );
        }
        Objects.requireNonNull(generatedAt, "generatedAt");
    }
}
