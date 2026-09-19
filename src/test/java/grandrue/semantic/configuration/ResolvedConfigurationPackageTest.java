package grandrue.semantic.configuration;

import grandrue.semantic.compiler.ConfigurationCompiler;
import grandrue.semantic.executable.ExecutableMerchantModel;
import grandrue.semantic.registry.InMemorySemanticRegistry;
import grandrue.semantic.registry.OwnedOperationalObjectDefinition;
import grandrue.semantic.registry.OwnedOperationDefinition;
import grandrue.semantic.registry.RegisteredCapability;
import grandrue.semantic.registry.SemanticRegistrySnapshot;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResolvedConfigurationPackageTest {

    private static final Instant GENERATED_AT =
            Instant.parse("2026-08-25T14:20:00Z");

    @Test
    void resolver_produces_one_exact_immutable_configuration_package() {
        MerchantConfiguration configuration = configuration("merchant-a");
        ConfigurationPackageResolver resolver = new ConfigurationPackageResolver(
                new ConfigurationCompiler(registry())
        );

        ResolvedConfigurationPackage resolved = resolver.resolve(
                configuration,
                "mainstreet-compiler-1",
                GENERATED_AT
        );

        assertEquals("merchant-a", resolved.merchantIdentifier());
        assertEquals(
                "configuration-1",
                resolved.sourceConfigurationRevisionIdentifier()
        );
        assertEquals(
                "semantic-registry-1.0",
                resolved.semanticRegistryReleaseIdentifier()
        );
        assertEquals(
                resolved.executableSemanticModel().policies(),
                resolved.resolvedConfiguration().policyValues()
        );
        assertTrue(resolved.fulfilmentPlan().bindings().isEmpty());
        assertTrue(
                resolved.staticSurfaceContributionCatalogue()
                        .contributions()
                        .isEmpty()
        );
        assertEquals(
                "mainstreet-compiler-1",
                resolved.provenance().compilerIdentifier()
        );
        assertEquals(GENERATED_AT, resolved.provenance().generatedAt());
    }

    @Test
    void configuration_release_binds_to_the_exact_resolved_package() {
        MerchantConfiguration configuration = configuration("merchant-a");
        ResolvedConfigurationPackage resolved =
                new ConfigurationPackageResolver(
                        new ConfigurationCompiler(registry())
                ).resolve(
                        configuration,
                        "mainstreet-compiler-1",
                        GENERATED_AT
                );

        ConfigurationRelease release = new ConfigurationRelease(
                "release-1",
                configuration,
                resolved
        );

        assertSame(resolved, release.resolvedPackage());
        assertSame(resolved.executableSemanticModel(), release.executableModel());
        assertEquals("mainstreet-compiler-1", release.compilerIdentifier());
        assertEquals(GENERATED_AT, release.generatedAt());
    }

    @Test
    void package_rejects_mismatched_configuration_provenance() {
        MerchantConfiguration source = configuration("merchant-a");
        ExecutableMerchantModel wrongMerchantModel =
                new ConfigurationCompiler(registry()).compile(
                        configuration("merchant-b")
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> ResolvedConfigurationPackage.currentFoundation(
                        source,
                        wrongMerchantModel,
                        new ResolvedConfigurationProvenance(
                                "mainstreet-compiler-1",
                                GENERATED_AT
                        )
                )
        );
    }

    private static MerchantConfiguration configuration(String merchantIdentifier) {
        return new MerchantConfiguration(
                merchantIdentifier,
                "configuration-1",
                1,
                "semantic-registry-1.0",
                Set.of("booking")
        );
    }

    private static InMemorySemanticRegistry registry() {
        InMemorySemanticRegistry registry = new InMemorySemanticRegistry();
        registry.publish(new SemanticRegistrySnapshot(
                "semantic-registry-1.0",
                Set.of(new RegisteredCapability(
                        "booking",
                        List.of(new OwnedOperationalObjectDefinition(
                                "booking",
                                Set.of("requested", "confirmed"),
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
        return registry;
    }
}
