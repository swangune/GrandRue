package mainstreet.observability;

import java.util.Objects;

/**
 * Ordinary observability adapter whose exporter failure is deliberately isolated
 * from business execution. Required durable audit evidence belongs to Audit,
 * not this boundary.
 */
public final class BestEffortOperationalTelemetry implements OperationalTelemetrySink {

    private final OperationalTelemetrySink delegate;

    public BestEffortOperationalTelemetry(OperationalTelemetrySink delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
    }

    @Override
    public void record(DiagnosticLogObservation observation) {
        attempt(() -> delegate.record(Objects.requireNonNull(observation, "observation")));
    }

    @Override
    public void record(OperationalMetricObservation observation) {
        attempt(() -> delegate.record(Objects.requireNonNull(observation, "observation")));
    }

    @Override
    public void record(TraceObservation observation) {
        attempt(() -> delegate.record(Objects.requireNonNull(observation, "observation")));
    }

    @Override
    public void record(HealthObservation observation) {
        attempt(() -> delegate.record(Objects.requireNonNull(observation, "observation")));
    }

    private static void attempt(Runnable export) {
        try {
            export.run();
        } catch (RuntimeException ignored) {
            // MS-PROT-068: ordinary telemetry loss reduces visibility, not business truth.
        }
    }
}
