package mainstreet.surface;

import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BoundedProjectionReadTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final String RELEASE = "semantic-registry-1.0";
    private static final ProjectionContractIdentity CONTRACT =
            new ProjectionContractIdentity("platform", "merchant-presence");
    private static final ProjectionReadUseIdentity READ_USE =
            new ProjectionReadUseIdentity("platform", "public-merchant-presence");
    private static final ExposableElementReference DISPLAY_NAME =
            new ExposableElementReference("profile", "public-display-name");
    private static final ProjectionSourceDependencyReference PROFILE_SOURCE =
            new ProjectionSourceDependencyReference(
                    "profile",
                    "merchant-public-descriptor"
            );

    @Test
    void binds_one_immutable_read_to_exact_request_scope_release_contract_and_read_use() {
        EstablishedObservationRequest request = request();
        TestProfileFragment fragment = fragment("Bella", "profile-r8");

        BoundedProjectionRead read = new BoundedProjectionRead(
                request,
                CONTRACT,
                READ_USE,
                List.of(fragment)
        );

        assertEquals(
                EstablishedObservationRequestDetails.requestBinding(request),
                read.requestBinding()
        );
        assertEquals(MERCHANT, read.merchantScope());
        assertEquals(RELEASE, read.semanticRegistryReleaseIdentifier());
        assertEquals(CONTRACT, read.contractIdentity());
        assertEquals(READ_USE, read.readUseIdentity());
        assertEquals(List.of(fragment), read.fragments());
        assertThrows(
                UnsupportedOperationException.class,
                () -> read.fragments().add(fragment("Other", "profile-r9"))
        );
    }

    @Test
    void each_bounded_read_receives_fresh_opaque_runtime_identity() {
        EstablishedObservationRequest request = request();

        BoundedProjectionRead first = new BoundedProjectionRead(
                request,
                CONTRACT,
                READ_USE,
                List.of(fragment("Bella", "profile-r8"))
        );
        BoundedProjectionRead second = new BoundedProjectionRead(
                request,
                CONTRACT,
                READ_USE,
                List.of(fragment("Bella", "profile-r8"))
        );

        assertNotEquals(first.binding(), second.binding());
        assertFalse(Serializable.class.isAssignableFrom(
                BoundedProjectionReadBinding.class
        ));
        assertEquals(
                Set.of(),
                Arrays.stream(BoundedProjectionReadBinding.class.getMethods())
                        .filter(method -> method.getDeclaringClass()
                                == BoundedProjectionReadBinding.class)
                        .map(Method::getName)
                        .collect(Collectors.toUnmodifiableSet())
        );
    }

    @Test
    void fragment_exposes_only_candidate_identity_and_source_affinity_to_generic_spine() {
        assertEquals(
                Set.of("candidateObservation", "sourceAffinities"),
                Arrays.stream(ProjectionMaterialFragment.class.getMethods())
                        .filter(method -> method.getDeclaringClass()
                                == ProjectionMaterialFragment.class)
                        .map(Method::getName)
                        .collect(Collectors.toUnmodifiableSet())
        );

        TestProfileFragment fragment = fragment("Bella", "profile-r8");
        assertEquals("Bella", fragment.displayName());
        assertEquals(
                new ExposureCandidateObservation(DISPLAY_NAME, Optional.empty()),
                fragment.candidateObservation()
        );
        assertEquals(
                Set.of(new ProjectionMaterialSourceAffinity(
                        PROFILE_SOURCE,
                        "profile-r8"
                )),
                fragment.sourceAffinities()
        );
    }

    @Test
    void source_affinity_requires_exact_non_blank_observed_progress() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ProjectionMaterialSourceAffinity(PROFILE_SOURCE, " ")
        );
        assertThrows(
                NullPointerException.class,
                () -> new ProjectionMaterialSourceAffinity(null, "profile-r8")
        );
    }

    @Test
    void rejects_read_use_owned_by_another_projection_owner() {
        ProjectionReadUseIdentity foreignReadUse =
                new ProjectionReadUseIdentity("calendar", "public-calendar");

        assertThrows(
                IllegalArgumentException.class,
                () -> new BoundedProjectionRead(
                        request(),
                        CONTRACT,
                        foreignReadUse,
                        List.of(fragment("Bella", "profile-r8"))
                )
        );
    }

    @Test
    void rejects_duplicate_competing_fragment_identity_before_collection_deduplication() {
        TestProfileFragment r8 = fragment("Bella", "profile-r8");
        TestProfileFragment r9 = fragment("Bella Updated", "profile-r9");

        assertThrows(
                IllegalArgumentException.class,
                () -> new BoundedProjectionRead(
                        request(),
                        CONTRACT,
                        READ_USE,
                        List.of(r8, r9)
                )
        );
    }

    private static EstablishedObservationRequest request() {
        return new DefaultEstablishedObservationRequest(
                TestReleases.activeRelease(
                        MERCHANT.merchantIdentifier(),
                        RELEASE
                ),
                MERCHANT,
                RELEASE,
                Optional.empty()
        );
    }

    private static TestProfileFragment fragment(
            String displayName,
            String observedProgressIdentifier
    ) {
        return new TestProfileFragment(
                displayName,
                new ExposureCandidateObservation(
                        DISPLAY_NAME,
                        Optional.empty()
                ),
                Set.of(new ProjectionMaterialSourceAffinity(
                        PROFILE_SOURCE,
                        observedProgressIdentifier
                ))
        );
    }

    private record TestProfileFragment(
            String displayName,
            ExposureCandidateObservation candidateObservation,
            Set<ProjectionMaterialSourceAffinity> sourceAffinities
    ) implements ProjectionMaterialFragment {

        private TestProfileFragment {
            if (displayName == null || displayName.isBlank()) {
                throw new IllegalArgumentException(
                        "Display name must not be blank"
                );
            }
            if (candidateObservation == null) {
                throw new NullPointerException("candidateObservation");
            }
            sourceAffinities = Set.copyOf(sourceAffinities);
        }
    }
}
