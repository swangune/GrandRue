package mainstreet.merchantprofile;

import java.util.Objects;

/**
 * Domain-facing profile mutation failure with a stable recovery category.
 */
public final class MerchantProfileMutationException
        extends IllegalStateException {

    private final MerchantProfileFailureCategory category;

    public MerchantProfileMutationException(
            MerchantProfileFailureCategory category,
            String message
    ) {
        super(message);
        this.category = Objects.requireNonNull(category, "category");
    }

    public MerchantProfileMutationException(
            MerchantProfileFailureCategory category,
            String message,
            Throwable cause
    ) {
        super(message, cause);
        this.category = Objects.requireNonNull(category, "category");
    }

    public MerchantProfileFailureCategory category() {
        return category;
    }
}
