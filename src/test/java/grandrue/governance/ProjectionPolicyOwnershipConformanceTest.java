package grandrue.governance;

import grandrue.surface.InitialProjectionPolicyEvaluatorPortfolio;
import grandrue.surface.ProjectionPolicyAssessment;
import grandrue.surface.ProjectionPolicyEvaluationContext;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Protects release composition from becoming a multi-owner policy authority. */
class ProjectionPolicyOwnershipConformanceTest {

    @Test
    void initial_projection_policy_portfolio_is_mechanical_release_composition_only() {
        Set<Method> declaredMethods = Arrays.stream(
                        InitialProjectionPolicyEvaluatorPortfolio.class
                                .getDeclaredMethods()
                )
                .filter(method -> !method.isSynthetic())
                .collect(Collectors.toUnmodifiableSet());

        assertEquals(
                Set.of("forRelease"),
                declaredMethods.stream()
                        .map(Method::getName)
                        .collect(Collectors.toUnmodifiableSet()),
                "The release portfolio must compose owner-supplied bindings, "
                        + "not implement projection policy semantics"
        );
        assertEquals(
                0,
                InitialProjectionPolicyEvaluatorPortfolio.class
                        .getDeclaredFields().length,
                "The release portfolio must not retain owner-specific semantic state"
        );
        assertTrue(
                declaredMethods.stream().noneMatch(method ->
                        method.getReturnType() == ProjectionPolicyAssessment.class
                                || Arrays.asList(method.getParameterTypes())
                                .contains(ProjectionPolicyEvaluationContext.class)
                ),
                "Projection policy evaluation belongs to the policy owner, "
                        + "not release composition"
        );
    }
}
