package grandrue.merchantprofile;

import grandrue.merchantprofile.MerchantServiceAreaExposure;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/** Exact intent for creating one independently identified Service Area. */
public record CreateMerchantServiceAreaCommand(
        MerchantScope merchantScope,
        String serviceAreaIdentity,
        ServiceAreaGeographyV1 geography,
        String publicDescription,
        MerchantServiceAreaExposure exposure,
        String logicalRequestIdentity,
        String provenanceReference,
        String actingPrincipalIdentity,
        Instant committedAt
) {
    public CreateMerchantServiceAreaCommand {
        Objects.requireNonNull(merchantScope, "merchantScope");
        CreateMerchantLocationCommand.require(
                serviceAreaIdentity,
                "serviceAreaIdentity"
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
