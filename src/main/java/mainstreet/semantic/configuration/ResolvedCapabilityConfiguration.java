package mainstreet.semantic.configuration;

import grandrue.semantic.executable.ExecutablePolicyValue;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Resolved bounded capability configuration kept separate from executable
 * semantic structure.
 *
 * <p>Registered Policy values are the currently implemented concrete form of
 * MS-PROT-047 configuration decisions. {@link #decisions()} exposes them
 * through the broader capability-configuration vocabulary without changing
 * Policy semantics or implying that other value domains are implemented.</p>
 */
public record ResolvedCapabilityConfiguration(
        List<ExecutablePolicyValue> policyValues
) {
    public ResolvedCapabilityConfiguration {
        policyValues = List.copyOf(
                Objects.requireNonNull(policyValues, "policyValues")
        );
    }

    /**
     * Returns the currently materialised configuration decisions in the same
     * deterministic order as the executable Policy values that produced them.
     */
    public List<ResolvedCapabilityConfigurationDecision> decisions() {
        return policyValues.stream()
                .map(ResolvedCapabilityConfiguration::fromPolicy)
                .toList();
    }

    private static ResolvedCapabilityConfigurationDecision fromPolicy(
            ExecutablePolicyValue policy
    ) {
        CapabilityConfigurationResolutionStatus status = switch (policy.status()) {
            case NOT_APPLICABLE ->
                    CapabilityConfigurationResolutionStatus.NOT_APPLICABLE;
            case DEFAULTED -> CapabilityConfigurationResolutionStatus.DEFAULTED;
            case EXPLICITLY_SELECTED ->
                    CapabilityConfigurationResolutionStatus.EXPLICITLY_SELECTED;
        };

        Optional<ResolvedCapabilityConfigurationValue> value = policy.value()
                .isEmpty()
                ? Optional.empty()
                : Optional.of(new ResolvedEnumCapabilityConfigurationValue(
                        policy.value().orElseThrow()
                ));

        return new ResolvedCapabilityConfigurationDecision(
                new CapabilityConfigurationDecisionIdentity(
                        policy.ownerCapabilityIdentifier(),
                        policy.policyIdentifier()
                ),
                CapabilityConfigurationValueDomain.ENUM,
                status,
                value
        );
    }
}
