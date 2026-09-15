package mainstreet.semantic.capability;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MerchantCapabilityCompositionTest {

    @Test
    void merchant_can_enable_multiple_capabilities() {

        Capability booking =
                new Capability("booking");

        Capability payment =
                new Capability("payment");


        MerchantCapabilityConfiguration merchant =
                new MerchantCapabilityConfiguration();


        merchant.enable(booking);
        merchant.enable(payment);


        assertEquals(
                2,
                merchant.capabilities().size()
        );
    }
}