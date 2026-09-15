package mainstreet.api;

/**
 * Explicit platform-owned execution scope. It prevents bootstrap,
 * administration and applicable integration work from fabricating a merchant.
 */
public record PlatformApiTransportScope(String contextReference)
        implements ApiTransportScope {

    public PlatformApiTransportScope {
        if (contextReference == null || contextReference.isBlank()) {
            throw new IllegalArgumentException(
                    "Platform context reference must not be blank"
            );
        }
    }
}
