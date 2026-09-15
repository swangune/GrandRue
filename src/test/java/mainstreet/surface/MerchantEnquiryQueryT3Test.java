package mainstreet.surface;

import mainstreet.api.*;
import mainstreet.application.MerchantScope;
import mainstreet.enquiry.*;
import mainstreet.enquiry.delivery.*;
import mainstreet.runtime.*;
import mainstreet.semantic.configuration.ConfigurationReleaseActivation;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import static mainstreet.surface.EnquiryMerchantExposureM1Test.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MerchantEnquiryQueryT3Test {
    static final String PATH = "/api/merchant/workspaces/shop/enquiries/E1";

    @Test
    void response_mapping_rejects_foreign_resource_or_read_and_never_reacquires_material() {
        var f = new EnquiryMerchantRepresentationM2Test.Fixture();
        var read = f.read();
        var p2 = EnquiryMerchantRepresentationM2Test.serviceability(read, true);
        var exposure = f.exposure(read);
        clearInvocations(f.m1.store);
        var adapter = new MerchantEnquiryResponseAdapter();
        assertTrue(adapter.map("E1", read, p2, exposure).isPresent());
        assertThrows(IllegalArgumentException.class, () -> adapter.map("E2", read, p2, exposure));
        var other = new EnquiryMerchantRepresentationM2Test.Fixture();
        var otherRead = other.read();
        assertThrows(RuntimeException.class, () -> adapter.map("E1", read,
                EnquiryMerchantRepresentationM2Test.serviceability(otherRead, true), exposure));
        assertThrows(RuntimeException.class, () -> adapter.map("E1", read, p2, other.exposure(otherRead)));
        verifyNoInteractions(f.m1.store);
    }

    @Test
    void production_profile_wires_required_authorities_and_owner_privileges() throws Exception {
        var f = new Fixture();
        try (var context = new org.springframework.context.annotation.AnnotationConfigApplicationContext()) {
            context.getEnvironment().setActiveProfiles("enquiry-merchant-api");
            context.registerBean(MerchantEnquiryRouteScopeAuthority.class,
                    () -> new MerchantEnquiryRouteScopeAuthority(Map.of("shop", SCOPE)));
            context.registerBean(ConfigurationReleaseActivation.class, () -> f.activation);
            context.registerBean(SemanticRegistrySnapshot.class, () -> f.semantic);
            context.registerBean(EnquirySubmissionStore.class, () -> f.store);
            context.registerBean(SessionTrustedExecutionContextEstablisher.class, f::authentication);
            context.registerBean(AudienceObservationContextEstablisher.class,
                    () -> new AudienceObservationContextEstablisher(f.sessions));
            context.registerBean(AudienceObservationAdmissionEvaluator.class, f::admission);
            context.registerBean(ActorAuthorisationAuthority.class, () -> f.m1.actor);
            context.registerBean(MerchantEnquiryObservationPrivileges.class,
                    () -> new MerchantEnquiryObservationPrivileges(f.m1.privileges));
            context.registerBean(Clock.class, () -> f.clock);
            context.register(MerchantEnquiryApiConfiguration.class, MerchantEnquiryController.class);
            context.refresh();
            MockMvcBuilders.standaloneSetup(context.getBean(MerchantEnquiryController.class)).build()
                    .perform(get(PATH).header("Authorization", f.authorization()))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.submission.question").value("Original question"));
        }
        assertThrows(IllegalArgumentException.class, () -> new MerchantEnquiryObservationPrivileges(Map.of()));
    }

    @Test
    void credential_to_exact_selected_response_does_not_expose_internal_model_or_session() throws Exception {
        var f = new Fixture();
        var response = f.mvc().perform(get(PATH).header("Authorization", f.authorization()))
                .andExpect(status().isOk()).andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(jsonPath("$.enquiryIdentity").value("E1"))
                .andExpect(jsonPath("$.submission.question").value("Original question"))
                .andExpect(jsonPath("$.contact.name").value("Alex"))
                .andExpect(jsonPath("$.subjectContext.submissionRevisionIdentity").value("R7"))
                .andReturn().getResponse().getContentAsString();
        for (var internal : List.of("merchant-a", "session", "modelIdentifier", "sourceEvidence", "requestBinding"))
            assertFalse(response.contains(internal));
    }

    @Test
    void withheld_family_is_absent_even_when_other_families_are_selected() throws Exception {
        var f = new Fixture();
        f.m1.actor = (scope, principal, privilege) ->
                !privilege.equals(f.m1.privileges.get(EnquiryMerchantExposureReferences.SUBMITTED_CONTACT));
        var response = f.mvc().perform(get(PATH).header("Authorization", f.authorization()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.contact").doesNotExist())
                .andExpect(jsonPath("$.submission.question").value("Original question"))
                .andReturn().getResponse().getContentAsString();
        assertFalse(response.contains("Alex"));
        f.m1.actor = (scope, principal, privilege) ->
                privilege.equals(f.m1.privileges.get(EnquiryMerchantExposureReferences.SUBMITTED_CONTACT));
        f.mvc().perform(get(PATH).header("Authorization", f.authorization()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.submission").doesNotExist())
                .andExpect(jsonPath("$.subjectContext").doesNotExist()).andExpect(jsonPath("$.contact.name").value("Alex"));
    }

    @Test
    void missing_invalid_or_revoked_credentials_and_membership_fail_before_material_reads() throws Exception {
        var f = new Fixture();
        var mvc = f.mvc();
        clearInvocations(f.m1.store);
        mvc.perform(get(PATH)).andExpect(status().isUnauthorized());
        mvc.perform(get(PATH).header("Authorization", "Bearer wrong")).andExpect(status().isUnauthorized());
        f.session = f.session.revoke(NOW, "test");
        mvc.perform(get(PATH).header("Authorization", f.authorization())).andExpect(status().isUnauthorized());
        verifyNoInteractions(f.m1.store);
        f = new Fixture();
        f.member.set(false);
        clearInvocations(f.m1.store);
        f.mvc().perform(get(PATH).header("Authorization", f.authorization())).andExpect(status().isForbidden());
        verifyNoInteractions(f.m1.store);
    }

    @Test
    void revocation_during_acquisition_and_current_family_privilege_changes_fail_closed() throws Exception {
        var f = new Fixture();
        doAnswer(c -> { f.member.set(false); return Optional.of(submission(true)); })
                .when(f.m1.store).submission(SCOPE, "E1");
        f.mvc().perform(get(PATH).header("Authorization", f.authorization())).andExpect(status().isForbidden())
                .andExpect(jsonPath("$.submission").doesNotExist());
        var other = new Fixture();
        var mvc = other.mvc();
        mvc.perform(get(PATH).header("Authorization", other.authorization())).andExpect(status().isOk());
        other.m1.authorised.set(false);
        mvc.perform(get(PATH).header("Authorization", other.authorization())).andExpect(status().isNotFound());
    }

    @Test
    void unregistered_locator_foreign_scope_absence_and_all_withheld_are_safe() throws Exception {
        var f = new Fixture();
        var mvc = f.mvc();
        var absent = mvc.perform(get(PATH.replace("E1", "missing")).header("Authorization", f.authorization()))
                .andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();
        mvc.perform(get(PATH.replace("shop", "merchant-a")).header("Authorization", f.authorization()))
                .andExpect(status().isNotFound()).andExpect(content().string(absent));
        mvc.perform(get(PATH.replace("shop", "other")).header("Authorization", f.authorization()))
                .andExpect(status().isNotFound()).andExpect(content().string(absent));
        f.m1.authorised.set(false);
        mvc.perform(get(PATH).header("Authorization", f.authorization()))
                .andExpect(status().isNotFound()).andExpect(content().string(absent));
    }

    @Test
    void caller_scope_principal_filters_and_duplicate_credentials_do_not_establish_authority() throws Exception {
        var f = new Fixture();
        var mvc = f.mvc();
        clearInvocations(f.m1.store);
        mvc.perform(get(PATH).header("Authorization", f.authorization()).param("principal", "admin"))
                .andExpect(status().isBadRequest());
        mvc.perform(get(PATH).header("Authorization", f.authorization(), "Bearer wrong"))
                .andExpect(status().isUnauthorized());
        verifyNoInteractions(f.m1.store);
    }

    @Test
    void serviceability_rejects_missing_progress_and_general_record_omits_optional_families() throws Exception {
        var f = new Fixture();
        when(f.m1.store.submission(SCOPE, "E1")).thenReturn(Optional.of(submission(false)));
        f.mvc().perform(get(PATH).header("Authorization", f.authorization())).andExpect(status().isOk())
                .andExpect(jsonPath("$.contact").doesNotExist()).andExpect(jsonPath("$.subjectContext").doesNotExist());
        var read = new EnquiryMerchantRepresentationM2Test.Fixture().read();
        var engine = new ProjectionServiceabilityEvaluationEngine(EnquiryMerchantQueryProjectionPortfolio.contracts(RELEASE),
                EnquiryMerchantQueryProjectionPortfolio.evaluators(RELEASE));
        assertEquals(ProjectionServiceabilityOutcome.FULLY_SERVICEABLE,
                engine.evaluate(ProjectionServiceabilityEvaluationRequest.forBoundedRead(read, NOW)).outcome());
        var evidence = read.sourceEvidence().iterator().next();
        var stale = new ProjectionSourceEvidence(evidence.sourceReference(), evidence.evidenceIdentifier(),
                evidence.observedProgressIdentifier(), Optional.of("another"), NOW, evidence.availability(),
                evidence.completeness(), evidence.revocationState());
        var bad = new BoundedProjectionRead(EnquiryMerchantRepresentationM2Test.request(SCOPE, ApiSurfaceClass.MERCHANT_OPERATIONAL),
                read.contractIdentity(), read.readUseIdentity(), Set.of(stale), read.fragments());
        assertEquals(ProjectionServiceabilityOutcome.NOT_SERVICEABLE,
                engine.evaluate(ProjectionServiceabilityEvaluationRequest.forBoundedRead(bad, NOW)).outcome());
    }

    @Test
    void unexpected_failures_are_safe_and_do_not_echo_private_details() throws Exception {
        var f = new Fixture();
        when(f.m1.store.submission(SCOPE, "E1")).thenThrow(new IllegalStateException("private SQL Alex"));
        var body = f.mvc().perform(get(PATH).header("Authorization", f.authorization()))
                .andExpect(status().isServiceUnavailable()).andReturn().getResponse().getContentAsString();
        assertFalse(body.contains("private"));
        assertFalse(body.contains("Alex"));
    }

    static class Fixture {
        final EnquiryMerchantExposureM1Test.Fixture m1 = new EnquiryMerchantExposureM1Test.Fixture();
        final SessionRecordStore sessions;
        final EnquirySubmissionStore store;
        final OpaqueSessionCredential credential = OpaqueSessionCredential.generate();
        SessionRecord session = new SessionRecord("session", "staff", credential.verifier(), NOW.minusSeconds(30),
                "assurance", "password", NOW.plusSeconds(3600), NOW.minusSeconds(30), "generation",
                Optional.empty(), Optional.empty());
        final Clock clock = Clock.fixed(NOW, ZoneOffset.UTC);
        final AtomicBoolean member = new AtomicBoolean(true);
        final IdentitySecurityGenerationAuthority generations = identity -> "generation";
        final ConfigurationReleaseActivation activation = mock(ConfigurationReleaseActivation.class);
        final SemanticRegistrySnapshot semantic = mock(SemanticRegistrySnapshot.class);

        Fixture() {
            this(null, null);
        }
        Fixture(EnquirySubmissionStore submissions, SessionRecordStore records) {
            store = submissions == null ? m1.store : submissions;
            sessions = records == null ? mock(SessionRecordStore.class) : records;
            if (records == null) {
                when(sessions.sessionByCredentialVerifier(credential.verifier())).thenAnswer(c -> Optional.of(session));
                when(sessions.sessionByIdentity("session")).thenAnswer(c -> Optional.of(session));
            } else {
                sessions.create(session);
            }
            when(semantic.version()).thenReturn(RELEASE);
            for (var merchant : List.of(SCOPE.merchantIdentifier(), "merchant-b"))
                when(activation.current(merchant)).thenReturn(Optional.of(TestReleases.activeRelease(merchant, RELEASE)));
        }
        String authorization() { return "Bearer " + credential.value(); }
        SessionTrustedExecutionContextEstablisher authentication() {
            return new SessionTrustedExecutionContextEstablisher(new SessionCredentialResolver(sessions, generations, clock),
                    (scope, identity) -> new ExecutionPrincipal(identity));
        }
        AudienceObservationAdmissionEvaluator admission() {
            return new AudienceObservationAdmissionEvaluator(
                    new DefaultAuthenticationSessionCurrentnessAuthority(sessions, generations, clock),
                    scope -> Optional.empty(), (scope, principal) -> member.get(),
                    (context, binding, time) -> AudienceObservationPlatformProtection.SATISFIED, clock);
        }
        MerchantEnquiryQuery query() {
            return MerchantEnquiryQuery.create(new MerchantEnquiryRouteScopeAuthority(Map.of(
                    "shop", SCOPE, "other", new MerchantScope("merchant-b"))), activation, semantic,
                    store, authentication(), new AudienceObservationContextEstablisher(sessions),
                    admission(), m1.actor, new MerchantEnquiryObservationPrivileges(m1.privileges), clock);
        }
        MockMvc mvc() { return MockMvcBuilders.standaloneSetup(new MerchantEnquiryController(query())).build(); }
    }
}
