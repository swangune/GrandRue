package grandrue.semantic.execution;

import mainstreet.semantic.executable.ApplicableOperation;

/**
 * Resolves the exact ADR-012 executable-support requirement for one operation
 * already bound to a captured executable-model snapshot.
 *
 * <p>The resolver is technical composition. It must consume accepted semantic
 * contract identity; it must not invent participant/effect meaning.</p>
 */
@FunctionalInterface
public interface ExecutableSupportRequirementResolver {

    ExecutableSupportRequirement resolve(ApplicableOperation operation);
}
