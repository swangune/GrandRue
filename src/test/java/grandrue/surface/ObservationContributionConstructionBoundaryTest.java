package grandrue.surface;

import grandrue.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** BR4 tests-only RED for the accepted v1.13/v1.14 construction boundary. */
class ObservationContributionConstructionBoundaryTest {

    private static final MerchantScope MERCHANT =
            new MerchantScope("merchant-a");
    private static final ObservationContributionKind KIND =
            new ObservationContributionKind("profile", "material-affinity");

    @Test
    void supplies_exact_request_binding_and_trusted_scope_once() {
        EstablishedObservationRequest request =
                TestObservationRequests.request(
                        MERCHANT,
                        "semantic-registry-1.0"
                );
        ObservationRequestBinding expectedBinding =
                TestObservationRequests.binding(request);
        AtomicInteger calls = new AtomicInteger();

        EstablishedObservationContribution contribution =
                new ObservationContributionConstructionBoundary().construct(
                        request,
                        (requestBinding, merchantScope) -> {
                            calls.incrementAndGet();
                            assertSame(expectedBinding, requestBinding);
                            assertEquals(MERCHANT, merchantScope);
                            return new TestContribution(
                                    KIND,
                                    requestBinding,
                                    merchantScope
                            );
                        }
                );

        assertEquals(1, calls.get());
        assertSame(expectedBinding, contribution.requestBinding());
        assertEquals(MERCHANT, contribution.merchantScope());
    }

    @Test
    void rejects_cross_request_and_cross_merchant_output() {
        EstablishedObservationRequest request =
                TestObservationRequests.request(
                        MERCHANT,
                        "semantic-registry-1.0"
                );
        EstablishedObservationRequest otherRequest =
                TestObservationRequests.request(
                        MERCHANT,
                        "semantic-registry-1.0"
                );
        ObservationRequestBinding wrongBinding =
                TestObservationRequests.binding(otherRequest);

        assertThrows(
                IllegalStateException.class,
                () -> new ObservationContributionConstructionBoundary().construct(
                        request,
                        (ignoredBinding, scope) -> new TestContribution(
                                KIND,
                                wrongBinding,
                                scope
                        )
                )
        );

        assertThrows(
                IllegalStateException.class,
                () -> new ObservationContributionConstructionBoundary().construct(
                        request,
                        (binding, ignoredScope) -> new TestContribution(
                                KIND,
                                binding,
                                new MerchantScope("merchant-b")
                        )
                )
        );
    }

    @Test
    void constructor_failure_and_null_output_fail_closed() {
        EstablishedObservationRequest request =
                TestObservationRequests.request(
                        MERCHANT,
                        "semantic-registry-1.0"
                );

        assertThrows(
                IllegalStateException.class,
                () -> new ObservationContributionConstructionBoundary().construct(
                        request,
                        (binding, scope) -> {
                            throw new IllegalArgumentException("owner failure");
                        }
                )
        );

        assertThrows(
                IllegalStateException.class,
                () -> new ObservationContributionConstructionBoundary().construct(
                        request,
                        (binding, scope) -> null
                )
        );
    }

    private record TestContribution(
            ObservationContributionKind kind,
            ObservationRequestBinding requestBinding,
            MerchantScope merchantScope
    ) implements EstablishedObservationContribution {
    }
}
