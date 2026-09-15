package mainstreet.merchantprofile;

import java.time.Instant;
import java.util.Objects;

/** Exact retirement intent for one current active contact-point revision. */
public record RetireMerchantContactPointCommand(
        MerchantContactPointScope scope,
        String contactPointIdentity,
        String expectedCurrentRevisionIdentity,
        String logicalRequestIdentity,
        String provenanceReference,
        String actingPrincipalIdentity,
        Instant committedAt
) {
    public RetireMerchantContactPointCommand {
        Objects.requireNonNull(scope, "scope");
        CreateMerchantLocationCommand.require(
                contactPointIdentity,
                "contactPointIdentity"
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
