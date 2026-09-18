package grandrue.semantic.configuration;

/** Effective value for the currently implemented Policy-backed ENUM domain. */
public record ResolvedEnumCapabilityConfigurationValue(
        String value
) implements ResolvedCapabilityConfigurationValue {

    public ResolvedEnumCapabilityConfigurationValue {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Resolved ENUM configuration value must not be blank"
            );
        }
    }

    @Override
    public CapabilityConfigurationValueDomain valueDomain() {
        return CapabilityConfigurationValueDomain.ENUM;
    }
}
