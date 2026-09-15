package mainstreet.semantic;

import java.util.Objects;

public final class RequirementDefinition {

    private final Operation owner;
    private final String identifier;

    RequirementDefinition(
            Operation owner,
            String identifier
    ) {

        this.owner = Objects.requireNonNull(owner);

        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Requirement identifier must not be blank"
            );
        }

        this.identifier = identifier;
    }

    public Operation owner() {
        return owner;
    }

    public String identifier() {
        return identifier;
    }
}
