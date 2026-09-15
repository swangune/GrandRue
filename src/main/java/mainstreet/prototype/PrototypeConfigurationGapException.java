package mainstreet.prototype;

/**
 * Prototype-only signal that onboarding evidence cannot be mapped to already
 * supported semantics and therefore must not be invented during discovery.
 */
public final class PrototypeConfigurationGapException
        extends IllegalArgumentException {

    public PrototypeConfigurationGapException(String message) {
        super(message);
    }
}
