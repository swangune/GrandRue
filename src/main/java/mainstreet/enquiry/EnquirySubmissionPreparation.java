package mainstreet.enquiry;

/**
 * Internal application preparation invoked only for an uncommitted logical request, inside the
 * submission transaction. The implementation must perform authoritative revalidation and return
 * the same supplied intent with generated identity/time and resolved semantic evidence.
 * It must not append the Enquiry or perform external effects. This is not a client callback.
 */
@FunctionalInterface
public interface EnquirySubmissionPreparation {
    EnquirySubmission prepare(EnquirySubmissionIntent intent);
}
