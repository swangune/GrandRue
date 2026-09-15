package mainstreet.enquiry.delivery;

import com.fasterxml.jackson.annotation.JsonAnySetter;

/** The binding carries the known subject; contact values remain supplied, unauthenticated data. */
public record PublicOpportunityEnquiryRequest(String binding, String question, String name, String email, String telephone) {
    public PublicOpportunityEnquiryRequest {
        if (binding == null || binding.isBlank() || binding.length() > 32768)
            throw new IllegalArgumentException("Binding is required");
        new PublicGeneralEnquiryRequest(question, name, email, telephone);
    }
    @JsonAnySetter public void rejectUnknown(String name, Object value) {
        throw new IllegalArgumentException("Unknown submission field");
    }
}

