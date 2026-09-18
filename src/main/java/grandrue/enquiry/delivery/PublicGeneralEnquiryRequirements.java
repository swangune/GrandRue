package grandrue.enquiry.delivery;

import mainstreet.enquiry.EnquirySubmissionPreparation;

/**
 * Required Enquiry-owned preparation for this bounded general public input. Must resolve applicable
 * requirements and generate identity without appending or external effects. No default schema or
 * permissive implementation is supplied. Throw the typed unsatisfied exception for known rejection.
 */
@FunctionalInterface
public interface PublicGeneralEnquiryRequirements extends EnquirySubmissionPreparation { }
