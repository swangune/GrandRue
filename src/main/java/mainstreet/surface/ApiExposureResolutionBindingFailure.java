package mainstreet.surface;

/** Structural fail-closed failures while binding one API Exposure result. */
public enum ApiExposureResolutionBindingFailure {
    AUDIENCE_NOT_ADMITTED,
    AUDIENCE_ADMISSION_REQUEST_MISMATCH,
    SEMANTIC_RELEASE_MISMATCH,
    API_PROVENANCE_MISMATCH,
    DUPLICATE_EXPOSED_ELEMENT
}
