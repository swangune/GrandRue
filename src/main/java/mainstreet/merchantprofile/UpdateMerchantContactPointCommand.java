package mainstreet.merchantprofile;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** Exact replacement intent for one current active contact-point revision. */
public record UpdateMerchantContactPointCommand(
        MerchantContactPointScope scope,
        String contactPointIdentity,
        String expectedCurrentRevisionIdentity,
        MerchantContactPointKind kind,
        String value,
        MerchantContactPointExposure exposure,
        Optional<String> label,
        String logicalRequestIdentity,
        String provenanceReference,
        String actingPrincipalIdentity,
        Instant committedAt
) {
    public UpdateMerchantContactPointCommand {
        Objects.requireNonNull(scope, "scope");
        CreateMerchantLocationCommand.require(
                contactPointIdentity,
                "contactPointIdentity"
        );
        CreateMerchantLocationCommand.require(
                expectedCurrentRevisionIdentity,
                "expectedCurrentRevisionIdentity"
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
