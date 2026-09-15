package mainstreet.merchantaccount;

import java.util.Optional;

/**
 * Resolves a supplied occurrence only when it exactly matches Merchant
 * Account's durable committed publication evidence. Event identity or payload
 * possession alone is not source or Merchant Scope authority.
 *
 * <p>Authority: MS-PROT-026 v1.1, §47 — Event Receipt Does Not Carry Actor
 * Authority; §48 — Merchant Scope; MS-PROT-071 v1.2, §3 — Establishment
 * atomicity and §5 — Domain Event propagation.</p>
 */
@FunctionalInterface
public interface MerchantAccountEstablishedOccurrenceAuthority {
    Optional<MerchantAccountEstablishedOccurrence> authoritativeOccurrence(
            MerchantAccountEstablishedOccurrence suppliedOccurrence);
}
