package grandrue.semantic.configuration;

/**
 * Typed effective value of one resolved capability configuration decision.
 * Implementations remain bounded by their registered value-domain contracts.
 */
public sealed interface ResolvedCapabilityConfigurationValue
        permits ResolvedEnumCapabilityConfigurationValue {

    CapabilityConfigurationValueDomain valueDomain();
}
