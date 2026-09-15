package mainstreet.fulfilment;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Immutable static fulfilment bindings resolved for one configuration. */
public record FulfilmentPlan(
        List<ResolvedFulfilmentBinding> bindings,
        Optional<FulfilmentBindingSetRevisionReference> sourceBindingSetRevision
) {
    public FulfilmentPlan {
        bindings = List.copyOf(Objects.requireNonNull(bindings, "bindings"));
        sourceBindingSetRevision = Objects.requireNonNull(
                sourceBindingSetRevision,
                "sourceBindingSetRevision"
        );
    }

    public FulfilmentPlan(List<ResolvedFulfilmentBinding> bindings) {
        this(bindings, Optional.empty());
    }

    public static FulfilmentPlan empty() {
        return new FulfilmentPlan(List.of(), Optional.empty());
    }

    public static FulfilmentPlan fromBindingSet(
            FulfilmentBindingSetRevisionReference sourceBindingSetRevision,
            List<ResolvedFulfilmentBinding> bindings
    ) {
        return new FulfilmentPlan(
                bindings,
                Optional.of(Objects.requireNonNull(
                        sourceBindingSetRevision,
                        "sourceBindingSetRevision"
                ))
        );
    }
}
