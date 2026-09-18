package grandrue.surface;

/**
 * Opaque trusted request establishment result for Audience Observation
 * Context construction.
 *
 * <p>Callers can retain the result only for the bounded request. Package-owned
 * context construction inspects its exact release, scope and provenance
 * without exposing a public unwrapping contract.</p>
 */
public sealed interface EstablishedObservationRequest
        permits DefaultEstablishedObservationRequest {
}
