package mainstreet.semantic.executable;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Fully resolved operation contract consumed by the generic runtime. Effects
 * remain bounded and explicit; one operation is not assumed to equal one
 * state transition. Presence in a resolved model establishes applicability,
 * not permission to execute in every runtime context.
 */
public record ExecutableOperationDefinition(
        String identifier,
        List<ExecutableOperationEffect> effects,
        List<ExecutableRequirementDefinition> unconditionalRequirements,
        List<ExecutableConditionalRequirementDefinition>
                conditionalRequirements,
        Set<String> eventIdentifiers,
        String requiredPrivilegeIdentifier
) {

    public ExecutableOperationDefinition {
        Objects.requireNonNull(identifier);
        effects = List.copyOf(Objects.requireNonNull(effects));
        if (effects.isEmpty()) {
            throw new IllegalArgumentException(
                    "An executable operation requires at least one effect"
            );
        }
        unconditionalRequirements = List.copyOf(
                Objects.requireNonNull(unconditionalRequirements)
        );
        conditionalRequirements = List.copyOf(
                Objects.requireNonNull(conditionalRequirements)
        );
        requireUniqueRequirements(
                unconditionalRequirements,
                conditionalRequirements
        );
        eventIdentifiers = Set.copyOf(
                Objects.requireNonNull(eventIdentifiers)
        );
        Objects.requireNonNull(requiredPrivilegeIdentifier);
    }

    public ExecutableOperationDefinition(
            String identifier,
            List<ExecutableOperationEffect> effects,
            List<ExecutableRequirementDefinition> unconditionalRequirements,
            Set<String> eventIdentifiers,
            String requiredPrivilegeIdentifier
    ) {
        this(
                identifier,
                effects,
                unconditionalRequirements,
                List.of(),
                eventIdentifiers,
                requiredPrivilegeIdentifier
        );
    }

    public ExecutableOperationDefinition(
            String identifier,
            List<ExecutableOperationEffect> effects,
            Set<String> eventIdentifiers,
            String requiredPrivilegeIdentifier
    ) {
        this(
                identifier,
                effects,
                List.of(),
                List.of(),
                eventIdentifiers,
                requiredPrivilegeIdentifier
        );
    }

    private static void requireUniqueRequirements(
            List<ExecutableRequirementDefinition> unconditionalRequirements,
            List<ExecutableConditionalRequirementDefinition>
                    conditionalRequirements
    ) {
        Set<String> identifiers = new HashSet<>();
        for (ExecutableRequirementDefinition requirement
                : unconditionalRequirements) {
            requireUniqueIdentifier(identifiers, requirement.identifier());
        }
        for (ExecutableConditionalRequirementDefinition requirement
                : conditionalRequirements) {
            requireUniqueIdentifier(identifiers, requirement.identifier());
        }
    }

    private static void requireUniqueIdentifier(
            Set<String> identifiers,
            String identifier
    ) {
        if (!identifiers.add(identifier)) {
            throw new IllegalArgumentException(
                    "Executable operation has duplicate requirements"
            );
        }
    }
}
