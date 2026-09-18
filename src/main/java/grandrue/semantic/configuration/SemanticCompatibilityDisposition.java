package grandrue.semantic.configuration;

/**
 * Directional, reference-scoped compatibility dispositions defined by
 * MS-PROT-054. These values describe a transition from one semantic registry
 * context to another; they are not release-global compatibility flags.
 */
public enum SemanticCompatibilityDisposition {
    UNAFFECTED,
    SEMANTICALLY_EQUIVALENT,
    MIGRATABLE_PRESERVING_INTENT,
    MIGRATION_REQUIRES_DECISION,
    INCOMPATIBLE
}
