package mainstreet.merchantprofile;

import grandrue.merchantprofile.MerchantLocationExposure;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** One immutable revision of merchant-owned Location public-observation choice. */
public record MerchantLocationExposureChoiceRevision(
        String revisionIdentity,
        MerchantScope merchantScope,
        String locationIdentity,
        long revisionNumber,
        Optional<String> predecessorRevisionIdentity,
        MerchantLocationExposure exposure,
        String logicalRequestIdentity,
        String provenanceReference,
        String actingPrincipalIdentity,
        String controllerRelationshipIdentity,
        Instant committedAt
) {
    public MerchantLocationExposureChoiceRevision {
        require(revisionIdentity, "revisionIdentity");
        Objects.requireNonNull(merchantScope, "merchantScope");
        require(locationIdentity, "locationIdentity");
        if (revisionNumber < 1) {
            throw new IllegalArgumentException(
                    "Location Exposure choice revision number must be positive"
            );
        }
        Objects.requireNonNull(
                predecessorRevisionIdentity,
                "predecessorRevisionIdentity"
        );
        if ((revisionNumber == 1) != predecessorRevisionIdentity.isEmpty()) {
            throw new IllegalArgumentException(
                    "Location Exposure choice predecessor does not match revision number"
            );
        }
        Objects.requireNonNull(exposure, "exposure");
        require(logicalRequestIdentity, "logicalRequestIdentity");
        require(provenanceReference, "provenanceReference");
        require(actingPrincipalIdentity, "actingPrincipalIdentity");
        require(controllerRelationshipIdentity, "controllerRelationshipIdentity");
        Objects.requireNonNull(committedAt, "committedAt");
    }

    private static void require(String value, String name) {
        Objects.requireNonNull(value, name);
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
    }
}
