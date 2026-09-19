package grandrue.merchantprofile;

import grandrue.application.MerchantScope;
import grandrue.surface.ObservationRequestBinding;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Explicit BR4 falsification for MS-PROT-027 v1.14 §18 payload limits. */
class ProfileMaterialAffinityPayloadConformanceTest {

    @Test
    void material_affinity_contents_are_integrity_only_and_contain_no_rendered_business_value() {
        assertTrue(ProfileMaterialAffinityEntry.class.isRecord());
        assertEquals(
                List.of(
                        "candidateObservation",
                        "sourceReference",
                        "expectedProgressIdentifier"
                ),
                Arrays.stream(ProfileMaterialAffinityEntry.class.getRecordComponents())
                        .map(RecordComponent::getName)
                        .toList()
        );

        List<Field> contributionState = Arrays.stream(
                        ProfileMaterialAffinityObservationContribution.class
                                .getDeclaredFields()
                )
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .toList();
        assertEquals(
                Set.of("requestBinding", "merchantScope", "entries"),
                contributionState.stream().map(Field::getName).collect(java.util.stream.Collectors.toSet())
        );
        assertEquals(
                Set.of(
                        ObservationRequestBinding.class,
                        MerchantScope.class,
                        Set.class
                ),
                contributionState.stream().map(Field::getType).collect(java.util.stream.Collectors.toSet())
        );
    }
}
