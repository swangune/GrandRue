package grandrue.application;

import grandrue.commercial.CatalogueResolutionException;
import grandrue.commercial.OrdinaryMerchantAccountPathReadiness;
import grandrue.merchantaccount.MerchantAccount;
import grandrue.merchantaccount.MerchantAccountBootstrapStore;
import grandrue.merchantaccount.MerchantAccountEstablisher;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MS-PROT-056 v1.9 §11 composed with MS-PROT-071:
 * rollout readiness is checked outside the Merchant Account owner operation.
 */
class OrdinaryMerchantAccountEstablishmentApplicationServiceTest {

    @Test
    void unavailable_catalogue_blocks_delivery_before_owner_operation_is_touched() {
        var storeCalls = new AtomicInteger();
        var owner = owner(storeCalls);
        OrdinaryMerchantAccountPathReadiness readiness = () -> {
            throw new CatalogueResolutionException(
                    CatalogueResolutionException.Reason.NOT_ESTABLISHED,
                    "initial catalogue not published");
        };
        var application = new OrdinaryMerchantAccountEstablishmentApplicationService(
                readiness, owner);

        var failure = assertThrows(
                CatalogueResolutionException.class,
                () -> application.establish(
                        "request-1",
                        new TrustedPlatformHumanPrincipal("identity-1")));

        assertEquals(CatalogueResolutionException.Reason.NOT_ESTABLISHED, failure.reason());
        assertEquals(0, storeCalls.get());
    }

    @Test
    void ready_rollout_delegates_without_changing_merchant_account_result_semantics() {
        var readinessCalls = new AtomicInteger();
        var storeCalls = new AtomicInteger();
        var application = new OrdinaryMerchantAccountEstablishmentApplicationService(
                readinessCalls::incrementAndGet,
                owner(storeCalls));

        var result = application.establish(
                "request-1",
                new TrustedPlatformHumanPrincipal("identity-1"));

        assertEquals(MerchantAccountEstablisher.Status.SUCCESS, result.status());
        assertEquals("merchant-1", result.merchantAccount().merchantIdentifier());
        assertEquals(1, readinessCalls.get());
        assertEquals(1, storeCalls.get());
    }

    private static MerchantAccountEstablisher owner(AtomicInteger storeCalls) {
        MerchantAccountBootstrapStore store = (request, controller) -> {
            storeCalls.incrementAndGet();
            return new MerchantAccountBootstrapStore.BootstrapOutcome(
                    new MerchantAccount(new MerchantScope("merchant-1")),
                    false);
        };
        return new MerchantAccountEstablisher(principal -> true, store);
    }
}
