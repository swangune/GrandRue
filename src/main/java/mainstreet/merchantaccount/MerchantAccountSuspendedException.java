package mainstreet.merchantaccount;

/** Ordinary merchant operation rejected because at least one suspension is effective. */
public final class MerchantAccountSuspendedException extends IllegalStateException {
    public MerchantAccountSuspendedException(String merchantIdentifier) {
        super("Merchant Account is suspended: " + merchantIdentifier);
    }
}
