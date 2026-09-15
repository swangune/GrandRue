package mainstreet.semantic.configuration;

import java.util.Optional;

/**
 * Trusted authority for resolving merchant approval of one exact immutable
 * configuration revision.
 */
@FunctionalInterface
public interface ConfigurationRevisionApprovalAuthority {

    Optional<ConfigurationRevisionApproval> approvalFor(
            String merchantIdentifier,
            String configurationRevisionIdentifier
    );
}
