package mainstreet.merchantprofile;

import grandrue.merchantprofile.MerchantServiceAreaExposure;

import grandrue.merchantprofile.MerchantServiceAreaLifecycle;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** One immutable exact revision of one Merchant Service Area identity. */
public record MerchantServiceAreaRevision(
        String revisionIdentity,
        MerchantScope merchantScope,
        String serviceAreaIdentity,
        long revisionNumber,
        Optional<String> predecessorRevisionIdentity,
        MerchantServiceAreaLifecycle lifecycle,
        ServiceAreaGeographyV1 geography,
        String publicDescription,
        MerchantServiceAreaExposure exposure,
        String logicalRequestIdentity,
        String provenanceReference,
        String actingPrincipalIdentity,
        String controllerRelationshipIdentity,
        Instant committedAt
) {
    public MerchantServiceAreaRevision {
        CreateMerchantLocationCommand.require(
                revisionIdentity,
                "revisionIdentity"
        );
        Objects.requireNonNull(merchantScope, "merchantScope");
        CreateMerchantLocationCommand.require(
                serviceAreaIdentity,
                "serviceAreaIdentity"
        );
        if (revisionNumber < 1) {
            throw new IllegalArgumentException(
                    "Service Area revision number must be positive"
            );
        }
        Objects.requireNonNull(
                predecessorRevisionIdentity,
                "predecessorRevisionIdentity"
        );
        if ((revisionNumber == 1) != predecessorRevisionIdentity.isEmpty()) {
            throw new IllegalArgumentException(
                    "Service Area predecessor does not match revision number"
            );
        }
        Objects.requireNonNull(lifecycle, "lifecycle");
        Objects.requireNonNull(geography, "geography");
        publicDescription = ServiceAreaGeographyV1.normalizeText(
                publicDescription,
                "publicDescription"
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
        CreateMerchantLocationCommand.require(
                controllerRelationshipIdentity,
                "controllerRelationshipIdentity"
        );
        Objects.requireNonNull(committedAt, "committedAt");
    }
}
