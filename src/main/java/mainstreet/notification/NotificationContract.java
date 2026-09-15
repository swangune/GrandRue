package mainstreet.notification;

import java.util.Objects;
import java.util.Set;

/**
 * Registered communication contract contributed by the semantic owner. It
 * references source, projection and exposure contracts without owning their
 * underlying business/privacy semantics.
 */
public record NotificationContract(
        String notificationTypeIdentifier,
        String semanticOwnerIdentifier,
        String sourceRequirementIdentifier,
        NotificationRecipientResolutionBasis recipientResolutionBasis,
        Set<NotificationChannel> permittedChannels,
        boolean preferenceMaySuppress,
        String requiredDeliveryGuaranteeIdentifier,
        String contentProjectionContractIdentifier,
        String exposureConstraintIdentifier
) {
    public NotificationContract {
        require(notificationTypeIdentifier, "Notification type");
        require(semanticOwnerIdentifier, "Semantic owner");
        require(sourceRequirementIdentifier, "Source requirement");
        Objects.requireNonNull(recipientResolutionBasis, "recipientResolutionBasis");
        permittedChannels = Set.copyOf(
                Objects.requireNonNull(permittedChannels, "permittedChannels")
        );
        if (permittedChannels.isEmpty()) {
            throw new IllegalArgumentException(
                    "Notification Contract must permit at least one channel"
            );
        }
        require(requiredDeliveryGuaranteeIdentifier, "Delivery guarantee");
        require(contentProjectionContractIdentifier, "Content projection contract");
        require(exposureConstraintIdentifier, "Exposure constraint");
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
