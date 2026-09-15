package mainstreet.application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MerchantScopeTest {

    @Test
    void retains_the_explicit_merchant_boundary() {
        MerchantScope scope = new MerchantScope("merchant-a");

        assertEquals("merchant-a", scope.merchantIdentifier());
    }

    @Test
    void rejects_an_absent_merchant_boundary() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MerchantScope(null)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new MerchantScope(" ")
        );
    }
}
