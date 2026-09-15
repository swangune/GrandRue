package mainstreet.notification;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** Main Street registry preventing duplicate notification semantic identities. */
public final class NotificationContractRegistry {
    private final Map<String, NotificationContract> contracts = new LinkedHashMap<>();

    public void register(NotificationContract contract) {
        Objects.requireNonNull(contract, "contract");
        NotificationContract existing = contracts.putIfAbsent(
                contract.notificationTypeIdentifier(),
                contract
        );
        if (existing != null) {
            throw new IllegalArgumentException(
                    "Notification type already registered: "
                            + contract.notificationTypeIdentifier()
            );
        }
    }

    public NotificationContract require(String notificationTypeIdentifier) {
        NotificationContract contract = contracts.get(notificationTypeIdentifier);
        if (contract == null) {
            throw new IllegalArgumentException(
                    "Unknown Notification Contract: " + notificationTypeIdentifier
            );
        }
        return contract;
    }
}
