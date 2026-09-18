package grandrue.enquiry;

import grandrue.surface.ProjectionContractIdentity;
import grandrue.surface.ProjectionReadUseIdentity;
import grandrue.surface.ProjectionSourceDependencyReference;

/** Owner-qualified references for one request-scoped initial merchant Enquiry projection. */
public final class EnquiryMerchantRepresentationProjectionReferences {
    public static final ProjectionContractIdentity PROJECTION_CONTRACT =
            new ProjectionContractIdentity("enquiry", "merchant-enquiry-representation");
    public static final ProjectionReadUseIdentity READ_USE =
            new ProjectionReadUseIdentity("enquiry", "merchant-enquiry-representation");
    public static final ProjectionSourceDependencyReference SUBMISSION_SOURCE =
            new ProjectionSourceDependencyReference("enquiry", "immutable-submission-material");
    private EnquiryMerchantRepresentationProjectionReferences() { }
}
