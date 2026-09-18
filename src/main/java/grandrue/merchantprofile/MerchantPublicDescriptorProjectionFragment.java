package grandrue.merchantprofile;

import mainstreet.surface.ExposureCandidateObservation;
import mainstreet.surface.ProjectionMaterialFragment;
import mainstreet.surface.ProjectionMaterialSourceAffinity;

import java.util.Objects;
import java.util.Set;

/**
 * One typed Merchant Public Descriptor value retained inside a bounded
 * Projection read.
 *
 * <p>Business-value access remains on this Profile-owned type. Generic
 * Projection/Surface infrastructure sees only the candidate identity and
 * source-progress affinity declared by {@link ProjectionMaterialFragment}.</p>
 */
public final class MerchantPublicDescriptorProjectionFragment
        implements ProjectionMaterialFragment {

    private final ExposureCandidateObservation candidateObservation;
    private final Set<ProjectionMaterialSourceAffinity> sourceAffinities;
    private final String value;

    MerchantPublicDescriptorProjectionFragment(
            ExposureCandidateObservation candidateObservation,
            Set<ProjectionMaterialSourceAffinity> sourceAffinities,
            String value
    ) {
        this.candidateObservation = Objects.requireNonNull(
                candidateObservation,
                "candidateObservation"
        );
        if (candidateObservation.instanceReference().isPresent()) {
            throw new IllegalArgumentException(
                    "Merchant Public Descriptor projection fragments are singleton candidates"
            );
        }
        this.sourceAffinities = Set.copyOf(
                Objects.requireNonNull(sourceAffinities, "sourceAffinities")
        );
        if (this.sourceAffinities.isEmpty()
                || this.sourceAffinities.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException(
                    "Descriptor projection fragment requires source affinity"
            );
        }
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Descriptor projection value must not be blank"
            );
        }
        this.value = value;
    }

    @Override
    public ExposureCandidateObservation candidateObservation() {
        return candidateObservation;
    }

    @Override
    public Set<ProjectionMaterialSourceAffinity> sourceAffinities() {
        return sourceAffinities;
    }

    public String value() {
        return value;
    }
}
