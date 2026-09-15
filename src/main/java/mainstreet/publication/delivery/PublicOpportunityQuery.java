package mainstreet.publication.delivery;

import mainstreet.api.*;
import mainstreet.publication.*;
import mainstreet.semantic.configuration.ConfigurationReleaseActivation;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import mainstreet.surface.*;
import java.time.Clock;
import java.util.*;

/** Concrete owner composition of the existing trusted query, P5, P2 and P4/E4 boundaries. */
public final class PublicOpportunityQuery {
    private final PublicBoundedQueryExecution execution;
    private final OpportunityPublicRepresentationProjectionReadPort material;
    private final Clock clock;
    private final ApiContractIdentity contract;

    public PublicOpportunityQuery(PublicBoundedQueryExecution execution,
            OpportunityPublicRepresentationProjectionReadPort material, Clock clock) {
        this(execution, material, clock, PublicOpportunityQueryContract.definition().registration().identity());
    }

    private PublicOpportunityQuery(PublicBoundedQueryExecution execution,
            OpportunityPublicRepresentationProjectionReadPort material, Clock clock, ApiContractIdentity contract) {
        this.execution = Objects.requireNonNull(execution);
        this.material = Objects.requireNonNull(material);
        this.clock = Objects.requireNonNull(clock);
        this.contract = Objects.requireNonNull(contract);
    }

    public static PublicOpportunityQuery create(PublicOpportunityRouteScopeAuthority routes,
            ConfigurationReleaseActivation activation, SemanticRegistrySnapshot semantic,
            OpportunityPublicationStateAuthority owner, AudienceObservationContextEstablisher contexts,
            AudienceObservationAdmissionEvaluator admission, Clock clock) {
        return create(routes, activation, semantic, owner, contexts, admission, clock,
                PublicOpportunityQueryContract.definition());
    }

    /** Owner composition may register a distinct projection of the same exact bounded material. */
    public static PublicOpportunityQuery create(PublicOpportunityRouteScopeAuthority routes,
            ConfigurationReleaseActivation activation, SemanticRegistrySnapshot semantic,
            OpportunityPublicationStateAuthority owner, AudienceObservationContextEstablisher contexts,
            AudienceObservationAdmissionEvaluator admission, Clock clock, ApiQueryContractDefinition contract) {
        var release = semantic.version();
        var apis = new ApiContractRegistrySnapshot(Set.of(contract.registration()));
        var projections = OpportunityPublicQueryProjectionPortfolio.contracts(release);
        var exposures = OpportunityPublicExposureContractPortfolio.forRelease(release);
        var requests = new ObservationRequestEstablisher(apis,
                new ApiTransportScopeAuthorityRegistrySnapshot(List.of(routes)), activation, semantic, projections, exposures);
        var requirements = new ExposureRequirementEvaluatorBindingSnapshot(release, List.of(
                new ExposureRequirementEvaluatorBinding(OpportunityPublicExposureReferences.EXPOSURE_WINDOW_REQUIREMENT,
                        new OpportunityExposureWindowRequirementEvaluator(new AuthorityBackedOpportunityPublicExposureReadPort(owner)))));
        var execution = new PublicBoundedQueryExecution(requests, contexts, new ApiAudienceObservationContextBinder(apis),
                admission, new ProjectionServiceabilityEvaluationEngine(projections,
                        OpportunityPublicQueryProjectionPortfolio.evaluators(release)), exposures, requirements,
                new MerchantExposureChoiceEvaluatorBindingSnapshot(release, List.of()), clock);
        return new PublicOpportunityQuery(execution,
                new AuthorityBackedOpportunityPublicRepresentationProjectionReadPort(owner), clock, contract.registration().identity());
    }

    public Optional<PublicOpportunityResponse> get(String locator, String opportunity) {
        return project(locator, opportunity, (model, catalogue, selection) ->
                new PublicOpportunityQueryResponseAdapter().map(opportunity, selection.read(),
                        selection.serviceability(), selection.exposure()));
    }

    public <T> T project(String locator, String opportunity, PublicBoundedQueryExecution.SelectedProjection<T> projection) {
        if (locator == null || locator.isBlank() || opportunity == null || opportunity.isBlank()) {
            throw new ApiQueryUnavailableException(ApiProblemCategory.INVALID_REQUEST);
        }
        return execution.execute(contract,
                new PublicOpportunityRouteScopeAuthority.Route(locator), (scope, request) -> {
                    var read = material.observe(scope, opportunity, clock.instant()).toBoundedRead(request);
                    if (read.fragments().isEmpty()) {
                        throw new ApiQueryUnavailableException(ApiProblemCategory.NOT_FOUND_OR_NOT_ACCESSIBLE);
                    }
                    return new PublicBoundedQueryExecution.Material(read, List.of(
                            OpportunityMaterialAffinityObservationContributionRegistration.constructor(read)));
                }, projection);
    }
}
