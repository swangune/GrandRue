package grandrue.semantic.capability;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class CapabilityRegistry {

    private final Map<String, Capability> capabilities =
            new HashMap<>();

    public void register(Capability capability) {

        Objects.requireNonNull(capability);

        if (capabilities.containsKey(
                capability.identifier())) {

            throw new IllegalArgumentException(
                    "Capability already registered: "
                            + capability.identifier());
        }

        capabilities.put(
                capability.identifier(),
                capability);
    }

    public Capability find(String identifier) {

        return capabilities.get(identifier);
    }
}