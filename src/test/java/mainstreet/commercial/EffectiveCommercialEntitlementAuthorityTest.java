package mainstreet.commercial;

import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EffectiveCommercialEntitlementAuthorityTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final CommercialEntitlementIdentity BOOKING =
            new CommercialEntitlementIdentity("booking.new-activity");
    private static final Instant NOW = Instant.parse("2026-08-23T04:00:00Z");
    private static final CommercialEntitlementGrantProvenance PROVENANCE =
            new CommercialEntitlementGrantProvenance(
                    "test-commercial-source",
                    "source-1"
            );

    @Test
    void active_grant_makes_entitlement_effective() {
        CommercialEntitlementAuthority authority = authority(List.of(
                grant(
                        MERCHANT,
                        BOOKING,
                        "2026-08-01T00:00:00Z",
                        Optional.of("2026-09-01T00:00:00Z")
                )
        ));

        assertTrue(authority.isEntitled(MERCHANT, BOOKING));
    }

    @Test
    void expired_grant_does_not_make_entitlement_effective() {
        CommercialEntitlementAuthority authority = authority(List.of(
                grant(
                        MERCHANT,
                        BOOKING,
                        "2026-08-01T00:00:00Z",
                        Optional.of("2026-08-22T00:00:00Z")
                )
        ));

        assertFalse(authority.isEntitled(MERCHANT, BOOKING));
    }

    @Test
    void one_effective_grant_is_sufficient_when_another_source_has_expired() {
        CommercialEntitlementAuthority authority = authority(List.of(
                grant(
                        MERCHANT,
                        BOOKING,
                        "2026-07-01T00:00:00Z",
                        Optional.of("2026-08-20T00:00:00Z")
                ),
                grant(
                        MERCHANT,
                        BOOKING,
                        "2026-08-15T00:00:00Z",
                        Optional.empty()
                )
        ));

        assertTrue(authority.isEntitled(MERCHANT, BOOKING));
    }

    @Test
    void grants_remain_merchant_scoped() {
        MerchantScope otherMerchant = new MerchantScope("merchant-b");
        CommercialEntitlementAuthority authority = authority(List.of(
                grant(
                        MERCHANT,
                        BOOKING,
                        "2026-08-01T00:00:00Z",
                        Optional.empty()
                )
        ));

        assertFalse(authority.isEntitled(otherMerchant, BOOKING));
    }

    @Test
    void grant_for_another_entitlement_does_not_manufacture_access() {
        CommercialEntitlementIdentity analytics =
                new CommercialEntitlementIdentity("analytics.advanced");
        CommercialEntitlementAuthority authority = authority(List.of(
                grant(
                        MERCHANT,
                        analytics,
                        "2026-08-01T00:00:00Z",
                        Optional.empty()
                )
        ));

        assertFalse(authority.isEntitled(MERCHANT, BOOKING));
    }

    private static CommercialEntitlementAuthority authority(
            List<CommercialEntitlementGrant> grants
    ) {
        return new EffectiveCommercialEntitlementAuthority(
                grants,
                Clock.fixed(NOW, ZoneOffset.UTC)
        );
    }

    private static CommercialEntitlementGrant grant(
            MerchantScope merchantScope,
            CommercialEntitlementIdentity entitlementIdentity,
            String effectiveFrom,
            Optional<String> effectiveUntil
    ) {
        return new CommercialEntitlementGrant(
                merchantScope,
                entitlementIdentity,
                PROVENANCE,
                Instant.parse(effectiveFrom),
                effectiveUntil.map(Instant::parse)
        );
    }
}
