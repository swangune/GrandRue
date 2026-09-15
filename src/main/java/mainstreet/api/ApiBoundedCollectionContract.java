package mainstreet.api;

import java.util.Objects;
import java.util.Set;

/**
 * Static bounded-retrieval contract for one potentially growing API query.
 *
 * <p>This definition closes the permitted filter vocabulary and records the
 * stable-order, continuation and affinity rules. It does not encode, issue or
 * trust a continuation token.</p>
 */
public record ApiBoundedCollectionContract(
        ApiContractIdentity queryContractIdentity,
        int maximumPageSize,
        String stableOrderingBasisReference,
        Set<String> permittedFilterReferences,
        String continuationSemanticsReference,
        String scopeQueryAffinityRuleReference
) {
    public ApiBoundedCollectionContract {
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
    }

    private static void requireReference(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
