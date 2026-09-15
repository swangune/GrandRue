package mainstreet.surface;

/** Whether a projection is bounded to one request or retained beyond it. */
public enum ProjectionMaterialisationKind {
    REQUEST_SCOPED,
    MATERIALISED
}
