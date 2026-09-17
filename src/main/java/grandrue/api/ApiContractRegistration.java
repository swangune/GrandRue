package grandrue.api;

import java.util.Objects;

/**
 * Common registration spine for one production API contract.
 *
 * <p>This declaration identifies the logical contract, its transport/trust
 * surface, kind, owning accepted contract and trusted scope-establishment
 * rule. It intentionally contains no URI, HTTP method, DTO, controller or
 * executable dispatch semantics.</p>
 */
public record ApiContractRegistration(
        ApiContractIdentity identity,
        ApiSurfaceClass surface,
        ApiContractKind kind,
        ApiOwnerContractReference ownerContractReference,
        String scopeEstablishmentRuleReference
) {
    public ApiContractRegistration {
        Objects.requireNonNull(identity, "identity");
        Objects.requireNonNull(surface, "surface");
        Objects.requireNonNull(kind, "kind");
        Objects.requireNonNull(ownerContractReference, "ownerContractReference");
        if (scopeEstablishmentRuleReference == null
                || scopeEstablishmentRuleReference.isBlank()) {
            throw new IllegalArgumentException(
                    "Scope-establishment rule reference must not be blank"
            );
        }
    }
}
