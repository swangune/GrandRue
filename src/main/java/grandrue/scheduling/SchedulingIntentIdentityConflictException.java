package grandrue.scheduling;

public final class SchedulingIntentIdentityConflictException
        extends RuntimeException {

    public SchedulingIntentIdentityConflictException(String identifier) {
        super("Scheduling intent identifier already represents another fact: "
                + identifier);
    }
}
