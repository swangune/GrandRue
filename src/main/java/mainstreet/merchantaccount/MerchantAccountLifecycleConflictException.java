package mainstreet.merchantaccount;

/** Requested Merchant Account lifecycle transition is stale or invalid. */
public final class MerchantAccountLifecycleConflictException
        extends IllegalStateException {
    public MerchantAccountLifecycleConflictException(
            String merchantIdentifier,
            MerchantAccountLifecycle actual,
            MerchantAccountLifecycle required
    ) {
        super("Merchant Account lifecycle conflict for " + merchantIdentifier
                + ": required " + required + " but was " + actual);
    }
}
