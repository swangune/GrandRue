package grandrue.semantic.registry;

/**
 * Canonical kinds of globally addressable platform semantic definitions.
 * Operations and states normally remain scoped to their semantic owner.
 */
public enum SemanticDefinitionKind {
    PRIMITIVE,
    ABSTRACT_PRIMITIVE,
    FRAMEWORK,
    CAPABILITY,
    RESOURCE,
    DATA_CONCEPT,
    REQUIREMENT,
    POLICY,
    EVENT
}
