package grandrue.scheduling;

import mainstreet.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/**
 * Customer scheduling intent. Recording this fact does not reserve, allocate
 * or commit capacity.
 */
public record SchedulingRequest(
        MerchantScope merchantScope,
        String identifier,
        String customerContextIdentifier,
        String scheduledOperationIdentifier,
        String governingReleaseIdentifier,
        SchedulingRequestStatus status,
        Instant requestedAt
) {

    public SchedulingRequest {
        Objects.requireNonNull(merchantScope);
        requireIdentifier(identifier, "Scheduling request identifier");
        requireIdentifier(
                customerContextIdentifier,
                "Customer context identifier"
        );
        requireIdentifier(
                scheduledOperationIdentifier,
                "Scheduled operation identifier"
        );
        requireIdentifier(
                governingReleaseIdentifier,
                "Governing release identifier"
        );
        Objects.requireNonNull(status);
        Objects.requireNonNull(requestedAt);
    }

    public SchedulingRequest resolved() {
        if (status == SchedulingRequestStatus.RESOLVED) {
            return this;
        }
        if (status != SchedulingRequestStatus.REQUESTED) {
            throw new IllegalStateException(
                    "Only an open scheduling request can be resolved"
            );
        }
        return new SchedulingRequest(
                merchantScope,
                identifier,
                customerContextIdentifier,
                scheduledOperationIdentifier,
                governingReleaseIdentifier,
                SchedulingRequestStatus.RESOLVED,
                requestedAt
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
