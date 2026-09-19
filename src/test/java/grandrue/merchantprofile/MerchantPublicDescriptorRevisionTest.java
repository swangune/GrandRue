package grandrue.merchantprofile;

import grandrue.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MerchantPublicDescriptorRevisionTest {

    private static final Instant NOW =
            Instant.parse("2026-08-28T22:00:00Z");

    @Test
    void revision_preserves_exact_descriptor_and_mutation_evidence() {
        MerchantPublicDescriptor descriptor = descriptor("Acme");

        MerchantPublicDescriptorRevision revision =
                new MerchantPublicDescriptorRevision(
                        descriptor,
                        3,
                        "request-3",
                        "merchant-approved-profile",
                        "identity-42",
                        Optional.of("privileged-browser"),
                        NOW
                );

        assertEquals(descriptor, revision.descriptor());
        assertEquals(3, revision.revision());
        assertEquals("request-3", revision.mutationRequestIdentifier());
        assertEquals(
                "merchant-approved-profile",
                revision.provenanceReference()
        );
        assertEquals("identity-42", revision.principalReference());
        assertEquals(
                Optional.of("privileged-browser"),
                revision.originIdentifier()
        );
        assertEquals(NOW, revision.committedAt());
    }

    @Test
    void mutation_command_requires_positive_expected_revision_and_evidence() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MerchantPublicDescriptorMutationCommand(
                        descriptor("Acme"),
                        Optional.of(0L),
                        "request-1",
                        "merchant-approved-profile",
                        "identity-42",
                        Optional.empty(),
                        NOW
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new MerchantPublicDescriptorMutationCommand(
                        descriptor("Acme"),
                        Optional.empty(),
                        " ",
                        "merchant-approved-profile",
                        "identity-42",
                        Optional.empty(),
                        NOW
                )
        );
    }

    private static MerchantPublicDescriptor descriptor(String name) {
        return new MerchantPublicDescriptor(
                new MerchantScope("merchant-acme"),
                name,
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );
    }
}
