package mainstreet.semantic.configuration;

import mainstreet.application.MerchantScope;

import java.util.Optional;

/**
 * Resolves the exact then-current non-initial Configuration Revision approval
 * applicable to activation.
 *
 * <p>Historical approval existence is not sufficient. The returned approval
 * must still be applicable under the current Merchant Controller relationship
 * for the requested Merchant Scope.</p>
 *
 * Authority: approved MS-PROT-040 v1.6.
 */
@FunctionalInterface
public interface NonInitialConfigurationRevisionApprovalApplicabilityAuthority {

    Optional<NonInitialConfigurationRevisionApproval>
            currentApplicableApproval(
                    MerchantScope merchantScope,
                    String configurationRevisionIdentifier
            );
}