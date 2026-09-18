package grandrue.semantic.configuration;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable revision with its actual change source, independent of onboarding.
 * Authority: MS-PROT-040 v1.1,
 * designs/MS-PROT-040 v1.1 — Configuration Revision, Resolved Package & Atomic Activation Amendment.md,
 * §2 — Ownership; §3 — Configuration Revision identity and immutability.
 */
public record ChangedConfigurationRevision(
        MerchantConfiguration configuration,
        ConfigurationChangeSet changeSet,
        Instant materialisedAt
) {
    public ChangedConfigurationRevision {
        Objects.requireNonNull(configuration, "configuration");
        Objects.requireNonNull(changeSet, "changeSet");
        Objects.requireNonNull(materialisedAt, "materialisedAt");
        if (configuration.version() <= 1
                || !configuration.merchantIdentifier().equals(changeSet.merchantScope().merchantIdentifier())
                || !configuration.baseConfigurationIdentifier().equals(Optional.of(changeSet.baseConfigurationRevisionIdentifier()))
                || !configuration.capabilityIdentifiers().equals(changeSet.capabilityIdentifiers())
                || !configuration.policySelections().equals(changeSet.policySelections())
                || !configuration.fulfilmentBindingSetRevisionReference().equals(changeSet.fulfilmentBindingSetRevisionReference())) {
            throw new IllegalArgumentException("Changed revision must retain its exact complete change intent");
        }
    }
}
