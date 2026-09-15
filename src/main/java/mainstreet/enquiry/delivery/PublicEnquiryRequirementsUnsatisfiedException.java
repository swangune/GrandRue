package mainstreet.enquiry.delivery;

/** Known owner requirement rejection before Enquiry creation; contains no submitted data. */
public final class PublicEnquiryRequirementsUnsatisfiedException extends RuntimeException {
    public PublicEnquiryRequirementsUnsatisfiedException() { super("Enquiry requirements are unsatisfied"); }
}
