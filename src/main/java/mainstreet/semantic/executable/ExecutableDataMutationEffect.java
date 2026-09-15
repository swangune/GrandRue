package mainstreet.semantic.executable;

import java.util.Objects;

/** Resolved field-targeted authoritative data mutation contract. */
public record ExecutableDataMutationEffect(
        ExecutableOperationalObjectTypeIdentity targetObjectType,
        ExecutableFieldReference targetField
) implements ExecutableOperationEffect {
    public ExecutableDataMutationEffect {
        Objects.requireNonNull(targetObjectType);
        Objects.requireNonNull(targetField);
        if (!targetObjectType.ownerCapabilityIdentifier().equals(
                targetField.ownerCapabilityIdentifier()
        )) {
            throw new IllegalArgumentException(
                    "Mutation target object and field must have the same capability owner"
            );
        }
    }
}
