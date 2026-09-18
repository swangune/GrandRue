package grandrue.enquiry;

import grandrue.application.MerchantScope;

import java.util.Optional;

/**
 * Internal append-only persistence boundary, not a public submission or merchant observation API.
 * Identity conflicts never replace evidence. Logical-request retry reconciliation belongs to the
 * application transaction boundary; identical content under distinct identities remains distinct.
 */
public interface EnquirySubmissionStore {
    void append(EnquirySubmission submission);
    Optional<EnquirySubmission> submission(MerchantScope merchantScope, String enquiryIdentity);
}
