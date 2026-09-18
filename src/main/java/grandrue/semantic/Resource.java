package grandrue.semantic;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Resource {

    private final String identifier;

    private final Map<String, State> states =
            new LinkedHashMap<>();

    private final Map<String, Operation> operations =
            new LinkedHashMap<>();

    public Resource(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Resource identifier must not be blank"
            );
        }

        this.identifier = identifier;
    }

    public String identifier() {
        return identifier;
    }

    public State defineState(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "State identifier must not be blank"
            );
        }

        if (states.containsKey(identifier)) {
            throw new IllegalArgumentException(
                    "State already exists: " + identifier
            );
        }

        State state =
                new State(this, identifier);

        states.put(identifier, state);

        return state;
    }

    public State state(String identifier) {
        return states.get(identifier);
    }

    public Map<String, State> states() {
        return Collections.unmodifiableMap(states);
    }

    public Operation defineOperation(
            String identifier,
            Transition transition,
            Privilege requiredPrivilege
    ) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Operation identifier must not be blank"
            );
        }

        if (operations.containsKey(identifier)) {
            throw new IllegalArgumentException(
                    "Operation already exists: " + identifier
            );
        }

        Operation operation =
                new Operation(
                        identifier,
                        this,
                        transition,
                        requiredPrivilege
                );

        operations.put(identifier, operation);

        return operation;
    }

    public Operation operation(String identifier) {
        return operations.get(identifier);
    }

    public Map<String, Operation> operations() {
        return Collections.unmodifiableMap(operations);
    }
}