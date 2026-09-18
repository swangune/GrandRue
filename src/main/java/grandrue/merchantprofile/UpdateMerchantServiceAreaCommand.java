package grandrue.merchantprofile;

import grandrue.merchantprofile.MerchantServiceAreaExposure;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/** Exact replacement intent for one current active Service Area revision. */
public record UpdateMerchantServiceAreaCommand(
        MerchantScope merchantScope,
        String serviceAreaIdentity,
        String expectedCurrentRevisionIdentity,
        ServiceAreaGeographyV1 geography,
        String publicDescription,
        MerchantServiceAreaExposure exposure,
        String logicalRequestIdentity,
        String provenanceReference,
        String actingPrincipalIdentity,
        Instant committedAt
) {
    public UpdateMerchantServiceAreaCommand {
        Objects.requireNonNull(merchantScope, "merchantScope");
        CreateMerchantLocationCommand.require(
                serviceAreaIdentity,
                "serviceAreaIdentity"
        );
        CreateMerchantLocationCommand.require(
                expectedCurrentRevisionIdentity,
                "expectedCurrentRevisionIdentity"
        );
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
        Objects.requireNonNull(committedAt, "committedAt");
    }
}
