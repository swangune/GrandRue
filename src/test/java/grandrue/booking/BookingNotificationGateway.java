package grandrue.booking;

import grandrue.semantic.DomainEvent;

public interface BookingNotificationGateway {

    void deliver(DomainEvent event);
}
