package grandrue.enquiry;

import grandrue.application.ApplicationRequestIdentity;

/**
 * Internal transaction/retry boundary. Reconciles committed intent before fresh preparation.
 * A successful new submission atomically records both the Enquiry and logical request result.
 * Callers remain responsible for trusted merchant/access context, including access to retries.
 * This contract is not a public endpoint or an implementation of current subject revalidation.
 */
public interface EnquirySubmissionApplicationService {
    EnquirySubmission submit(
            ApplicationRequestIdentity requestIdentity,
            EnquirySubmissionIntent intent,
            EnquirySubmissionPreparation preparation
    );
}
