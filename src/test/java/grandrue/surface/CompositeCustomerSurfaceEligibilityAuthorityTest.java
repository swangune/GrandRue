package grandrue.surface;

import grandrue.application.MerchantScope;
import grandrue.runtime.ExecutionPrincipal;
import grandrue.runtime.TrustedExecutionContext;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CompositeCustomerSurfaceEligibilityAuthorityTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final TrustedExecutionContext TRUSTED_CONTEXT =
            new TrustedExecutionContext(
                    MERCHANT,
                    new ExecutionPrincipal("customer-a"),
                    Optional.empty()
            );

    @Test
    void routes_owner_qualified_requirement_to_matching_owner_evaluator() {
        CompositeCustomerSurfaceEligibilityAuthority authority =
                new CompositeCustomerSurfaceEligibilityAuthority(List.of(
                        new CustomerSurfaceEligibilityRequirementBinding(
                                "booking",
                                (scope, trustedContext, requirementIdentifier, requestContext) -> {
                                    assertEquals(MERCHANT, scope);
                                    assertEquals(TRUSTED_CONTEXT, trustedContext);
                                    assertEquals("related-customer-booking", requirementIdentifier);
                                    assertEquals(Optional.of("booking/B101"), requestContext);
                                    return Optional.of(true);
                                }
                        )
                ));

        assertEquals(
                Optional.of(true),
                authority.currentEligibility(
                        MERCHANT,
                        TRUSTED_CONTEXT,
                        new CustomerSurfaceEligibilityRequirementIdentity(
                                "booking",
                                "related-customer-booking"
                        ),
                        Optional.of("booking/B101")
                )
        );
    }

    @Test
    void missing_owner_evaluator_fails_closed_without_blocking_unrelated_owner() {
        AtomicInteger bookingCalls = new AtomicInteger();
        CompositeCustomerSurfaceEligibilityAuthority authority =
                new CompositeCustomerSurfaceEligibilityAuthority(List.of(
                        new CustomerSurfaceEligibilityRequirementBinding(
                                "booking",
                                (scope, trustedContext, requirementIdentifier, requestContext) -> {
                                    bookingCalls.incrementAndGet();
                                    return Optional.of(true);
                                }
                        )
                ));

        assertEquals(
                Optional.empty(),
                authority.currentEligibility(
                        MERCHANT,
                        TRUSTED_CONTEXT,
                        new CustomerSurfaceEligibilityRequirementIdentity(
                                "appointment",
                                "related-customer-appointment"
                        ),
                        Optional.empty()
                )
        );
        assertEquals(0, bookingCalls.get());

        assertEquals(
                Optional.of(true),
                authority.currentEligibility(
                        MERCHANT,
                        TRUSTED_CONTEXT,
                        new CustomerSurfaceEligibilityRequirementIdentity(
                                "booking",
                                "related-customer-booking"
                        ),
                        Optional.empty()
                )
        );
        assertEquals(1, bookingCalls.get());
    }

    @Test
    void mismatched_merchant_scope_and_null_owner_decision_fail_closed() {
        CompositeCustomerSurfaceEligibilityAuthority authority =
                new CompositeCustomerSurfaceEligibilityAuthority(List.of(
                        new CustomerSurfaceEligibilityRequirementBinding(
                                "booking",
                                (scope, trustedContext, requirementIdentifier, requestContext) -> null
                        )
                ));

        assertEquals(
                Optional.empty(),
                authority.currentEligibility(
                        new MerchantScope("merchant-b"),
                        TRUSTED_CONTEXT,
                        new CustomerSurfaceEligibilityRequirementIdentity(
                                "booking",
                                "related-customer-booking"
                        ),
                        Optional.empty()
                )
        );

        assertEquals(
                Optional.empty(),
                authority.currentEligibility(
                        MERCHANT,
                        TRUSTED_CONTEXT,
                        new CustomerSurfaceEligibilityRequirementIdentity(
                                "booking",
                                "related-customer-booking"
                        ),
                        Optional.empty()
                )
        );
    }

    @Test
    void rejects_duplicate_owner_bindings() {
        CustomerSurfaceEligibilityRequirementEvaluator evaluator =
                (scope, trustedContext, requirementIdentifier, requestContext) ->
                        Optional.of(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> new CompositeCustomerSurfaceEligibilityAuthority(List.of(
                        new CustomerSurfaceEligibilityRequirementBinding("booking", evaluator),
                        new CustomerSurfaceEligibilityRequirementBinding("booking", evaluator)
                ))
        );
    }
}
