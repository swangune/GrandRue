package grandrue.notification;

/** Provider adapter boundary for one selected Notification Dispatch channel. */
@FunctionalInterface
public interface NotificationChannelDeliveryPort {
    NotificationDeliveryProviderResult deliver(
            NotificationDispatch dispatch,
            String providerIdempotencyReference
    );
}
