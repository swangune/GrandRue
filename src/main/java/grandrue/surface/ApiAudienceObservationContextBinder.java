package grandrue.surface;

import grandrue.api.ApiContractKind;
import grandrue.api.ApiContractRegistration;
import grandrue.api.ApiContractRegistrySnapshot;
import grandrue.api.ApiSurfaceClass;
import grandrue.application.MerchantScope;
import grandrue.runtime.TrustedExecutionContext;

import java.util.Objects;
import java.util.Optional;

/** Exact registry/provenance/scope/audience binder for API observation. */
public final class ApiAudienceObservationContextBinder {

    private final ApiContractRegistrySnapshot apiContracts;

    public ApiAudienceObservationContextBinder(
            ApiContractRegistrySnapshot apiContracts
    ) {
        this.apiContracts = Objects.requireNonNull(
                apiContracts,
                "apiContracts"
        );
    }

    public ApiAudienceObservationContextBindingResult bind(
            AudienceObservationContext context
    ) {
        Objects.requireNonNull(context, "context");
        EstablishedObservationRequest request =
                AudienceObservationContextDetails.request(context);
        Optional<ApiObservationRequestProvenance> optionalProvenance =
                EstablishedObservationRequestDetails.apiProvenance(request);
        if (optionalProvenance.isEmpty()) {
            return rejected(
                    ApiAudienceObservationContextBindingFailure
                            .API_PROVENANCE_MISSING
            );
        }

        ApiObservationRequestProvenance provenance =
                optionalProvenance.orElseThrow();
        Optional<ApiContractRegistration> optionalRegistration =
                apiContracts.contract(provenance.contractIdentity());
        if (optionalRegistration.isEmpty()) {
            return rejected(
                    ApiAudienceObservationContextBindingFailure
                            .API_CONTRACT_UNREGISTERED
            );
        }

        ApiContractRegistration registration =
                optionalRegistration.orElseThrow();
        if (registration.kind() != ApiContractKind.QUERY) {
            return rejected(
                    ApiAudienceObservationContextBindingFailure
                            .API_CONTRACT_NOT_QUERY
            );
        }
        if (!registration.scopeEstablishmentRuleReference().equals(
                provenance.scopeEstablishmentRuleReference()
        )) {
            return rejected(
                    ApiAudienceObservationContextBindingFailure
                            .API_PROVENANCE_MISMATCH
            );
        }
        if (registration.surface() != provenance.surface()) {
            return rejected(
                    ApiAudienceObservationContextBindingFailure
                            .API_SURFACE_MISMATCH
            );
        }

        MerchantScope requestScope =
                EstablishedObservationRequestDetails.merchantScope(request);
        Optional<TrustedExecutionContext> executionContext =
                AudienceObservationContextDetails.executionContext(context);
        if (executionContext.isPresent()
                && !requestScope.equals(
                        executionContext.orElseThrow().merchantScope()
                )) {
            return rejected(
                    ApiAudienceObservationContextBindingFailure
                            .API_SCOPE_MISMATCH
            );
        }

        if (!permits(
                registration.surface(),
                AudienceObservationContextDetails.subject(context)
        )) {
            return rejected(
                    ApiAudienceObservationContextBindingFailure
                            .API_AUDIENCE_MISMATCH
            );
        }

        return ApiAudienceObservationContextBindingResults.bound(
                new DefaultApiAudienceObservationContext(
                        registration.identity(),
                        registration.surface(),
                        context
                )
        );
    }

    private static boolean permits(
            ApiSurfaceClass surface,
            ObservationSubject subject
    ) {
        return switch (surface) {
            case PUBLIC -> subject instanceof PublicObservationSubject;
            case CUSTOMER_CONTEXTUAL ->
                    subject instanceof CustomerPrincipalObservationSubject
                            || subject
                            instanceof CustomerContextualObservationSubject;
            case MERCHANT_OPERATIONAL ->
                    subject instanceof MerchantInteractiveObservationSubject;
            case PLATFORM_IDENTITY_BOOTSTRAP,
                 PLATFORM_ADMINISTRATIVE,
                 INTEGRATION_INGRESS -> false;
        };
    }

    private static ApiAudienceObservationContextBindingResult rejected(
            ApiAudienceObservationContextBindingFailure failure
    ) {
        return ApiAudienceObservationContextBindingResults.rejected(failure);
    }
}
