package grandrue.notification;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** Evidence about notification transport/delivery, never business acknowledgement. */
public record DeliveryEvidence(
        String evidenceIdentity,
        String dispatchIdentity,
        NotificationOwnerScope ownerScope,
        Optional<String> attemptIdentity,
        String evidenceTypeIdentifier,
        Instant observedAt,
        Optional<String> providerReference,
        String provenanceIdentifier
) {
    public DeliveryEvidence {
        require(evidenceIdentity, "Delivery Evidence identity");
        require(dispatchIdentity, "Dispatch identity");
        Objects.requireNonNull(ownerScope, "ownerScope");
        attemptIdentity = Objects.requireNonNull(attemptIdentity, "attemptIdentity");
        attemptIdentity.ifPresent(value -> require(value, "Attempt identity"));
        require(evidenceTypeIdentifier, "Evidence type");
        Objects.requireNonNull(observedAt, "observedAt");
        providerReference = Objects.requireNonNull(providerReference, "providerReference");
        providerReference.ifPresent(value -> require(value, "Provider reference"));
        require(provenanceIdentifier, "Evidence provenance");
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
