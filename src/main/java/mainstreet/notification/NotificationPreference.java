package mainstreet.notification;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Recipient/context preference that may narrow or rank channels only where the
 * governing Notification Contract permits preference control.
 */
public record NotificationPreference(
        String preferenceIdentity,
        String recipientReference,
        String notificationTypeIdentifier,
        Set<NotificationChannel> disabledChannels,
        List<NotificationChannel> preferredChannels,
        Instant effectiveFrom
) {
    private static final String NONE = "__NO_NOTIFICATION_PREFERENCE__";

    public NotificationPreference {
        require(preferenceIdentity, "Preference identity");
        require(recipientReference, "Preference recipient reference");
        require(notificationTypeIdentifier, "Preference notification type");
        disabledChannels = Set.copyOf(
                Objects.requireNonNull(disabledChannels, "disabledChannels")
        );
        preferredChannels = List.copyOf(
                Objects.requireNonNull(preferredChannels, "preferredChannels")
        );
        if (new HashSet<>(preferredChannels).size() != preferredChannels.size()) {
            throw new IllegalArgumentException("Preferred channels must not repeat");
        }
        for (NotificationChannel channel : preferredChannels) {
            if (disabledChannels.contains(channel)) {
                throw new IllegalArgumentException(
                        "A disabled channel cannot also be preferred"
                );
            }
        }
        Objects.requireNonNull(effectiveFrom, "effectiveFrom");
    }

    public static NotificationPreference none() {
        return new NotificationPreference(
                NONE,
                NONE,
                NONE,
                Set.of(),
                List.of(),
                Instant.MIN
        );
    }

    public boolean isNone() {
        return NONE.equals(preferenceIdentity);
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
