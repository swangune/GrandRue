package mainstreet.enquiry;

import mainstreet.surface.ExposableElementReference;
import java.time.Instant;
import java.util.Objects;

/**
 * Separate immutable material per accepted MERCHANT Exposure family. A fragment never holds the
 * whole EnquirySubmission, so a permitted content/subject fragment cannot reveal withheld contact.
 * These are internal projection values, not transport DTOs or current subject representations.
 */
public sealed interface EnquiryMerchantRepresentation {
    ExposableElementReference element();

    record SubmissionContent(Instant submittedAt, String question, EnquirySemanticContext submissionContext)
            implements EnquiryMerchantRepresentation {
        public SubmissionContent {
            Objects.requireNonNull(submittedAt, "submittedAt");
            EnquirySubmission.requireIdentifier(question, "question");
            Objects.requireNonNull(submissionContext, "submissionContext");
        }
        @Override public ExposableElementReference element() {
            return EnquiryMerchantExposureReferences.SUBMISSION_CONTENT;
        }
    }

    record SubmittedContact(EnquirySubmittedContact submittedContact) implements EnquiryMerchantRepresentation {
        public SubmittedContact { Objects.requireNonNull(submittedContact, "submittedContact"); }
        @Override public ExposableElementReference element() {
            return EnquiryMerchantExposureReferences.SUBMITTED_CONTACT;
        }
    }

    record SubjectContext(EnquiryRevisionProvenance submissionTimeSubject) implements EnquiryMerchantRepresentation {
        public SubjectContext { Objects.requireNonNull(submissionTimeSubject, "submissionTimeSubject"); }
        @Override public ExposableElementReference element() {
            return EnquiryMerchantExposureReferences.SUBJECT_CONTEXT;
        }
    }
}
