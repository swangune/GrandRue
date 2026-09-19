package grandrue.merchantprofile;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MerchantPublicDescriptorProjectionObservationBoundaryTest {

    @Test
    void coherent_observation_cannot_be_forged_through_a_public_creation_path() {
        Class<MerchantPublicDescriptorProjectionObservation> observationType =
                MerchantPublicDescriptorProjectionObservation.class;

        assertTrue(
                Arrays.stream(observationType.getConstructors()).findAny().isEmpty(),
                "BR3 observation must not expose a public constructor that can pair material "
                        + "from one Merchant Scope with equal-progress evidence from another"
        );

        assertTrue(
                Arrays.stream(observationType.getMethods())
                        .filter(method -> method.getDeclaringClass().equals(observationType))
                        .filter(method -> Modifier.isPublic(method.getModifiers()))
                        .filter(method -> Modifier.isStatic(method.getModifiers()))
                        .map(Method::getReturnType)
                        .noneMatch(observationType::equals),
                "BR3 observation must not expose a public factory that bypasses Profile ownership"
        );
    }
}
