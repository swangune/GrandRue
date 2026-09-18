package grandrue.inventory;

import grandrue.semantic.AllocationClaim;
import grandrue.semantic.AllocationResolution;
import grandrue.semantic.AllocationResolutionType;
import grandrue.semantic.QuantityAllocationScope;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuantityAllocationAuthorityTest {

    private static final Instant CLAIMED_AT =
            Instant.parse("2026-08-20T10:16:00Z");

    @Test
    void claim_reduces_available_to_promise_without_depleting_stock() {

        InMemoryQuantityAllocationAuthority authority =
                authority(Map.of("coffee-beans", 10L));
        QuantityAllocationScope scope =
                new QuantityAllocationScope(
                        "coffee-beans",
                        3L
                );

        AllocationClaim claim = authority.claim(
                "allocation-001",
                scope,
                "order-123",
                CLAIMED_AT
        );

        assertEquals(10L, authority.stockOnHand("coffee-beans"));
        assertEquals(
                7L,
                authority.availableToPromise("coffee-beans")
        );
        assertSame(
                claim,
                authority.claim("allocation-001").orElseThrow()
        );
    }

    @Test
    void aggregate_claims_cannot_exceed_available_to_promise() {

        InMemoryQuantityAllocationAuthority authority =
                authority(Map.of("coffee-beans", 10L));

        authority.claim(
                "allocation-001",
                new QuantityAllocationScope(
                        "coffee-beans",
                        8L
                ),
                "order-123",
                CLAIMED_AT
        );

        InsufficientQuantityException exception = assertThrows(
                InsufficientQuantityException.class,
                () -> authority.claim(
                        "allocation-002",
                        new QuantityAllocationScope(
                                "coffee-beans",
                                3L
                        ),
                        "order-456",
                        CLAIMED_AT.plusSeconds(1)
                )
        );

        assertEquals(3L, exception.requestedQuantity());
        assertEquals(2L, exception.availableQuantity());
        assertEquals(
                2L,
                authority.availableToPromise("coffee-beans")
        );
        assertTrue(authority.claim("allocation-002").isEmpty());
    }

    @Test
    void stock_pools_have_independent_quantity_authority() {

        InMemoryQuantityAllocationAuthority authority = authority(
                Map.of(
                        "coffee-beans",
                        10L,
                        "tea-boxes",
                        2L
                )
        );

        authority.claim(
                "allocation-001",
                new QuantityAllocationScope(
                        "coffee-beans",
                        10L
                ),
                "order-123",
                CLAIMED_AT
        );

        authority.claim(
                "allocation-002",
                new QuantityAllocationScope(
                        "tea-boxes",
                        2L
                ),
                "order-123",
                CLAIMED_AT
        );

        assertEquals(
                0L,
                authority.availableToPromise("coffee-beans")
        );
        assertEquals(
                0L,
                authority.availableToPromise("tea-boxes")
        );
    }

    @Test
    void release_restores_capacity_without_changing_stock_or_history() {

        InMemoryQuantityAllocationAuthority authority =
                authority(Map.of("coffee-beans", 10L));
        AllocationClaim claim = authority.claim(
                "allocation-001",
                new QuantityAllocationScope(
                        "coffee-beans",
                        3L
                ),
                "order-123",
                CLAIMED_AT
        );

        AllocationResolution release = authority.release(
                "resolution-001",
                claim.identifier(),
                CLAIMED_AT.plusSeconds(60)
        );

        assertEquals(
                AllocationResolutionType.RELEASED,
                release.type()
        );
        assertEquals(10L, authority.stockOnHand("coffee-beans"));
        assertEquals(
                10L,
                authority.availableToPromise("coffee-beans")
        );
        assertSame(
                claim,
                authority.claim(claim.identifier()).orElseThrow()
        );
        assertSame(
                release,
                authority.resolution(claim.identifier()).orElseThrow()
        );
    }

    @Test
    void expiry_restores_capacity_and_cannot_be_resolved_again() {

        InMemoryQuantityAllocationAuthority authority =
                authority(Map.of("coffee-beans", 10L));
        AllocationClaim claim = authority.claim(
                "allocation-001",
                new QuantityAllocationScope(
                        "coffee-beans",
                        3L
                ),
                "order-123",
                CLAIMED_AT
        );

        AllocationResolution expiry = authority.expire(
                "resolution-001",
                claim.identifier(),
                CLAIMED_AT.plusSeconds(60)
        );

        assertEquals(
                AllocationResolutionType.EXPIRED,
                expiry.type()
        );
        assertEquals(
                10L,
                authority.availableToPromise("coffee-beans")
        );
        assertThrows(
                IllegalStateException.class,
                () -> authority.release(
                        "resolution-002",
                        claim.identifier(),
                        CLAIMED_AT.plusSeconds(61)
                )
        );
    }

    @Test
    void fulfilment_resolves_claim_and_records_inventory_movement() {

        InMemoryQuantityAllocationAuthority authority =
                authority(Map.of("coffee-beans", 10L));
        AllocationClaim claim = authority.claim(
                "allocation-001",
                new QuantityAllocationScope(
                        "coffee-beans",
                        3L
                ),
                "order-123",
                CLAIMED_AT
        );

        QuantityFulfilment fulfilment = authority.fulfil(
                "resolution-001",
                "movement-001",
                claim.identifier(),
                CLAIMED_AT.plusSeconds(60)
        );

        assertEquals(
                AllocationResolutionType.FULFILLED,
                fulfilment.resolution().type()
        );
        assertEquals(
                3L,
                fulfilment.inventoryMovement().quantity()
        );
        assertEquals(
                "coffee-beans",
                fulfilment.inventoryMovement().subjectIdentifier()
        );
        assertEquals(7L, authority.stockOnHand("coffee-beans"));
        assertEquals(
                7L,
                authority.availableToPromise("coffee-beans")
        );
        assertSame(
                fulfilment.inventoryMovement(),
                authority.inventoryMovement(
                        "movement-001"
                ).orElseThrow()
        );
        assertSame(
                claim,
                authority.claim(claim.identifier()).orElseThrow()
        );
    }

    @Test
    void failed_fulfilment_leaves_claim_active_and_stock_unchanged() {

        InMemoryQuantityAllocationAuthority authority =
                authority(Map.of("coffee-beans", 10L));
        AllocationClaim first = authority.claim(
                "allocation-001",
                new QuantityAllocationScope(
                        "coffee-beans",
                        3L
                ),
                "order-123",
                CLAIMED_AT
        );
        authority.fulfil(
                "resolution-001",
                "movement-001",
                first.identifier(),
                CLAIMED_AT.plusSeconds(60)
        );

        AllocationClaim second = authority.claim(
                "allocation-002",
                new QuantityAllocationScope(
                        "coffee-beans",
                        2L
                ),
                "order-456",
                CLAIMED_AT.plusSeconds(61)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> authority.fulfil(
                        "resolution-002",
                        "movement-001",
                        second.identifier(),
                        CLAIMED_AT.plusSeconds(62)
                )
        );

        assertEquals(7L, authority.stockOnHand("coffee-beans"));
        assertEquals(
                5L,
                authority.availableToPromise("coffee-beans")
        );
        assertTrue(
                authority.resolution(second.identifier()).isEmpty()
        );
    }

    private static InMemoryQuantityAllocationAuthority authority(
            Map<String, Long> stockOnHand
    ) {
        return new InMemoryQuantityAllocationAuthority(stockOnHand);
    }
}
