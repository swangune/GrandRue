package grandrue.publication;

import grandrue.application.MerchantScope;
import mainstreet.surface.BoundedProjectionRead;
import mainstreet.surface.EstablishedObservationRequest;
import mainstreet.surface.ProjectionMaterialFragment;
import mainstreet.surface.ProjectionMaterialSourceAffinity;
import mainstreet.surface.ProjectionSourceEvidence;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Publication-owned coherent P5 observation. It binds one exact public Opportunity fragment to
 * the P2 source evidence from the same published-revision read.
 */
public final class OpportunityPublicRepresentationProjectionObservation {

    private final MerchantScope merchantScope;
    private final Optional<OpportunityPublicRepresentationProjectionFragment> fragment;
    private final ProjectionSourceEvidence sourceEvidence;

    OpportunityPublicRepresentationProjectionObservation(
            MerchantScope merchantScope,
            Optional<OpportunityPublicRepresentationProjectionFragment> fragment,
            ProjectionSourceEvidence sourceEvidence
    ) {
        this.merchantScope = Objects.requireNonNull(merchantScope, "merchantScope");
        this.fragment = Objects.requireNonNull(fragment, "fragment");
        this.sourceEvidence = Objects.requireNonNull(sourceEvidence, "sourceEvidence");

        if (!OpportunityPublicRepresentationProjectionReferences.PUBLISHED_MATERIAL_SOURCE.equals(
                sourceEvidence.sourceReference()
        )) {
            throw new IllegalArgumentException(
                    "Opportunity representation evidence must belong to published material source"
            );
        }

        if (fragment.isPresent()) {
            ProjectionMaterialSourceAffinity affinity = fragment.orElseThrow()
                    .sourceAffinities()
                    .iterator()
                    .next();
            Optional<String> progress = Optional.of(affinity.observedProgressIdentifier());
            if (!sourceEvidence.isCurrent()
                    || !progress.equals(sourceEvidence.observedProgressIdentifier())
                    || !progress.equals(sourceEvidence.requiredCurrentProgressIdentifier())) {
                throw new IllegalArgumentException(
                        "Opportunity representation and P2 evidence must share exact published progress"
                );
            }
        } else if (sourceEvidence.isCurrent()) {
            throw new IllegalArgumentException(
                    "Current published-material evidence requires representation fragment"
            );
        }
    }

    public MerchantScope merchantScope() {
        return merchantScope;
    }

    public Optional<OpportunityPublicRepresentationProjectionFragment> fragment() {
        return fragment;
    }

    public ProjectionSourceEvidence sourceEvidence() {
        return sourceEvidence;
    }

    public BoundedProjectionRead toBoundedRead(EstablishedObservationRequest request) {
        Objects.requireNonNull(request, "request");
        List<ProjectionMaterialFragment> fragments = fragment
                .<List<ProjectionMaterialFragment>>map(List::of)
                .orElseGet(List::of);
        BoundedProjectionRead read = new BoundedProjectionRead(
                request,
                OpportunityPublicRepresentationProjectionReferences.PROJECTION_CONTRACT,
                OpportunityPublicRepresentationProjectionReferences.READ_USE,
                Set.of(sourceEvidence),
                fragments
        );
        if (!merchantScope.equals(read.merchantScope())) {
            throw new IllegalStateException(
                    "Opportunity representation observation belongs to another Merchant Scope"
            );
        }
        return read;
    }
}
