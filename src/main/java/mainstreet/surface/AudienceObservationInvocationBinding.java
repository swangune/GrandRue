package mainstreet.surface;

import java.util.Objects;
import java.util.UUID;

/** Opaque identity of one current audience-admission invocation. */
public sealed interface AudienceObservationInvocationBinding
        permits DefaultAudienceObservationInvocationBinding {
}

final class DefaultAudienceObservationInvocationBinding
        implements AudienceObservationInvocationBinding {

    private final UUID identifier = UUID.randomUUID();
    private final ObservationRequestBinding requestBinding;

    DefaultAudienceObservationInvocationBinding(
            ObservationRequestBinding requestBinding
    ) {
        this.requestBinding = Objects.requireNonNull(
                requestBinding,
                "requestBinding"
        );
    }

    UUID identifier() {
        return identifier;
    }

    ObservationRequestBinding requestBinding() {
        return requestBinding;
    }
}

/** Package-owned affinity inspection for trusted result binding only. */
final class AudienceObservationInvocationBindingDetails {

    private AudienceObservationInvocationBindingDetails() {
    }

    static ObservationRequestBinding requestBinding(
            AudienceObservationInvocationBinding binding
    ) {
        Objects.requireNonNull(binding, "binding");
        return ((DefaultAudienceObservationInvocationBinding) binding)
                .requestBinding();
    }
}
