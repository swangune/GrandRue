package mainstreet.publication;

import grandrue.application.MerchantScope;

import java.util.Objects;
import java.util.Optional;

/** Ordered durable evidence for one authoritative Publication lifecycle operation. */
public record OpportunityPublicationLifecycleHistoryEntry(
        MerchantScope merchantScope,
        String opportunityIdentity,
        long sequence,
        PublicationTransitionKind transitionKind,
        Optional<PublicationLifecycle> previousLifecycle,
        PublicationLifecycle lifecycle,
        String currentRevisionIdentity,
        Optional<String> publishedRevisionIdentity
) {
    public OpportunityPublicationLifecycleHistoryEntry {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireText(opportunityIdentity, "opportunityIdentity");
        if (sequence < 1) {
            throw new IllegalArgumentException("History sequence must be positive");
        }
        Objects.requireNonNull(transitionKind, "transitionKind");
        Objects.requireNonNull(previousLifecycle, "previousLifecycle");
        Objects.requireNonNull(lifecycle, "lifecycle");
        requireText(currentRevisionIdentity, "currentRevisionIdentity");
        Objects.requireNonNull(publishedRevisionIdentity, "publishedRevisionIdentity");
        publishedRevisionIdentity.ifPresent(
                value -> requireText(value, "publishedRevisionIdentity")
        );

        if (lifecycle == PublicationLifecycle.DRAFT && publishedRevisionIdentity.isPresent()) {
            throw new IllegalArgumentException("DRAFT history cannot have a published revision");
        }
        if (lifecycle != PublicationLifecycle.DRAFT && publishedRevisionIdentity.isEmpty()) {
            throw new IllegalArgumentException(
                    "PUBLISHED or WITHDRAWN history must preserve published revision evidence"
            );
        }

        switch (transitionKind) {
            case ESTABLISH -> {
                if (previousLifecycle.isPresent()) {
                    throw new IllegalArgumentException(
                            "ESTABLISH history cannot have a previous lifecycle"
                    );
                }
            }
            case PUBLISH -> {
                PublicationLifecycle previous = previousLifecycle.orElseThrow(
                        () -> new IllegalArgumentException(
                                "PUBLISH history requires a previous lifecycle"
                        )
                );
                if ((previous != PublicationLifecycle.DRAFT
                        && previous != PublicationLifecycle.PUBLISHED)
                        || lifecycle != PublicationLifecycle.PUBLISHED
                        || !publishedCurrent(currentRevisionIdentity, publishedRevisionIdentity)) {
                    throw new IllegalArgumentException("Invalid PUBLISH history evidence");
                }
            }
            case WITHDRAW -> {
                if (previousLifecycle.orElse(null) != PublicationLifecycle.PUBLISHED
                        || lifecycle != PublicationLifecycle.WITHDRAWN) {
                    throw new IllegalArgumentException("Invalid WITHDRAW history evidence");
                }
            }
            case REPUBLISH -> {
                if (previousLifecycle.orElse(null) != PublicationLifecycle.WITHDRAWN
                        || lifecycle != PublicationLifecycle.PUBLISHED
                        || !publishedCurrent(currentRevisionIdentity, publishedRevisionIdentity)) {
                    throw new IllegalArgumentException("Invalid REPUBLISH history evidence");
                }
            }
        }
    }

    private static boolean publishedCurrent(
            String currentRevisionIdentity,
            Optional<String> publishedRevisionIdentity
    ) {
        return publishedRevisionIdentity.filter(currentRevisionIdentity::equals).isPresent();
    }

    private static void requireText(String value, String field) {
        Objects.requireNonNull(value, field);
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
    }
}
