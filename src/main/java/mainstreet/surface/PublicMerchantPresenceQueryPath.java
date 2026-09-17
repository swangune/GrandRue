package mainstreet.surface;

import grandrue.api.ApiContractIdentity;
import grandrue.api.ApiContractKind;
import grandrue.api.ApiContractRegistration;
import grandrue.api.ApiOwnerContractReference;
import grandrue.api.ApiQueryContractDefinition;
import grandrue.api.ApiSurfaceClass;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * First concrete production bounded query path required by MS-PROT-027 v1.13.
 *
 * <p>The path binds the accepted PUBLIC Merchant Presence query contract to the
 * exact immutable S2 assembly. It performs no owner read, P2 evaluation,
 * Exposure evaluation, value interpretation, transport mapping or continuation
 * handling.</p>
 */
public final class PublicMerchantPresenceQueryPath {

    private static final ApiQueryContractDefinition DEFINITION =
            new ApiQueryContractDefinition(
                    new ApiContractRegistration(
                            new ApiContractIdentity(
                                    "platform",
                                    "public-merchant-presence"
                            ),
                            ApiSurfaceClass.PUBLIC,
                            ApiContractKind.QUERY,
                            new ApiOwnerContractReference(
                                    "platform",
                                    "merchant-presence"
                            ),
                            "surface/established-observation-request-merchant-scope"
                    ),
                    "surface/public-audience-observation-context",
                    Optional.empty(),
                    "platform/merchant-presence-truthful-serviceability",
                    Set.of("exposure/current-observation-restrictions"),
                    "platform/public-merchant-presence-no-client-filter-or-sort",
                    "platform/public-merchant-presence-single-bounded-read",
                    "surface/public-customer-projection-assembly",
                    "platform/merchant-presence-reduced-or-unserviceable"
            );

    public static ApiQueryContractDefinition definition() {
        return DEFINITION;
    }

    public PublicMerchantPresenceBoundedQuery bind(
            PublicCustomerProjectionAssembly assembly
    ) {
        return new PublicMerchantPresenceBoundedQuery(
                DEFINITION,
                Objects.requireNonNull(assembly, "assembly")
        );
    }
}
