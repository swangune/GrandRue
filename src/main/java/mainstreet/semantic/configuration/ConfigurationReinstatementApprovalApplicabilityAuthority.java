package mainstreet.semantic.configuration;

import grandrue.application.MerchantScope;

import java.util.Optional;

/**
 * Read boundary for exact currently applicable reinstatement approval.
 *
 * <p>A historical approval is returned only while its exact Reinstatement
 * Basis Activation still owns the merchant's current Configuration pointer
 * and its exact Controller Relationship remains current ACTIVE authority.</p>
 *
 * Authority: MS-PROT-040 v1.8,
 * {@code designs/MS-PROT-040 v1.8 — Configuration Reinstatement Decision
 * Affinity Amendment.md}, §11 — Current Applicability of Reinstatement
 * Approval.
 */
@FunctionalInterface
public interface ConfigurationReinstatementApprovalApplicabilityAuthority {

    Optional<ConfigurationReinstatementApproval> currentApplicableApproval(
            MerchantScope merchantScope,
            String configurationRevisionIdentifier
    );
}
