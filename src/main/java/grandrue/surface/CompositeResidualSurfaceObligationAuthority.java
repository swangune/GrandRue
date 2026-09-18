package grandrue.surface;

import grandrue.application.MerchantScope;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Immutable application-composition implementation of the Surface residual
 * obligation port governed by ADR-009.
 *
 * <p>The composite routes by registered capability identity only. The selected
 * capability-owned authority remains responsible for deciding whether residual
 * obligations exist.</p>
 */
public final class CompositeResidualSurfaceObligationAuthority
        implements ResidualSurfaceObligationAuthority {

    private final Map<String, ResidualObligationProbe> probesByCapability;

    public CompositeResidualSurfaceObligationAuthority(
            SurfaceContributionRegistrySnapshot surfaceRegistry,
            Collection<ResidualSurfaceObligationBinding> bindings
    ) {
        Objects.requireNonNull(surfaceRegistry, "surfaceRegistry");
        Objects.requireNonNull(bindings, "bindings");

        Map<String, ResidualObligationProbe> assembled = new HashMap<>();
        for (ResidualSurfaceObligationBinding binding : bindings) {
            Objects.requireNonNull(binding, "binding");
            ResidualObligationProbe previous = assembled.putIfAbsent(
                    binding.capabilityIdentifier(),
                    binding.probe()
            );
            if (previous != null) {
                throw new IllegalArgumentException(
                        "Duplicate residual obligation binding for capability: "
                                + binding.capabilityIdentifier()
                );
            }
        }

        Set<String> requiredCapabilities = new HashSet<>();
        for (SurfaceContributionDefinition definition : surfaceRegistry.definitions()) {
            if (definition.audience() == SurfaceAudience.MERCHANT
                    && definition.eligibilityContract().allowsResidualManagement()) {
                requiredCapabilities.add(
                        definition.identity().ownerCapabilityIdentifier()
                );
            }
        }

        Set<String> missingCapabilities = new HashSet<>(requiredCapabilities);
        missingCapabilities.removeAll(assembled.keySet());
        if (!missingCapabilities.isEmpty()) {
            throw new IllegalArgumentException(
                    "Missing residual obligation binding for capabilities: "
                            + missingCapabilities.stream().sorted().toList()
            );
        }

        this.probesByCapability = Map.copyOf(assembled);
    }

    @Override
    public boolean hasOutstandingObligations(
            MerchantScope merchantScope,
            String capabilityIdentifier
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        if (capabilityIdentifier == null || capabilityIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Residual obligation capability identifier must not be blank"
            );
        }

        ResidualObligationProbe probe = probesByCapability.get(capabilityIdentifier);
        if (probe == null) {
            throw new IllegalStateException(
                    "No residual obligation binding exists for capability: "
                            + capabilityIdentifier
            );
        }
        return probe.hasOutstandingObligations(merchantScope);
    }
}
