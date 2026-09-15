package mainstreet.scheduling;

import mainstreet.application.MerchantScope;
import mainstreet.semantic.TimeWindowAllocationScope;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Candidate time offered for a scheduling request. A proposal is not an
 * allocation claim; acceptance must still reach authoritative confirmation.
 */
public record TimeProposal(
        MerchantScope merchantScope,
        String identifier,
        String schedulingRequestIdentifier,
        String customerContextIdentifier,
        String scheduledOperationIdentifier,
        String proposerIdentifier,
        TimeWindowAllocationScope candidateInterval,
        String governingReleaseIdentifier,
        TimeProposalStatus status,
        Instant proposedAt,
        Optional<Instant> expiresAt,
        Optional<String> acceptedByIdentifier,
        Optional<Instant> acceptedAt
) {

    public TimeProposal {
        Objects.requireNonNull(merchantScope);
        requireIdentifier(identifier, "Time proposal identifier");
        requireIdentifier(
                schedulingRequestIdentifier,
                "Scheduling request identifier"
        );
        requireIdentifier(
                customerContextIdentifier,
                "Customer context identifier"
        );
        requireIdentifier(
                scheduledOperationIdentifier,
                "Scheduled operation identifier"
        );
        requireIdentifier(proposerIdentifier, "Proposer identifier");
        Objects.requireNonNull(candidateInterval);
        requireIdentifier(
                governingReleaseIdentifier,
                "Governing release identifier"
        );
        Objects.requireNonNull(status);
        Objects.requireNonNull(proposedAt);
        Objects.requireNonNull(expiresAt);
        Objects.requireNonNull(acceptedByIdentifier);
        Objects.requireNonNull(acceptedAt);
        if (expiresAt.isPresent()
                && !proposedAt.isBefore(expiresAt.orElseThrow())) {
            throw new IllegalArgumentException(
                    "Proposal expiry must follow proposal time"
            );
        }
        if (acceptedByIdentifier.isPresent() != acceptedAt.isPresent()) {
            throw new IllegalArgumentException(
                    "Proposal acceptance actor and time must appear together"
            );
        }
        if ((status == TimeProposalStatus.ACCEPTED)
                != acceptedAt.isPresent()) {
            throw new IllegalArgumentException(
                    "Accepted proposal status requires acceptance evidence"
            );
        }
        acceptedByIdentifier.ifPresent(acceptanceActor ->
                requireIdentifier(
                        acceptanceActor,
                        "Acceptance actor identifier"
                )
        );
        acceptedAt.ifPresent(time -> {
            if (time.isBefore(proposedAt)) {
                throw new IllegalArgumentException(
                        "Proposal cannot be accepted before it was proposed"
                );
            }
        });
    }

    public TimeProposal acceptedBy(
            String actorIdentifier,
            Instant acceptanceTime
    ) {
        requireIdentifier(actorIdentifier, "Acceptance actor identifier");
        Objects.requireNonNull(acceptanceTime);

        if (status == TimeProposalStatus.ACCEPTED) {
            if (acceptedByIdentifier.orElseThrow().equals(actorIdentifier)
                    && acceptedAt.orElseThrow().equals(acceptanceTime)) {
                return this;
            }
            throw new SchedulingIntentIdentityConflictException(identifier);
        }
        if (status != TimeProposalStatus.PROPOSED) {
            throw new IllegalStateException(
                    "Only a proposed time can be accepted"
            );
        }
        if (expiresAt.isPresent()
                && !acceptanceTime.isBefore(expiresAt.orElseThrow())) {
            throw new IllegalStateException("Time proposal has expired");
        }

        return new TimeProposal(
                merchantScope,
                identifier,
                schedulingRequestIdentifier,
                customerContextIdentifier,
                scheduledOperationIdentifier,
                proposerIdentifier,
                candidateInterval,
                governingReleaseIdentifier,
                TimeProposalStatus.ACCEPTED,
                proposedAt,
                expiresAt,
                Optional.of(actorIdentifier),
                Optional.of(acceptanceTime)
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
