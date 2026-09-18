package grandrue.application;

import grandrue.commercial.FirstConfigurationActivationAuthority;
import mainstreet.application.MerchantScope;
import mainstreet.semantic.configuration.ConfigurationActivation;
import mainstreet.semantic.configuration.ConfigurationReleaseActivation;

import java.time.Instant;
import java.util.Objects;

/**
 * Application-layer adapter allowing Commercial to verify first configuration
 * activation provenance without owning Configuration state.
 */
public final class ConfigurationBackedFirstActivationAuthority
        implements FirstConfigurationActivationAuthority {

    private final ConfigurationReleaseActivation activationAuthority;

    public ConfigurationBackedFirstActivationAuthority(
            ConfigurationReleaseActivation activationAuthority
    ) {
        this.activationAuthority = Objects.requireNonNull(
                activationAuthority,
                "activationAuthority"
        );
    }

    @Override
    public boolean isFirstCommittedActivation(
            MerchantScope merchantScope,
            String configurationRevisionIdentifier,
            String activationIdentity,
            Instant activatedAt
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(activatedAt, "activatedAt");

        return activationAuthority.committedActivation(activationIdentity)
                .filter(ConfigurationBackedFirstActivationAuthority::isFirst)
                .filter(activation -> activation.merchantIdentifier().equals(
                        merchantScope.merchantIdentifier()
                ))
                .filter(activation -> activation.configurationRevisionIdentifier()
                        .equals(configurationRevisionIdentifier))
                .filter(activation -> activation.activatedAt().equals(activatedAt))
                .isPresent();
    }

    private static boolean isFirst(ConfigurationActivation activation) {
        return activation.replacedConfigurationRevisionIdentifier().isEmpty();
    }
}
