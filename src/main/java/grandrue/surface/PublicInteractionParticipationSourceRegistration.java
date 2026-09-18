package grandrue.surface;

import java.util.Objects;

/** One explicitly registered capability-owned participation source. */
public record PublicInteractionParticipationSourceRegistration(
        PublicInteractionParticipationSourceIdentity identity,
        PublicInteractionParticipationSource source
) {
    public PublicInteractionParticipationSourceRegistration {
        Objects.requireNonNull(identity, "identity");
        Objects.requireNonNull(source, "source");
    }
}
