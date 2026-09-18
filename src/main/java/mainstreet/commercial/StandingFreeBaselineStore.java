package mainstreet.commercial;

import grandrue.application.MerchantScope;

import java.util.Optional;

/**
 * Atomic Commercial persistence boundary for the one Standing Free baseline per
 * Merchant Account invariant.
 */
public interface StandingFreeBaselineStore {

    StandingFreeBaseline establishIfAbsent(StandingFreeBaseline candidate);

    Optional<StandingFreeBaseline> baselineFor(MerchantScope merchantScope);
}
