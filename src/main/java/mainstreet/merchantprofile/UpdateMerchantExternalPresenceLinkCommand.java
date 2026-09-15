package mainstreet.merchantprofile;

import mainstreet.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/** Exact replacement intent for one current active external-presence revision. */
public record UpdateMerchantExternalPresenceLinkCommand(
        MerchantScope merchantScope,
        String presenceIdentity,
        String expectedCurrentRevisionIdentity,
        String platformKind,
        String publicUrl,
        MerchantExternalPresenceExposure exposure,
        String logicalRequestIdentity,
        String provenanceReference,
        String actingPrincipalIdentity,
        Instant committedAt
) {
    public UpdateMerchantExternalPresenceLinkCommand {
        Objects.requireNonNull(merchantScope, "merchantScope");
        CreateMerchantLocationCommand.require(
                presenceIdentity,
                "presenceIdentity"
        );
        CreateMerchantLocationCommand.require(
                expectedCurrentRevisionIdentity,
                "expectedCurrentRevisionIdentity"
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
