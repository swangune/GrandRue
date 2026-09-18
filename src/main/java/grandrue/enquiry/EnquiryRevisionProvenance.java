package grandrue.enquiry;

import mainstreet.semantic.registry.OwnedOperationalObjectTypeReference;

import java.util.Objects;

/**
 * Immutable evidence for an Enquiry SUBJECT relationship within the submission's merchant scope.
 * This value is not an Operational Object, a subject snapshot, or authority to use the subject.
 * It supports revision-backed subjects; owners without reconstructible revisions need their own
 * bounded evidence implementation before they can use this persistence path.
 */
public record EnquiryRevisionProvenance(
        OwnedOperationalObjectTypeReference subjectType,
        String subjectIdentity,
        String revisionIdentity
) {
    public EnquiryRevisionProvenance {
        Objects.requireNonNull(subjectType, "subjectType");
        EnquirySubmission.requireIdentifier(subjectIdentity, "subjectIdentity");
        EnquirySubmission.requireIdentifier(revisionIdentity, "revisionIdentity");
    }
}
