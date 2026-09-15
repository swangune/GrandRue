package mainstreet.surface;

import java.util.List;
import java.util.Objects;

/**
 * Trusted generic Surface composition boundary for the final PUBLIC/CUSTOMER
 * projection seam.
 *
 * <p>The service delegates selection to the already-established BR6 selector
 * and assembles only the resulting immutable material from the same bounded
 * read. It does not re-run P2 or E4, query owner state, or perform transport
 * mapping.</p>
 */
public final class PublicCustomerProjectionAssemblyService {

    public PublicCustomerProjectionAssembly assemble(
            BoundedProjectionRead boundedRead,
            ProjectionServiceabilityResult serviceability,
            ApiExposureResolution exposureResolution
    ) {
        Objects.requireNonNull(boundedRead, "boundedRead");
        Objects.requireNonNull(serviceability, "serviceability");
        Objects.requireNonNull(exposureResolution, "exposureResolution");

        List<ProjectionMaterialFragment> selected =
                new BoundedProjectionFragmentSelector().select(
                        boundedRead,
                        serviceability,
                        exposureResolution
                );

        return new PublicCustomerProjectionAssembly(
                boundedRead.binding(),
                selected
        );
    }
}
