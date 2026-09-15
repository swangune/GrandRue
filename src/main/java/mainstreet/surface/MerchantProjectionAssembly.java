package mainstreet.surface;

import java.util.List;
import java.util.Objects;

/** Immutable selected MERCHANT material; only the trusted same-read selection boundary constructs it. */
public final class MerchantProjectionAssembly {
    private final BoundedProjectionReadBinding boundedProjectionReadBinding;
    private final List<ProjectionMaterialFragment> selectedFragments;

    MerchantProjectionAssembly(BoundedProjectionReadBinding binding, List<ProjectionMaterialFragment> fragments) {
        this.boundedProjectionReadBinding = Objects.requireNonNull(binding, "binding");
        this.selectedFragments = List.copyOf(fragments);
    }

    public BoundedProjectionReadBinding boundedProjectionReadBinding() { return boundedProjectionReadBinding; }
    public List<ProjectionMaterialFragment> selectedFragments() { return selectedFragments; }
}
