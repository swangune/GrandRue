package mainstreet.surface;

import grandrue.api.ApiContractIdentity;
import grandrue.api.ApiSurfaceClass;

import java.util.Objects;
import java.util.Optional;

/** Opaque API-bound successful Exposure resolution. */
public sealed interface ApiExposureResolution
        permits DefaultApiExposureResolution {

    ApiContractIdentity contractIdentity();

    ApiSurfaceClass surface();

    ApiExposedElementSet exposedElements();
}

final class DefaultApiExposureResolution implements ApiExposureResolution {

    private final ApiContractIdentity contractIdentity;
    private final ApiSurfaceClass surface;
    private final DefaultApiExposedElementSet exposedElements;

    DefaultApiExposureResolution(
            ApiContractIdentity contractIdentity,
            ApiSurfaceClass surface,
            DefaultApiExposedElementSet exposedElements
    ) {
        this.contractIdentity = Objects.requireNonNull(
                contractIdentity,
                "contractIdentity"
        );
        this.surface = Objects.requireNonNull(surface, "surface");
        this.exposedElements = Objects.requireNonNull(
                exposedElements,
                "exposedElements"
        );
    }

    @Override
    public ApiContractIdentity contractIdentity() {
        return contractIdentity;
    }

    @Override
    public ApiSurfaceClass surface() {
        return surface;
    }

    @Override
    public ApiExposedElementSet exposedElements() {
        return exposedElements;
    }

    DefaultApiExposedElementSet exposedElementDetails() {
        return exposedElements;
    }
}

/** Package-owned inspection boundary for trusted later Exposure/API composition. */
final class ApiExposureResolutionDetails {

    private ApiExposureResolutionDetails() {
    }

    static ObservationRequestBinding requestBinding(
            ApiExposureResolution resolution
    ) {
        return require(resolution).exposedElementDetails().requestBinding();
    }

    static AudienceObservationInvocationBinding invocationBinding(
            ApiExposureResolution resolution
    ) {
        return require(resolution).exposedElementDetails().invocationBinding();
    }

    static String semanticRegistryReleaseIdentifier(
            ApiExposureResolution resolution
    ) {
        return require(resolution).exposedElementDetails()
                .semanticRegistryReleaseIdentifier();
    }

    static Optional<ExposureElementContractIdentity> memberContractIdentity(
            ApiExposureResolution resolution,
            ExposedElementMembership membership
    ) {
        return require(resolution).exposedElementDetails()
                .memberContractIdentity(membership);
    }

    private static DefaultApiExposureResolution require(
            ApiExposureResolution resolution
    ) {
        Objects.requireNonNull(resolution, "resolution");
        return (DefaultApiExposureResolution) resolution;
    }
}
