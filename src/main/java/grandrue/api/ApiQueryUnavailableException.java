package grandrue.api;

import java.util.Objects;

/** Safe query delivery category, without owner exception text or evidence. */
public final class ApiQueryUnavailableException extends RuntimeException {
    private final ApiProblemCategory category;
    public ApiQueryUnavailableException(ApiProblemCategory category) {
        super(Objects.requireNonNull(category, "category").name());
        this.category = category;
    }
    public ApiProblemCategory category() { return category; }
}
