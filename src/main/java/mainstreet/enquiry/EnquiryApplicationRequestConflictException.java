package mainstreet.enquiry;

/** Reuse of a logical request identity with different supplied intent. */
public final class EnquiryApplicationRequestConflictException extends RuntimeException {
    public EnquiryApplicationRequestConflictException() {
        super("Enquiry application request identity is already bound to different intent");
    }
}
