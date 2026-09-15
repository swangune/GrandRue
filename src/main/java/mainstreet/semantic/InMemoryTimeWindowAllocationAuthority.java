package mainstreet.semantic;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class InMemoryTimeWindowAllocationAuthority
        implements AllocationAuthority, AllocationConflictQuery {

    private final List<AllocationClaim> claims =
            new ArrayList<>();

    @Override
    public synchronized AllocationClaim claim(
            String identifier,
            AllocationScope scope,
            String useIdentifier,
            Instant claimedAt
    ) {
        requireTimeWindowScope(scope);

        AllocationClaim claim = new AllocationClaim(
                identifier,
                scope,
                useIdentifier,
                claimedAt
        );

        boolean duplicateIdentifier = claims.stream()
                .anyMatch(existing -> existing.identifier()
                        .equals(identifier));

        if (duplicateIdentifier) {
            throw new IllegalArgumentException(
                    "Allocation claim identifier already used: "
                            + identifier
            );
        }

        Optional<AllocationClaim> conflict =
                findConflict(scope);

        if (conflict.isPresent()) {
            throw new AllocationConflictException(
                    conflict.orElseThrow()
            );
        }

        claims.add(claim);
        return claim;
    }

    @Override
    public synchronized Optional<AllocationClaim> conflictingClaim(
            AllocationScope scope
    ) {
        Objects.requireNonNull(scope);
        return findConflict(scope);
    }

    private Optional<AllocationClaim> findConflict(
            AllocationScope scope
    ) {
        TimeWindowAllocationScope candidate =
                requireTimeWindowScope(scope);

        return claims.stream()
                .filter(existing -> requireTimeWindowScope(
                        existing.scope()
                ).overlaps(candidate))
                .findFirst();
    }

    private static TimeWindowAllocationScope requireTimeWindowScope(
            AllocationScope scope
    ) {
        Objects.requireNonNull(scope);

        if (!(scope instanceof TimeWindowAllocationScope timeWindow)) {
            throw new IllegalArgumentException(
                    "Time-window allocation authority requires a time-window scope"
            );
        }

        return timeWindow;
    }
}
