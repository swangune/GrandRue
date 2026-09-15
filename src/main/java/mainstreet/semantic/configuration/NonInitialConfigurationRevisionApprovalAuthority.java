package mainstreet.semantic.configuration;

import mainstreet.runtime.TrustedExecutionContext;

import java.util.Optional;

/**
 * Durable current-Controller authority for non-initial Configuration Revision
 * approvals.
 *
 * <p>The write boundary establishes immutable approval history. The exact
 * applicability boundary separately resolves whether a historical approval is
 * still current for activation.</p>
 *
 * <p>The inherited reduced ConfigurationRevisionApprovalAuthority read
 * boundary remains for backward compatibility. Real replacement activation
 * consumes the full non-initial applicability authority instead.</p>
 *
 * Authority: approved MS-PROT-040 v1.6.
 */
public interface NonInitialConfigurationRevisionApprovalAuthority
        extends ConfigurationRevisionApprovalAuthority,
        NonInitialConfigurationRevisionApprovalApplicabilityAuthority {

    NonInitialConfigurationRevisionApproval approve(
            ApproveNonInitialConfigurationRevisionCommand command,
            TrustedExecutionContext trustedContext
    );

    Optional<NonInitialConfigurationRevisionApproval> approvalByRequest(
            String requestIdentifier
    );
}