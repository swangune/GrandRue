package mainstreet.surface;

import java.util.Objects;

/**
 * Explicit application-composition binding from one registered capability
 * identity to that capability's residual-obligation authority.
 *
 * <p>This binding carries identity and delegation only. It contains no
 * capability lifecycle semantics.</p>
 */
public record ResidualSurfaceObligationBinding(
        String capabilityIdentifier,
        ResidualObligationProbe probe
) {

    public ResidualSurfaceObligationBinding {
        if (capabilityIdentifier == null || capabilityIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Residual obligation capability identifier must not be blank"
            );
        }
        Objects.requireNonNull(probe, "probe");
    }
}
