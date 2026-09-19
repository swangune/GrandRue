package grandrue.surface;

import grandrue.merchantprofile.MerchantContactPointExposureChoiceReadPort;
import grandrue.merchantprofile.MerchantLocationExposureChoiceReadPort;
import grandrue.merchantprofile.ProfileContactPointExposureChoiceEvaluator;
import grandrue.merchantprofile.ProfileMerchantLocationExposureChoiceEvaluator;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExposureSubmissionAffinityArchitectureConformanceTest {

    @Test
    void binding_issuance_and_concrete_identity_remain_surface_owned_and_non_public() {
        assertEquals("mainstreet.surface", ExposureCandidateEvaluationBinding.class.getPackageName());
        assertEquals("mainstreet.surface", ExposureCandidateEvaluationBindings.class.getPackageName());
        assertEquals("mainstreet.surface", RuntimeExposureCandidateEvaluationBinding.class.getPackageName());

        assertFalse(Modifier.isPublic(ExposureCandidateEvaluationBindings.class.getModifiers()));
        assertFalse(Modifier.isPublic(RuntimeExposureCandidateEvaluationBinding.class.getModifiers()));
        assertTrue(Arrays.stream(RuntimeExposureCandidateEvaluationBinding.class.getDeclaredConstructors())
                .noneMatch(constructor -> Modifier.isPublic(constructor.getModifiers())));
        assertEquals(
                Set.of(),
                Arrays.stream(ExposureCandidateEvaluationBinding.class.getMethods())
                        .filter(method -> method.getDeclaringClass()
                                == ExposureCandidateEvaluationBinding.class)
                        .map(java.lang.reflect.Method::getName)
                        .collect(Collectors.toUnmodifiableSet())
        );
    }

    @Test
    void execution_affinity_does_not_enter_owner_context_candidate_or_positive_membership() {
        assertNoBindingComponent(ExposureCandidateObservation.class.getRecordComponents());
        assertNoBindingComponent(ExposedElementMembership.class.getRecordComponents());

        assertTrue(Arrays.stream(OwnerExposureEvaluationContext.class.getMethods())
                .noneMatch(method -> method.getReturnType()
                        == ExposureCandidateEvaluationBinding.class));
        assertTrue(Arrays.stream(OwnerExposureEvaluationContext.class.getMethods())
                .flatMap(method -> Arrays.stream(method.getParameterTypes()))
                .noneMatch(type -> type == ExposureCandidateEvaluationBinding.class));
    }

    @Test
    void evaluator_boundary_uses_submission_wrapper_and_results_do_not_repeat_candidate_identity() {
        assertEquals(
                "java.util.List<mainstreet.surface.ExposureCandidateEvaluationSubmission>",
                Arrays.stream(ExposureRequirementEvaluator.class.getDeclaredMethods())
                        .filter(method -> method.getName().equals("evaluateBatch"))
                        .findFirst()
                        .orElseThrow()
                        .getGenericParameterTypes()[2]
                        .getTypeName()
        );
        assertEquals(
                "java.util.List<mainstreet.surface.ExposureCandidateEvaluationSubmission>",
                Arrays.stream(MerchantExposureChoiceEvaluator.class.getDeclaredMethods())
                        .filter(method -> method.getName().equals("evaluateBatch"))
                        .findFirst()
                        .orElseThrow()
                        .getGenericParameterTypes()[2]
                        .getTypeName()
        );
        assertEquals(
                List.of("evaluationBinding", "decision"),
                Arrays.stream(ExposureRequirementCandidateEvaluation.class.getRecordComponents())
                        .map(RecordComponent::getName)
                        .toList()
        );
        assertEquals(
                List.of("evaluationBinding", "decision"),
                Arrays.stream(MerchantExposureChoiceCandidateEvaluation.class.getRecordComponents())
                        .map(RecordComponent::getName)
                        .toList()
        );
    }

    @Test
    void profile_evaluators_depend_only_on_profile_read_ports_and_cannot_own_surface_issuance() {
        assertEquals(
                Set.of(MerchantContactPointExposureChoiceReadPort.class),
                Arrays.stream(ProfileContactPointExposureChoiceEvaluator.class.getDeclaredFields())
                        .filter(field -> !Modifier.isStatic(field.getModifiers()))
                        .map(java.lang.reflect.Field::getType)
                        .collect(Collectors.toUnmodifiableSet())
        );
        assertEquals(
                Set.of(MerchantLocationExposureChoiceReadPort.class),
                Arrays.stream(ProfileMerchantLocationExposureChoiceEvaluator.class.getDeclaredFields())
                        .filter(field -> !Modifier.isStatic(field.getModifiers()))
                        .map(java.lang.reflect.Field::getType)
                        .collect(Collectors.toUnmodifiableSet())
        );
        assertTrue(Arrays.stream(ProfileContactPointExposureChoiceEvaluator.class.getDeclaredMethods())
                .noneMatch(method -> method.getReturnType()
                        == ExposureCandidateEvaluationBinding.class));
        assertTrue(Arrays.stream(ProfileMerchantLocationExposureChoiceEvaluator.class.getDeclaredMethods())
                .noneMatch(method -> method.getReturnType()
                        == ExposureCandidateEvaluationBinding.class));
    }

    @Test
    void generic_resolver_retains_no_domain_dependency_as_state_or_public_contract() {
        assertEquals("mainstreet.surface", ExposureResolver.class.getPackageName());
        assertEquals(0, ExposureResolver.class.getDeclaredFields().length);
        assertTrue(Arrays.stream(ExposureResolver.class.getDeclaredMethods())
                .flatMap(method -> {
                    Class<?>[] parameters = method.getParameterTypes();
                    Class<?>[] all = Arrays.copyOf(parameters, parameters.length + 1);
                    all[parameters.length] = method.getReturnType();
                    return Arrays.stream(all);
                })
                .filter(type -> !type.isPrimitive())
                .map(Class::getPackageName)
                .noneMatch(packageName -> packageName.startsWith("mainstreet.merchantprofile")
                        || packageName.startsWith("mainstreet.booking")
                        || packageName.startsWith("mainstreet.ordering")
                        || packageName.startsWith("mainstreet.inventory")
                        || packageName.startsWith("mainstreet.payment")
                        || packageName.startsWith("mainstreet.fulfilment")
                        || packageName.startsWith("mainstreet.scheduling")
                        || packageName.startsWith("mainstreet.publication")));
    }

    private static void assertNoBindingComponent(RecordComponent[] components) {
        assertTrue(Arrays.stream(components)
                .noneMatch(component -> component.getType()
                        == ExposureCandidateEvaluationBinding.class));
    }
}
