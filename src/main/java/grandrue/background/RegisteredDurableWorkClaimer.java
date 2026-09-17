package grandrue.background;

import java.time.Instant;
import java.util.List;

/**
 * Infrastructure claim boundary scoped to one registered Background Work Contract.
 * Claiming remains technical lease establishment and grants no business mutation authority.
 */
public interface RegisteredDurableWorkClaimer {

    List<ClaimedWork> claimDue(
            BackgroundWorkContractIdentity contractIdentity,
            String workerIdentity,
            Instant now,
            Instant claimExpiresAt,
            int limit
    );
}
