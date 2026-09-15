package mainstreet.semantic;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DomainEventTest {

    @Test
    void domain_event_records_meaningful_fact_and_causation() {

        Instant occurredAt = Instant.parse(
                "2026-08-20T10:15:30Z"
        );

        DomainEvent event = new DomainEvent(
                "event-001",
                "appointment_confirmed",
                "appointment-123",
                "command-001",
                occurredAt
        );

        assertEquals("event-001", event.identifier());
        assertEquals(
                "appointment_confirmed",
                event.factIdentifier()
        );
        assertEquals(
                "appointment-123",
                event.subjectIdentifier()
        );
        assertEquals(
                "command-001",
                event.causationIdentifier()
        );
        assertEquals(occurredAt, event.occurredAt());
    }

    @Test
    void one_command_can_cause_multiple_distinct_domain_facts() {

        Instant occurredAt = Instant.parse(
                "2026-08-20T10:15:30Z"
        );

        DomainEvent appointmentConfirmed = new DomainEvent(
                "event-001",
                "appointment_confirmed",
                "appointment-123",
                "command-001",
                occurredAt
        );

        DomainEvent capacityAllocated = new DomainEvent(
                "event-002",
                "capacity_allocated",
                "allocation-456",
                "command-001",
                occurredAt
        );

        assertNotEquals(
                appointmentConfirmed.identifier(),
                capacityAllocated.identifier()
        );

        assertEquals(
                appointmentConfirmed.causationIdentifier(),
                capacityAllocated.causationIdentifier()
        );
    }
}
