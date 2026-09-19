package grandrue.surface;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExposureEvaluatorRuntimeBindingSnapshotTest {

    @Test
    void requirement_bindings_are_exact_release_and_owner_qualified() {
        RequirementEvaluator profileEvaluator = new RequirementEvaluator();
        RequirementEvaluator protectionEvaluator = new RequirementEvaluator();
        ExposureRequirementReference profile =
                new ExposureRequirementReference("profile", "visible");
        ExposureRequirementReference protection =
                new ExposureRequirementReference("data-protection", "visible");

        ExposureRequirementEvaluatorBindingSnapshot snapshot =
                new ExposureRequirementEvaluatorBindingSnapshot(
                        "release-2",
                        List.of(
                                new ExposureRequirementEvaluatorBinding(
                                        profile,
                                        profileEvaluator
                                ),
                                new ExposureRequirementEvaluatorBinding(
                                        protection,
                                        protectionEvaluator
                                )
                        )
                );

        assertEquals("release-2", snapshot.semanticRegistryReleaseIdentifier());
        assertSame(profileEvaluator, snapshot.evaluator(profile).orElseThrow());
        assertSame(protectionEvaluator, snapshot.evaluator(protection).orElseThrow());
        assertTrue(snapshot.evaluator(
                new ExposureRequirementReference("profile", "other")
        ).isEmpty());
        assertThrows(
                UnsupportedOperationException.class,
                () -> snapshot.bindings().clear()
        );
    }

    @Test
    void merchant_choice_bindings_are_exact_release_and_do_not_fallback() {
        ChoiceEvaluator evaluator = new ChoiceEvaluator();
        MerchantExposureChoiceSourceReference exact =
                new MerchantExposureChoiceSourceReference(
                        "profile",
                        "contact-point-public-exposure"
                );

        MerchantExposureChoiceEvaluatorBindingSnapshot snapshot =
                new MerchantExposureChoiceEvaluatorBindingSnapshot(
                        "release-3",
                        List.of(new MerchantExposureChoiceEvaluatorBinding(
                                exact,
                                evaluator
                        ))
                );

        assertEquals("release-3", snapshot.semanticRegistryReleaseIdentifier());
        assertSame(evaluator, snapshot.evaluator(exact).orElseThrow());
        assertTrue(snapshot.evaluator(
                new MerchantExposureChoiceSourceReference(
                        "profile",
                        "merchant-location-public-exposure"
                )
        ).isEmpty());
    }

    @Test
    void duplicate_exact_semantic_references_are_rejected() {
        ExposureRequirementReference requirement =
                new ExposureRequirementReference("profile", "visible");
        MerchantExposureChoiceSourceReference choice =
                new MerchantExposureChoiceSourceReference(
                        "profile",
                        "contact-point-public-exposure"
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> new ExposureRequirementEvaluatorBindingSnapshot(
                        "release-2",
                        List.of(
                                new ExposureRequirementEvaluatorBinding(
                                        requirement,
                                        new RequirementEvaluator()
                                ),
                                new ExposureRequirementEvaluatorBinding(
                                        requirement,
                                        new RequirementEvaluator()
                                )
                        )
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new MerchantExposureChoiceEvaluatorBindingSnapshot(
                        "release-2",
                        List.of(
                                new MerchantExposureChoiceEvaluatorBinding(
                                        choice,
                                        new ChoiceEvaluator()
                                ),
                                new MerchantExposureChoiceEvaluatorBinding(
                                        choice,
                                        new ChoiceEvaluator()
                                )
                        )
                )
        );
    }

    @Test
    void release_identifier_is_mandatory_and_binding_sets_are_immutable() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ExposureRequirementEvaluatorBindingSnapshot(
                        " ",
                        List.of()
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new MerchantExposureChoiceEvaluatorBindingSnapshot(
                        "",
                        List.of()
                )
        );

        ExposureRequirementEvaluatorBindingSnapshot requirements =
                new ExposureRequirementEvaluatorBindingSnapshot(
                        "release-1",
                        List.of()
                );
        MerchantExposureChoiceEvaluatorBindingSnapshot choices =
                new MerchantExposureChoiceEvaluatorBindingSnapshot(
                        "release-1",
                        List.of()
                );
        assertEquals(Set.of(), requirements.bindings());
        assertEquals(Set.of(), choices.bindings());
    }

    private static final class RequirementEvaluator
            implements ExposureRequirementEvaluator {

        @Override
        public ExposureRequirementBatchEvaluation evaluateBatch(
                ExposureRequirementReference reference,
                OwnerExposureEvaluationContext context,
                List<ExposureCandidateEvaluationSubmission> submissions
        ) {
            throw new UnsupportedOperationException("not used by E4-F binding tests");
        }
    }

    private static final class ChoiceEvaluator
            implements MerchantExposureChoiceEvaluator {

        @Override
        public MerchantExposureChoiceBatchEvaluation evaluateBatch(
                MerchantExposureChoiceSourceReference reference,
                OwnerExposureEvaluationContext context,
                List<ExposureCandidateEvaluationSubmission> submissions
        ) {
            throw new UnsupportedOperationException("not used by E4-F binding tests");
        }
    }
}
