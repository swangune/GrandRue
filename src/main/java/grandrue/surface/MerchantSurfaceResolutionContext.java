package grandrue.surface;

import grandrue.application.MerchantScope;
import grandrue.runtime.ExecutionPrincipal;

import java.util.Objects;

/**
 * Trusted merchant-surface request context. It identifies the Merchant Scope
 * and actor only; privileges remain resolved from current authority.
 */
public record MerchantSurfaceResolutionContext(
        MerchantScope merchantScope,
        ExecutionPrincipal principal
) {
    public MerchantSurfaceResolutionContext {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(principal, "principal");
    }
}
