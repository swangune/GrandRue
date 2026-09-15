package mainstreet.merchantprofile;

import mainstreet.application.MerchantScope;
import mainstreet.surface.BoundedProjectionRead;
import mainstreet.surface.EstablishedObservationContribution;
import mainstreet.surface.EstablishedObservationRequest;
import mainstreet.surface.ExposableElementReference;
import mainstreet.surface.ExposureCandidateInstanceReference;
import mainstreet.surface.ExposureCandidateObservation;
import mainstreet.surface.ObservationContributionCardinality;
import mainstreet.surface.ObservationContributionConstructionBoundary;
import mainstreet.surface.ObservationContributionDefinition;
import mainstreet.surface.ObservationContributionKind;
import mainstreet.surface.ObservationContributionRuntimeBinding;
import mainstreet.surface.ObservationContributionConstructor;
import mainstreet.surface.ProjectionContractIdentity;
import mainstreet.surface.ProjectionMaterialFragment;
import mainstreet.surface.ProjectionMaterialSourceAffinity;
import mainstreet.surface.ProjectionReadUseIdentity;
import mainstreet.surface.ProjectionSourceAvailability;
import mainstreet.surface.ProjectionSourceCompleteness;
import mainstreet.surface.ProjectionSourceDependencyReference;
import mainstreet.surface.ProjectionSourceEvidence;
import mainstreet.surface.ProjectionSourceRevocationState;
import mainstreet.surface.SurfaceAudience;
import mainstreet.surface.TestObservationRequests;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProfileMaterialAffinityObservationContributionTest {

    private static final MerchantScope MERCHANT =
            new MerchantScope("merchant-a");
    private static final ObservationContributionKind KIND =
            new ObservationContributionKind("profile", "material-affinity");
    private static final ProjectionSourceDependencyReference CONTACT_SOURCE =
            new ProjectionSourceDependencyReference("profile", "contact-points");
    private static final ProjectionSourceDependencyReference LOCATION_SOURCE =
            new ProjectionSourceDependencyReference("profile", "merchant-locations");
    private static final ProjectionSourceDependencyReference DESCRIPTOR_SOURCE =
            new ProjectionSourceDependencyReference(
                    "profile",
                    "merchant-public-descriptor"
            );
    private static final Instant OBSERVED_AT =
            Instant.parse("2026-09-03T02:30:00Z");

    @Test
    void registers_one_public_single_profile_material_affinity_kind_with_exact_runtime_class() {
        assertEquals(
                KIND,
                ProfileMaterialAffinityObservationContributionRegistration.kind()
        );

        ObservationContributionDefinition definition =
                ProfileMaterialAffinityObservationContributionRegistration.definition();
        assertEquals(KIND, definition.kind());
        assertEquals(Set.of(SurfaceAudience.PUBLIC), definition.permittedAudiences());
        assertEquals(
                ObservationContributionCardinality.SINGLE,
                definition.cardinality()
        );

        ObservationContributionRuntimeBinding binding =
                ProfileMaterialAffinityObservationContributionRegistration.runtimeBinding();
        assertEquals(KIND, binding.kind());
        Class<? extends EstablishedObservationContribution> implementation =
                binding.contractClass();
        assertEquals(
                ProfileMaterialAffinityObservationContribution.class,
                implementation
        );
        assertTrue(Modifier.isFinal(implementation.getModifiers()));
        assertFalse(Modifier.isPublic(implementation.getModifiers()));
    }

    @Test
    void derives_expected_progress_only_from_the_exact_bounded_read_and_p2_evidence() {
        EstablishedObservationRequest request = request(MERCHANT);
        ExposureCandidateObservation contact = contactCandidate("C1");
        ExposureCandidateObservation location = locationCandidate("L1");
        BoundedProjectionRead read = read(
                request,
                List.of(
                        fragment(contact, CONTACT_SOURCE, "contact-revision-8"),
                        fragment(location, LOCATION_SOURCE, "location-revision-3")
                ),
                Set.of(
                        evidence(CONTACT_SOURCE, "contact-revision-8"),
                        evidence(LOCATION_SOURCE, "location-revision-3")
                )
        );

        ProfileMaterialAffinityEvidence affinity =
                ProfileMaterialAffinityEvidence.from(read);

        assertSame(read.requestBinding(), affinity.requestBinding());
        assertEquals(MERCHANT, affinity.merchantScope());
        assertEquals(2, affinity.entries().size());
        assertEquals(
                Set.of(
                        new ProfileMaterialAffinityEntry(
                                contact,
                                CONTACT_SOURCE,
                                "contact-revision-8"
                        ),
                        new ProfileMaterialAffinityEntry(
                                location,
                                LOCATION_SOURCE,
                                "location-revision-3"
                        )
                ),
                affinity.entries()
        );
        assertThrows(
                UnsupportedOperationException.class,
                () -> affinity.entries().clear()
        );
    }

    @Test
    void request_construction_binds_exact_bounded_read_evidence_without_requerying() {
        EstablishedObservationRequest request = request(MERCHANT);
        ProfileMaterialAffinityEvidence evidence =
                ProfileMaterialAffinityEvidence.from(read(
                        request,
                        List.of(fragment(
                                contactCandidate("C1"),
                                CONTACT_SOURCE,
                                "contact-revision-8"
                        )),
                        Set.of(evidence(
                                CONTACT_SOURCE,
                                "contact-revision-8"
                        ))
                ));
        ObservationContributionConstructor constructor =
                ProfileMaterialAffinityObservationContributionRegistration
                        .constructor(evidence);

        EstablishedObservationContribution contribution =
                new ObservationContributionConstructionBoundary().construct(
                        request,
                        constructor
                );

        ProfileMaterialAffinityObservationContribution typed =
                assertInstanceOf(
                        ProfileMaterialAffinityObservationContribution.class,
                        contribution
                );
        assertEquals(KIND, typed.kind());
        assertSame(readBinding(evidence), typed.requestBinding());
        assertEquals(MERCHANT, typed.merchantScope());
        assertEquals(evidence.entries(), typed.entries());
    }

    @Test
    void rejects_cross_request_rebinding_even_for_the_same_merchant_and_progress() {
        EstablishedObservationRequest q1 = request(MERCHANT);
        ProfileMaterialAffinityEvidence q1Evidence =
                ProfileMaterialAffinityEvidence.from(read(
                        q1,
                        List.of(fragment(
                                contactCandidate("C1"),
                                CONTACT_SOURCE,
                                "contact-revision-8"
                        )),
                        Set.of(evidence(
                                CONTACT_SOURCE,
                                "contact-revision-8"
                        ))
                ));
        EstablishedObservationRequest q2 = request(MERCHANT);

        assertThrows(
                IllegalStateException.class,
                () -> new ObservationContributionConstructionBoundary().construct(
                        q2,
                        ProfileMaterialAffinityObservationContributionRegistration
                                .constructor(q1Evidence)
                )
        );
    }

    @Test
    void rejects_cross_merchant_material_evidence() {
        EstablishedObservationRequest merchantARequest = request(MERCHANT);
        ProfileMaterialAffinityEvidence evidence =
                ProfileMaterialAffinityEvidence.from(read(
                        merchantARequest,
                        List.of(fragment(
                                contactCandidate("C1"),
                                CONTACT_SOURCE,
                                "contact-revision-8"
                        )),
                        Set.of(evidence(
                                CONTACT_SOURCE,
                                "contact-revision-8"
                        ))
                ));

        assertThrows(
                IllegalStateException.class,
                () -> new ObservationContributionConstructionBoundary().construct(
                        request(new MerchantScope("merchant-b")),
                        ProfileMaterialAffinityObservationContributionRegistration
                                .constructor(evidence)
                )
        );
    }

    @Test
    void ignores_baseline_descriptor_but_rejects_wrong_or_incoherent_affinity() {
        EstablishedObservationRequest request = request(MERCHANT);
        ProfileMaterialAffinityEvidence baselineOnly =
                ProfileMaterialAffinityEvidence.from(read(
                        request,
                        List.of(fragment(
                                new ExposureCandidateObservation(
                                        new ExposableElementReference(
                                                "profile",
                                                "public-display-name"
                                        ),
                                        Optional.empty()
                                ),
                                DESCRIPTOR_SOURCE,
                                "descriptor-revision-7"
                        )),
                        Set.of(evidence(
                                DESCRIPTOR_SOURCE,
                                "descriptor-revision-7"
                        ))
                ));
        assertTrue(baselineOnly.entries().isEmpty());

        ExposureCandidateObservation contact = contactCandidate("C1");
        assertThrows(
                IllegalArgumentException.class,
                () -> ProfileMaterialAffinityEvidence.from(read(
                        request,
                        List.of(fragment(
                                contact,
                                LOCATION_SOURCE,
                                "location-revision-3"
                        )),
                        Set.of(evidence(
                                LOCATION_SOURCE,
                                "location-revision-3"
                        ))
                ))
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> ProfileMaterialAffinityEvidence.from(read(
                        request,
                        List.of(fragment(
                                contact,
                                CONTACT_SOURCE,
                                "contact-revision-8"
                        )),
                        Set.of(evidence(
                                CONTACT_SOURCE,
                                "contact-revision-9"
                        ))
                ))
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> ProfileMaterialAffinityEvidence.from(read(
                        request,
                        List.of(new TestFragment(
                                contact,
                                Set.of(
                                        new ProjectionMaterialSourceAffinity(
                                                CONTACT_SOURCE,
                                                "contact-revision-8"
                                        ),
                                        new ProjectionMaterialSourceAffinity(
                                                CONTACT_SOURCE,
                                                "contact-revision-9"
                                        )
                                )
                        )),
                        Set.of(evidence(
                                CONTACT_SOURCE,
                                "contact-revision-8"
                        ))
                ))
        );
    }

    private static EstablishedObservationRequest request(MerchantScope scope) {
        return TestObservationRequests.request(
                scope,
                "semantic-registry-1.0"
        );
    }

    private static Object readBinding(ProfileMaterialAffinityEvidence evidence) {
        return evidence.requestBinding();
    }

    private static BoundedProjectionRead read(
            EstablishedObservationRequest request,
            List<? extends ProjectionMaterialFragment> fragments,
            Set<ProjectionSourceEvidence> sourceEvidence
    ) {
        return new BoundedProjectionRead(
                request,
                new ProjectionContractIdentity("platform", "merchant-presence"),
                new ProjectionReadUseIdentity(
                        "platform",
                        "public-merchant-presence"
                ),
                sourceEvidence,
                fragments
        );
    }

    private static ProjectionSourceEvidence evidence(
            ProjectionSourceDependencyReference source,
            String progress
    ) {
        return new ProjectionSourceEvidence(
                source,
                "evidence-" + source.sourceIdentifier() + "-" + progress,
                Optional.of(progress),
                Optional.of(progress),
                OBSERVED_AT,
                ProjectionSourceAvailability.AVAILABLE,
                ProjectionSourceCompleteness.COMPLETE,
                ProjectionSourceRevocationState.NOT_APPLICABLE
        );
    }

    private static ProjectionMaterialFragment fragment(
            ExposureCandidateObservation candidate,
            ProjectionSourceDependencyReference source,
            String progress
    ) {
        return new TestFragment(
                candidate,
                Set.of(new ProjectionMaterialSourceAffinity(source, progress))
        );
    }

    private static ExposureCandidateObservation contactCandidate(String identity) {
        return new ExposureCandidateObservation(
                new ExposableElementReference(
                        "profile",
                        "public-contact-point"
                ),
                Optional.of(new ExposureCandidateInstanceReference(
                        "profile",
                        "contact-point",
                        identity
                ))
        );
    }

    private static ExposureCandidateObservation locationCandidate(String identity) {
        return new ExposureCandidateObservation(
                new ExposableElementReference(
                        "profile",
                        "public-merchant-location"
                ),
                Optional.of(new ExposureCandidateInstanceReference(
                        "profile",
                        "merchant-location",
                        identity
                ))
        );
    }

    private record TestFragment(
            ExposureCandidateObservation candidateObservation,
            Set<ProjectionMaterialSourceAffinity> sourceAffinities
    ) implements ProjectionMaterialFragment {
        private TestFragment {
            sourceAffinities = Set.copyOf(sourceAffinities);
        }
    }
}
