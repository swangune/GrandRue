package grandrue.enquiry;

import grandrue.application.MerchantScope;
import grandrue.surface.BoundedProjectionRead;
import grandrue.surface.EstablishedObservationRequest;
import grandrue.surface.ProjectionSourceEvidence;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/** Coherent owner observation: material and source evidence derive from one immutable E1 read. */
public final class EnquiryMerchantRepresentationProjectionObservation {
    private final MerchantScope scope;
    private final List<EnquiryMerchantRepresentationProjectionFragment> fragments;
    private final ProjectionSourceEvidence evidence;

    EnquiryMerchantRepresentationProjectionObservation(MerchantScope scope,
            List<EnquiryMerchantRepresentationProjectionFragment> fragments, ProjectionSourceEvidence evidence) {
        this.scope = Objects.requireNonNull(scope, "scope");
        this.fragments = List.copyOf(fragments);
        this.evidence = Objects.requireNonNull(evidence, "evidence");
        if (!evidence.sourceReference().equals(EnquiryMerchantRepresentationProjectionReferences.SUBMISSION_SOURCE)
                || (this.fragments.isEmpty() == evidence.isCurrent())) {
            throw new IllegalArgumentException("Enquiry material and source evidence disagree");
        }
        for (var fragment : this.fragments) {
            var progress = fragment.sourceAffinities().iterator().next().observedProgressIdentifier();
            if (!evidence.observedProgressIdentifier().filter(progress::equals).isPresent()
                    || !evidence.requiredCurrentProgressIdentifier().filter(progress::equals).isPresent()) {
                throw new IllegalArgumentException("Enquiry material and source progress disagree");
            }
        }
    }

    public BoundedProjectionRead toBoundedRead(EstablishedObservationRequest request) {
        var read = new BoundedProjectionRead(request,
                EnquiryMerchantRepresentationProjectionReferences.PROJECTION_CONTRACT,
                EnquiryMerchantRepresentationProjectionReferences.READ_USE, Set.of(evidence), fragments);
        if (!scope.equals(read.merchantScope())) {
            throw new IllegalStateException("Enquiry observation belongs to another Merchant Scope");
        }
        return read;
    }
}
