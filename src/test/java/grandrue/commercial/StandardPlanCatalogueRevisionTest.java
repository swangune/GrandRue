package grandrue.commercial;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StandardPlanCatalogueRevisionTest {

    private static final CommercialEntitlementIdentity PRESENCE =
            new CommercialEntitlementIdentity("presence.public");
    private static final CommercialEntitlementIdentity BOOKING =
            new CommercialEntitlementIdentity("booking.new-activity");
    private static final CommercialEntitlementIdentity ANALYTICS =
            new CommercialEntitlementIdentity("analytics.advanced");

    @Test
    void accepts_explicit_monotonic_standard_plan_entitlement_sets() {
        StandardPlanRevision free = revision(
                StandardPlanLevel.FREE,
                "free-2026-08",
                Set.of(PRESENCE)
        );
        StandardPlanRevision business = revision(
                StandardPlanLevel.BUSINESS,
                "business-2026-08",
                Set.of(PRESENCE, BOOKING)
        );
        StandardPlanRevision growth = revision(
                StandardPlanLevel.GROWTH,
                "growth-2026-08",
                Set.of(PRESENCE, BOOKING, ANALYTICS)
        );

        StandardPlanCatalogueRevision catalogue =
                new StandardPlanCatalogueRevision(
                        "catalogue-2026-08",
                        free,
                        business,
                        growth
                );

        assertEquals(Set.of(PRESENCE), catalogue.freePlan().entitlements());
        assertEquals(
                Set.of(PRESENCE, BOOKING),
                catalogue.businessPlan().entitlements()
        );
        assertEquals(
                Set.of(PRESENCE, BOOKING, ANALYTICS),
                catalogue.growthPlan().entitlements()
        );
    }

    @Test
    void rejects_business_revision_that_removes_a_free_entitlement() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new StandardPlanCatalogueRevision(
                        "catalogue-2026-08",
                        revision(
                                StandardPlanLevel.FREE,
                                "free-2026-08",
                                Set.of(PRESENCE)
                        ),
                        revision(
                                StandardPlanLevel.BUSINESS,
                                "business-2026-08",
                                Set.of(BOOKING)
                        ),
                        revision(
                                StandardPlanLevel.GROWTH,
                                "growth-2026-08",
                                Set.of(PRESENCE, BOOKING, ANALYTICS)
                        )
                )
        );
    }

    @Test
    void rejects_growth_revision_that_removes_a_business_entitlement() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new StandardPlanCatalogueRevision(
                        "catalogue-2026-08",
                        revision(
                                StandardPlanLevel.FREE,
                                "free-2026-08",
                                Set.of(PRESENCE)
                        ),
                        revision(
                                StandardPlanLevel.BUSINESS,
                                "business-2026-08",
                                Set.of(PRESENCE, BOOKING)
                        ),
                        revision(
                                StandardPlanLevel.GROWTH,
                                "growth-2026-08",
                                Set.of(PRESENCE, ANALYTICS)
                        )
                )
        );
    }

    @Test
    void each_revision_preserves_an_explicit_snapshot_instead_of_live_inheritance() {
        HashSet<CommercialEntitlementIdentity> source =
                new HashSet<>(Set.of(PRESENCE, BOOKING));
        StandardPlanRevision business = revision(
                StandardPlanLevel.BUSINESS,
                "business-2026-08",
                source
        );

        source.add(ANALYTICS);

        assertEquals(Set.of(PRESENCE, BOOKING), business.entitlements());
        assertThrows(
                UnsupportedOperationException.class,
                () -> business.entitlements().add(ANALYTICS)
        );
    }

    @Test
    void catalogue_slots_require_the_matching_standard_plan_level() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new StandardPlanCatalogueRevision(
                        "catalogue-2026-08",
                        revision(
                                StandardPlanLevel.BUSINESS,
                                "wrong-free",
                                Set.of(PRESENCE)
                        ),
                        revision(
                                StandardPlanLevel.BUSINESS,
                                "business-2026-08",
                                Set.of(PRESENCE, BOOKING)
                        ),
                        revision(
                                StandardPlanLevel.GROWTH,
                                "growth-2026-08",
                                Set.of(PRESENCE, BOOKING, ANALYTICS)
                        )
                )
        );
    }

    @Test
    void catalogue_requires_three_distinct_plan_revision_identities() {
        for (var duplicatePair : java.util.List.of(
                java.util.List.of("same", "same", "growth"),
                java.util.List.of("same", "business", "same"),
                java.util.List.of("free", "same", "same"))) {
            assertThrows(IllegalArgumentException.class, () -> new StandardPlanCatalogueRevision(
                    "catalogue", revision(StandardPlanLevel.FREE, duplicatePair.get(0), Set.of()),
                    revision(StandardPlanLevel.BUSINESS, duplicatePair.get(1), Set.of()),
                    revision(StandardPlanLevel.GROWTH, duplicatePair.get(2), Set.of())));
        }
    }

    private static StandardPlanRevision revision(
            StandardPlanLevel level,
            String revisionIdentifier,
            Set<CommercialEntitlementIdentity> entitlements
    ) {
        return new StandardPlanRevision(level, revisionIdentifier, entitlements);
    }
}
