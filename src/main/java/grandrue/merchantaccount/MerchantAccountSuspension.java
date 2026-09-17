package grandrue.merchantaccount;

import mainstreet.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * One independently effective Merchant Account Suspension with source and
 * release provenance. Releasing one fact never erases another suspension.
 */
public record MerchantAccountSuspension(
        String suspensionIdentity,
        MerchantScope merchantScope,
        String sourceAuthorityIdentifier,
        String reasonClassIdentifier,
        Instant establishedAt,
        String establishedByIdentifier,
        String releaseAuthorityIdentifier,
        String provenanceIdentifier,
        Optional<Instant> releasedAt,
        Optional<String> releasedByIdentifier,
        Optional<String> releaseEvidenceIdentifier
) {
    public MerchantAccountSuspension {
        requireIdentifier(suspensionIdentity, "Suspension identity");
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(sourceAuthorityIdentifier, "Source authority");
        requireIdentifier(reasonClassIdentifier, "Reason class");
        Objects.requireNonNull(establishedAt, "establishedAt");
        requireIdentifier(establishedByIdentifier, "Established-by identity");
        requireIdentifier(releaseAuthorityIdentifier, "Release authority");
        requireIdentifier(provenanceIdentifier, "Suspension provenance");
        releasedAt = Objects.requireNonNull(releasedAt, "releasedAt");
        releasedByIdentifier = Objects.requireNonNull(
                releasedByIdentifier,
                "releasedByIdentifier"
        );
        releaseEvidenceIdentifier = Objects.requireNonNull(
                releaseEvidenceIdentifier,
                "releaseEvidenceIdentifier"
        );
        boolean allReleaseFieldsPresent = releasedAt.isPresent()
                && releasedByIdentifier.isPresent()
                && releaseEvidenceIdentifier.isPresent();
        boolean noReleaseFieldsPresent = releasedAt.isEmpty()
                && releasedByIdentifier.isEmpty()
                && releaseEvidenceIdentifier.isEmpty();
        if (!allReleaseFieldsPresent && !noReleaseFieldsPresent) {
            throw new IllegalArgumentException(
                    "Suspension release evidence must be complete or absent"
            );
        }
        releasedAt.ifPresent(value -> {
            if (value.isBefore(establishedAt)) {
                throw new IllegalArgumentException(
                        "Suspension release cannot precede establishment"
                );
            }
        });
        releasedByIdentifier.ifPresent(value ->
                requireIdentifier(value, "Released-by identity"));
        releaseEvidenceIdentifier.ifPresent(value ->
                requireIdentifier(value, "Release evidence"));
    }

    public boolean isEffective() {
        return releasedAt.isEmpty();
    }

    public MerchantAccountSuspension release(
            Instant at,
            String releasedBy,
            String evidenceIdentifier
    ) {
        if (!isEffective()) {
            return this;
        }
        return new MerchantAccountSuspension(
                suspensionIdentity,
                merchantScope,
                sourceAuthorityIdentifier,
                reasonClassIdentifier,
                establishedAt,
                establishedByIdentifier,
                releaseAuthorityIdentifier,
                provenanceIdentifier,
                Optional.of(Objects.requireNonNull(at, "at")),
                Optional.of(requireAndReturn(releasedBy, "Released-by identity")),
                Optional.of(requireAndReturn(evidenceIdentifier, "Release evidence"))
        );
    }

    private static String requireAndReturn(String value, String label) {
        requireIdentifier(value, label);
        return value;
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
