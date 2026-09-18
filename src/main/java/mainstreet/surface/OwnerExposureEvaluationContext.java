package mainstreet.surface;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Immutable invocation-scoped view supplied to one exact Exposure evaluator
 * owner. It carries trusted request evidence only; current owner policy and
 * business authority remain evaluator-owned.
 */
public sealed interface OwnerExposureEvaluationContext
        permits DefaultOwnerExposureEvaluationContext {

    MerchantScope merchantScope();

    SurfaceAudience audience();

    ObservationSubjectBinding subjectBinding();

    Instant evaluatedAt();

    Set<EstablishedObservationContribution> contributions();

    Optional<EstablishedContextualAccessProof> contextualAccessProof();
}

final class DefaultOwnerExposureEvaluationContext
        implements OwnerExposureEvaluationContext {

    private final MerchantScope merchantScope;
    private final SurfaceAudience audience;
    private final ObservationSubjectBinding subjectBinding;
    private final Instant evaluatedAt;
    private final Set<EstablishedObservationContribution> contributions;
    private final Optional<EstablishedContextualAccessProof> contextualAccessProof;

    DefaultOwnerExposureEvaluationContext(
            MerchantScope merchantScope,
            SurfaceAudience audience,
            ObservationSubjectBinding subjectBinding,
            Instant evaluatedAt,
            Set<EstablishedObservationContribution> contributions,
            Optional<EstablishedContextualAccessProof> contextualAccessProof
    ) {
        this.merchantScope = Objects.requireNonNull(merchantScope, "merchantScope");
        this.audience = Objects.requireNonNull(audience, "audience");
        this.subjectBinding = Objects.requireNonNull(subjectBinding, "subjectBinding");
        if (subjectBinding.audience() != audience) {
            throw new IllegalArgumentException(
                    "Observation subject binding audience must match owner evaluation audience"
            );
        }
        this.evaluatedAt = Objects.requireNonNull(evaluatedAt, "evaluatedAt");
        this.contributions = Set.copyOf(
                Objects.requireNonNull(contributions, "contributions")
        );
        this.contextualAccessProof = Objects.requireNonNull(
                contextualAccessProof,
                "contextualAccessProof"
        );
    }

    @Override
    public MerchantScope merchantScope() {
        return merchantScope;
    }

    @Override
    public SurfaceAudience audience() {
        return audience;
    }

    @Override
    public ObservationSubjectBinding subjectBinding() {
        return subjectBinding;
    }

    @Override
    public Instant evaluatedAt() {
        return evaluatedAt;
    }

    @Override
    public Set<EstablishedObservationContribution> contributions() {
        return contributions;
    }

    @Override
    public Optional<EstablishedContextualAccessProof> contextualAccessProof() {
        return contextualAccessProof;
    }
}

/** Package-owned construction boundary used by the generic E4 resolver. */
final class OwnerExposureEvaluationContexts {

    private OwnerExposureEvaluationContexts() {
    }

    static OwnerExposureEvaluationContext forOwner(
            String ownerIdentifier,
            AudienceObservationContext context,
            AudienceObservationAdmissionResult admission
    ) {
        requireIdentifier(ownerIdentifier, "ownerIdentifier");
        Objects.requireNonNull(context, "context");
        Objects.requireNonNull(admission, "admission");
        if (!admission.admitted()) {
            throw new IllegalArgumentException(
                    "Owner Exposure evaluation context requires admitted audience observation"
            );
        }

        EstablishedObservationRequest request =
                AudienceObservationContextDetails.request(context);
        ObservationRequestBinding requestBinding =
                EstablishedObservationRequestDetails.requestBinding(request);
        if (!requestBinding.equals(
                AudienceObservationInvocationBindingDetails.requestBinding(
                        admission.invocationBinding()
                )
        )) {
            throw new IllegalArgumentException(
                    "Audience admission must belong to the same established observation request"
            );
        }

        Set<EstablishedObservationContribution> ownerContributions =
                new LinkedHashSet<>();
        for (EstablishedObservationContribution contribution
                : AudienceObservationContextDetails.contributions(context)) {
            Objects.requireNonNull(contribution, "observation contribution");
            if (ownerIdentifier.equals(
                    contribution.kind().ownerCapabilityIdentifier()
            )) {
                ownerContributions.add(contribution);
            }
        }

        Optional<EstablishedContextualAccessProof> ownerProof =
                AudienceObservationContextDetails.contextualAccessProof(context)
                        .filter(proof -> ownerIdentifier.equals(
                                proof.kind().ownerCapabilityIdentifier()
                        ));

        return new DefaultOwnerExposureEvaluationContext(
                EstablishedObservationRequestDetails.merchantScope(request),
                AudienceObservationContextDetails.audience(context),
                ObservationSubjectBindings.from(
                        AudienceObservationContextDetails.subject(context)
                ),
                admission.evaluatedAt(),
                ownerContributions,
                ownerProof
        );
    }

    private static void requireIdentifier(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
    }
}
