package mainstreet.surface;

import grandrue.api.ApiContractIdentity;
import grandrue.api.ApiOwnerContractReference;
import grandrue.api.ApiQueryContractDefinition;

import java.util.Objects;

/**
 * Immutable handoff from the registered public Merchant Presence query to the
 * exact S2 assembly selected from one bounded Projection read.
 *
 * <p>This is not a transport DTO and exposes no owner reread capability. Owner-
 * private fragment values remain owner-private until a later concrete safe
 * mapping boundary is authorised.</p>
 */
public record PublicMerchantPresenceBoundedQuery(
        ApiQueryContractDefinition definition,
        PublicCustomerProjectionAssembly assembly
) {
    private static final ApiContractIdentity QUERY_IDENTITY =
            new ApiContractIdentity("platform", "public-merchant-presence");
    private static final ApiOwnerContractReference PROJECTION_OWNER =
            new ApiOwnerContractReference("platform", "merchant-presence");

    public PublicMerchantPresenceBoundedQuery {
        Objects.requireNonNull(definition, "definition");
        Objects.requireNonNull(assembly, "assembly");
        if (!QUERY_IDENTITY.equals(definition.registration().identity())) {
            throw new IllegalArgumentException(
                    "Bounded Merchant Presence handoff requires the public-merchant-presence query"
            );
        }
        if (!PROJECTION_OWNER.equals(
                definition.registration().ownerContractReference())) {
            throw new IllegalArgumentException(
                    "Bounded Merchant Presence handoff requires the merchant-presence Projection Contract"
            );
        }
    }
}
