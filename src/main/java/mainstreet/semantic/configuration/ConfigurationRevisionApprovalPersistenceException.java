package mainstreet.semantic.configuration;

import java.util.Objects;

/** Categorized fail-closed ordinary first-approval rejection. */
public final class ConfigurationRevisionApprovalPersistenceException extends RuntimeException {
    private final ConfigurationRevisionApprovalFailureCategory category;
    public ConfigurationRevisionApprovalPersistenceException(ConfigurationRevisionApprovalFailureCategory category, String message) {
        super(message); this.category = Objects.requireNonNull(category, "category");
    }
    public ConfigurationRevisionApprovalPersistenceException(ConfigurationRevisionApprovalFailureCategory category, String message, Throwable cause) {
        super(message, cause); this.category = Objects.requireNonNull(category, "category");
    }
    public ConfigurationRevisionApprovalFailureCategory category() { return category; }
}
