package mainstreet.money;

/**
 * Stable currency identity carried by every authoritative monetary value.
 *
 * <p>The current semantic contract requires an explicit identity but does not
 * yet mandate a provider, locale or foreign-exchange representation.</p>
 */
public record CurrencyIdentity(String identifier) {

    public CurrencyIdentity {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Currency identity must not be blank"
            );
        }
    }
}
