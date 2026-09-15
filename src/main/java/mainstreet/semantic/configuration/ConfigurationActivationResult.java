package mainstreet.semantic.configuration;

import java.util.Objects;
import java.util.Optional;

/**
 * Explicit result of one activation attempt. Only SUCCESS carries a committed
 * activation fact.
 */
public record ConfigurationActivationResult(
        ConfigurationActivationStatus status,
        Optional<ConfigurationActivation> activation
) {

    public ConfigurationActivationResult {
        status = Objects.requireNonNull(status, "status");
        activation = Objects.requireNonNull(activation, "activation");
        if ((status == ConfigurationActivationStatus.SUCCESS)
                != activation.isPresent()) {
            throw new IllegalArgumentException(
                    "Only successful activation results carry an activation fact"
            );
        }
    }

    public static ConfigurationActivationResult rejected(
            ConfigurationActivationStatus status
    ) {
        if (status == ConfigurationActivationStatus.SUCCESS) {
            throw new IllegalArgumentException(
                    "SUCCESS requires a committed activation fact"
            );
        }
        return new ConfigurationActivationResult(status, Optional.empty());
    }

    public static ConfigurationActivationResult success(
            ConfigurationActivation activation
    ) {
        return new ConfigurationActivationResult(
                ConfigurationActivationStatus.SUCCESS,
                Optional.of(Objects.requireNonNull(activation, "activation"))
        );
    }
}
