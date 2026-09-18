package grandrue.merchantaccount;

import grandrue.application.MerchantScope;
import grandrue.application.TrustedPlatformHumanPrincipal;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MerchantAccountEstablisherTest {

    @Test
    void rejects_unauthorised_principal_before_bootstrap_store_is_touched() {
        CountingBootstrapStore store = new CountingBootstrapStore();
        MerchantAccountEstablisher establisher = new MerchantAccountEstablisher(
                principal -> false,
                store
        );

        MerchantAccountEstablisher.Result result = establisher.establish(
                "request-1",
                new TrustedPlatformHumanPrincipal("identity-1")
        );

        assertEquals(MerchantAccountEstablisher.Status.AUTHORISATION_REJECTION, result.status());
        assertNull(result.merchantAccount());
        assertEquals(0, store.calls);
    }

    @Test
    void passes_the_authenticated_initiating_identity_as_initial_controller() {
        CountingBootstrapStore store = new CountingBootstrapStore();
        MerchantAccountEstablisher establisher = new MerchantAccountEstablisher(
                principal -> true,
                store
        );

        MerchantAccountEstablisher.Result result = establisher.establish(
                "request-1",
                new TrustedPlatformHumanPrincipal("identity-1")
        );

        assertEquals(MerchantAccountEstablisher.Status.SUCCESS, result.status());
        assertEquals("identity-1", store.lastInitialController.identityIdentifier());
        assertEquals("merchant-1", result.merchantAccount().merchantIdentifier());
    }

    @Test
    void replay_of_same_logical_request_returns_original_bootstrap() {
        CountingBootstrapStore store = new CountingBootstrapStore();
        MerchantAccountEstablisher establisher = new MerchantAccountEstablisher(
                principal -> true,
                store
        );
        TrustedPlatformHumanPrincipal principal = new TrustedPlatformHumanPrincipal("identity-1");

        MerchantAccountEstablisher.Result first = establisher.establish("request-1", principal);
        MerchantAccountEstablisher.Result replay = establisher.establish("request-1", principal);

        assertEquals(MerchantAccountEstablisher.Status.SUCCESS, first.status());
        assertEquals(MerchantAccountEstablisher.Status.ALREADY_ESTABLISHED, replay.status());
        assertEquals(first.merchantAccount(), replay.merchantAccount());
    }

    @Test
    void invalid_request_identity_is_rejected_before_authorisation_or_commit() {
        CountingBootstrapStore store = new CountingBootstrapStore();
        MerchantAccountEstablisher establisher = new MerchantAccountEstablisher(
                principal -> true,
                store
        );

        MerchantAccountEstablisher.Result result = establisher.establish(
                " ",
                new TrustedPlatformHumanPrincipal("identity-1")
        );

        assertEquals(MerchantAccountEstablisher.Status.VALIDATION_REJECTION, result.status());
        assertEquals(0, store.calls);
    }

    private static final class CountingBootstrapStore implements MerchantAccountBootstrapStore {
        private final Map<String, MerchantAccount> established = new HashMap<>();
        private int calls;
        private TrustedPlatformHumanPrincipal lastInitialController;

        @Override
        public BootstrapOutcome establishIfAbsent(
                String logicalEstablishmentRequestIdentity,
                TrustedPlatformHumanPrincipal initialController) {
            calls++;
            lastInitialController = initialController;

            MerchantAccount existing = established.get(logicalEstablishmentRequestIdentity);
            if (existing != null) {
                return new BootstrapOutcome(existing, true);
            }

            MerchantAccount created = new MerchantAccount(new MerchantScope("merchant-" + calls));
            established.put(logicalEstablishmentRequestIdentity, created);
            return new BootstrapOutcome(created, false);
        }
    }
}
