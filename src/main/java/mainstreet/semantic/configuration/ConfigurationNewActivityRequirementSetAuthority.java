package mainstreet.semantic.configuration;

import grandrue.application.MerchantScope;

import java.util.Optional;

/** Durable authority for immutable exact RCP-affined requirement evidence. */
public interface ConfigurationNewActivityRequirementSetAuthority {

    ConfigurationNewActivityRequirementSet record(
            RecordConfigurationNewActivityRequirementSetCommand command
    );

    Optional<ConfigurationNewActivityRequirementSet> evidenceForPackage(
            MerchantScope merchantScope,
            String resolvedPackageEvidenceIdentifier
    );
}
