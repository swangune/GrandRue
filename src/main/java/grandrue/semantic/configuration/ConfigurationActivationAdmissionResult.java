package grandrue.semantic.configuration;

import java.util.Objects;
import java.util.Optional;

/** Fail-closed result of evaluating current exact activation admission. */
public record ConfigurationActivationAdmissionResult(
        ConfigurationActivationStatus status,
        Optional<ConfigurationActivationAdmissionEvidence> evidence
) {
    public ConfigurationActivationAdmissionResult {
        Objects.requireNonNull(status, "status");
        evidence = Objects.requireNonNull(evidence, "evidence");
        if ((status == ConfigurationActivationStatus.SUCCESS) != evidence.isPresent()) {
            throw new IllegalArgumentException("Only admitted results carry evidence");
        }
    }

    public static ConfigurationActivationAdmissionResult admitted(
            ConfigurationActivationAdmissionEvidence evidence
    ) {
        return new ConfigurationActivationAdmissionResult(
                ConfigurationActivationStatus.SUCCESS,
                Optional.of(Objects.requireNonNull(evidence, "evidence"))
        );
    }

    public static ConfigurationActivationAdmissionResult rejected(
            ConfigurationActivationStatus status
    ) {
        if (status == ConfigurationActivationStatus.SUCCESS) {
            throw new IllegalArgumentException("SUCCESS requires evidence");
        }
        return new ConfigurationActivationAdmissionResult(status, Optional.empty());
    }
}
