package mainstreet.fulfilment;

import mainstreet.application.MerchantScope;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Immutable merchant-scoped revision of stable Provider Fulfilment routing.
 * It has no independent activation authority; Merchant Configuration pins the
 * exact revision that governs one Resolved Configuration Package.
 */
public record FulfilmentBindingSetRevision(
        String bindingSetIdentifier,
        long revision,
        MerchantScope merchantScope,
        String semanticRegistryReleaseIdentifier,
        Set<FulfilmentBindingSelection> bindings
) {
    public FulfilmentBindingSetRevision {
        if (bindingSetIdentifier == null || bindingSetIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Fulfilment binding-set identifier must not be blank"
            );
        }
        if (revision < 1) {
            throw new IllegalArgumentException(
                    "Fulfilment binding-set revision must be positive"
            );
        }
        Objects.requireNonNull(merchantScope, "merchantScope");
        if (semanticRegistryReleaseIdentifier == null
                || semanticRegistryReleaseIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Semantic registry release identifier must not be blank"
            );
        }
        bindings = Set.copyOf(Objects.requireNonNull(bindings, "bindings"));
        requireUniqueBindingScope(bindings);
    }

    public FulfilmentBindingSetRevisionReference reference() {
        return new FulfilmentBindingSetRevisionReference(
                bindingSetIdentifier,
                revision
        );
    }

    private static void requireUniqueBindingScope(
            Set<FulfilmentBindingSelection> bindings
    ) {
        Set<BindingKey> keys = new HashSet<>();
        for (FulfilmentBindingSelection binding : bindings) {
            Objects.requireNonNull(binding, "fulfilment binding selection");
            BindingKey key = new BindingKey(
                    binding.roleIdentity(),
                    binding.semanticContextIdentifier().orElse(null)
            );
            if (!keys.add(key)) {
                throw new IllegalArgumentException(
                        "Fulfilment binding set selects one role/context more than once"
                );
            }
        }
    }

    private record BindingKey(
            FulfilmentRoleIdentity roleIdentity,
            String semanticContextIdentifier
    ) {
    }
}
