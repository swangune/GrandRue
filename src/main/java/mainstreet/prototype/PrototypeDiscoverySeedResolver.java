package mainstreet.prototype;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Deterministic prototype discovery mapping for the accepted initial
 * customer-interaction options in MS-PROT-052 v1.1.
 *
 * <p>This resolver produces candidate semantic seeds only. It does not
 * activate configuration, invent dependencies or make merchant approval
 * unnecessary.</p>
 */
public final class PrototypeDiscoverySeedResolver {

    private PrototypeDiscoverySeedResolver() {
    }

    public static PrototypeDiscoverySeedResolver standard() {
        return new PrototypeDiscoverySeedResolver();
    }

    public Set<String> resolve(
            Set<PrototypeCustomerInteractionChoice> choices
    ) {
        Set<PrototypeCustomerInteractionChoice> selected = Set.copyOf(
                Objects.requireNonNull(choices, "choices")
        );
        if (selected.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one discovery outcome must be supplied"
            );
        }

        if (selected.contains(
                PrototypeCustomerInteractionChoice.NOTHING_ELSE_FOR_NOW
        )) {
            if (selected.size() != 1) {
                throw new IllegalArgumentException(
                        "NOTHING_ELSE_FOR_NOW is mutually exclusive with additional interaction intent"
                );
            }
            return Set.of();
        }

        if (selected.contains(PrototypeCustomerInteractionChoice.OTHER)) {
            throw new PrototypeConfigurationGapException(
                    "OTHER requires bounded clarification or a governed configuration gap"
            );
        }

        LinkedHashSet<String> seeds = new LinkedHashSet<>();
        for (PrototypeCustomerInteractionChoice choice : selected) {
            switch (choice) {
                case PUBLISH_INFORMATION -> seeds.add("publication");
                case SEND_ENQUIRY -> seeds.add("enquiry");
                case ARRANGE_APPOINTMENT -> {
                    seeds.add("appointment");
                    seeds.add("scheduling");
                    seeds.add("customer");
                }
                case RESERVE_SUBJECT -> {
                    seeds.add("booking");
                    seeds.add("customer");
                }
                case PLACE_ORDER -> seeds.add("ordering");
                case SUBSCRIBE_UPDATES -> throw new PrototypeConfigurationGapException(
                        "Subscription discovery is accepted but not executable in this prototype slice"
                );
                case NOTHING_ELSE_FOR_NOW, OTHER -> throw new IllegalStateException(
                        "Discovery control outcome was not handled before semantic mapping"
                );
            }
        }
        return Set.copyOf(seeds);
    }
}
