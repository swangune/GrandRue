package grandrue.background;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** One attributable physical attempt for one durable work instruction. */
public record WorkAttempt(
        String attemptIdentity,
        String workIdentity,
        Instant attemptedAt,
        String principalReference,
        Optional<BackgroundWorkResultClassification> resultClassification,
        Optional<String> evidenceReference
) {
    public WorkAttempt {
        require(attemptIdentity, "attemptIdentity");
        require(workIdentity, "workIdentity");
        Objects.requireNonNull(attemptedAt, "attemptedAt");
        require(principalReference, "principalReference");

        resultClassification =
                Objects.requireNonNull(resultClassification, "resultClassification");
        evidenceReference =
                Objects.requireNonNull(evidenceReference, "evidenceReference");

        evidenceReference.ifPresent(value -> require(value, "evidenceReference"));
    }

    /**
     * Compatibility constructor for existing callers that already possess a
     * known attempt outcome.
     *
     * <p>This does not manufacture additional lifecycle evidence. It represents
     * the supplied known classification as the outcome of this physical
     * attempt.</p>
     */
    public WorkAttempt(
            String attemptIdentity,
            String workIdentity,
            Instant attemptedAt,
            String principalReference,
            BackgroundWorkResultClassification resultClassification,
            Optional<String> evidenceReference
    ) {
        this(
                attemptIdentity,
                workIdentity,
                attemptedAt,
                principalReference,
                Optional.of(Objects.requireNonNull(
                        resultClassification,
                        "resultClassification"
                )),
                evidenceReference
        );
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}