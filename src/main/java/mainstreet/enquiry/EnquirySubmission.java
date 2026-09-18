package mainstreet.enquiry;

import grandrue.enquiry.EnquirySubmittedContact;
import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Enquiry-owned immutable initial submission fact under composite MS-PROT-043.
 * The application caller must resolve scope, applicable requirements, current interaction/subject
 * authority and any trusted CustomerContext association before append. Construction is not
 * revalidation. No merchant-attention, communication or commitment lifecycle is stored here.
 */
public record EnquirySubmission(
        MerchantScope merchantScope,
        String enquiryIdentity,
        Instant submittedAt,
        String question,
        EnquirySubmittedContact contact,
        EnquirySemanticContext semanticContext,
        Optional<EnquiryRevisionProvenance> subjectRevision,
        Optional<String> customerContextIdentity
) {
    public EnquirySubmission {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(enquiryIdentity, "enquiryIdentity");
        Objects.requireNonNull(submittedAt, "submittedAt");
        requireIdentifier(question, "question");
        Objects.requireNonNull(contact, "contact");
        Objects.requireNonNull(semanticContext, "semanticContext");
        Objects.requireNonNull(subjectRevision, "subjectRevision");
        Objects.requireNonNull(customerContextIdentity, "customerContextIdentity")
                .ifPresent(value -> requireIdentifier(value, "customerContextIdentity"));
    }

    static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
