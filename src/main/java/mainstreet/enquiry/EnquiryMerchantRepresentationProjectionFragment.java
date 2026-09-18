package mainstreet.enquiry;

import grandrue.enquiry.EnquiryMerchantRepresentationProjectionReferences;
import mainstreet.surface.ExposureCandidateObservation;
import mainstreet.surface.ProjectionMaterialFragment;
import mainstreet.surface.ProjectionMaterialSourceAffinity;

import java.util.Objects;
import java.util.Set;

/** One family of typed material from the immutable submission acquired by this bounded read. */
public final class EnquiryMerchantRepresentationProjectionFragment implements ProjectionMaterialFragment {
    private final ExposureCandidateObservation candidate;
    private final Set<ProjectionMaterialSourceAffinity> affinities;
    private final EnquiryMerchantRepresentation representation;

    EnquiryMerchantRepresentationProjectionFragment(String enquiryIdentity, EnquiryMerchantRepresentation representation) {
        this.representation = Objects.requireNonNull(representation, "representation");
        this.candidate = EnquiryMerchantExposureReferences.candidate(representation.element(), enquiryIdentity);
        this.affinities = Set.of(new ProjectionMaterialSourceAffinity(
                EnquiryMerchantRepresentationProjectionReferences.SUBMISSION_SOURCE, enquiryIdentity));
    }

    @Override public ExposureCandidateObservation candidateObservation() { return candidate; }
    @Override public Set<ProjectionMaterialSourceAffinity> sourceAffinities() { return affinities; }
    public EnquiryMerchantRepresentation representation() { return representation; }
}
