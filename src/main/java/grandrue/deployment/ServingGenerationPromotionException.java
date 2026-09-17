package grandrue.deployment;

import java.util.Objects;

public final class ServingGenerationPromotionException extends RuntimeException {
    private final ServingGenerationPromotionFailureCategory category;

    public ServingGenerationPromotionException(ServingGenerationPromotionFailureCategory category, String message) {
        super(message);
        this.category = Objects.requireNonNull(category, "category");
    }

    public ServingGenerationPromotionException(ServingGenerationPromotionFailureCategory category, String message, Throwable cause) {
        super(message, cause);
        this.category = Objects.requireNonNull(category, "category");
    }

    public ServingGenerationPromotionFailureCategory category() { return category; }
}
