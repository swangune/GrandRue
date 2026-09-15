package mainstreet.merchantaccount;

import mainstreet.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/** Request from the authority permitted to release one specific suspension. */
public record MerchantAccountSuspensionReleaseCommand(
        String logicalRequestIdentity,
        MerchantScope merchantScope,
        String suspensionIdentity,
        String releasingAuthorityIdentifier,
        Instant releasedAt,
        String releasedByIdentifier,
        String releaseEvidenceIdentifier
) {
    public MerchantAccountSuspensionReleaseCommand {
        require(logicalRequestIdentity, "Logical request identity");
        Objects.requireNonNull(merchantScope, "merchantScope");
        require(suspensionIdentity, "Suspension identity");
        require(releasingAuthorityIdentifier, "Releasing authority");
        Objects.requireNonNull(releasedAt, "releasedAt");
        require(releasedByIdentifier, "Released-by identity");
        require(releaseEvidenceIdentifier, "Release evidence");
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
