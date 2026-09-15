package mainstreet.merchantprofile;

import mainstreet.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/** Exact retirement intent for one current active Service Area revision. */
public record RetireMerchantServiceAreaCommand(
        MerchantScope merchantScope,
        String serviceAreaIdentity,
        String expectedCurrentRevisionIdentity,
        String logicalRequestIdentity,
        String provenanceReference,
        String actingPrincipalIdentity,
        Instant committedAt
) {
    public RetireMerchantServiceAreaCommand {
        Objects.requireNonNull(merchantScope, "merchantScope");
        CreateMerchantLocationCommand.require(
                serviceAreaIdentity,
                "serviceAreaIdentity"
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
