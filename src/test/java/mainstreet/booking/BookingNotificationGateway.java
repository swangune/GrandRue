package mainstreet.booking;

import mainstreet.semantic.DomainEvent;

public interface BookingNotificationGateway {

    void deliver(DomainEvent event);
}
