package grandrue.merchantprofile;

import mainstreet.merchantprofile.MerchantPublicDescriptor;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * One immutable logical revision of a merchant-scoped public descriptor.
 */
public record MerchantPublicDescriptorRevision(
        MerchantPublicDescriptor descriptor,
        long revision,
        String mutationRequestIdentifier,
        String provenanceReference,
        String principalReference,
        Optional<String> originIdentifier,
        Instant committedAt
) {

    public MerchantPublicDescriptorRevision {
        Objects.requireNonNull(descriptor, "descriptor");
        if (revision < 1) {
            throw new IllegalArgumentException(
                    "Descriptor revision must be positive"
            );
        }
        require(mutationRequestIdentifier, "mutationRequestIdentifier");
        require(provenanceReference, "provenanceReference");
        require(principalReference, "principalReference");
        originIdentifier = Objects.requireNonNull(
                originIdentifier,
                "originIdentifier"
        );
        originIdentifier.ifPresent(origin ->
                require(origin, "originIdentifier")
        );
        Objects.requireNonNull(committedAt, "committedAt");
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
