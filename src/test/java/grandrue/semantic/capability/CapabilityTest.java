package grandrue.semantic.capability;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CapabilityTest {

    @Test
    void capability_defines_identity() {

        Capability capability =
                new Capability("booking");

        assertEquals(
                "booking",
                capability.identifier());
    }
}