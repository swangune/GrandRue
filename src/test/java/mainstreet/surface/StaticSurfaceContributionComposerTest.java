package mainstreet.surface;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StaticSurfaceContributionComposerTest {

    @Test
    void aggregates_same_audience_contributions_at_one_composition_target_without_merging_semantics() {
        StaticSurfaceContribution scheduling = contribution(
                "scheduling",
                "calendar-workspace",
                SurfaceAudience.MERCHANT,
                Optional.of("merchant/calendar"),
                Set.of("schedule-summary"),
                Set.of()
        );
        StaticSurfaceContribution booking = contribution(
                "booking",
                "calendar-booking-actions",
                SurfaceAudience.MERCHANT,
                Optional.of("merchant/calendar"),
                Set.of("booking-summary"),
                Set.of("booking.create")
        );

        StaticSurfaceComposition composition =
                new StaticSurfaceContributionComposer().compose(
                        new StaticSurfaceContributionCatalogue(
                                List.of(booking, scheduling)
                        )
                );

        assertEquals(1, composition.groups().size());
        StaticSurfaceCompositionGroup group = composition.groups().getFirst();
        assertEquals(SurfaceAudience.MERCHANT, group.audience());
        assertEquals(Optional.of("merchant/calendar"), group.compositionTargetReference());
        assertEquals(
                List.of(booking.identity(), scheduling.identity()),
                group.contributions().stream()
                        .map(StaticSurfaceContribution::identity)
                        .toList()
        );
        assertEquals(
                Set.of("schedule-summary", "booking-summary"),
                group.contributions().stream()
                        .flatMap(value -> value.projectionRequirements().stream())
                        .collect(java.util.stream.Collectors.toSet())
        );
        assertEquals(
                Set.of("booking.create"),
                group.contributions().stream()
                        .flatMap(value -> value.supportedOperationReferences().stream())
                        .collect(java.util.stream.Collectors.toSet())
        );
    }

    @Test
    void does_not_combine_the_same_target_across_different_audiences() {
        StaticSurfaceContribution merchant = contribution(
                "booking",
                "merchant-book",
                SurfaceAudience.MERCHANT,
                Optional.of("shared/booking"),
                Set.of(),
                Set.of("booking.create")
        );
        StaticSurfaceContribution publicContribution = contribution(
                "booking",
                "public-book",
                SurfaceAudience.PUBLIC,
                Optional.of("shared/booking"),
                Set.of(),
                Set.of("booking.create")
        );

        StaticSurfaceComposition composition =
                new StaticSurfaceContributionComposer().compose(
                        new StaticSurfaceContributionCatalogue(
                                List.of(publicContribution, merchant)
                        )
                );

        assertEquals(2, composition.groups().size());
    }

    @Test
    void leaves_untargeted_contributions_as_independent_composition_groups() {
        StaticSurfaceContribution first = contribution(
                "inventory",
                "low-stock-attention",
                SurfaceAudience.MERCHANT,
                Optional.empty(),
                Set.of("low-stock"),
                Set.of()
        );
        StaticSurfaceContribution second = contribution(
                "notification",
                "delivery-attention",
                SurfaceAudience.MERCHANT,
                Optional.empty(),
                Set.of("failed-delivery"),
                Set.of()
        );

        StaticSurfaceComposition composition =
                new StaticSurfaceContributionComposer().compose(
                        new StaticSurfaceContributionCatalogue(List.of(second, first))
                );

        assertEquals(2, composition.groups().size());
        assertEquals(
                List.of(first.identity(), second.identity()),
                composition.groups().stream()
                        .map(group -> group.contributions().getFirst().identity())
                        .toList()
        );
    }

    private static StaticSurfaceContribution contribution(
            String owner,
            String identifier,
            SurfaceAudience audience,
            Optional<String> target,
            Set<String> projections,
            Set<String> operations
    ) {
        return new StaticSurfaceContribution(
                new SurfaceContributionIdentity(owner, identifier),
                audience,
                SurfaceContributionKind.WORKSPACE,
                target,
                projections,
                operations,
                Set.of(new ActiveCapabilitySurfaceEligibilityEvidence(owner))
        );
    }
}
