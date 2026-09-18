package mainstreet.enquiry;

import grandrue.application.MerchantScope;
import grandrue.enquiry.EnquirySubmissionStore;
import mainstreet.surface.ExposureCandidateObservation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Internal owner candidate formation from current merchant-scoped E1 facts. Only identity leaves
 * this boundary. It is not a public query, representation or authorization decision.
 * Communication has no source in E1, so its registered family supplies no placeholder candidate.
 */
public final class EnquiryMerchantExposureCandidateSource {
    private final EnquirySubmissionStore submissions;

    public EnquiryMerchantExposureCandidateSource(EnquirySubmissionStore submissions) {
        this.submissions = Objects.requireNonNull(submissions, "submissions");
    }

    public List<ExposureCandidateObservation> currentCandidates(MerchantScope scope, String enquiryIdentity) {
        Objects.requireNonNull(scope, "scope");
        EnquirySubmission.requireIdentifier(enquiryIdentity, "enquiryIdentity");
        return submissions.submission(scope, enquiryIdentity).map(submission -> {
            if (!scope.equals(submission.merchantScope()) || !enquiryIdentity.equals(submission.enquiryIdentity())) {
                throw new IllegalStateException("Enquiry owner returned mismatched scope or identity");
            }
            List<ExposureCandidateObservation> candidates = new ArrayList<>();
            candidates.add(EnquiryMerchantExposureReferences.candidate(
                    EnquiryMerchantExposureReferences.SUBMISSION_CONTENT, enquiryIdentity));
            var contact = submission.contact();
            if (contact.name().isPresent() || contact.email().isPresent() || contact.telephone().isPresent()) {
                candidates.add(EnquiryMerchantExposureReferences.candidate(
                        EnquiryMerchantExposureReferences.SUBMITTED_CONTACT, enquiryIdentity));
            }
            if (submission.subjectRevision().isPresent()) {
                candidates.add(EnquiryMerchantExposureReferences.candidate(
                        EnquiryMerchantExposureReferences.SUBJECT_CONTEXT, enquiryIdentity));
            }
            return List.copyOf(candidates);
        }).orElseGet(List::of);
    }
}
