package mainstreet.money;

import java.math.BigInteger;

/**
 * Exact, immutable, currency-qualified monetary magnitude expressed in minor
 * units. Semantic meaning such as price, deposit, obligation, fee or refund
 * remains owned by the surrounding domain fact rather than by this value.
 */
public record MonetaryAmount(
        CurrencyIdentity currencyIdentity,
        BigInteger minorUnitAmount
) {

    public MonetaryAmount {
        if (currencyIdentity == null) {
            throw new IllegalArgumentException(
                    "Currency identity must not be null"
            );
        }
        if (minorUnitAmount == null) {
            throw new IllegalArgumentException(
                    "Minor-unit amount must not be null"
            );
        }
        if (minorUnitAmount.signum() < 0) {
            throw new IllegalArgumentException(
                    "Minor-unit amount must not be negative"
            );
        }
    }
}
