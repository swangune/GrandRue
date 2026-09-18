package grandrue.surface;

import java.util.Objects;
import java.util.Optional;

/** Exact result of Audience Observation Context establishment. */
public sealed interface AudienceObservationContextEstablishmentResult
        permits EstablishedAudienceObservationContextResult,
        RejectedAudienceObservationContextResult {

    Optional<AudienceObservationContext> context();

    Optional<AudienceObservationContextEstablishmentFailure> failure();
}

final class EstablishedAudienceObservationContextResult
        implements AudienceObservationContextEstablishmentResult {

    private final AudienceObservationContext context;

    EstablishedAudienceObservationContextResult(
            AudienceObservationContext context
    ) {
        this.context = Objects.requireNonNull(context, "context");
    }

    @Override
    public Optional<AudienceObservationContext> context() {
        return Optional.of(context);
    }

    @Override
    public Optional<AudienceObservationContextEstablishmentFailure> failure() {
        return Optional.empty();
    }
}

final class RejectedAudienceObservationContextResult
        implements AudienceObservationContextEstablishmentResult {

    private final AudienceObservationContextEstablishmentFailure failure;

    RejectedAudienceObservationContextResult(
            AudienceObservationContextEstablishmentFailure failure
    ) {
        this.failure = Objects.requireNonNull(failure, "failure");
    }

    @Override
    public Optional<AudienceObservationContext> context() {
        return Optional.empty();
    }

    @Override
    public Optional<AudienceObservationContextEstablishmentFailure> failure() {
        return Optional.of(failure);
    }
}

final class AudienceObservationContextEstablishmentResults {

    private AudienceObservationContextEstablishmentResults() {
    }

    static AudienceObservationContextEstablishmentResult established(
            AudienceObservationContext context
    ) {
        return new EstablishedAudienceObservationContextResult(context);
    }

    static AudienceObservationContextEstablishmentResult rejected(
            AudienceObservationContextEstablishmentFailure failure
    ) {
        return new RejectedAudienceObservationContextResult(failure);
    }
}
