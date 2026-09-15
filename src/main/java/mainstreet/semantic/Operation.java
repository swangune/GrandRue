package mainstreet.semantic;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class Operation {

    private final String identifier;
    private final Resource target;
    private final Transition transition;
    private final Privilege requiredPrivilege;

    private final Map<String, RequirementDefinition> requirements =
            new LinkedHashMap<>();

    Operation(
            String identifier,
            Resource target,
            Transition transition,
            Privilege requiredPrivilege
    ) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Operation identifier must not be blank"
            );
        }

        this.target = Objects.requireNonNull(target);
        this.transition = Objects.requireNonNull(transition);
        this.requiredPrivilege = Objects.requireNonNull(requiredPrivilege);

        if (transition.source().resource() != target) {
            throw new IllegalArgumentException(
                    "Operation transition must belong to target resource"
            );
        }

        this.identifier = identifier;
    }

    public String identifier() {
        return identifier;
    }

    public Resource target() {
        return target;
    }

    public Transition transition() {
        return transition;
    }

    public Privilege requiredPrivilege() {
        return requiredPrivilege;
    }

    public RequirementDefinition defineRequirement(
            String identifier
    ) {

        if (requirements.containsKey(identifier)) {
            throw new IllegalArgumentException(
                    "Requirement already exists: " + identifier
            );
        }

        RequirementDefinition requirement =
                new RequirementDefinition(this, identifier);

        requirements.put(identifier, requirement);

        return requirement;
    }

    public RequirementDefinition requirement(
            String identifier
    ) {
        return requirements.get(identifier);
    }

    public Map<String, RequirementDefinition> requirements() {
        return Collections.unmodifiableMap(requirements);
    }
}
