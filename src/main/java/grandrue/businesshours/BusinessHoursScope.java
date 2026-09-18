package grandrue.businesshours;

import grandrue.application.MerchantScope;

import java.util.Objects;
import java.util.Optional;

/**
 * Explicit authority scope for Public Business Hours.
 *
 * <p>Merchant and merchant-location scopes are intentionally distinct. No
 * inheritance or precedence between them is implied by this value.</p>
 */
public record BusinessHoursScope(
        BusinessHoursScopeKind kind,
        MerchantScope merchantScope,
        Optional<String> merchantLocationIdentity
) {

    public BusinessHoursScope {
        Objects.requireNonNull(kind, "kind");
        Objects.requireNonNull(merchantScope, "merchantScope");
        merchantLocationIdentity = Objects.requireNonNull(
                merchantLocationIdentity,
                "merchantLocationIdentity"
        );

        switch (kind) {
            case MERCHANT -> {
                if (merchantLocationIdentity.isPresent()) {
                    throw new IllegalArgumentException(
                            "Merchant Business Hours scope must not identify a location"
                    );
                }
            }
            case MERCHANT_LOCATION -> {
                if (merchantLocationIdentity.isEmpty()
                        || merchantLocationIdentity.orElseThrow().isBlank()) {
                    throw new IllegalArgumentException(
                            "Merchant-location Business Hours scope requires a location identity"
                    );
                }
            }
        }
    }

    public static BusinessHoursScope merchant(MerchantScope merchantScope) {
        return new BusinessHoursScope(
                BusinessHoursScopeKind.MERCHANT,
                merchantScope,
                Optional.empty()
        );
    }

    public static BusinessHoursScope merchantLocation(
            MerchantScope merchantScope,
            String merchantLocationIdentity
    ) {
        if (merchantLocationIdentity == null
                || merchantLocationIdentity.isBlank()) {
            throw new IllegalArgumentException(
                    "Merchant location identity must not be blank"
            );
        }
        return new BusinessHoursScope(
                BusinessHoursScopeKind.MERCHANT_LOCATION,
                merchantScope,
                Optional.of(merchantLocationIdentity)
        );
    }
}
