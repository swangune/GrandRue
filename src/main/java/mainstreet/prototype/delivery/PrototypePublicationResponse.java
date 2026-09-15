package mainstreet.prototype.delivery;

import mainstreet.prototype.PrototypePublicationView;

import java.time.Instant;
import java.util.Optional;

/** Transport DTO for the seeded publisher read projection. */
public record PrototypePublicationResponse(
        String publicationIdentifier,
        String title,
        String summary,
        String publicationState,
        long revision,
        Optional<Instant> publishFrom,
        Optional<Instant> publishUntil
) {
    public static PrototypePublicationResponse from(
            PrototypePublicationView view
    ) {
        return new PrototypePublicationResponse(
                view.publicationIdentifier(),
                view.title(),
                view.summary(),
                view.publicationState(),
                view.revision(),
                view.publishFrom(),
                view.publishUntil()
        );
    }
}
