package mainstreet.surface;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Aggregates statically applicable contributions by compatible presentation
 * target without merging their semantic contracts.
 *
 * <p>Only contributions with the same audience and the same explicit
 * composition target are grouped. Untargeted contributions remain independent.
 * Final actor/context visibility is intentionally outside this static step.</p>
 */
public final class StaticSurfaceContributionComposer {

    private static final Comparator<StaticSurfaceContribution> CONTRIBUTION_ORDER =
            Comparator.comparingInt(
                            (StaticSurfaceContribution contribution) ->
                                    contribution.audience().ordinal()
                    )
                    .thenComparing(contribution ->
                            contribution.identity().ownerCapabilityIdentifier())
                    .thenComparing(contribution ->
                            contribution.identity().contributionIdentifier());

    public StaticSurfaceComposition compose(
            StaticSurfaceContributionCatalogue catalogue
    ) {
        Objects.requireNonNull(catalogue, "catalogue");

        List<StaticSurfaceContribution> ordered = new ArrayList<>(
                catalogue.contributions()
        );
        ordered.sort(CONTRIBUTION_ORDER);

        Map<TargetKey, List<StaticSurfaceContribution>> targeted =
                new LinkedHashMap<>();
        List<StaticSurfaceCompositionGroup> groups = new ArrayList<>();

        for (StaticSurfaceContribution contribution : ordered) {
            Optional<String> target = contribution.compositionTargetReference();
            if (target.isPresent()) {
                targeted.computeIfAbsent(
                        new TargetKey(contribution.audience(), target.orElseThrow()),
                        ignored -> new ArrayList<>()
                ).add(contribution);
            } else {
                groups.add(new StaticSurfaceCompositionGroup(
                        contribution.audience(),
                        Optional.empty(),
                        List.of(contribution)
                ));
            }
        }

        for (Map.Entry<TargetKey, List<StaticSurfaceContribution>> entry
                : targeted.entrySet()) {
            groups.add(new StaticSurfaceCompositionGroup(
                    entry.getKey().audience(),
                    Optional.of(entry.getKey().targetReference()),
                    entry.getValue()
            ));
        }

        groups.sort(GROUP_ORDER);
        return new StaticSurfaceComposition(groups);
    }

    private static final Comparator<StaticSurfaceCompositionGroup> GROUP_ORDER =
            Comparator.comparingInt(
                            (StaticSurfaceCompositionGroup group) ->
                                    group.audience().ordinal()
                    )
                    .thenComparing(group ->
                            group.compositionTargetReference().isEmpty())
                    .thenComparing(group ->
                            group.compositionTargetReference().orElse(""))
                    .thenComparing(group ->
                            group.contributions().getFirst().identity()
                                    .ownerCapabilityIdentifier())
                    .thenComparing(group ->
                            group.contributions().getFirst().identity()
                                    .contributionIdentifier());

    private record TargetKey(
            SurfaceAudience audience,
            String targetReference
    ) {
        private TargetKey {
            Objects.requireNonNull(audience, "audience");
            if (targetReference == null || targetReference.isBlank()) {
                throw new IllegalArgumentException(
                        "Composition target reference must not be blank"
                );
            }
        }
    }
}
