package grandrue.scheduling;

import grandrue.application.MerchantScope;
import grandrue.semantic.TimeWindowAllocationScope;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SchedulingIntentAuthorityTest {

    private static final MerchantScope MERCHANT =
            new MerchantScope("merchant-a");
    private static final Instant REQUESTED_AT =
            Instant.parse("2026-08-21T09:00:00Z");
    private static final Instant PROPOSED_AT =
            Instant.parse("2026-08-21T09:05:00Z");

    @Test
    void request_and_proposal_remain_non_committing_intents() {
        InMemorySchedulingIntentAuthority authority =
                new InMemorySchedulingIntentAuthority();
        SchedulingRequest request = request(MERCHANT);
        TimeProposal proposal = proposal(MERCHANT, request);

        assertSame(request, authority.recordRequest(request));
        assertSame(proposal, authority.recordProposal(proposal));
        assertSame(
                request,
                authority.request(MERCHANT, request.identifier())
                        .orElseThrow()
        );
        assertSame(
                proposal,
                authority.proposal(MERCHANT, proposal.identifier())
                        .orElseThrow()
        );
        assertTrue(
                !(((Object) authority)
                        instanceof grandrue.semantic.AllocationAuthority),
                "Scheduling intent authority must not allocate capacity"
        );
    }

    @Test
    void exact_retries_replay_the_original_intent() {
        InMemorySchedulingIntentAuthority authority =
                new InMemorySchedulingIntentAuthority();
        SchedulingRequest request = request(MERCHANT);
        TimeProposal proposal = proposal(MERCHANT, request);

        assertSame(request, authority.recordRequest(request));
        assertSame(request, authority.recordRequest(request));
        assertSame(proposal, authority.recordProposal(proposal));
        assertSame(proposal, authority.recordProposal(proposal));

        SchedulingRequest reusedIdentity = new SchedulingRequest(
                MERCHANT,
                request.identifier(),
                "customer-999",
                request.scheduledOperationIdentifier(),
                request.governingReleaseIdentifier(),
                SchedulingRequestStatus.REQUESTED,
                request.requestedAt()
        );
        assertThrows(
                SchedulingIntentIdentityConflictException.class,
                () -> authority.recordRequest(reusedIdentity)
        );
    }

    @Test
    void proposal_must_preserve_its_request_relationship() {
        InMemorySchedulingIntentAuthority authority =
                new InMemorySchedulingIntentAuthority();
        SchedulingRequest request = request(MERCHANT);
        authority.recordRequest(request);
        TimeProposal inconsistent = new TimeProposal(
                MERCHANT,
                "proposal-001",
                request.identifier(),
                "customer-999",
                request.scheduledOperationIdentifier(),
                "staff-123",
                interval(),
                request.governingReleaseIdentifier(),
                TimeProposalStatus.PROPOSED,
                PROPOSED_AT,
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> authority.recordProposal(inconsistent)
        );
        assertTrue(authority.proposal(
                MERCHANT,
                inconsistent.identifier()
        ).isEmpty());
    }

    @Test
    void equal_identifiers_are_isolated_by_merchant_scope() {
        InMemorySchedulingIntentAuthority authority =
                new InMemorySchedulingIntentAuthority();
        MerchantScope secondMerchant = new MerchantScope("merchant-b");

        SchedulingRequest first = request(MERCHANT);
        SchedulingRequest second = request(secondMerchant);

        assertSame(first, authority.recordRequest(first));
        assertSame(second, authority.recordRequest(second));
        assertSame(
                first,
                authority.request(MERCHANT, first.identifier()).orElseThrow()
        );
        assertSame(
                second,
                authority.request(
                        secondMerchant,
                        second.identifier()
                ).orElseThrow()
        );
    }

    @Test
    void failed_confirmation_preserves_acceptance_and_keeps_request_open() {
        InMemorySchedulingIntentAuthority authority =
                new InMemorySchedulingIntentAuthority();
        SchedulingRequest request = request(MERCHANT);
        TimeProposal proposal = proposal(MERCHANT, request);
        authority.recordRequest(request);
        authority.recordProposal(proposal);

        TimeProposal accepted = authority.acceptProposal(
                MERCHANT,
                proposal.identifier(),
                "customer-123",
                Instant.parse("2026-08-21T09:10:00Z")
        );
        AppointmentConfirmationFailure failure =
                new AppointmentConfirmationFailure(
                        MERCHANT,
                        "attempt-001",
                        request.identifier(),
                        proposal.identifier(),
                        "capacity.conflict",
                        "release-002",
                        Instant.parse("2026-08-21T09:10:01Z")
                );

        assertSame(failure, authority.recordConfirmationFailure(failure));
        assertSame(
                accepted,
                authority.proposal(MERCHANT, proposal.identifier())
                        .orElseThrow()
        );
        assertSame(
                request,
                authority.request(MERCHANT, request.identifier())
                        .orElseThrow()
        );
        assertSame(
                failure,
                authority.confirmationFailure(
                        MERCHANT,
                        failure.identifier()
                ).orElseThrow()
        );
    }

    @Test
    void successful_appointment_commitment_resolves_the_request() {
        InMemorySchedulingIntentAuthority authority =
                new InMemorySchedulingIntentAuthority();
        SchedulingRequest request = request(MERCHANT);
        TimeProposal proposal = proposal(MERCHANT, request);
        authority.recordRequest(request);
        authority.recordProposal(proposal);
        Instant acceptedAt = Instant.parse("2026-08-21T09:10:00Z");
        TimeProposal accepted = authority.acceptProposal(
                MERCHANT,
                proposal.identifier(),
                "customer-123",
                acceptedAt
        );
        SchedulingCommitmentCorrelation correlation =
                new SchedulingCommitmentCorrelation(
                        MERCHANT,
                        "commitment-001",
                        request.identifier(),
                        proposal.identifier(),
                        "appointment-123",
                        "release-002",
                        Instant.parse("2026-08-21T09:10:01Z")
                );
        Appointment validAppointment = appointment(proposal, correlation);
        Appointment mismatchedAppointment = new Appointment(
                validAppointment.merchantScope(),
                validAppointment.identifier(),
                "customer-999",
                validAppointment.scheduledOperationIdentifier(),
                validAppointment.scheduledInterval(),
                validAppointment.allocationClaimIdentifier(),
                validAppointment.governingReleaseIdentifier(),
                validAppointment.revision(),
                validAppointment.confirmedAt()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> authority.recordCommitment(
                        correlation,
                        mismatchedAppointment
                )
        );
        assertTrue(authority.request(
                MERCHANT,
                request.identifier()
        ).orElseThrow().status() == SchedulingRequestStatus.REQUESTED);

        assertSame(
                correlation,
                authority.recordCommitment(
                        correlation,
                        validAppointment
                )
        );
        assertTrue(authority.request(
                MERCHANT,
                request.identifier()
        ).orElseThrow().status() == SchedulingRequestStatus.RESOLVED);
        assertTrue(authority.proposal(
                MERCHANT,
                proposal.identifier()
        ).orElseThrow().status() == TimeProposalStatus.ACCEPTED);
        assertSame(
                correlation,
                authority.commitment(
                        MERCHANT,
                        correlation.identifier()
                ).orElseThrow()
        );
        assertSame(
                accepted,
                authority.acceptProposal(
                        MERCHANT,
                        proposal.identifier(),
                        "customer-123",
                        acceptedAt
                )
        );
    }

    private static SchedulingRequest request(MerchantScope merchantScope) {
        return new SchedulingRequest(
                merchantScope,
                "request-001",
                "customer-123",
                "consultation.perform",
                "release-001",
                SchedulingRequestStatus.REQUESTED,
                REQUESTED_AT
        );
    }

    private static TimeProposal proposal(
            MerchantScope merchantScope,
            SchedulingRequest request
    ) {
        return new TimeProposal(
                merchantScope,
                "proposal-001",
                request.identifier(),
                request.customerContextIdentifier(),
                request.scheduledOperationIdentifier(),
                "staff-123",
                interval(),
                request.governingReleaseIdentifier(),
                TimeProposalStatus.PROPOSED,
                PROPOSED_AT,
                Optional.of(Instant.parse("2026-08-22T09:05:00Z")),
                Optional.empty(),
                Optional.empty()
        );
    }

    private static TimeWindowAllocationScope interval() {
        return new TimeWindowAllocationScope(
                "consultant-001:appointment-capacity",
                Instant.parse("2026-08-23T14:00:00Z"),
                Instant.parse("2026-08-23T15:00:00Z")
        );
    }

    private static Appointment appointment(
            TimeProposal proposal,
            SchedulingCommitmentCorrelation correlation
    ) {
        return new Appointment(
                proposal.merchantScope(),
                correlation.appointmentIdentifier(),
                proposal.customerContextIdentifier(),
                proposal.scheduledOperationIdentifier(),
                proposal.candidateInterval(),
                "allocation-123",
                correlation.governingReleaseIdentifier(),
                1,
                correlation.committedAt()
        );
    }
}
