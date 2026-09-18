package mainstreet.surface;

import grandrue.application.MerchantScope;

import java.util.Objects;
import java.util.Optional;

/** Merchant-scoped read context for resolving a PUBLIC surface. */
public record PublicSurfaceResolutionContext(
        MerchantScope merchantScope,
        Optional<String> requestContext
) {
    public PublicSurfaceResolutionContext {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requestContext = Objects.requireNonNull(requestContext, "requestContext");
        requestContext.ifPresent(value -> {
            if (value.isBlank()) {
                throw new IllegalArgumentException(
                        "Request context must not be blank when present"
                );
            }
        });
    }
}
