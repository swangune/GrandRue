package grandrue.semantic.configuration;

import java.util.Objects;

/** Persistence/admission failure while recording exact validation evidence. */
public final class ConfigurationValidationEvidencePersistenceException
        extends RuntimeException {

    private final ConfigurationValidationEvidenceFailureCategory category;

    public ConfigurationValidationEvidencePersistenceException(
            ConfigurationValidationEvidenceFailureCategory category,
            String message
    ) {
        super(message);
        this.category = Objects.requireNonNull(category, "category");
    }

    public ConfigurationValidationEvidencePersistenceException(
            ConfigurationValidationEvidenceFailureCategory category,
            String message,
            Throwable cause
    ) {
        super(message, cause);
        this.category = Objects.requireNonNull(category, "category");
    }

    public ConfigurationValidationEvidenceFailureCategory category() {
        return category;
    }
}
