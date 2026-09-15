package mainstreet.semantic.event;

import java.time.Instant;
import java.util.Objects;

public record EventReactionAcknowledgement(
        EventReactionIdentity identity, String outcomeReference, Instant acknowledgedAt) {
    public EventReactionAcknowledgement {
        Objects.requireNonNull(identity, "identity");
        EventContractIdentity.requireReference(outcomeReference, "outcomeReference");
        Objects.requireNonNull(acknowledgedAt, "acknowledgedAt");
    }
}
