package grandrue.background;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Minimized read-only evidence for one outstanding durable work responsibility.
 * It is diagnostic progression evidence, not a business outcome or retry
 * decision.
 */
public record DurableWorkProgressEvidence(
        String workIdentity,
        BackgroundWorkContractAffinity contractAffinity,
        Optional<MerchantScope> merchantScope,
        Instant dueAt,
        Instant nextAttemptAt,
        Instant createdAt,
        Optional<Instant> claimExpiresAt,
        int attemptCount,
        Optional<String> latestAttemptIdentity,
        Optional<Instant> latestAttemptedAt,
        Optional<BackgroundWorkResultClassification>
                latestResultClassification
) {
    public DurableWorkProgressEvidence {
        requireReference(workIdentity, "workIdentity");
        Objects.requireNonNull(
                contractAffinity,
                "contractAffinity"
        );
        merchantScope = Objects.requireNonNull(
                merchantScope,
                "merchantScope"
        );
        Objects.requireNonNull(dueAt, "dueAt");
        Objects.requireNonNull(
                nextAttemptAt,
                "nextAttemptAt"
        );
        Objects.requireNonNull(createdAt, "createdAt");
        claimExpiresAt = Objects.requireNonNull(
                claimExpiresAt,
                "claimExpiresAt"
        );
        if (attemptCount < 0) {
            throw new IllegalArgumentException(
                    "attemptCount must not be negative"
            );
        }
        latestAttemptIdentity = Objects.requireNonNull(
                latestAttemptIdentity,
                "latestAttemptIdentity"
        );
        latestAttemptIdentity.ifPresent(value ->
                requireReference(
                        value,
                        "latestAttemptIdentity"
                )
        );
        latestAttemptedAt = Objects.requireNonNull(
                latestAttemptedAt,
                "latestAttemptedAt"
        );
        latestResultClassification = Objects.requireNonNull(
                latestResultClassification,
                "latestResultClassification"
        );
    }

    private static void requireReference(
            String value,
            String label
    ) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    label + " must not be blank"
            );
        }
    }
}
