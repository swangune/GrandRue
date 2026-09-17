package grandrue.businesshours;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** One immutable exact revision of stable weekly Public Business Hours. */
public record StandardBusinessHoursRevision(
        String revisionIdentity,
        BusinessHoursScope scope,
        long revisionNumber,
        Optional<String> predecessorRevisionIdentity,
        StandardBusinessHoursRevisionDisposition disposition,
        Optional<StandardBusinessHours> standardBusinessHours,
        Optional<String> merchantLocationRevisionIdentity,
        String logicalRequestIdentity,
        String actingPrincipalIdentity,
        String controllerRelationshipIdentity,
        Instant committedAt
) {
    public StandardBusinessHoursRevision {
        require(revisionIdentity, "revisionIdentity");
        Objects.requireNonNull(scope, "scope");
        if (revisionNumber < 1) {
            throw new IllegalArgumentException("revisionNumber must be positive");
        }
        predecessorRevisionIdentity = Objects.requireNonNull(
                predecessorRevisionIdentity,
                "predecessorRevisionIdentity"
        );
        predecessorRevisionIdentity.ifPresent(value ->
                require(value, "predecessorRevisionIdentity")
        );
        disposition = Objects.requireNonNull(disposition, "disposition");
        standardBusinessHours = Objects.requireNonNull(
                standardBusinessHours,
                "standardBusinessHours"
        );
        if ((disposition == StandardBusinessHoursRevisionDisposition.CONFIGURED)
                != standardBusinessHours.isPresent()) {
            throw new IllegalArgumentException(
                    "CONFIGURED revisions require hours and WITHDRAWN revisions forbid them"
            );
        }
        standardBusinessHours.ifPresent(hours -> {
            if (!hours.scope().equals(scope)) {
                throw new IllegalArgumentException("Revision hours must match its scope");
            }
        });
        merchantLocationRevisionIdentity = Objects.requireNonNull(
                merchantLocationRevisionIdentity,
                "merchantLocationRevisionIdentity"
        );
        merchantLocationRevisionIdentity.ifPresent(value ->
                require(value, "merchantLocationRevisionIdentity")
        );
        if ((scope.kind() == BusinessHoursScopeKind.MERCHANT_LOCATION)
                != merchantLocationRevisionIdentity.isPresent()) {
            throw new IllegalArgumentException(
                    "Location-scoped revisions require exact Merchant Location provenance"
            );
        }
        require(logicalRequestIdentity, "logicalRequestIdentity");
        require(actingPrincipalIdentity, "actingPrincipalIdentity");
        require(controllerRelationshipIdentity, "controllerRelationshipIdentity");
        Objects.requireNonNull(committedAt, "committedAt");
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
