package grandrue.semantic.registry;

/**
 * Typed semantic basis for one capability-owned schema field. A field either
 * reuses a global DataConcept, reuses a capability-local DataConcept, or owns
 * field-specific meaning itself.
 */
public sealed interface FieldSemanticBasis
        permits GlobalDataConceptReference,
        CapabilityDataConceptReference,
        FieldSpecificSemantics {
}
