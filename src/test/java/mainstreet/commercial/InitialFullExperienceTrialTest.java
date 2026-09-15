package mainstreet.commercial;

import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InitialFullExperienceTrialTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final Instant START = Instant.parse("2026-08-23T04:00:00Z");

    @Test
    void retains_first_activation_provenance_and_exact_720_hour_interval() {
        InitialFullExperienceTrial trial = trial();

        assertEquals("trial-1", trial.trialIdentity());
        assertEquals(MERCHANT, trial.merchantScope());
        assertEquals("config-1", trial.originConfigurationRevisionIdentifier());
        assertEquals("activation-1", trial.originatingFirstActivationIdentity());
        assertEquals(START, trial.startsAt());
        assertEquals(START.plus(Duration.ofHours(720)), trial.expiresAt());
        assertEquals(
                new CommercialEntitlementGrantProvenance(
                        "initial-full-experience-trial",
                        "trial-1"
                ),
                trial.grantProvenance()
        );
    }

    @Test
    void effectiveness_uses_half_open_interval() {
        InitialFullExperienceTrial trial = trial();

        assertFalse(trial.isEffectiveAt(START.minusNanos(1)));
        assertTrue(trial.isEffectiveAt(START));
        assertTrue(trial.isEffectiveAt(trial.expiresAt().minusNanos(1)));
        assertFalse(trial.isEffectiveAt(trial.expiresAt()));
    }

    @Test
    void rejects_missing_identity_or_activation_provenance() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new InitialFullExperienceTrial(
                        " ", MERCHANT, "config-1", "activation-1", START
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new InitialFullExperienceTrial(
                        "trial-1", MERCHANT, " ", "activation-1", START
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new InitialFullExperienceTrial(
                        "trial-1", MERCHANT, "config-1", " ", START
                )
        );
    }

    private static InitialFullExperienceTrial trial() {
        return new InitialFullExperienceTrial(
                "trial-1",
                MERCHANT,
                "config-1",
                "activation-1",
                START
        );
    }
}
