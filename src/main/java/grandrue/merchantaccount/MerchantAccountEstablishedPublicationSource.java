package grandrue.merchantaccount;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Merchant Account owner port for durable post-commit publication responsibility.
 *
 * <p>An empty registered occurrence means durable owner evidence exists but its
 * historical Event Contract affinity cannot currently be resolved. It must stay
 * unpublished rather than be reinterpreted using the latest contract.</p>
 *
 * <p>Authority: MS-PROT-026 v1.1 — Production Domain Event Publication, Reaction & Consumption Contract Amendment,
 * §10 — Publication Failure After Commit; §11 — Event Publication Is Not Reaction Completion;
 * §12 — Publication Responsibility; §32 — Historical Affinity.</p>
 */
public interface MerchantAccountEstablishedPublicationSource {
    List<PendingPublication> pendingPublications(int limit);

    boolean recordPublished(String publicationIntentIdentifier, Instant publishedAt);

    record PendingPublication(
            String publicationIntentIdentifier,
            Optional<MerchantAccountEstablishedOccurrence> registeredOccurrence) {
        public PendingPublication {
            if (publicationIntentIdentifier == null || publicationIntentIdentifier.isBlank()) {
                throw new IllegalArgumentException(
                        "publicationIntentIdentifier must not be blank");
            }
            registeredOccurrence = Objects.requireNonNull(
                    registeredOccurrence, "registeredOccurrence");
        }
    }
}
