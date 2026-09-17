package grandrue.notification;

/**
 * Revalidates mutable applicability, recipient relationship, Exposure and
 * channel authority immediately before externalisation. Implementations may
 * compose owning capability, privacy, workforce/device and endpoint authority.
 */
@FunctionalInterface
public interface NotificationDispatchCurrentEligibility {
    boolean mayExternalise(NotificationDispatch dispatch);
}
