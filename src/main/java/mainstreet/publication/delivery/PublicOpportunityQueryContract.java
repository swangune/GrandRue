package mainstreet.publication.delivery;

import mainstreet.api.*;

import java.util.Optional;
import java.util.Set;

/** Logical PUBLIC query registration; physical routing and current request execution are T1B. */
public final class PublicOpportunityQueryContract {
    private static final ApiQueryContractDefinition DEFINITION = new ApiQueryContractDefinition(
            new ApiContractRegistration(new ApiContractIdentity("publication", "public-opportunity-query"),
                    ApiSurfaceClass.PUBLIC, ApiContractKind.QUERY,
                    new ApiOwnerContractReference("publication", "public-opportunity-representation"),
                    "publication/public-opportunity-route"),
            "surface/public-audience-observation-context", Optional.empty(),
            "publication/public-opportunity-published-material-serviceability",
            Set.of("publication/public-opportunity-representation"),
            "publication/public-opportunity-no-filter-or-sort",
            "publication/public-opportunity-single-resource",
            "publication/public-opportunity-response",
            "publication/public-opportunity-absent-or-unserviceable");

    private PublicOpportunityQueryContract() { }

    public static ApiQueryContractDefinition definition() { return DEFINITION; }
}
