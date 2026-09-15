package mainstreet.semantic.capability;

import mainstreet.semantic.Resource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class Capability {

    private final String identifier;

    private final Set<Resource> resources =
            new HashSet<>();

    public Capability(String identifier) {
        this.identifier =
                Objects.requireNonNull(identifier);
    }

    public String identifier() {
        return identifier;
    }

    public void expose(Resource resource) {
        resources.add(
                Objects.requireNonNull(resource)
        );
    }

    public Set<Resource> resources() {
        return Collections.unmodifiableSet(resources);
    }
}
