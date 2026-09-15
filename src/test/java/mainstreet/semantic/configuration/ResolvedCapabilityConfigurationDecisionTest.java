package mainstreet.semantic.configuration;

import mainstreet.semantic.executable.ExecutablePolicyValue;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResolvedCapabilityConfigurationDecisionTest {

    @Test
    void exposes_policy_resolution_through_capability_configuration_contract() {
        ResolvedCapabilityConfiguration configuration =
                new ResolvedCapabilityConfiguration(List.of(
                        ExecutablePolicyValue.defaulted(
                                "booking",
                                "confirmation-mode",
                                "AUTOMATIC"
                        ),
                        ExecutablePolicyValue.explicitlySelected(
                                "scheduling",
                                "cancellation-mode",
                                "MERCHANT_APPROVAL"
                        ),
                        ExecutablePolicyValue.notApplicable(
                                "inventory",
                                "stock-policy"
                        )
                ));

        assertEquals(
                List.of(
                        new ResolvedCapabilityConfigurationDecision(
                                new CapabilityConfigurationDecisionIdentity(
                                        "booking",
                                        "confirmation-mode"
                                ),
                                CapabilityConfigurationValueDomain.ENUM,
                                CapabilityConfigurationResolutionStatus.DEFAULTED,
                                Optional.of(new ResolvedEnumCapabilityConfigurationValue(
                                        "AUTOMATIC"
                                ))
                        ),
                        new ResolvedCapabilityConfigurationDecision(
                                new CapabilityConfigurationDecisionIdentity(
                                        "scheduling",
                                        "cancellation-mode"
                                ),
                                CapabilityConfigurationValueDomain.ENUM,
                                CapabilityConfigurationResolutionStatus.EXPLICITLY_SELECTED,
                                Optional.of(new ResolvedEnumCapabilityConfigurationValue(
                                        "MERCHANT_APPROVAL"
                                ))
                        ),
                        new ResolvedCapabilityConfigurationDecision(
                                new CapabilityConfigurationDecisionIdentity(
                                        "inventory",
                                        "stock-policy"
                                ),
                                CapabilityConfigurationValueDomain.ENUM,
                                CapabilityConfigurationResolutionStatus.NOT_APPLICABLE,
                                Optional.empty()
                        )
                ),
                configuration.decisions()
        );
    }
}
