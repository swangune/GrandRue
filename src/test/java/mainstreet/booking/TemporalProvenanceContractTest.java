package mainstreet.booking;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class TemporalProvenanceContractTest {

    @Test
    void caller_cannot_supply_platform_recorded_confirmation_time() {
        boolean exposesCallerSuppliedRecordedTime = Arrays.stream(ConfirmBookingCommand.class.getRecordComponents())
                .anyMatch(component -> component.getName().equals("confirmedAt"));

        assertFalse(
                exposesCallerSuppliedRecordedTime,
                "MS-PROT-025 forbids client input from substituting for platform-observed execution/audit time");
    }
}
