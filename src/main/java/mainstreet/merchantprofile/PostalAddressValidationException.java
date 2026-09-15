package mainstreet.merchantprofile;

/** Structural PostalAddressV1 validation rejection. */
public final class PostalAddressValidationException
        extends IllegalArgumentException {
    public PostalAddressValidationException(String message) {
        super(message);
    }
}
