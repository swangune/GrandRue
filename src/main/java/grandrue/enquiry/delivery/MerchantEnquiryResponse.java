package grandrue.enquiry.delivery;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Optional;

/** Only individually selected families; subject evidence is explicitly submission-time context. */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public record MerchantEnquiryResponse(String enquiryIdentity, Optional<Submission> submission,
        Optional<Contact> contact, Optional<SubjectContext> subjectContext) {
    public record Submission(String submittedAt, String question) { }
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public record Contact(Optional<String> name, Optional<String> email, Optional<String> telephone) { }
    public record SubjectContext(String ownerIdentifier, String typeIdentifier,
            String subjectIdentity, String submissionRevisionIdentity) { }
}

