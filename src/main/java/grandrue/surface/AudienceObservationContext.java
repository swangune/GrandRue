package grandrue.surface;

/**
 * Opaque immutable request-scoped observation fact.
 *
 * <p>The package-owned implementation retains the exact established request
 * and closed subject without exposing public construction or unwrapping.</p>
 */
public sealed interface AudienceObservationContext
        permits DefaultAudienceObservationContext {
}
