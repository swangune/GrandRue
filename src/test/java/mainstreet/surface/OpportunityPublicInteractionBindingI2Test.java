package mainstreet.surface;

import mainstreet.api.ApiContractIdentity;
import mainstreet.api.ApiSurfaceClass;
import mainstreet.application.MerchantScope;
import mainstreet.publication.*;
import mainstreet.semantic.compiler.ConfigurationCompiler;
import mainstreet.semantic.configuration.*;
import mainstreet.semantic.executable.ExecutableMerchantModel;
import mainstreet.semantic.registry.*;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** I2 composition proof: MS-PROT-049 v1.4 §§7–10, 15; MS-PROT-046 v1.2 §§16–20. */
class OpportunityPublicInteractionBindingI2Test {
    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final String RELEASE = "semantic-registry-1.0";
    private static final Instant NOW = Instant.parse("2026-09-05T12:00:00Z");
    private static final ApiContractIdentity API =
            new ApiContractIdentity("publication", "public-opportunity-query");
    private static final SurfaceContributionIdentity ENQUIRY =
            new SurfaceContributionIdentity("enquiry", "send-enquiry");

    @Test
    void concrete_source_binds_the_same_published_material_under_the_compiled_context() {
        var f = new Fixture();
        var read = f.read();
        var assembly = f.assemble(read, NOW, true);
        var selected = assertInstanceOf(OpportunityPublicRepresentationProjectionFragment.class,
                assembly.selectedFragments().getFirst());
        assertSame(read.fragments().getFirst(), selected);
        assertEquals("Published title", selected.representation().title());
        clearInvocations(f.authority);

        var bindings = f.project(read, assembly, f.sources("opportunity-1"), catalogue());

        assertEquals(List.of(new PublicInteractionBinding(MERCHANT, RELEASE,
                f.model.modelIdentifier(), f.model.version(), ENQUIRY, "send-enquiry",
                OpportunityPublicExposureReferences.candidate("opportunity-1"),
                f.definition.sourceIdentity(), Optional.empty())), bindings);
        // S3 asks the real source to establish identity, but never reacquires public values.
        verify(f.authority).current(MERCHANT, "opportunity-1");
        verifyNoMoreInteractions(f.authority);
    }

    @Test
    void exposed_opportunity_does_not_bind_without_source_or_applicable_interaction() {
        var f = new Fixture();
        var read = f.read();
        var assembly = f.assemble(read, NOW, true);
        assertEquals(1, assembly.selectedFragments().size());
        assertTrue(f.project(read, assembly,
                new PublicInteractionParticipationSourceRegistrySnapshot(RELEASE, Set.of()),
                catalogue()).isEmpty());
        clearInvocations(f.authority);
        assertTrue(f.project(read, assembly, f.sources("opportunity-1"),
                new StaticSurfaceContributionCatalogue(List.of())).isEmpty());
        verifyNoInteractions(f.authority);
    }

    @Test
    void participation_cannot_override_publication_exposure_window() {
        for (var time : List.of(NOW.minusSeconds(1), NOW.plusSeconds(60))) {
            var f = new Fixture();
            var read = f.read();
            var assembly = f.assemble(read, time, true);
            assertTrue(assembly.selectedFragments().isEmpty());
            assertTrue(f.project(read, assembly, f.sources("opportunity-1"), catalogue()).isEmpty());
        }
    }

    @Test
    void draft_withdrawn_or_missing_opportunity_has_no_public_binding() {
        for (var lifecycle : List.of(PublicationLifecycle.DRAFT, PublicationLifecycle.WITHDRAWN)) {
            var f = new Fixture();
            when(f.authority.current(MERCHANT, "opportunity-1")).thenReturn(Optional.of(
                    new OpportunityPublicationState(MERCHANT, "opportunity-1", "revision-2",
                            lifecycle, lifecycle == PublicationLifecycle.DRAFT
                            ? Optional.empty() : Optional.of("revision-1"))));
            var read = f.read();
            assertTrue(read.fragments().isEmpty());
            assertThrows(ProjectionFragmentSelectionStructuralException.class,
                    () -> f.project(read, f.assemble(read, NOW, false),
                            f.sources("opportunity-1"), catalogue()));
        }
        var missing = new Fixture();
        when(missing.authority.current(MERCHANT, "opportunity-1")).thenReturn(Optional.empty());
        var read = missing.read();
        assertTrue(read.fragments().isEmpty());
        assertThrows(ProjectionFragmentSelectionStructuralException.class,
                () -> missing.project(read, missing.assemble(read, NOW, false),
                        missing.sources("opportunity-1"), catalogue()));
    }

