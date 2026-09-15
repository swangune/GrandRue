package mainstreet.publication;

import java.time.Instant;
import java.util.Objects;

/** Shared Publication-owned temporal rule used by P4 Exposure and submission-time revalidation. */
public final class OpportunityPublicExposureWindow {
    private OpportunityPublicExposureWindow() { }

    public static boolean contains(OpportunityPublicExposureEvidence evidence, Instant evaluatedAt) {
        Objects.requireNonNull(evidence, "evidence");
        Objects.requireNonNull(evaluatedAt, "evaluatedAt");
        return evidence.publishFrom().map(OpportunityPublicExposureWindow::lowerBound)
                .filter(evaluatedAt::isBefore).isEmpty()
                && evidence.publishUntil().map(OpportunityPublicExposureWindow::upperCutoff)
                .filter(upper -> !evaluatedAt.isBefore(upper)).isEmpty();
    }

    private static Instant lowerBound(OpportunityTemporalBoundary boundary) {
        return switch (boundary) {
            case OpportunityCalendarDateBoundary calendar -> calendar.lowerBoundInstant();
            case OpportunityExactInstantBoundary exact -> exact.instant();
        };
    }

    private static Instant upperCutoff(OpportunityTemporalBoundary boundary) {
        return switch (boundary) {
            case OpportunityCalendarDateBoundary calendar -> calendar.inclusiveUpperCutoff();
            case OpportunityExactInstantBoundary exact -> exact.instant();
        };
    }
}
