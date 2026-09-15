package mainstreet.publication.delivery;

import mainstreet.api.ApiSurfaceClass;
import mainstreet.publication.*;
import mainstreet.surface.*;

import java.util.Objects;
import java.util.Optional;

/**
 * T1A bounded response mapping. Inputs come from trusted server composition, never deserialized
 * client authority. Generic P2/E4 selection precedes all value mapping. No repository or policy
 * dependency is available here; the adapter cannot refresh or authorize owner material.
 */
public final class PublicOpportunityQueryResponseAdapter {
    public Optional<PublicOpportunityResponse> map(
            String requestedOpportunityIdentity, BoundedProjectionRead read,
            ProjectionServiceabilityResult serviceability, ApiExposureResolution exposure) {
        Objects.requireNonNull(requestedOpportunityIdentity, "requestedOpportunityIdentity");
        Objects.requireNonNull(read, "read");
        Objects.requireNonNull(serviceability, "serviceability");
        Objects.requireNonNull(exposure, "exposure");
        var expectedCandidate = OpportunityPublicExposureReferences.candidate(requestedOpportunityIdentity);
        if (exposure.surface() != ApiSurfaceClass.PUBLIC
                || !PublicOpportunityQueryContract.definition().registration().identity()
                        .equals(exposure.contractIdentity())
                || !OpportunityPublicRepresentationProjectionReferences.PROJECTION_CONTRACT
                        .equals(read.contractIdentity())
                || !OpportunityPublicRepresentationProjectionReferences.READ_USE.equals(read.readUseIdentity())) {
            throw new IllegalArgumentException("Public Opportunity query contract affinity mismatch");
        }
        if (read.fragments().size() > 1 || read.fragments().stream().anyMatch(fragment ->
                !(fragment instanceof OpportunityPublicRepresentationProjectionFragment)
                        || !expectedCandidate.equals(fragment.candidateObservation()))) {
            throw new IllegalArgumentException("Public Opportunity query requires one exact resource");
        }

        var assembly = new PublicCustomerProjectionAssemblyService().assemble(read, serviceability, exposure);
        if (assembly.selectedFragments().isEmpty()) return Optional.empty();
        var material = ((OpportunityPublicRepresentationProjectionFragment)
                assembly.selectedFragments().getFirst()).representation();
        return Optional.of(new PublicOpportunityResponse(requestedOpportunityIdentity, material.title(),
                material.description(), material.eligibilityInformation(), material.externalProviderName(),
                material.sourceName(), material.externalLinks().stream().map(link ->
                        new PublicOpportunityResponse.ExternalLink(link.role().name(),
                                link.uri().toString(), link.label())).toList(),
                material.applicationsOpen().map(PublicOpportunityQueryResponseAdapter::boundary),
                material.applicationDeadline().map(PublicOpportunityQueryResponseAdapter::boundary)));
    }

    private static PublicOpportunityResponse.TemporalBoundary boundary(OpportunityTemporalBoundary boundary) {
        return switch (boundary) {
            case OpportunityCalendarDateBoundary date -> new PublicOpportunityResponse.TemporalBoundary(
                    "CALENDAR_DATE", date.date().toString(), Optional.of(date.zoneId().getId()));
            case OpportunityExactInstantBoundary instant -> new PublicOpportunityResponse.TemporalBoundary(
                    "EXACT_INSTANT", instant.instant().toString(), Optional.empty());
        };
    }
}
