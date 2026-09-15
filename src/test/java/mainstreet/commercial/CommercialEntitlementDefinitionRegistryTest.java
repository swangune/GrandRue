package mainstreet.commercial;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommercialEntitlementDefinitionRegistryTest {

    @Test
    void registers_and_resolves_definition_by_stable_entitlement_identity() {
        CommercialEntitlementDefinitionRegistry registry =
                new CommercialEntitlementDefinitionRegistry();
        CommercialEntitlementDefinition definition = definition(
                "booking.create",
                "booking.create-operation"
        );

        registry.register(definition);

        assertEquals(
                definition,
                registry.find(definition.entitlementIdentity()).orElseThrow()
        );
    }

    @Test
    void rejects_duplicate_entitlement_identity() {
        CommercialEntitlementDefinitionRegistry registry =
                new CommercialEntitlementDefinitionRegistry();
        registry.register(definition("booking.create", "booking.create-operation"));

        assertThrows(
                IllegalArgumentException.class,
                () -> registry.register(new CommercialEntitlementDefinition(
                        new CommercialEntitlementIdentity("booking.create"),
                        CommercialEntitlementTargetKind.OPERATION_ACCESS,
                        "booking.other-operation",
                        "create new booking"
                ))
        );
    }

    @Test
    void target_lookup_does_not_collapse_multiple_definitions_into_one_rule() {
        CommercialEntitlementDefinitionRegistry registry =
                new CommercialEntitlementDefinitionRegistry();
        registry.register(definition("booking.create.standard", "booking.create-operation"));
        registry.register(definition("booking.create.promo", "booking.create-operation"));

        var matches = registry.definitionsTargeting(
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                "booking.create-operation"
        );

        assertEquals(2, matches.size());
        assertTrue(matches.stream().anyMatch(definition ->
                definition.entitlementIdentity().identifier().equals(
                        "booking.create.standard"
                )));
        assertTrue(matches.stream().anyMatch(definition ->
                definition.entitlementIdentity().identifier().equals(
                        "booking.create.promo"
                )));
    }

    @Test
    void definition_rejects_missing_target_or_access_purpose() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new CommercialEntitlementDefinition(
                        new CommercialEntitlementIdentity("booking.create"),
                        CommercialEntitlementTargetKind.OPERATION_ACCESS,
                        " ",
                        "create new booking"
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new CommercialEntitlementDefinition(
                        new CommercialEntitlementIdentity("booking.create"),
                        CommercialEntitlementTargetKind.OPERATION_ACCESS,
                        "booking.create-operation",
                        " "
                )
        );
    }

    private static CommercialEntitlementDefinition definition(
            String entitlementIdentity,
            String targetReference
    ) {
        return new CommercialEntitlementDefinition(
                new CommercialEntitlementIdentity(entitlementIdentity),
                CommercialEntitlementTargetKind.OPERATION_ACCESS,
                targetReference,
                "create new booking"
        );
    }
}
