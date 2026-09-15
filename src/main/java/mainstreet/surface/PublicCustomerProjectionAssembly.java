package mainstreet.surface;

import java.util.List;
import java.util.Objects;

/**
 * Immutable final PUBLIC/CUSTOMER Surface assembly produced only after the
 * bounded-read P2/E4 selection spine has accepted its material.
 *
 * <p>The assembly deliberately retains Surface identities only: the exact
 * bounded-read binding and the already-selected Projection material. It does
 * not carry transport DTO identity, capability ownership, or executable
 * interaction membership.</p>
 */
public final class PublicCustomerProjectionAssembly {

    private final BoundedProjectionReadBinding boundedProjectionReadBinding;
    private final List<ProjectionMaterialFragment> selectedFragments;

    PublicCustomerProjectionAssembly(
            BoundedProjectionReadBinding boundedProjectionReadBinding,
            List<? extends ProjectionMaterialFragment> selectedFragments
    ) {
        this.boundedProjectionReadBinding = Objects.requireNonNull(
                boundedProjectionReadBinding,
                "boundedProjectionReadBinding"
        );
        this.selectedFragments = List.copyOf(
                Objects.requireNonNull(selectedFragments, "selectedFragments")
        );
    }

    public BoundedProjectionReadBinding boundedProjectionReadBinding() {
        return boundedProjectionReadBinding;
    }

    public List<ProjectionMaterialFragment> selectedFragments() {
        return selectedFragments;
    }
}
