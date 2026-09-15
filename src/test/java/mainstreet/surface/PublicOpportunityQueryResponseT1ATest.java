package mainstreet.surface;

import mainstreet.api.*;
import mainstreet.application.MerchantScope;
import mainstreet.publication.*;
import mainstreet.publication.delivery.*;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import java.net.URI;
import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublicOpportunityQueryResponseT1ATest {
    static final MerchantScope SCOPE = new MerchantScope("merchant-a");
    static final String RELEASE = "semantic-registry-1.0";
    static final Instant NOW = Instant.parse("2026-09-05T12:00:00Z");

    @Test
    void registers_public_single_opportunity_query_over_p5() {
        var definition = PublicOpportunityQueryContract.definition();
        var registration = definition.registration();
        assertEquals(new ApiContractIdentity("publication", "public-opportunity-query"), registration.identity());
        assertEquals(ApiSurfaceClass.PUBLIC, registration.surface());
        assertEquals(ApiContractKind.QUERY, registration.kind());
        assertEquals(new ApiOwnerContractReference("publication", "public-opportunity-representation"),
                registration.ownerContractReference());
        assertEquals(Optional.empty(), definition.relationshipRequirementReference());
        assertEquals(Set.of("publication/public-opportunity-representation"), definition.exposureContractReferences());
        assertEquals("publication/public-opportunity-single-resource", definition.paginationBoundRuleReference());
        assertEquals("publication/public-opportunity-no-filter-or-sort", definition.filterSortContractReference());
        assertEquals(registration, new ApiContractRegistrySnapshot(Set.of(registration))
                .contract(registration.identity()).orElseThrow());
    }

    @Test
    void maps_only_selected_published_material_without_reloading_owner_state() {
        var f = new Fixture();
        var read = f.read();
        var exposure = f.exposure(read);
        clearInvocations(f.owner);
        var response = new PublicOpportunityQueryResponseAdapter()
                .map("O1", read, serviceability(read, true), exposure).orElseThrow();
        assertEquals("Published title", response.title());
        assertEquals("O1", response.opportunityIdentity());
        assertEquals(Optional.of("Description"), response.description());
        assertEquals("OFFICIAL_APPLICATION", response.externalLinks().getFirst().role());
        assertEquals("https://example.com/apply", response.externalLinks().getFirst().uri());
        assertEquals("CALENDAR_DATE", response.applicationsOpen().orElseThrow().kind());
        assertEquals("2026-10-25", response.applicationsOpen().orElseThrow().value());
        assertEquals(Optional.of("Europe/London"), response.applicationsOpen().orElseThrow().zone());
        assertEquals("EXACT_INSTANT", response.applicationDeadline().orElseThrow().kind());
        assertEquals("2026-11-01T10:15:30.123456789Z", response.applicationDeadline().orElseThrow().value());
        assertTrue(response.applicationDeadline().orElseThrow().zone().isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> response.externalLinks().clear());
        verifyNoInteractions(f.owner);
    }

    @Test
    void serialized_response_excludes_internal_revision_scope_and_affinity_evidence() {
        var f = new Fixture();
        var read = f.read();
        var response = new PublicOpportunityQueryResponseAdapter()
                .map("O1", read, serviceability(read, true), f.exposure(read)).orElseThrow();
        var json = JsonMapper.builder().findAndAddModules().build().writeValueAsString(response);
        assertTrue(json.contains("Published title"));
        assertTrue(json.contains("Europe/London"));
        for (var forbidden : List.of("secret-revision", "merchant-a", "publishFrom", "publishUntil",
                "sourceAffinities", "sourceEvidence", "requestBinding", "schemaReference", "Edited title")) {
            assertFalse(json.contains(forbidden), forbidden);
        }
    }

    @Test
    void withdrawal_after_acquisition_yields_no_response_and_does_not_refresh_values() {
        var f = new Fixture();
        var read = f.read();
        when(f.owner.current(SCOPE, "O1")).thenReturn(Optional.of(new OpportunityPublicationState(
                SCOPE, "O1", "edited-revision", PublicationLifecycle.WITHDRAWN, Optional.of("secret-revision"))));
        var exposure = f.exposure(read);
        assertTrue(exposure.exposedElements().members().isEmpty());
        clearInvocations(f.owner);
        assertTrue(new PublicOpportunityQueryResponseAdapter()
                .map("O1", read, serviceability(read, true), exposure).isEmpty());
        verifyNoInteractions(f.owner);
    }

    @Test
    void positive_exposure_cannot_rescue_nonserviceable_material() {
        var f = new Fixture();
        var read = f.read();
        assertThrows(ProjectionFragmentSelectionStructuralException.class, () -> new PublicOpportunityQueryResponseAdapter()
                .map("O1", read, serviceability(read, false), f.exposure(read)));
    }

    @Test
    void rejects_other_read_request_and_requested_resource() {
        var f = new Fixture();
        var read = f.read();
        var adapter = new PublicOpportunityQueryResponseAdapter();
        assertThrows(ProjectionFragmentSelectionStructuralException.class, () -> adapter.map("O1", read,
                serviceability(f.read(), true), f.exposure(read)));
        var other = new Fixture();
        assertThrows(ProjectionFragmentSelectionStructuralException.class, () -> adapter.map("O1", read,
                serviceability(read, true), other.exposure(other.read())));
        assertThrows(IllegalArgumentException.class, () -> adapter.map("O2", read,
                serviceability(read, true), f.exposure(read)));
    }

    @Test
    void rejects_wrong_api_contract_surface_and_projection_even_with_empty_membership() {
        var f = new Fixture();
        var read = f.read();
        var adapter = new PublicOpportunityQueryResponseAdapter();
        var context = new DefaultAudienceObservationContext(f.request, new PublicObservationSubject());
        var admission = admission(f.request);
        var emptyExposure = new ApiExposureResolutionBinder().bind(new DefaultApiAudienceObservationContext(
                PublicOpportunityQueryContract.definition().registration().identity(), ApiSurfaceClass.PUBLIC, context),
                admission, RELEASE, List.of()).resolution().orElseThrow();
        var wrongApi = new DefaultApiExposureResolution(new ApiContractIdentity("publication", "other-query"),
                ApiSurfaceClass.PUBLIC, ((DefaultApiExposureResolution) emptyExposure).exposedElementDetails());
        assertThrows(IllegalArgumentException.class, () -> adapter.map("O1", read,
                serviceability(read, true), wrongApi));
        // Opaque construction is package-owned: this adversarial fixture tests the adapter's surface guard.
        var wrongSurface = new DefaultApiExposureResolution(PublicOpportunityQueryContract.definition()
                .registration().identity(), ApiSurfaceClass.CUSTOMER_CONTEXTUAL,
                ((DefaultApiExposureResolution) emptyExposure).exposedElementDetails());
        assertThrows(IllegalArgumentException.class, () -> adapter.map("O1", read,
                serviceability(read, true), wrongSurface));
        var otherProjection = new BoundedProjectionRead(f.request,
                new ProjectionContractIdentity("publication", "other-projection"), read.readUseIdentity(),
                read.sourceEvidence(), read.fragments());
        assertThrows(IllegalArgumentException.class, () -> adapter.map("O1", otherProjection,
                serviceability(otherProjection, true), f.exposure(read)));
    }

    @Test
    void empty_bounded_read_produces_no_material() {
        var f = new Fixture();
        when(f.owner.current(SCOPE, "O1")).thenReturn(Optional.empty());
        var read = f.read();
        assertTrue(read.fragments().isEmpty());
        // P2 nonserviceability is preserved; delivery must map it rather than returning a successful DTO.
        assertThrows(ProjectionFragmentSelectionStructuralException.class, () -> new PublicOpportunityQueryResponseAdapter()
                .map("O1", read, serviceability(read, false), f.exposure(read)));
    }

    static ProjectionServiceabilityResult serviceability(BoundedProjectionRead read, boolean serviceable) {
        return new ProjectionServiceabilityResult(RELEASE, read.contractIdentity(), read.readUseIdentity(), NOW,
                serviceable ? ProjectionServiceabilityOutcome.FULLY_SERVICEABLE : ProjectionServiceabilityOutcome.NOT_SERVICEABLE,
                serviceable ? Set.of() : Set.of(ProjectionServiceabilityReasonCode.POLICY_EVALUATOR_UNRESOLVED),
                Set.of(), read.sourceEvidence(), Set.of(), Set.of(), Optional.of(read.binding()));
    }

    static AudienceObservationAdmissionResult admission(EstablishedObservationRequest request) {
        return AudienceObservationAdmissionResults.admitted(new DefaultAudienceObservationInvocationBinding(
                EstablishedObservationRequestDetails.requestBinding(request)), NOW);
    }

    static class Fixture {
        final OpportunityPublicationStateAuthority owner = mock(OpportunityPublicationStateAuthority.class);
        final EstablishedObservationRequest request = new DefaultEstablishedObservationRequest(
                TestReleases.activeRelease(SCOPE.merchantIdentifier(), RELEASE), SCOPE, RELEASE,
                Optional.of(new ApiObservationRequestProvenance(
                        PublicOpportunityQueryContract.definition().registration().identity(),
                        ApiSurfaceClass.PUBLIC, PublicOpportunityQueryContract.definition()
                                .registration().scopeEstablishmentRuleReference())));

        Fixture() {
            when(owner.current(SCOPE, "O1")).thenReturn(Optional.of(new OpportunityPublicationState(
                    SCOPE, "O1", "edited-revision", PublicationLifecycle.PUBLISHED, Optional.of("secret-revision"))));
            when(owner.revision(SCOPE, "O1", "secret-revision")).thenReturn(Optional.of(
                    new OpportunityPublicationMaterialRevision(SCOPE, "O1", "secret-revision", "Published title",
                            Optional.of("Description"), Optional.of("Eligibility"), Optional.of("Provider"),
                            Optional.of("Source"), List.of(new OpportunityExternalLink(
                                    OpportunityExternalLinkRole.OFFICIAL_APPLICATION, URI.create("https://example.com/apply"),
                                    Optional.of("Apply"))),
                            Optional.of(new OpportunityCalendarDateBoundary(LocalDate.parse("2026-10-25"),
                                    ZoneId.of("Europe/London"))),
                            Optional.of(new OpportunityExactInstantBoundary(Instant.parse("2026-11-01T10:15:30.123456789Z"))),
                            Optional.empty(), Optional.empty())));
        }

        BoundedProjectionRead read() {
            return new AuthorityBackedOpportunityPublicRepresentationProjectionReadPort(owner)
                    .observe(SCOPE, "O1", NOW).toBoundedRead(request);
        }

        ApiExposureResolution exposure(BoundedProjectionRead read) {
            var affinity = new ObservationContributionConstructionBoundary().construct(request,
                    OpportunityMaterialAffinityObservationContributionRegistration.constructor(read));
            var context = new DefaultAudienceObservationContext(request, new PublicObservationSubject(), Set.of(affinity));
            var admission = admission(request);
            var resolved = new ExposureResolver().resolve(context, admission,
                    read.fragments().stream().map(ProjectionMaterialFragment::candidateObservation).toList(),
                    OpportunityPublicExposureContractPortfolio.forRelease(RELEASE),
                    new ExposureRequirementEvaluatorBindingSnapshot(RELEASE, List.of(
                            new ExposureRequirementEvaluatorBinding(OpportunityPublicExposureReferences.EXPOSURE_WINDOW_REQUIREMENT,
                                    new OpportunityExposureWindowRequirementEvaluator(
                                            new AuthorityBackedOpportunityPublicExposureReadPort(owner))))),
                    new MerchantExposureChoiceEvaluatorBindingSnapshot(RELEASE, List.of()));
            return new ApiExposureResolutionBinder().bind(new DefaultApiAudienceObservationContext(
                    PublicOpportunityQueryContract.definition().registration().identity(), ApiSurfaceClass.PUBLIC, context),
                    admission, RELEASE, resolved).resolution().orElseThrow();
        }
    }
}
