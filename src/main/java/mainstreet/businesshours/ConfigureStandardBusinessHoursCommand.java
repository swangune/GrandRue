package mainstreet.businesshours;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** Exact intent for one stable weekly Business Hours configuration revision. */
public record ConfigureStandardBusinessHoursCommand(
        StandardBusinessHours standardBusinessHours,
        Optional<String> expectedCurrentRevisionIdentity,
        String logicalRequestIdentity,
        String actingPrincipalIdentity,
        Instant committedAt
) {
    public ConfigureStandardBusinessHoursCommand {
        Objects.requireNonNull(standardBusinessHours, "standardBusinessHours");
        expectedCurrentRevisionIdentity = Objects.requireNonNull(
                expectedCurrentRevisionIdentity,
                "expectedCurrentRevisionIdentity"
        );
        expectedCurrentRevisionIdentity.ifPresent(value ->
                require(value, "expectedCurrentRevisionIdentity")
        );
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
