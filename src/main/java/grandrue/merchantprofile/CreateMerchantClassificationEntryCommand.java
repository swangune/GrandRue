package grandrue.merchantprofile;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/** Exact merchant-approved intent to create one independent classification fact. */
public record CreateMerchantClassificationEntryCommand(
        MerchantScope merchantScope,
        String classificationIdentity,
        MerchantClassificationEntryV1 entry,
        String logicalRequestIdentity,
        String provenanceReference,
        String actingPrincipalIdentity,
        Instant committedAt
) {
    public CreateMerchantClassificationEntryCommand {
        Objects.requireNonNull(merchantScope, "merchantScope");
        CreateMerchantLocationCommand.require(
                classificationIdentity,
                "classificationIdentity"
        );
        Objects.requireNonNull(entry, "entry");
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
