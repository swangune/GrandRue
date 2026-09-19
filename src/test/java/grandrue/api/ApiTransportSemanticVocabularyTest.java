package grandrue.api;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiTransportSemanticVocabularyTest {

    @Test
    void command_outcomes_preserve_the_exact_transport_execution_classes() {
        assertEquals(
                EnumSet.of(
                        ApiCommandOutcomeClass.COMPLETED,
                        ApiCommandOutcomeClass.ACCEPTED_PENDING,
                        ApiCommandOutcomeClass.REJECTED,
                        ApiCommandOutcomeClass.OUTCOME_UNCERTAIN
                ),
                EnumSet.allOf(ApiCommandOutcomeClass.class)
        );
    }

    @Test
    void problem_categories_preserve_the_closed_client_relevant_vocabulary() {
        assertEquals(
                EnumSet.of(
                        ApiProblemCategory.INVALID_REQUEST,
                        ApiProblemCategory.UNAUTHENTICATED,
                        ApiProblemCategory.NOT_AUTHORISED,
                        ApiProblemCategory.NOT_FOUND_OR_NOT_ACCESSIBLE,
                        ApiProblemCategory.NOT_APPLICABLE,
                        ApiProblemCategory.NOT_ENTITLED,
                        ApiProblemCategory.TRUST_REQUIREMENT_UNSATISFIED,
                        ApiProblemCategory.OPERATIONALLY_INELIGIBLE,
                        ApiProblemCategory.PROVIDER_UNAVAILABLE,
                        ApiProblemCategory.REPRESENTATION_UNAVAILABLE,
                        ApiProblemCategory.CONFLICT,
                        ApiProblemCategory.IDEMPOTENCY_CONFLICT,
                        ApiProblemCategory.RATE_LIMITED,
                        ApiProblemCategory.OUTCOME_UNCERTAIN,
                        ApiProblemCategory.TEMPORARILY_UNAVAILABLE,
                        ApiProblemCategory.INTERNAL_FAILURE
                ),
                EnumSet.allOf(ApiProblemCategory.class)
        );
    }
}
