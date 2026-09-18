package grandrue.semantic.configuration;

import java.time.Instant;
import java.util.Objects;

/** The change-set identity identifies logical intent; materialisedAt is physical execution evidence. */
public record MaterialiseConfigurationChangeCommand(
        ConfigurationChangeSet changeSet,
        String requestedRevisionIdentifier,
        Instant materialisedAt
) {
    public MaterialiseConfigurationChangeCommand {
        Objects.requireNonNull(changeSet, "changeSet");
        if (requestedRevisionIdentifier == null || requestedRevisionIdentifier.isBlank()) {
            throw new IllegalArgumentException("Requested revision identifier must not be blank");
        }
        Objects.requireNonNull(materialisedAt, "materialisedAt");
    }
}
