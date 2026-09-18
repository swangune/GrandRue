package grandrue.surface;

import grandrue.api.*;
import grandrue.application.MerchantScope;
import java.time.Clock;
import java.util.List;
import java.util.Objects;

/**
 * Trusted PUBLIC bounded-query composition. Owner acquisition receives scope only inside this
 * request-bound callback; no public request-unwrapping API or transport dispatcher is introduced.
 */
public final class PublicBoundedQueryExecution {
    /** Server-only projection under the exact release captured by request establishment. */
    @FunctionalInterface
    public interface SelectedProjection<T> {
        T project(grandrue.semantic.executable.ExecutableMerchantModel model,
                  StaticSurfaceContributionCatalogue catalogue, Selection selection);
    }
    @FunctionalInterface
    public interface Acquisition {
        Material acquire(MerchantScope scope, EstablishedObservationRequest request);
    }
    public record Material(BoundedProjectionRead read, List<ObservationContributionConstructor> contributions) {
        public Material {
            Objects.requireNonNull(read, "read");
            contributions = List.copyOf(contributions);
        }
    }
    /** Internal server handoff to an owner response adapter; never a transport DTO. */
    public record Selection(BoundedProjectionRead read, ProjectionServiceabilityResult serviceability,
                            ApiExposureResolution exposure) { }

    private final ObservationRequestEstablisher requests;
    private final AudienceObservationContextEstablisher contexts;
    private final ApiAudienceObservationContextBinder apiContexts;
    private final AudienceObservationAdmissionEvaluator admission;
    private final ProjectionServiceabilityEvaluationEngine serviceability;
    private final ExposureElementContractRegistrySnapshot exposures;
    private final ExposureRequirementEvaluatorBindingSnapshot requirements;
    private final MerchantExposureChoiceEvaluatorBindingSnapshot choices;
    private final Clock clock;

    public PublicBoundedQueryExecution(ObservationRequestEstablisher requests,
            AudienceObservationContextEstablisher contexts, ApiAudienceObservationContextBinder apiContexts,
            AudienceObservationAdmissionEvaluator admission, ProjectionServiceabilityEvaluationEngine serviceability,
            ExposureElementContractRegistrySnapshot exposures, ExposureRequirementEvaluatorBindingSnapshot requirements,
            MerchantExposureChoiceEvaluatorBindingSnapshot choices, Clock clock) {
        this.requests = Objects.requireNonNull(requests);
        this.contexts = Objects.requireNonNull(contexts);
        this.apiContexts = Objects.requireNonNull(apiContexts);
        this.admission = Objects.requireNonNull(admission);
        this.serviceability = Objects.requireNonNull(serviceability);
        this.exposures = Objects.requireNonNull(exposures);
        this.requirements = Objects.requireNonNull(requirements);
        this.choices = Objects.requireNonNull(choices);
        this.clock = Objects.requireNonNull(clock);
    }

    public Selection execute(ApiContractIdentity api, ApiTransportScopeEvidence evidence, Acquisition acquisition) {
        return execute(api, evidence, acquisition, (model, catalogue, selection) -> selection);
    }

    public <T> T execute(ApiContractIdentity api, ApiTransportScopeEvidence evidence, Acquisition acquisition,
                         SelectedProjection<T> projection) {
        Objects.requireNonNull(projection);
        var establishment = requests.establishForApi(api, evidence);
        if (establishment.establishedRequest().isEmpty()) {
            throw new ApiQueryUnavailableException(establishment.failure().orElseThrow()
                    == ObservationRequestEstablishmentFailure.API_SCOPE_ESTABLISHMENT_FAILED
                    ? ApiProblemCategory.NOT_FOUND_OR_NOT_ACCESSIBLE : ApiProblemCategory.REPRESENTATION_UNAVAILABLE);
        }
        var request = establishment.establishedRequest().orElseThrow();
        if (EstablishedObservationRequestDetails.apiProvenance(request).orElseThrow().surface()
                != ApiSurfaceClass.PUBLIC) throw new IllegalArgumentException("PUBLIC query required");
        var material = Objects.requireNonNull(acquisition.acquire(
                EstablishedObservationRequestDetails.merchantScope(request), request));
        var read = material.read();
        if (read.requestBinding() != EstablishedObservationRequestDetails.requestBinding(request)) {
            throw new IllegalStateException("Query acquisition returned another request");
        }
        var contributions = material.contributions().stream().map(constructor ->
                new ObservationContributionConstructionBoundary().construct(request, constructor)).toList();
        var context = contexts.establishPublic(request, contributions).context()
                .orElseThrow(() -> new ApiQueryUnavailableException(ApiProblemCategory.REPRESENTATION_UNAVAILABLE));
        var apiContext = apiContexts.bind(context).context()
                .orElseThrow(() -> new ApiQueryUnavailableException(ApiProblemCategory.REPRESENTATION_UNAVAILABLE));
        var admitted = admission.evaluate(context);
        if (!admitted.admitted()) throw new ApiQueryUnavailableException(ApiProblemCategory.NOT_AUTHORISED);
        var p2 = serviceability.evaluate(ProjectionServiceabilityEvaluationRequest.forBoundedRead(read, clock.instant()));
        if (p2.outcome() == ProjectionServiceabilityOutcome.NOT_SERVICEABLE) {
            throw new ApiQueryUnavailableException(ApiProblemCategory.REPRESENTATION_UNAVAILABLE);
        }
        var resolved = new ExposureResolver().resolve(context, admitted,
                read.fragments().stream().map(ProjectionMaterialFragment::candidateObservation).toList(),
                exposures, requirements, choices);
        var exposure = new ApiExposureResolutionBinder().bind(apiContext, admitted,
                read.semanticRegistryReleaseIdentifier(), resolved).resolution()
                .orElseThrow(() -> new ApiQueryUnavailableException(ApiProblemCategory.REPRESENTATION_UNAVAILABLE));
        var captured = EstablishedObservationRequestDetails.activeRelease(request).release().resolvedPackage();
        return projection.project(captured.executableSemanticModel(), captured.staticSurfaceContributionCatalogue(),
                new Selection(read, p2, exposure));
    }
}
