package grandrue.runtime;

import grandrue.application.MerchantScope;
import grandrue.background.BackgroundWorkContractDefinition;

/**
 * Establishes the bounded scheduled principal for one currently registered
 * Background Work Contract and trusted Merchant Scope.
 *
 * <p>The durable instruction and a technical worker claim are not authority.
 * Implementations must resolve a current registration under MS-PROT-063 and
 * MS-PROT-065 v1.1 §§8 and 54–55.</p>
 */
@FunctionalInterface
public interface ScheduledBackgroundWorkExecutionAuthority {
    TrustedExecutionContext establish(
            BackgroundWorkContractDefinition currentContract,
            MerchantScope merchantScope);
}
