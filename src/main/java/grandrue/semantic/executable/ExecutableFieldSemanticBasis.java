package grandrue.semantic.executable;

/** Resolved semantic basis for one executable schema field. */
public sealed interface ExecutableFieldSemanticBasis
        permits ExecutableGlobalDataConceptReference,
        ExecutableCapabilityDataConceptReference,
        ExecutableFieldSpecificSemantics {
}
