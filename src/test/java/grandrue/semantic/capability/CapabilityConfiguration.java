package grandrue.semantic.capability;


import grandrue.semantic.policy.PolicyDefinition;
import grandrue.semantic.policy.PolicyValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public final class CapabilityConfiguration {


    private final Capability capability;

    private final Map<String, PolicyValue> policies =
            new HashMap<>();


    public CapabilityConfiguration(
            Capability capability
    ) {

        this.capability =
                Objects.requireNonNull(capability);
    }


    public Capability capability() {
        return capability;
    }


    public void setPolicy(
            PolicyDefinition definition,
            PolicyValue value
    ) {

        Objects.requireNonNull(definition);
        Objects.requireNonNull(value);

        if (definition.owner() != capability) {
            throw new IllegalArgumentException(
                    "Policy is not owned by capability: "
                            + capability.identifier()
            );
        }

        if (!definition.allows(value)) {
            throw new IllegalArgumentException(
                    "Policy value is not allowed: "
                            + definition.identifier()
            );
        }

        policies.put(
                definition.identifier(),
                value
        );
    }


    public PolicyValue policy(
            String identifier
    ) {

        return policies.get(identifier);
    }
}
