package mainstreet.semantic.release;

/** Failure classes accepted by ADR-013 for semantic release materialisation. */
public enum SemanticMaterialisationFailure {
    UNSUPPORTED_BUNDLE_FORMAT,
    CONTENT_INTEGRITY_FAILURE,
    RELEASE_IDENTITY_MISMATCH,
    DUPLICATE_RELEASE_IDENTITY,
    DEFINITION_DECODE_FAILURE,
    ASSEMBLY_COHERENCE_FAILURE,
    REQUIRED_RELEASE_NOT_MATERIALISED
}
