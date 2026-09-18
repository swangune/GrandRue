package grandrue.surface;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** Closed current audience-admission result for exactly one invocation. */
public sealed interface AudienceObservationAdmissionResult
        permits AdmittedAudienceObservationResult,
        RejectedAudienceObservationResult {

    AudienceObservationInvocationBinding invocationBinding();

    Instant evaluatedAt();

    boolean admitted();

    Optional<AudienceObservationAdmissionFailure> failure();
}

final class AdmittedAudienceObservationResult
        implements AudienceObservationAdmissionResult {

    private final AudienceObservationInvocationBinding invocationBinding;
    private final Instant evaluatedAt;

    AdmittedAudienceObservationResult(
            AudienceObservationInvocationBinding invocationBinding,
            Instant evaluatedAt
    ) {
        this.invocationBinding = Objects.requireNonNull(
                invocationBinding,
                "invocationBinding"
        );
        this.evaluatedAt = Objects.requireNonNull(evaluatedAt, "evaluatedAt");
    }

    @Override
    public AudienceObservationInvocationBinding invocationBinding() {
        return invocationBinding;
    }

    @Override
    public Instant evaluatedAt() {
        return evaluatedAt;
    }

    @Override
    public boolean admitted() {
        return true;
    }

    @Override
    public Optional<AudienceObservationAdmissionFailure> failure() {
        return Optional.empty();
    }
}

final class RejectedAudienceObservationResult
        implements AudienceObservationAdmissionResult {

    private final AudienceObservationInvocationBinding invocationBinding;
    private final Instant evaluatedAt;
    private final AudienceObservationAdmissionFailure failure;

    RejectedAudienceObservationResult(
            AudienceObservationInvocationBinding invocationBinding,
            Instant evaluatedAt,
            AudienceObservationAdmissionFailure failure
    ) {
        this.invocationBinding = Objects.requireNonNull(
                invocationBinding,
                "invocationBinding"
        );
        this.evaluatedAt = Objects.requireNonNull(evaluatedAt, "evaluatedAt");
        this.failure = Objects.requireNonNull(failure, "failure");
    }

    @Override
    public AudienceObservationInvocationBinding invocationBinding() {
        return invocationBinding;
    }

    @Override
    public Instant evaluatedAt() {
        return evaluatedAt;
    }

    @Override
    public boolean admitted() {
        return false;
    }

    @Override
    public Optional<AudienceObservationAdmissionFailure> failure() {
        return Optional.of(failure);
    }
}

final class AudienceObservationAdmissionResults {

    private AudienceObservationAdmissionResults() {
    }

    static AudienceObservationAdmissionResult admitted(
            AudienceObservationInvocationBinding invocationBinding,
            Instant evaluatedAt
    ) {
        return new AdmittedAudienceObservationResult(
                invocationBinding,
                evaluatedAt
        );
    }

    static AudienceObservationAdmissionResult rejected(
            AudienceObservationInvocationBinding invocationBinding,
            Instant evaluatedAt,
            AudienceObservationAdmissionFailure failure
    ) {
        return new RejectedAudienceObservationResult(
                invocationBinding,
                evaluatedAt,
                failure
        );
    }
}
