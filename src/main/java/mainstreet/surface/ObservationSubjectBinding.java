package mainstreet.surface;

import mainstreet.runtime.TrustedExecutionContext;

import java.util.Objects;
import java.util.Optional;

/**
 * Stable non-authoritative observer affinity exposed to bounded read/evaluation
 * code without exposing raw execution, authentication, session or credential
 * state.
 */
public sealed interface ObservationSubjectBinding
        permits PublicObservationSubjectBinding,
        PrincipalObservationSubjectBinding,
        ContextualObservationSubjectBinding {

    SurfaceAudience audience();

    Optional<String> principalIdentifier();

    Optional<ContextualAccessProofKind> contextualAccessKind();

    Optional<String> contextualAccessBinding();
}

record PublicObservationSubjectBinding()
        implements ObservationSubjectBinding {

    @Override
    public SurfaceAudience audience() {
        return SurfaceAudience.PUBLIC;
    }

    @Override
    public Optional<String> principalIdentifier() {
        return Optional.empty();
    }

    @Override
    public Optional<ContextualAccessProofKind> contextualAccessKind() {
        return Optional.empty();
    }

    @Override
    public Optional<String> contextualAccessBinding() {
        return Optional.empty();
    }
}

record PrincipalObservationSubjectBinding(
        SurfaceAudience audience,
        String principal
) implements ObservationSubjectBinding {

    PrincipalObservationSubjectBinding {
        Objects.requireNonNull(audience, "audience");
        if (audience == SurfaceAudience.PUBLIC) {
            throw new IllegalArgumentException(
                    "Principal observation binding cannot represent PUBLIC audience"
            );
        }
        requireIdentifier(principal, "principal");
    }

    @Override
    public Optional<String> principalIdentifier() {
        return Optional.of(principal);
    }

    @Override
    public Optional<ContextualAccessProofKind> contextualAccessKind() {
        return Optional.empty();
    }

    @Override
    public Optional<String> contextualAccessBinding() {
        return Optional.empty();
    }

    private static void requireIdentifier(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
    }
}

record ContextualObservationSubjectBinding(
        String principal,
        ContextualAccessProofKind accessKind,
        String accessBinding
) implements ObservationSubjectBinding {

    ContextualObservationSubjectBinding {
        requireIdentifier(principal, "principal");
        Objects.requireNonNull(accessKind, "accessKind");
        requireIdentifier(accessBinding, "accessBinding");
    }

    @Override
    public SurfaceAudience audience() {
        return SurfaceAudience.CUSTOMER;
    }

    @Override
    public Optional<String> principalIdentifier() {
        return Optional.of(principal);
    }

    @Override
    public Optional<ContextualAccessProofKind> contextualAccessKind() {
        return Optional.of(accessKind);
    }

    @Override
    public Optional<String> contextualAccessBinding() {
        return Optional.of(accessBinding);
    }

    private static void requireIdentifier(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
    }
}

final class ObservationSubjectBindings {

    private ObservationSubjectBindings() {
    }

    static ObservationSubjectBinding from(ObservationSubject subject) {
        Objects.requireNonNull(subject, "subject");
        return switch (subject) {
            case PublicObservationSubject ignored ->
                    new PublicObservationSubjectBinding();
            case CustomerPrincipalObservationSubject customer ->
                    principalBinding(
                            SurfaceAudience.CUSTOMER,
                            customer.executionContext()
                    );
            case CustomerContextualObservationSubject contextual ->
                    new ContextualObservationSubjectBinding(
                            contextual.executionContext().principal().identifier(),
                            contextual.contextualAccessProof().kind(),
                            contextual.contextualAccessProof().accessBinding()
                    );
            case MerchantInteractiveObservationSubject merchant ->
                    principalBinding(
                            SurfaceAudience.MERCHANT,
                            merchant.executionContext()
                    );
        };
    }

    private static ObservationSubjectBinding principalBinding(
            SurfaceAudience audience,
            TrustedExecutionContext executionContext
    ) {
        return new PrincipalObservationSubjectBinding(
                audience,
                executionContext.principal().identifier()
        );
    }
}