    @Test
    void republish_or_withdraw_between_acquisition_and_exposure_withholds_stale_binding() {
        for (var lifecycle : List.of(PublicationLifecycle.PUBLISHED, PublicationLifecycle.WITHDRAWN)) {
            var f = new Fixture();
            var read = f.read();
            when(f.authority.current(MERCHANT, "opportunity-1")).thenReturn(Optional.of(
                    new OpportunityPublicationState(MERCHANT, "opportunity-1", "revision-2",
                            lifecycle, Optional.of("revision-2"))));
            var assembly = f.assemble(read, NOW, true);
            assertTrue(assembly.selectedFragments().isEmpty());
            assertTrue(f.project(read, assembly, f.sources("opportunity-1"), catalogue()).isEmpty());
        }
    }

    @Test
    void unserviceable_material_cannot_reach_binding_projection() {
        var f = new Fixture();
        var read = f.read();
        var exposure = f.exposure(read, NOW, false);
        clearInvocations(f.authority);
        var failure = assertThrows(ProjectionFragmentSelectionStructuralException.class,
                () -> f.project(read, new PublicCustomerProjectionAssemblyService().assemble(
                        read, serviceability(read, false), exposure),
                        f.sources("opportunity-1"), catalogue()));
        assertEquals("A non-serviceable bounded read cannot supply representation material", failure.getMessage());
        verifyNoInteractions(f.authority);
        // Inconsistent upstream positive Exposure cannot override a negative P2 decision.
        assertThrows(ProjectionFragmentSelectionStructuralException.class, () -> new PublicCustomerProjectionAssemblyService()
                .assemble(read, serviceability(read, false), f.exposure(read, NOW, true)));
    }

    @Test
    void participation_for_another_real_opportunity_never_binds_the_selected_subject() {
        var f = new Fixture();
        when(f.authority.current(MERCHANT, "opportunity-2")).thenReturn(Optional.of(
                OpportunityPublicationState.draft(MERCHANT, "opportunity-2", "revision-1")));
        var read = f.read();
        var assembly = f.assemble(read, NOW, true);
        assertEquals(1, assembly.selectedFragments().size());
        assertTrue(f.project(read, assembly, f.sources("opportunity-2"), catalogue()).isEmpty());
    }

    @Test
    void incompatible_registry_model_scope_or_bounded_read_is_rejected() {
        var f = new Fixture();
        var read = f.read();
        var assembly = f.assemble(read, NOW, true);
        var sources = f.sources("opportunity-1");
        assertThrows(PublicInteractionBindingProjectionStructuralException.class, () -> f.project(read, assembly,
                new PublicInteractionParticipationSourceRegistrySnapshot("other-release",
                        sources.registrations()), catalogue()));
        for (var model : List.of(f.model("merchant-b", "configuration-1", 1, RELEASE),
                f.model("merchant-a", "configuration-1", 1, "other-release"),
                f.model("merchant-a", "configuration-2", 2, RELEASE))) {
            assertThrows(RuntimeException.class, () -> new PublicInteractionBindingProjector()
                    .project(model, catalogue(), read, assembly, sources));
        }
        assertThrows(PublicInteractionBindingProjectionStructuralException.class,
                () -> f.project(f.read(), assembly, sources, catalogue()));
    }

    private static StaticSurfaceContributionCatalogue catalogue() {
        return new StaticSurfaceContributionCatalogue(List.of(new StaticSurfaceContribution(
                ENQUIRY, SurfaceAudience.PUBLIC, SurfaceContributionKind.PUBLIC_INTERACTION,
                Optional.empty(), Set.of(), Set.of("send-enquiry"))));
    }

    // Established P2 outcomes are seam inputs, as in P5. Policy-engine correctness is proved
    // separately; I2 must not invent a new Publication serviceability policy portfolio.
    private static ProjectionServiceabilityResult serviceability(BoundedProjectionRead read,
            boolean serviceable) {
        return new ProjectionServiceabilityResult(RELEASE, read.contractIdentity(),
                read.readUseIdentity(), NOW, serviceable
                ? ProjectionServiceabilityOutcome.FULLY_SERVICEABLE
                : ProjectionServiceabilityOutcome.NOT_SERVICEABLE,
                serviceable ? Set.of() : Set.of(ProjectionServiceabilityReasonCode.POLICY_EVALUATOR_UNRESOLVED),
                Set.of(), read.sourceEvidence(), Set.of(), Set.of(),
                Optional.of(read.binding()));
    }

