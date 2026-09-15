package mainstreet.identitysecurity;

import java.util.Objects;

/**
 * Authoritative non-technical failure during Identity security-generation
 * mutation.
 */
public final class IdentitySecurityGenerationException
        extends RuntimeException {

    private final IdentitySecurityGenerationFailureCategory category;

    public IdentitySecurityGenerationException(
            IdentitySecurityGenerationFailureCategory category,
            String message
    ) {
        super(message);
        this.category = Objects.requireNonNull(category, "category");
    }

    public IdentitySecurityGenerationFailureCategory category() {
        return category;
    }
}
