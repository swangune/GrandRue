package grandrue.observability;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OperationalObservabilityModelTest {

    private static final Instant T0 = Instant.parse("2026-08-24T16:00:00Z");

    @Test
    void ordinary_exporter_failure_is_isolated_from_business_execution() {
        OperationalTelemetrySink failing = new OperationalTelemetrySink() {
            @Override public void record(DiagnosticLogObservation observation) { throw new IllegalStateException("down"); }
            @Override public void record(OperationalMetricObservation observation) { throw new IllegalStateException("down"); }
            @Override public void record(TraceObservation observation) { throw new IllegalStateException("down"); }
            @Override public void record(HealthObservation observation) { throw new IllegalStateException("down"); }
        };
        BestEffortOperationalTelemetry telemetry = new BestEffortOperationalTelemetry(failing);
        AtomicInteger businessCommits = new AtomicInteger();

        assertDoesNotThrow(() -> {
            businessCommits.incrementAndGet();
            telemetry.record(new DiagnosticLogObservation(
                    T0,
                    DiagnosticSeverity.INFO,
                    "booking",
                    "booking.confirmed",
                    Optional.of("correlation-1"),
                    Optional.of("booking-1")
            ));
        });
        assertEquals(1, businessCommits.get());
    }

    @Test
    void trace_identity_and_business_correlation_remain_distinct_fields() {
        TraceObservation trace = new TraceObservation(
                "trace-1",
                "span-1",
                Optional.empty(),
                "booking",
                "booking.confirm",
                T0,
                T0.plusMillis(12),
                "SUCCESS",
                Optional.of("booking-process-1")
        );

        assertEquals("trace-1", trace.traceIdentifier());
        assertEquals("booking-process-1", trace.businessCorrelationReference().orElseThrow());
    }

    @Test
    void health_is_explicitly_subject_and_dimension_qualified() {
        HealthObservation payment = new HealthObservation(
                "provider:payment",
                OperationalHealthDimension.READINESS,
                OperationalHealthState.DEGRADED,
                T0,
                Optional.of("elevated-timeout-rate")
        );
        HealthObservation storefront = new HealthObservation(
                "surface:public-storefront",
                OperationalHealthDimension.READINESS,
                OperationalHealthState.READY,
                T0,
                Optional.empty()
        );

        assertEquals(OperationalHealthState.DEGRADED, payment.state());
        assertEquals(OperationalHealthState.READY, storefront.state());
    }

    @Test
    void metric_requires_finite_value_and_bounded_explicit_scope() {
        assertThrows(IllegalArgumentException.class, () -> new OperationalMetricObservation(
                T0,
                "background",
                "queue.age",
                Double.NaN,
                "seconds",
                "queue:notifications"
        ));

        OperationalMetricObservation metric = new OperationalMetricObservation(
                T0,
                "background",
                "queue.age",
                14.0,
                "seconds",
                "queue:notifications"
        );
        assertTrue(Double.isFinite(metric.value()));
    }

    @Test
    void trace_end_cannot_precede_start() {
        assertThrows(IllegalArgumentException.class, () -> new TraceObservation(
                "trace-1",
                "span-1",
                Optional.empty(),
                "booking",
                "booking.confirm",
                T0,
                T0.minusMillis(1),
                "FAILED",
                Optional.empty()
        ));
    }
}
