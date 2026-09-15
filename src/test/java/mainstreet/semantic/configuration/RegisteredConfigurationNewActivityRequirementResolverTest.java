package mainstreet.semantic.configuration;

import mainstreet.semantic.compiler.ConfigurationCompiler;
import mainstreet.semantic.execution.ExecutableSupportRequirement;
import mainstreet.semantic.execution.SemanticExecutionContractReference;
import mainstreet.semantic.registry.InMemorySemanticRegistry;
import mainstreet.semantic.registry.OwnedOperationDefinition;
import mainstreet.semantic.registry.OwnedOperationalObjectDefinition;
import mainstreet.semantic.registry.RegisteredCapability;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegisteredConfigurationNewActivityRequirementResolverTest {

    private static final String RELEASE = "semantic-release-21";

    @Test
    void derives_every_operation_requirement_from_exact_release_mappings() {
        ExecutableSupportRequirement requirement = new ExecutableSupportRequirement(
                reference("booking.create"),
                Set.of(reference("notification.intent"))
        );
        RegisteredConfigurationNewActivityRequirementResolver resolver =
                new RegisteredConfigurationNewActivityRequirementResolver(
                        List.of(new ConfigurationOperationExecutionRequirement(
                                RELEASE,
                                "booking.create",
                                requirement
                        ))
                );

        assertEquals(Set.of(requirement), resolver.resolve(resolvedPackage()));
    }

    @Test
    void fails_closed_when_one_rcp_operation_has_no_release_mapping() {
        RegisteredConfigurationNewActivityRequirementResolver resolver =
                new RegisteredConfigurationNewActivityRequirementResolver(
                        List.of()
                );

        assertThrows(
                IllegalStateException.class,
                () -> resolver.resolve(resolvedPackage())
        );
    }

    @Test
    void rejects_a_mapping_with_cross_release_contracts() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ConfigurationOperationExecutionRequirement(
                        RELEASE,
                        "booking.create",
                        new ExecutableSupportRequirement(
                                new SemanticExecutionContractReference(
                                        "semantic-release-20",
                                        "booking.create"
                                )
                        )
                )
        );
    }

    private static ResolvedConfigurationPackage resolvedPackage() {
        MerchantConfiguration configuration = new MerchantConfiguration(
                "merchant-acme",
                "configuration-1",
                1,
                RELEASE,
                Set.of("booking")
        );
        InMemorySemanticRegistry registry = new InMemorySemanticRegistry();
        registry.publish(new SemanticRegistrySnapshot(
                RELEASE,
                Set.of(new RegisteredCapability(
                        "booking",
                        List.of(new OwnedOperationalObjectDefinition(
                                "booking",
                                Set.of("requested"),
                                "requested"
                        )),
                        List.of(OwnedOperationDefinition.creation(
                                "booking.create",
                                "booking",
                                "requested",
                                "BookingCreated",
                                "booking.create"
                        ))
                ))
        ));
        return new ConfigurationPackageResolver(
                new ConfigurationCompiler(registry)
        ).resolve(
                configuration,
                "mainstreet-compiler-1",
                Instant.parse("2026-08-29T11:00:00Z")
        );
    }

    private static SemanticExecutionContractReference reference(
            String contract
    ) {
        return new SemanticExecutionContractReference(RELEASE, contract);
    }
}
