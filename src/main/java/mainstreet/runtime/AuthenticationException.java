package mainstreet.runtime;

import grandrue.runtime.AuthenticationFailureCategory;

import java.util.Objects;

/**
 * Fail-closed authentication/principal-establishment failure. It remains
 * distinct from downstream authorisation failures.
 */
public final class AuthenticationException extends RuntimeException {

    private final AuthenticationFailureCategory category;

    public AuthenticationException(
            AuthenticationFailureCategory category,
            String message
    ) {
        super(message);
        this.category = Objects.requireNonNull(category, "category");
    }

    public AuthenticationFailureCategory category() {
        return category;
    }
}
