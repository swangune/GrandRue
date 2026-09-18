package mainstreet.surface;

import grandrue.runtime.TrustedExecutionContext;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

final class DefaultAudienceObservationContext
        implements AudienceObservationContext {

    private final EstablishedObservationRequest request;
    private final ObservationSubject subject;
    private final Set<EstablishedObservationContribution> contributions;

    DefaultAudienceObservationContext(
            EstablishedObservationRequest request,
            ObservationSubject subject
    ) {
        this(request, subject, Set.of());
    }

    DefaultAudienceObservationContext(
            EstablishedObservationRequest request,
            ObservationSubject subject,
            Set<EstablishedObservationContribution> contributions
    ) {
        this.request = Objects.requireNonNull(request, "request");
        this.subject = Objects.requireNonNull(subject, "subject");
        this.contributions = Set.copyOf(
                Objects.requireNonNull(contributions, "contributions")
        );
    }

    EstablishedObservationRequest request() {
        return request;
    }

    ObservationSubject subject() {
        return subject;
    }

    Set<EstablishedObservationContribution> contributions() {
        return contributions;
    }
}

/** Package-owned inspection boundary for later E3 admission and API binding. */
final class AudienceObservationContextDetails {

    private AudienceObservationContextDetails() {
    }

    static EstablishedObservationRequest request(
            AudienceObservationContext context
    ) {
        return require(context).request();
    }

    static ObservationSubject subject(AudienceObservationContext context) {
        return require(context).subject();
    }

    static SurfaceAudience audience(AudienceObservationContext context) {
        return ObservationSubjectDetails.audience(subject(context));
    }

    static Optional<TrustedExecutionContext> executionContext(
            AudienceObservationContext context
    ) {
        return ObservationSubjectDetails.executionContext(subject(context));
    }

    static Optional<EstablishedContextualAccessProof> contextualAccessProof(
            AudienceObservationContext context
    ) {
        return ObservationSubjectDetails.contextualAccessProof(
                subject(context)
        );
    }

    static Set<EstablishedObservationContribution> contributions(
            AudienceObservationContext context
    ) {
        return require(context).contributions();
    }

    private static DefaultAudienceObservationContext require(
            AudienceObservationContext context
    ) {
        Objects.requireNonNull(context, "context");
        return (DefaultAudienceObservationContext) context;
    }
}
