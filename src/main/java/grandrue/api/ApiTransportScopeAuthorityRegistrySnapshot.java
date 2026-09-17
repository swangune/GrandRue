package grandrue.api;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable exact-rule registry for API transport scope establishment.
 * Registration selects an authority by its governed rule reference; the
 * registry additionally enforces surface and evidence affinity and fails closed
 * on absent, ambiguous or invalid results.
 */
public final class ApiTransportScopeAuthorityRegistrySnapshot {

    private final Map<String, ApiTransportScopeEstablishmentAuthority> byRule;

    public ApiTransportScopeAuthorityRegistrySnapshot(
            List<ApiTransportScopeEstablishmentAuthority> authorities
    ) {
        Objects.requireNonNull(authorities, "authorities");
        Map<String, ApiTransportScopeEstablishmentAuthority> indexed =
                new LinkedHashMap<>();
        for (ApiTransportScopeEstablishmentAuthority authority : authorities) {
            Objects.requireNonNull(authority, "authority");
            String ruleReference = requireRuleReference(authority.ruleReference());
            Objects.requireNonNull(authority.surface(), "authority.surface");
            Objects.requireNonNull(authority.evidenceType(), "authority.evidenceType");
            if (indexed.putIfAbsent(ruleReference, authority) != null) {
                throw new IllegalArgumentException(
                        "Duplicate API transport scope rule: " + ruleReference
                );
            }
        }
        byRule = Map.copyOf(indexed);
    }

    public ApiTransportScope establish(
            ApiContractRegistration registration,
            ApiTransportScopeEvidence evidence
    ) {
        Objects.requireNonNull(registration, "registration");
        Objects.requireNonNull(evidence, "evidence");

        String ruleReference = registration.scopeEstablishmentRuleReference();
        ApiTransportScopeEstablishmentAuthority authority = byRule.get(ruleReference);
        if (authority == null) {
            throw new IllegalStateException(
                    "No API transport scope authority for rule " + ruleReference
            );
        }
        if (authority.surface() != registration.surface()) {
            throw new IllegalStateException(
                    "API transport scope authority surface does not match registration"
            );
        }
        if (!authority.evidenceType().isInstance(evidence)) {
            throw new IllegalStateException(
                    "Transport evidence does not match scope authority"
            );
        }

        ApiTransportScope established = authority.establish(evidence);
        if (established == null) {
            throw new IllegalStateException(
                    "API transport scope authority did not establish scope"
            );
        }
        requireSurfaceScopeAffinity(registration.surface(), established);
        return established;
    }

    private static void requireSurfaceScopeAffinity(
            ApiSurfaceClass surface,
            ApiTransportScope established
    ) {
        switch (surface) {
            case PUBLIC, CUSTOMER_CONTEXTUAL, MERCHANT_OPERATIONAL -> {
                if (!(established instanceof MerchantApiTransportScope)) {
                    throw new IllegalStateException(
                            surface + " requires established Merchant Scope"
                    );
                }
            }
            case PLATFORM_IDENTITY_BOOTSTRAP, PLATFORM_ADMINISTRATIVE -> {
                if (!(established instanceof PlatformApiTransportScope)) {
                    throw new IllegalStateException(
                            surface + " requires explicit Platform Scope"
                    );
                }
            }
            case INTEGRATION_INGRESS -> {
                // A registered integration may resolve merchant or platform/process scope.
            }
        }
    }

    private static String requireRuleReference(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Scope-establishment rule reference must not be blank"
            );
        }
        return value;
    }
}
