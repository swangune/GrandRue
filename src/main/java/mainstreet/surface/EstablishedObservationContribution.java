package mainstreet.surface;

import grandrue.application.MerchantScope;

/**
 * Typed capability-owned observation evidence established for one request.
 *
 * <p>Presence is never authority and never satisfies an Exposure requirement.
 * Implementations remain final and package-private to their owner.</p>
 */
public interface EstablishedObservationContribution {

    ObservationContributionKind kind();

    ObservationRequestBinding requestBinding();

    MerchantScope merchantScope();
}
