package mainstreet.enquiry.delivery;

import grandrue.api.*;
import grandrue.application.MerchantScope;
import java.util.*;

/** Server-owned workspace locator mapping; neither the locator nor the session grants merchant authority. */
public final class MerchantEnquiryRouteScopeAuthority implements ApiTransportScopeEstablishmentAuthority {
    public record Route(String locator) implements ApiTransportScopeEvidence {
        public Route { Objects.requireNonNull(locator); }
    }
    private final Map<String, MerchantScope> routes;
    public MerchantEnquiryRouteScopeAuthority(Map<String, MerchantScope> routes) {
        this.routes = Map.copyOf(routes);
        if (this.routes.keySet().stream().anyMatch(String::isBlank)) throw new IllegalArgumentException("Blank locator");
    }
    @Override public String ruleReference() {
        return MerchantEnquiryQueryContract.definition().registration().scopeEstablishmentRuleReference();
    }
    @Override public ApiSurfaceClass surface() { return ApiSurfaceClass.MERCHANT_OPERATIONAL; }
    @Override public Class<? extends ApiTransportScopeEvidence> evidenceType() { return Route.class; }
    @Override public ApiTransportScope establish(ApiTransportScopeEvidence evidence) {
        if (!(evidence instanceof Route route)) throw new IllegalArgumentException("Workspace route required");
        var scope = routes.get(route.locator());
        if (scope == null) throw new IllegalArgumentException("Unregistered workspace locator");
        return new MerchantApiTransportScope(scope);
    }
}

