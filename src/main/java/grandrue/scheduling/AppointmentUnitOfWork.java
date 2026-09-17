package grandrue.scheduling;

import java.util.function.Function;

public interface AppointmentUnitOfWork {

    AppointmentConfirmation execute(
            ConfirmAppointmentCommand command,
            Function<AppointmentTransaction, AppointmentConfirmation> work
    );
}
