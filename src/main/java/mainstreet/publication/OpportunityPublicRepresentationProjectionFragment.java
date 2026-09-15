package mainstreet.publication;

import mainstreet.surface.ExposureCandidateObservation;
import mainstreet.surface.ProjectionMaterialFragment;
import mainstreet.surface.ProjectionMaterialSourceAffinity;

import java.util.Objects;
import java.util.Set;

/** Typed Publication-owned P5 fragment for one stable Opportunity member identity. */
public final class OpportunityPublicRepresentationProjectionFragment
        implements ProjectionMaterialFragment {

    private final ExposureCandidateObservation candidateObservation;
    private final Set<ProjectionMaterialSourceAffinity> sourceAffinities;
    private final OpportunityPublicRepresentation representation;

    OpportunityPublicRepresentationProjectionFragment(
            ExposureCandidateObservation candidateObservation,
            Set<ProjectionMaterialSourceAffinity> sourceAffinities,
            OpportunityPublicRepresentation representation
    ) {
        this.candidateObservation = Objects.requireNonNull(
                candidateObservation,
                "candidateObservation"
        );
        OpportunityPublicExposureReferences.requireOpportunityIdentity(candidateObservation);
        this.sourceAffinities = Set.copyOf(
                Objects.requireNonNull(sourceAffinities, "sourceAffinities")
        );
        if (this.sourceAffinities.size() != 1
                || !OpportunityPublicRepresentationProjectionReferences.PUBLISHED_MATERIAL_SOURCE
                .equals(this.sourceAffinities.iterator().next().sourceReference())) {
            throw new IllegalArgumentException(
                    "Opportunity public representation requires one published-material affinity"
            );
        }
        this.representation = Objects.requireNonNull(representation, "representation");
    }

    @Override
    public ExposureCandidateObservation candidateObservation() {
        return candidateObservation;
    }

    @Override
    public Set<ProjectionMaterialSourceAffinity> sourceAffinities() {
        return sourceAffinities;
    }

    public OpportunityPublicRepresentation representation() {
        return representation;
    }
}
