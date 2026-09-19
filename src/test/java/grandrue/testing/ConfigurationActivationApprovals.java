package grandrue.testing;

import grandrue.semantic.configuration.ConfigurationPublication;
import grandrue.semantic.configuration.InitialConfigurationRevisionApproval;
import grandrue.semantic.configuration.InitialConfigurationRevisionApprovalApplicabilityAuthority;

import java.time.Instant;
import java.util.Objects;

/** Test fixtures for the ordinary first-activation approval boundary. */
public final class ConfigurationActivationApprovals {

    private ConfigurationActivationApprovals() {
    }

    public static InitialConfigurationRevisionApprovalApplicabilityAuthority
    currentInitialApproval(
            ConfigurationPublication publication,
            String approvingPrincipalIdentifier,
            Instant approvedAt
    ) {
        Objects.requireNonNull(publication, "publication");
        return (scope, revision) -> publication
                .version(scope.merchantIdentifier(), 1)
                .filter(release -> release.configurationIdentifier().equals(revision))
                .map(release -> new InitialConfigurationRevisionApproval(
                        "approval-" + scope.merchantIdentifier(),
                        scope.merchantIdentifier(),
                        revision,
                        release.semanticRegistryVersion(),
                        "validation-" + revision,
                        "impact-" + revision,
                        "package-" + revision,
                        approvingPrincipalIdentifier,
                        "controller-relationship-" + scope.merchantIdentifier(),
                        approvedAt
                ));
    }
}
