package mainstreet.merchantprofile;

import mainstreet.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/** Exact update intent; kind is deliberately absent because it is immutable. */
public record UpdateMerchantClassificationEntryCommand(
        MerchantScope merchantScope,
        String classificationIdentity,
        String expectedCurrentRevisionIdentity,
        String merchantApprovedLabel,
        MerchantClassificationExposure exposure,
        String logicalRequestIdentity,
        String provenanceReference,
        String actingPrincipalIdentity,
        Instant committedAt
) {
    public UpdateMerchantClassificationEntryCommand {
        Objects.requireNonNull(merchantScope, "merchantScope");
        CreateMerchantLocationCommand.require(
                classificationIdentity,
                "classificationIdentity"
        );
        CreateMerchantLocationCommand.require(
                expectedCurrentRevisionIdentity,
                "expectedCurrentRevisionIdentity"
        );
        merchantApprovedLabel = MerchantClassificationEntryV1.normalizeLabel(
                merchantApprovedLabel
        );
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
