package grandrue.observability;

/** Replaceable technical telemetry boundary. Implementations own no business mutation authority. */
public interface OperationalTelemetrySink {

    void record(DiagnosticLogObservation observation);

    void record(OperationalMetricObservation observation);

    void record(TraceObservation observation);

    void record(HealthObservation observation);
}
