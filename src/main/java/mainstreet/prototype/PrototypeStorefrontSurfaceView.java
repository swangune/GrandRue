package mainstreet.prototype;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/** Frontend-facing projection of already-composed public surface inputs. */
public record PrototypeStorefrontSurfaceView(
        String merchantIdentifier,
        String configurationIdentifier,
        String releaseIdentifier,
        List<Group> groups
) {
    public PrototypeStorefrontSurfaceView {
        requireIdentifier(merchantIdentifier, "Merchant identifier");
        requireIdentifier(configurationIdentifier, "Configuration identifier");
        requireIdentifier(releaseIdentifier, "Release identifier");
        groups = List.copyOf(Objects.requireNonNull(groups, "groups"));
    }

    public record Group(
            String compositionTargetReference,
            List<Contribution> contributions
    ) {
        public Group {
            requireIdentifier(
                    compositionTargetReference,
                    "Composition target reference"
            );
            contributions = List.copyOf(
                    Objects.requireNonNull(contributions, "contributions")
            );
        }
    }

    public record Contribution(
            String ownerCapabilityIdentifier,
            String contributionIdentifier,
            String kind,
            Set<String> supportedOperationReferences,
            List<PrototypePublicInteractionBinding> bindings
    ) {
        public Contribution {
            requireIdentifier(
                    ownerCapabilityIdentifier,
                    "Owner capability identifier"
            );
            requireIdentifier(
                    contributionIdentifier,
                    "Contribution identifier"
            );
            requireIdentifier(kind, "Contribution kind");
            supportedOperationReferences = Set.copyOf(
                    Objects.requireNonNull(
                            supportedOperationReferences,
                            "supportedOperationReferences"
                    )
            );
            bindings = List.copyOf(Objects.requireNonNull(bindings, "bindings"));
        }
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
