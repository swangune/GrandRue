package mainstreet.commercial;

import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommercialAccessDecisionTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final CommercialEntitlementIdentity BOOKING =
            new CommercialEntitlementIdentity("booking.new-activity");
    private static final Instant NOW = Instant.parse("2026-08-23T12:00:00Z");

    @Test
    void decision_preserves_all_effective_commercial_grant_sources() {
        CommercialEntitlementGrantProvenance trial =
                new CommercialEntitlementGrantProvenance(
                        "initial-full-experience-trial",
                        "trial-1"
                );
        CommercialEntitlementGrantProvenance agreement =
                new CommercialEntitlementGrantProvenance(
                        "merchant-commercial-agreement",
                        "agreement-1"
                );
        EffectiveCommercialEntitlementAuthority authority =
                new EffectiveCommercialEntitlementAuthority(
                        List.of(
                                grant(trial, NOW.minusSeconds(10), Optional.empty()),
                                grant(agreement, NOW.minusSeconds(5), Optional.empty())
                        ),
                        Clock.fixed(NOW, ZoneOffset.UTC)
                );

        CommercialAccessDecision decision = authority.decisionFor(MERCHANT, BOOKING);

        assertTrue(decision.permitted());
        assertEquals(BOOKING, decision.entitlementIdentity());
        assertEquals(NOW, decision.evaluatedAt());
        assertEquals(List.of(trial, agreement), decision.effectiveGrantSources());
        assertTrue(authority.isEntitled(MERCHANT, BOOKING));
    }

    @Test
    void denial_preserves_evaluation_instant_and_has_no_effective_source() {
        EffectiveCommercialEntitlementAuthority authority =
                new EffectiveCommercialEntitlementAuthority(
                        List.of(),
                        Clock.fixed(NOW, ZoneOffset.UTC)
                );

        CommercialAccessDecision decision = authority.decisionFor(MERCHANT, BOOKING);

        assertFalse(decision.permitted());
        assertTrue(decision.effectiveGrantSources().isEmpty());
        assertEquals(NOW, decision.evaluatedAt());
        assertFalse(authority.isEntitled(MERCHANT, BOOKING));
    }

    @Test
    void decision_rejects_permission_without_effective_grant_evidence() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new CommercialAccessDecision(
                        BOOKING,
                        true,
                        List.of(),
                        NOW
                )
        );
    }

    private static CommercialEntitlementGrant grant(
            CommercialEntitlementGrantProvenance provenance,
            Instant effectiveFrom,
            Optional<Instant> effectiveUntil
    ) {
        return new CommercialEntitlementGrant(
                MERCHANT,
                BOOKING,
                provenance,
                effectiveFrom,
                effectiveUntil
        );
    }
}
