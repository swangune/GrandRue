package grandrue.customer;

import mainstreet.application.MerchantScope;

import java.util.Optional;

/**
 * Authority for stable merchant-owned customer relationships. Implementations
 * must scope identity by merchant and make exact registration retry-safe.
 */
public interface CustomerContextAuthority {

    CustomerContext register(CustomerContext customerContext);

    Optional<CustomerContext> find(
            MerchantScope merchantScope,
            String identifier
    );

    default CustomerContext require(
            MerchantScope merchantScope,
            String identifier
    ) {
        return find(merchantScope, identifier).orElseThrow(() ->
                new IllegalArgumentException(
                        "Customer context does not exist for merchant: "
                                + identifier
                )
        );
    }
}
