package grandrue.api;

import java.util.Objects;

/** Safe, owner-qualified detail beneath a stable API problem category. */
public record ApiOwnerProblemDetail(
        ApiOwnerContractReference ownerContractReference,
        String safeCode
) {
    public ApiOwnerProblemDetail {
        Objects.requireNonNull(ownerContractReference, "ownerContractReference");
        if (safeCode == null || safeCode.isBlank()) {
            throw new IllegalArgumentException(
                    "Owner-qualified safe problem code must not be blank"
            );
        }
    }
}
