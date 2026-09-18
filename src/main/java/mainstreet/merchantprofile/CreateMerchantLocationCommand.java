package mainstreet.merchantprofile;

import grandrue.merchantprofile.PostalAddressInput;

import grandrue.merchantprofile.AcceptedLocationCoordinates;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** Exact intent for creating one merchant-scoped Location identity. */
public record CreateMerchantLocationCommand(
        MerchantScope merchantScope,
        String locationIdentity,
        Optional<String> publicLabel,
        PostalAddressInput postalAddress,
        Optional<AcceptedLocationCoordinates> acceptedCoordinates,
        String logicalRequestIdentity,
        String provenanceReference,
        String actingPrincipalIdentity,
        Instant committedAt
) {
    public CreateMerchantLocationCommand {
        Objects.requireNonNull(merchantScope, "merchantScope");
        require(locationIdentity, "locationIdentity");
        publicLabel = requireOptional(publicLabel, "publicLabel");
        Objects.requireNonNull(postalAddress, "postalAddress");
        Objects.requireNonNull(acceptedCoordinates, "acceptedCoordinates");
        require(logicalRequestIdentity, "logicalRequestIdentity");
        require(provenanceReference, "provenanceReference");
        require(actingPrincipalIdentity, "actingPrincipalIdentity");
        Objects.requireNonNull(committedAt, "committedAt");
        validateCoordinates(
                acceptedCoordinates,
                actingPrincipalIdentity,
                committedAt
        );
    }

    static Optional<String> requireOptional(
            Optional<String> value,
            String label
    ) {
        Objects.requireNonNull(value, label);
        value.ifPresent(component -> require(component, label));
        return value;
    }

    static void validateCoordinates(
            Optional<AcceptedLocationCoordinates> coordinates,
            String actor,
            Instant committedAt
    ) {
        coordinates.ifPresent(value -> {
            if (!value.acceptedByActorIdentity().equals(actor)
                    || value.acceptedAt().isAfter(committedAt)) {
                throw new IllegalArgumentException(
                        "Coordinates require matching actor acceptance before commit"
                );
            }
        });
    }

    static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
