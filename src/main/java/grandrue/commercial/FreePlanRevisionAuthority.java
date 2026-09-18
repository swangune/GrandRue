package grandrue.commercial;

import mainstreet.commercial.StandardPlanRevision;

import java.time.Instant;

/**
 * Commercial-owned historical catalogue boundary for resolving the FREE plan
 * revision effective at an authoritative instant.
 *
 * <p>The implementation must resolve by the revision's accepted effective
 * policy window. Worker execution time must not replace the supplied instant.</p>
 */
@FunctionalInterface
public interface FreePlanRevisionAuthority {

    StandardPlanRevision effectiveFreePlanRevisionAt(Instant instant);
}
