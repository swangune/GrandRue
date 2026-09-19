package grandrue.booking;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * C4E specification: Booking owns technical event-publication responsibility,
 * while consumer completion is represented only by registered Event Reactions.
 *
 * <p>Authority: MS-PROT-026 v1.1,
 * designs/MS-PROT-026 v1.1 — Production Domain Event Publication, Reaction & Consumption Contract Amendment.md,
 * §11 — Event Publication Is Not Reaction Completion; §12 — Publication Responsibility;
 * §17 — Per-Reaction Acknowledgement.</p>
 */
class BookingOutboxPublicationContractTest {

    @Test
    void booking_outbox_exposes_publication_evidence_not_global_consumer_acknowledgement() {
        Set<String> methods = Arrays.stream(BookingOutbox.class.getDeclaredMethods())
                .map(method -> method.getName())
                .collect(Collectors.toSet());

        assertTrue(methods.contains("recordPublished"));
        assertFalse(methods.contains("acknowledgeDelivery"));
    }
}
