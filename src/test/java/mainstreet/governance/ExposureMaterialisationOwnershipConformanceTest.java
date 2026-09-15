package mainstreet.governance;

import mainstreet.fulfilment.FulfilmentContractRegistrySnapshot;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import mainstreet.semantic.release.SemanticDefinitionSectionDecoder;
import mainstreet.semantic.release.SemanticReleaseAssembly;
import mainstreet.semantic.release.SemanticReleaseMaterialiser;
import mainstreet.surface.ExposureDecision;
import mainstreet.surface.ExposureElementContractRegistrySnapshot;
import mainstreet.surface.ExposureRequirementReference;
import mainstreet.surface.MerchantExposureChoiceSourceReference;
import mainstreet.surface.SurfaceContributionRegistrySnapshot;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Prevents semantic-release materialisation from becoming Exposure policy or
 * decision authority while ADR-016 adds the exact-release Exposure registry.
 */
class ExposureMaterialisationOwnershipConformanceTest {

    @Test
    void materialiser_retains_only_typed_definition_decoders_not_exposure_policy_state() {
        Field[] fields = SemanticReleaseMaterialiser.class.getDeclaredFields();

        assertEquals(
                4,
                fields.length,
                "E2 should add exactly one typed Exposure decoder, not semantic state"
        );
        assertTrue(
                Arrays.stream(fields).allMatch(field ->
                        field.getType() == SemanticDefinitionSectionDecoder.class
                ),
                "The materialiser may retain definition decoders only"
        );

        Set<Class<?>> forbiddenSemanticTypes = Set.of(
                ExposureDecision.class,
                MerchantExposureChoiceSourceReference.class,
                ExposureRequirementReference.class
        );
        for (Method method : SemanticReleaseMaterialiser.class.getDeclaredMethods()) {
            assertTrue(
                    !forbiddenSemanticTypes.contains(method.getReturnType())
                            && Arrays.stream(method.getParameterTypes())
                            .noneMatch(forbiddenSemanticTypes::contains),
                    "Materialisation must not expose Exposure policy/evaluation APIs: "
                            + method.getName()
            );
        }
    }

    @Test
    void release_assembly_remains_explicit_typed_coherence_not_a_generic_component_bag() {
        List<Class<?>> componentTypes = Arrays.stream(
                        SemanticReleaseAssembly.class.getRecordComponents()
                )
                .map(RecordComponent::getType)
                .toList();

        assertEquals(
                List.of(
                        SemanticRegistrySnapshot.class,
                        SurfaceContributionRegistrySnapshot.class,
                        FulfilmentContractRegistrySnapshot.class,
                        ExposureElementContractRegistrySnapshot.class
                ),
                componentTypes,
                "ADR-016 requires four explicit typed release constituents"
        );
        assertTrue(
                componentTypes.stream().noneMatch(type ->
                        java.util.Map.class.isAssignableFrom(type)
                                || Object.class.equals(type)
                ),
                "Release coherence must not become a generic semantic component bag"
        );
    }
}
