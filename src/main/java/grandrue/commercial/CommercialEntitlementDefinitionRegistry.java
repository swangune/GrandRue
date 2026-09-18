package grandrue.commercial;

import grandrue.commercial.CommercialEntitlementDefinition;
import mainstreet.commercial.CommercialEntitlementIdentity;
import grandrue.commercial.CommercialEntitlementTargetKind;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Commercial authority registry for entitlement definitions.
 *
 * <p>One entitlement identity has one commercial definition within a registry.
 * Target lookup is intentionally descriptive only; this registry does not
 * decide runtime satisfaction when multiple entitlements target the same
 * access point.</p>
 */
public final class CommercialEntitlementDefinitionRegistry {

    private final Map<CommercialEntitlementIdentity, CommercialEntitlementDefinition>
            definitions = new LinkedHashMap<>();

    public void register(CommercialEntitlementDefinition definition) {
        Objects.requireNonNull(definition, "definition");
        CommercialEntitlementDefinition existing = definitions.putIfAbsent(
                definition.entitlementIdentity(),
                definition
        );
        if (existing != null) {
            throw new IllegalArgumentException(
                    "Commercial entitlement identity already registered: "
                            + definition.entitlementIdentity().identifier()
            );
        }
    }

    public Optional<CommercialEntitlementDefinition> find(
            CommercialEntitlementIdentity entitlementIdentity
    ) {
        return Optional.ofNullable(definitions.get(
                Objects.requireNonNull(entitlementIdentity, "entitlementIdentity")
        ));
    }

    public List<CommercialEntitlementDefinition> definitionsTargeting(
            CommercialEntitlementTargetKind targetKind,
            String targetReference
    ) {
        Objects.requireNonNull(targetKind, "targetKind");
        if (targetReference == null || targetReference.isBlank()) {
            throw new IllegalArgumentException(
                    "Commercial entitlement target reference must not be blank"
            );
        }
        List<CommercialEntitlementDefinition> matches = new ArrayList<>();
        definitions.values().stream()
                .filter(definition -> definition.targetKind() == targetKind)
                .filter(definition -> definition.targetReference().equals(targetReference))
                .forEach(matches::add);
        return List.copyOf(matches);
    }
}
