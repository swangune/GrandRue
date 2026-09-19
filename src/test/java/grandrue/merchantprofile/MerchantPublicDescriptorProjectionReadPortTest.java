package grandrue.merchantprofile;

import grandrue.application.MerchantScope;
import grandrue.surface.ExposableElementReference;
import grandrue.surface.ProjectionMaterialSourceAffinity;
import grandrue.surface.ProjectionSourceAvailability;
import grandrue.surface.ProjectionSourceCompleteness;
import grandrue.surface.ProjectionSourceDependencyReference;
import grandrue.surface.ProjectionSourceRevocationState;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MerchantPublicDescriptorProjectionReadPortTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final Instant OBSERVED_AT =
            Instant.parse("2026-09-03T01:00:00Z");
    private static final ProjectionSourceDependencyReference DESCRIPTOR_SOURCE =
            new ProjectionSourceDependencyReference(
                    "profile",
                    "merchant-public-descriptor"
            );

    @Test
    void current_descriptor_is_acquired_as_typed_owner_material_with_exact_revision_affinity() {
        MerchantPublicDescriptor descriptor = descriptor(
                MERCHANT,
                Optional.of("Neighbourhood bakery"),
                Optional.of("Fresh bread every morning"),
                Optional.of("Independent bakery serving Swansea."));
        MerchantPublicDescriptorRevision revision = revision(descriptor, 7);
        MerchantPublicDescriptorProjectionReadPort port =
                new AuthorityBackedMerchantPublicDescriptorProjectionReadPort(
                        authority(Optional.of(revision))
                );

        MerchantPublicDescriptorProjectionObservation observation =
                port.observe(MERCHANT, OBSERVED_AT);
        MerchantPublicDescriptorProjectionMaterial material =
                observation.material().orElseThrow();

        assertEquals(MERCHANT, material.merchantScope());
        assertEquals("descriptor-revision-7", material.observedProgressIdentifier());
        assertEquals(4, material.fragments().size());
        assertEquals(
                Set.of(
                        new ExposableElementReference("profile", "public-display-name"),
                        new ExposableElementReference("profile", "public-tagline"),
                        new ExposableElementReference("profile", "public-short-summary"),
                        new ExposableElementReference("profile", "public-approved-description")
                ),
                material.fragments().stream()
                        .map(fragment -> fragment.candidateObservation().elementReference())
                        .collect(Collectors.toUnmodifiableSet())
        );

        ProjectionMaterialSourceAffinity expectedAffinity =
                new ProjectionMaterialSourceAffinity(
                        DESCRIPTOR_SOURCE,
                        "descriptor-revision-7"
                );
        assertTrue(material.fragments().stream().allMatch(fragment ->
                fragment.sourceAffinities().equals(Set.of(expectedAffinity))
        ));
        assertEquals(
                Set.of(
                        "Bella Bakery",
                        "Neighbourhood bakery",
                        "Fresh bread every morning",
                        "Independent bakery serving Swansea."
                ),
                material.fragments().stream()
                        .map(MerchantPublicDescriptorProjectionFragment::value)
                        .collect(Collectors.toUnmodifiableSet())
        );

        assertEquals(DESCRIPTOR_SOURCE, observation.sourceEvidence().sourceReference());
        assertEquals(
                Optional.of("descriptor-revision-7"),
                observation.sourceEvidence().observedProgressIdentifier()
        );
        assertEquals(
                Optional.of("descriptor-revision-7"),
                observation.sourceEvidence().requiredCurrentProgressIdentifier()
        );
        assertEquals(OBSERVED_AT, observation.sourceEvidence().observedAt());
        assertEquals(
                ProjectionSourceAvailability.AVAILABLE,
                observation.sourceEvidence().availability()
        );
        assertEquals(
                ProjectionSourceCompleteness.COMPLETE,
                observation.sourceEvidence().completeness()
        );
        assertEquals(
                ProjectionSourceRevocationState.NOT_APPLICABLE,
                observation.sourceEvidence().revocationState()
        );
        assertTrue(observation.sourceEvidence().isCurrent());
    }

    @Test
    void one_owner_read_establishes_material_and_p2_evidence_for_the_same_progress() {
        MerchantPublicDescriptorRevision firstRevision = revision(
                descriptor(MERCHANT, Optional.empty(), Optional.empty(), Optional.empty()),
                7
        );
        MerchantPublicDescriptorRevision laterRevision = revision(
                descriptor(
                        MERCHANT,
                        Optional.of("Changed later"),
                        Optional.empty(),
                        Optional.empty()
                ),
                8
        );
        AtomicInteger reads = new AtomicInteger();
        MerchantPublicDescriptorAuthority changingAuthority = new MerchantPublicDescriptorAuthority() {
            @Override
            public MerchantPublicDescriptorRevision establish(
                    MerchantPublicDescriptorMutationCommand command
            ) {
                throw new UnsupportedOperationException();
            }

            @Override
            public MerchantPublicDescriptorRevision revise(
                    MerchantPublicDescriptorMutationCommand command
            ) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Optional<MerchantPublicDescriptorRevision> current(
                    MerchantScope merchantScope
            ) {
                return Optional.of(
                        reads.getAndIncrement() == 0
                                ? firstRevision
                                : laterRevision
                );
            }

            @Override
            public Optional<MerchantPublicDescriptorRevision> revision(
                    MerchantScope merchantScope,
                    long revision
            ) {
                return Optional.empty();
            }
        };
        MerchantPublicDescriptorProjectionReadPort port =
                new AuthorityBackedMerchantPublicDescriptorProjectionReadPort(
                        changingAuthority
                );

        MerchantPublicDescriptorProjectionObservation observation =
                port.observe(MERCHANT, OBSERVED_AT);
        MerchantPublicDescriptorProjectionMaterial material =
                observation.material().orElseThrow();

        assertEquals(1, reads.get());
        assertEquals("descriptor-revision-7", material.observedProgressIdentifier());
        assertEquals(
                Optional.of(material.observedProgressIdentifier()),
                observation.sourceEvidence().observedProgressIdentifier()
        );
        assertEquals(
                observation.sourceEvidence().observedProgressIdentifier(),
                observation.sourceEvidence().requiredCurrentProgressIdentifier()
        );
        assertTrue(material.fragments().stream().allMatch(fragment ->
                fragment.sourceAffinities().stream().allMatch(affinity ->
                        Optional.of(affinity.observedProgressIdentifier()).equals(
                                observation.sourceEvidence().observedProgressIdentifier()
                        )
                )
        ));
    }

    @Test
    void absent_optional_values_create_no_fragments_and_equal_values_at_new_revision_get_new_affinity() {
        MerchantPublicDescriptor descriptor = descriptor(
                MERCHANT,
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );
        MerchantPublicDescriptorProjectionMaterial first =
                new AuthorityBackedMerchantPublicDescriptorProjectionReadPort(
                        authority(Optional.of(revision(descriptor, 7)))
                ).observe(MERCHANT, OBSERVED_AT).material().orElseThrow();
        MerchantPublicDescriptorProjectionMaterial second =
                new AuthorityBackedMerchantPublicDescriptorProjectionReadPort(
                        authority(Optional.of(revision(descriptor, 8)))
                ).observe(MERCHANT, OBSERVED_AT).material().orElseThrow();

        assertEquals(1, first.fragments().size());
        assertEquals(
                new ExposableElementReference("profile", "public-display-name"),
                first.fragments().getFirst().candidateObservation().elementReference()
        );
        assertNotEquals(
                first.observedProgressIdentifier(),
                second.observedProgressIdentifier()
        );
        assertNotEquals(
                first.fragments().getFirst().sourceAffinities(),
                second.fragments().getFirst().sourceAffinities()
        );
    }

    @Test
    void missing_descriptor_yields_no_material_but_retains_exact_noncurrent_p2_evidence() {
        MerchantPublicDescriptorProjectionReadPort port =
                new AuthorityBackedMerchantPublicDescriptorProjectionReadPort(
                        authority(Optional.empty())
                );

        MerchantPublicDescriptorProjectionObservation observation =
                port.observe(MERCHANT, OBSERVED_AT);

        assertTrue(observation.material().isEmpty());
        assertEquals(DESCRIPTOR_SOURCE, observation.sourceEvidence().sourceReference());
        assertTrue(observation.sourceEvidence().observedProgressIdentifier().isEmpty());
        assertTrue(observation.sourceEvidence().requiredCurrentProgressIdentifier().isEmpty());
        assertEquals(
                ProjectionSourceAvailability.UNAVAILABLE,
                observation.sourceEvidence().availability()
        );
        assertEquals(
                ProjectionSourceCompleteness.MISSING,
                observation.sourceEvidence().completeness()
        );
        assertEquals(
                ProjectionSourceRevocationState.NOT_APPLICABLE,
                observation.sourceEvidence().revocationState()
        );
        assertTrue(observation.sourceEvidence().hasMissingOrUnverifiableEvidence());
    }

    @Test
    void authority_scope_mismatch_is_structural_failure() {
        MerchantScope other = new MerchantScope("merchant-b");
        MerchantPublicDescriptorRevision wrongScope = revision(
                descriptor(other, Optional.empty(), Optional.empty(), Optional.empty()),
                3
        );
        MerchantPublicDescriptorProjectionReadPort port =
                new AuthorityBackedMerchantPublicDescriptorProjectionReadPort(
                        authority(Optional.of(wrongScope))
                );

        assertThrows(
                IllegalStateException.class,
                () -> port.observe(MERCHANT, OBSERVED_AT)
        );
    }

    private static MerchantPublicDescriptor descriptor(
            MerchantScope scope,
            Optional<String> tagline,
            Optional<String> shortSummary,
            Optional<String> approvedDescription
    ) {
        return new MerchantPublicDescriptor(
                scope,
                "Bella Bakery",
                tagline,
                shortSummary,
                approvedDescription
        );
    }

    private static MerchantPublicDescriptorRevision revision(
            MerchantPublicDescriptor descriptor,
            long revision
    ) {
        return new MerchantPublicDescriptorRevision(
                descriptor,
                revision,
                "request-" + revision,
                "provenance-" + revision,
                "principal-1",
                Optional.empty(),
                Instant.parse("2026-09-03T00:00:00Z")
        );
    }

    private static MerchantPublicDescriptorAuthority authority(
            Optional<MerchantPublicDescriptorRevision> current
    ) {
        return new MerchantPublicDescriptorAuthority() {
            @Override
            public MerchantPublicDescriptorRevision establish(
                    MerchantPublicDescriptorMutationCommand command
            ) {
                throw new UnsupportedOperationException();
            }

            @Override
            public MerchantPublicDescriptorRevision revise(
                    MerchantPublicDescriptorMutationCommand command
            ) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Optional<MerchantPublicDescriptorRevision> current(
                    MerchantScope merchantScope
            ) {
                return current;
            }

            @Override
            public Optional<MerchantPublicDescriptorRevision> revision(
                    MerchantScope merchantScope,
                    long revision
            ) {
                return Optional.empty();
            }
        };
    }
}
