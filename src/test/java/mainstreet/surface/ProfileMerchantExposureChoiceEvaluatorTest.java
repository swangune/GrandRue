package mainstreet.surface;

import mainstreet.application.MerchantScope;
import mainstreet.merchantprofile.MerchantContactPointExposure;
import mainstreet.merchantprofile.MerchantContactPointExposureChoiceReadPort;
import mainstreet.merchantprofile.MerchantLocationExposure;
import mainstreet.merchantprofile.MerchantLocationExposureChoiceReadPort;
import mainstreet.merchantprofile.ProfileContactPointExposureChoiceEvaluator;
import mainstreet.merchantprofile.ProfileMerchantLocationExposureChoiceEvaluator;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProfileMerchantExposureChoiceEvaluatorTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final String RELEASE = "semantic-registry-1.0";
    private static final Instant EVALUATED_AT = Instant.parse("2026-09-02T12:00:00Z");
    private static final MerchantExposureChoiceSourceReference CONTACT_SOURCE =
            new MerchantExposureChoiceSourceReference(
                    "profile",
                    "contact-point-public-exposure"
            );
    private static final MerchantExposureChoiceSourceReference LOCATION_SOURCE =
            new MerchantExposureChoiceSourceReference(
                    "profile",
                    "merchant-location-public-exposure"
            );

    @Test
    void contact_point_evaluator_maps_current_active_owner_choice_per_instance() {
        MerchantContactPointExposureChoiceReadPort readPort = (merchantScope, identity) -> {
            assertEquals(MERCHANT, merchantScope);
            return switch (identity) {
                case "C1" -> Optional.of(MerchantContactPointExposure.PUBLIC);
                case "C2" -> Optional.of(MerchantContactPointExposure.PRIVATE_INTERNAL);
                default -> Optional.empty();
            };
        };
        ProfileContactPointExposureChoiceEvaluator evaluator =
                new ProfileContactPointExposureChoiceEvaluator(readPort);
        List<ExposureCandidateEvaluationSubmission> submissions = List.of(
                submission(contact("C1")),
                submission(contact("C2")),
                submission(contact("C3"))
        );

        MerchantExposureChoiceBatchEvaluation result = evaluator.evaluateBatch(
                CONTACT_SOURCE,
                context(),
                submissions
        );

        assertEquals(CONTACT_SOURCE, result.choiceSourceReference());
        assertEquals(
                submissions.stream()
                        .map(ExposureCandidateEvaluationSubmission::evaluationBinding)
                        .toList(),
                result.results().stream()
                        .map(MerchantExposureChoiceCandidateEvaluation::evaluationBinding)
                        .toList()
        );
        assertEquals(
                List.of(
                        MerchantExposureChoiceEvaluationDecision.EXPOSE,
                        MerchantExposureChoiceEvaluationDecision.WITHHOLD,
                        MerchantExposureChoiceEvaluationDecision.UNRESOLVED
                ),
                result.results().stream()
                        .map(MerchantExposureChoiceCandidateEvaluation::decision)
                        .toList()
        );
    }

    @Test
    void location_evaluator_maps_current_active_owner_choice_and_absence_is_unresolved() {
        MerchantLocationExposureChoiceReadPort readPort = (merchantScope, identity) -> {
            assertEquals(MERCHANT, merchantScope);
            return switch (identity) {
                case "L1" -> Optional.of(MerchantLocationExposure.PUBLIC);
                case "L2" -> Optional.of(MerchantLocationExposure.PRIVATE_INTERNAL);
                default -> Optional.empty();
            };
        };
        ProfileMerchantLocationExposureChoiceEvaluator evaluator =
                new ProfileMerchantLocationExposureChoiceEvaluator(readPort);
        List<ExposureCandidateEvaluationSubmission> submissions = List.of(
                submission(location("L1")),
                submission(location("L2")),
                submission(location("L3"))
        );

        MerchantExposureChoiceBatchEvaluation result = evaluator.evaluateBatch(
                LOCATION_SOURCE,
                context(),
                submissions
        );

        assertEquals(LOCATION_SOURCE, result.choiceSourceReference());
        assertEquals(
                submissions.stream()
                        .map(ExposureCandidateEvaluationSubmission::evaluationBinding)
                        .toList(),
                result.results().stream()
                        .map(MerchantExposureChoiceCandidateEvaluation::evaluationBinding)
                        .toList()
        );
        assertEquals(
                List.of(
                        MerchantExposureChoiceEvaluationDecision.EXPOSE,
                        MerchantExposureChoiceEvaluationDecision.WITHHOLD,
                        MerchantExposureChoiceEvaluationDecision.UNRESOLVED
                ),
                result.results().stream()
                        .map(MerchantExposureChoiceCandidateEvaluation::decision)
                        .toList()
        );
    }

    @Test
    void evaluators_reject_wrong_semantic_source_or_candidate_shape() {
        ProfileContactPointExposureChoiceEvaluator evaluator =
                new ProfileContactPointExposureChoiceEvaluator(
                        (merchantScope, identity) -> Optional.of(
                                MerchantContactPointExposure.PUBLIC
                        )
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> evaluator.evaluateBatch(
                        new MerchantExposureChoiceSourceReference(
                                "profile",
                                "merchant-location-public-exposure"
                        ),
                        context(),
                        List.of(submission(contact("C1")))
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> evaluator.evaluateBatch(
                        CONTACT_SOURCE,
                        context(),
                        List.of(submission(new ExposureCandidateObservation(
                                new ExposableElementReference(
                                        "profile",
                                        "public-contact-point"
                                ),
                                Optional.of(new ExposureCandidateInstanceReference(
                                        "profile",
                                        "merchant-location",
                                        "L1"
                                ))
                        )))
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> evaluator.evaluateBatch(
                        CONTACT_SOURCE,
                        context(),
                        List.of(submission(new ExposureCandidateObservation(
                                new ExposableElementReference(
                                        "profile",
                                        "public-contact-point"
                                ),
                                Optional.empty()
                        )))
                )
        );
    }

    @Test
    void profile_evaluator_dependencies_are_narrow_read_only_ports() {
        assertEquals(
                Set.of("currentActiveExposure"),
                Arrays.stream(MerchantContactPointExposureChoiceReadPort.class.getMethods())
                        .filter(method -> method.getDeclaringClass()
                                == MerchantContactPointExposureChoiceReadPort.class)
                        .map(java.lang.reflect.Method::getName)
                        .collect(java.util.stream.Collectors.toUnmodifiableSet())
        );
        assertEquals(
                Set.of("currentActiveExposure"),
                Arrays.stream(MerchantLocationExposureChoiceReadPort.class.getMethods())
                        .filter(method -> method.getDeclaringClass()
                                == MerchantLocationExposureChoiceReadPort.class)
                        .map(java.lang.reflect.Method::getName)
                        .collect(java.util.stream.Collectors.toUnmodifiableSet())
        );
        assertTrue(Arrays.stream(ProfileContactPointExposureChoiceEvaluator.class.getDeclaredFields())
                .noneMatch(field -> field.getType().getSimpleName().endsWith("Authority")));
        assertTrue(Arrays.stream(ProfileMerchantLocationExposureChoiceEvaluator.class.getDeclaredFields())
                .noneMatch(field -> field.getType().getSimpleName().endsWith("Authority")));
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

    private static OwnerExposureEvaluationContext context() {
        EstablishedObservationRequest request =
                new DefaultEstablishedObservationRequest(
                        TestReleases.activeRelease(
                                MERCHANT.merchantIdentifier(),
                                RELEASE
                        ),
                        MERCHANT,
                        RELEASE,
                        Optional.empty()
                );
        AudienceObservationContext observationContext =
                new DefaultAudienceObservationContext(
                        request,
                        new PublicObservationSubject()
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
}
