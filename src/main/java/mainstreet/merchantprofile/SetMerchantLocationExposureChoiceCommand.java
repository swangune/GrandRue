package mainstreet.merchantprofile;

import mainstreet.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/** Intent to establish or replace one Merchant Location public-observation choice. */
public record SetMerchantLocationExposureChoiceCommand(
        MerchantScope merchantScope,
        String locationIdentity,
        ExpectedMerchantLocationExposureChoice expectedCurrentChoice,
        MerchantLocationExposure exposure,
        String logicalRequestIdentity,
        String provenanceReference,
        String actingPrincipalIdentity,
        Instant committedAt
) {
    public SetMerchantLocationExposureChoiceCommand {
        Objects.requireNonNull(merchantScope, "merchantScope");
        require(locationIdentity, "locationIdentity");
        Objects.requireNonNull(expectedCurrentChoice, "expectedCurrentChoice");
        Objects.requireNonNull(exposure, "exposure");
        require(logicalRequestIdentity, "logicalRequestIdentity");
        require(provenanceReference, "provenanceReference");
        require(actingPrincipalIdentity, "actingPrincipalIdentity");
        Objects.requireNonNull(committedAt, "committedAt");
    }

    private static void require(String value, String name) {
        Objects.requireNonNull(value, name);
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
    }
}
