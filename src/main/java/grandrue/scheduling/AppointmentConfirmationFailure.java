package grandrue.scheduling;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/** Precise failed authoritative confirmation attempt after acceptance. */
public record AppointmentConfirmationFailure(
        MerchantScope merchantScope,
        String identifier,
        String schedulingRequestIdentifier,
        String timeProposalIdentifier,
        String reasonIdentifier,
        String governingReleaseIdentifier,
        Instant failedAt
) {

    public AppointmentConfirmationFailure {
        Objects.requireNonNull(merchantScope);
        requireIdentifier(identifier, "Confirmation attempt identifier");
        requireIdentifier(
                schedulingRequestIdentifier,
                "Scheduling request identifier"
        );
        requireIdentifier(
                timeProposalIdentifier,
                "Time proposal identifier"
        );
        requireIdentifier(reasonIdentifier, "Failure reason identifier");
        requireIdentifier(
                governingReleaseIdentifier,
                "Governing release identifier"
        );
        Objects.requireNonNull(failedAt);
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
