package mainstreet.semantic.capability;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class MerchantCapabilityConfiguration {

    private final Set<Capability> capabilities =
            new HashSet<>();


    public void enable(Capability capability) {

        capabilities.add(capability);
    }


    public Set<Capability> capabilities() {

        return Collections.unmodifiableSet(
                capabilities
        );
    }
}