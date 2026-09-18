package mainstreet.merchantprofile;

import grandrue.merchantprofile.MerchantExternalPresenceExposure;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/** Exact intent for creating one independently identified external-presence link. */
public record CreateMerchantExternalPresenceLinkCommand(
        MerchantScope merchantScope,
        String presenceIdentity,
        String platformKind,
        String publicUrl,
        MerchantExternalPresenceExposure exposure,
        String logicalRequestIdentity,
        String provenanceReference,
        String actingPrincipalIdentity,
        Instant committedAt
) {
    public CreateMerchantExternalPresenceLinkCommand {
        Objects.requireNonNull(merchantScope, "merchantScope");
        CreateMerchantLocationCommand.require(
                presenceIdentity,
                "presenceIdentity"
        );
        CreateMerchantLocationCommand.require(platformKind, "platformKind");
        CreateMerchantLocationCommand.require(publicUrl, "publicUrl");
        Objects.requireNonNull(exposure, "exposure");
        CreateMerchantLocationCommand.require(
                logicalRequestIdentity,
                "logicalRequestIdentity"
        );
        CreateMerchantLocationCommand.require(
                provenanceReference,
                "provenanceReference"
        );
        CreateMerchantLocationCommand.require(
                actingPrincipalIdentity,
                "actingPrincipalIdentity"
        );
        Objects.requireNonNull(committedAt, "committedAt");
    }
}
