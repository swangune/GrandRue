package mainstreet.semantic;

import java.time.Instant;

public interface AllocationAuthority {

    AllocationClaim claim(
            String identifier,
            AllocationScope scope,
            String useIdentifier,
            Instant claimedAt
    );
}
