package grandrue.merchantprofile;

import mainstreet.merchantprofile.MerchantPublicDescriptor;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Exact intent and evidence for one owner-specific Merchant Public Descriptor
 * mutation under MS-PROT-051 v1.1.
 */
public record MerchantPublicDescriptorMutationCommand(
        MerchantPublicDescriptor descriptor,
        Optional<Long> expectedRevision,
        String mutationRequestIdentifier,
        String provenanceReference,
        String principalReference,
        Optional<String> originIdentifier,
        Instant committedAt
) {

    public MerchantPublicDescriptorMutationCommand {
        Objects.requireNonNull(descriptor, "descriptor");
        expectedRevision = Objects.requireNonNull(
                expectedRevision,
                "expectedRevision"
        );
        expectedRevision.ifPresent(revision -> {
            if (revision < 1) {
                throw new IllegalArgumentException(
                        "Expected descriptor revision must be positive"
                );
            }
        });
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
