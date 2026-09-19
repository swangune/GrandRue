package grandrue.semantic.registry;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

class SemanticRegistrySnapshotTest {

    @Test
    void rejects_two_definitions_with_the_same_semantic_identity() {
        RegisteredCapability first = new RegisteredCapability(
                "booking",
                List.of(),
                List.of()
        );
        RegisteredCapability conflicting = new RegisteredCapability(
                "booking",
                List.of(new OwnedOperationalObjectDefinition(
                        "booking",
                        Set.of("requested"),
                        "requested"
                )),
                List.of()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new SemanticRegistrySnapshot(
                        "semantic-registry-1.0",
                        Set.of(first, conflicting)
                )
        );
    }

    @Test
    void rejects_creation_that_does_not_produce_the_owned_initial_state() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new RegisteredCapability(
                        "booking",
                        List.of(new OwnedOperationalObjectDefinition(
                                "booking",
                                Set.of("requested", "confirmed"),
                                "requested"
                        )),
                        List.of(OwnedOperationDefinition.creation(
                                "booking.create",
                                "booking",
                                "confirmed",
                                "BookingCreated",
                                "booking.create"
                        ))
                )
        );
    }

    @Test
    void rejects_data_mutation_without_a_registered_schema_field() {
        OwnedSchemaDefinition schema = new OwnedSchemaDefinition(
                "booking",
                1,
                List.of(new OwnedFieldDefinition(
                        "title",
                        new FieldSpecificSemantics()
                ))
        );
        OwnedOperationalObjectDefinition booking =
                new OwnedOperationalObjectDefinition(
                        "booking",
                        Set.of(),
                        Optional.empty(),
                        Optional.of(schema.reference())
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> new RegisteredCapability(
                        "booking",
                        List.of(booking),
                        List.of(new OwnedOperationDefinition(
                                "booking.change-note",
                                List.of(new OwnedDataMutationEffect(
                                        "booking",
                                        "note"
                                )),
                                Set.of("booking.note-changed"),
                                "booking.change-note"
                        )),
                        List.of(),
                        List.of(),
                        List.of(schema),
                        List.of()
                )
        );
    }

    @Test
    void rejects_relationship_effect_without_registered_relationship() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new RegisteredCapability(
                        "booking",
                        List.of(new OwnedOperationalObjectDefinition(
                                "booking",
                                Set.of(),
                                Optional.empty(),
                                Optional.empty()
                        )),
                        List.of(new OwnedOperationDefinition(
                                "booking.link-customer",
                                List.of(new OwnedRelationshipEstablishmentEffect(
                                        "booking.customer-context"
                                )),
                                Set.of("booking.customer-linked"),
                                "booking.link-customer"
                        ))
                )
        );
    }

    @Test
    void rejects_relationship_target_not_registered_in_the_snapshot() {
        RegisteredCapability booking = new RegisteredCapability(
                "booking",
                List.of(new OwnedOperationalObjectDefinition(
                        "booking",
                        Set.of(),
                        Optional.empty(),
                        Optional.empty()
                )),
                List.of(),
                List.of(),
                List.of(new OwnedRelationshipDefinition(
                        "booking.customer-context",
                        "CUSTOMER",
                        "booking",
                        new OwnedOperationalObjectTypeReference(
                                "customer",
                                "customer-context"
                        ),
                        RelationshipCardinality.REQUIRED_ONE,
                        RelationshipScopeConstraint.SAME_MERCHANT
                ))
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new SemanticRegistrySnapshot(
                        "semantic-registry-1.0",
                        Set.of(booking)
                )
        );
    }
}
