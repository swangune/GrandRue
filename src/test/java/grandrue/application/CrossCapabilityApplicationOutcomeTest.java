package grandrue.application;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CrossCapabilityApplicationOutcomeTest {

    @Test
    void accepted_pending_preserves_committed_progress_instead_of_reporting_total_failure() {
        CommittedProgressReference activation = new CommittedProgressReference(
                "configuration",
                "ConfigurationRevisionActivated",
                "activation-1"
        );
        CrossCapabilityApplicationOutcome outcome = new CrossCapabilityApplicationOutcome(
                new ApplicationRequestIdentity("setup-request-1"),
                ApplicationOutcomeClassification.ACCEPTED_PENDING,
                List.of(activation),
                Optional.of("commercial.initial-trial.establish"),
                Optional.of("COMMERCIAL_TEMPORARILY_UNAVAILABLE")
        );

        assertEquals(List.of(activation), outcome.committedProgress());
        assertEquals(
                "commercial.initial-trial.establish",
                outcome.pendingResponsibilityIdentifier().orElseThrow()
        );
    }

    @Test
    void rejected_and_conflict_outcomes_cannot_hide_known_committed_progress() {
        CommittedProgressReference committed = new CommittedProgressReference(
                "configuration",
                "ConfigurationRevisionActivated",
                "activation-1"
        );

        assertThrows(IllegalArgumentException.class, () -> new CrossCapabilityApplicationOutcome(
                new ApplicationRequestIdentity("request-1"),
                ApplicationOutcomeClassification.REJECTED,
                List.of(committed),
                Optional.empty(),
                Optional.of("DOWNSTREAM_REJECTED")
        ));
        assertThrows(IllegalArgumentException.class, () -> new CrossCapabilityApplicationOutcome(
                new ApplicationRequestIdentity("request-1"),
                ApplicationOutcomeClassification.CONFLICT,
                List.of(committed),
                Optional.empty(),
                Optional.of("STALE_STATE")
        ));
    }

    @Test
    void completed_outcome_cannot_claim_pending_work() {
        assertThrows(IllegalArgumentException.class, () -> new CrossCapabilityApplicationOutcome(
                new ApplicationRequestIdentity("request-1"),
                ApplicationOutcomeClassification.COMPLETED,
                List.of(),
                Optional.of("notification.dispatch"),
                Optional.empty()
        ));
    }

    @Test
    void application_request_identity_is_a_distinct_value_type() {
        ApplicationRequestIdentity requestIdentity = new ApplicationRequestIdentity("trace-1");
        assertEquals("trace-1", requestIdentity.value());
        assertThrows(
                IllegalArgumentException.class,
                () -> new ApplicationRequestIdentity(" ")
        );
    }
}
