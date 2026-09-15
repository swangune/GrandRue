package mainstreet.publication.delivery;

import mainstreet.api.*;
import mainstreet.application.MerchantScope;
import java.util.Map;
import java.util.Objects;

/** Explicit server-configured locator registry. An unregistered locator never becomes a Merchant ID. */
public final class PublicOpportunityRouteScopeAuthority implements ApiTransportScopeEstablishmentAuthority {
    public record Route(String locator) implements ApiTransportScopeEvidence {
        public Route { Objects.requireNonNull(locator, "locator"); }
    }
    private final Map<String, MerchantScope> routes;
    public PublicOpportunityRouteScopeAuthority(Map<String, MerchantScope> routes) {
        this.routes = Map.copyOf(routes);
        if (this.routes.keySet().stream().anyMatch(String::isBlank)) {
            throw new IllegalArgumentException("Public route locator must not be blank");
        }
    }
    @Override public String ruleReference() {
        return PublicOpportunityQueryContract.definition().registration().scopeEstablishmentRuleReference();
    }
    @Override public ApiSurfaceClass surface() { return ApiSurfaceClass.PUBLIC; }
    @Override public Class<? extends ApiTransportScopeEvidence> evidenceType() { return Route.class; }
    @Override public ApiTransportScope establish(ApiTransportScopeEvidence evidence) {
        if (!(evidence instanceof Route route)) throw new IllegalArgumentException("Public route evidence required");
        var scope = routes.get(route.locator());
        if (scope == null) throw new IllegalArgumentException("Unregistered public locator");
        return new MerchantApiTransportScope(scope);
    }
}
