package mainstreet.surface;

import grandrue.application.MerchantScope;

/**
 * Capability-owned constructor invoked by the trusted E3 contribution boundary.
 *
 * <p>The constructor receives only exact request affinity and trusted Merchant
 * Scope. Any owner evidence it needs must already be closed over by the
 * capability implementation.</p>
 */
@FunctionalInterface
public interface ObservationContributionConstructor {

    EstablishedObservationContribution construct(
            ObservationRequestBinding requestBinding,
            MerchantScope merchantScope
    );
}
