package grandrue.runtime;

import grandrue.semantic.Resource;
import grandrue.semantic.State;

import java.util.Objects;

public final class ResourceInstance {

    private final String identifier;
    private final Resource resource;
    private State currentState;

    public ResourceInstance(
            String identifier,
            Resource resource,
            State initialState
    ) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Resource instance identifier must not be blank"
            );
        }

        this.identifier = identifier;
        this.resource = Objects.requireNonNull(resource);
        this.currentState = Objects.requireNonNull(initialState);

        if (initialState.resource() != resource) {
            throw new IllegalArgumentException(
                    "Initial state does not belong to resource"
            );
        }
    }

    public String identifier() {
        return identifier;
    }

    public Resource resource() {
        return resource;
    }

    public State currentState() {
        return currentState;
    }

    void transitionTo(State target) {
        State nextState = Objects.requireNonNull(target);

        if (nextState.resource() != resource) {
            throw new IllegalArgumentException(
                    "Target state does not belong to resource"
            );
        }

        this.currentState = nextState;
    }
}
