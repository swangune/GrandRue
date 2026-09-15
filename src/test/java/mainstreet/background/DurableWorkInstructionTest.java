package mainstreet.background;

import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DurableWorkInstructionTest {

    private static final Instant T0 = Instant.parse("2026-08-24T15:00:00Z");

    @Test
    void merchant_work_preserves_scope_without_making_it_authority() {
        DurableWorkInstruction instruction = merchantWork("work-1", T0);
        assertEquals(BackgroundExecutionScope.MERCHANT, instruction.executionScope());
        assertEquals("merchant-a", instruction.merchantScope().orElseThrow().merchantIdentifier());
        assertEquals("booking.reminder.evaluate", instruction.responsibilityIdentifier());
    }

    @Test
    void platform_work_must_not_fabricate_merchant_scope() {
        DurableWorkInstruction instruction = new DurableWorkInstruction(
                "work-platform",
                "platform-security",
                BackgroundExecutionScope.PLATFORM,
                Optional.empty(),
                T0,
                "security.cleanup.evaluate",
                "correlation-1",
                Optional.empty(),
                Optional.empty(),
                "security-cleanup-v1",
                OverdueHandling.RE_EVALUATE_CURRENT_STATE,
                T0.minusSeconds(10)
        );
        assertTrue(instruction.merchantScope().isEmpty());

        assertThrows(IllegalArgumentException.class, () -> new DurableWorkInstruction(
                "work-invalid",
                "platform-security",
                BackgroundExecutionScope.PLATFORM,
                Optional.of(new MerchantScope("merchant-a")),
                T0,
                "security.cleanup.evaluate",
                "correlation-1",
                Optional.empty(),
                Optional.empty(),
                "security-cleanup-v1",
                OverdueHandling.RE_EVALUATE_CURRENT_STATE,
                T0.minusSeconds(10)
        ));
    }

    @Test
    void work_may_already_be_overdue_when_durably_created() {
        DurableWorkInstruction overdue = merchantWork("work-overdue", T0.minusSeconds(60));
        assertTrue(overdue.dueAt().isBefore(overdue.createdAt()));
        assertEquals(OverdueHandling.RE_EVALUATE_CURRENT_STATE, overdue.overdueHandling());
    }

    private static DurableWorkInstruction merchantWork(String id, Instant dueAt) {
        return new DurableWorkInstruction(
                id,
                "booking",
                BackgroundExecutionScope.MERCHANT,
                Optional.of(new MerchantScope("merchant-a")),
                dueAt,
                "booking.reminder.evaluate",
                "correlation-1",
                Optional.of("booking-1"),
                Optional.of("config-revision-1"),
                "booking-reminder-v1",
                OverdueHandling.RE_EVALUATE_CURRENT_STATE,
                T0
        );
    }
}
