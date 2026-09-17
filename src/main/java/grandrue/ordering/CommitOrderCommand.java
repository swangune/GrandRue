package grandrue.ordering;

import mainstreet.application.MerchantScope;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Intent to establish one purchase/order commitment. */
public record CommitOrderCommand(
        MerchantScope merchantScope,
        String identifier,
        String orderIdentifier,
        Optional<String> customerContextIdentifier,
        List<RequestedOrderPortion> requestedPortions
) {
    public CommitOrderCommand {
        Objects.requireNonNull(merchantScope);
        requireIdentifier(identifier, "Order command identifier");
        requireIdentifier(orderIdentifier, "Order identifier");
        customerContextIdentifier = Objects.requireNonNull(
                customerContextIdentifier
        );
        customerContextIdentifier.ifPresent(value ->
                requireIdentifier(value, "Customer context identifier")
        );
        requestedPortions = List.copyOf(
                Objects.requireNonNull(requestedPortions)
        );
        if (requestedPortions.isEmpty()) {
            throw new IllegalArgumentException(
                    "CommitOrder requires at least one requested portion"
            );
        }
        Set<String> identifiers = new HashSet<>();
        for (RequestedOrderPortion portion : requestedPortions) {
            if (!identifiers.add(portion.identifier())) {
                throw new IllegalArgumentException(
                        "CommitOrder contains duplicate portion identifier: "
                                + portion.identifier()
                );
            }
        }
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
