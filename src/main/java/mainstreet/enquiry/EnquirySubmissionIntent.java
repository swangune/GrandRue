package mainstreet.enquiry;

import mainstreet.application.MerchantScope;

import java.util.Objects;
import java.util.Optional;

/**
 * Stable supplied intent for a logical submission, excluding generated identity/time and
 * resolved semantic context. Subject revision evidence identifies the original selected context;
 * it grants no authority. Trusted application callers must resolve customer association separately.
 */
public record EnquirySubmissionIntent(
        MerchantScope merchantScope,
        String question,
        EnquirySubmittedContact contact,
        Optional<EnquiryRevisionProvenance> subjectRevision,
        Optional<String> customerContextIdentity
) {
    public EnquirySubmissionIntent {
        Objects.requireNonNull(merchantScope, "merchantScope");
        EnquirySubmission.requireIdentifier(question, "question");
        Objects.requireNonNull(contact, "contact");
        Objects.requireNonNull(subjectRevision, "subjectRevision");
        Objects.requireNonNull(customerContextIdentity, "customerContextIdentity")
                .ifPresent(value -> EnquirySubmission.requireIdentifier(value, "customerContextIdentity"));
    }

    public static EnquirySubmissionIntent from(EnquirySubmission submission) {
        Objects.requireNonNull(submission, "submission");
        return new EnquirySubmissionIntent(submission.merchantScope(), submission.question(),
                submission.contact(), submission.subjectRevision(), submission.customerContextIdentity());
    }
}
