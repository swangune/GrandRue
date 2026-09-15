package mainstreet.merchantprofile;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** One immutable exact revision of one Merchant Contact Point identity. */
public record MerchantContactPointRevision(
        String revisionIdentity,
        MerchantContactPointScope scope,
        String contactPointIdentity,
        long revisionNumber,
        Optional<String> predecessorRevisionIdentity,
        MerchantContactPointLifecycle lifecycle,
        MerchantContactPointKind kind,
        String value,
        MerchantContactPointExposure exposure,
        Optional<String> label,
        Optional<String> merchantLocationRevisionIdentity,
        String logicalRequestIdentity,
        String provenanceReference,
        String actingPrincipalIdentity,
        String controllerRelationshipIdentity,
        Instant committedAt
) {
    public MerchantContactPointRevision {
        CreateMerchantLocationCommand.require(
                revisionIdentity,
                "revisionIdentity"
        );
        Objects.requireNonNull(scope, "scope");
        CreateMerchantLocationCommand.require(
                contactPointIdentity,
                "contactPointIdentity"
        );
        if (revisionNumber < 1) {
            throw new IllegalArgumentException(
                    "Merchant Contact Point revision number must be positive"
            );
        }
        Objects.requireNonNull(
                predecessorRevisionIdentity,
                "predecessorRevisionIdentity"
        );
        if ((revisionNumber == 1) != predecessorRevisionIdentity.isEmpty()) {
            throw new IllegalArgumentException(
                    "Merchant Contact Point predecessor does not match revision number"
            );
        }
        Objects.requireNonNull(lifecycle, "lifecycle");
        Objects.requireNonNull(kind, "kind");
        CreateMerchantLocationCommand.require(value, "value");
        Objects.requireNonNull(exposure, "exposure");
        label = CreateMerchantLocationCommand.requireOptional(label, "label");
        merchantLocationRevisionIdentity = Objects.requireNonNull(
                merchantLocationRevisionIdentity,
                "merchantLocationRevisionIdentity"
        );
        merchantLocationRevisionIdentity.ifPresent(valueIdentity ->
                CreateMerchantLocationCommand.require(
                        valueIdentity,
                        "merchantLocationRevisionIdentity"
                )
        );
        if ((scope.kind() == MerchantContactPointScopeKind.MERCHANT_LOCATION)
                != merchantLocationRevisionIdentity.isPresent()) {
            throw new IllegalArgumentException(
                    "Location-scoped contact revisions require exact Location provenance"
            );
        }
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
