package mainstreet.merchantprofile;

import grandrue.merchantprofile.AcceptedLocationCoordinates;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** Same-place correction intent for an existing active Location identity. */
public record CorrectMerchantLocationCommand(
        MerchantScope merchantScope,
        String locationIdentity,
        String expectedCurrentRevisionIdentity,
        Optional<String> publicLabel,
        PostalAddressInput postalAddress,
        Optional<AcceptedLocationCoordinates> acceptedCoordinates,
        String logicalRequestIdentity,
        String provenanceReference,
        String actingPrincipalIdentity,
        Instant committedAt
) {
    public CorrectMerchantLocationCommand {
        Objects.requireNonNull(merchantScope, "merchantScope");
        CreateMerchantLocationCommand.require(
                locationIdentity,
                "locationIdentity"
        );
        CreateMerchantLocationCommand.require(
                expectedCurrentRevisionIdentity,
                "expectedCurrentRevisionIdentity"
        );
        publicLabel = CreateMerchantLocationCommand.requireOptional(
                publicLabel,
                "publicLabel"
        );
        Objects.requireNonNull(postalAddress, "postalAddress");
        Objects.requireNonNull(acceptedCoordinates, "acceptedCoordinates");
        CreateMerchantLocationCommand.require(
                logicalRequestIdentity,
                "logicalRequestIdentity"
        );
        CreateMerchantLocationCommand.require(
                provenanceReference,
                "provenanceReference"
        );
        CreateMerchantLocationCommand.require(
                actingPrincipalIdentity,
                "actingPrincipalIdentity"
        );
        Objects.requireNonNull(committedAt, "committedAt");
        CreateMerchantLocationCommand.validateCoordinates(
                acceptedCoordinates,
                actingPrincipalIdentity,
                committedAt
        );
    }
}
