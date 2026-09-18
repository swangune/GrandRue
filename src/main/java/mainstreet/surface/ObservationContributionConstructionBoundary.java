package mainstreet.surface;

import grandrue.application.MerchantScope;

import java.util.Objects;

/**
 * Trusted E3 boundary for capability-owned Observation Contribution creation.
 *
 * <p>It exposes no general request introspection: the exact request binding and
 * Merchant Scope are supplied only to one capability constructor invocation,
 * then mechanically validated on the returned contribution.</p>
 */
public final class ObservationContributionConstructionBoundary {

    public EstablishedObservationContribution construct(
            EstablishedObservationRequest request,
            ObservationContributionConstructor constructor
    ) {
        Objects.requireNonNull(request, "request");
        Objects.requireNonNull(constructor, "constructor");

        ObservationRequestBinding requestBinding =
                EstablishedObservationRequestDetails.requestBinding(request);
        MerchantScope merchantScope =
                EstablishedObservationRequestDetails.merchantScope(request);

        EstablishedObservationContribution contribution;
        try {
            contribution = constructor.construct(
                    requestBinding,
                    merchantScope
            );
        } catch (RuntimeException failure) {
            throw new IllegalStateException(
                    "Capability Observation Contribution construction failed",
                    failure
            );
        }

        if (contribution == null) {
            throw new IllegalStateException(
                    "Capability Observation Contribution construction returned null"
            );
        }
        if (contribution.kind() == null) {
            throw new IllegalStateException(
                    "Capability Observation Contribution kind must not be null"
            );
        }
        if (contribution.requestBinding() != requestBinding) {
            throw new IllegalStateException(
                    "Capability Observation Contribution request binding mismatch"
            );
        }
        if (!merchantScope.equals(contribution.merchantScope())) {
            throw new IllegalStateException(
                    "Capability Observation Contribution Merchant Scope mismatch"
            );
        }
        return contribution;
    }
}
