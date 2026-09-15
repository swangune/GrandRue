package mainstreet.prototype;

import mainstreet.semantic.executable.ExecutableAllocationClaimEffect;
import mainstreet.semantic.executable.ExecutableMerchantModel;
import mainstreet.semantic.executable.ExecutableObjectCreationEffect;
import mainstreet.semantic.executable.ExecutableOperationalObjectDefinition;
import mainstreet.semantic.executable.ExecutableOperationalObjectTypeIdentity;
import mainstreet.semantic.executable.ExecutableOperationDefinition;
import mainstreet.semantic.executable.ExecutableOperationEffect;
import mainstreet.semantic.executable.ExecutableRelationshipDefinition;
import mainstreet.semantic.executable.ExecutableRelationshipEstablishmentEffect;
import mainstreet.semantic.registry.RelationshipCardinality;
import mainstreet.semantic.registry.RelationshipScopeConstraint;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Prototype-only executable composition from already-resolved capability
 * selections. It contains no merchant-category branching.
 */
final class PrototypeExecutableModelFactory {

    private static final ExecutableOperationalObjectTypeIdentity CUSTOMER_TYPE =
            new ExecutableOperationalObjectTypeIdentity(
                    "customer",
                    "customer-context"
            );

    private PrototypeExecutableModelFactory() {
    }

    static ExecutableMerchantModel compose(
            String merchantIdentifier,
            String configurationIdentifier,
            long version,
            Set<String> capabilities
    ) {
        Set<String> selected = Set.copyOf(capabilities);

        // Discovery is permitted to propose Ordering before this generic
        // prototype factory knows how to assemble the complete Ordering
        // executable contribution. Do not promote an inert capability merely
        // because its discovery seed is valid.
        if (selected.contains("ordering")) {
            throw new PrototypeConfigurationGapException(
                    "Choice-driven Ordering executable assembly is not yet supported"
            );
        }

        List<ExecutableOperationalObjectDefinition> objects = new ArrayList<>();
        List<ExecutableRelationshipDefinition> relationships = new ArrayList<>();
        List<ExecutableOperationDefinition> operations = new ArrayList<>();

        boolean customerRequired = selected.contains("appointment")
                || selected.contains("booking");
        if (customerRequired && !selected.contains("customer")) {
            throw new IllegalArgumentException(
                    "Prototype Booking/Appointment composition requires Customer semantics"
            );
        }
        if (customerRequired) {
            objects.add(new ExecutableOperationalObjectDefinition(
                    "customer",
                    "customer-context",
                    Set.of(),
                    Optional.empty(),
                    Optional.empty()
            ));
        }

        if (selected.contains("appointment")) {
            if (!selected.contains("scheduling")) {
                throw new IllegalArgumentException(
                        "Prototype Appointment composition requires Scheduling semantics"
                );
            }
            addAppointment(objects, relationships, operations);
        }

        if (selected.contains("booking")) {
            addBooking(objects, relationships, operations);
        }

        return new ExecutableMerchantModel(
                merchantIdentifier,
                configurationIdentifier,
                version,
                PrototypeMerchantRuntime.SEMANTIC_REGISTRY,
                selected,
                objects,
                List.of(),
                relationships,
                operations,
                List.of()
        );
    }

    private static void addAppointment(
            List<ExecutableOperationalObjectDefinition> objects,
            List<ExecutableRelationshipDefinition> relationships,
            List<ExecutableOperationDefinition> operations
    ) {
        ExecutableOperationalObjectTypeIdentity appointmentType =
                new ExecutableOperationalObjectTypeIdentity(
                        "appointment",
                        "appointment"
                );
        objects.add(new ExecutableOperationalObjectDefinition(
                "appointment",
                "appointment",
                Set.of("confirmed"),
                Optional.of("confirmed"),
                Optional.empty()
        ));
        relationships.add(customerRelationship(
                "appointment.customer-context",
                appointmentType
        ));
        List<ExecutableOperationEffect> effects = List.of(
                new ExecutableObjectCreationEffect(appointmentType, "confirmed"),
                new ExecutableRelationshipEstablishmentEffect(
                        "appointment.customer-context"
                ),
                new ExecutableAllocationClaimEffect("appointment.capacity")
        );
        operations.add(new ExecutableOperationDefinition(
                "appointment.confirm",
                effects,
                Set.of("appointment.confirmed"),
                "appointment.confirm"
        ));
    }

    private static void addBooking(
            List<ExecutableOperationalObjectDefinition> objects,
            List<ExecutableRelationshipDefinition> relationships,
            List<ExecutableOperationDefinition> operations
    ) {
        ExecutableOperationalObjectTypeIdentity bookingType =
                new ExecutableOperationalObjectTypeIdentity(
                        "booking",
                        "booking"
                );
        objects.add(new ExecutableOperationalObjectDefinition(
                "booking",
                "booking",
                Set.of("confirmed"),
                Optional.of("confirmed"),
                Optional.empty()
        ));
        relationships.add(customerRelationship(
                "booking.customer-context",
                bookingType
        ));
        List<ExecutableOperationEffect> effects = List.of(
                new ExecutableObjectCreationEffect(bookingType, "confirmed"),
                new ExecutableRelationshipEstablishmentEffect(
                        "booking.customer-context"
                ),
                new ExecutableAllocationClaimEffect("booking.capacity")
        );
        operations.add(new ExecutableOperationDefinition(
                "booking.confirm",
                effects,
                Set.of("booking.confirmed"),
                "booking.confirm"
        ));
    }

    private static ExecutableRelationshipDefinition customerRelationship(
            String identifier,
            ExecutableOperationalObjectTypeIdentity source
    ) {
        return new ExecutableRelationshipDefinition(
                identifier,
                "CUSTOMER",
                source,
                CUSTOMER_TYPE,
                RelationshipCardinality.REQUIRED_ONE,
                RelationshipScopeConstraint.SAME_MERCHANT
        );
    }
}
