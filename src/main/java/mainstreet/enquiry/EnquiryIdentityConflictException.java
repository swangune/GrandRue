package mainstreet.enquiry;

/** An Enquiry identity is already occupied in this merchant scope. */
public final class EnquiryIdentityConflictException extends RuntimeException {
    public EnquiryIdentityConflictException() {
        super("Enquiry identity already exists in merchant scope");
    }
}
