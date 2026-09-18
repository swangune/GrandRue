package grandrue.enquiry.delivery;

import grandrue.api.*;
import grandrue.application.MerchantScope;
import java.util.Map;
import java.util.Objects;

/** Explicit server-owned registry for this command's public locator rule, with no ID fallback. */
public final class PublicEnquiryRouteScopeAuthority implements ApiTransportScopeEstablishmentAuthority {
    public record Route(String locator) implements ApiTransportScopeEvidence {
        public Route { Objects.requireNonNull(locator); }
    }
    private final Map<String, MerchantScope> routes;
    public PublicEnquiryRouteScopeAuthority(Map<String, MerchantScope> routes) {
        this.routes = Map.copyOf(routes);
        if (this.routes.keySet().stream().anyMatch(String::isBlank)) throw new IllegalArgumentException("Blank locator");
    }
    @Override public String ruleReference() {
        return PublicGeneralEnquiryContract.definition().registration().scopeEstablishmentRuleReference();
    }
    @Override public ApiSurfaceClass surface() { return ApiSurfaceClass.PUBLIC; }
    @Override public Class<? extends ApiTransportScopeEvidence> evidenceType() { return Route.class; }
    @Override public ApiTransportScope establish(ApiTransportScopeEvidence evidence) {
        if (!(evidence instanceof Route route)) throw new IllegalArgumentException("Route evidence required");
        var scope = routes.get(route.locator());
        if (scope == null) throw new IllegalArgumentException("Unregistered public locator");
        return new MerchantApiTransportScope(scope);
    }
}
