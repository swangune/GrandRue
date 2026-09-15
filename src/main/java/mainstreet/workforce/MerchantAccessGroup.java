package mainstreet.workforce;

import mainstreet.application.MerchantScope;

import java.util.Objects;

/** Merchant-scoped access-administration grouping; never an execution principal. */
public record MerchantAccessGroup(
        String groupIdentifier,
        MerchantScope merchantScope,
        String displayName
) {
    public MerchantAccessGroup {
        if (groupIdentifier == null || groupIdentifier.isBlank()) {
            throw new IllegalArgumentException("Group identifier must not be blank");
        }
        Objects.requireNonNull(merchantScope, "merchantScope");
        if (displayName == null || displayName.isBlank()) {
            throw new IllegalArgumentException("Group display name must not be blank");
        }
    }
}
