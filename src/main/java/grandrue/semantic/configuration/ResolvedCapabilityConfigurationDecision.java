package grandrue.semantic.configuration;

import java.util.Objects;
import java.util.Optional;

/**
 * One resolved capability configuration decision. This is configuration
 * evidence, not executable authority and not presentation metadata.
 */
public record ResolvedCapabilityConfigurationDecision(
        CapabilityConfigurationDecisionIdentity identity,
        CapabilityConfigurationValueDomain valueDomain,
        CapabilityConfigurationResolutionStatus status,
        Optional<ResolvedCapabilityConfigurationValue> value
) {
    public ResolvedCapabilityConfigurationDecision {
        Objects.requireNonNull(identity, "identity");
        Objects.requireNonNull(valueDomain, "valueDomain");
        Objects.requireNonNull(status, "status");
        value = Objects.requireNonNull(value, "value");

        if (status == CapabilityConfigurationResolutionStatus.NOT_APPLICABLE) {
            if (value.isPresent()) {
                throw new IllegalArgumentException(
                        "An inapplicable configuration decision must not have a value"
                );
            }
        } else {
            ResolvedCapabilityConfigurationValue resolvedValue = value.orElseThrow(
                    () -> new IllegalArgumentException(
                            "An applicable configuration decision requires a value"
                    )
            );
            if (resolvedValue.valueDomain() != valueDomain) {
                throw new IllegalArgumentException(
                        "Resolved configuration value does not match its value domain"
                );
            }
        }
    }
}
