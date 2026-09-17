package grandrue.merchantaccount;

import mainstreet.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/** Explicit source-authority request to establish one merchant-wide suspension. */
public record MerchantAccountSuspensionCommand(
        String logicalRequestIdentity,
        MerchantScope merchantScope,
        String suspensionIdentity,
        String sourceAuthorityIdentifier,
        String reasonClassIdentifier,
        Instant establishedAt,
        String establishedByIdentifier,
        String releaseAuthorityIdentifier,
        String provenanceIdentifier
) {
    public MerchantAccountSuspensionCommand {
        require(logicalRequestIdentity, "Logical request identity");
        Objects.requireNonNull(merchantScope, "merchantScope");
        require(suspensionIdentity, "Suspension identity");
        require(sourceAuthorityIdentifier, "Source authority");
        require(reasonClassIdentifier, "Reason class");
        Objects.requireNonNull(establishedAt, "establishedAt");
        require(establishedByIdentifier, "Established-by identity");
        require(releaseAuthorityIdentifier, "Release authority");
        require(provenanceIdentifier, "Provenance identity");
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
