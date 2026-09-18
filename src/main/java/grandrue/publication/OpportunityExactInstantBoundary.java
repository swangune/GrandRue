package grandrue.publication;

import java.time.Instant;
import java.util.Objects;

/** Exact absolute Opportunity temporal boundary. */
public record OpportunityExactInstantBoundary(
        Instant instant
) implements OpportunityTemporalBoundary {
    public OpportunityExactInstantBoundary {
        Objects.requireNonNull(instant, "instant");
    }
}
