package mainstreet.prototype;

import java.time.Instant;
import java.util.Optional;

/**
 * Seeded read projection for the information-only prototype merchant.
 * It is deliberately not Publication mutation authority.
 */
public record PrototypePublicationView(
        String publicationIdentifier,
        String title,
        String summary,
        String publicationState,
        long revision,
        Optional<Instant> publishFrom,
        Optional<Instant> publishUntil
) {
}
