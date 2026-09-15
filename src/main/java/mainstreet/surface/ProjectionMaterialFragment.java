package mainstreet.surface;

import java.util.Set;

/**
 * Typed owner-supplied material retained inside one bounded Projection read.
 *
 * <p>The generic spine may inspect only candidate identity and source-progress
 * affinity. Owner-private value access remains on the concrete fragment type.</p>
 */
public interface ProjectionMaterialFragment {

    ExposureCandidateObservation candidateObservation();

    Set<ProjectionMaterialSourceAffinity> sourceAffinities();
}
