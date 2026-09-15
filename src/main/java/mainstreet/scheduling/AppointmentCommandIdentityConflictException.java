package mainstreet.scheduling;

public final class AppointmentCommandIdentityConflictException
        extends IllegalArgumentException {

    public AppointmentCommandIdentityConflictException(
            String commandIdentifier
    ) {
        super(
                "Command identifier already used for different appointment intent: "
                        + commandIdentifier
        );
    }
}