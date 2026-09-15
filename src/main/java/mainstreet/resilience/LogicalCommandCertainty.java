package mainstreet.resilience;

import java.util.Objects;

/**
 * Communication/execution certainty for one logical business command.
 * Transport attempts are intentionally absent: one command may have many.
 */
public record LogicalCommandCertainty(
        String logicalCommandIdentity,
        AcceptanceCertainty acceptance,
        ExecutionCertainty execution,
        AcknowledgementCertainty acknowledgement
) {
    public LogicalCommandCertainty {
        if (logicalCommandIdentity == null || logicalCommandIdentity.isBlank()) {
            throw new IllegalArgumentException("logicalCommandIdentity must not be blank");
        }
        Objects.requireNonNull(acceptance, "acceptance");
        Objects.requireNonNull(execution, "execution");
        Objects.requireNonNull(acknowledgement, "acknowledgement");

        if (execution == ExecutionCertainty.KNOWN_EXECUTED
                && acceptance == AcceptanceCertainty.KNOWN_NOT_ACCEPTED) {
            throw new IllegalArgumentException(
                    "Known execution cannot coexist with known non-acceptance"
            );
        }
        if (acknowledgement == AcknowledgementCertainty.ACKNOWLEDGED
                && execution == ExecutionCertainty.EXECUTION_UNCERTAIN) {
            throw new IllegalArgumentException(
                    "An uncertain execution outcome cannot be acknowledged as resolved"
            );
        }
    }
}
