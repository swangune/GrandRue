package grandrue.scheduling;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/** Correlates an accepted proposal and request to a committed Appointment. */
public record SchedulingCommitmentCorrelation(
        MerchantScope merchantScope,
        String identifier,
        String schedulingRequestIdentifier,
        String timeProposalIdentifier,
        String appointmentIdentifier,
        String governingReleaseIdentifier,
        Instant committedAt
) {

    public SchedulingCommitmentCorrelation {
        Objects.requireNonNull(merchantScope);
        requireIdentifier(identifier, "Commitment correlation identifier");
        requireIdentifier(
                schedulingRequestIdentifier,
                "Scheduling request identifier"
        );
        requireIdentifier(
                timeProposalIdentifier,
                "Time proposal identifier"
        );
        requireIdentifier(appointmentIdentifier, "Appointment identifier");
        requireIdentifier(
                governingReleaseIdentifier,
                "Governing release identifier"
        );
        Objects.requireNonNull(committedAt);
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
