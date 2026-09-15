package mainstreet.audit;

import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuditRecordTest {

    private static final Instant T0 = Instant.parse("2026-08-24T15:00:00Z");

    @Test
    void merchant_scope_is_required_only_for_merchant_execution_context() {
        AuditRecord merchant = merchantRecord("audit-1");
        assertEquals(AuditExecutionScope.MERCHANT, merchant.executionScope());
        assertEquals("merchant-a", merchant.merchantScope().orElseThrow().merchantIdentifier());

        AuditRecord platform = new AuditRecord(
                "audit-platform",
                T0,
                "platform-operator-1",
                AuditExecutionScope.PLATFORM,
                Optional.empty(),
                AuditActionClass.ADMINISTRATIVE_ACTION,
                "session.revoke",
                Optional.of("SESSION"),
                Optional.of("session-1"),
                "COMPLETED",
                Optional.empty(),
                "correlation-2",
                Optional.empty(),
                Optional.of("support-console"),
                Optional.empty()
        );
        assertTrue(platform.merchantScope().isEmpty());
    }

    @Test
    void platform_scope_must_not_invent_merchant_affinity() {
        assertThrows(IllegalArgumentException.class, () -> new AuditRecord(
                "audit-1",
                T0,
                "principal-1",
                AuditExecutionScope.PLATFORM,
                Optional.of(new MerchantScope("merchant-a")),
                AuditActionClass.AUTHORISATION_SECURITY,
                "operation.execute",
                Optional.empty(),
                Optional.empty(),
                "REJECTED",
                Optional.of("AUTHORITY_MISSING"),
                "correlation-1",
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        ));
    }

    @Test
    void subject_type_and_reference_are_one_bounded_pair() {
        assertThrows(IllegalArgumentException.class, () -> new AuditRecord(
                "audit-1",
                T0,
                "principal-1",
                AuditExecutionScope.MERCHANT,
                Optional.of(new MerchantScope("merchant-a")),
                AuditActionClass.DATA_ACCESS,
                "customer.export",
                Optional.of("CUSTOMER_CONTEXT"),
                Optional.empty(),
                "REJECTED",
                Optional.empty(),
                "correlation-1",
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        ));
    }

    @Test
    void audit_record_contains_references_not_business_payloads() {
        AuditRecord record = merchantRecord("audit-1");
        assertEquals("booking.confirm", record.actionIdentifier());
        assertEquals("BOOKING", record.subjectType().orElseThrow());
        assertEquals("booking-1", record.subjectReference().orElseThrow());
        assertEquals(AuditActionClass.EXECUTION_ACCEPTED, record.actionClass());
    }

    private static AuditRecord merchantRecord(String auditIdentity) {
        return new AuditRecord(
                auditIdentity,
                T0,
                "identity-1",
                AuditExecutionScope.MERCHANT,
                Optional.of(new MerchantScope("merchant-a")),
                AuditActionClass.EXECUTION_ACCEPTED,
                "booking.confirm",
                Optional.of("BOOKING"),
                Optional.of("booking-1"),
                "COMPLETED",
                Optional.empty(),
                "correlation-1",
                Optional.of("command-1"),
                Optional.of("merchant-dashboard"),
                Optional.empty()
        );
    }
}
