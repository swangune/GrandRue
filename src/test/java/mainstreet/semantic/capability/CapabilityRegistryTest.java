package mainstreet.semantic.capability;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CapabilityRegistryTest {

    @Test
    void registry_can_register_capabilities() {

        Capability booking = new Capability("booking");

        CapabilityRegistry registry = new CapabilityRegistry();

        registry.register(booking);

        assertEquals(
                booking,
                registry.find("booking"));
    }

    @Test
    void registry_rejects_duplicate_capability_identifiers() {

        Capability bookingOne = new Capability("booking");

        Capability bookingTwo = new Capability("booking");

        CapabilityRegistry registry = new CapabilityRegistry();

        registry.register(bookingOne);

        assertThrows(
                IllegalArgumentException.class,
                () -> registry.register(bookingTwo));
    }
}