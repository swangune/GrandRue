package grandrue.businesshours;

import java.time.Instant;
import java.util.Objects;

/** Exact intent to withdraw current stable weekly Business Hours. */
public record WithdrawStandardBusinessHoursCommand(
        BusinessHoursScope scope,
        String expectedCurrentRevisionIdentity,
        String logicalRequestIdentity,
        String actingPrincipalIdentity,
        Instant committedAt
) {
    public WithdrawStandardBusinessHoursCommand {
        Objects.requireNonNull(scope, "scope");
        require(expectedCurrentRevisionIdentity, "expectedCurrentRevisionIdentity");
        require(logicalRequestIdentity, "logicalRequestIdentity");
        require(actingPrincipalIdentity, "actingPrincipalIdentity");
        Objects.requireNonNull(committedAt, "committedAt");
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
