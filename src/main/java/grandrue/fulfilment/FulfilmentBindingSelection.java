package grandrue.fulfilment;

import java.util.Objects;
import java.util.Optional;

/**
 * Stable merchant routing choice for one registered Fulfilment Role.
 *
 * <p>This value contains no credentials, provider health, retry state or other
 * mutable ProviderConnection state.</p>
 */
public record FulfilmentBindingSelection(
        FulfilmentRoleIdentity roleIdentity,
        Optional<String> semanticContextIdentifier,
        FulfillerKind fulfillerKind,
        String fulfillerIdentity,
        Optional<String> providerConnectionIdentity
) {
    public FulfilmentBindingSelection {
        Objects.requireNonNull(roleIdentity, "roleIdentity");
        semanticContextIdentifier = Objects.requireNonNull(
                semanticContextIdentifier,
                "semanticContextIdentifier"
        );
        Objects.requireNonNull(fulfillerKind, "fulfillerKind");
        requireIdentifier(fulfillerIdentity, "Fulfiller identity");
        providerConnectionIdentity = Objects.requireNonNull(
                providerConnectionIdentity,
                "providerConnectionIdentity"
        );
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
