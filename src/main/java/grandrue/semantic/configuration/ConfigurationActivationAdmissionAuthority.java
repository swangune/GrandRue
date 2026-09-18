package grandrue.semantic.configuration;

import grandrue.application.MerchantScope;

import java.util.Optional;

/** Transaction-participating exact D5c4 activation-admission authority. */
@FunctionalInterface
public interface ConfigurationActivationAdmissionAuthority {
    ConfigurationActivationAdmissionResult admit(
            MerchantScope merchantScope,
            String configurationRevisionIdentifier,
            String semanticRegistryReleaseIdentifier,
            Optional<String> approvedResolvedPackageEvidenceIdentifier
    );
}
