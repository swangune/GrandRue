package grandrue.semantic.configuration;

import java.util.Objects;

/** Categorized fail-closed impact-review evidence persistence failure. */
public final class ConfigurationImpactReviewEvidencePersistenceException
        extends RuntimeException {

    private final ConfigurationImpactReviewEvidenceFailureCategory category;

    public ConfigurationImpactReviewEvidencePersistenceException(
            ConfigurationImpactReviewEvidenceFailureCategory category,
            String message
    ) {
        super(message);
        this.category = Objects.requireNonNull(category, "category");
    }

    public ConfigurationImpactReviewEvidencePersistenceException(
            ConfigurationImpactReviewEvidenceFailureCategory category,
            String message,
            Throwable cause
    ) {
        super(message, cause);
        this.category = Objects.requireNonNull(category, "category");
    }

    public ConfigurationImpactReviewEvidenceFailureCategory category() {
        return category;
    }
}
