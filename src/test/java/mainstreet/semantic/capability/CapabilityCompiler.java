package mainstreet.semantic.capability;

import mainstreet.semantic.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class CapabilityCompiler {

    public CompiledCapability compile(
            Capability capability,
            List<CapabilityOperation> operations) {

        Objects.requireNonNull(capability);
        Objects.requireNonNull(operations);

        Resource resource = new Resource(capability.identifier());

        List<Operation> compiled = new ArrayList<>();

        for (CapabilityOperation definition : operations) {

            State source = findOrCreateState(
                    resource,
                    definition.sourceState());

            State target = findOrCreateState(
                    resource,
                    definition.targetState());

            Privilege privilege = new Privilege(
                    capability.identifier()
                            + "."
                            + definition.identifier());

            Operation operation = resource.defineOperation(
                    definition.identifier(),
                    new Transition(
                            source,
                            target),
                    privilege);

            compiled.add(operation);
        }

        return new CompiledCapability(
                resource,
                compiled);
    }

    private State findOrCreateState(
            Resource resource,
            String identifier) {

        State existing = resource.state(identifier);

        if (existing != null) {
            return existing;
        }

        return resource.defineState(identifier);
    }

    public record CompiledCapability(
            Resource resource,
            List<Operation> operations) {
    }

    public record CompiledModel(
            List<CompiledCapability> capabilities,
            List<OperationComposition> compositions) {

        public CompiledModel {
            capabilities = List.copyOf(
                    Objects.requireNonNull(capabilities));
            compositions = List.copyOf(
                    Objects.requireNonNull(compositions));

            List<Operation> compiledOperations =
                    capabilities.stream()
                            .flatMap(capability -> capability.operations().stream())
                            .toList();

            for (OperationComposition composition : compositions) {

                if (!compiledOperations.contains(composition.trigger())) {
                    throw new IllegalArgumentException(
                            "Composition trigger is not owned by a compiled capability"
                    );
                }

                if (!compiledOperations.contains(composition.consequence())) {
                    throw new IllegalArgumentException(
                            "Composition consequence is not owned by a compiled capability"
                    );
                }
            }
        }
    }

    public CompiledModel compile(
            List<CompiledCapability> capabilities,
            List<OperationComposition> compositions) {

        return new CompiledModel(
                capabilities,
                compositions);
    }

    public CompiledCapability compile(
            CapabilityConfiguration configuration,
            List<CapabilityOperation> operations) {

        Objects.requireNonNull(configuration);
        Objects.requireNonNull(operations);

        return compile(
                configuration.capability(),
                operations);
    }
}
