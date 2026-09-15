package mainstreet.merchantaccount;

import java.util.Optional;

/**
 * Owner port for reconstructing an exact MerchantAccountEstablished occurrence
 * from durable Merchant Account evidence by establishment identity.
 */
@FunctionalInterface
public interface MerchantAccountEstablishedOccurrenceLookup {
    Optional<MerchantAccountEstablishedOccurrence> authoritativeOccurrence(
            String establishmentIdentity);
}
