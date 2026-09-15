package mainstreet.enquiry;

import mainstreet.api.*;
import mainstreet.application.MerchantScope;
import mainstreet.enquiry.delivery.*;
import mainstreet.publication.*;
import mainstreet.publication.delivery.*;
import mainstreet.surface.*;
import mainstreet.runtime.*;
import mainstreet.merchantaccount.MerchantControllerRelationshipAuthority;
import mainstreet.workforce.MerchantMembershipAuthority;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.*;
import java.util.concurrent.atomic.*;
import static mainstreet.enquiry.OpportunityEnquirySubmissionPreparationTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PublicOpportunityEnquiryDeliveryT2BTest {
    static final String BINDING_PATH = "/api/public/storefronts/shop/opportunities/O1/enquiry-binding";
    static final String SUBMIT_PATH = "/api/public/storefronts/shop/enquiries/opportunity";

    @Test
    void binding_uses_captured_release_while_fresh_submission_revalidates_changed_configuration() throws Exception {
        var f = new Fixture();
        when(f.e3.activation.current(SCOPE.merchantIdentifier())).thenReturn(
                Optional.of(f.e3.release(true)), Optional.of(f.e3.release(false)));
        clearInvocations(f.e3.activation);
        var token = f.binding();
        verify(f.e3.activation, times(1)).current(SCOPE.merchantIdentifier());
        f.mvc(f.application).perform(post(SUBMIT_PATH).header("Idempotency-Key", "k")
                .contentType(MediaType.APPLICATION_JSON).content(body(token)))
                .andExpect(status().isUnprocessableEntity());
        assertEquals(0, f.preparations.get());
    }

    @Test
    void codec_requires_explicit_keys_and_unknown_key_or_malformed_binding_cannot_reach_application() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> new OpportunityEnquiryBindingCodec("missing", Map.of()));
        assertThrows(IllegalArgumentException.class, () -> new OpportunityEnquiryBindingCodec("test", Map.of("test", new byte[8])));
        var f = new Fixture();
        var token = f.binding();
        var mvc = f.mvc(f.application);
        for (var invalid : List.of("invalid", token.replace("v1.test.", "v2.test."),
                token.replace("v1.test.", "v1.unknown."))) {
            mvc.perform(post(SUBMIT_PATH).header("Idempotency-Key", "k")
                    .contentType(MediaType.APPLICATION_JSON).content(body(invalid)))
                    .andExpect(status().isBadRequest());
        }
        verifyNoInteractions(f.application);
    }

    @Test
    void production_profile_requires_explicit_providers_and_wires_both_routes() throws Exception {
        var f = new Fixture();
        try (var context = new org.springframework.context.annotation.AnnotationConfigApplicationContext()) {
            context.getEnvironment().setActiveProfiles("enquiry-opportunity-public-api");
            context.registerBean(PublicOpportunityRouteScopeAuthority.class,
                    () -> new PublicOpportunityRouteScopeAuthority(Map.of("shop", SCOPE)));
            context.registerBean(PublicEnquiryRouteScopeAuthority.class,
                    () -> new PublicEnquiryRouteScopeAuthority(Map.of("shop", SCOPE)));
            context.registerBean(mainstreet.semantic.configuration.ConfigurationReleaseActivation.class, () -> f.e3.activation);
            context.registerBean(mainstreet.semantic.registry.SemanticRegistrySnapshot.class, () -> f.e3.registry);
            context.registerBean(OpportunityPublicationStateAuthority.class, () -> f.e3.publication);
            context.registerBean(OpportunityPublicationSubmissionLock.class, () -> f.e3.lock);
            context.registerBean(AudienceObservationContextEstablisher.class, f::contexts);
            context.registerBean(AudienceObservationAdmissionEvaluator.class, f::queryAdmission);
            context.registerBean(PublicEnquirySubmissionAdmissionAuthority.class, () -> scope -> f.allowed.get());
            context.registerBean(PublicOpportunityEnquiryRequirements.class, () -> f.e3::prepared);
            context.registerBean(EnquirySubmissionApplicationService.class, () -> f.application);
            context.registerBean(java.time.Clock.class, () -> f.e3.clock);
            context.registerBean(OpportunityEnquiryBindingCodec.class, () -> f.codec);
            context.register(PublicOpportunityEnquiryApiConfiguration.class, PublicOpportunityEnquiryController.class);
            context.refresh();
            var mvc = MockMvcBuilders.standaloneSetup(context.getBean(PublicOpportunityEnquiryController.class)).build();
            var response = mvc.perform(get(BINDING_PATH)).andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
            var token = new tools.jackson.databind.ObjectMapper().readTree(response).get("binding").asText();
            mvc.perform(post(SUBMIT_PATH).header("Idempotency-Key", "k")
                    .contentType(MediaType.APPLICATION_JSON).content(body(token))).andExpect(status().isOk());
        }
    }

    @Test
    void real_selected_binding_carries_exact_subject_without_disclosing_revision() throws Exception {
        var f = new Fixture();
        var token = f.binding();
        assertTrue(token.startsWith("v1.test."));
        f.mvc(f.application).perform(post(SUBMIT_PATH).header("Idempotency-Key", "k")
                .contentType(MediaType.APPLICATION_JSON).content(body(token)))
                .andExpect(status().isOk()).andExpect(content().json("{\"outcome\":\"COMPLETED\"}", true))
                .andExpect(header().string("Cache-Control", "no-store"));
        assertEquals(f.e3.intent(true).subjectRevision(), f.lastIntent.get().subjectRevision());
        assertEquals(Optional.empty(), f.lastIntent.get().customerContextIdentity());
        assertEquals(Optional.of("visitor@example.com"), f.lastIntent.get().contact().email());
    }

    @Test
    void binding_is_absent_without_current_interaction_or_exposure() throws Exception {
        var f = new Fixture();
        when(f.e3.activation.current(SCOPE.merchantIdentifier())).thenReturn(Optional.of(f.e3.release(false)));
        f.mvc(f.application).perform(get(BINDING_PATH)).andExpect(status().isNotFound());
        when(f.e3.activation.current(SCOPE.merchantIdentifier())).thenReturn(Optional.of(f.e3.release(true)));
        when(f.e3.publication.current(SCOPE, "O1")).thenReturn(Optional.of(
                new OpportunityPublicationState(SCOPE, "O1", "R8", PublicationLifecycle.WITHDRAWN, Optional.of("R7"))));
        f.mvc(f.application).perform(get(BINDING_PATH)).andExpect(status().isNotFound());
        verifyNoInteractions(f.application);
    }

    @Test
    void tampering_foreign_scope_and_unknown_fields_are_rejected_before_application() throws Exception {
        var f = new Fixture();
        var token = f.binding();
        var bytes = Base64.getUrlDecoder().decode(token.substring(token.lastIndexOf('.') + 1));
        bytes[bytes.length - 1] ^= 1;
        var tampered = token.substring(0, token.lastIndexOf('.') + 1)
                + Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        var mvc = f.mvc(f.application);
        mvc.perform(post(SUBMIT_PATH).header("Idempotency-Key", "k").contentType(MediaType.APPLICATION_JSON)
                .content(body(tampered))).andExpect(status().isBadRequest());
        mvc.perform(post(SUBMIT_PATH.replace("/shop/", "/other/")).header("Idempotency-Key", "k")
                .contentType(MediaType.APPLICATION_JSON).content(body(token))).andExpect(status().isBadRequest());
        mvc.perform(post(SUBMIT_PATH).header("Idempotency-Key", "k").contentType(MediaType.APPLICATION_JSON)
                .content(body(token).replace("\"question\"", "\"subjectRevision\":null,\"question\"")))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(f.application);
    }

    @Test
    void stale_binding_never_downgrades_to_general_and_admission_is_current() throws Exception {
        var f = new Fixture();
        var token = f.binding();
        when(f.e3.lock.lockCurrent(SCOPE, "O1")).thenReturn(Optional.of(
                new OpportunityPublicationState(SCOPE, "O1", "R8", PublicationLifecycle.PUBLISHED, Optional.of("R8"))));
        f.mvc(f.application).perform(post(SUBMIT_PATH).header("Idempotency-Key", "k")
                .contentType(MediaType.APPLICATION_JSON).content(body(token)))
                .andExpect(status().isUnprocessableEntity());
        assertEquals(0, f.preparations.get());
        f.allowed.set(false);
        clearInvocations(f.application);
        f.mvc(f.application).perform(post(SUBMIT_PATH).header("Idempotency-Key", "k")
                .contentType(MediaType.APPLICATION_JSON).content(body(token))).andExpect(status().isForbidden());
        verifyNoInteractions(f.application);
    }

    @Test
    void reissued_and_rotated_locators_preserve_logical_intent() throws Exception {
        var f = new Fixture();
        var first = f.binding();
        var second = f.binding();
        assertNotEquals(first, second);
        f.submission(f.application).submit("shop", "k", new PublicOpportunityEnquiryRequest(first, "Question", null, null, null));
        var intent = f.lastIntent.get();
        f.codec = new OpportunityEnquiryBindingCodec("next", Map.of("test", Fixture.KEY, "next", Fixture.NEXT_KEY));
        var rotated = f.binding();
        f.submission(f.application).submit("shop", "k", new PublicOpportunityEnquiryRequest(rotated, "Question", null, null, null));
        assertEquals(intent, f.lastIntent.get());
        f.submission(f.application).submit("shop", "k", new PublicOpportunityEnquiryRequest(first, "Question", null, null, null));
        assertEquals(intent, f.lastIntent.get());
    }

    static String body(String token) {
        return "{\"binding\":\"" + token + "\",\"question\":\"Question\",\"email\":\"visitor@example.com\"}";
    }

    static class Fixture {
        static final byte[] KEY = new byte[32];
        static final byte[] NEXT_KEY = new byte[32];
        static { Arrays.fill(KEY, (byte) 7); Arrays.fill(NEXT_KEY, (byte) 9); }
        final OpportunityEnquirySubmissionPreparationTest.Fixture e3 = new OpportunityEnquirySubmissionPreparationTest.Fixture();
        final AtomicBoolean allowed = new AtomicBoolean(true);
        final AtomicInteger preparations = new AtomicInteger();
        final AtomicReference<EnquirySubmissionIntent> lastIntent = new AtomicReference<>();
        final EnquirySubmissionApplicationService application = mock(EnquirySubmissionApplicationService.class);
        OpportunityEnquiryBindingCodec codec = new OpportunityEnquiryBindingCodec("test", Map.of("test", KEY));

        Fixture() {
            when(application.submit(any(), any(), any())).thenAnswer(call -> {
                EnquirySubmissionIntent intent = call.getArgument(1);
                lastIntent.set(intent);
                return ((EnquirySubmissionPreparation) call.getArgument(2)).prepare(intent);
            });
        }

        AudienceObservationContextEstablisher contexts() {
            return new AudienceObservationContextEstablisher(mock(SessionRecordStore.class),
                    new ContextualAccessProofRuntimeBindingSnapshot(List.of()),
                    new ObservationContributionDefinitionRegistrySnapshot(RELEASE, List.of(
                            OpportunityMaterialAffinityObservationContributionRegistration.definition())),
                    new ObservationContributionRuntimeBindingSnapshot(List.of(
                            OpportunityMaterialAffinityObservationContributionRegistration.runtimeBinding())));
        }

        AudienceObservationAdmissionEvaluator queryAdmission() {
            return new AudienceObservationAdmissionEvaluator(mock(AuthenticationSessionCurrentnessAuthority.class),
                    mock(MerchantControllerRelationshipAuthority.class), mock(MerchantMembershipAuthority.class),
                    (context, binding, at) -> allowed.get() ? AudienceObservationPlatformProtection.SATISFIED
                            : AudienceObservationPlatformProtection.UNSATISFIED, e3.clock);
        }

        PublicOpportunityEnquiryBindingQuery query() {
            return PublicOpportunityEnquiryBindingQuery.create(
                    new PublicOpportunityRouteScopeAuthority(Map.of("shop", SCOPE)), e3.activation, e3.registry,
                    e3.publication, contexts(), queryAdmission(), e3.clock, codec);
        }

        PublicOpportunityEnquirySubmission submission(EnquirySubmissionApplicationService app) {
            return new PublicOpportunityEnquirySubmission(new PublicEnquiryRouteScopeAuthority(Map.of(
                    "shop", SCOPE, "other", new MerchantScope("merchant-b"))), scope -> allowed.get(), app,
                    e3.activation, e3.registry, e3.publication, e3.lock, intent -> {
                        lastIntent.set(intent);
                        int count = preparations.incrementAndGet();
                        var value = e3.prepared(intent);
                        return new EnquirySubmission(value.merchantScope(), "E" + count, value.submittedAt(),
                                value.question(), value.contact(), value.semanticContext(), value.subjectRevision(),
                                value.customerContextIdentity());
                    }, e3.clock, codec);
        }

        MockMvc mvc(EnquirySubmissionApplicationService app) {
            return MockMvcBuilders.standaloneSetup(new PublicOpportunityEnquiryController(query(), submission(app))).build();
        }

        String binding() throws Exception {
            var response = mvc(application).perform(get(BINDING_PATH)).andExpect(status().isOk())
                    .andExpect(header().string("Cache-Control", "no-store"))
                    .andExpect(jsonPath("$.opportunityIdentity").value("O1"))
                    .andReturn().getResponse().getContentAsString();
            assertFalse(response.contains("merchant-a"));
            assertFalse(response.contains("revision"));
            return new tools.jackson.databind.ObjectMapper().readTree(response).get("binding").asText();
        }
    }
}
