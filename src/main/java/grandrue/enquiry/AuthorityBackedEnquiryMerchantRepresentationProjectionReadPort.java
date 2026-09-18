package grandrue.enquiry;

import grandrue.application.MerchantScope;
import mainstreet.surface.ProjectionSourceAvailability;
import mainstreet.surface.ProjectionSourceCompleteness;
import mainstreet.surface.ProjectionSourceEvidence;
import mainstreet.surface.ProjectionSourceRevocationState;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * M2 material acquisition from one E1 submission read. Initial submission identity is stable
 * progress because E1 forbids rewriting its evidence. This port never reads Publication or
 * communication state and never substitutes current subject facts for submission-time provenance.
 */
public final class AuthorityBackedEnquiryMerchantRepresentationProjectionReadPort
        implements EnquiryMerchantRepresentationProjectionReadPort {
    private final EnquirySubmissionStore submissions;

    public AuthorityBackedEnquiryMerchantRepresentationProjectionReadPort(EnquirySubmissionStore submissions) {
        this.submissions = Objects.requireNonNull(submissions, "submissions");
    }

    @Override
    public EnquiryMerchantRepresentationProjectionObservation observe(
            MerchantScope scope, String enquiryIdentity, Instant observedAt) {
        Objects.requireNonNull(scope, "scope");
        EnquirySubmission.requireIdentifier(enquiryIdentity, "enquiryIdentity");
        Objects.requireNonNull(observedAt, "observedAt");
        var submission = submissions.submission(scope, enquiryIdentity);
        List<EnquiryMerchantRepresentationProjectionFragment> fragments = new ArrayList<>();
        submission.ifPresent(value -> {
            if (!scope.equals(value.merchantScope()) || !enquiryIdentity.equals(value.enquiryIdentity())) {
                throw new IllegalStateException("Enquiry material authority returned mismatched scope or identity");
            }
            fragments.add(new EnquiryMerchantRepresentationProjectionFragment(enquiryIdentity,
                    new EnquiryMerchantRepresentation.SubmissionContent(value.submittedAt(),
                            value.question(), value.semanticContext())));
            var contact = value.contact();
            if (contact.name().isPresent() || contact.email().isPresent() || contact.telephone().isPresent()) {
                fragments.add(new EnquiryMerchantRepresentationProjectionFragment(enquiryIdentity,
                        new EnquiryMerchantRepresentation.SubmittedContact(contact)));
            }
            value.subjectRevision().ifPresent(subject -> fragments.add(
                    new EnquiryMerchantRepresentationProjectionFragment(enquiryIdentity,
                            new EnquiryMerchantRepresentation.SubjectContext(subject))));
        });
        Optional<String> progress = submission.map(EnquirySubmission::enquiryIdentity);
        var evidence = new ProjectionSourceEvidence(
                EnquiryMerchantRepresentationProjectionReferences.SUBMISSION_SOURCE,
                "enquiry-submission:" + scope.merchantIdentifier().length() + ":"
                        + scope.merchantIdentifier() + enquiryIdentity,
                progress, progress, observedAt,
                submission.isPresent() ? ProjectionSourceAvailability.AVAILABLE : ProjectionSourceAvailability.UNAVAILABLE,
                submission.isPresent() ? ProjectionSourceCompleteness.COMPLETE : ProjectionSourceCompleteness.MISSING,
                ProjectionSourceRevocationState.NOT_APPLICABLE);
        return new EnquiryMerchantRepresentationProjectionObservation(scope, fragments, evidence);
    }
}
