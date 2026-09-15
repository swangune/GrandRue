package mainstreet.enquiry.delivery;

import mainstreet.api.*;
import java.util.*;

public final class MerchantEnquiryQueryContract {
    private static final ApiQueryContractDefinition DEFINITION = new ApiQueryContractDefinition(
            new ApiContractRegistration(new ApiContractIdentity("enquiry", "merchant-enquiry-query"),
                    ApiSurfaceClass.MERCHANT_OPERATIONAL, ApiContractKind.QUERY,
                    new ApiOwnerContractReference("enquiry", "merchant-enquiry-representation"),
                    "enquiry/merchant-workspace-route"),
            "surface/merchant-interactive-observation-context", Optional.empty(),
            "enquiry/merchant-submission-serviceability", Set.of("enquiry/merchant-enquiry-representation"),
            "enquiry/merchant-enquiry-no-filter-or-sort", "enquiry/merchant-enquiry-single-resource",
            "enquiry/merchant-enquiry-response", "enquiry/merchant-enquiry-absent-or-unserviceable");
    private MerchantEnquiryQueryContract() { }
    public static ApiQueryContractDefinition definition() { return DEFINITION; }
}

