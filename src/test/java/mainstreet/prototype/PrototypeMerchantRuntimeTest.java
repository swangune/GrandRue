package mainstreet.prototype;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrototypeMerchantRuntimeTest {

    @Test
    void standard_runtime_activates_reference_merchant_configurations() {
        PrototypeMerchantRuntime runtime = PrototypeMerchantRuntime.standard();

        PrototypeMerchantView retailer = runtime.merchant("prototype-retailer");
        assertEquals(
                Set.of("ordering", "inventory", "payment", "order-fulfilment"),
                retailer.capabilityIdentifiers()
        );
        assertEquals(Set.of("ordering.commit"), retailer.operationIdentifiers());

        PrototypeMerchantView consultant = runtime.merchant("prototype-consultant");
        assertEquals(
                Set.of("appointment", "scheduling", "customer", "payment"),
                consultant.capabilityIdentifiers()
        );
        assertEquals(Set.of("appointment.confirm"), consultant.operationIdentifiers());

        PrototypeMerchantView motel = runtime.merchant("prototype-motel");
        assertEquals(
                Set.of("booking", "customer", "payment"),
                motel.capabilityIdentifiers()
        );
        assertEquals(Set.of("booking.confirm"), motel.operationIdentifiers());

        PrototypeMerchantView daycare = runtime.merchant("prototype-daycare");
        assertEquals(
                Set.of("booking", "customer", "payment"),
                daycare.capabilityIdentifiers()
        );
        assertEquals(Set.of("booking.confirm"), daycare.operationIdentifiers());

        PrototypeMerchantView gardener = runtime.merchant("prototype-gardener");
        assertEquals(
                Set.of("appointment", "scheduling", "customer", "payment"),
                gardener.capabilityIdentifiers()
        );
        assertEquals(Set.of("appointment.confirm"), gardener.operationIdentifiers());

        PrototypeMerchantView publisher = runtime.merchant("prototype-publisher");
        assertEquals(
                Set.of("publication", "enquiry"),
                publisher.capabilityIdentifiers()
        );
        assertTrue(publisher.operationIdentifiers().isEmpty());
    }

    @Test
    void same_trade_can_have_materially_different_capability_graphs() {
        PrototypeMerchantRuntime runtime = PrototypeMerchantRuntime.standard();

        PrototypeMerchantView showcase =
                runtime.merchant("prototype-gardener-showcase");
        assertEquals(
                Set.of("publication", "enquiry"),
                showcase.capabilityIdentifiers()
        );
        assertTrue(showcase.operationIdentifiers().isEmpty());

        PrototypeMerchantView bookable =
                runtime.merchant("prototype-gardener-bookable");
        assertEquals(
                Set.of(
                        "publication",
                        "enquiry",
                        "appointment",
                        "scheduling",
                        "customer"
                ),
                bookable.capabilityIdentifiers()
        );
        assertEquals(
                Set.of("appointment.confirm"),
                bookable.operationIdentifiers()
        );
    }

    @Test
    void one_merchant_can_add_interaction_semantics_through_a_later_configuration_release() {
        PrototypeMerchantRuntime runtime = PrototypeMerchantRuntime.standard();
        String merchantIdentifier = "prototype-gardener-evolving";

        PrototypeMerchantView initial = runtime.merchant(merchantIdentifier);
        assertEquals("prototype-gardener-evolving-config-1",
                initial.configurationIdentifier());
        assertEquals("prototype-gardener-evolving-release-1",
                initial.releaseIdentifier());
        assertEquals(
                Set.of("publication", "enquiry"),
                initial.capabilityIdentifiers()
        );
        assertTrue(initial.operationIdentifiers().isEmpty());

        PrototypeMerchantView evolved = runtime.activateApprovedDiscoveryChoices(
                merchantIdentifier,
                "prototype-gardener-evolving-config-2",
                2,
                "prototype-gardener-evolving-release-2",
                Set.of(
                        PrototypeCustomerInteractionChoice.PUBLISH_INFORMATION,
                        PrototypeCustomerInteractionChoice.SEND_ENQUIRY,
                        PrototypeCustomerInteractionChoice.ARRANGE_APPOINTMENT
                )
        );

        assertEquals(merchantIdentifier, evolved.merchantIdentifier());
        assertEquals("prototype-gardener-evolving-config-2",
                evolved.configurationIdentifier());
        assertEquals("prototype-gardener-evolving-release-2",
                evolved.releaseIdentifier());
        assertEquals(
                Set.of(
                        "publication",
                        "enquiry",
                        "appointment",
                        "scheduling",
                        "customer"
                ),
                evolved.capabilityIdentifiers()
        );
        assertEquals(Set.of("appointment.confirm"), evolved.operationIdentifiers());

        var activation = runtime.activation()
                .committedActivation(
                        "prototype-gardener-evolving-release-2:activate"
                )
                .orElseThrow();
        assertEquals(
                "prototype-gardener-evolving-config-1",
                activation.replacedConfigurationRevisionIdentifier()
                        .orElseThrow()
        );
    }

    @Test
    void discovery_seed_is_not_activated_when_executable_assembly_is_not_yet_supported() {
        PrototypeMerchantRuntime runtime = PrototypeMerchantRuntime.standard();
        String merchantIdentifier = "prototype-gardener-evolving";

        assertThrows(
                PrototypeConfigurationGapException.class,
                () -> runtime.activateApprovedDiscoveryChoices(
                        merchantIdentifier,
                        "prototype-gardener-evolving-config-ordering",
                        2,
                        "prototype-gardener-evolving-release-ordering",
                        Set.of(PrototypeCustomerInteractionChoice.PLACE_ORDER)
                )
        );

        PrototypeMerchantView stillActive = runtime.merchant(merchantIdentifier);
        assertEquals(
                "prototype-gardener-evolving-config-1",
                stillActive.configurationIdentifier()
        );
        assertEquals(
                Set.of("publication", "enquiry"),
                stillActive.capabilityIdentifiers()
        );
    }

    @Test
    void reference_merchants_do_not_inherit_unselected_business_semantics() {
        PrototypeMerchantRuntime runtime = PrototypeMerchantRuntime.standard();

        assertFalse(runtime.merchant("prototype-consultant")
                .capabilityIdentifiers().contains("booking"));
        assertFalse(runtime.merchant("prototype-motel")
                .capabilityIdentifiers().contains("appointment"));
        assertFalse(runtime.merchant("prototype-daycare")
                .capabilityIdentifiers().contains("appointment"));
        assertFalse(runtime.merchant("prototype-gardener")
                .capabilityIdentifiers().contains("booking"));
        assertFalse(runtime.merchant("prototype-gardener-showcase")
                .capabilityIdentifiers().contains("appointment"));
        assertFalse(runtime.merchant("prototype-publisher")
                .capabilityIdentifiers().contains("inventory"));
        assertFalse(runtime.merchant("prototype-retailer")
                .capabilityIdentifiers().contains("appointment"));
    }

    @Test
    void every_reference_merchant_is_visible_through_the_real_activation_boundary() {
        PrototypeMerchantRuntime runtime = PrototypeMerchantRuntime.standard();

        for (String merchantIdentifier : Set.of(
                "prototype-retailer",
                "prototype-consultant",
                "prototype-motel",
                "prototype-daycare",
                "prototype-gardener",
                "prototype-gardener-showcase",
                "prototype-gardener-bookable",
                "prototype-gardener-evolving",
                "prototype-publisher"
        )) {
            assertTrue(runtime.activation().current(merchantIdentifier).isPresent());
            assertEquals(
                    merchantIdentifier,
                    runtime.activation().current(merchantIdentifier)
                            .orElseThrow()
                            .release()
                            .merchantIdentifier()
            );
        }
    }
}
