package mainstreet.enquiry.delivery;

import com.fasterxml.jackson.annotation.JsonAnySetter;

/** Transport bounds only; Enquiry-owned requirements decide semantic completeness. */
public record PublicGeneralEnquiryRequest(String question, String name, String email, String telephone) {
    public PublicGeneralEnquiryRequest {
        if (question == null || question.isBlank() || question.length() > 10000) {
            throw new IllegalArgumentException("Question is absent or exceeds the transport bound");
        }
        for (String value : new String[] {name, email, telephone}) {
            if (value != null && value.length() > 500) throw new IllegalArgumentException("Contact exceeds transport bound");
        }
    }
    /** General submission must never silently discard a supplied subject or authority field. */
    @JsonAnySetter
    public void rejectUnknown(String name, Object value) {
        throw new IllegalArgumentException("Unrecognized general Enquiry input");
    }
}
