package mainstreet.surface;

import grandrue.api.*;
import grandrue.application.MerchantScope;
import grandrue.runtime.AuthenticationException;
import grandrue.runtime.SessionTrustedExecutionContextEstablisher;
import mainstreet.runtime.*;
import java.time.Clock;
import java.util.Objects;

/** Trusted interactive MERCHANT query composition; no client principal or observation context is accepted. */
public final class MerchantBoundedQueryExecution {
    @FunctionalInterface
    public interface Acquisition {
        BoundedProjectionRead acquire(MerchantScope scope, EstablishedObservationRequest request);
    }
    /** Server-only handoff to an owner adapter; never deserialized from transport. */
    public record Selection(BoundedProjectionRead read, ProjectionServiceabilityResult serviceability,
                            ApiExposureResolution exposure) { }
    private final ObservationRequestEstablisher requests;
    private final SessionTrustedExecutionContextEstablisher authentication;
    private final AudienceObservationContextEstablisher contexts;
    private final ApiAudienceObservationContextBinder apiContexts;
    private final AudienceObservationAdmissionEvaluator admission;
    private final ProjectionServiceabilityEvaluationEngine serviceability;
    private final ExposureElementContractRegistrySnapshot exposures;
    private final ExposureRequirementEvaluatorBindingSnapshot requirements;
    private final MerchantExposureChoiceEvaluatorBindingSnapshot choices;
    private final Clock clock;

    public MerchantBoundedQueryExecution(ObservationRequestEstablisher requests,
            SessionTrustedExecutionContextEstablisher authentication, AudienceObservationContextEstablisher contexts,
            ApiAudienceObservationContextBinder apiContexts, AudienceObservationAdmissionEvaluator admission,
            ProjectionServiceabilityEvaluationEngine serviceability, ExposureElementContractRegistrySnapshot exposures,
            ExposureRequirementEvaluatorBindingSnapshot requirements, MerchantExposureChoiceEvaluatorBindingSnapshot choices,
            Clock clock) {
        this.requests = Objects.requireNonNull(requests);
        this.authentication = Objects.requireNonNull(authentication);
        this.contexts = Objects.requireNonNull(contexts);
        this.apiContexts = Objects.requireNonNull(apiContexts);
        this.admission = Objects.requireNonNull(admission);
        this.serviceability = Objects.requireNonNull(serviceability);
        this.exposures = Objects.requireNonNull(exposures);
        this.requirements = Objects.requireNonNull(requirements);
        this.choices = Objects.requireNonNull(choices);
        this.clock = Objects.requireNonNull(clock);
    }

    public Selection execute(ApiContractIdentity api, ApiTransportScopeEvidence evidence, String credential,
            Acquisition acquisition) {
        var established = requests.establishForApi(api, evidence);
        var request = established.establishedRequest().orElseThrow(() -> new ApiQueryUnavailableException(
                established.failure().orElseThrow() == ObservationRequestEstablishmentFailure.API_SCOPE_ESTABLISHMENT_FAILED
                        ? ApiProblemCategory.NOT_FOUND_OR_NOT_ACCESSIBLE : ApiProblemCategory.REPRESENTATION_UNAVAILABLE));
        if (EstablishedObservationRequestDetails.apiProvenance(request).orElseThrow().surface()
                != ApiSurfaceClass.MERCHANT_OPERATIONAL) throw new IllegalArgumentException("MERCHANT query required");
        var scope = EstablishedObservationRequestDetails.merchantScope(request);
        TrustedExecutionContext execution;
        try { execution = authentication.establish(scope, credential); }
        catch (AuthenticationException invalid) { throw new ApiQueryUnavailableException(ApiProblemCategory.UNAUTHENTICATED); }
        var context = contexts.establishMerchantInteractive(request, execution).context()
                .orElseThrow(() -> new ApiQueryUnavailableException(ApiProblemCategory.UNAUTHENTICATED));
        var apiContext = apiContexts.bind(context).context()
                .orElseThrow(() -> new ApiQueryUnavailableException(ApiProblemCategory.REPRESENTATION_UNAVAILABLE));
        if (!admission.evaluate(context).admitted())
            throw new ApiQueryUnavailableException(ApiProblemCategory.NOT_AUTHORISED);
        var read = Objects.requireNonNull(acquisition.acquire(scope, request));
        if (read.requestBinding() != EstablishedObservationRequestDetails.requestBinding(request))
            throw new IllegalStateException("Merchant acquisition returned another request");
        var p2 = serviceability.evaluate(ProjectionServiceabilityEvaluationRequest.forBoundedRead(read, clock.instant()));
        if (p2.outcome() == ProjectionServiceabilityOutcome.NOT_SERVICEABLE)
            throw new ApiQueryUnavailableException(ApiProblemCategory.REPRESENTATION_UNAVAILABLE);
        var admitted = admission.evaluate(context);
        if (!admitted.admitted()) throw new ApiQueryUnavailableException(ApiProblemCategory.NOT_AUTHORISED);
        var resolved = new ExposureResolver().resolve(context, admitted,
                read.fragments().stream().map(ProjectionMaterialFragment::candidateObservation).toList(),
                exposures, requirements, choices);
        var exposure = new ApiExposureResolutionBinder().bind(apiContext, admitted,
                read.semanticRegistryReleaseIdentifier(), resolved).resolution()
                .orElseThrow(() -> new ApiQueryUnavailableException(ApiProblemCategory.REPRESENTATION_UNAVAILABLE));
        return new Selection(read, p2, exposure);
    }
}

