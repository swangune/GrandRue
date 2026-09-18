package grandrue.application;

import grandrue.background.ClaimedWork;
import grandrue.background.DurableWorkStore;
import grandrue.commercial.StandingFreeBaseline;

import java.util.Optional;

/** Bounded application execution seam for one claimed Standing Free durable-work item. */
@FunctionalInterface
public interface StandingFreeClaimedWorkExecution {

    Optional<StandingFreeBaseline> executeClaimed(
            ClaimedWork claimedWork,
            DurableWorkStore workStore,
            String attemptIdentity
    );
}
