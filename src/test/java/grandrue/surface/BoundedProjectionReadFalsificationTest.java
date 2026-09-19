package grandrue.surface;

import grandrue.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BoundedProjectionReadFalsificationTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final String RELEASE = "semantic-registry-1.0";
    private static final ProjectionContractIdentity CONTRACT =
            new ProjectionContractIdentity("platform", "merchant-presence");
    private static final ProjectionReadUseIdentity READ_USE =
            new ProjectionReadUseIdentity("platform", "public-merchant-presence");
    private static final ExposableElementReference CONTACT_POINT =
            new ExposableElementReference("profile", "public-contact-point");
    private static final ProjectionSourceDependencyReference PROFILE_SOURCE =
            new ProjectionSourceDependencyReference(
                    "profile",
                    "merchant-contact-point"
            );

    @Test
    void distinct_instances_of_same_exposable_element_remain_distinct_material() {
        TestFragment first = fragment("contact-a", "profile-r8");
        TestFragment second = fragment("contact-b", "profile-r8");

        BoundedProjectionRead read = new BoundedProjectionRead(
                request(),
                CONTRACT,
                READ_USE,
                List.of(first, second)
        );

        assertEquals(List.of(first, second), read.fragments());
    }

    @Test
    void exact_duplicate_instance_identity_is_rejected_even_when_private_value_and_progress_differ() {
        ExposureCandidateObservation candidate = candidate("contact-a");
        TestFragment first = new TestFragment(
                "first-private-value",
                candidate,
                Set.of(new ProjectionMaterialSourceAffinity(
                        PROFILE_SOURCE,
                        "profile-r8"
                ))
        );
        TestFragment competing = new TestFragment(
                "competing-private-value",
                candidate,
                Set.of(new ProjectionMaterialSourceAffinity(
                        PROFILE_SOURCE,
                        "profile-r9"
                ))
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new BoundedProjectionRead(
                        request(),
                        CONTRACT,
                        READ_USE,
                        List.of(first, competing)
                )
        );
    }

    @Test
    void bounded_read_binding_is_not_request_or_evaluator_submission_identity() {
        EstablishedObservationRequest request = request();
        BoundedProjectionRead read = new BoundedProjectionRead(
                request,
                CONTRACT,
                READ_USE,
                List.of(fragment("contact-a", "profile-r8"))
        );

        assertNotSame(
                EstablishedObservationRequestDetails.requestBinding(request),
                read.binding()
        );
        assertFalse(ObservationRequestBinding.class.isAssignableFrom(
                BoundedProjectionReadBinding.class
        ));
        assertFalse(ExposureCandidateEvaluationBinding.class.isAssignableFrom(
                BoundedProjectionReadBinding.class
        ));
    }

    @Test
    void generic_fragment_contract_has_no_untyped_payload_or_map_escape_hatch() {
        for (Method method : ProjectionMaterialFragment.class.getDeclaredMethods()) {
            assertFalse(method.getReturnType().equals(Object.class));
            assertFalse(Map.class.isAssignableFrom(method.getReturnType()));
            for (Class<?> parameterType : method.getParameterTypes()) {
                assertFalse(parameterType.equals(Object.class));
                assertFalse(Map.class.isAssignableFrom(parameterType));
            }
        }
        assertEquals(
                Set.of("candidateObservation", "sourceAffinities"),
                Set.of(
                        ProjectionMaterialFragment.class.getDeclaredMethods()[0].getName(),
                        ProjectionMaterialFragment.class.getDeclaredMethods()[1].getName()
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

    private static TestFragment fragment(
            String instanceIdentifier,
            String observedProgressIdentifier
    ) {
        return new TestFragment(
                "owner-private-" + instanceIdentifier,
                candidate(instanceIdentifier),
                Set.of(new ProjectionMaterialSourceAffinity(
                        PROFILE_SOURCE,
                        observedProgressIdentifier
                ))
        );
    }

    private static ExposureCandidateObservation candidate(String instanceIdentifier) {
        return new ExposureCandidateObservation(
                CONTACT_POINT,
                Optional.of(new ExposureCandidateInstanceReference(
                        "profile",
                        "contact-point",
                        instanceIdentifier
                ))
        );
    }

    private record TestFragment(
            String ownerPrivateValue,
            ExposureCandidateObservation candidateObservation,
            Set<ProjectionMaterialSourceAffinity> sourceAffinities
    ) implements ProjectionMaterialFragment {

        private TestFragment {
            sourceAffinities = Set.copyOf(sourceAffinities);
        }
    }
}
