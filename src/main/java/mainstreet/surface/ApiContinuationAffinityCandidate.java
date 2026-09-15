package mainstreet.surface;

import mainstreet.api.ApiContractIdentity;
import mainstreet.application.MerchantScope;

import java.util.Objects;
import java.util.Set;

/**
 * Untrusted decoded continuation-affinity candidate for T4b validation.
 *
 * <p>This is not a credential, Merchant Scope authority, resource authority,
 * token encoding or cursor position. A candidate becomes usable only after
 * validation against the current bounded-query contract and server-established
 * API Audience Observation Context.</p>
 */
public record ApiContinuationAffinityCandidate(
        ApiContractIdentity queryContractIdentity,
        int maximumPageSize,
        String stableOrderingBasisReference,
        Set<String> permittedFilterReferences,
        String continuationSemanticsReference,
        String scopeQueryAffinityRuleReference,
        MerchantScope merchantScope,
        SurfaceAudience audience,
        String semanticRegistryReleaseIdentifier
) {
    public ApiContinuationAffinityCandidate {
        Objects.requireNonNull(queryContractIdentity, "queryContractIdentity");
        if (maximumPageSize <= 0) {
            throw new IllegalArgumentException(
                    "Maximum page size must be positive"
            );
        }
        requireReference(
                stableOrderingBasisReference,
                "Stable ordering basis reference"
        );
        permittedFilterReferences = Set.copyOf(Objects.requireNonNull(
                permittedFilterReferences,
                "permittedFilterReferences"
        ));
        permittedFilterReferences.forEach(reference ->
                requireReference(reference, "Permitted filter reference"));
        requireReference(
                continuationSemanticsReference,
                "Continuation semantics reference"
        );
        requireReference(
                scopeQueryAffinityRuleReference,
                "Scope/query affinity rule reference"
        );
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(audience, "audience");
        requireReference(
                semanticRegistryReleaseIdentifier,
                "Semantic Registry Release identifier"
        );
    }

    private static void requireReference(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
