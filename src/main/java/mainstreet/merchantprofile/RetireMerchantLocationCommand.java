package mainstreet.merchantprofile;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/** Exact terminal-retirement intent for one active Merchant Location. */
public record RetireMerchantLocationCommand(
        MerchantScope merchantScope,
        String locationIdentity,
        String expectedCurrentRevisionIdentity,
        String logicalRequestIdentity,
        String provenanceReference,
        String actingPrincipalIdentity,
        Instant committedAt
) {
    public RetireMerchantLocationCommand {
        Objects.requireNonNull(merchantScope, "merchantScope");
        CreateMerchantLocationCommand.require(
                locationIdentity,
                "locationIdentity"
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
