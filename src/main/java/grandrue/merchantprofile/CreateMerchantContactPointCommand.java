package grandrue.merchantprofile;

import grandrue.merchantprofile.MerchantContactPointExposure;
import grandrue.merchantprofile.MerchantContactPointKind;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** Exact intent for creating one independently identified contact point. */
public record CreateMerchantContactPointCommand(
        MerchantContactPointScope scope,
        String contactPointIdentity,
        MerchantContactPointKind kind,
        String value,
        MerchantContactPointExposure exposure,
        Optional<String> label,
        String logicalRequestIdentity,
        String provenanceReference,
        String actingPrincipalIdentity,
        Instant committedAt
) {
    public CreateMerchantContactPointCommand {
        Objects.requireNonNull(scope, "scope");
        CreateMerchantLocationCommand.require(
                contactPointIdentity,
                "contactPointIdentity"
        );
        Objects.requireNonNull(kind, "kind");
        CreateMerchantLocationCommand.require(value, "value");
        Objects.requireNonNull(exposure, "exposure");
        label = CreateMerchantLocationCommand.requireOptional(label, "label");
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
