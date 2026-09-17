package grandrue.booking;

public final class CommandIdentityConflictException
        extends IllegalArgumentException {

    public CommandIdentityConflictException(
            String commandIdentifier
    ) {
        super(
                "Command identifier already used for different booking intent: "
                        + commandIdentifier
        );
    }
}
