package grandrue.surface;

import grandrue.application.MerchantScope;
import grandrue.publication.*;
import grandrue.publication.delivery.*;
import grandrue.runtime.SessionRecordStore;
import grandrue.semantic.configuration.ConfigurationReleaseActivation;
import grandrue.semantic.registry.SemanticRegistrySnapshot;
import grandrue.runtime.AuthenticationSessionCurrentnessAuthority;
import grandrue.merchantaccount.MerchantControllerRelationshipAuthority;
import grandrue.workforce.MerchantMembershipAuthority;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Clock;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static grandrue.surface.PublicOpportunityQueryResponseT1ATest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PublicOpportunityQueryDeliveryT1BTest {
    @Test
    void production_profile_wires_the_registered_query_and_controller() throws Exception {
        var f = new DeliveryFixture(new Fixture().owner);
        try (var context = new org.springframework.context.annotation.AnnotationConfigApplicationContext()) {
            context.getEnvironment().setActiveProfiles("publication-public-api");
            context.registerBean(PublicOpportunityRouteScopeAuthority.class,
                    () -> new PublicOpportunityRouteScopeAuthority(Map.of("shop", SCOPE)));
            context.registerBean(ConfigurationReleaseActivation.class, () -> f.activation);
            context.registerBean(SemanticRegistrySnapshot.class, () -> f.semantic);
            context.registerBean(OpportunityPublicationStateAuthority.class, () -> f.owner);
            context.registerBean(AudienceObservationContextEstablisher.class, f::contexts);
            context.registerBean(AudienceObservationAdmissionEvaluator.class, f::admission);
            context.registerBean(Clock.class, () -> Clock.fixed(NOW, ZoneOffset.UTC));
            context.register(PublicOpportunityApiConfiguration.class, PublicOpportunityController.class);
            context.refresh();
            MockMvcBuilders.standaloneSetup(context.getBean(PublicOpportunityController.class)).build()
                    .perform(get("/api/public/storefronts/shop/opportunities/O1"))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.title").value("Published title"));
        }
    }

    @Test
    void trusted_locator_to_real_p2_exposure_and_http_response() throws Exception {
        var f = new DeliveryFixture(new Fixture().owner);
        var body = f.mvc().perform(get("/api/public/storefronts/shop/opportunities/O1"))
                .andExpect(status().isOk()).andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(jsonPath("$.title").value("Published title"))
                .andExpect(jsonPath("$.applicationsOpen.zone").value("Europe/London"))
                .andReturn().getResponse().getContentAsString();
        assertFalse(body.contains("secret-revision"));
        assertFalse(body.contains("merchant-a"));
        assertFalse(body.contains("sourceEvidence"));
    }

    @Test
    void caller_scope_and_filters_do_not_establish_authority() throws Exception {
        var owner = new Fixture().owner;
        var f = new DeliveryFixture(owner);
        clearInvocations(owner);
        f.mvc().perform(get("/api/public/storefronts/shop/opportunities/O1").param("merchantId", "merchant-b"))
                .andExpect(status().isBadRequest());
        f.mvc().perform(get("/api/public/storefronts/merchant-a/opportunities/O1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.category").value("NOT_FOUND_OR_NOT_ACCESSIBLE"));
        verifyNoInteractions(owner);
    }

    @Test
    void foreign_merchant_same_resource_identifier_remains_absent() throws Exception {
        var f = new DeliveryFixture(new Fixture().owner);
        f.mvc().perform(get("/api/public/storefronts/other-shop/opportunities/O1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void unavailable_active_release_is_safe_and_precedes_material_reads() throws Exception {
        var owner = new Fixture().owner;
        var f = new DeliveryFixture(owner);
        when(f.activation.current(SCOPE.merchantIdentifier())).thenReturn(Optional.empty());
        clearInvocations(owner);
        f.mvc().perform(get("/api/public/storefronts/shop/opportunities/O1"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.category").value("REPRESENTATION_UNAVAILABLE"));
        verifyNoInteractions(owner);
    }

    @Test
    void withdrawal_between_acquisition_and_exposure_withholds_http_body() throws Exception {
        var owner = new Fixture().owner;
        var initial = owner.current(SCOPE, "O1");
        var withdrawn = Optional.of(new OpportunityPublicationState(SCOPE, "O1", "edited-revision",
                PublicationLifecycle.WITHDRAWN, Optional.of("secret-revision")));
        // P5 acquisition checks current binding before/after material; Exposure sees withdrawal.
        when(owner.current(SCOPE, "O1")).thenReturn(initial, initial, withdrawn);
        new DeliveryFixture(owner).mvc().perform(get("/api/public/storefronts/shop/opportunities/O1"))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.title").doesNotExist());
    }

    @Test
    void current_platform_protection_is_evaluated_for_every_request() throws Exception {
        var f = new DeliveryFixture(new Fixture().owner);
        var mvc = f.mvc();
        mvc.perform(get("/api/public/storefronts/shop/opportunities/O1")).andExpect(status().isOk());
        f.allowed.set(false);
        mvc.perform(get("/api/public/storefronts/shop/opportunities/O1"))
                .andExpect(status().isForbidden()).andExpect(jsonPath("$.title").doesNotExist());
    }

    @Test
    void owner_failure_never_serializes_exception_details() throws Exception {
        var owner = new Fixture().owner;
        when(owner.current(SCOPE, "O1")).thenThrow(new IllegalStateException("secret SQL detail"));
        var response = new DeliveryFixture(owner).mvc().perform(get("/api/public/storefronts/shop/opportunities/O1"))
                .andExpect(status().isServiceUnavailable()).andReturn().getResponse().getContentAsString();
        assertFalse(response.contains("secret"));
        assertFalse(response.contains("SQL"));
    }

    @Test
    void real_serviceability_portfolio_rejects_missing_or_stale_source_progress() {
        var read = new Fixture().read();
        var engine = new ProjectionServiceabilityEvaluationEngine(
                OpportunityPublicQueryProjectionPortfolio.contracts(RELEASE),
                OpportunityPublicQueryProjectionPortfolio.evaluators(RELEASE));
        assertEquals(ProjectionServiceabilityOutcome.FULLY_SERVICEABLE, engine.evaluate(
                ProjectionServiceabilityEvaluationRequest.forBoundedRead(read, NOW)).outcome());
        var evidence = read.sourceEvidence().iterator().next();
        var stale = new ProjectionSourceEvidence(evidence.sourceReference(), evidence.evidenceIdentifier(),
                evidence.observedProgressIdentifier(), Optional.of("different-revision"), NOW,
                evidence.availability(), evidence.completeness(), evidence.revocationState());
        var staleRead = new BoundedProjectionRead(new Fixture().request, read.contractIdentity(), read.readUseIdentity(),
                Set.of(stale), read.fragments());
        assertEquals(ProjectionServiceabilityOutcome.NOT_SERVICEABLE, engine.evaluate(
                ProjectionServiceabilityEvaluationRequest.forBoundedRead(staleRead, NOW)).outcome());
    }

    static class DeliveryFixture {
        final ConfigurationReleaseActivation activation = mock(ConfigurationReleaseActivation.class);
        final SemanticRegistrySnapshot semantic = mock(SemanticRegistrySnapshot.class);
        final AtomicBoolean allowed = new AtomicBoolean(true);
        final OpportunityPublicationStateAuthority owner;

        DeliveryFixture(OpportunityPublicationStateAuthority owner) {
            this.owner = owner;
            when(semantic.version()).thenReturn(RELEASE);
            when(activation.current(SCOPE.merchantIdentifier())).thenReturn(Optional.of(
                    TestReleases.activeRelease(SCOPE.merchantIdentifier(), RELEASE)));
            when(activation.current("merchant-b")).thenReturn(Optional.of(
                    TestReleases.activeRelease("merchant-b", RELEASE)));
        }

        AudienceObservationContextEstablisher contexts() {
            return new AudienceObservationContextEstablisher(mock(SessionRecordStore.class),
                    new ContextualAccessProofRuntimeBindingSnapshot(List.of()),
                    new ObservationContributionDefinitionRegistrySnapshot(RELEASE, List.of(
                            OpportunityMaterialAffinityObservationContributionRegistration.definition())),
                    new ObservationContributionRuntimeBindingSnapshot(List.of(
                            OpportunityMaterialAffinityObservationContributionRegistration.runtimeBinding())));
        }

        AudienceObservationAdmissionEvaluator admission() {
            return new AudienceObservationAdmissionEvaluator(
                    mock(AuthenticationSessionCurrentnessAuthority.class),
                    mock(MerchantControllerRelationshipAuthority.class), mock(MerchantMembershipAuthority.class),
                    (context, binding, at) -> allowed.get() ? AudienceObservationPlatformProtection.SATISFIED
                            : AudienceObservationPlatformProtection.UNSATISFIED, Clock.fixed(NOW, ZoneOffset.UTC));
        }

        PublicOpportunityQuery query() {
            return PublicOpportunityQuery.create(new PublicOpportunityRouteScopeAuthority(Map.of(
                    "shop", SCOPE, "other-shop", new MerchantScope("merchant-b"))),
                    activation, semantic, owner, contexts(), admission(), Clock.fixed(NOW, ZoneOffset.UTC));
        }

        MockMvc mvc() { return MockMvcBuilders.standaloneSetup(new PublicOpportunityController(query())).build(); }
    }
}
