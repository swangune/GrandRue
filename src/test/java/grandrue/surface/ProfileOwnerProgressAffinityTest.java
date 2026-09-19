package grandrue.surface;

import grandrue.application.MerchantScope;
import grandrue.merchantprofile.MerchantContactPointExposure;
import grandrue.merchantprofile.MerchantContactPointExposureChoiceReadPort;
import grandrue.merchantprofile.MerchantLocationExposure;
import grandrue.merchantprofile.MerchantLocationExposureChoiceReadPort;
import grandrue.merchantprofile.ProfileContactPointExposureChoiceEvaluator;
import grandrue.merchantprofile.ProfileMaterialAffinityObservationContributionRegistration;
import grandrue.merchantprofile.ProfileMerchantLocationExposureChoiceEvaluator;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * BR5 specification: current Profile exposure choices may govern bounded
 * material only when the owner can prove exact progress affinity to that
 * bounded material.
 */
class ProfileOwnerProgressAffinityTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final String RELEASE = "semantic-registry-1.0";
    private static final Instant EVALUATED_AT = Instant.parse("2026-09-03T05:00:00Z");
    private static final MerchantExposureChoiceSourceReference CONTACT_CHOICE_SOURCE =
            new MerchantExposureChoiceSourceReference(
                    "profile",
                    "contact-point-public-exposure"
            );
    private static final MerchantExposureChoiceSourceReference LOCATION_CHOICE_SOURCE =
            new MerchantExposureChoiceSourceReference(
                    "profile",
                    "merchant-location-public-exposure"
            );
    private static final ProjectionSourceDependencyReference CONTACT_MATERIAL_SOURCE =
            new ProjectionSourceDependencyReference("profile", "contact-points");
    private static final ProjectionSourceDependencyReference LOCATION_MATERIAL_SOURCE =
            new ProjectionSourceDependencyReference("profile", "merchant-locations");

    @Test
    void current_public_choice_does_not_authorize_bounded_contact_material_without_matching_progress_proof() {
        ExposureCandidateObservation candidate = contact("C1");
        OwnerExposureEvaluationContext context = contextWithAffinity(
                candidate,
                CONTACT_MATERIAL_SOURCE,
                "contact-revision-8"
        );
        MerchantContactPointExposureChoiceReadPort readPort =
                (merchantScope, identity) -> {
                    assertEquals(MERCHANT, merchantScope);
                    assertEquals("C1", identity);
                    return Optional.of(MerchantContactPointExposure.PUBLIC);
                };
        ProfileContactPointExposureChoiceEvaluator evaluator =
                new ProfileContactPointExposureChoiceEvaluator(readPort);

        MerchantExposureChoiceBatchEvaluation result = evaluator.evaluateBatch(
                CONTACT_CHOICE_SOURCE,
                context,
                List.of(submission(candidate))
        );

        assertEquals(
                MerchantExposureChoiceEvaluationDecision.UNRESOLVED,
                result.results().getFirst().decision()
        );
    }

    @Test
    void current_public_location_choice_is_unresolved_when_bounded_context_has_no_matching_location_affinity_entry() {
        OwnerExposureEvaluationContext context = contextWithAffinity(
                contact("C1"),
                CONTACT_MATERIAL_SOURCE,
                "contact-revision-8"
        );
        MerchantLocationExposureChoiceReadPort readPort =
                (merchantScope, identity) -> {
                    assertEquals(MERCHANT, merchantScope);
                    assertEquals("L1", identity);
                    return Optional.of(MerchantLocationExposure.PUBLIC);
                };
        ProfileMerchantLocationExposureChoiceEvaluator evaluator =
                new ProfileMerchantLocationExposureChoiceEvaluator(readPort);

        MerchantExposureChoiceBatchEvaluation result = evaluator.evaluateBatch(
                LOCATION_CHOICE_SOURCE,
                context,
                List.of(submission(location("L1")))
        );

        assertEquals(
                MerchantExposureChoiceEvaluationDecision.UNRESOLVED,
                result.results().getFirst().decision()
        );
    }

    @Test
    void contact_candidate_cannot_consume_location_material_affinity() {
        ExposureCandidateObservation candidate = contact("C1");
        OwnerExposureEvaluationContext context = contextWithAffinity(
                location("L1"),
                LOCATION_MATERIAL_SOURCE,
                "location-revision-8"
        );
        MerchantContactPointExposureChoiceReadPort readPort =
                new MerchantContactPointExposureChoiceReadPort() {
                    @Override
                    public Optional<MerchantContactPointExposure> currentActiveExposure(
                            MerchantScope merchantScope,
                            String identity
                    ) {
                        throw new AssertionError(
                                "Cross-source Contact affinity must fail before owner read"
                        );
                    }

                    @Override
                    public Optional<MerchantContactPointExposure> currentActiveExposureAtProgress(
                            MerchantScope merchantScope,
                            String identity,
                            String expectedProgressIdentifier
                    ) {
                        throw new AssertionError(
                                "Contact candidate must not consume Location progress"
                        );
                    }
                };

        MerchantExposureChoiceBatchEvaluation result =
                new ProfileContactPointExposureChoiceEvaluator(readPort).evaluateBatch(
                        CONTACT_CHOICE_SOURCE,
                        context,
                        List.of(submission(candidate))
                );

        assertEquals(
                MerchantExposureChoiceEvaluationDecision.UNRESOLVED,
                result.results().getFirst().decision()
        );
    }

    @Test
    void exact_contact_progress_proof_preserves_public_exposure() {
        ExposureCandidateObservation candidate = contact("C1");
        OwnerExposureEvaluationContext context = contextWithAffinity(
                candidate,
                CONTACT_MATERIAL_SOURCE,
                "contact-revision-8"
        );
        MerchantContactPointExposureChoiceReadPort readPort =
                new MerchantContactPointExposureChoiceReadPort() {
                    @Override
                    public Optional<MerchantContactPointExposure> currentActiveExposure(
                            MerchantScope merchantScope,
                            String identity
                    ) {
                        throw new AssertionError(
                                "Bounded Contact evaluation must use progress-affinity read"
                        );
                    }

                    @Override
                    public Optional<MerchantContactPointExposure> currentActiveExposureAtProgress(
                            MerchantScope merchantScope,
                            String identity,
                            String expectedProgressIdentifier
                    ) {
                        assertEquals(MERCHANT, merchantScope);
                        assertEquals("C1", identity);
                        assertEquals("contact-revision-8", expectedProgressIdentifier);
                        return Optional.of(MerchantContactPointExposure.PUBLIC);
                    }
                };

        MerchantExposureChoiceBatchEvaluation result =
                new ProfileContactPointExposureChoiceEvaluator(readPort).evaluateBatch(
                        CONTACT_CHOICE_SOURCE,
                        context,
                        List.of(submission(candidate))
                );

        assertEquals(
                MerchantExposureChoiceEvaluationDecision.EXPOSE,
                result.results().getFirst().decision()
        );
    }

    @Test
    void exact_contact_progress_proof_preserves_private_withhold() {
        ExposureCandidateObservation candidate = contact("C1");
        OwnerExposureEvaluationContext context = contextWithAffinity(
                candidate,
                CONTACT_MATERIAL_SOURCE,
                "contact-revision-8"
        );
        MerchantContactPointExposureChoiceReadPort readPort =
                new MerchantContactPointExposureChoiceReadPort() {
                    @Override
                    public Optional<MerchantContactPointExposure> currentActiveExposure(
                            MerchantScope merchantScope,
                            String identity
                    ) {
                        throw new AssertionError(
                                "Bounded Contact evaluation must use progress-affinity read"
                        );
                    }

                    @Override
                    public Optional<MerchantContactPointExposure> currentActiveExposureAtProgress(
                            MerchantScope merchantScope,
                            String identity,
                            String expectedProgressIdentifier
                    ) {
                        assertEquals("contact-revision-8", expectedProgressIdentifier);
                        return Optional.of(MerchantContactPointExposure.PRIVATE_INTERNAL);
                    }
                };

        MerchantExposureChoiceBatchEvaluation result =
                new ProfileContactPointExposureChoiceEvaluator(readPort).evaluateBatch(
                        CONTACT_CHOICE_SOURCE,
                        context,
                        List.of(submission(candidate))
                );

        assertEquals(
                MerchantExposureChoiceEvaluationDecision.WITHHOLD,
                result.results().getFirst().decision()
        );
    }

    @Test
    void exact_location_progress_proof_is_required_before_current_choice_can_expose() {
        ExposureCandidateObservation candidate = location("L1");
        OwnerExposureEvaluationContext context = contextWithAffinity(
                candidate,
                LOCATION_MATERIAL_SOURCE,
                "location-revision-8"
        );
        MerchantLocationExposureChoiceReadPort readPort =
                new MerchantLocationExposureChoiceReadPort() {
                    @Override
                    public Optional<MerchantLocationExposure> currentActiveExposure(
                            MerchantScope merchantScope,
                            String identity
                    ) {
                        throw new AssertionError(
                                "Bounded Location evaluation must use progress-affinity read"
                        );
                    }

                    @Override
                    public Optional<MerchantLocationExposure> currentActiveExposureAtProgress(
                            MerchantScope merchantScope,
                            String identity,
                            String expectedProgressIdentifier
                    ) {
                        assertEquals(MERCHANT, merchantScope);
                        assertEquals("L1", identity);
                        assertEquals("location-revision-8", expectedProgressIdentifier);
                        return Optional.of(MerchantLocationExposure.PUBLIC);
                    }
                };

        MerchantExposureChoiceBatchEvaluation result =
                new ProfileMerchantLocationExposureChoiceEvaluator(readPort).evaluateBatch(
                        LOCATION_CHOICE_SOURCE,
                        context,
                        List.of(submission(candidate))
                );

        assertEquals(
                MerchantExposureChoiceEvaluationDecision.EXPOSE,
                result.results().getFirst().decision()
        );
    }

    private static OwnerExposureEvaluationContext contextWithAffinity(
            ExposureCandidateObservation candidate,
            ProjectionSourceDependencyReference materialSource,
            String expectedProgress
    ) {
        EstablishedObservationRequest request = TestObservationRequests.request(
                MERCHANT,
                RELEASE
        );
        ProjectionSourceEvidence sourceEvidence = new ProjectionSourceEvidence(
                materialSource,
                "evidence-" + expectedProgress,
                Optional.of(expectedProgress),
                Optional.of(expectedProgress),
                EVALUATED_AT,
                ProjectionSourceAvailability.AVAILABLE,
                ProjectionSourceCompleteness.COMPLETE,
                ProjectionSourceRevocationState.NOT_APPLICABLE
        );
        ProjectionMaterialFragment fragment = new TestFragment(
                candidate,
                Set.of(new ProjectionMaterialSourceAffinity(
                        materialSource,
                        expectedProgress
                ))
        );
        BoundedProjectionRead read = new BoundedProjectionRead(
                request,
                new ProjectionContractIdentity("platform", "merchant-presence"),
                new ProjectionReadUseIdentity(
                        "platform",
                        "public-merchant-presence"
                ),
                Set.of(sourceEvidence),
                List.of(fragment)
        );
        EstablishedObservationContribution contribution =
                new ObservationContributionConstructionBoundary().construct(
                        request,
                        ProfileMaterialAffinityObservationContributionRegistration
                                .constructor(read)
                );
        AudienceObservationContext observationContext =
                new DefaultAudienceObservationContext(
                        request,
                        new PublicObservationSubject(),
                        Set.of(contribution)
                );
        AudienceObservationAdmissionResult admission =
                AudienceObservationAdmissionResults.admitted(
                        new DefaultAudienceObservationInvocationBinding(
                                EstablishedObservationRequestDetails
                                        .requestBinding(request)
                        ),
                        EVALUATED_AT
                );
        return OwnerExposureEvaluationContexts.forOwner(
                "profile",
                observationContext,
                admission
        );
    }

    private static ExposureCandidateEvaluationSubmission submission(
            ExposureCandidateObservation candidate
    ) {
        return new ExposureCandidateEvaluationSubmission(
                ExposureCandidateEvaluationBindings.issue(),
                candidate
        );
    }

    private static ExposureCandidateObservation contact(String identity) {
        return new ExposureCandidateObservation(
                new ExposableElementReference("profile", "public-contact-point"),
                Optional.of(new ExposureCandidateInstanceReference(
                        "profile",
                        "contact-point",
                        identity
                ))
        );
    }

    private static ExposureCandidateObservation location(String identity) {
        return new ExposureCandidateObservation(
                new ExposableElementReference("profile", "public-merchant-location"),
                Optional.of(new ExposureCandidateInstanceReference(
                        "profile",
                        "merchant-location",
                        identity
                ))
        );
    }

    private record TestFragment(
            ExposureCandidateObservation candidateObservation,
            Set<ProjectionMaterialSourceAffinity> sourceAffinities
    ) implements ProjectionMaterialFragment {
        private TestFragment {
            sourceAffinities = Set.copyOf(sourceAffinities);
        }
    }
}
