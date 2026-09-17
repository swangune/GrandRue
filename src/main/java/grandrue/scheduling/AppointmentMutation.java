package grandrue.scheduling;

/**
 * Appointment-owned mutation port for staging authoritative Appointment state.
 * A wider local transaction may implement this port to preserve an accepted
 * atomic invariant without acquiring semantic ownership of Appointment.
 */
@FunctionalInterface
public interface AppointmentMutation {

    void recordAppointment(Appointment appointment);
}
