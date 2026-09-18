package grandrue.semantic.configuration;

import java.util.Objects;

/** Fail-closed Configuration Revision materialisation/persistence failure. */
public final class ConfigurationRevisionPersistenceException
        extends IllegalStateException {

    private final ConfigurationRevisionFailureCategory category;

    public ConfigurationRevisionPersistenceException(
            ConfigurationRevisionFailureCategory category,
            String message
    ) {
        super(message);
        this.category = Objects.requireNonNull(category, "category");
    }

    public ConfigurationRevisionFailureCategory category() {
        return category;
    }

    public ConfigurationRevisionPersistenceException(ConfigurationRevisionFailureCategory category, String message, Throwable cause) {
        super(message, cause);
        this.category = Objects.requireNonNull(category, "category");
    }
}
