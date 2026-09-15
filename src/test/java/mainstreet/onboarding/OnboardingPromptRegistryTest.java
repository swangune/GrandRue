package mainstreet.onboarding;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OnboardingPromptRegistryTest {

    private static final OnboardingQuestionDefinitionVersion V1 =
            new OnboardingQuestionDefinitionVersion("v1");

    @Test
    void rejects_duplicate_prompt_scope_keys() {
        OnboardingPromptDefinition definition = definition("question");

        assertThrows(
                IllegalArgumentException.class,
                () -> new OnboardingPromptRegistry(
                        List.of(definition, definition),
                        List.of()
                )
        );
    }

    @Test
    void rejects_mapping_for_unregistered_option_definition() {
        OnboardingPromptDefinition definition = definition("question");

        assertThrows(
                IllegalArgumentException.class,
                () -> new OnboardingPromptRegistry(
                        List.of(definition),
                        List.of(new OnboardingDiscoveryMapping(
                                definition.key().questionIdentity(),
                                V1,
                                "UNKNOWN",
                                Set.of(new OnboardingSemanticSeed(
                                        "test",
                                        "seed"
                                ))
                        ))
                )
        );
    }

    @Test
    void single_select_rejects_more_than_one_registered_option() {
        OnboardingPromptDefinition definition =
                new OnboardingPromptDefinition(
                        new OnboardingPromptKey(
                                new OnboardingQuestionIdentity(
                                        "onboarding.test",
                                        "single"
                                ),
                                Optional.empty()
                        ),
                        V1,
                        OnboardingPromptClass.DISCOVERY_QUESTION,
                        OnboardingAnswerForm.SINGLE_SELECT,
                        OnboardingPromptPriority.HIGH_BRANCH_DISCOVERY,
                        Map.of(V1, Set.of("YES", "NO")),
                        OnboardingPromptApplicability.always()
                );
        OnboardingAnswerEvidence answer = new OnboardingAnswerEvidence(
                definition.key().questionIdentity(),
                V1,
                OnboardingAnswerForm.SINGLE_SELECT,
                Set.of("YES", "NO"),
                Optional.empty(),
                Optional.empty(),
                OnboardingAnswerOrigin.MERCHANT_SELECTED,
                "identity-42",
                Instant.parse("2026-08-28T23:45:00Z"),
                Optional.empty()
        );

        assertEquals(false, definition.accepts(answer));
    }

    @Test
    void supports_registered_alternative_applicability_clauses() {
        OnboardingSemanticSeed a =
                new OnboardingSemanticSeed("test", "a");
        OnboardingSemanticSeed b =
                new OnboardingSemanticSeed("test", "b");
        OnboardingSemanticSeed c =
                new OnboardingSemanticSeed("test", "c");
        OnboardingPromptApplicability applicability =
                OnboardingPromptApplicability.satisfiedByAnyOf(
                        List.of(Set.of(a, b), Set.of(c))
                );

        assertTrue(applicability.isSatisfiedBy(Set.of(a, b)));
        assertTrue(applicability.isSatisfiedBy(Set.of(c)));
        assertEquals(false, applicability.isSatisfiedBy(Set.of(a)));
    }

    private static OnboardingPromptDefinition definition(String identifier) {
        return new OnboardingPromptDefinition(
                new OnboardingPromptKey(
                        new OnboardingQuestionIdentity(
                                "onboarding.test",
                                identifier
                        ),
                        Optional.empty()
                ),
                V1,
                OnboardingPromptClass.DISCOVERY_QUESTION,
                OnboardingAnswerForm.SINGLE_SELECT,
                OnboardingPromptPriority.HIGH_BRANCH_DISCOVERY,
                Map.of(V1, Set.of("YES")),
                OnboardingPromptApplicability.always()
        );
    }
}
