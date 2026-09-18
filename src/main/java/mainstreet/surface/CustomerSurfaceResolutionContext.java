package mainstreet.surface;

import grandrue.application.MerchantScope;
import grandrue.runtime.TrustedExecutionContext;

import java.util.Objects;
import java.util.Optional;

/** Current trusted context used to resolve one merchant-scoped CUSTOMER Surface. */
public record CustomerSurfaceResolutionContext(
        TrustedExecutionContext trustedExecutionContext,
        Optional<String> requestContext
) {
    public CustomerSurfaceResolutionContext {
        Objects.requireNonNull(trustedExecutionContext, "trustedExecutionContext");
        requestContext = Objects.requireNonNull(requestContext, "requestContext");
    }

    public MerchantScope merchantScope() {
        return trustedExecutionContext.merchantScope();
    }
}
