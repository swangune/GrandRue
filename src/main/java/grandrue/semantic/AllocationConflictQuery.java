package grandrue.semantic;

import java.util.Optional;

public interface AllocationConflictQuery {

    Optional<AllocationClaim> conflictingClaim(
            AllocationScope scope
    );
}
