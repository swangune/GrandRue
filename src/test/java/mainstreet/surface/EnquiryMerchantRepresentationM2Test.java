package mainstreet.surface;

import mainstreet.api.ApiContractIdentity;
import mainstreet.api.ApiSurfaceClass;
import mainstreet.application.MerchantScope;
import mainstreet.enquiry.*;
import mainstreet.runtime.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static mainstreet.surface.EnquiryMerchantExposureM1Test.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnquiryMerchantRepresentationM2Test {
    private static final ApiContractIdentity API = new ApiContractIdentity("enquiry", "merchant-enquiry-query");

    @Test
    void one_immutable_read_separates_content_contact_and_submission_time_subject_provenance() {
        var f = new Fixture();
        var read = f.read();
        assertEquals(3, read.fragments().size());
        assertTrue(read.sourceEvidence().iterator().next().isCurrent());
        var content = (EnquiryMerchantRepresentation.SubmissionContent) material(read, 0);
        assertEquals("Original question", content.question());
        assertEquals(NOW, content.submittedAt());
        var contact = (EnquiryMerchantRepresentation.SubmittedContact) material(read, 1);
        assertEquals(Optional.of("Alex"), contact.submittedContact().name());
        var subject = (EnquiryMerchantRepresentation.SubjectContext) material(read, 2);
        assertEquals("R7", subject.submissionTimeSubject().revisionIdentity());
        assertEquals(submission(true).subjectRevision().orElseThrow(), subject.submissionTimeSubject());
        assertThrows(UnsupportedOperationException.class, () -> read.fragments().clear());
    }

    @Test
    void denied_contact_is_absent_from_assembled_material_and_assembly_never_reloads_values() {
        var f = new Fixture();
        f.m1.actor = (scope, principal, privilege) ->
                !privilege.equals(f.m1.privileges.get(EnquiryMerchantExposureReferences.SUBMITTED_CONTACT));
        var read = f.read();
        var exposure = f.exposure(read);
        clearInvocations(f.m1.store);
        var result = new MerchantProjectionAssemblyService().assemble(read, serviceability(read, true), exposure);
        assertEquals(2, result.selectedFragments().size());
        assertSame(read.binding(), result.boundedProjectionReadBinding());
        assertSame(read.fragments().getFirst(), result.selectedFragments().getFirst());
        assertTrue(result.selectedFragments().stream().map(fragment ->
                ((EnquiryMerchantRepresentationProjectionFragment) fragment).representation())
                .noneMatch(EnquiryMerchantRepresentation.SubmittedContact.class::isInstance));
        assertTrue(result.selectedFragments().stream().map(fragment ->
                ((EnquiryMerchantRepresentationProjectionFragment) fragment).representation().toString())
                .noneMatch(value -> value.contains("Alex")));
        verifyNoInteractions(f.m1.store);
    }

    @Test
    void revocation_between_material_read_and_exposure_withholds_all_values() {
        var f = new Fixture();
        var read = f.read();
        f.m1.authorised.set(false);
        assertTrue(new MerchantProjectionAssemblyService()
                .assemble(read, serviceability(read, true), f.exposure(read)).selectedFragments().isEmpty());
        assertEquals(3, read.fragments().size());
    }

    @Test
    void missing_record_reports_missing_source_and_cannot_be_rescued_by_exposure() {
        var f = new Fixture();
        when(f.m1.store.submission(SCOPE, "E1")).thenReturn(Optional.empty());
        var read = f.read();
        assertTrue(read.fragments().isEmpty());
        assertFalse(read.sourceEvidence().iterator().next().isCurrent());
        assertThrows(ProjectionFragmentSelectionStructuralException.class,
                () -> new MerchantProjectionAssemblyService().assemble(read, serviceability(read, false), f.exposure(read)));
    }

    @Test
    void nonserviceable_result_rejects_even_with_positive_merchant_exposure() {
        var f = new Fixture();
        var read = f.read();
        var exposure = f.exposure(read);
        assertEquals(3, exposure.exposedElements().members().size());
        assertThrows(ProjectionFragmentSelectionStructuralException.class,
                () -> new MerchantProjectionAssemblyService().assemble(read, serviceability(read, false), exposure));
    }

    @Test
    void serviceability_and_exposure_from_another_read_or_request_are_rejected() {
        var f = new Fixture();
        var read = f.read();
        var second = f.read();
        assertThrows(ProjectionFragmentSelectionStructuralException.class,
                () -> new MerchantProjectionAssemblyService().assemble(read, serviceability(second, true), f.exposure(read)));
        var other = new Fixture();
        assertThrows(ProjectionFragmentSelectionStructuralException.class,
                () -> new MerchantProjectionAssemblyService().assemble(read, serviceability(read, true),
                        other.exposure(other.read())));
    }

    @Test
    void wrong_merchant_observation_cannot_be_bound_to_the_request() {
        var f = new Fixture();
        var observation = new AuthorityBackedEnquiryMerchantRepresentationProjectionReadPort(f.m1.store)
                .observe(SCOPE, "E1", NOW);
        var otherRequest = request(new MerchantScope("other"), ApiSurfaceClass.MERCHANT_OPERATIONAL);
        assertThrows(IllegalStateException.class, () -> observation.toBoundedRead(otherRequest));
    }

    @Test
    void general_enquiry_has_no_contact_subject_or_communication_placeholders() {
        var f = new Fixture();
        when(f.m1.store.submission(SCOPE, "E1")).thenReturn(Optional.of(submission(false)));
        var read = f.read();
        assertEquals(1, read.fragments().size());
        assertInstanceOf(EnquiryMerchantRepresentation.SubmissionContent.class, material(read, 0));
    }

    @Test
    void public_exposure_result_cannot_be_used_as_a_merchant_assembly() {
        var f = new Fixture();
        var request = request(SCOPE, ApiSurfaceClass.PUBLIC);
        var read = new AuthorityBackedEnquiryMerchantRepresentationProjectionReadPort(f.m1.store)
                .observe(SCOPE, "E1", NOW).toBoundedRead(request);
        var context = new DefaultAudienceObservationContext(request, new PublicObservationSubject());
        var admission = f.m1.admission().evaluate(context);
        var exposed = new ApiExposureResolutionBinder().bind(
                new DefaultApiAudienceObservationContext(API, ApiSurfaceClass.PUBLIC, context),
                admission, RELEASE, List.of()).resolution().orElseThrow();
        assertThrows(IllegalArgumentException.class,
                () -> new MerchantProjectionAssemblyService().assemble(read, serviceability(read, true), exposed));
    }

    @Test
    void positive_exposure_for_another_enquiry_cannot_select_material_from_this_read() {
        var f = new Fixture();
        var read = f.read();
        var original = submission(true);
        when(f.m1.store.submission(SCOPE, "E2")).thenReturn(Optional.of(new EnquirySubmission(
                SCOPE, "E2", original.submittedAt(), original.question(), original.contact(),
                original.semanticContext(), original.subjectRevision(), original.customerContextIdentity())));
        var otherRead = new AuthorityBackedEnquiryMerchantRepresentationProjectionReadPort(f.m1.store)
                .observe(SCOPE, "E2", NOW).toBoundedRead(f.request);
        assertThrows(ProjectionFragmentSelectionStructuralException.class,
                () -> new MerchantProjectionAssemblyService().assemble(read, serviceability(read, true), f.exposure(otherRead)));
    }

    static EnquiryMerchantRepresentation material(BoundedProjectionRead read, int index) {
        return ((EnquiryMerchantRepresentationProjectionFragment) read.fragments().get(index)).representation();
    }

    static ProjectionServiceabilityResult serviceability(BoundedProjectionRead read, boolean serviceable) {
        return new ProjectionServiceabilityResult(RELEASE, read.contractIdentity(), read.readUseIdentity(), NOW,
                serviceable ? ProjectionServiceabilityOutcome.FULLY_SERVICEABLE : ProjectionServiceabilityOutcome.NOT_SERVICEABLE,
                serviceable ? Set.of() : Set.of(ProjectionServiceabilityReasonCode.POLICY_EVALUATOR_UNRESOLVED),
                Set.of(), read.sourceEvidence(), Set.of(), Set.of(), Optional.of(read.binding()));
    }

    static EstablishedObservationRequest request(MerchantScope scope, ApiSurfaceClass surface) {
        return new DefaultEstablishedObservationRequest(TestReleases.activeRelease(scope.merchantIdentifier(), RELEASE),
                scope, RELEASE, Optional.of(new ApiObservationRequestProvenance(API, surface, "merchant-enquiry-query")));
    }

    static final class Fixture {
        final EnquiryMerchantExposureM1Test.Fixture m1 = new EnquiryMerchantExposureM1Test.Fixture();
        final EstablishedObservationRequest request = request(SCOPE, ApiSurfaceClass.MERCHANT_OPERATIONAL);

        BoundedProjectionRead read() {
            return new AuthorityBackedEnquiryMerchantRepresentationProjectionReadPort(m1.store)
                    .observe(SCOPE, "E1", NOW).toBoundedRead(request);
        }

        ApiExposureResolution exposure(BoundedProjectionRead read) {
            return exposure(read, m1.store);
        }

        ApiExposureResolution exposure(BoundedProjectionRead read, EnquirySubmissionStore store) {
            var execution = new TrustedExecutionContext(SCOPE, new ExecutionPrincipal("staff"),
                    Optional.of(new AuthenticationProvenance("session", "staff", NOW.minusSeconds(30))));
            var context = new DefaultAudienceObservationContext(request, new MerchantInteractiveObservationSubject(execution));
            var admission = m1.admission().evaluate(context);
            var resolved = new ExposureResolver().resolve(context, admission,
                    read.fragments().stream().map(ProjectionMaterialFragment::candidateObservation).toList(),
                    EnquiryMerchantExposureContractPortfolio.forRelease(RELEASE),
                    new ExposureRequirementEvaluatorBindingSnapshot(RELEASE, List.of(
                            new ExposureRequirementEvaluatorBinding(EnquiryMerchantExposureReferences.OBSERVATION_REQUIREMENT,
                                    new EnquiryMerchantExposureRequirementEvaluator(store, m1.actor, m1.privileges)))),
                    new MerchantExposureChoiceEvaluatorBindingSnapshot(RELEASE, List.of()));
            return new ApiExposureResolutionBinder().bind(
                    new DefaultApiAudienceObservationContext(API, ApiSurfaceClass.MERCHANT_OPERATIONAL, context),
                    admission, RELEASE, resolved).resolution().orElseThrow();
        }
    }
}
