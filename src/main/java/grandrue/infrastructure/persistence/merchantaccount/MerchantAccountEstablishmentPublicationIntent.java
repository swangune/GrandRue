package grandrue.infrastructure.persistence.merchantaccount;

import grandrue.application.MerchantScope;
import grandrue.merchantaccount.MerchantAccountEstablished;
import grandrue.merchantaccount.MerchantAccountEstablishedEventContract;
import grandrue.merchantaccount.MerchantAccountEstablishedOccurrence;
import grandrue.semantic.event.EventContractAffinity;
import grandrue.semantic.event.EventContractRegistrySnapshot;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Infrastructure representation of durable publication intent for one
 * committed MerchantAccountEstablished fact.
 */
public record MerchantAccountEstablishmentPublicationIntent(
        String publicationIntentIdentifier,
        String establishmentIdentity,
        String merchantIdentifier,
        String logicalEstablishmentRequestIdentity,
        Instant occurredAt,
        Optional<Instant> publishedAt,
        Optional<EventContractAffinity> eventContractAffinity
) {
    public MerchantAccountEstablishmentPublicationIntent {
        requireIdentifier(publicationIntentIdentifier, "publicationIntentIdentifier");
        requireIdentifier(establishmentIdentity, "establishmentIdentity");
        requireIdentifier(merchantIdentifier, "merchantIdentifier");
        requireIdentifier(
                logicalEstablishmentRequestIdentity,
                "logicalEstablishmentRequestIdentity"
        );
        Objects.requireNonNull(occurredAt, "occurredAt");
        publishedAt = Objects.requireNonNull(publishedAt, "publishedAt");
        eventContractAffinity = Objects.requireNonNull(eventContractAffinity, "eventContractAffinity");
    }

    /** Legacy evidence has no inferred event registration. */
    public MerchantAccountEstablishmentPublicationIntent(String publicationIntentIdentifier,
            String establishmentIdentity, String merchantIdentifier, String logicalEstablishmentRequestIdentity,
            Instant occurredAt, Optional<Instant> publishedAt) {
        this(publicationIntentIdentifier, establishmentIdentity, merchantIdentifier,
                logicalEstablishmentRequestIdentity, occurredAt, publishedAt, Optional.empty());
    }

    /** Resolves only the exact supported owner mapping; technical publication is not reaction acknowledgement. */
    public Optional<MerchantAccountEstablishedOccurrence> registeredOccurrence(EventContractRegistrySnapshot registry) {
        Objects.requireNonNull(registry, "registry");
        return eventContractAffinity.filter(MerchantAccountEstablishedEventContract.AFFINITY::equals)
                .filter(affinity -> registry.contract(affinity)
                        .filter(MerchantAccountEstablishedEventContract.DEFINITION::equals).isPresent())
                .map(affinity -> new MerchantAccountEstablishedOccurrence(
                        "merchant-account-established-event/" + publicationIntentIdentifier, affinity, event()));
    }

    public MerchantAccountEstablished event() {
        return new MerchantAccountEstablished(
                establishmentIdentity,
                new MerchantScope(merchantIdentifier),
                logicalEstablishmentRequestIdentity,
                occurredAt
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
