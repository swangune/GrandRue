package grandrue.merchantaccount;

import grandrue.application.MerchantScope;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MerchantAccountTest {

    @Test
    void exposes_the_immutable_main_street_merchant_identity() {
        MerchantAccount account = new MerchantAccount(
                new MerchantScope("merchant-a")
        );

        assertEquals("merchant-a", account.merchantIdentifier());
    }

    @Test
    void rejects_absent_merchant_scope() {
        assertThrows(
                NullPointerException.class,
                () -> new MerchantAccount(null)
        );
    }

    @Test
    void identity_is_not_business_name_or_other_profile_data() {
        MerchantAccount first = new MerchantAccount(
                new MerchantScope("merchant-a")
        );
        MerchantAccount second = new MerchantAccount(
                new MerchantScope("merchant-b")
        );

        assertNotEquals(first, second);
    }
}
