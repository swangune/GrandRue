package grandrue.application;

/**
 * Explicit merchant authority and ownership boundary propagated by an
 * application use case. Delivery or background-work adapters remain
 * responsible for resolving and authorising this scope from trusted context.
 */
public record MerchantScope(String merchantIdentifier) {

    public MerchantScope {
        if (merchantIdentifier == null || merchantIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Merchant identifier must not be blank"
            );
        }
    }
}
