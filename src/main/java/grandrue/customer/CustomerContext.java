package grandrue.customer;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/**
 * Stable merchant-scoped operational representation of a customer
 * relationship. It does not imply a platform account.
 */
public record CustomerContext(
        MerchantScope merchantScope,
        String identifier,
        Instant establishedAt
) {

    public CustomerContext {
        Objects.requireNonNull(merchantScope);
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Customer context identifier must not be blank"
            );
        }
        Objects.requireNonNull(establishedAt);
    }
}
