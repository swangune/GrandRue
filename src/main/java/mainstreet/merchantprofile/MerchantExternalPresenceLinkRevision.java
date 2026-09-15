package mainstreet.merchantprofile;

import mainstreet.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** One immutable exact revision of one Merchant External Presence identity. */
public record MerchantExternalPresenceLinkRevision(
        String revisionIdentity,
        MerchantScope merchantScope,
        String presenceIdentity,
        long revisionNumber,
        Optional<String> predecessorRevisionIdentity,
        MerchantExternalPresenceLifecycle lifecycle,
        String platformKind,
        String publicUrl,
        MerchantExternalPresenceExposure exposure,
        String logicalRequestIdentity,
        String provenanceReference,
        String actingPrincipalIdentity,
        String controllerRelationshipIdentity,
        Instant committedAt
) {
    public MerchantExternalPresenceLinkRevision {
        CreateMerchantLocationCommand.require(
                revisionIdentity,
                "revisionIdentity"
        );
        Objects.requireNonNull(merchantScope, "merchantScope");
        CreateMerchantLocationCommand.require(
                presenceIdentity,
                "presenceIdentity"
        );
        if (revisionNumber < 1) {
            throw new IllegalArgumentException(
                    "External Presence revision number must be positive"
            );
        }
        Objects.requireNonNull(
                predecessorRevisionIdentity,
                "predecessorRevisionIdentity"
        );
        if ((revisionNumber == 1) != predecessorRevisionIdentity.isEmpty()) {
            throw new IllegalArgumentException(
                    "External Presence predecessor does not match revision number"
            );
        }
        Objects.requireNonNull(lifecycle, "lifecycle");
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
        CreateMerchantLocationCommand.require(
                controllerRelationshipIdentity,
                "controllerRelationshipIdentity"
        );
        Objects.requireNonNull(committedAt, "committedAt");
    }
}
