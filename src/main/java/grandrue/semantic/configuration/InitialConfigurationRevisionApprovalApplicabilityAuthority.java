package grandrue.semantic.configuration;

import grandrue.application.MerchantScope;

import java.util.Optional;

/**
 * Read boundary for resolving the then-current exact approval that may admit
 * one ordinary first Configuration Revision to activation.
 */
@FunctionalInterface
public interface InitialConfigurationRevisionApprovalApplicabilityAuthority {

    Optional<InitialConfigurationRevisionApproval> currentApplicableApproval(
            MerchantScope merchantScope,
            String configurationRevisionIdentifier
    );
}
