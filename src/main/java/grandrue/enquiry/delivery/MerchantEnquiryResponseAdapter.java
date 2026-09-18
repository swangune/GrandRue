package grandrue.enquiry.delivery;

import grandrue.api.ApiSurfaceClass;
import grandrue.enquiry.EnquiryMerchantRepresentationProjectionReferences;
import grandrue.enquiry.EnquiryMerchantExposureReferences;
import grandrue.enquiry.EnquiryMerchantRepresentation;
import grandrue.enquiry.EnquiryMerchantRepresentationProjectionFragment;
import mainstreet.surface.*;
import java.util.*;

/** Maps only exact M2 fragments selected by P2/E4. No owner reads or fresh authorization occur here. */
public final class MerchantEnquiryResponseAdapter {
    public Optional<MerchantEnquiryResponse> map(String identity, BoundedProjectionRead read,
            ProjectionServiceabilityResult serviceability, ApiExposureResolution exposure) {
        Objects.requireNonNull(identity);
        if (exposure.surface() != ApiSurfaceClass.MERCHANT_OPERATIONAL
                || !MerchantEnquiryQueryContract.definition().registration().identity().equals(exposure.contractIdentity())
                || !EnquiryMerchantRepresentationProjectionReferences.PROJECTION_CONTRACT.equals(read.contractIdentity())
                || !EnquiryMerchantRepresentationProjectionReferences.READ_USE.equals(read.readUseIdentity()))
            throw new IllegalArgumentException("Merchant Enquiry query affinity mismatch");
        var families = new HashSet<ExposableElementReference>();
        for (var fragment : read.fragments()) {
            if (!(fragment instanceof EnquiryMerchantRepresentationProjectionFragment enquiry)
                    || !EnquiryMerchantExposureReferences.candidate(enquiry.representation().element(), identity)
                            .equals(fragment.candidateObservation())
                    || !enquiry.representation().element().equals(fragment.candidateObservation().elementReference())
                    || !families.add(enquiry.representation().element()))
                throw new IllegalArgumentException("Merchant Enquiry query requires exact unique resource families");
        }
        var selected = new MerchantProjectionAssemblyService().assemble(read, serviceability, exposure);
        if (selected.selectedFragments().isEmpty()) return Optional.empty();
        Optional<MerchantEnquiryResponse.Submission> content = Optional.empty();
        Optional<MerchantEnquiryResponse.Contact> contact = Optional.empty();
        Optional<MerchantEnquiryResponse.SubjectContext> subject = Optional.empty();
        for (var fragment : selected.selectedFragments()) {
            switch (((EnquiryMerchantRepresentationProjectionFragment) fragment).representation()) {
                case EnquiryMerchantRepresentation.SubmissionContent value ->
                    content = Optional.of(new MerchantEnquiryResponse.Submission(value.submittedAt().toString(), value.question()));
                case EnquiryMerchantRepresentation.SubmittedContact value ->
                    contact = Optional.of(new MerchantEnquiryResponse.Contact(value.submittedContact().name(),
                            value.submittedContact().email(), value.submittedContact().telephone()));
                case EnquiryMerchantRepresentation.SubjectContext value -> {
                    var provenance = value.submissionTimeSubject();
                    subject = Optional.of(new MerchantEnquiryResponse.SubjectContext(provenance.subjectType().ownerCapabilityIdentifier(),
                            provenance.subjectType().objectIdentifier(), provenance.subjectIdentity(), provenance.revisionIdentity()));
                }
            }
        }
        return Optional.of(new MerchantEnquiryResponse(identity, content, contact, subject));
    }
}
