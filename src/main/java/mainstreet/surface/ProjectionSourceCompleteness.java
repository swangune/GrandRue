package mainstreet.surface;

/** Server-established completeness of one declared projection source. */
public enum ProjectionSourceCompleteness {
    COMPLETE,
    PARTIAL,
    MISSING,
    CORRUPT,
    UNVERIFIABLE,
    NOT_APPLICABLE
}
