package grandrue.enquiry.delivery;

import grandrue.api.*;
import java.util.Optional;
import java.util.Set;

/** Explicit anonymous/public general Enquiry use case; no stored-data observation contract. */
public final class PublicGeneralEnquiryContract {
    private static final ApiCommandContractDefinition DEFINITION = new ApiCommandContractDefinition(
            new ApiContractRegistration(new ApiContractIdentity("enquiry", "public-general-enquiry"),
                    ApiSurfaceClass.PUBLIC, ApiContractKind.COMMAND,
                    new ApiOwnerContractReference("enquiry", "send-enquiry"), "enquiry/public-general-enquiry-route"),
            Set.of("surface/public-visitor"), "enquiry/public-general-enquiry-input",
            Optional.of("enquiry/logical-submission-request"), Optional.empty(), "enquiry/atomic-submission",
            "enquiry/public-completion-acknowledgement", "enquiry/public-submission-problem",
            Optional.empty(), "enquiry/submitted-personal-contact");
    private PublicGeneralEnquiryContract() { }
    public static ApiCommandContractDefinition definition() { return DEFINITION; }
}
