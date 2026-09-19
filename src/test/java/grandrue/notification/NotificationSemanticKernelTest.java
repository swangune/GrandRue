package grandrue.notification;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotificationSemanticKernelTest {

    @Test
    void contract_is_registered_by_semantic_type_not_provider() {
        NotificationContract contract = contract();
        NotificationContractRegistry registry = new NotificationContractRegistry();
        registry.register(contract);

        assertEquals(contract, registry.require("appointment.confirmed"));
        assertThrows(IllegalArgumentException.class, () -> registry.register(contract));
    }

    @Test
    void recipient_is_semantic_relationship_not_delivery_endpoint() {
        NotificationRecipient recipient = new NotificationRecipient(
                "recipient-1",
                "CustomerContext",
                "customer-context-42",
                NotificationRecipientResolutionBasis.COMMITMENT_BOUND
        );

        assertEquals("customer-context-42", recipient.semanticReference());
        assertFalse(recipient.semanticReference().contains("@"));
    }

    @Test
    void current_relationship_recipient_requires_current_revalidation() {
        NotificationRecipient recipient = new NotificationRecipient(
                "recipient-2",
                "MerchantMembership",
                "membership-17",
                NotificationRecipientResolutionBasis.CURRENT_RELATIONSHIP
        );
        assertTrue(recipient.requiresCurrentRevalidation());
    }

    @Test
    void preference_can_narrow_but_never_add_a_channel() {
        NotificationContract contract = contract();
        NotificationPreference preference = new NotificationPreference(
                "preference-1",
                "recipient-1",
                "appointment.confirmed",
                Set.of(NotificationChannel.SMS),
                List.of(NotificationChannel.EMAIL),
                Instant.parse("2026-08-24T06:30:00Z")
        );

        Set<NotificationChannel> externallyPermitted = Set.of(
                NotificationChannel.EMAIL,
                NotificationChannel.SMS
        );
        Set<NotificationChannel> eligible = new NotificationChannelEligibilityResolver()
                .eligibleChannels(contract, externallyPermitted, preference);

        assertEquals(Set.of(NotificationChannel.EMAIL), eligible);
        assertFalse(eligible.contains(NotificationChannel.PUSH));
    }

    @Test
    void non_preference_controlled_contract_does_not_allow_preference_to_suppress() {
        NotificationContract security = new NotificationContract(
                "security.credential-changed",
                "security",
                "credential-change",
                NotificationRecipientResolutionBasis.CURRENT_RELATIONSHIP,
                Set.of(NotificationChannel.EMAIL, NotificationChannel.IN_APP),
                false,
                "security-notice-v1",
                "security-minimal-content-v1",
                "security-exposure-v1"
        );
        NotificationPreference preference = new NotificationPreference(
                "preference-1",
                "recipient-1",
                "security.credential-changed",
                Set.of(NotificationChannel.EMAIL),
                List.of(),
                Instant.parse("2026-08-24T06:30:00Z")
        );

        Set<NotificationChannel> eligible = new NotificationChannelEligibilityResolver()
                .eligibleChannels(
                        security,
                        Set.of(NotificationChannel.EMAIL, NotificationChannel.IN_APP),
                        preference
                );

        assertEquals(Set.of(NotificationChannel.EMAIL, NotificationChannel.IN_APP), eligible);
    }

    @Test
    void channel_candidates_outside_contract_are_never_manufactured() {
        Set<NotificationChannel> eligible = new NotificationChannelEligibilityResolver()
                .eligibleChannels(
                        contract(),
                        Set.of(NotificationChannel.PUSH),
                        NotificationPreference.none()
                );
        assertTrue(eligible.isEmpty());
    }

    private static NotificationContract contract() {
        return new NotificationContract(
                "appointment.confirmed",
                "scheduling",
                "appointment-confirmation-requirement",
                NotificationRecipientResolutionBasis.COMMITMENT_BOUND,
                Set.of(NotificationChannel.EMAIL, NotificationChannel.SMS),
                true,
                "durable-postcommit-v1",
                "appointment-confirmation-content-v1",
                "customer-transactional-exposure-v1"
        );
    }
}
