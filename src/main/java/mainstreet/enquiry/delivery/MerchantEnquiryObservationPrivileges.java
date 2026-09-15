package mainstreet.enquiry.delivery;

import mainstreet.enquiry.EnquiryMerchantExposureReferences;
import mainstreet.semantic.Privilege;
import mainstreet.surface.ExposableElementReference;
import java.util.Map;

/** Explicit owner mapping of the four accepted MERCHANT families; no default read privilege. */
public record MerchantEnquiryObservationPrivileges(Map<ExposableElementReference, Privilege> byElement) {
    public MerchantEnquiryObservationPrivileges {
        byElement = Map.copyOf(byElement);
        if (!byElement.keySet().equals(EnquiryMerchantExposureReferences.elements()))
            throw new IllegalArgumentException("Every merchant Enquiry family requires a privilege");
    }
}

