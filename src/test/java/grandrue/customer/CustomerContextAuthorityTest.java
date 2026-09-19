package grandrue.customer;

import grandrue.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerContextAuthorityTest {

    private static final Instant ESTABLISHED_AT =
            Instant.parse("2026-08-21T10:00:00Z");

    @Test
    void identical_local_identifiers_are_isolated_by_merchant() {
        InMemoryCustomerContextAuthority authority =
                new InMemoryCustomerContextAuthority();
        CustomerContext first = context("merchant-a", ESTABLISHED_AT);
        CustomerContext second = context("merchant-b", ESTABLISHED_AT);

        authority.register(first);
        authority.register(second);

        assertSame(
                first,
                authority.require(first.merchantScope(), first.identifier())
        );
        assertSame(
                second,
                authority.require(second.merchantScope(), second.identifier())
        );
    }

    @Test
    void exact_registration_retry_returns_the_original_context() {
        InMemoryCustomerContextAuthority authority =
                new InMemoryCustomerContextAuthority();
        CustomerContext context = context("merchant-a", ESTABLISHED_AT);

        CustomerContext original = authority.register(context);
        CustomerContext replay = authority.register(new CustomerContext(
                context.merchantScope(),
                context.identifier(),
                context.establishedAt()
        ));

        assertSame(original, replay);
    }

    @Test
    void identity_cannot_be_reused_for_another_relationship_fact() {
        InMemoryCustomerContextAuthority authority =
                new InMemoryCustomerContextAuthority();
        CustomerContext original = context("merchant-a", ESTABLISHED_AT);
        authority.register(original);

        assertThrows(
                CustomerContextIdentityConflictException.class,
                () -> authority.register(new CustomerContext(
                        original.merchantScope(),
                        original.identifier(),
                        ESTABLISHED_AT.plusSeconds(1)
                ))
        );
        assertSame(
                original,
                authority.require(
                        original.merchantScope(),
                        original.identifier()
                )
        );
    }

    private static CustomerContext context(
            String merchantIdentifier,
            Instant establishedAt
    ) {
        return new CustomerContext(
                new MerchantScope(merchantIdentifier),
                "customer-123",
                establishedAt
        );
    }
}
