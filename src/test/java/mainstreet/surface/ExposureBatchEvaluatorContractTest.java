package mainstreet.surface;

import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExposureBatchEvaluatorContractTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final String RELEASE = "semantic-registry-1.0";
    private static final Instant EVALUATED_AT = Instant.parse("2026-09-02T12:00:00Z");

    @Test
    void candidate_observation_preserves_exact_optional_stable_instance_identity() {
        ExposableElementReference element =
                new ExposableElementReference("profile", "public-contact-point");
        ExposureCandidateInstanceReference instance =
                new ExposureCandidateInstanceReference(
                        "profile",
                        "contact-point",
                        "C1"
                );

        ExposureCandidateObservation candidate =
                new ExposureCandidateObservation(
                        element,
                        Optional.of(instance)
                );

        assertEquals(element, candidate.elementReference());
        assertEquals(Optional.of(instance), candidate.instanceReference());
        assertThrows(
                IllegalArgumentException.class,
                () -> new ExposureCandidateObservation(
                        element,
                        Optional.of(new ExposureCandidateInstanceReference(
                                "booking",
                                "booking",
                                "B1"
                        ))
                )
        );
    }

    @Test
    void requirement_evaluator_is_one_batch_call_with_exact_reference_context_and_immutable_submissions() {
        RecordingRequirementEvaluator evaluator =
                new RecordingRequirementEvaluator();
        ExposureRequirementReference reference =
                new ExposureRequirementReference("data-protection", "public-readable");
        OwnerExposureEvaluationContext context = context("data-protection");
        List<ExposureCandidateEvaluationSubmission> submissions = List.of(
                submission(singleton("profile", "public-display-name")),
                submission(instance("profile", "public-contact-point", "contact-point", "C1"))
        );

        ExposureRequirementBatchEvaluation result = evaluator.evaluateBatch(
                reference,
                context,
                submissions
        );

        assertEquals(1, evaluator.calls);
        assertEquals(reference, evaluator.reference);
        assertSame(context, evaluator.context);
        assertEquals(submissions, evaluator.submissions);
        assertThrows(
                UnsupportedOperationException.class,
                () -> evaluator.submissions.add(
                        submission(singleton("profile", "public-tagline"))
                )
        );
        assertEquals(reference, result.requirementReference());
        assertEquals(2, result.results().size());
        assertEquals(
                submissions.stream()
                        .map(ExposureCandidateEvaluationSubmission::evaluationBinding)
                        .toList(),
                result.results().stream()
                        .map(ExposureRequirementCandidateEvaluation::evaluationBinding)
                        .toList()
        );
    }

    @Test
    void merchant_choice_evaluator_is_one_batch_call_with_exact_reference_context_and_immutable_submissions() {
        RecordingChoiceEvaluator evaluator = new RecordingChoiceEvaluator();
        MerchantExposureChoiceSourceReference reference =
                new MerchantExposureChoiceSourceReference(
                        "profile",
                        "contact-point-public-exposure"
                );
        OwnerExposureEvaluationContext context = context("profile");
        List<ExposureCandidateEvaluationSubmission> submissions = List.of(
                submission(instance("profile", "public-contact-point", "contact-point", "C1")),
                submission(instance("profile", "public-contact-point", "contact-point", "C2"))
        );

        MerchantExposureChoiceBatchEvaluation result = evaluator.evaluateBatch(
                reference,
                context,
                submissions
        );

        assertEquals(1, evaluator.calls);
        assertEquals(reference, evaluator.reference);
        assertSame(context, evaluator.context);
        assertEquals(submissions, evaluator.submissions);
        assertEquals(reference, result.choiceSourceReference());
        assertEquals(2, result.results().size());
    }

    @Test
    void result_vocabularies_are_closed_and_batch_results_do_not_silently_deduplicate() {
        assertEquals(
                Set.of("SATISFIED", "UNSATISFIED", "UNRESOLVED"),
                Arrays.stream(ExposureRequirementEvaluationDecision.values())
                        .map(Enum::name)
                        .collect(java.util.stream.Collectors.toUnmodifiableSet())
        );
        assertEquals(
                Set.of("EXPOSE", "WITHHOLD", "UNRESOLVED"),
                Arrays.stream(MerchantExposureChoiceEvaluationDecision.values())
                        .map(Enum::name)
                        .collect(java.util.stream.Collectors.toUnmodifiableSet())
        );

        ExposureCandidateEvaluationBinding binding =
                ExposureCandidateEvaluationBindings.issue();
        ExposureRequirementReference requirement =
                new ExposureRequirementReference("profile", "visible");
        List<ExposureRequirementCandidateEvaluation> requirementResults =
                new ArrayList<>(List.of(
                        new ExposureRequirementCandidateEvaluation(
                                binding,
                                ExposureRequirementEvaluationDecision.SATISFIED
                        ),
                        new ExposureRequirementCandidateEvaluation(
                                binding,
                                ExposureRequirementEvaluationDecision.UNRESOLVED
                        )
                ));
        ExposureRequirementBatchEvaluation requirementBatch =
                new ExposureRequirementBatchEvaluation(
                        requirement,
                        requirementResults
                );
        requirementResults.clear();
        assertEquals(2, requirementBatch.results().size());
        assertThrows(
                UnsupportedOperationException.class,
                () -> requirementBatch.results().clear()
        );

        MerchantExposureChoiceSourceReference choice =
                new MerchantExposureChoiceSourceReference("profile", "choice");
        MerchantExposureChoiceBatchEvaluation choiceBatch =
                new MerchantExposureChoiceBatchEvaluation(
                        choice,
                        List.of(
                                new MerchantExposureChoiceCandidateEvaluation(
                                        binding,
                                        MerchantExposureChoiceEvaluationDecision.EXPOSE
                                ),
                                new MerchantExposureChoiceCandidateEvaluation(
                                        binding,
                                        MerchantExposureChoiceEvaluationDecision.WITHHOLD
                                )
                        )
                );
        assertEquals(2, choiceBatch.results().size());
    }

    @Test
    void evaluator_interfaces_expose_only_the_batch_boundary() {
        assertEquals(
                Set.of("evaluateBatch"),
                Arrays.stream(ExposureRequirementEvaluator.class.getMethods())
                        .filter(method -> method.getDeclaringClass()
                                == ExposureRequirementEvaluator.class)
                        .map(java.lang.reflect.Method::getName)
                        .collect(java.util.stream.Collectors.toUnmodifiableSet())
        );
        assertEquals(
                Set.of("evaluateBatch"),
                Arrays.stream(MerchantExposureChoiceEvaluator.class.getMethods())
                        .filter(method -> method.getDeclaringClass()
                                == MerchantExposureChoiceEvaluator.class)
                        .map(java.lang.reflect.Method::getName)
                        .collect(java.util.stream.Collectors.toUnmodifiableSet())
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

    private static ExposureCandidateObservation singleton(
            String owner,
            String element
    ) {
        return new ExposureCandidateObservation(
                new ExposableElementReference(owner, element),
                Optional.empty()
        );
    }

    private static ExposureCandidateObservation instance(
            String owner,
            String element,
            String kind,
            String instance
    ) {
        return new ExposureCandidateObservation(
                new ExposableElementReference(owner, element),
                Optional.of(new ExposureCandidateInstanceReference(
                        owner,
                        kind,
                        instance
                ))
        );
    }

    private static OwnerExposureEvaluationContext context(String owner) {
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
                owner,
                observationContext,
                admission
        );
    }

    private static final class RecordingRequirementEvaluator
            implements ExposureRequirementEvaluator {

        private int calls;
        private ExposureRequirementReference reference;
        private OwnerExposureEvaluationContext context;
        private List<ExposureCandidateEvaluationSubmission> submissions;

        @Override
        public ExposureRequirementBatchEvaluation evaluateBatch(
                ExposureRequirementReference reference,
                OwnerExposureEvaluationContext context,
                List<ExposureCandidateEvaluationSubmission> submissions
        ) {
            calls++;
            this.reference = reference;
            this.context = context;
            this.submissions = List.copyOf(submissions);
            return new ExposureRequirementBatchEvaluation(
                    reference,
                    submissions.stream()
                            .map(submission -> new ExposureRequirementCandidateEvaluation(
                                    submission.evaluationBinding(),
                                    ExposureRequirementEvaluationDecision.SATISFIED
                            ))
                            .toList()
            );
        }
    }

    private static final class RecordingChoiceEvaluator
            implements MerchantExposureChoiceEvaluator {

        private int calls;
        private MerchantExposureChoiceSourceReference reference;
        private OwnerExposureEvaluationContext context;
        private List<ExposureCandidateEvaluationSubmission> submissions;

        @Override
        public MerchantExposureChoiceBatchEvaluation evaluateBatch(
                MerchantExposureChoiceSourceReference reference,
                OwnerExposureEvaluationContext context,
                List<ExposureCandidateEvaluationSubmission> submissions
        ) {
            calls++;
            this.reference = reference;
            this.context = context;
            this.submissions = List.copyOf(submissions);
            return new MerchantExposureChoiceBatchEvaluation(
                    reference,
                    submissions.stream()
                            .map(submission -> new MerchantExposureChoiceCandidateEvaluation(
                                    submission.evaluationBinding(),
                                    MerchantExposureChoiceEvaluationDecision.EXPOSE
                            ))
                            .toList()
            );
        }
    }
}
