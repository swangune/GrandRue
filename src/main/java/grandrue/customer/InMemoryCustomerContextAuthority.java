package grandrue.customer;

import grandrue.application.MerchantScope;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/** Thread-safe reference authority with merchant-scoped identity. */
public final class InMemoryCustomerContextAuthority
        implements CustomerContextAuthority {

    private final Map<ScopedIdentifier, CustomerContext> contexts =
            new LinkedHashMap<>();

    @Override
    public synchronized CustomerContext register(
            CustomerContext customerContext
    ) {
        Objects.requireNonNull(customerContext);
        ScopedIdentifier key = new ScopedIdentifier(
                customerContext.merchantScope(),
                customerContext.identifier()
        );
        CustomerContext existing = contexts.get(key);
        if (existing == null) {
            contexts.put(key, customerContext);
            return customerContext;
        }
        if (!existing.equals(customerContext)) {
            throw new CustomerContextIdentityConflictException(
                    customerContext.identifier()
            );
        }
        return existing;
    }

    @Override
    public synchronized Optional<CustomerContext> find(
            MerchantScope merchantScope,
            String identifier
    ) {
        return Optional.ofNullable(contexts.get(new ScopedIdentifier(
                merchantScope,
                identifier
        )));
    }

    private record ScopedIdentifier(
            MerchantScope merchantScope,
            String identifier
    ) {
        private ScopedIdentifier {
            Objects.requireNonNull(merchantScope);
            if (identifier == null || identifier.isBlank()) {
                throw new IllegalArgumentException(
                        "Customer context identifier must not be blank"
                );
            }
        }
    }
}
