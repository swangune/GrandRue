package mainstreet.surface;

import grandrue.api.ApiContractIdentity;
import grandrue.api.ApiSurfaceClass;
import grandrue.application.MerchantScope;
import grandrue.semantic.configuration.ActiveRelease;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

final class DefaultObservationRequestBinding
        implements ObservationRequestBinding {

    private final UUID identifier;

    DefaultObservationRequestBinding() {
        identifier = UUID.randomUUID();
    }

    UUID identifier() {
        return identifier;
    }
}

record ApiObservationRequestProvenance(
        ApiContractIdentity contractIdentity,
        ApiSurfaceClass surface,
        String scopeEstablishmentRuleReference
) {
    ApiObservationRequestProvenance {
        Objects.requireNonNull(contractIdentity, "contractIdentity");
        Objects.requireNonNull(surface, "surface");
        if (scopeEstablishmentRuleReference == null
                || scopeEstablishmentRuleReference.isBlank()) {
            throw new IllegalArgumentException(
                    "Scope-establishment rule reference must not be blank"
            );
        }
    }
}

final class DefaultEstablishedObservationRequest
        implements EstablishedObservationRequest {

    private final ActiveRelease activeRelease;
    private final MerchantScope merchantScope;
    private final ObservationRequestBinding requestBinding;
    private final String semanticRegistryReleaseIdentifier;
    private final Optional<ApiObservationRequestProvenance> apiProvenance;

    DefaultEstablishedObservationRequest(
            ActiveRelease activeRelease,
            MerchantScope merchantScope,
            String semanticRegistryReleaseIdentifier,
            Optional<ApiObservationRequestProvenance> apiProvenance
    ) {
        this.activeRelease = Objects.requireNonNull(
                activeRelease,
                "activeRelease"
        );
        this.merchantScope = Objects.requireNonNull(
                merchantScope,
                "merchantScope"
        );
        this.requestBinding = new DefaultObservationRequestBinding();
        if (semanticRegistryReleaseIdentifier == null
                || semanticRegistryReleaseIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Semantic registry release identifier must not be blank"
            );
        }
        this.semanticRegistryReleaseIdentifier =
                semanticRegistryReleaseIdentifier;
        this.apiProvenance = Objects.requireNonNull(
                apiProvenance,
                "apiProvenance"
        );
    }

    ActiveRelease activeRelease() {
        return activeRelease;
    }

    MerchantScope merchantScope() {
        return merchantScope;
    }

    ObservationRequestBinding requestBinding() {
        return requestBinding;
    }

    String semanticRegistryReleaseIdentifier() {
        return semanticRegistryReleaseIdentifier;
    }

    Optional<ApiObservationRequestProvenance> apiProvenance() {
        return apiProvenance;
    }
}

/** Package-owned inspection boundary used by later E3 context construction. */
final class EstablishedObservationRequestDetails {

    private EstablishedObservationRequestDetails() {
    }

    static ActiveRelease activeRelease(EstablishedObservationRequest request) {
        return require(request).activeRelease();
    }

    static MerchantScope merchantScope(EstablishedObservationRequest request) {
        return require(request).merchantScope();
    }

    static ObservationRequestBinding requestBinding(
            EstablishedObservationRequest request
    ) {
        return require(request).requestBinding();
    }

    static String semanticRegistryReleaseIdentifier(
            EstablishedObservationRequest request
    ) {
        return require(request).semanticRegistryReleaseIdentifier();
    }

    static Optional<ApiContractIdentity> apiContractIdentity(
            EstablishedObservationRequest request
    ) {
        return require(request).apiProvenance()
                .map(ApiObservationRequestProvenance::contractIdentity);
    }

    static Optional<ApiSurfaceClass> apiSurface(
            EstablishedObservationRequest request
    ) {
        return require(request).apiProvenance()
                .map(ApiObservationRequestProvenance::surface);
    }

    static Optional<ApiObservationRequestProvenance> apiProvenance(
            EstablishedObservationRequest request
    ) {
        return require(request).apiProvenance();
    }

    private static DefaultEstablishedObservationRequest require(
            EstablishedObservationRequest request
    ) {
        Objects.requireNonNull(request, "request");
        return (DefaultEstablishedObservationRequest) request;
    }
}
