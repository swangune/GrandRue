package grandrue.notification;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Intersects contract-permitted channels with eligibility already established
 * by endpoint/exposure/consent/policy authorities, then applies any permitted
 * preference narrowing. It never manufactures communication authority.
 */
public final class NotificationChannelEligibilityResolver {

    public Set<NotificationChannel> eligibleChannels(
            NotificationContract contract,
            Set<NotificationChannel> externallyPermittedChannels,
            NotificationPreference preference
    ) {
        Objects.requireNonNull(contract, "contract");
        Set<NotificationChannel> external = Set.copyOf(
                Objects.requireNonNull(
                        externallyPermittedChannels,
                        "externallyPermittedChannels"
                )
        );
        Objects.requireNonNull(preference, "preference");

        LinkedHashSet<NotificationChannel> eligible = new LinkedHashSet<>(
                contract.permittedChannels()
        );
        eligible.retainAll(external);

        if (!preference.isNone()) {
            if (!preference.notificationTypeIdentifier().equals(
                    contract.notificationTypeIdentifier()
            )) {
                throw new IllegalArgumentException(
                        "Notification Preference belongs to another notification type"
                );
            }
            if (contract.preferenceMaySuppress()) {
                eligible.removeAll(preference.disabledChannels());
            }
        }
        return Set.copyOf(eligible);
    }
}
