package mainstreet.semantic.policy;

import mainstreet.semantic.capability.Capability;

import java.util.Objects;
import java.util.Set;

public final class PolicyDefinition {

    private final Capability owner;
    private final String identifier;
    private final PolicyValue defaultValue;
    private final Set<PolicyValue> allowedValues;


    public PolicyDefinition(
            Capability owner,
            String identifier,
            PolicyValue defaultValue,
            Set<PolicyValue> allowedValues
    ) {

        this.owner = Objects.requireNonNull(owner);

        if(identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Policy identifier required"
            );
        }

        this.identifier = identifier;
        this.defaultValue = Objects.requireNonNull(defaultValue);
        this.allowedValues = Set.copyOf(
                Objects.requireNonNull(allowedValues)
        );

        if (!this.allowedValues.contains(defaultValue)) {
            throw new IllegalArgumentException(
                    "Default policy value must be allowed"
            );
        }
    }


    public Capability owner() {
        return owner;
    }


    public String identifier() {
        return identifier;
    }


    public PolicyValue defaultValue() {
        return defaultValue;
    }


    public boolean allows(PolicyValue value) {
        return allowedValues.contains(
                Objects.requireNonNull(value)
        );
    }
}
