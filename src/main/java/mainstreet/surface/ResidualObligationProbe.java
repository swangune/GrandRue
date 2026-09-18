package mainstreet.surface;

import grandrue.application.MerchantScope;

/**
 * Narrow composition-boundary delegate used by the Surface residual composite.
 * The owning capability remains authoritative for the meaning of the result.
 */
@FunctionalInterface
public interface ResidualObligationProbe {

    boolean hasOutstandingObligations(MerchantScope merchantScope);
}
