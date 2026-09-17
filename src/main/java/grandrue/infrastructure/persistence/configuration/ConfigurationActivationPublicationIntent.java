package grandrue.infrastructure.persistence.configuration;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Infrastructure representation of durable publication intent for the
 * accepted ConfigurationRevisionActivated post-commit fact.
 *
 * <p>This is delivery/recovery evidence only. It does not own Configuration
 * activation semantics and does not grant mutation authority to consumers.</p>
 */
public record ConfigurationActivationPublicationIntent(
        String publicationIntentIdentifier,
        String activationRequestIdentifier,
        String merchantIdentifier,
        String configurationRevisionIdentifier,
        String releaseIdentifier,
        Instant occurredAt,
        Optional<Instant> publishedAt
) {

    public ConfigurationActivationPublicationIntent {
        requireIdentifier(
                publicationIntentIdentifier,
                "Publication intent identifier"
        );
        requireIdentifier(
                activationRequestIdentifier,
                "Activation request identifier"
        );
        requireIdentifier(merchantIdentifier, "Merchant identifier");
        requireIdentifier(
                configurationRevisionIdentifier,
                "Configuration revision identifier"
        );
        requireIdentifier(releaseIdentifier, "Release identifier");
        occurredAt = Objects.requireNonNull(occurredAt, "occurredAt");
        publishedAt = Objects.requireNonNull(publishedAt, "publishedAt");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
