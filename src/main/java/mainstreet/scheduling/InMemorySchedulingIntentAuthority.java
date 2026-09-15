package mainstreet.scheduling;

import mainstreet.application.MerchantScope;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Thread-safe reference adapter for merchant-scoped scheduling intent. It
 * establishes relationship and retry semantics without acquiring capacity.
 */
public final class InMemorySchedulingIntentAuthority
        implements SchedulingIntentAuthority {

    private final Map<ScopedIdentifier, SchedulingRequest> requests =
            new LinkedHashMap<>();
    private final Map<ScopedIdentifier, TimeProposal> proposals =
            new LinkedHashMap<>();
    private final Map<ScopedIdentifier, AppointmentConfirmationFailure>
            confirmationFailures = new LinkedHashMap<>();
    private final Map<ScopedIdentifier, SchedulingCommitmentCorrelation>
            commitments = new LinkedHashMap<>();

    @Override
    public synchronized SchedulingRequest recordRequest(
            SchedulingRequest request
    ) {
        Objects.requireNonNull(request);
        if (request.status() != SchedulingRequestStatus.REQUESTED) {
            throw new IllegalArgumentException(
                    "Only a newly requested scheduling intent can be recorded"
            );
        }

        ScopedIdentifier key = new ScopedIdentifier(
                request.merchantScope(),
                request.identifier()
        );
        SchedulingRequest existing = requests.get(key);
        if (existing == null) {
            requests.put(key, request);
            return request;
        }
        if (!existing.equals(request)) {
            throw new SchedulingIntentIdentityConflictException(
                    request.identifier()
            );
        }
        return existing;
    }

    @Override
    public synchronized TimeProposal recordProposal(TimeProposal proposal) {
        Objects.requireNonNull(proposal);
        if (proposal.status() != TimeProposalStatus.PROPOSED) {
            throw new IllegalArgumentException(
                    "Only a newly proposed time can be recorded"
            );
        }

        SchedulingRequest request = request(
                proposal.merchantScope(),
                proposal.schedulingRequestIdentifier()
        ).orElseThrow(() -> new IllegalArgumentException(
                "Time proposal requires its scheduling request"
        ));
        validateRelationship(request, proposal);

        ScopedIdentifier key = new ScopedIdentifier(
                proposal.merchantScope(),
                proposal.identifier()
        );
        TimeProposal existing = proposals.get(key);
        if (existing == null) {
            proposals.put(key, proposal);
            return proposal;
        }
        if (!existing.equals(proposal)) {
            throw new SchedulingIntentIdentityConflictException(
                    proposal.identifier()
            );
        }
        return existing;
    }

    @Override
    public synchronized TimeProposal acceptProposal(
            MerchantScope merchantScope,
            String proposalIdentifier,
            String acceptedByIdentifier,
            Instant acceptedAt
    ) {
        Objects.requireNonNull(merchantScope);
        Objects.requireNonNull(acceptedAt);
        ScopedIdentifier key = new ScopedIdentifier(
                merchantScope,
                proposalIdentifier
        );
        TimeProposal proposal = proposals.get(key);
        if (proposal == null) {
            throw new IllegalArgumentException(
                    "Time proposal does not exist: " + proposalIdentifier
            );
        }

        if (proposal.status() == TimeProposalStatus.ACCEPTED) {
            return proposal.acceptedBy(
                    acceptedByIdentifier,
                    acceptedAt
            );
        }

        SchedulingRequest request = request(
                merchantScope,
                proposal.schedulingRequestIdentifier()
        ).orElseThrow();
        if (request.status() != SchedulingRequestStatus.REQUESTED) {
            throw new IllegalStateException(
                    "Resolved scheduling request cannot accept another proposal"
            );
        }

        TimeProposal accepted = proposal.acceptedBy(
                acceptedByIdentifier,
                acceptedAt
        );
        proposals.put(key, accepted);
        return accepted;
    }

    @Override
    public synchronized AppointmentConfirmationFailure
    recordConfirmationFailure(AppointmentConfirmationFailure failure) {
        Objects.requireNonNull(failure);
        ScopedIdentifier key = new ScopedIdentifier(
                failure.merchantScope(),
                failure.identifier()
        );
        AppointmentConfirmationFailure existing =
                confirmationFailures.get(key);
        if (existing != null) {
            if (!existing.equals(failure)) {
                throw new SchedulingIntentIdentityConflictException(
                        failure.identifier()
                );
            }
            return existing;
        }

        SchedulingRequest request = requiredOpenRequest(
                failure.merchantScope(),
                failure.schedulingRequestIdentifier()
        );
        TimeProposal proposal = requiredAcceptedProposal(
                failure.merchantScope(),
                failure.timeProposalIdentifier()
        );
        validateCorrelation(request, proposal);
        confirmationFailures.put(key, failure);
        return failure;
    }

    @Override
    public synchronized SchedulingCommitmentCorrelation recordCommitment(
            SchedulingCommitmentCorrelation correlation,
            Appointment appointment
    ) {
        Objects.requireNonNull(correlation);
        Objects.requireNonNull(appointment);
        ScopedIdentifier key = new ScopedIdentifier(
                correlation.merchantScope(),
                correlation.identifier()
        );
        SchedulingCommitmentCorrelation existing = commitments.get(key);
        if (existing != null) {
            if (!existing.equals(correlation)) {
                throw new SchedulingIntentIdentityConflictException(
                        correlation.identifier()
                );
            }
            SchedulingRequest replayRequest = request(
                    correlation.merchantScope(),
                    correlation.schedulingRequestIdentifier()
            ).orElseThrow();
            TimeProposal replayProposal = requiredAcceptedProposal(
                    correlation.merchantScope(),
                    correlation.timeProposalIdentifier()
            );
            validateCommittedAppointment(
                    replayRequest,
                    replayProposal,
                    correlation,
                    appointment
            );
            return existing;
        }

        SchedulingRequest request = requiredOpenRequest(
                correlation.merchantScope(),
                correlation.schedulingRequestIdentifier()
        );
        TimeProposal proposal = requiredAcceptedProposal(
                correlation.merchantScope(),
                correlation.timeProposalIdentifier()
        );
        validateCorrelation(request, proposal);
        validateCommittedAppointment(
                request,
                proposal,
                correlation,
                appointment
        );

        commitments.put(key, correlation);
        requests.put(
                new ScopedIdentifier(
                        request.merchantScope(),
                        request.identifier()
                ),
                request.resolved()
        );
        return correlation;
    }

    @Override
    public synchronized Optional<SchedulingRequest> request(
            MerchantScope merchantScope,
            String identifier
    ) {
        return Optional.ofNullable(requests.get(new ScopedIdentifier(
                merchantScope,
                identifier
        )));
    }

    @Override
    public synchronized Optional<TimeProposal> proposal(
            MerchantScope merchantScope,
            String identifier
    ) {
        return Optional.ofNullable(proposals.get(new ScopedIdentifier(
                merchantScope,
                identifier
        )));
    }

    @Override
    public synchronized Optional<AppointmentConfirmationFailure>
    confirmationFailure(
            MerchantScope merchantScope,
            String identifier
    ) {
        return Optional.ofNullable(confirmationFailures.get(
                new ScopedIdentifier(merchantScope, identifier)
        ));
    }

    @Override
    public synchronized Optional<SchedulingCommitmentCorrelation> commitment(
            MerchantScope merchantScope,
            String identifier
    ) {
        return Optional.ofNullable(commitments.get(
                new ScopedIdentifier(merchantScope, identifier)
        ));
    }

    private SchedulingRequest requiredOpenRequest(
            MerchantScope merchantScope,
            String identifier
    ) {
        SchedulingRequest request = request(
                merchantScope,
                identifier
        ).orElseThrow(() -> new IllegalArgumentException(
                "Scheduling request does not exist: " + identifier
        ));
        if (request.status() != SchedulingRequestStatus.REQUESTED) {
            throw new IllegalStateException(
                    "Scheduling request is not open: " + identifier
            );
        }
        return request;
    }

    private TimeProposal requiredAcceptedProposal(
            MerchantScope merchantScope,
            String identifier
    ) {
        TimeProposal proposal = proposal(
                merchantScope,
                identifier
        ).orElseThrow(() -> new IllegalArgumentException(
                "Time proposal does not exist: " + identifier
        ));
        if (proposal.status() != TimeProposalStatus.ACCEPTED) {
            throw new IllegalStateException(
                    "Time proposal has not been accepted: " + identifier
            );
        }
        return proposal;
    }

    private static void validateCorrelation(
            SchedulingRequest request,
            TimeProposal proposal
    ) {
        if (!proposal.schedulingRequestIdentifier().equals(
                request.identifier()
        )) {
            throw new IllegalArgumentException(
                    "Proposal belongs to another scheduling request"
            );
        }
        validateRelationship(request, proposal);
    }

    private static void validateCommittedAppointment(
            SchedulingRequest request,
            TimeProposal proposal,
            SchedulingCommitmentCorrelation correlation,
            Appointment appointment
    ) {
        if (!appointment.merchantScope().equals(request.merchantScope())
                || !correlation.merchantScope().equals(
                request.merchantScope()
        ) || !correlation.schedulingRequestIdentifier().equals(
                request.identifier()
        ) || !correlation.timeProposalIdentifier().equals(
                proposal.identifier()
        ) || !proposal.schedulingRequestIdentifier().equals(
                request.identifier()
        )
                || !appointment.identifier().equals(
                correlation.appointmentIdentifier()
        ) || !appointment.customerContextIdentifier().equals(
                request.customerContextIdentifier()
        ) || !appointment.scheduledOperationIdentifier().equals(
                request.scheduledOperationIdentifier()
        ) || !appointment.scheduledInterval().equals(
                proposal.candidateInterval()
        ) || !appointment.governingReleaseIdentifier().equals(
                correlation.governingReleaseIdentifier()
        ) || !appointment.confirmedAt().equals(
                correlation.committedAt()
        )) {
            throw new IllegalArgumentException(
                    "Committed Appointment does not match scheduling decision"
            );
        }
    }

    private static void validateRelationship(
            SchedulingRequest request,
            TimeProposal proposal
    ) {
        if (!request.customerContextIdentifier().equals(
                proposal.customerContextIdentifier()
        ) || !request.scheduledOperationIdentifier().equals(
                proposal.scheduledOperationIdentifier()
        )) {
            throw new IllegalArgumentException(
                    "Time proposal does not preserve request relationship"
            );
        }
        if (request.status() != SchedulingRequestStatus.REQUESTED) {
            throw new IllegalArgumentException(
                    "Time proposal requires an active scheduling request"
            );
        }
    }

    private record ScopedIdentifier(
            MerchantScope merchantScope,
            String identifier
    ) {
        private ScopedIdentifier {
            Objects.requireNonNull(merchantScope);
            if (identifier == null || identifier.isBlank()) {
                throw new IllegalArgumentException(
                        "Scheduling intent identifier must not be blank"
                );
            }
        }
    }
}
