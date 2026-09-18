package grandrue.merchantaccount;

import grandrue.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MerchantAccountLifecycleModelTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-1");
    private static final Instant T0 = Instant.parse("2026-08-24T06:00:00Z");

    @Test
    void lifecycle_is_exactly_open_closing_closed() {
        assertEquals(3, MerchantAccountLifecycle.values().length);
        assertEquals(MerchantAccountLifecycle.OPEN, MerchantAccountLifecycle.valueOf("OPEN"));
        assertEquals(MerchantAccountLifecycle.CLOSING, MerchantAccountLifecycle.valueOf("CLOSING"));
        assertEquals(MerchantAccountLifecycle.CLOSED, MerchantAccountLifecycle.valueOf("CLOSED"));
    }

    @Test
    void controller_relationship_ended_state_is_terminal_value_not_account_lifecycle() {
        MerchantControllerRelationship relationship =
                new MerchantControllerRelationship(
                        "controller-rel-1",
                        MERCHANT,
                        "identity-1",
                        MerchantControllerRelationshipLifecycle.ACTIVE
                );
        assertEquals(MerchantControllerRelationshipLifecycle.ACTIVE, relationship.lifecycle());
    }

    @Test
    void suspension_release_is_all_or_none_and_preserves_source_provenance() {
        MerchantAccountSuspension active = new MerchantAccountSuspension(
                "suspension-1",
                MERCHANT,
                "security-authority",
                "account-control-compromise",
                T0,
                "security-principal-1",
                "security-authority",
                "security-case-1",
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );
        assertTrue(active.isEffective());

        MerchantAccountSuspension released = active.release(
                T0.plusSeconds(60),
                "security-principal-2",
                "release-evidence-1"
        );
        assertFalse(released.isEffective());
        assertEquals("security-authority", released.sourceAuthorityIdentifier());
        assertEquals("security-case-1", released.provenanceIdentifier());
    }

    @Test
    void suspension_cannot_be_released_before_establishment() {
        MerchantAccountSuspension active = suspension();
        assertThrows(
                IllegalArgumentException.class,
                () -> active.release(
                        T0.minusSeconds(1),
                        "principal",
                        "evidence"
                )
        );
    }

    @Test
    void transfer_command_requires_distinct_receiving_identity_and_evidence() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MerchantControllerTransferCommand(
                        "request-1",
                        MERCHANT,
                        "controller-rel-1",
                        "identity-1",
                        "identity-1",
                        "controller-rel-2",
                        "auth-assurance-1",
                        "acceptance-1",
                        T0
                )
        );
    }

    private static MerchantAccountSuspension suspension() {
        return new MerchantAccountSuspension(
                "suspension-1",
                MERCHANT,
                "security-authority",
                "account-control-compromise",
                T0,
                "security-principal-1",
                "security-authority",
                "security-case-1",
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );
    }
}
