package mainstreet.semantic.configuration;

import mainstreet.runtime.TrustedExecutionContext;

import java.util.Optional;

/**
 * Bounded Configuration authority for approval of genuine reinstatement.
 *
 * <p>This authority is distinct from both first-configuration approval and
 * ordinary forward non-initial replacement approval.</p>
 *
 * Authority: MS-PROT-040 v1.8,
 * {@code designs/MS-PROT-040 v1.8 — Configuration Reinstatement Decision
 * Affinity Amendment.md}, §§7–9 — Reinstatement Approval, Superseded Initial
 * Revision, and Approval Authority Separation.
 */
public interface ConfigurationReinstatementApprovalAuthority
        extends ConfigurationReinstatementApprovalApplicabilityAuthority {

    ConfigurationReinstatementApproval approve(
            ApproveConfigurationReinstatementCommand command,
            TrustedExecutionContext trustedContext
    );

    Optional<ConfigurationReinstatementApproval> approvalByRequest(
            String requestIdentifier
    );

}
