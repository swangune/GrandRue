package grandrue.fulfilment;

import mainstreet.application.MerchantScope;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Static resolved routing for one applicable fulfilment role/context.
 * Mutable provider health, credentials and connection state are deliberately
 * excluded. The exact applicable obligation set is retained as static
 * compatibility evidence.
 */
public record ResolvedFulfilmentBinding(
        MerchantScope merchantScope,
        FulfilmentRoleIdentity roleIdentity,
        Optional<String> semanticContextIdentifier,
        Set<String> requiredObligations,
        FulfillerKind fulfillerKind,
        String fulfillerIdentity,
        Optional<String> providerConnectionIdentity,
        String bindingProvenance
) {
    public ResolvedFulfilmentBinding {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(roleIdentity, "roleIdentity");
        semanticContextIdentifier = Objects.requireNonNull(
                semanticContextIdentifier,
                "semanticContextIdentifier"
        );
        requiredObligations = Set.copyOf(Objects.requireNonNull(
                requiredObligations,
                "requiredObligations"
        ));
        if (requiredObligations.isEmpty()) {
            throw new IllegalArgumentException(
                    "A resolved fulfilment binding requires at least one applicable obligation"
            );
        }
        requiredObligations.forEach(value ->
                requireIdentifier(value, "Required fulfilment obligation"));
        Objects.requireNonNull(fulfillerKind, "fulfillerKind");
        requireIdentifier(fulfillerIdentity, "Fulfiller identity");
        providerConnectionIdentity = Objects.requireNonNull(
                providerConnectionIdentity,
                "providerConnectionIdentity"
        );
        requireIdentifier(bindingProvenance, "Binding provenance");
        semanticContextIdentifier.ifPresent(value ->
                requireIdentifier(value, "Semantic context identifier"));
        providerConnectionIdentity.ifPresent(value ->
                requireIdentifier(value, "Provider connection identity"));
        if (fulfillerKind == FulfillerKind.INTERNAL
                && providerConnectionIdentity.isPresent()) {
            throw new IllegalArgumentException(
                    "Internal fulfilment must not identify a provider connection"
            );
        }
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
