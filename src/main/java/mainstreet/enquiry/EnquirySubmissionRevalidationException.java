package mainstreet.enquiry;

/** Safe internal rejection category; no subject existence or submitted contact details escape. */
public final class EnquirySubmissionRevalidationException extends RuntimeException {
    public EnquirySubmissionRevalidationException() {
        super("Enquiry submission context is no longer eligible");
    }
}
