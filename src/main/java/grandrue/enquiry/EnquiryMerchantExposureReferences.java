package grandrue.enquiry;

import grandrue.surface.ExposableElementReference;
import grandrue.surface.ExposureCandidateInstanceKindReference;
import grandrue.surface.ExposureCandidateInstanceReference;
import grandrue.surface.ExposureCandidateObservation;
import grandrue.surface.ExposureRequirementReference;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Exact MERCHANT Enquiry Exposure families accepted by MS-PROT-043 v1.4 §24. */
public final class EnquiryMerchantExposureReferences {
    public static final ExposableElementReference SUBMISSION_CONTENT =
            new ExposableElementReference("enquiry", "merchant-submission-content");
    public static final ExposableElementReference SUBMITTED_CONTACT =
            new ExposableElementReference("enquiry", "merchant-submitted-contact");
    public static final ExposableElementReference SUBJECT_CONTEXT =
            new ExposableElementReference("enquiry", "merchant-subject-context");
    public static final ExposableElementReference COMMUNICATION_SUMMARY =
            new ExposableElementReference("enquiry", "merchant-communication-summary");
    public static final ExposureCandidateInstanceKindReference ENQUIRY_INSTANCE_KIND =
            new ExposureCandidateInstanceKindReference("enquiry", "enquiry");
    public static final ExposureRequirementReference OBSERVATION_REQUIREMENT =
            new ExposureRequirementReference("enquiry", "merchant-enquiry-observation");

    private EnquiryMerchantExposureReferences() { }

    public static Set<ExposableElementReference> elements() {
        return Set.of(SUBMISSION_CONTENT, SUBMITTED_CONTACT, SUBJECT_CONTEXT, COMMUNICATION_SUMMARY);
    }

    public static ExposureCandidateObservation candidate(ExposableElementReference element, String enquiryIdentity) {
        if (!elements().contains(Objects.requireNonNull(element, "element"))) {
            throw new IllegalArgumentException("Unknown MERCHANT Enquiry Exposure family");
        }
        EnquirySubmission.requireIdentifier(enquiryIdentity, "enquiryIdentity");
        return new ExposureCandidateObservation(element, Optional.of(new ExposureCandidateInstanceReference(
                "enquiry", "enquiry", enquiryIdentity)));
    }

    static String requireIdentity(ExposureCandidateObservation candidate) {
        Objects.requireNonNull(candidate, "candidate");
        if (!elements().contains(candidate.elementReference())) {
            throw new IllegalArgumentException("Unknown MERCHANT Enquiry Exposure family");
        }
        var instance = candidate.instanceReference().orElseThrow(
                () -> new IllegalArgumentException("Enquiry Exposure requires an exact instance"));
        if (!instance.ownerIdentifier().equals("enquiry") || !instance.instanceKindIdentifier().equals("enquiry")) {
            throw new IllegalArgumentException("Enquiry Exposure requires the Enquiry instance kind");
        }
        return instance.instanceIdentifier();
    }
}
