package mainstreet.money;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MonetaryAmountTest {

    @Test
    void monetary_amount_is_exact_and_currency_qualified() {
        MonetaryAmount amount = new MonetaryAmount(
                new CurrencyIdentity("GBP"),
                BigInteger.valueOf(1250)
        );

        assertEquals(new CurrencyIdentity("GBP"), amount.currencyIdentity());
        assertEquals(BigInteger.valueOf(1250), amount.minorUnitAmount());
    }

    @Test
    void same_minor_unit_magnitude_in_different_currencies_is_not_equal() {
        MonetaryAmount pounds = new MonetaryAmount(
                new CurrencyIdentity("GBP"),
                BigInteger.valueOf(1000)
        );
        MonetaryAmount euros = new MonetaryAmount(
                new CurrencyIdentity("EUR"),
                BigInteger.valueOf(1000)
        );

        assertNotEquals(pounds, euros);
    }

    @Test
    void zero_is_a_valid_monetary_magnitude() {
        MonetaryAmount zero = new MonetaryAmount(
                new CurrencyIdentity("GBP"),
                BigInteger.ZERO
        );

        assertEquals(BigInteger.ZERO, zero.minorUnitAmount());
    }

    @Test
    void negative_monetary_magnitude_is_rejected() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MonetaryAmount(
                        new CurrencyIdentity("GBP"),
                        BigInteger.valueOf(-1)
                )
        );
    }

    @Test
    void currency_identity_must_not_be_blank() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new CurrencyIdentity(" ")
        );
    }
}
