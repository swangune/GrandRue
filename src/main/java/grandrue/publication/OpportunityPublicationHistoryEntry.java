package grandrue.publication;

import grandrue.application.MerchantScope;

import java.util.Objects;
import java.util.Optional;

/** Immutable ordered evidence for one successful Publication History operation. */
public record OpportunityPublicationHistoryEntry(
        MerchantScope merchantScope,
        String opportunityIdentity,
        long sequence,
        PublicationHistoryOperationKind operationKind,
        String currentRevisionIdentity,
        Optional<String> publishedRevisionIdentity,
        PublicationLifecycle resultingLifecycle
) {
    public OpportunityPublicationHistoryEntry {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireText(opportunityIdentity, "opportunityIdentity");
        if (sequence < 1) {
            throw new IllegalArgumentException("Publication History sequence must be positive");
        }
        Objects.requireNonNull(operationKind, "operationKind");
        requireText(currentRevisionIdentity, "currentRevisionIdentity");
        Objects.requireNonNull(publishedRevisionIdentity, "publishedRevisionIdentity");
        publishedRevisionIdentity.ifPresent(
                value -> requireText(value, "publishedRevisionIdentity")
        );
        Objects.requireNonNull(resultingLifecycle, "resultingLifecycle");

        if (resultingLifecycle == PublicationLifecycle.DRAFT) {
            throw new IllegalArgumentException(
                    "Publication History cannot record DRAFT as a successful publication operation result"
            );
        }
        if (publishedRevisionIdentity.isEmpty()) {
            throw new IllegalArgumentException(
                    "Publication History must preserve published revision evidence"
            );
        }
        if ((operationKind == PublicationHistoryOperationKind.PUBLISH
                || operationKind == PublicationHistoryOperationKind.REPUBLISH)
                && !publishedRevisionIdentity.orElseThrow().equals(currentRevisionIdentity)) {
            throw new IllegalArgumentException(
                    "PUBLISH or REPUBLISH must bind the exact current revision"
            );
        }
        if (operationKind == PublicationHistoryOperationKind.WITHDRAW
                && resultingLifecycle != PublicationLifecycle.WITHDRAWN) {
            throw new IllegalArgumentException(
                    "WITHDRAW Publication History must result in WITHDRAWN lifecycle"
            );
        }
        if ((operationKind == PublicationHistoryOperationKind.PUBLISH
                || operationKind == PublicationHistoryOperationKind.REPUBLISH)
                && resultingLifecycle != PublicationLifecycle.PUBLISHED) {
            throw new IllegalArgumentException(
                    "PUBLISH or REPUBLISH Publication History must result in PUBLISHED lifecycle"
            );
        }
    }

    private static void requireText(String value, String field) {
        Objects.requireNonNull(value, field);
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
    }
}
