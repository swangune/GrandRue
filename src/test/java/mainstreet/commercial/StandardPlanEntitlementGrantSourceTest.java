package mainstreet.commercial;

import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StandardPlanEntitlementGrantSourceTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-1");
    private static final Instant START = Instant.parse("2026-08-01T00:00:00Z");
    private static final Instant END = Instant.parse("2026-09-01T00:00:00Z");
    private static final CommercialEntitlementGrantProvenance AGREEMENT_SOURCE =
            new CommercialEntitlementGrantProvenance(
                    "merchant-commercial-agreement",
                    "agreement-1"
            );

    private final StandardPlanEntitlementGrantSource source =
            new StandardPlanEntitlementGrantSource();

    @Test
    void explicit_plan_entitlements_become_merchant_scoped_grants_with_authoritative_provenance() {
        CommercialEntitlementIdentity booking = entitlement("booking.new-activity");
        CommercialEntitlementIdentity inventory = entitlement("inventory.operation");
        StandardPlanRevision plan = plan(
                StandardPlanLevel.BUSINESS,
                "business-v1",
                Set.of(booking, inventory)
        );

        var grants = source.grantsFor(
                MERCHANT,
                plan,
                AGREEMENT_SOURCE,
                START,
                Optional.of(END)
        );

        assertEquals(2, grants.size());
        assertEquals(
                Set.of(booking, inventory),
                grants.stream()
                        .map(CommercialEntitlementGrant::entitlementIdentity)
                        .collect(java.util.stream.Collectors.toSet())
        );
        assertTrue(grants.stream().allMatch(grant -> grant.merchantScope().equals(MERCHANT)));
        assertTrue(grants.stream().allMatch(grant -> grant.provenance().equals(AGREEMENT_SOURCE)));
        assertTrue(grants.stream().allMatch(grant -> grant.effectiveFrom().equals(START)));
        assertTrue(grants.stream().allMatch(grant -> grant.effectiveUntilExclusive().equals(Optional.of(END))));
    }

    @Test
    void plan_level_does_not_infer_unlisted_entitlements() {
        CommercialEntitlementIdentity booking = entitlement("booking.new-activity");
        StandardPlanRevision growth = plan(
                StandardPlanLevel.GROWTH,
                "growth-v1",
                Set.of(booking)
        );

        var grants = source.grantsFor(
                MERCHANT,
                growth,
                AGREEMENT_SOURCE,
                START,
                Optional.empty()
        );

        assertEquals(1, grants.size());
        assertEquals(booking, grants.getFirst().entitlementIdentity());
    }

    @Test
    void empty_plan_entitlement_snapshot_creates_no_grants() {
        StandardPlanRevision free = plan(
                StandardPlanLevel.FREE,
                "free-v1",
                Set.of()
        );

        assertTrue(source.grantsFor(
                MERCHANT,
                free,
                new CommercialEntitlementGrantProvenance(
                        "standing-commercial-baseline",
                        "merchant-1-free-baseline"
                ),
                START,
                Optional.empty()
        ).isEmpty());
    }

    @Test
    void early_paid_subscription_does_not_shorten_remaining_trial_entitlements() {
        CommercialEntitlementIdentity trialOnly = entitlement("advanced.analytics");
        CommercialEntitlementIdentity booking = entitlement("booking.new-activity");
        Instant firstConfigurationActivatedAt = Instant.parse("2026-08-01T00:00:00Z");
        Instant paidAgreementEffectiveAt = Instant.parse("2026-08-20T00:00:00Z");
        InitialFullExperienceTrial trial = new InitialFullExperienceTrial(
                "trial-1",
                MERCHANT,
                "config-1",
                "activation-1",
                firstConfigurationActivatedAt
        );

        var trialGrants = new InitialFullExperienceTrialGrantSource().grantsFor(
                trial,
                Set.of(trialOnly, booking)
        );
        var paidGrants = source.grantsFor(
                MERCHANT,
                plan(StandardPlanLevel.BUSINESS, "business-v1", Set.of(booking)),
                AGREEMENT_SOURCE,
                paidAgreementEffectiveAt,
                Optional.empty()
        );
        var allGrants = new ArrayList<CommercialEntitlementGrant>();
        allGrants.addAll(trialGrants);
        allGrants.addAll(paidGrants);

        EffectiveCommercialEntitlementAuthority beforeTrialExpiry =
                new EffectiveCommercialEntitlementAuthority(
                        allGrants,
                        Clock.fixed(trial.expiresAt().minusSeconds(1), ZoneOffset.UTC)
                );
        EffectiveCommercialEntitlementAuthority afterTrialExpiry =
                new EffectiveCommercialEntitlementAuthority(
                        allGrants,
                        Clock.fixed(trial.expiresAt(), ZoneOffset.UTC)
                );

        assertTrue(beforeTrialExpiry.isEntitled(MERCHANT, trialOnly));
        assertTrue(beforeTrialExpiry.isEntitled(MERCHANT, booking));
        assertFalse(afterTrialExpiry.isEntitled(MERCHANT, trialOnly));
        assertTrue(afterTrialExpiry.isEntitled(MERCHANT, booking));
    }

    private static StandardPlanRevision plan(
            StandardPlanLevel level,
            String revision,
            Set<CommercialEntitlementIdentity> entitlements
    ) {
        return new StandardPlanRevision(level, revision, entitlements);
    }

    private static CommercialEntitlementIdentity entitlement(String identifier) {
        return new CommercialEntitlementIdentity(identifier);
    }
}
