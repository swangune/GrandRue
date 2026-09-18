package grandrue.runtime;

import grandrue.application.MerchantScope;
import mainstreet.semantic.event.EventReactionContractDefinition;

/**
 * Establishes the separately registered scheduled principal for one current
 * Event Reaction Contract and trusted Merchant Scope.
 *
 * <p>Authority: MS-PROT-026 v1.1, §21 — Authoritative Downstream Mutation and
 * §47 — Event Receipt Does Not Carry Actor Authority; MS-PROT-063, §12 —
 * System and scheduled principals.</p>
 */
@FunctionalInterface
public interface ScheduledEventReactionExecutionAuthority {
    TrustedExecutionContext establish(
            EventReactionContractDefinition currentContract,
            MerchantScope merchantScope);
}
