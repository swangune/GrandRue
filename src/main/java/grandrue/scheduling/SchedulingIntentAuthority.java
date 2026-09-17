package grandrue.scheduling;

import mainstreet.application.MerchantScope;

import java.util.Optional;

/**
 * Authority for non-committing scheduling requests and proposals. Capacity
 * allocation deliberately belongs to a separate authority and confirmation
 * boundary.
 */
public interface SchedulingIntentAuthority {

    SchedulingRequest recordRequest(SchedulingRequest request);

    TimeProposal recordProposal(TimeProposal proposal);

    TimeProposal acceptProposal(
            MerchantScope merchantScope,
            String proposalIdentifier,
            String acceptedByIdentifier,
            java.time.Instant acceptedAt
    );

    AppointmentConfirmationFailure recordConfirmationFailure(
            AppointmentConfirmationFailure failure
    );

    SchedulingCommitmentCorrelation recordCommitment(
            SchedulingCommitmentCorrelation correlation,
            Appointment appointment
    );

    Optional<SchedulingRequest> request(
            MerchantScope merchantScope,
            String identifier
    );

    Optional<TimeProposal> proposal(
            MerchantScope merchantScope,
            String identifier
    );

    Optional<AppointmentConfirmationFailure> confirmationFailure(
            MerchantScope merchantScope,
            String identifier
    );

    Optional<SchedulingCommitmentCorrelation> commitment(
            MerchantScope merchantScope,
            String identifier
    );
}
