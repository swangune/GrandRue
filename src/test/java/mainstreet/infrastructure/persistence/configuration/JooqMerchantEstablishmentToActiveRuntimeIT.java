package mainstreet.infrastructure.persistence.configuration;

import org.junit.jupiter.api.Test;

/** Retains the complete IMP-05 assertion path in the shared establishment fixture. */
class JooqMerchantEstablishmentToActiveRuntimeIT {
    @Test
    void established_defined_merchant_reaches_exact_active_entitled_runtime() {
        var fixture = new MerchantEstablishmentRuntimeFixture(false);
        fixture.setUp();
        fixture.establish();
    }
}
