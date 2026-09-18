package grandrue.privacy;

import grandrue.application.MerchantScope;

import java.util.Objects;

/**
 * Bounded merchant/contextual reference to an affected natural-person subject.
 * It is not a GrandRue Identity, CustomerAccount, CustomerContext or
 * Merchant Membership.
 */
public record DataSubjectReference(
        MerchantScope merchantScope,
        String subjectReference
) {
    public DataSubjectReference {
        Objects.requireNonNull(merchantScope, "merchantScope");
        if (subjectReference == null || subjectReference.isBlank()) {
            throw new IllegalArgumentException("subjectReference must not be blank");
        }
    }
}
