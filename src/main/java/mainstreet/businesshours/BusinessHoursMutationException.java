package mainstreet.businesshours;

import java.util.Objects;

/** Observable failure from the stable Business Hours mutation authority. */
public final class BusinessHoursMutationException extends RuntimeException {
    private final BusinessHoursFailureCategory category;

    public BusinessHoursMutationException(
            BusinessHoursFailureCategory category,
            String message
    ) {
        super(message);
        this.category = Objects.requireNonNull(category, "category");
    }

    public BusinessHoursMutationException(
            BusinessHoursFailureCategory category,
            String message,
            Throwable cause
    ) {
        super(message, cause);
        this.category = Objects.requireNonNull(category, "category");
    }

    public BusinessHoursFailureCategory category() {
        return category;
    }
}
