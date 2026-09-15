package mainstreet.enquiry.delivery;

import mainstreet.application.MerchantScope;

/**
 * Required current permission/protection decision for this registered public operation, before
 * every E2 attempt including retries. It grants no stored-Enquiry observation or customer access.
 */
@FunctionalInterface
public interface PublicEnquirySubmissionAdmissionAuthority {
    boolean admits(MerchantScope scope);
}
