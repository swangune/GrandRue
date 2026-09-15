package mainstreet.surface;

import mainstreet.api.ApiSurfaceClass;
import java.util.Objects;

/**
 * MERCHANT assembly over the existing bounded-read selector. It reuses P2/E4 affinity and exact
 * positive-membership checks without loading owner state, rerunning policy or translating DTOs.
 */
public final class MerchantProjectionAssemblyService {
    public MerchantProjectionAssembly assemble(BoundedProjectionRead read,
            ProjectionServiceabilityResult serviceability, ApiExposureResolution exposure) {
        Objects.requireNonNull(read, "read");
        Objects.requireNonNull(serviceability, "serviceability");
        Objects.requireNonNull(exposure, "exposure");
        if (exposure.surface() != ApiSurfaceClass.MERCHANT_OPERATIONAL) {
            throw new IllegalArgumentException("Merchant assembly requires MERCHANT Exposure");
        }
        return new MerchantProjectionAssembly(read.binding(),
                new BoundedProjectionFragmentSelector().select(read, serviceability, exposure));
    }
}
