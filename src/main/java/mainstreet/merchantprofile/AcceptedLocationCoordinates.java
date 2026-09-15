package mainstreet.merchantprofile;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** Optional coordinates explicitly accepted by the governing Location operation. */
public record AcceptedLocationCoordinates(
        BigDecimal latitude,
        BigDecimal longitude,
        LocationCoordinateSourceKind sourceKind,
        Optional<String> sourceReference,
        String acceptedByActorIdentity,
        Instant acceptedAt
) {
    private static final BigDecimal MIN_LATITUDE = new BigDecimal("-90");
    private static final BigDecimal MAX_LATITUDE = new BigDecimal("90");
    private static final BigDecimal MIN_LONGITUDE = new BigDecimal("-180");
    private static final BigDecimal MAX_LONGITUDE = new BigDecimal("180");

    public AcceptedLocationCoordinates {
        Objects.requireNonNull(latitude, "latitude");
        Objects.requireNonNull(longitude, "longitude");
        Objects.requireNonNull(sourceKind, "sourceKind");
        Objects.requireNonNull(sourceReference, "sourceReference");
        requireIdentifier(acceptedByActorIdentity, "acceptedByActorIdentity");
        Objects.requireNonNull(acceptedAt, "acceptedAt");
        if (latitude.compareTo(MIN_LATITUDE) < 0
                || latitude.compareTo(MAX_LATITUDE) > 0) {
            throw new IllegalArgumentException("latitude is outside [-90, 90]");
        }
        if (longitude.compareTo(MIN_LONGITUDE) < 0
                || longitude.compareTo(MAX_LONGITUDE) > 0) {
            throw new IllegalArgumentException(
                    "longitude is outside [-180, 180]"
            );
        }
        sourceReference.ifPresent(value ->
                requireIdentifier(value, "sourceReference")
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
