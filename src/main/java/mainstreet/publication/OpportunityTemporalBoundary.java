package mainstreet.publication;

/**
 * Typed Opportunity temporal boundary required by MS-PROT-046 v1.3.
 *
 * <p>A boundary preserves either calendar-date meaning with an explicit interpretation zone or an
 * exact absolute instant. Client clock, browser locale and deployment timezone are not part of this
 * authority.</p>
 */
public sealed interface OpportunityTemporalBoundary
        permits OpportunityCalendarDateBoundary, OpportunityExactInstantBoundary {
}
