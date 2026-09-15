package mainstreet.semantic.configuration;

import mainstreet.runtime.TrustedExecutionContext;

import java.util.Optional;

/** Durable current-Controller authority for ordinary first approval facts. */
public interface InitialConfigurationRevisionApprovalAuthority
        extends InitialConfigurationRevisionApprovalApplicabilityAuthority {
    InitialConfigurationRevisionApproval approve(
            ApproveInitialConfigurationRevisionCommand command,
            TrustedExecutionContext trustedContext
    );
    Optional<InitialConfigurationRevisionApproval> approvalByRequest(
            String requestIdentifier
    );
}
