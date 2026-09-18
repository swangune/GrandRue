package grandrue.semantic;

import java.util.Objects;

public final class State {

    private final Resource resource;
    private final String identifier;

    State(Resource resource, String identifier) {
        this.resource = Objects.requireNonNull(resource);
        this.identifier = Objects.requireNonNull(identifier);
    }

    public Resource resource() {
        return resource;
    }

    public String identifier() {
        return identifier;
    }
}