package grandrue.merchantaccount;

/** Stale current-Controller/lifecycle basis for a transfer attempt. */
public final class MerchantControllerTransferConflictException
        extends IllegalStateException {
    public MerchantControllerTransferConflictException(String message) {
        super(message);
    }
}
