package grandrue.surface;

import grandrue.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObservationContributionRegistrySnapshotTest {

    private static final ObservationContributionKind KIND =
            new ObservationContributionKind("booking", "customer-context");

    @Test
    void accepts_empty_definition_and_runtime_binding_snapshots() {
        ObservationContributionDefinitionRegistrySnapshot definitions =
                new ObservationContributionDefinitionRegistrySnapshot(
                        "semantic-registry-1.0",
                        List.of()
                );
        ObservationContributionRuntimeBindingSnapshot bindings =
                new ObservationContributionRuntimeBindingSnapshot(List.of());

        assertEquals("semantic-registry-1.0",
                definitions.semanticRegistryReleaseIdentifier());
        assertTrue(definitions.definitions().isEmpty());
        assertTrue(bindings.bindings().isEmpty());
        assertTrue(definitions.definition(KIND).isEmpty());
        assertTrue(bindings.binding(KIND).isEmpty());
    }

    @Test
    void registers_exact_owner_qualified_definition_for_one_release() {
        ObservationContributionDefinition definition =
                new ObservationContributionDefinition(
                        KIND,
                        Set.of(SurfaceAudience.CUSTOMER),
                        ObservationContributionCardinality.SINGLE
                );
        ObservationContributionDefinitionRegistrySnapshot snapshot =
                new ObservationContributionDefinitionRegistrySnapshot(
                        "semantic-registry-1.0",
                        List.of(definition)
                );

        assertEquals(Optional.of(definition), snapshot.definition(KIND));
        assertEquals(Set.of(definition), snapshot.definitions());
        assertThrows(
                UnsupportedOperationException.class,
                () -> snapshot.definitions().clear()
        );
    }

    @Test
    void definition_contains_only_kind_audiences_and_single_cardinality() {
        ObservationContributionDefinition definition =
                new ObservationContributionDefinition(
                        KIND,
                        Set.of(
                                SurfaceAudience.PUBLIC,
                                SurfaceAudience.CUSTOMER
                        ),
                        ObservationContributionCardinality.SINGLE
                );

        assertEquals(
                List.of(
                        ObservationContributionKind.class,
                        Set.class,
                        ObservationContributionCardinality.class
                ),
                List.of(ObservationContributionDefinition.class
                        .getRecordComponents())
                        .stream()
                        .map(component -> component.getType())
                        .toList()
        );
        assertEquals(1, ObservationContributionCardinality.values().length);
        assertEquals(
                ObservationContributionCardinality.SINGLE,
                definition.cardinality()
        );
        assertFalse(Modifier.isPublic(
                ValidContribution.class.getModifiers()
        ));
        assertTrue(Modifier.isFinal(
                ValidContribution.class.getModifiers()
        ));
    }

    @Test
    void rejects_blank_identity_empty_audience_and_duplicate_definition() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ObservationContributionKind(" ", "contribution")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ObservationContributionKind("booking", " ")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ObservationContributionDefinition(
                        KIND,
                        Set.of(),
                        ObservationContributionCardinality.SINGLE
                )
        );

        ObservationContributionDefinition definition =
                new ObservationContributionDefinition(
                        KIND,
                        Set.of(SurfaceAudience.CUSTOMER),
                        ObservationContributionCardinality.SINGLE
                );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ObservationContributionDefinitionRegistrySnapshot(
                        "semantic-registry-1.0",
                        List.of(definition, definition)
                )
        );
    }

    @Test
    void runtime_binding_requires_exact_final_package_private_contract_class() {
        ObservationContributionRuntimeBinding binding =
                new ObservationContributionRuntimeBinding(
                        KIND,
                        ValidContribution.class
                );
        ObservationContributionRuntimeBindingSnapshot snapshot =
                new ObservationContributionRuntimeBindingSnapshot(
                        List.of(binding)
                );

        assertEquals(Optional.of(binding), snapshot.binding(KIND));
        assertTrue(snapshot.matches(KIND, ValidContribution.class));
        assertFalse(snapshot.matches(
                new ObservationContributionKind("booking", "other"),
                ValidContribution.class
        ));
        assertThrows(
                IllegalArgumentException.class,
                () -> new ObservationContributionRuntimeBinding(
                        KIND,
                        PublicContribution.class
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ObservationContributionRuntimeBinding(
                        KIND,
                        NonFinalContribution.class
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ObservationContributionRuntimeBindingSnapshot(
                        List.of(binding, binding)
                )
        );
    }

    public static final class PublicContribution
            implements EstablishedObservationContribution {

        @Override
        public ObservationContributionKind kind() {
            return new ObservationContributionKind(
                    "booking",
                    "customer-context"
            );
        }

        @Override
        public ObservationRequestBinding requestBinding() {
            throw new UnsupportedOperationException();
        }

        @Override
        public MerchantScope merchantScope() {
            throw new UnsupportedOperationException();
        }
    }
}

final class ValidContribution implements EstablishedObservationContribution {

    @Override
    public ObservationContributionKind kind() {
        return new ObservationContributionKind("booking", "customer-context");
    }

    @Override
    public ObservationRequestBinding requestBinding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MerchantScope merchantScope() {
        throw new UnsupportedOperationException();
    }
}


class NonFinalContribution implements EstablishedObservationContribution {

    @Override
    public ObservationContributionKind kind() {
        return new ObservationContributionKind("booking", "customer-context");
    }

    @Override
    public ObservationRequestBinding requestBinding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MerchantScope merchantScope() {
        throw new UnsupportedOperationException();
    }
}
