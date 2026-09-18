package grandrue.surface;

import java.time.Instant;

/**
 * Current surface-wide platform-protection authority for one observation
 * invocation. It grants no audience, relationship or element authority.
 */
@FunctionalInterface
public interface AudienceObservationPlatformProtectionAuthority {

    AudienceObservationPlatformProtection evaluate(
            AudienceObservationContext context,
            AudienceObservationInvocationBinding invocationBinding,
            Instant evaluatedAt
    );
}
