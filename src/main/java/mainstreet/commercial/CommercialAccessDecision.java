package mainstreet.commercial;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Reasoned current-entitlement decision preserving the commercial evidence that
 * permitted access at one evaluation instant.
 *
 * <p>This decision describes commercial entitlement only. Semantic
 * applicability, actor authority, trust, provider readiness and residual
 * commitment access remain separate authorities.</p>
 *
 * <p>Governed by MS-PROT-056 v1.5.</p>
 */
public record CommercialAccessDecision(
        CommercialEntitlementIdentity entitlementIdentity,
        boolean permitted,
        List<CommercialEntitlementGrantProvenance> effectiveGrantSources,
        Instant evaluatedAt
) {

    public CommercialAccessDecision {
        Objects.requireNonNull(entitlementIdentity, "entitlementIdentity");
        effectiveGrantSources = List.copyOf(Objects.requireNonNull(
                effectiveGrantSources,
                "effectiveGrantSources"
        ));
        Objects.requireNonNull(evaluatedAt, "evaluatedAt");
        if (permitted != !effectiveGrantSources.isEmpty()) {
            throw new IllegalArgumentException(
                    "Commercial entitlement permission must match effective grant evidence"
            );
        }
    }
}
