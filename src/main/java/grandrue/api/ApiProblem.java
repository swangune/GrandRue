package grandrue.api;

import java.util.Objects;
import java.util.Optional;

/**
 * Transport-neutral, externally safe problem semantics.
 *
 * <p>Protocol status, internal exception detail and retry policy are not part
 * of this value.</p>
 */
public record ApiProblem(
        ApiProblemCategory category,
        Optional<ApiOwnerProblemDetail> ownerDetail
) {
    public ApiProblem {
        Objects.requireNonNull(category, "category");
        ownerDetail = Objects.requireNonNull(ownerDetail, "ownerDetail");
    }
}