    private static final class Fixture {
        final SemanticRegistrySnapshot registry = new SemanticRegistrySnapshot(RELEASE, Set.of(
                new RegisteredCapability("publication", List.of(new OwnedOperationalObjectDefinition(
                        "opportunity", Set.of(), (String) null)), List.of()),
                new RegisteredCapability("enquiry", List.of(new OwnedOperationalObjectDefinition(
                        "enquiry", Set.of(), (String) null)), List.of(OwnedOperationDefinition.creation(
                        "send-enquiry", "enquiry", null, "enquiry-submitted", "submit-enquiry")))));
        final MerchantConfiguration configuration = new MerchantConfiguration(
                MERCHANT.merchantIdentifier(), "configuration-1", 1, RELEASE,
                Set.of("publication", "enquiry"), Set.of(), Optional.empty());
        final ExecutableMerchantModel model = new ConfigurationCompiler(
                version -> version.equals(RELEASE) ? Optional.of(registry) : Optional.empty())
                .compile(configuration);
        final OpportunityEnquiryParticipationDefinition definition =
                OpportunityEnquiryParticipationDefinition.forRelease(registry);
        final OpportunityPublicationStateAuthority authority = mock(OpportunityPublicationStateAuthority.class);
        final EstablishedObservationRequest request = new DefaultEstablishedObservationRequest(
                new ActiveRelease(new ConfigurationRelease("release-a", configuration, model,
                        "test-compiler", NOW)), MERCHANT, RELEASE,
                Optional.of(new ApiObservationRequestProvenance(API, ApiSurfaceClass.PUBLIC, "opportunity-query")));

        Fixture() {
            when(authority.current(MERCHANT, "opportunity-1")).thenReturn(Optional.of(
                    new OpportunityPublicationState(MERCHANT, "opportunity-1", "revision-2",
                            PublicationLifecycle.PUBLISHED, Optional.of("revision-1"))));
            when(authority.revision(MERCHANT, "opportunity-1", "revision-1"))
                    .thenReturn(Optional.of(revision("revision-1", "Published title")));
            when(authority.revision(MERCHANT, "opportunity-1", "revision-2"))
                    .thenReturn(Optional.of(revision("revision-2", "Unpublished edit")));
        }

        BoundedProjectionRead read() {
            return new AuthorityBackedOpportunityPublicRepresentationProjectionReadPort(authority)
                    .observe(MERCHANT, "opportunity-1", NOW).toBoundedRead(request);
        }

        PublicInteractionParticipationSourceRegistrySnapshot sources(String identity) {
            return new PublicInteractionParticipationSourceRegistrySnapshot(RELEASE, Set.of(
                    new OpportunityEnquiryParticipationSource(definition, model, authority,
                            Set.of(identity)).registration()));
        }

        List<PublicInteractionBinding> project(BoundedProjectionRead read,
                PublicCustomerProjectionAssembly assembly,
                PublicInteractionParticipationSourceRegistrySnapshot sources,
                StaticSurfaceContributionCatalogue catalogue) {
            return new PublicInteractionBindingProjector().project(model, catalogue, read, assembly, sources);
        }

        PublicCustomerProjectionAssembly assemble(BoundedProjectionRead read, Instant time, boolean serviceable) {
            return new PublicCustomerProjectionAssemblyService().assemble(read,
                    serviceability(read, serviceable), exposure(read, time, serviceable));
        }

        ApiExposureResolution exposure(BoundedProjectionRead read, Instant time, boolean serviceable) {
            var affinity = new ObservationContributionConstructionBoundary().construct(request,
                    OpportunityMaterialAffinityObservationContributionRegistration.constructor(read));
            var context = new DefaultAudienceObservationContext(request, new PublicObservationSubject(), Set.of(affinity));
            var admission = AudienceObservationAdmissionResults.admitted(
                    new DefaultAudienceObservationInvocationBinding(read.requestBinding()), time);
            var resolved = new ExposureResolver().resolve(context, admission,
                    serviceable ? read.fragments().stream().map(ProjectionMaterialFragment::candidateObservation).toList()
                            : List.of(),
                    OpportunityPublicExposureContractPortfolio.forRelease(RELEASE),
                    new ExposureRequirementEvaluatorBindingSnapshot(RELEASE, List.of(
                            new ExposureRequirementEvaluatorBinding(
                                    OpportunityPublicExposureReferences.EXPOSURE_WINDOW_REQUIREMENT,
                                    new OpportunityExposureWindowRequirementEvaluator(
                                            new AuthorityBackedOpportunityPublicExposureReadPort(authority))))),
                    new MerchantExposureChoiceEvaluatorBindingSnapshot(RELEASE, List.of()));
            return new ApiExposureResolutionBinder().bind(new DefaultApiAudienceObservationContext(
                    API, ApiSurfaceClass.PUBLIC, context), admission, RELEASE, resolved).resolution().orElseThrow();
        }

        ExecutableMerchantModel model(String merchant, String identity, long version, String release) {
            return new ExecutableMerchantModel(merchant, identity, version, release,
                    model.capabilityIdentifiers(), model.operationalObjects(), model.operations());
        }
    }

    private static OpportunityPublicationMaterialRevision revision(String id, String title) {
        return new OpportunityPublicationMaterialRevision(MERCHANT, "opportunity-1", id, title,
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), List.of(),
                Optional.empty(), Optional.empty(), Optional.of(new OpportunityExactInstantBoundary(NOW)),
                Optional.of(new OpportunityExactInstantBoundary(NOW.plusSeconds(60))));
    }
}
