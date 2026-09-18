package grandrue.fulfilment;

import grandrue.semantic.configuration.CapabilityConfigurationDecisionIdentity;

import java.util.Objects;
import java.util.Set;

/** Applicability when one resolved ENUM configuration decision has an accepted value. */
public record EnumDecisionValueFulfilmentRequirementApplicability(
        CapabilityConfigurationDecisionIdentity decisionIdentity,
        Set<String> acceptedValues
) implements FulfilmentRequirementApplicability {
    public EnumDecisionValueFulfilmentRequirementApplicability {
        Objects.requireNonNull(decisionIdentity, "decisionIdentity");
        acceptedValues = Set.copyOf(Objects.requireNonNull(
                acceptedValues,
                "acceptedValues"
        ));
        if (acceptedValues.isEmpty()) {
            throw new IllegalArgumentException(
                    "ENUM fulfilment requirement applicability needs at least one accepted value"
            );
        }
        acceptedValues.forEach(value -> requireIdentifier(
                value,
                "Accepted ENUM value"
        ));
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
