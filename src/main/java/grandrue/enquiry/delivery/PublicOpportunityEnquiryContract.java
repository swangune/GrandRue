package grandrue.enquiry.delivery;

import grandrue.api.*;
import mainstreet.publication.delivery.PublicOpportunityQueryContract;
import java.util.*;

public final class PublicOpportunityEnquiryContract {
    private static final ApiCommandContractDefinition COMMAND = new ApiCommandContractDefinition(
            new ApiContractRegistration(new ApiContractIdentity("enquiry", "public-opportunity-enquiry"),
                    ApiSurfaceClass.PUBLIC, ApiContractKind.COMMAND,
                    new ApiOwnerContractReference("enquiry", "send-enquiry"),
                    PublicGeneralEnquiryContract.definition().registration().scopeEstablishmentRuleReference()),
            Set.of("surface/public-visitor"), "enquiry/public-opportunity-enquiry-input",
            Optional.of("enquiry/logical-submission-request"), Optional.empty(), "enquiry/atomic-submission",
            "enquiry/public-completion-acknowledgement", "enquiry/public-submission-problem",
            Optional.empty(), "enquiry/submitted-personal-contact");
    private static final ApiQueryContractDefinition QUERY = new ApiQueryContractDefinition(
            new ApiContractRegistration(new ApiContractIdentity("enquiry", "public-opportunity-binding"),
                    ApiSurfaceClass.PUBLIC, ApiContractKind.QUERY,
                    new ApiOwnerContractReference("enquiry", "send-enquiry"),
                    PublicOpportunityQueryContract.definition().registration().scopeEstablishmentRuleReference()),
            "surface/public-audience-observation-context", Optional.empty(),
            "publication/public-opportunity-published-material-serviceability",
            Set.of("publication/public-opportunity-representation"),
            "publication/public-opportunity-no-filter-or-sort", "publication/public-opportunity-single-resource",
            "enquiry/public-opportunity-binding-response", "enquiry/public-binding-absent-or-unserviceable");
    private PublicOpportunityEnquiryContract() { }
    public static ApiCommandContractDefinition command() { return COMMAND; }
    public static ApiQueryContractDefinition query() { return QUERY; }
}

