package mainstreet.surface;

import grandrue.runtime.TrustedExecutionContext;

import java.util.Objects;
import java.util.Optional;

/**
 * Closed initial observer classification. Audience is derived from the subject
 * implementation and never supplied independently by a caller.
 */
public sealed interface ObservationSubject
        permits PublicObservationSubject,
        CustomerPrincipalObservationSubject,
        CustomerContextualObservationSubject,
        MerchantInteractiveObservationSubject {
}

final class PublicObservationSubject implements ObservationSubject {
}

final class CustomerPrincipalObservationSubject
        implements ObservationSubject {

    private final TrustedExecutionContext executionContext;

    CustomerPrincipalObservationSubject(
            TrustedExecutionContext executionContext
    ) {
        this.executionContext = Objects.requireNonNull(
                executionContext,
                "executionContext"
        );
    }

    TrustedExecutionContext executionContext() {
        return executionContext;
    }
}

final class CustomerContextualObservationSubject
        implements ObservationSubject {

    private final TrustedExecutionContext executionContext;
    private final EstablishedContextualAccessProof contextualAccessProof;

    CustomerContextualObservationSubject(
            TrustedExecutionContext executionContext,
            EstablishedContextualAccessProof contextualAccessProof
    ) {
        this.executionContext = Objects.requireNonNull(
                executionContext,
                "executionContext"
        );
        this.contextualAccessProof = Objects.requireNonNull(
                contextualAccessProof,
                "contextualAccessProof"
        );
    }

    TrustedExecutionContext executionContext() {
        return executionContext;
    }

    EstablishedContextualAccessProof contextualAccessProof() {
        return contextualAccessProof;
    }
}

final class MerchantInteractiveObservationSubject
        implements ObservationSubject {

    private final TrustedExecutionContext executionContext;

    MerchantInteractiveObservationSubject(
            TrustedExecutionContext executionContext
    ) {
        this.executionContext = Objects.requireNonNull(
                executionContext,
                "executionContext"
        );
    }

    TrustedExecutionContext executionContext() {
        return executionContext;
    }
}

final class ObservationSubjectDetails {

    private ObservationSubjectDetails() {
    }

    static SurfaceAudience audience(ObservationSubject subject) {
        Objects.requireNonNull(subject, "subject");
        return switch (subject) {
            case PublicObservationSubject ignored -> SurfaceAudience.PUBLIC;
            case CustomerPrincipalObservationSubject ignored ->
                    SurfaceAudience.CUSTOMER;
            case CustomerContextualObservationSubject ignored ->
                    SurfaceAudience.CUSTOMER;
            case MerchantInteractiveObservationSubject ignored ->
                    SurfaceAudience.MERCHANT;
        };
    }

    static Optional<TrustedExecutionContext> executionContext(
            ObservationSubject subject
    ) {
        Objects.requireNonNull(subject, "subject");
        return switch (subject) {
            case PublicObservationSubject ignored -> Optional.empty();
            case CustomerPrincipalObservationSubject customer ->
                    Optional.of(customer.executionContext());
            case CustomerContextualObservationSubject contextual ->
                    Optional.of(contextual.executionContext());
            case MerchantInteractiveObservationSubject merchant ->
                    Optional.of(merchant.executionContext());
        };
    }

    static Optional<EstablishedContextualAccessProof> contextualAccessProof(
            ObservationSubject subject
    ) {
        Objects.requireNonNull(subject, "subject");
        return switch (subject) {
            case CustomerContextualObservationSubject contextual ->
                    Optional.of(contextual.contextualAccessProof());
            default -> Optional.empty();
        };
    }
}
