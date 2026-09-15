package mainstreet.enquiry.delivery;

import mainstreet.api.*;
import mainstreet.enquiry.*;
import mainstreet.runtime.*;
import mainstreet.semantic.configuration.ConfigurationReleaseActivation;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import mainstreet.surface.*;
import java.time.Clock;
import java.util.*;

public final class MerchantEnquiryQuery {
    private final MerchantBoundedQueryExecution execution;
    private final EnquiryMerchantRepresentationProjectionReadPort material;
    private final Clock clock;
    public MerchantEnquiryQuery(MerchantBoundedQueryExecution execution,
            EnquiryMerchantRepresentationProjectionReadPort material, Clock clock) {
        this.execution = Objects.requireNonNull(execution);
        this.material = Objects.requireNonNull(material);
        this.clock = Objects.requireNonNull(clock);
    }
    public static MerchantEnquiryQuery create(MerchantEnquiryRouteScopeAuthority routes,
            ConfigurationReleaseActivation activation, SemanticRegistrySnapshot semantic, EnquirySubmissionStore submissions,
            SessionTrustedExecutionContextEstablisher authentication, AudienceObservationContextEstablisher contexts,
            AudienceObservationAdmissionEvaluator admission, ActorAuthorisationAuthority actors,
            MerchantEnquiryObservationPrivileges privileges, Clock clock) {
        var release = semantic.version();
        var apis = new ApiContractRegistrySnapshot(Set.of(MerchantEnquiryQueryContract.definition().registration()));
        var projections = EnquiryMerchantQueryProjectionPortfolio.contracts(release);
        var exposures = EnquiryMerchantExposureContractPortfolio.forRelease(release);
        var requests = new ObservationRequestEstablisher(apis,
                new ApiTransportScopeAuthorityRegistrySnapshot(List.of(routes)), activation, semantic, projections, exposures);
        var requirements = new ExposureRequirementEvaluatorBindingSnapshot(release, List.of(
                new ExposureRequirementEvaluatorBinding(EnquiryMerchantExposureReferences.OBSERVATION_REQUIREMENT,
                        new EnquiryMerchantExposureRequirementEvaluator(submissions, actors, privileges.byElement()))));
        var execution = new MerchantBoundedQueryExecution(requests, authentication, contexts,
                new ApiAudienceObservationContextBinder(apis), admission,
                new ProjectionServiceabilityEvaluationEngine(projections, EnquiryMerchantQueryProjectionPortfolio.evaluators(release)),
                exposures, requirements, new MerchantExposureChoiceEvaluatorBindingSnapshot(release, List.of()), clock);
        return new MerchantEnquiryQuery(execution, new AuthorityBackedEnquiryMerchantRepresentationProjectionReadPort(submissions), clock);
    }
    public Optional<MerchantEnquiryResponse> get(String locator, String identity, String credential) {
        if (locator == null || locator.isBlank() || identity == null || identity.isBlank())
            throw new ApiQueryUnavailableException(ApiProblemCategory.INVALID_REQUEST);
        if (credential == null || credential.isBlank() || credential.length() > 4096
                || !credential.matches("[A-Za-z0-9_-]+"))
            throw new ApiQueryUnavailableException(ApiProblemCategory.UNAUTHENTICATED);
        var selected = execution.execute(MerchantEnquiryQueryContract.definition().registration().identity(),
                new MerchantEnquiryRouteScopeAuthority.Route(locator), credential, (scope, request) -> {
                    var read = material.observe(scope, identity, clock.instant()).toBoundedRead(request);
                    if (read.fragments().isEmpty())
                        throw new ApiQueryUnavailableException(ApiProblemCategory.NOT_FOUND_OR_NOT_ACCESSIBLE);
                    return read;
                });
        return new MerchantEnquiryResponseAdapter().map(identity, selected.read(), selected.serviceability(), selected.exposure());
    }
}

