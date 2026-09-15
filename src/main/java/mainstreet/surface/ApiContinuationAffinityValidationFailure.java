package mainstreet.surface;

/** Closed fail-closed T4b continuation-affinity mismatch vocabulary. */
public enum ApiContinuationAffinityValidationFailure {
    CURRENT_QUERY_CONTEXT_MISMATCH,
    QUERY_SEMANTICS_MISMATCH,
    MERCHANT_SCOPE_MISMATCH,
    AUDIENCE_MISMATCH,
    SEMANTIC_RELEASE_MISMATCH
}
