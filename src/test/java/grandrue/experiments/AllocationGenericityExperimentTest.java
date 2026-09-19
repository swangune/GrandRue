package grandrue.experiments;

import grandrue.inventory.InMemoryQuantityAllocationAuthority;
import grandrue.semantic.AllocationClaim;
import grandrue.semantic.InMemoryTimeWindowAllocationAuthority;
import grandrue.semantic.QuantityAllocationScope;
import grandrue.semantic.TimeWindowAllocationScope;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class AllocationGenericityExperimentTest {

    private static final Instant START =
            Instant.parse("2026-08-21T14:00:00Z");
    private static final Instant END =
            Instant.parse("2026-08-21T15:00:00Z");
    private static final Instant CLAIMED_AT =
            Instant.parse("2026-08-20T10:16:00Z");

    @Test
    void typed_allocation_supports_motel_grocery_and_realtor_constraints() {

        InMemoryTimeWindowAllocationAuthority scheduledCapacity =
                new InMemoryTimeWindowAllocationAuthority();

        AllocationClaim motelStay = scheduledCapacity.claim(
                "allocation-motel-001",
                new TimeWindowAllocationScope(
                        "motel-001:queen-capacity-001",
                        START,
                        END
                ),
                "booking-123",
                CLAIMED_AT
        );
        AllocationClaim realtorViewing = scheduledCapacity.claim(
                "allocation-realtor-001",
                new TimeWindowAllocationScope(
                        "listing-456:viewing-capacity",
                        START,
                        END
                ),
                "viewing-789",
                CLAIMED_AT
        );

        InMemoryQuantityAllocationAuthority stockCapacity =
                new InMemoryQuantityAllocationAuthority(
                        Map.of("coffee-beans", 10L)
                );
        AllocationClaim groceryStock = stockCapacity.claim(
                "allocation-grocery-001",
                new QuantityAllocationScope(
                        "coffee-beans",
                        3L
                ),
                "order-321",
                CLAIMED_AT
        );

        assertInstanceOf(
                TimeWindowAllocationScope.class,
                motelStay.scope()
        );
        assertInstanceOf(
                TimeWindowAllocationScope.class,
                realtorViewing.scope()
        );
        assertInstanceOf(
                QuantityAllocationScope.class,
                groceryStock.scope()
        );
        assertEquals(
                7L,
                stockCapacity.availableToPromise("coffee-beans")
        );
    }
}
