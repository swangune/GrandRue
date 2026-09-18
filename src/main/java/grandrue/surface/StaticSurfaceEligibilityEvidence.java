package grandrue.surface;

/**
 * Marker for configuration-level evidence that made a registered surface
 * contribution statically applicable. Runtime actor authority, provider
 * health and residual operational obligations are deliberately not modelled
 * as static evidence.
 */
public sealed interface StaticSurfaceEligibilityEvidence
        permits ActiveCapabilitySurfaceEligibilityEvidence {
}
