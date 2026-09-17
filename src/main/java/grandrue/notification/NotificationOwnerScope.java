package grandrue.notification;

/** Scope owning one notification responsibility; merchant scope remains explicit. */
public record NotificationOwnerScope(
        String scopeKindIdentifier,
        String scopeIdentifier
) {
    public NotificationOwnerScope {
        require(scopeKindIdentifier, "Notification owner scope kind");
        require(scopeIdentifier, "Notification owner scope identity");
    }

    public static NotificationOwnerScope merchant(String merchantIdentifier) {
        return new NotificationOwnerScope("MERCHANT", merchantIdentifier);
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
