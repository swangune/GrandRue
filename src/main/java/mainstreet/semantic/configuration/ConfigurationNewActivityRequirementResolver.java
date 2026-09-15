package mainstreet.semantic.configuration;

import mainstreet.semantic.execution.ExecutableSupportRequirement;

import java.util.Set;

/** Derives complete new-activity execution requirements from one exact RCP. */
@FunctionalInterface
public interface ConfigurationNewActivityRequirementResolver {

    Set<ExecutableSupportRequirement> resolve(
            ResolvedConfigurationPackage resolvedPackage
    );
}
