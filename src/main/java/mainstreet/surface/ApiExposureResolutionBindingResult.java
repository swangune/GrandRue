package mainstreet.surface;

import java.util.Objects;
import java.util.Optional;

/** Closed success/failure wrapper keeping structural failure distinct from empty success. */
public final class ApiExposureResolutionBindingResult {

    private final Optional<ApiExposureResolution> resolution;
    private final Optional<ApiExposureResolutionBindingFailure> failure;

    private ApiExposureResolutionBindingResult(
            Optional<ApiExposureResolution> resolution,
            Optional<ApiExposureResolutionBindingFailure> failure
    ) {
        this.resolution = Objects.requireNonNull(resolution, "resolution");
        this.failure = Objects.requireNonNull(failure, "failure");
        if (resolution.isPresent() == failure.isPresent()) {
            throw new IllegalArgumentException(
                    "Exactly one of resolution or failure must be present"
            );
        }
    }

    public Optional<ApiExposureResolution> resolution() {
        return resolution;
    }

    public Optional<ApiExposureResolutionBindingFailure> failure() {
        return failure;
    }

    static ApiExposureResolutionBindingResult resolved(
            ApiExposureResolution resolution
    ) {
        return new ApiExposureResolutionBindingResult(
                Optional.of(Objects.requireNonNull(resolution, "resolution")),
                Optional.empty()
        );
    }

    static ApiExposureResolutionBindingResult failed(
            ApiExposureResolutionBindingFailure failure
    ) {
        return new ApiExposureResolutionBindingResult(
                Optional.empty(),
                Optional.of(Objects.requireNonNull(failure, "failure"))
        );
    }
}
