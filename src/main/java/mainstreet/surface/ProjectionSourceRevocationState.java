package mainstreet.surface;

/** Current revocation evidence applicable to one projection source. */
public enum ProjectionSourceRevocationState {
    CLEAR,
    REVOKED,
    UNVERIFIABLE,
    NOT_APPLICABLE
}
