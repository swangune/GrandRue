package mainstreet.surface;

import grandrue.api.ApiContractIdentity;
import grandrue.api.ApiContractKind;
import grandrue.api.ApiContractRegistration;
import grandrue.api.ApiContractRegistrySnapshot;
import grandrue.api.ApiTransportScope;
import grandrue.api.ApiTransportScopeAuthorityRegistrySnapshot;
import grandrue.api.ApiTransportScopeEvidence;
import grandrue.api.MerchantApiTransportScope;
import grandrue.application.MerchantScope;
import mainstreet.runtime.TrustedExecutionContext;
import mainstreet.semantic.configuration.ActiveRelease;
import mainstreet.semantic.configuration.ConfigurationReleaseActivation;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;

import java.util.Objects;
import java.util.Optional;

/**
 * Trusted E3 boundary that establishes exact request scope and release
 * provenance without accepting caller scope or release authority.
 */
public final class ObservationRequestEstablisher {

    private final ApiContractRegistrySnapshot apiContracts;
    private final ApiTransportScopeAuthorityRegistrySnapshot scopeAuthorities;
    private final ConfigurationReleaseActivation configurationActivation;
    private final SemanticRegistrySnapshot servingSemanticRegistry;
    private final ProjectionContractRegistrySnapshot projectionContracts;
    private final ExposureElementContractRegistrySnapshot exposureContracts;

    public ObservationRequestEstablisher(
            ApiContractRegistrySnapshot apiContracts,
            ApiTransportScopeAuthorityRegistrySnapshot scopeAuthorities,
            ConfigurationReleaseActivation configurationActivation,
            SemanticRegistrySnapshot servingSemanticRegistry,
            ProjectionContractRegistrySnapshot projectionContracts,
            ExposureElementContractRegistrySnapshot exposureContracts
    ) {
        this.apiContracts = Objects.requireNonNull(
                apiContracts,
                "apiContracts"
        );
        this.scopeAuthorities = Objects.requireNonNull(
                scopeAuthorities,
                "scopeAuthorities"
        );
        this.configurationActivation = Objects.requireNonNull(
                configurationActivation,
                "configurationActivation"
        );
        this.servingSemanticRegistry = Objects.requireNonNull(
                servingSemanticRegistry,
                "servingSemanticRegistry"
        );
        this.projectionContracts = Objects.requireNonNull(
                projectionContracts,
                "projectionContracts"
        );
        this.exposureContracts = Objects.requireNonNull(
                exposureContracts,
                "exposureContracts"
        );
    }

    public ObservationRequestEstablishmentResult establishForApi(
            ApiContractIdentity contractIdentity,
            ApiTransportScopeEvidence transportEvidence
    ) {
        Objects.requireNonNull(contractIdentity, "contractIdentity");
        Objects.requireNonNull(transportEvidence, "transportEvidence");

        Optional<ApiContractRegistration> resolved =
                apiContracts.contract(contractIdentity);
        if (resolved.isEmpty()) {
            return rejected(
                    ObservationRequestEstablishmentFailure
                            .API_CONTRACT_UNREGISTERED
            );
        }

        ApiContractRegistration registration = resolved.orElseThrow();
        if (registration.kind() != ApiContractKind.QUERY) {
            return rejected(
                    ObservationRequestEstablishmentFailure
                            .API_CONTRACT_NOT_QUERY
            );
        }

        ApiTransportScope establishedScope;
        try {
            establishedScope = scopeAuthorities.establish(
                    registration,
                    transportEvidence
            );
        } catch (RuntimeException failure) {
            return rejected(
                    ObservationRequestEstablishmentFailure
                            .API_SCOPE_ESTABLISHMENT_FAILED
            );
        }

        if (!(establishedScope instanceof MerchantApiTransportScope merchant)) {
            return rejected(
                    ObservationRequestEstablishmentFailure
                            .API_SCOPE_NOT_MERCHANT
            );
        }

        ApiObservationRequestProvenance provenance =
                new ApiObservationRequestProvenance(
                        registration.identity(),
                        registration.surface(),
                        registration.scopeEstablishmentRuleReference()
                );
        return establishCurrent(
                merchant.merchantScope(),
                Optional.of(provenance)
        );
    }

    public ObservationRequestEstablishmentResult establishForExecution(
            TrustedExecutionContext executionContext
    ) {
        Objects.requireNonNull(executionContext, "executionContext");
        return establishCurrent(
                executionContext.merchantScope(),
                Optional.empty()
        );
    }

    private ObservationRequestEstablishmentResult establishCurrent(
            MerchantScope merchantScope,
            Optional<ApiObservationRequestProvenance> apiProvenance
    ) {
        ActiveRelease activeRelease;
        try {
            Optional<ActiveRelease> current = configurationActivation.current(
                    merchantScope.merchantIdentifier()
            );
            if (current == null || current.isEmpty()) {
                return rejected(
                        ObservationRequestEstablishmentFailure
                                .ACTIVE_CONFIGURATION_UNRESOLVED
                );
            }
            activeRelease = current.orElseThrow();
        } catch (RuntimeException failure) {
            return rejected(
                    ObservationRequestEstablishmentFailure
                            .ACTIVE_CONFIGURATION_UNRESOLVED
            );
        }

        if (!activeRelease.merchantIdentifier().equals(
                merchantScope.merchantIdentifier()
        )) {
            return rejected(
                    ObservationRequestEstablishmentFailure
                            .ACTIVE_CONFIGURATION_SCOPE_MISMATCH
            );
        }

        String semanticRelease =
                activeRelease.release().semanticRegistryVersion();
        if (!servingSemanticRegistry.version().equals(semanticRelease)) {
            return rejected(
                    ObservationRequestEstablishmentFailure
                            .SEMANTIC_RELEASE_NOT_MATERIALISED
            );
        }
        if (!projectionContracts.semanticRegistryReleaseIdentifier()
                .equals(semanticRelease)) {
            return rejected(
                    ObservationRequestEstablishmentFailure
                            .PROJECTION_REGISTRY_RELEASE_MISMATCH
            );
        }
        if (!exposureContracts.semanticRegistryReleaseIdentifier()
                .equals(semanticRelease)) {
            return rejected(
                    ObservationRequestEstablishmentFailure
                            .EXPOSURE_REGISTRY_RELEASE_MISMATCH
            );
        }

        return ObservationRequestEstablishmentResults.established(
                new DefaultEstablishedObservationRequest(
                        activeRelease,
                        merchantScope,
                        semanticRelease,
                        apiProvenance
                )
        );
    }

    private static ObservationRequestEstablishmentResult rejected(
            ObservationRequestEstablishmentFailure failure
    ) {
        return ObservationRequestEstablishmentResults.rejected(failure);
    }
}
