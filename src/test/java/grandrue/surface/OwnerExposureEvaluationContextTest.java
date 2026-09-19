package grandrue.surface;

import grandrue.application.MerchantScope;
import grandrue.runtime.ExecutionPrincipal;
import grandrue.runtime.TrustedExecutionContext;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OwnerExposureEvaluationContextTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final String RELEASE = "semantic-registry-1.0";
    private static final Instant EVALUATED_AT = Instant.parse("2026-09-02T12:00:00Z");

    @Test
    void owner_context_filters_contributions_and_reuses_one_admission_instant() {
        EstablishedObservationRequest request = request();
        TestContribution profile = contribution("profile", "profile-evidence", request);
        TestContribution booking = contribution("booking", "booking-evidence", request);
        AudienceObservationContext context = new DefaultAudienceObservationContext(
                request,
                new PublicObservationSubject(),
                Set.of(profile, booking)
        );

        OwnerExposureEvaluationContext ownerContext =
                OwnerExposureEvaluationContexts.forOwner(
                        "profile",
                        context,
                        admitted(request)
                );

        assertEquals(MERCHANT, ownerContext.merchantScope());
        assertEquals(SurfaceAudience.PUBLIC, ownerContext.audience());
        assertEquals(EVALUATED_AT, ownerContext.evaluatedAt());
        assertEquals(Set.of(profile), ownerContext.contributions());
        assertTrue(ownerContext.contextualAccessProof().isEmpty());
        assertEquals(SurfaceAudience.PUBLIC, ownerContext.subjectBinding().audience());
        assertTrue(ownerContext.subjectBinding().principalIdentifier().isEmpty());
        assertTrue(ownerContext.subjectBinding().contextualAccessKind().isEmpty());
        assertTrue(ownerContext.subjectBinding().contextualAccessBinding().isEmpty());
        assertThrows(
                UnsupportedOperationException.class,
                () -> ownerContext.contributions().add(booking)
        );
    }

    @Test
    void contextual_proof_is_visible_only_to_its_owner_while_subject_binding_stays_non_secret() {
        EstablishedObservationRequest request = request();
        TestContextualProof proof = new TestContextualProof(
                new ContextualAccessProofKind("booking", "guest-booking-access"),
                EstablishedObservationRequestDetails.requestBinding(request),
                MERCHANT,
                "guest-1",
                "booking-access-binding-1"
        );
        TrustedExecutionContext execution = new TrustedExecutionContext(
                MERCHANT,
                new ExecutionPrincipal("guest-1"),
                Optional.empty()
        );
        AudienceObservationContext context = new DefaultAudienceObservationContext(
                request,
                new CustomerContextualObservationSubject(execution, proof),
                Set.of()
        );
        AudienceObservationAdmissionResult admission = admitted(request);

        OwnerExposureEvaluationContext booking =
                OwnerExposureEvaluationContexts.forOwner(
                        "booking",
                        context,
                        admission
                );
        OwnerExposureEvaluationContext profile =
                OwnerExposureEvaluationContexts.forOwner(
                        "profile",
                        context,
                        admission
                );

        assertEquals(Optional.of(proof), booking.contextualAccessProof());
        assertTrue(profile.contextualAccessProof().isEmpty());
        assertEquals(SurfaceAudience.CUSTOMER, booking.subjectBinding().audience());
        assertEquals(Optional.of("guest-1"), booking.subjectBinding().principalIdentifier());
        assertEquals(Optional.of(proof.kind()), booking.subjectBinding().contextualAccessKind());
        assertEquals(Optional.of(proof.accessBinding()), booking.subjectBinding().contextualAccessBinding());
        assertEquals(booking.subjectBinding(), profile.subjectBinding());
    }

    @Test
    void rejected_or_cross_request_admission_cannot_construct_owner_context() {
        EstablishedObservationRequest request = request();
        AudienceObservationContext context = new DefaultAudienceObservationContext(
                request,
                new PublicObservationSubject()
        );
        AudienceObservationInvocationBinding binding =
                new DefaultAudienceObservationInvocationBinding(
                        EstablishedObservationRequestDetails.requestBinding(request)
                );
        AudienceObservationAdmissionResult rejected =
                AudienceObservationAdmissionResults.rejected(
                        binding,
                        EVALUATED_AT,
                        AudienceObservationAdmissionFailure.PLATFORM_PROTECTION_UNSATISFIED
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> OwnerExposureEvaluationContexts.forOwner(
                        "profile",
                        context,
                        rejected
                )
        );

        EstablishedObservationRequest other = request();
        assertThrows(
                IllegalArgumentException.class,
                () -> OwnerExposureEvaluationContexts.forOwner(
                        "profile",
                        context,
                        admitted(other)
                )
        );
    }

    @Test
    void public_owner_context_contract_exposes_only_the_approved_shared_core() {
        assertEquals(
                Set.of(
                        "merchantScope",
                        "audience",
                        "subjectBinding",
                        "evaluatedAt",
                        "contributions",
                        "contextualAccessProof"
                ),
                Arrays.stream(OwnerExposureEvaluationContext.class.getMethods())
                        .filter(method -> method.getDeclaringClass()
                                == OwnerExposureEvaluationContext.class)
                        .map(Method::getName)
                        .collect(Collectors.toUnmodifiableSet())
        );
        assertFalse(Arrays.stream(OwnerExposureEvaluationContext.class.getMethods())
                .map(Method::getReturnType)
                .anyMatch(TrustedExecutionContext.class::equals));
    }

    private static EstablishedObservationRequest request() {
        return new DefaultEstablishedObservationRequest(
                TestReleases.activeRelease(MERCHANT.merchantIdentifier(), RELEASE),
                MERCHANT,
                RELEASE,
                Optional.empty()
        );
    }

    private static AudienceObservationAdmissionResult admitted(
            EstablishedObservationRequest request
    ) {
        return AudienceObservationAdmissionResults.admitted(
                new DefaultAudienceObservationInvocationBinding(
                        EstablishedObservationRequestDetails.requestBinding(request)
                ),
                EVALUATED_AT
        );
    }

    private static TestContribution contribution(
            String owner,
            String identifier,
            EstablishedObservationRequest request
    ) {
        return new TestContribution(
                new ObservationContributionKind(owner, identifier),
                EstablishedObservationRequestDetails.requestBinding(request),
                MERCHANT
        );
    }

    private record TestContribution(
            ObservationContributionKind kind,
            ObservationRequestBinding requestBinding,
            MerchantScope merchantScope
    ) implements EstablishedObservationContribution {
    }

    private record TestContextualProof(
            ContextualAccessProofKind kind,
            ObservationRequestBinding requestBinding,
            MerchantScope merchantScope,
            String principalIdentifier,
            String accessBinding
    ) implements EstablishedContextualAccessProof {
    }
}
