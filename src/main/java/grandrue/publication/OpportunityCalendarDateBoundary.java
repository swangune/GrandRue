package grandrue.publication;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;

/** Calendar-date Opportunity boundary with explicit IANA timezone interpretation. */
public record OpportunityCalendarDateBoundary(
        LocalDate date,
        ZoneId zoneId
) implements OpportunityTemporalBoundary {
    public OpportunityCalendarDateBoundary {
        Objects.requireNonNull(date, "date");
        Objects.requireNonNull(zoneId, "zoneId");
    }

    /** Lower-bound instant: start of the recorded calendar date in its recorded zone. */
    public Instant lowerBoundInstant() {
        return date.atStartOfDay(zoneId).toInstant();
    }

    /**
     * Exclusive instant immediately after the inclusive recorded calendar date, used for upper
     * deadline / publish-until semantics.
     */
    public Instant inclusiveUpperCutoff() {
        return date.plusDays(1).atStartOfDay(zoneId).toInstant();
    }
}
