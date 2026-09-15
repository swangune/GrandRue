package mainstreet.merchantprofile;

import mainstreet.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** One immutable exact revision of one Merchant Classification Entry identity. */
public record MerchantClassificationEntryRevision(
        String revisionIdentity,
        MerchantScope merchantScope,
        String classificationIdentity,
        long revisionNumber,
        Optional<String> predecessorRevisionIdentity,
        MerchantClassificationLifecycle lifecycle,
        MerchantClassificationEntryV1 entry,
        String logicalRequestIdentity,
        String provenanceReference,
        String actingPrincipalIdentity,
        String controllerRelationshipIdentity,
        Instant committedAt
) {
    public MerchantClassificationEntryRevision {
        CreateMerchantLocationCommand.require(
                revisionIdentity,
                "revisionIdentity"
        );
        Objects.requireNonNull(merchantScope, "merchantScope");
        CreateMerchantLocationCommand.require(
                classificationIdentity,
                "classificationIdentity"
        );
        if (revisionNumber < 1) {
            throw new IllegalArgumentException(
                    "Classification Entry revision number must be positive"
            );
        }
        Objects.requireNonNull(
                predecessorRevisionIdentity,
                "predecessorRevisionIdentity"
        );
        if ((revisionNumber == 1) != predecessorRevisionIdentity.isEmpty()) {
            throw new IllegalArgumentException(
                    "Classification Entry predecessor does not match revision number"
            );
        }
        Objects.requireNonNull(lifecycle, "lifecycle");
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
        CreateMerchantLocationCommand.require(
                controllerRelationshipIdentity,
                "controllerRelationshipIdentity"
        );
        Objects.requireNonNull(committedAt, "committedAt");
    }
}
