package grandrue.surface;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExposureEvaluatorSubmissionAffinityContractTest {

    @Test
    void e4_issues_fresh_opaque_bindings_for_repeated_stable_candidate_submissions() {
        ExposureCandidateObservation candidate = candidate("C1");

        ExposureCandidateEvaluationBinding first =
                ExposureCandidateEvaluationBindings.issue();
        ExposureCandidateEvaluationBinding second =
                ExposureCandidateEvaluationBindings.issue();

        assertNotEquals(first, second);
        assertFalse(first.getClass().isRecord());
        assertEquals(
                List.of(),
                Arrays.stream(first.getClass().getDeclaredMethods())
                        .filter(method -> Modifier.isPublic(method.getModifiers()))
                        .filter(method -> !method.getName().equals("equals"))
                        .filter(method -> !method.getName().equals("hashCode"))
                        .map(java.lang.reflect.Method::getName)
                        .toList()
        );

        ExposureCandidateEvaluationSubmission submission =
                new ExposureCandidateEvaluationSubmission(first, candidate);

        assertSame(first, submission.evaluationBinding());
        assertEquals(candidate, submission.candidate());
        assertThrows(
                NullPointerException.class,
                () -> new ExposureCandidateEvaluationSubmission(null, candidate)
        );
        assertThrows(
                NullPointerException.class,
                () -> new ExposureCandidateEvaluationSubmission(first, null)
        );
    }

    @Test
    void requirement_and_choice_results_echo_only_binding_and_decision() {
        ExposureCandidateEvaluationBinding binding =
                ExposureCandidateEvaluationBindings.issue();

        ExposureRequirementCandidateEvaluation requirement =
                new ExposureRequirementCandidateEvaluation(
                        binding,
                        ExposureRequirementEvaluationDecision.SATISFIED
                );
        MerchantExposureChoiceCandidateEvaluation choice =
                new MerchantExposureChoiceCandidateEvaluation(
                        binding,
                        MerchantExposureChoiceEvaluationDecision.EXPOSE
                );

        assertSame(binding, requirement.evaluationBinding());
        assertSame(binding, choice.evaluationBinding());
        assertEquals(
                List.of("evaluationBinding", "decision"),
                java.util.Arrays.stream(ExposureRequirementCandidateEvaluation.class.getRecordComponents())
                        .map(java.lang.reflect.RecordComponent::getName)
                        .toList()
        );
        assertEquals(
                List.of("evaluationBinding", "decision"),
                java.util.Arrays.stream(MerchantExposureChoiceCandidateEvaluation.class.getRecordComponents())
                        .map(java.lang.reflect.RecordComponent::getName)
                        .toList()
        );
    }

    @Test
    void raw_batches_preserve_duplicate_submission_bindings_for_e4_validation() {
        ExposureCandidateEvaluationBinding binding =
                ExposureCandidateEvaluationBindings.issue();
        ExposureRequirementReference requirementReference =
                new ExposureRequirementReference("profile", "public-readable");
        MerchantExposureChoiceSourceReference choiceReference =
                new MerchantExposureChoiceSourceReference("profile", "public-exposure");

        ExposureRequirementBatchEvaluation requirementBatch =
                new ExposureRequirementBatchEvaluation(
                        requirementReference,
                        List.of(
                                new ExposureRequirementCandidateEvaluation(
                                        binding,
                                        ExposureRequirementEvaluationDecision.SATISFIED
                                ),
                                new ExposureRequirementCandidateEvaluation(
                                        binding,
                                        ExposureRequirementEvaluationDecision.UNRESOLVED
                                )
                        )
                );
        MerchantExposureChoiceBatchEvaluation choiceBatch =
                new MerchantExposureChoiceBatchEvaluation(
                        choiceReference,
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

        assertEquals(2, requirementBatch.results().size());
        assertEquals(2, choiceBatch.results().size());
    }

    @Test
    void evaluator_batch_boundary_accepts_submission_wrappers_not_raw_candidates() {
        assertEquals(
                "java.util.List<grandrue.surface.ExposureCandidateEvaluationSubmission>",
                ExposureRequirementEvaluator.class.getMethods()[0]
                        .getGenericParameterTypes()[2]
                        .getTypeName()
        );
        assertEquals(
                "java.util.List<grandrue.surface.ExposureCandidateEvaluationSubmission>",
                MerchantExposureChoiceEvaluator.class.getMethods()[0]
                        .getGenericParameterTypes()[2]
                        .getTypeName()
        );
    }

    private static ExposureCandidateObservation candidate(String identity) {
        return new ExposureCandidateObservation(
                new ExposableElementReference("profile", "public-contact-point"),
                Optional.of(new ExposureCandidateInstanceReference(
                        "profile",
                        "contact-point",
                        identity
                ))
        );
    }
}
