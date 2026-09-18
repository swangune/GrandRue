package grandrue.surface;

import java.util.Objects;
import java.util.Optional;

/**
 * Exact result of trusted observation request establishment.
 *
 * <p>Only an established result carries the opaque request. Rejection carries
 * one exact failure and never a partial request.</p>
 */
public sealed interface ObservationRequestEstablishmentResult
        permits EstablishedObservationRequestResult,
        RejectedObservationRequestResult {

    Optional<EstablishedObservationRequest> establishedRequest();

    Optional<ObservationRequestEstablishmentFailure> failure();
}

final class EstablishedObservationRequestResult
        implements ObservationRequestEstablishmentResult {

    private final EstablishedObservationRequest request;

    EstablishedObservationRequestResult(
            EstablishedObservationRequest request
    ) {
        this.request = Objects.requireNonNull(request, "request");
    }

    @Override
    public Optional<EstablishedObservationRequest> establishedRequest() {
        return Optional.of(request);
    }

    @Override
    public Optional<ObservationRequestEstablishmentFailure> failure() {
        return Optional.empty();
    }
}

final class RejectedObservationRequestResult
        implements ObservationRequestEstablishmentResult {

    private final ObservationRequestEstablishmentFailure failure;

    RejectedObservationRequestResult(
            ObservationRequestEstablishmentFailure failure
    ) {
        this.failure = Objects.requireNonNull(failure, "failure");
    }

    @Override
    public Optional<EstablishedObservationRequest> establishedRequest() {
        return Optional.empty();
    }

    @Override
    public Optional<ObservationRequestEstablishmentFailure> failure() {
        return Optional.of(failure);
    }
}

final class ObservationRequestEstablishmentResults {

    private ObservationRequestEstablishmentResults() {
    }

    static ObservationRequestEstablishmentResult established(
            EstablishedObservationRequest request
    ) {
        return new EstablishedObservationRequestResult(request);
    }

    static ObservationRequestEstablishmentResult rejected(
            ObservationRequestEstablishmentFailure failure
    ) {
        return new RejectedObservationRequestResult(failure);
    }
}
