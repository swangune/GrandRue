package mainstreet.merchantprofile;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/** Exact retirement intent for one current active external-presence revision. */
public record RetireMerchantExternalPresenceLinkCommand(
        MerchantScope merchantScope,
        String presenceIdentity,
        String expectedCurrentRevisionIdentity,
        String logicalRequestIdentity,
        String provenanceReference,
        String actingPrincipalIdentity,
        Instant committedAt
) {
    public RetireMerchantExternalPresenceLinkCommand {
        Objects.requireNonNull(merchantScope, "merchantScope");
        CreateMerchantLocationCommand.require(
                presenceIdentity,
                "presenceIdentity"
        );
        CreateMerchantLocationCommand.require(
                expectedCurrentRevisionIdentity,
                "expectedCurrentRevisionIdentity"
        );
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
