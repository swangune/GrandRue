package grandrue.commercial;

import grandrue.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InitialFullExperienceTrialGrantSourceTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final Instant FIRST_CONFIGURATION_ACTIVATED_AT =
            Instant.parse("2026-08-23T04:00:00Z");
    private static final CommercialEntitlementIdentity BOOKING =
            new CommercialEntitlementIdentity("booking.new-activity");
    private static final CommercialEntitlementIdentity ANALYTICS =
            new CommercialEntitlementIdentity("analytics.advanced");

    @Test
    void creates_one_grant_for_each_entitlement_from_authoritative_trial() {
        InitialFullExperienceTrial trial = trial();
        InitialFullExperienceTrialGrantSource source =
                new InitialFullExperienceTrialGrantSource();

        List<CommercialEntitlementGrant> grants = source.grantsFor(
                trial,
                Set.of(BOOKING, ANALYTICS)
        );

        assertEquals(2, grants.size());
        assertTrue(grants.stream().anyMatch(grant ->
                grant.entitlementIdentity().equals(BOOKING)));
        assertTrue(grants.stream().anyMatch(grant ->
                grant.entitlementIdentity().equals(ANALYTICS)));
        assertTrue(grants.stream().allMatch(grant ->
                grant.merchantScope().equals(MERCHANT)));
        assertTrue(grants.stream().allMatch(grant ->
                grant.effectiveFrom().equals(trial.startsAt())));
        assertTrue(grants.stream().allMatch(grant ->
                grant.effectiveUntilExclusive().orElseThrow().equals(
                        trial.expiresAt()
                )));
        assertTrue(grants.stream().allMatch(grant ->
                grant.provenance().equals(trial.grantProvenance())));
    }

    @Test
    void grant_effectiveness_matches_trial_half_open_interval() {
        InitialFullExperienceTrial trial = trial();
        CommercialEntitlementGrant grant =
                new InitialFullExperienceTrialGrantSource()
                        .grantsFor(trial, Set.of(BOOKING))
                        .getFirst();

        assertTrue(grant.isEffectiveAt(trial.startsAt()));
        assertTrue(grant.isEffectiveAt(trial.expiresAt().minusNanos(1)));
        assertFalse(grant.isEffectiveAt(trial.expiresAt()));
        assertEquals(
                Duration.ofHours(720),
                Duration.between(trial.startsAt(), trial.expiresAt())
        );
    }

    @Test
    void empty_entitlement_set_does_not_invent_commercial_permissions() {
        assertTrue(new InitialFullExperienceTrialGrantSource()
                .grantsFor(trial(), Set.of())
                .isEmpty());
    }

    @Test
    void trial_grants_feed_existing_effective_entitlement_authority() {
        InitialFullExperienceTrial trial = trial();
        List<CommercialEntitlementGrant> grants =
                new InitialFullExperienceTrialGrantSource()
                        .grantsFor(trial, Set.of(BOOKING));

        var duringTrial = new EffectiveCommercialEntitlementAuthority(
                grants,
                java.time.Clock.fixed(
                        trial.startsAt().plus(Duration.ofHours(360)),
                        java.time.ZoneOffset.UTC
                )
        );
        var afterTrial = new EffectiveCommercialEntitlementAuthority(
                grants,
                java.time.Clock.fixed(
                        trial.expiresAt(),
                        java.time.ZoneOffset.UTC
                )
        );

        assertTrue(duringTrial.isEntitled(MERCHANT, BOOKING));
        assertFalse(afterTrial.isEntitled(MERCHANT, BOOKING));
    }

    private static InitialFullExperienceTrial trial() {
        return new InitialFullExperienceTrial(
                "trial-1",
                MERCHANT,
                "config-1",
                "activation-1",
                FIRST_CONFIGURATION_ACTIVATED_AT
        );
    }
}
