package grandrue.surface;

import grandrue.application.MerchantScope;

import java.util.Optional;

/** Cross-package test fixture for opaque established Observation Requests. */
public final class TestObservationRequests {

    private TestObservationRequests() {
    }

    public static EstablishedObservationRequest request(
            MerchantScope merchantScope,
            String semanticRegistryReleaseIdentifier
    ) {
        return new DefaultEstablishedObservationRequest(
                TestReleases.activeRelease(
                        merchantScope.merchantIdentifier(),
                        semanticRegistryReleaseIdentifier
                ),
                merchantScope,
                semanticRegistryReleaseIdentifier,
                Optional.empty()
        );
    }

    public static ObservationRequestBinding binding(
            EstablishedObservationRequest request
    ) {
        return EstablishedObservationRequestDetails.requestBinding(request);
    }
}
