package mainstreet.semantic.registry;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Immutable operation semantics owned by a registered capability. An
 * operation owns a bounded collection of declarative authoritative effects.
 */
public record OwnedOperationDefinition(
        String identifier,
        List<OwnedOperationEffect> effects,
        List<OwnedRequirementDefinition> unconditionalRequirements,
        List<OwnedConditionalRequirementDefinition> conditionalRequirements,
        Set<String> eventIdentifiers,
        String requiredPrivilegeIdentifier
) {

    public OwnedOperationDefinition {
        requireIdentifier(identifier, "Operation identifier");
        effects = List.copyOf(Objects.requireNonNull(effects));
        if (effects.isEmpty()) {
            throw new IllegalArgumentException(
                    "An owned operation requires at least one effect"
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
        eventIdentifiers = Set.copyOf(Objects.requireNonNull(eventIdentifiers));
        for (String eventIdentifier : eventIdentifiers) {
            requireIdentifier(eventIdentifier, "Event identifier");
        }
        requireIdentifier(
                requiredPrivilegeIdentifier,
                "Required privilege identifier"
        );
    }

    public OwnedOperationDefinition(
            String identifier,
            List<OwnedOperationEffect> effects,
            List<OwnedRequirementDefinition> unconditionalRequirements,
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

    public OwnedOperationDefinition(
            String identifier,
            List<OwnedOperationEffect> effects,
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

    /** Defines an operation whose bounded effect creates one Operational Object. */
    public static OwnedOperationDefinition creation(
            String identifier,
            String targetObjectIdentifier,
            String initialStateIdentifier,
            String eventIdentifier,
            String requiredPrivilegeIdentifier
    ) {
        return new OwnedOperationDefinition(
                identifier,
                List.of(new OwnedObjectCreationEffect(
                        targetObjectIdentifier,
                        initialStateIdentifier
                )),
                List.of(),
                List.of(),
                Set.of(eventIdentifier),
                requiredPrivilegeIdentifier
        );
    }

    private static void requireUniqueRequirements(
            List<OwnedRequirementDefinition> unconditionalRequirements,
            List<OwnedConditionalRequirementDefinition> conditionalRequirements
    ) {
        Set<String> identifiers = new HashSet<>();
        for (OwnedRequirementDefinition requirement : unconditionalRequirements) {
            requireUniqueIdentifier(identifiers, requirement.identifier());
        }
        for (OwnedConditionalRequirementDefinition requirement
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
                    "Owned operation has duplicate requirements"
            );
        }
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
