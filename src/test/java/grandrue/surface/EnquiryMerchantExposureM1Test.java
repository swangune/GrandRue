package grandrue.surface;

import grandrue.application.MerchantScope;
import grandrue.enquiry.*;
import grandrue.runtime.*;
import grandrue.semantic.Privilege;
import grandrue.semantic.registry.OwnedOperationalObjectTypeReference;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnquiryMerchantExposureM1Test {
    static final MerchantScope SCOPE = new MerchantScope("merchant-a");
    static final Instant NOW = Instant.parse("2026-09-05T21:00:00Z");
    static final String RELEASE = "semantic-registry-1.0";

    @Test
    void registers_exact_four_merchant_only_instance_qualified_families() {
        var registry = EnquiryMerchantExposureContractPortfolio.forRelease(RELEASE);
        assertEquals(4, registry.contracts().size());
        for (var element : EnquiryMerchantExposureReferences.elements()) {
            var contract = registry.contract(RELEASE, element, SurfaceAudience.MERCHANT).orElseThrow();
            assertEquals(ExposureDecision.EXPOSE, contract.baselineDecision());
            assertEquals(Set.of(EnquiryMerchantExposureReferences.OBSERVATION_REQUIREMENT),
                    contract.requirementReferences());
            assertEquals(ExposureMemberIdentitySpecification.instanceQualified(
                    EnquiryMerchantExposureReferences.ENQUIRY_INSTANCE_KIND), contract.memberIdentitySpecification());
            assertTrue(registry.contract(RELEASE, element, SurfaceAudience.PUBLIC).isEmpty());
            assertTrue(registry.contract(RELEASE, element, SurfaceAudience.CUSTOMER).isEmpty());
        }
    }

    @Test
    void candidates_contain_only_available_e1_owned_families_and_no_communication_placeholder() {
        var f = new Fixture();
        var source = new EnquiryMerchantExposureCandidateSource(f.store);
        assertEquals(3, source.currentCandidates(SCOPE, "E1").size());
        when(f.store.submission(SCOPE, "E1")).thenReturn(Optional.of(submission(false)));
        assertEquals(List.of(EnquiryMerchantExposureReferences.candidate(
                EnquiryMerchantExposureReferences.SUBMISSION_CONTENT, "E1")), source.currentCandidates(SCOPE, "E1"));
        assertTrue(source.currentCandidates(SCOPE, "missing").isEmpty());
        assertTrue(source.currentCandidates(new MerchantScope("other"), "E1").isEmpty());
    }

    @Test
    void legitimate_merchant_with_current_actor_privileges_exposes_exact_three_available_families() {
        var f = new Fixture();
        assertEquals(3, f.resolve(f.context(SCOPE, SurfaceAudience.MERCHANT), f.candidates()).size());
        verify(f.store, atLeastOnce()).submission(SCOPE, "E1");
    }

    @Test
    void privilege_revocation_is_current_and_precedes_submission_reads() {
        var f = new Fixture();
        var context = f.context(SCOPE, SurfaceAudience.MERCHANT);
        assertEquals(3, f.resolve(context, f.candidates()).size());
        clearInvocations(f.store);
        f.authorised.set(false);
        assertTrue(f.resolve(context, f.candidates()).isEmpty());
        verifyNoInteractions(f.store);
    }

    @Test
    void individual_privilege_mapping_can_withhold_contact_without_hiding_submission() {
        var f = new Fixture();
        f.actor = (scope, principal, privilege) ->
                !privilege.equals(f.privileges.get(EnquiryMerchantExposureReferences.SUBMITTED_CONTACT));
        assertEquals(2, f.resolve(f.context(SCOPE, SurfaceAudience.MERCHANT), f.candidates()).size());
    }

    @Test
    void unregistered_or_failed_actor_authority_never_defaults_to_allow() {
        var f = new Fixture();
        assertThrows(IllegalArgumentException.class,
                () -> new EnquiryMerchantExposureRequirementEvaluator(f.store, f.actor, Map.of()));
        f.actor = (scope, principal, privilege) -> { throw new IllegalStateException("Unavailable"); };
        assertTrue(f.resolve(f.context(SCOPE, SurfaceAudience.MERCHANT), f.candidates()).isEmpty());
        verifyNoInteractions(f.store);
    }

    @Test
    void public_and_customer_audiences_never_receive_stored_enquiry_exposure() {
        var f = new Fixture();
        assertTrue(f.resolve(f.context(SCOPE, SurfaceAudience.PUBLIC), f.candidates()).isEmpty());
        assertTrue(f.resolve(f.context(SCOPE, SurfaceAudience.CUSTOMER), f.candidates()).isEmpty());
        verifyNoInteractions(f.store);
    }

    @Test
    void missing_cross_merchant_or_unavailable_material_cannot_be_exposed_by_forged_candidates() {
        var f = new Fixture();
        f.actor = (scope, principal, privilege) -> true;
        assertTrue(f.resolve(f.context(new MerchantScope("other"), SurfaceAudience.MERCHANT),
                f.candidates()).isEmpty());
        when(f.store.submission(SCOPE, "E1")).thenReturn(Optional.of(submission(false)));
        var absent = List.of(
                EnquiryMerchantExposureReferences.candidate(EnquiryMerchantExposureReferences.SUBMITTED_CONTACT, "E1"),
                EnquiryMerchantExposureReferences.candidate(EnquiryMerchantExposureReferences.SUBJECT_CONTEXT, "E1"),
                EnquiryMerchantExposureReferences.candidate(EnquiryMerchantExposureReferences.COMMUNICATION_SUMMARY, "E1"),
                EnquiryMerchantExposureReferences.candidate(EnquiryMerchantExposureReferences.SUBMISSION_CONTENT, "missing"));
        assertTrue(f.resolve(f.context(SCOPE, SurfaceAudience.MERCHANT), absent).isEmpty());
    }

    @Test
    void expired_authentication_and_lost_merchant_association_reject_before_owner_evaluation() {
        var f = new Fixture();
        var context = f.context(SCOPE, SurfaceAudience.MERCHANT);
        f.current.set(false);
        assertFalse(f.admission().evaluate(context).admitted());
        assertThrows(RuntimeException.class, () -> f.resolve(context, f.candidates()));
        f.current.set(true);
        f.member.set(false);
        assertFalse(f.admission().evaluate(context).admitted());
        assertThrows(RuntimeException.class, () -> f.resolve(context, f.candidates()));
        verifyNoInteractions(f.store);
    }

    @Test
    void absent_requirement_binding_fails_closed() {
        var f = new Fixture();
        var context = f.context(SCOPE, SurfaceAudience.MERCHANT);
        assertTrue(new ExposureResolver().resolve(context, f.admission().evaluate(context), f.candidates(),
                EnquiryMerchantExposureContractPortfolio.forRelease(RELEASE),
                new ExposureRequirementEvaluatorBindingSnapshot(RELEASE, List.of()),
                new MerchantExposureChoiceEvaluatorBindingSnapshot(RELEASE, List.of())).isEmpty());
    }

    static EnquirySubmission submission(boolean optional) {
        return new EnquirySubmission(SCOPE, "E1", NOW, "Original question",
                new EnquirySubmittedContact(optional ? Optional.of("Alex") : Optional.empty(),
                        Optional.empty(), Optional.empty()),
                new EnquirySemanticContext(RELEASE, "model", 1),
                optional ? Optional.of(new EnquiryRevisionProvenance(
                        new OwnedOperationalObjectTypeReference("publication", "opportunity"), "O1", "R7"))
                        : Optional.empty(), Optional.empty());
    }

    static final class Fixture {
        final EnquirySubmissionStore store = mock(EnquirySubmissionStore.class);
        final AtomicBoolean authorised = new AtomicBoolean(true);
        final AtomicBoolean current = new AtomicBoolean(true);
        final AtomicBoolean member = new AtomicBoolean(true);
        final Map<ExposableElementReference, Privilege> privileges =
                EnquiryMerchantExposureReferences.elements().stream().collect(Collectors.toMap(
                        element -> element, element -> new Privilege("test-read-" + element.elementIdentifier())));
        ActorAuthorisationAuthority actor = (scope, principal, privilege) ->
                scope.equals(SCOPE) && principal.identifier().equals("staff") && authorised.get();

        Fixture() { when(store.submission(SCOPE, "E1")).thenReturn(Optional.of(submission(true))); }

        List<ExposureCandidateObservation> candidates() {
            return List.of(EnquiryMerchantExposureReferences.SUBMISSION_CONTENT,
                    EnquiryMerchantExposureReferences.SUBMITTED_CONTACT,
                    EnquiryMerchantExposureReferences.SUBJECT_CONTEXT).stream()
                    .map(element -> EnquiryMerchantExposureReferences.candidate(element, "E1")).toList();
        }

        AudienceObservationAdmissionEvaluator admission() {
            return new AudienceObservationAdmissionEvaluator(
                    provenance -> current.get() ? AuthenticationSessionCurrentness.CURRENT
                            : AuthenticationSessionCurrentness.NOT_CURRENT,
                    scope -> Optional.empty(), (scope, principal) -> member.get(),
                    (context, binding, time) -> AudienceObservationPlatformProtection.SATISFIED,
                    Clock.fixed(NOW, ZoneOffset.UTC));
        }

        AudienceObservationContext context(MerchantScope scope, SurfaceAudience audience) {
            var request = new DefaultEstablishedObservationRequest(
                    TestReleases.activeRelease(scope.merchantIdentifier(), RELEASE), scope, RELEASE, Optional.empty());
            var execution = new TrustedExecutionContext(scope, new ExecutionPrincipal("staff"),
                    Optional.of(new AuthenticationProvenance("session", "staff", NOW.minusSeconds(30))));
            return new DefaultAudienceObservationContext(request, switch (audience) {
                case PUBLIC -> new PublicObservationSubject();
                case CUSTOMER -> new CustomerPrincipalObservationSubject(execution);
                case MERCHANT -> new MerchantInteractiveObservationSubject(execution);
            });
        }

        List<ResolvedExposedElement> resolve(AudienceObservationContext context,
                List<ExposureCandidateObservation> candidates) {
            return new ExposureResolver().resolve(context, admission().evaluate(context), candidates,
                    EnquiryMerchantExposureContractPortfolio.forRelease(RELEASE),
                    new ExposureRequirementEvaluatorBindingSnapshot(RELEASE, List.of(
                            new ExposureRequirementEvaluatorBinding(EnquiryMerchantExposureReferences.OBSERVATION_REQUIREMENT,
                                    new EnquiryMerchantExposureRequirementEvaluator(store, actor, privileges)))),
                    new MerchantExposureChoiceEvaluatorBindingSnapshot(RELEASE, List.of()));
        }
    }
}
