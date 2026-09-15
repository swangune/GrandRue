package mainstreet.prototype;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Prototype read-side adapter over capability/configuration-owned public
 * subject participation. It creates no participation relationship of its own.
 */
final class PrototypePublicInteractionBindingResolver {

    private final Map<ContributionKey, Function<String, List<PrototypePublicInteractionBinding>>>
            providers;

    private PrototypePublicInteractionBindingResolver(
            Map<ContributionKey, Function<String, List<PrototypePublicInteractionBinding>>> providers
    ) {
        this.providers = Map.copyOf(Objects.requireNonNull(providers, "providers"));
    }

    static PrototypePublicInteractionBindingResolver standard() {
        return new PrototypePublicInteractionBindingResolver(Map.of(
                new ContributionKey("appointment", "arrange-appointment"),
                PrototypePublicInteractionBindingResolver::appointmentBindings,
                new ContributionKey("booking", "reserve-subject"),
                PrototypePublicInteractionBindingResolver::bookingBindings,
                new ContributionKey("ordering", "place-order"),
                PrototypePublicInteractionBindingResolver::orderingBindings
        ));
    }

    List<PrototypePublicInteractionBinding> resolve(
            String merchantIdentifier,
            String ownerCapabilityIdentifier,
            String contributionIdentifier
    ) {
        Objects.requireNonNull(merchantIdentifier, "merchantIdentifier");
        Function<String, List<PrototypePublicInteractionBinding>> provider =
                providers.get(new ContributionKey(
                        ownerCapabilityIdentifier,
                        contributionIdentifier
                ));
        if (provider == null) {
            return List.of();
        }
        return List.copyOf(provider.apply(merchantIdentifier));
    }

    private static List<PrototypePublicInteractionBinding> appointmentBindings(
            String merchantIdentifier
    ) {
        return PrototypeAppointmentSubjectConfiguration.subjectsFor(merchantIdentifier)
                .stream()
                .map(subject -> new PrototypePublicInteractionBinding(
                        subject.publicSubjectReference(),
                        subject.label()
                ))
                .toList();
    }

    private static List<PrototypePublicInteractionBinding> bookingBindings(
            String merchantIdentifier
    ) {
        return PrototypeBookingSubjectConfiguration.subjectsFor(merchantIdentifier)
                .stream()
                .map(subject -> new PrototypePublicInteractionBinding(
                        subject.publicSubjectReference(),
                        subject.label()
                ))
                .toList();
    }

    private static List<PrototypePublicInteractionBinding> orderingBindings(
            String merchantIdentifier
    ) {
        return PrototypeOrderingSubjectConfiguration.subjectsFor(merchantIdentifier)
                .stream()
                .map(subject -> new PrototypePublicInteractionBinding(
                        subject.publicSubjectReference(),
                        subject.label()
                ))
                .toList();
    }

    private record ContributionKey(
            String ownerCapabilityIdentifier,
            String contributionIdentifier
    ) {
        private ContributionKey {
            if (ownerCapabilityIdentifier == null
                    || ownerCapabilityIdentifier.isBlank()
                    || contributionIdentifier == null
                    || contributionIdentifier.isBlank()) {
                throw new IllegalArgumentException(
                        "Contribution identity must be fully qualified"
                );
            }
        }
    }
}
