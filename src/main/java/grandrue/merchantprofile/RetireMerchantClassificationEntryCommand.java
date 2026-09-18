package grandrue.merchantprofile;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/** Exact retirement intent for one current active classification identity. */
public record RetireMerchantClassificationEntryCommand(
        MerchantScope merchantScope,
        String classificationIdentity,
        String expectedCurrentRevisionIdentity,
        String logicalRequestIdentity,
        String provenanceReference,
        String actingPrincipalIdentity,
        Instant committedAt
) {
    public RetireMerchantClassificationEntryCommand {
        Objects.requireNonNull(merchantScope, "merchantScope");
        CreateMerchantLocationCommand.require(
                classificationIdentity,
                "classificationIdentity"
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
