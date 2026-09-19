package grandrue.enquiry;

import grandrue.api.*;
import grandrue.application.*;
import grandrue.enquiry.delivery.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.*;
import java.util.concurrent.atomic.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PublicGeneralEnquiryDeliveryT2ATest {
    static final String PATH = "/api/public/storefronts/shop/enquiries/general";
    static final String BODY = "{\"question\":\"Original question\",\"email\":\"supplied@example.com\"}";

    @Test
    void production_profile_wires_required_authorities_and_the_general_command() throws Exception {
        var f = new Fixture();
        try (var context = new org.springframework.context.annotation.AnnotationConfigApplicationContext()) {
            context.getEnvironment().setActiveProfiles("enquiry-public-api");
            context.registerBean(PublicEnquiryRouteScopeAuthority.class, () -> new PublicEnquiryRouteScopeAuthority(
                    Map.of("shop", OpportunityEnquirySubmissionPreparationTest.SCOPE)));
            context.registerBean(PublicEnquirySubmissionAdmissionAuthority.class, f::admission);
            context.registerBean(PublicGeneralEnquiryRequirements.class, f::requirements);
            context.registerBean(EnquirySubmissionApplicationService.class, () -> f.application);
            context.registerBean(grandrue.semantic.configuration.ConfigurationReleaseActivation.class, () -> f.e3.activation);
            context.registerBean(grandrue.semantic.registry.SemanticRegistrySnapshot.class, () -> f.e3.registry);
            context.registerBean(grandrue.publication.OpportunityPublicationStateAuthority.class, () -> f.e3.publication);
            context.registerBean(grandrue.publication.OpportunityPublicationSubmissionLock.class, () -> f.e3.lock);
            context.registerBean(java.time.Clock.class, () -> f.e3.clock);
            context.register(PublicGeneralEnquiryApiConfiguration.class, PublicGeneralEnquiryController.class);
            context.refresh();
            MockMvcBuilders.standaloneSetup(context.getBean(PublicGeneralEnquiryController.class)).build()
                    .perform(post(PATH).header("Idempotency-Key", "k").contentType(MediaType.APPLICATION_JSON).content(BODY))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.outcome").value("COMPLETED"));
            verifyNoInteractions(f.e3.publication, f.e3.lock);
        }
    }

    @Test
    void contract_is_explicit_public_command_with_retry_and_no_stored_data_result() {
        var d = PublicGeneralEnquiryContract.definition();
        assertEquals(ApiSurfaceClass.PUBLIC, d.registration().surface());
        assertEquals(ApiContractKind.COMMAND, d.registration().kind());
        assertEquals(new ApiOwnerContractReference("enquiry", "send-enquiry"), d.registration().ownerContractReference());
        assertTrue(d.logicalRetryMechanismReference().isPresent());
        assertEquals("enquiry/public-completion-acknowledgement", d.safeResultRepresentationReference());
    }

    @Test
    void current_scope_and_e3_preparation_produce_only_completion_acknowledgement() throws Exception {
        var f = new Fixture();
        var body = f.mvc(f.application).perform(post(PATH).header("Idempotency-Key", "retry-1")
                .contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk()).andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(jsonPath("$.outcome").value("COMPLETED")).andReturn().getResponse().getContentAsString();
        assertEquals("{\"outcome\":\"COMPLETED\"}", body);
        assertEquals(1, f.preparations.get());
        assertEquals(Optional.empty(), f.lastIntent.get().customerContextIdentity());
        assertEquals(Optional.empty(), f.lastIntent.get().subjectRevision());
        assertEquals(Optional.of("supplied@example.com"), f.lastIntent.get().contact().email());
        assertEquals(OpportunityEnquirySubmissionPreparationTest.SCOPE, f.lastIntent.get().merchantScope());
    }

    @Test
    void subject_customer_scope_and_other_unknown_fields_are_rejected_not_downgraded() throws Exception {
        var f = new Fixture();
        var mvc = f.mvc(f.application);
        for (var extra : List.of("\"subject\":null", "\"opportunityIdentity\":\"O1\"",
                "\"customerContextIdentity\":\"C1\"", "\"merchantId\":\"other\"")) {
            mvc.perform(post(PATH).header("Idempotency-Key", "retry-1").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"question\":\"Question\"," + extra + "}"))
                    .andExpect(status().isBadRequest());
        }
        verifyNoInteractions(f.application);
    }

    @Test
    void invalid_transport_and_unregistered_locator_never_reach_application() throws Exception {
        var f = new Fixture();
        var mvc = f.mvc(f.application);
        mvc.perform(post(PATH).contentType(MediaType.APPLICATION_JSON).content(BODY)).andExpect(status().isBadRequest());
        mvc.perform(post(PATH).header("Idempotency-Key", " ").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isBadRequest());
        mvc.perform(post(PATH).header("Idempotency-Key", "k").param("merchantId", "other")
                .contentType(MediaType.APPLICATION_JSON).content(BODY)).andExpect(status().isBadRequest());
        mvc.perform(post(PATH.replace("/shop/", "/merchant-a/")).header("Idempotency-Key", "k")
                .contentType(MediaType.APPLICATION_JSON).content(BODY)).andExpect(status().isNotFound());
        verifyNoInteractions(f.application);
    }

    @Test
    void current_admission_is_required_before_every_application_attempt() throws Exception {
        var f = new Fixture();
        f.allowed.set(false);
        f.mvc(f.application).perform(post(PATH).header("Idempotency-Key", "retry-1")
                .contentType(MediaType.APPLICATION_JSON).content(BODY)).andExpect(status().isForbidden());
        verifyNoInteractions(f.application);
        assertEquals(1, f.admissions.get());
    }

    @Test
    void unmet_owner_requirements_and_inactive_interaction_are_known_rejections() throws Exception {
        var f = new Fixture();
        f.requirementsSatisfied.set(false);
        f.mvc(f.application).perform(post(PATH).header("Idempotency-Key", "retry-1")
                .contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isUnprocessableEntity()).andExpect(jsonPath("$.outcome").value("REJECTED"));
        f.requirementsSatisfied.set(true);
        when(f.e3.activation.current(OpportunityEnquirySubmissionPreparationTest.SCOPE.merchantIdentifier()))
                .thenReturn(Optional.of(f.e3.release(false)));
        f.mvc(f.application).perform(post(PATH).header("Idempotency-Key", "retry-2")
                .contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void conflict_is_distinct_from_uncertain_application_failure_without_internal_details() throws Exception {
        var f = new Fixture();
        doThrow(new EnquiryApplicationRequestConflictException()).when(f.application).submit(any(), any(), any());
        f.mvc(f.application).perform(post(PATH).header("Idempotency-Key", "k")
                .contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isConflict()).andExpect(jsonPath("$.outcome").value("REJECTED"))
                .andExpect(jsonPath("$.problem.category").value("IDEMPOTENCY_CONFLICT"));
        doThrow(new IllegalStateException("secret SQL result")).when(f.application).submit(any(), any(), any());
        var body = f.mvc(f.application).perform(post(PATH).header("Idempotency-Key", "k")
                .contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isServiceUnavailable()).andExpect(jsonPath("$.outcome").value("OUTCOME_UNCERTAIN"))
                .andReturn().getResponse().getContentAsString();
        assertFalse(body.contains("secret"));
    }

    @Test
    void client_retry_keys_are_stable_scoped_and_never_business_identity() {
        var a = PublicGeneralEnquirySubmission.requestIdentity(new MerchantScope("a"), "bc");
        var b = PublicGeneralEnquirySubmission.requestIdentity(new MerchantScope("ab"), "c");
        assertNotEquals(a, b);
        assertEquals(a, PublicGeneralEnquirySubmission.requestIdentity(new MerchantScope("a"), "bc"));
        assertNotEquals("bc", a.value());
    }

    static class Fixture {
        final OpportunityEnquirySubmissionPreparationTest.Fixture e3 = new OpportunityEnquirySubmissionPreparationTest.Fixture();
        final AtomicBoolean allowed = new AtomicBoolean(true);
        final AtomicBoolean requirementsSatisfied = new AtomicBoolean(true);
        final AtomicInteger preparations = new AtomicInteger();
        final AtomicInteger admissions = new AtomicInteger();
        final AtomicReference<EnquirySubmissionIntent> lastIntent = new AtomicReference<>();
        final EnquirySubmissionApplicationService application = mock(EnquirySubmissionApplicationService.class);

        Fixture() {
            when(application.submit(any(), any(), any())).thenAnswer(call ->
                    ((EnquirySubmissionPreparation) call.getArgument(2)).prepare(call.getArgument(1)));
        }

        PublicGeneralEnquiryRequirements requirements() {
            return intent -> {
                lastIntent.set(intent);
                if (!requirementsSatisfied.get()) throw new PublicEnquiryRequirementsUnsatisfiedException();
                var prepared = e3.prepared(intent);
                return new EnquirySubmission(prepared.merchantScope(), "E" + preparations.incrementAndGet(),
                        prepared.submittedAt(), prepared.question(), prepared.contact(), prepared.semanticContext(),
                        prepared.subjectRevision(), prepared.customerContextIdentity());
            };
        }

        PublicEnquirySubmissionAdmissionAuthority admission() {
            return scope -> {
                admissions.incrementAndGet();
                return allowed.get();
            };
        }

        PublicGeneralEnquirySubmission delivery(EnquirySubmissionApplicationService app) {
            return new PublicGeneralEnquirySubmission(new PublicEnquiryRouteScopeAuthority(
                    Map.of("shop", OpportunityEnquirySubmissionPreparationTest.SCOPE)), admission(), app,
                    e3.activation, e3.registry, e3.publication, e3.lock, requirements(), e3.clock);
        }

        MockMvc mvc(EnquirySubmissionApplicationService app) {
            return MockMvcBuilders.standaloneSetup(new PublicGeneralEnquiryController(delivery(app))).build();
        }
    }
}
