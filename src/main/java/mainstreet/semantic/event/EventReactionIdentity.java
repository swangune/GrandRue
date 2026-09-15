package mainstreet.semantic.event;

import java.util.Objects;

/** One logical reaction of one owner contract to one occurrence; delivery/attempt/release identity is separate. */
public record EventReactionIdentity(String eventIdentity, EventReactionContractIdentity contractIdentity) {
    public EventReactionIdentity {
        EventContractIdentity.requireReference(eventIdentity, "eventIdentity");
        Objects.requireNonNull(contractIdentity, "contractIdentity");
    }
}
