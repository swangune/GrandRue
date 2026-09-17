package mainstreet.surface;

import grandrue.api.ApiContractIdentity;
import grandrue.api.ApiSurfaceClass;

import java.util.Objects;

final class DefaultApiAudienceObservationContext
        implements ApiAudienceObservationContext {

    private final ApiContractIdentity contractIdentity;
    private final ApiSurfaceClass surface;
    private final AudienceObservationContext context;

    DefaultApiAudienceObservationContext(
            ApiContractIdentity contractIdentity,
            ApiSurfaceClass surface,
            AudienceObservationContext context
    ) {
        this.contractIdentity = Objects.requireNonNull(
                contractIdentity,
                "contractIdentity"
        );
        this.surface = Objects.requireNonNull(surface, "surface");
        this.context = Objects.requireNonNull(context, "context");
    }

    @Override
    public ApiContractIdentity contractIdentity() {
        return contractIdentity;
    }

    @Override
    public ApiSurfaceClass surface() {
        return surface;
    }

    AudienceObservationContext context() {
        return context;
    }
}

/** Package-owned inspection boundary for the later colocated API resolver. */
final class ApiAudienceObservationContextDetails {

    private ApiAudienceObservationContextDetails() {
    }

    static AudienceObservationContext context(
            ApiAudienceObservationContext apiContext
    ) {
        Objects.requireNonNull(apiContext, "apiContext");
        return ((DefaultApiAudienceObservationContext) apiContext).context();
    }
}
