package grandrue.semantic.configuration;

import java.util.Objects;

/** Categorized failure to derive or persist exact requirement evidence. */
public final class ConfigurationNewActivityRequirementSetPersistenceException
        extends RuntimeException {

    private final ConfigurationNewActivityRequirementSetFailureCategory
            category;

    public ConfigurationNewActivityRequirementSetPersistenceException(
            ConfigurationNewActivityRequirementSetFailureCategory category,
            String message
    ) {
        super(message);
        this.category = Objects.requireNonNull(category, "category");
    }

    public ConfigurationNewActivityRequirementSetPersistenceException(
            ConfigurationNewActivityRequirementSetFailureCategory category,
            String message,
            Throwable cause
    ) {
        super(message, cause);
        this.category = Objects.requireNonNull(category, "category");
    }

    public ConfigurationNewActivityRequirementSetFailureCategory category() {
        return category;
    }
}
