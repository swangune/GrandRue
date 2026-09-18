package grandrue.semantic.configuration;

import java.util.Optional;

/**
 * Atomic merchant-scoped activation boundary for staged configuration
 * releases.
 */
public interface ConfigurationReleaseActivation {

    /**
     * Attempts one explicit logical activation request against the current
     * authoritative merchant configuration state.
     */
    ConfigurationActivationResult activate(
            ConfigurationActivationRequest request
    );

    /**
     * Captures the complete release currently active for the merchant.
     */
    Optional<ActiveRelease> current(String merchantIdentifier);

    /**
     * Resolves a successfully committed activation by logical request identity
     * for retry/reconciliation after lost acknowledgement.
     */
    Optional<ConfigurationActivation> committedActivation(
            String activationRequestIdentifier
    );
}
