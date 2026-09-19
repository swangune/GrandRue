package grandrue.onboarding;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OnboardingAnswerEvidenceTest {

    private static final Instant NOW =
            Instant.parse("2026-08-28T23:00:00Z");

    @Test
    void preserves_exact_question_definition_and_answer_provenance() {
        OnboardingAnswerEvidence evidence = new OnboardingAnswerEvidence(
                new OnboardingQuestionIdentity(
                        "onboarding.discovery",
                        "customer-interactions"
                ),
                new OnboardingQuestionDefinitionVersion("v2"),
                OnboardingAnswerForm.MULTI_SELECT,
                Set.of("PUBLISH_INFORMATION", "SEND_ENQUIRY"),
                Optional.empty(),
                Optional.of("merchant-wide"),
                OnboardingAnswerOrigin.MERCHANT_SELECTED,
                "identity-42",
                NOW,
                Optional.of("semantic-release-7")
        );

        assertEquals(
                Set.of("PUBLISH_INFORMATION", "SEND_ENQUIRY"),
                evidence.answerOptionIdentifiers()
        );
        assertEquals(
                new OnboardingQuestionDefinitionVersion("v2"),
                evidence.questionDefinitionVersion()
        );
        assertEquals(
                Optional.of("semantic-release-7"),
                evidence.semanticRegistryRelease()
        );
    }

    @Test
    void requires_exactly_one_structured_answer_representation() {
        assertThrows(
                IllegalArgumentException.class,
                () -> answer(Set.of(), Optional.empty())
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> answer(
                        Set.of("SEND_ENQUIRY"),
                        Optional.of("value-reference")
                )
        );
    }

    private static OnboardingAnswerEvidence answer(
            Set<String> options,
            Optional<String> valueReference
    ) {
        return new OnboardingAnswerEvidence(
                new OnboardingQuestionIdentity(
                        "onboarding.discovery",
                        "customer-interactions"
                ),
                new OnboardingQuestionDefinitionVersion("v1"),
                OnboardingAnswerForm.MULTI_SELECT,
                options,
                valueReference,
                Optional.empty(),
                OnboardingAnswerOrigin.MERCHANT_SELECTED,
                "identity-42",
                NOW,
                Optional.empty()
        );
    }
}
