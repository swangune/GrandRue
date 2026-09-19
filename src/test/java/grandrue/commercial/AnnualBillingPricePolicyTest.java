package grandrue.commercial;

import grandrue.money.CurrencyIdentity;
import grandrue.money.MonetaryAmount;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AnnualBillingPricePolicyTest {

    private final AnnualBillingPricePolicy policy = new AnnualBillingPricePolicy();

    @Test
    void annual_price_applies_five_percent_discount_to_twelve_monthly_periods() {
        MonetaryAmount monthly = amount("GBP", 1000);

        MonetaryAmount annual = policy.annualPrice(monthly);

        assertEquals(amount("GBP", 11400), annual);
    }

    @Test
    void fractional_minor_unit_result_rounds_down() {
        MonetaryAmount monthly = amount("GBP", 999);

        MonetaryAmount annual = policy.annualPrice(monthly);

        assertEquals(amount("GBP", 11388), annual);
    }

    @Test
    void zero_monthly_price_remains_zero() {
        assertEquals(
                amount("GBP", 0),
                policy.annualPrice(amount("GBP", 0))
        );
    }

    @Test
    void currency_identity_is_preserved() {
        MonetaryAmount annual = policy.annualPrice(amount("EUR", 999));

        assertEquals(new CurrencyIdentity("EUR"), annual.currencyIdentity());
    }

    @Test
    void null_monthly_price_is_rejected() {
        assertThrows(
                NullPointerException.class,
                () -> policy.annualPrice(null)
        );
    }

    private static MonetaryAmount amount(String currency, long minorUnits) {
        return new MonetaryAmount(
                new CurrencyIdentity(currency),
                BigInteger.valueOf(minorUnits)
        );
    }
}
