package grandrue.surface;

/** Closed runtime Projection Serviceability result vocabulary. */
public enum ProjectionServiceabilityOutcome {
    FULLY_SERVICEABLE,
    REDUCED_SERVICEABLE,
    NOT_SERVICEABLE;

    public static ProjectionServiceabilityOutcome mostRestrictive(
            ProjectionServiceabilityOutcome first,
            ProjectionServiceabilityOutcome second
    ) {
        return first.ordinal() >= second.ordinal() ? first : second;
    }
}
