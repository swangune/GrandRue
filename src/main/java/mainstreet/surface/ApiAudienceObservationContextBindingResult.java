package mainstreet.surface;

import java.util.Objects;
import java.util.Optional;

/** Sealed exact result of API observation-context binding. */
public sealed interface ApiAudienceObservationContextBindingResult
        permits BoundApiAudienceObservationContextResult,
        RejectedApiAudienceObservationContextResult {

    Optional<ApiAudienceObservationContext> context();

    Optional<ApiAudienceObservationContextBindingFailure> failure();
}

final class BoundApiAudienceObservationContextResult
        implements ApiAudienceObservationContextBindingResult {

    private final ApiAudienceObservationContext context;

    BoundApiAudienceObservationContextResult(
            ApiAudienceObservationContext context
    ) {
        this.context = Objects.requireNonNull(context, "context");
    }

    @Override
    public Optional<ApiAudienceObservationContext> context() {
        return Optional.of(context);
    }

    @Override
    public Optional<ApiAudienceObservationContextBindingFailure> failure() {
        return Optional.empty();
    }
}

final class RejectedApiAudienceObservationContextResult
        implements ApiAudienceObservationContextBindingResult {

    private final ApiAudienceObservationContextBindingFailure failure;

    RejectedApiAudienceObservationContextResult(
            ApiAudienceObservationContextBindingFailure failure
    ) {
        this.failure = Objects.requireNonNull(failure, "failure");
    }

    @Override
    public Optional<ApiAudienceObservationContext> context() {
        return Optional.empty();
    }

    @Override
    public Optional<ApiAudienceObservationContextBindingFailure> failure() {
        return Optional.of(failure);
    }
}

final class ApiAudienceObservationContextBindingResults {

    private ApiAudienceObservationContextBindingResults() {
    }

    static ApiAudienceObservationContextBindingResult bound(
            ApiAudienceObservationContext context
    ) {
        return new BoundApiAudienceObservationContextResult(context);
    }

    static ApiAudienceObservationContextBindingResult rejected(
            ApiAudienceObservationContextBindingFailure failure
    ) {
        return new RejectedApiAudienceObservationContextResult(failure);
    }
}
