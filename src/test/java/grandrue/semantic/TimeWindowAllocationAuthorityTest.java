package grandrue.semantic;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TimeWindowAllocationAuthorityTest {

    private static final Instant CHECK_IN =
            Instant.parse("2026-08-20T14:00:00Z");
    private static final Instant CHECK_OUT =
            Instant.parse("2026-08-22T10:00:00Z");

    @Test
    void availability_decision_does_not_establish_a_claim() {

        AllocationScope scope = roomStay(
                CHECK_IN,
                CHECK_OUT
        );

        new AvailabilityDecision(
                "availability-request-001",
                "room-104",
                true,
                Instant.parse("2026-08-20T10:15:30Z")
        );

        InMemoryTimeWindowAllocationAuthority authority =
                new InMemoryTimeWindowAllocationAuthority();

        assertTrue(
                authority.conflictingClaim(scope).isEmpty()
        );
    }

    @Test
    void allocation_establishes_an_authoritative_claim() {

        AllocationScope scope = roomStay(
                CHECK_IN,
                CHECK_OUT
        );
        InMemoryTimeWindowAllocationAuthority authority =
                new InMemoryTimeWindowAllocationAuthority();

        AllocationClaim claim = authority.claim(
                "allocation-001",
                scope,
                "stay-123",
                Instant.parse("2026-08-20T10:16:00Z")
        );

        assertSame(
                claim,
                authority.conflictingClaim(scope).orElseThrow()
        );
    }

    @Test
    void allocation_rejects_an_overlapping_claim_for_the_same_subject() {

        InMemoryTimeWindowAllocationAuthority authority =
                new InMemoryTimeWindowAllocationAuthority();

        AllocationClaim existing = authority.claim(
                "allocation-001",
                roomStay(CHECK_IN, CHECK_OUT),
                "stay-123",
                Instant.parse("2026-08-20T10:16:00Z")
        );

        AllocationScope overlap = roomStay(
                Instant.parse("2026-08-21T14:00:00Z"),
                Instant.parse("2026-08-23T10:00:00Z")
        );

        AllocationConflictException conflict = assertThrows(
                AllocationConflictException.class,
                () -> authority.claim(
                        "allocation-002",
                        overlap,
                        "stay-456",
                        Instant.parse("2026-08-20T10:16:01Z")
                )
        );

        assertSame(existing, conflict.conflictingClaim());
    }

    @Test
    void allocation_allows_adjacent_claims_for_the_same_subject() {

        InMemoryTimeWindowAllocationAuthority authority =
                new InMemoryTimeWindowAllocationAuthority();

        authority.claim(
                "allocation-001",
                roomStay(CHECK_IN, CHECK_OUT),
                "stay-123",
                Instant.parse("2026-08-20T10:16:00Z")
        );

        AllocationClaim adjacent = authority.claim(
                "allocation-002",
                roomStay(
                        CHECK_OUT,
                        Instant.parse("2026-08-24T10:00:00Z")
                ),
                "stay-456",
                Instant.parse("2026-08-20T10:16:01Z")
        );

        assertSame(
                adjacent,
                authority.conflictingClaim(
                        adjacent.scope()
                ).orElseThrow()
        );
    }

    @Test
    void allocation_allows_the_same_window_for_a_different_subject() {

        InMemoryTimeWindowAllocationAuthority authority =
                new InMemoryTimeWindowAllocationAuthority();

        authority.claim(
                "allocation-001",
                roomStay(CHECK_IN, CHECK_OUT),
                "stay-123",
                Instant.parse("2026-08-20T10:16:00Z")
        );

        AllocationClaim otherRoom = authority.claim(
                "allocation-002",
                new TimeWindowAllocationScope(
                        "room-105",
                        CHECK_IN,
                        CHECK_OUT
                ),
                "stay-456",
                Instant.parse("2026-08-20T10:16:01Z")
        );

        assertSame(
                otherRoom,
                authority.conflictingClaim(
                        otherRoom.scope()
                ).orElseThrow()
        );
    }

    @Test
    void allocation_scope_requires_a_positive_time_window() {

        assertThrows(
                IllegalArgumentException.class,
                () -> roomStay(CHECK_IN, CHECK_IN)
        );
    }

    @Test
    void allocation_claim_identifier_cannot_be_reused() {

        InMemoryTimeWindowAllocationAuthority authority =
                new InMemoryTimeWindowAllocationAuthority();

        authority.claim(
                "allocation-001",
                roomStay(CHECK_IN, CHECK_OUT),
                "stay-123",
                Instant.parse("2026-08-20T10:16:00Z")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> authority.claim(
                        "allocation-001",
                        roomStay(
                                CHECK_OUT,
                                Instant.parse("2026-08-24T10:00:00Z")
                        ),
                        "stay-456",
                        Instant.parse("2026-08-20T10:16:01Z")
                )
        );
    }

    private static AllocationScope roomStay(
            Instant startsAt,
            Instant endsAt
    ) {
        return new TimeWindowAllocationScope(
                "room-104",
                startsAt,
                endsAt
        );
    }
}
