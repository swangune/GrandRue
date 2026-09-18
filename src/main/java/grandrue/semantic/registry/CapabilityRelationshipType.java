package grandrue.semantic.registry;

/**
 * Platform-defined meaning of a relationship between registered
 * capabilities.
 */
public enum CapabilityRelationshipType {
    /** The target is activated whenever the source is active. */
    REQUIRES,

    /** The source and target must not both be active. */
    CONFLICTS_WITH,

    /** The target complements the source but is not activated by it. */
    SUPPORTS,

    /** Reserved relationship with no executable activation meaning yet. */
    DEPENDS_ON
}
