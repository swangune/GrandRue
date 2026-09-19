package grandrue.semantic.configuration;

import org.junit.jupiter.api.Test;
import grandrue.semantic.executable.ExecutableMerchantModel;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigurationPublicationTest {

    private static final Instant GENERATED_AT =
            Instant.parse("2026-08-20T10:16:00Z");

    @Test
    void publishes_immutable_versions_for_different_business_domains() {
        ConfigurationPublication publication =
                new InMemoryConfigurationPublication();
        Set<String> groceryCapabilities = new HashSet<>(Set.of(
                "commerce-order",
                "inventory"
        ));
        ConfigurationRelease groceryV1 = release(
                "merchant-grocery",
                "configuration-grocery-001",
                1,
                "semantic-registry-1.0",
                groceryCapabilities
        );

        publication.publish(groceryV1);
        groceryCapabilities.add("delivery");

        ConfigurationRelease groceryV2 = release(
                "merchant-grocery",
                "configuration-grocery-002",
                2,
                "semantic-registry-1.1",
                Set.of("commerce-order", "inventory", "delivery")
        );
        ConfigurationRelease engineerV1 = release(
                "merchant-engineer",
                "configuration-engineer-001",
                1,
                "semantic-registry-1.0",
                Set.of("professional-consultation", "documents")
        );
        publication.publish(groceryV2);
        publication.publish(engineerV1);

        assertEquals(
                Set.of("commerce-order", "inventory"),
                groceryV1.configuration().capabilityIdentifiers()
        );
        assertEquals(
                groceryV2,
                publication.latest("merchant-grocery").orElseThrow()
        );
        assertEquals(
                groceryV1,
                publication.version("merchant-grocery", 1)
                        .orElseThrow()
        );
        assertEquals(
                "semantic-registry-1.0",
                publication.version("merchant-grocery", 1)
                        .orElseThrow()
                        .semanticRegistryVersion()
        );
        assertEquals(
                "semantic-registry-1.1",
                publication.latest("merchant-grocery")
                        .orElseThrow()
                        .semanticRegistryVersion()
        );
        assertEquals(
                engineerV1,
                publication.latest("merchant-engineer").orElseThrow()
        );
    }

    @Test
    void rejected_version_leaves_current_publication_unchanged() {
        ConfigurationPublication publication =
                new InMemoryConfigurationPublication();
        ConfigurationRelease current = release(
                "merchant-grocery",
                "configuration-grocery-001",
                1,
                "semantic-registry-1.0",
                Set.of("commerce-order")
        );
        publication.publish(current);

        ConfigurationRelease skippedVersion = release(
                "merchant-grocery",
                "configuration-grocery-003",
                3,
                "semantic-registry-1.1",
                Set.of("commerce-order", "delivery")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> publication.publish(skippedVersion)
        );
        assertEquals(
                current,
                publication.latest("merchant-grocery").orElseThrow()
        );
    }

    private static ConfigurationRelease release(
            String merchantIdentifier,
            String configurationIdentifier,
            long version,
            String semanticRegistryVersion,
            Set<String> capabilityIdentifiers
    ) {
        MerchantConfiguration configuration = new MerchantConfiguration(
                merchantIdentifier,
                configurationIdentifier,
                version,
                semanticRegistryVersion,
                capabilityIdentifiers
        );
        ExecutableMerchantModel model = new ExecutableMerchantModel(
                merchantIdentifier,
                configurationIdentifier,
                version,
                semanticRegistryVersion,
                capabilityIdentifiers,
                List.of(),
                List.of()
        );
        return new ConfigurationRelease(
                "release-" + configurationIdentifier,
                configuration,
                model,
                "grandrue-compiler-1",
                GENERATED_AT
        );
    }
}
