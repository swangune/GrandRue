package mainstreet.configuration;

import java.util.Objects;

public record OperationConfiguration(
        String identifier,
        String targetResource,
        TransitionConfiguration transition,
        String requiredPrivilege
) {
    public OperationConfiguration {
        Objects.requireNonNull(identifier);
        Objects.requireNonNull(targetResource);
        Objects.requireNonNull(transition);
        Objects.requireNonNull(requiredPrivilege);
    }
}