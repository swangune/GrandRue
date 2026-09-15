package mainstreet.semantic.executable;

import java.util.Objects;
import java.util.Optional;

/** Resolved creation contract for one Operational Object. */
public record ExecutableObjectCreationEffect(
        ExecutableOperationalObjectTypeIdentity targetObjectType,
        Optional<String> initialStateIdentifier
) implements ExecutableOperationEffect {
    public ExecutableObjectCreationEffect {
        Objects.requireNonNull(targetObjectType);
        initialStateIdentifier = Objects.requireNonNull(initialStateIdentifier);
        initialStateIdentifier.ifPresent(initial -> {
            if (initial.isBlank()) {
                throw new IllegalArgumentException(
                        "Initial state identifier must not be blank"
                );
            }
        });
    }

    public ExecutableObjectCreationEffect(
            ExecutableOperationalObjectTypeIdentity targetObjectType,
            String initialStateIdentifier
    ) {
        this(targetObjectType, Optional.ofNullable(initialStateIdentifier));
    }
}
