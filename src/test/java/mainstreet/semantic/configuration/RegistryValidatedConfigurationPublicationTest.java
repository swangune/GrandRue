package mainstreet.semantic.configuration;

import mainstreet.semantic.registry.InMemorySemanticRegistry;
import mainstreet.semantic.registry.RegisteredCapability;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import mainstreet.semantic.executable.ExecutableMerchantModel;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegistryValidatedConfigurationPublicationTest {

    private static final Instant GENERATED_AT =
            Instant.parse("2026-08-20T11:30:00Z");

    @Test
    void rejects_capabilities_absent_from_the_referenced_registry_version() {
        InMemorySemanticRegistry registry = registry();
        ConfigurationPublication stored =
                new InMemoryConfigurationPublication();
        ConfigurationPublication publication =
                new RegistryValidatedConfigurationPublication(
                        registry,
                        stored
                );

        ConfigurationRelease invalid = release(
                1,
                "semantic-registry-1.0",
                Set.of("commerce-order", "delivery")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> publication.publish(invalid)
        );
        assertTrue(stored.latest("merchant-grocery").isEmpty());
    }

    @Test
    void preserves_historical_meaning_across_registry_versions() {
        InMemorySemanticRegistry registry = registry();
        ConfigurationPublication publication =
                new RegistryValidatedConfigurationPublication(
                        registry,
                        new InMemoryConfigurationPublication()
                );

        ConfigurationRelease versionOne = release(
                1,
                "semantic-registry-1.0",
                Set.of("commerce-order")
        );
        ConfigurationRelease versionTwo = release(
                2,
                "semantic-registry-1.1",
                Set.of("commerce-order", "delivery")
        );

        publication.publish(versionOne);
        publication.publish(versionTwo);

        assertEquals(
                versionOne,
                publication.version("merchant-grocery", 1)
                        .orElseThrow()
        );
        assertEquals(
                versionTwo,
                publication.latest("merchant-grocery").orElseThrow()
        );
    }

    @Test
    void rejects_an_unknown_registry_version_before_publication() {
        ConfigurationPublication stored =
                new InMemoryConfigurationPublication();
        ConfigurationPublication publication =
                new RegistryValidatedConfigurationPublication(
                        registry(),
                        stored
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> publication.publish(release(
                        1,
                        "semantic-registry-2.0",
                        Set.of("commerce-order")
                ))
        );
        assertTrue(stored.latest("merchant-grocery").isEmpty());
    }

    private static InMemorySemanticRegistry registry() {
        InMemorySemanticRegistry registry =
                new InMemorySemanticRegistry();
        registry.publish(new SemanticRegistrySnapshot(
                "semantic-registry-1.0",
                Set.of(capability("commerce-order"))
        ));
        registry.publish(new SemanticRegistrySnapshot(
                "semantic-registry-1.1",
                Set.of(
                        capability("commerce-order"),
                        capability("delivery")
                )
        ));
        return registry;
    }

    private static RegisteredCapability capability(String identifier) {
        return new RegisteredCapability(
                identifier,
                List.of(),
                List.of()
        );
    }

    private static ConfigurationRelease release(
            long version,
            String semanticRegistryVersion,
            Set<String> capabilities
    ) {
        String configurationIdentifier =
                "configuration-grocery-00" + version;
        MerchantConfiguration configuration = new MerchantConfiguration(
                "merchant-grocery",
                configurationIdentifier,
                version,
                semanticRegistryVersion,
                capabilities
        );
        ExecutableMerchantModel model = new ExecutableMerchantModel(
                "merchant-grocery",
                configurationIdentifier,
                version,
                semanticRegistryVersion,
                capabilities,
                List.of(),
                List.of()
        );
        return new ConfigurationRelease(
                "release-grocery-00" + version,
                configuration,
                model,
                "mainstreet-compiler-1",
                GENERATED_AT
        );
    }
}
