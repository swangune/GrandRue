package mainstreet.surface;

/** Exact failure for API-surface observation-context binding. */
public enum ApiAudienceObservationContextBindingFailure {
    API_PROVENANCE_MISSING,
    API_CONTRACT_UNREGISTERED,
    API_CONTRACT_NOT_QUERY,
    API_PROVENANCE_MISMATCH,
    API_SURFACE_MISMATCH,
    API_SCOPE_MISMATCH,
    API_AUDIENCE_MISMATCH
}
