package mainstreet.merchantprofile;

import grandrue.merchantprofile.PostalAddressEvidence;

import grandrue.merchantprofile.MerchantLocationLifecycle;

import grandrue.merchantprofile.AcceptedLocationCoordinates;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** One immutable exact revision of one merchant-scoped Location identity. */
public record MerchantLocationRevision(
        String revisionIdentity,
        MerchantScope merchantScope,
        String locationIdentity,
        long revisionNumber,
        Optional<String> predecessorRevisionIdentity,
        MerchantLocationLifecycle lifecycle,
        Optional<String> publicLabel,
        PostalAddressEvidence addressEvidence,
        Optional<AcceptedLocationCoordinates> acceptedCoordinates,
        String logicalRequestIdentity,
        String provenanceReference,
        String actingPrincipalIdentity,
        String controllerRelationshipIdentity,
        Instant committedAt
) {
    public MerchantLocationRevision {
        CreateMerchantLocationCommand.require(
                revisionIdentity,
                "revisionIdentity"
        );
        Objects.requireNonNull(merchantScope, "merchantScope");
        CreateMerchantLocationCommand.require(
                locationIdentity,
                "locationIdentity"
        );
        if (revisionNumber < 1) {
            throw new IllegalArgumentException(
                    "Merchant Location revision number must be positive"
            );
        }
        Objects.requireNonNull(
                predecessorRevisionIdentity,
                "predecessorRevisionIdentity"
        );
        if ((revisionNumber == 1) != predecessorRevisionIdentity.isEmpty()) {
            throw new IllegalArgumentException(
                    "Merchant Location predecessor does not match revision number"
            );
        }
        Objects.requireNonNull(lifecycle, "lifecycle");
        publicLabel = CreateMerchantLocationCommand.requireOptional(
                publicLabel,
                "publicLabel"
        );
        Objects.requireNonNull(addressEvidence, "addressEvidence");
        Objects.requireNonNull(acceptedCoordinates, "acceptedCoordinates");
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
