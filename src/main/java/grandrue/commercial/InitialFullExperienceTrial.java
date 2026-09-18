package grandrue.commercial;

import grandrue.commercial.CommercialEntitlementGrantProvenance;

import grandrue.application.MerchantScope;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * Authoritative Commercial fact for one Merchant Account's automatic initial
 * full-experience trial.
 *
 * <p>The trial is anchored to the Merchant Account's first committed
 * Configuration Revision activation and lasts exactly 720 elapsed hours. It
 * does not own Configuration state or semantic applicability.</p>
 *
 * <p>Governed by MS-PROT-056 v1.4 and v1.5.</p>
 */
public record InitialFullExperienceTrial(
        String trialIdentity,
        MerchantScope merchantScope,
        String originConfigurationRevisionIdentifier,
        String originatingFirstActivationIdentity,
        Instant startsAt
) {

    private static final Duration DURATION = Duration.ofHours(720);

    public InitialFullExperienceTrial {
        if (trialIdentity == null || trialIdentity.isBlank()) {
            throw new IllegalArgumentException("Trial identity must not be blank");
        }
        Objects.requireNonNull(merchantScope, "merchantScope");
        if (originConfigurationRevisionIdentifier == null
                || originConfigurationRevisionIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Origin configuration revision identifier must not be blank"
            );
        }
        if (originatingFirstActivationIdentity == null
                || originatingFirstActivationIdentity.isBlank()) {
            throw new IllegalArgumentException(
                    "Originating first activation identity must not be blank"
            );
        }
        Objects.requireNonNull(startsAt, "startsAt");
    }

    public Instant expiresAt() {
        return startsAt.plus(DURATION);
    }

    public boolean isEffectiveAt(Instant instant) {
        Objects.requireNonNull(instant, "instant");
        return !instant.isBefore(startsAt) && instant.isBefore(expiresAt());
    }

    public CommercialEntitlementGrantProvenance grantProvenance() {
        return new CommercialEntitlementGrantProvenance(
                "initial-full-experience-trial",
                trialIdentity
        );
    }
}
