package grandrue.commercial;

import grandrue.money.MonetaryAmount;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Initial accepted Main Street annual-billing commercial price policy.
 *
 * <p>Annual billing is twelve monthly periods with a five-percent discount.
 * If that calculation produces a fractional minor unit, the result is rounded
 * down so the merchant receives at least the advertised discount. This is a
 * commercial pricing rule, not a primitive money invariant.</p>
 */
public final class AnnualBillingPricePolicy {

    private static final BigInteger MONTHS_PER_YEAR = BigInteger.valueOf(12);
    private static final BigInteger DISCOUNTED_PERCENT = BigInteger.valueOf(95);
    private static final BigInteger PERCENT_DENOMINATOR = BigInteger.valueOf(100);

    public MonetaryAmount annualPrice(MonetaryAmount monthlyPrice) {
        Objects.requireNonNull(monthlyPrice, "monthlyPrice");

        BigInteger annualMinorUnits = monthlyPrice.minorUnitAmount()
                .multiply(MONTHS_PER_YEAR)
                .multiply(DISCOUNTED_PERCENT)
                .divide(PERCENT_DENOMINATOR);

        return new MonetaryAmount(
                monthlyPrice.currencyIdentity(),
                annualMinorUnits
        );
    }
}
