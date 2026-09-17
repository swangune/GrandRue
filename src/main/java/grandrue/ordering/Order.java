package grandrue.ordering;

import mainstreet.application.MerchantScope;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Capability-owned durable purchase/order commitment. */
public record Order(
        MerchantScope merchantScope,
        String identifier,
        Optional<String> customerContextIdentifier,
        List<OrderCommitmentPortion> commitmentPortions,
        String governingReleaseIdentifier,
        Instant committedAt
) {
    public Order {
        Objects.requireNonNull(merchantScope);
        requireIdentifier(identifier, "Order identifier");
        customerContextIdentifier = Objects.requireNonNull(
                customerContextIdentifier
        );
        customerContextIdentifier.ifPresent(value ->
                requireIdentifier(value, "Customer context identifier")
        );
        commitmentPortions = List.copyOf(
                Objects.requireNonNull(commitmentPortions)
        );
        if (commitmentPortions.isEmpty()) {
            throw new IllegalArgumentException(
                    "Order requires at least one commitment portion"
            );
        }
        Set<String> portionIdentifiers = new HashSet<>();
        for (OrderCommitmentPortion portion : commitmentPortions) {
            if (!portionIdentifiers.add(portion.identifier())) {
                throw new IllegalArgumentException(
                        "Order contains duplicate commitment portion identifier: "
                                + portion.identifier()
                );
            }
        }
        requireIdentifier(
                governingReleaseIdentifier,
                "Governing release identifier"
        );
        Objects.requireNonNull(committedAt);
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
