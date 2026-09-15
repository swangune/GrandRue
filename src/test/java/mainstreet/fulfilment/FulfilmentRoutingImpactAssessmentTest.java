package mainstreet.fulfilment;

import mainstreet.application.MerchantScope;
import mainstreet.semantic.compiler.ConfigurationCompiler;
import mainstreet.semantic.configuration.ConfigurationImpactClassification;
import mainstreet.semantic.configuration.ConfigurationImpactContext;
import mainstreet.semantic.configuration.ConfigurationPackageResolver;
import mainstreet.semantic.configuration.ConfigurationValidationEvidence;
import mainstreet.semantic.configuration.ConfigurationValidationOutcome;
import mainstreet.semantic.configuration.MerchantConfiguration;
import mainstreet.semantic.registry.InMemorySemanticRegistry;
import mainstreet.semantic.registry.RegisteredCapability;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import mainstreet.semantic.release.InMemorySemanticReleaseAssemblyRepository;
import mainstreet.semantic.release.SemanticReleaseAssembly;
import mainstreet.surface.ExposureElementContractRegistrySnapshot;
import mainstreet.surface.SurfaceContributionRegistrySnapshot;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Authority: MS-PROT-040 v1.0 §§20–24; MS-PROT-040 v1.3 §§9–11;
 * MS-PROT-048 v1.2 §§4–6, 12–18; MS-PROT-048 v1.3 §§1–2, 9–18.
 */
class FulfilmentRoutingImpactAssessmentTest {

    private static final Instant AT = Instant.parse("2026-09-07T19:30:00Z");
    private static final FulfilmentRoleIdentity STORAGE =
            new FulfilmentRoleIdentity("booking", "reservation-storage");
    private static final FulfilmentBindingSetRevisionReference F8 =
            new FulfilmentBindingSetRevisionReference("merchant-routing", 8);
    private static final FulfilmentBindingSetRevisionReference F9 =
            new FulfilmentBindingSetRevisionReference("merchant-routing", 9);

    @Test
    void changed_fulfiller_is_a_consequential_routing_effect() {
        var compiler = compiler(snapshot("r1"));
        var registry = fulfilmentRegistry("r1", Set.of("persist-reservation"));
        var base = configuration("c1", 1, "r1", Optional.empty(), F8);
        var candidate = configuration("c2", 2, "r1", Optional.of("c1"), F9);
        var oldBinding = bindingSet(F8, "r1", "internal-storage-a");
        var newBinding = bindingSet(F9, "r1", "internal-storage-b");

        var result = assessment(compiler, Map.of("r1", registry), Map.of(F8, oldBinding))
                .assess(context(compiler, Optional.of(base), candidate, newBinding, registry));

        assertEquals(List.of(FulfilmentRoutingImpactAssessment.ROUTING_EFFECT),
                result.businessFacingEffects());
        assertEquals(List.of(ConfigurationImpactClassification.CONSEQUENTIAL),
                result.findings().stream().map(finding -> finding.classification()).toList());
    }

    @Test
    void changed_applicable_obligations_are_a_consequential_supporting_service_effect() {
        var compiler = compiler(snapshot("r1"), snapshot("r2"));
        var oldRegistry = fulfilmentRegistry("r1", Set.of("persist-reservation"));
        var newRegistry = fulfilmentRegistry(
                "r2",
                Set.of("persist-reservation", "release-reservation")
        );
        var base = configuration("c1", 1, "r1", Optional.empty(), F8);
        var candidate = configuration("c2", 2, "r2", Optional.of("c1"), F9);
        var oldBinding = bindingSet(F8, "r1", "internal-storage");
        var newBinding = bindingSet(F9, "r2", "internal-storage");

        var result = assessment(compiler, Map.of("r1", oldRegistry), Map.of(F8, oldBinding))
                .assess(context(compiler, Optional.of(base), candidate, newBinding, newRegistry));

        assertEquals(List.of(FulfilmentRoutingImpactAssessment.RESPONSIBILITY_EFFECT),
                result.businessFacingEffects());
    }

    @Test
    void binding_revision_provenance_alone_is_not_a_business_effect() {
        var compiler = compiler(snapshot("r1"));
        var registry = fulfilmentRegistry("r1", Set.of("persist-reservation"));
        var base = configuration("c1", 1, "r1", Optional.empty(), F8);
        var candidate = configuration("c2", 2, "r1", Optional.of("c1"), F9);
        var oldBinding = bindingSet(F8, "r1", "internal-storage");
        var newBinding = bindingSet(F9, "r1", "internal-storage");

        var result = assessment(compiler, Map.of("r1", registry), Map.of(F8, oldBinding))
                .assess(context(compiler, Optional.of(base), candidate, newBinding, registry));

        assertTrue(result.businessFacingEffects().isEmpty());
        assertTrue(result.findings().isEmpty());
    }

    @Test
    void apparently_equal_plan_shape_across_releases_is_not_assumed_equivalent() {
        var compiler = compiler(snapshot("r1"), snapshot("r2"));
        var oldRegistry = fulfilmentRegistry("r1", Set.of("persist-reservation"));
        var newRegistry = fulfilmentRegistry("r2", Set.of("persist-reservation"));
        var base = configuration("c1", 1, "r1", Optional.empty(), F8);
        var candidate = configuration("c2", 2, "r2", Optional.of("c1"), F9);
        var oldBinding = bindingSet(F8, "r1", "internal-storage");
        var newBinding = bindingSet(F9, "r2", "internal-storage");

        assertThrows(IllegalStateException.class, () ->
                assessment(compiler, Map.of("r1", oldRegistry), Map.of(F8, oldBinding))
                        .assess(context(
                                compiler,
                                Optional.of(base),
                                candidate,
                                newBinding,
                                newRegistry
                        )));
    }

    @Test
    void missing_historical_binding_revision_fails_closed() {
        var compiler = compiler(snapshot("r1"));
        var registry = fulfilmentRegistry("r1", Set.of("persist-reservation"));
        var base = configuration("c1", 1, "r1", Optional.empty(), F8);
        var candidate = configuration("c2", 2, "r1", Optional.of("c1"), F9);
        var newBinding = bindingSet(F9, "r1", "internal-storage-b");

        assertThrows(IllegalStateException.class, () ->
                assessment(compiler, Map.of("r1", registry), Map.of())
                        .assess(context(
                                compiler,
                                Optional.of(base),
                                candidate,
                                newBinding,
                                registry
                        )));
    }

    @Test
    void missing_historical_release_assembly_fails_closed() {
        var compiler = compiler(snapshot("r1"));
        var registry = fulfilmentRegistry("r1", Set.of("persist-reservation"));
        var base = configuration("c1", 1, "r1", Optional.empty(), F8);
        var candidate = configuration("c2", 2, "r1", Optional.of("c1"), F9);
        var oldBinding = bindingSet(F8, "r1", "internal-storage-a");
        var newBinding = bindingSet(F9, "r1", "internal-storage-b");

        assertThrows(IllegalStateException.class, () ->
                assessment(compiler, Map.of(), Map.of(F8, oldBinding))
                        .assess(context(
                                compiler,
                                Optional.of(base),
                                candidate,
                                newBinding,
                                registry
                        )));
    }

    @Test
    void first_configuration_with_required_supporting_service_reports_the_effect() {
        var compiler = compiler(snapshot("r1"));
        var registry = fulfilmentRegistry("r1", Set.of("persist-reservation"));
        var candidate = configuration("c1", 1, "r1", Optional.empty(), F8);
        var binding = bindingSet(F8, "r1", "internal-storage");

        var result = assessment(compiler, Map.of(), Map.of())
                .assess(context(compiler, Optional.empty(), candidate, binding, registry));

        assertEquals(List.of(FulfilmentRoutingImpactAssessment.RESPONSIBILITY_EFFECT),
                result.businessFacingEffects());
    }

    private static FulfilmentRoutingImpactAssessment assessment(
            ConfigurationCompiler compiler,
            Map<String, FulfilmentContractRegistrySnapshot> releases,
            Map<FulfilmentBindingSetRevisionReference, FulfilmentBindingSetRevision> bindingSets
    ) {
        var assemblies = releases.entrySet().stream()
                .map(entry -> assembly(entry.getKey(), entry.getValue()))
                .toList();
        FulfilmentBindingSetRevisionAuthority authority = (merchantScope, reference) ->
                Optional.ofNullable(bindingSets.get(reference));
        return new FulfilmentRoutingImpactAssessment(
                compiler,
                new InMemorySemanticReleaseAssemblyRepository(assemblies),
                authority
        );
    }

    private static ConfigurationImpactContext context(
            ConfigurationCompiler compiler,
            Optional<MerchantConfiguration> base,
            MerchantConfiguration candidate,
            FulfilmentBindingSetRevision candidateBinding,
            FulfilmentContractRegistrySnapshot candidateRegistry
    ) {
        var resolved = new ConfigurationPackageResolver(compiler, candidateRegistry)
                .resolve(candidate, candidateBinding, "compiler", AT);
        var validation = new ConfigurationValidationEvidence(
                "validation-1",
                candidate.merchantIdentifier(),
                candidate.configurationIdentifier(),
                candidate.semanticRegistryVersion(),
                "package-1",
                ConfigurationValidationOutcome.SUCCEEDED,
                "compiler",
                AT,
                AT
        );
        return new ConfigurationImpactContext(candidate, base, validation, resolved);
    }

    private static MerchantConfiguration configuration(
            String identifier,
            long version,
            String release,
            Optional<String> baseIdentifier,
            FulfilmentBindingSetRevisionReference bindingReference
    ) {
        return new MerchantConfiguration(
                "merchant-a",
                identifier,
                version,
                release,
                Set.of("booking"),
                Set.of(),
                baseIdentifier,
                Optional.of(bindingReference)
        );
    }

    private static FulfilmentBindingSetRevision bindingSet(
            FulfilmentBindingSetRevisionReference reference,
            String release,
            String fulfillerIdentity
    ) {
        return new FulfilmentBindingSetRevision(
                reference.bindingSetIdentifier(),
                reference.revision(),
                new MerchantScope("merchant-a"),
                release,
                Set.of(new FulfilmentBindingSelection(
                        STORAGE,
                        Optional.empty(),
                        FulfillerKind.INTERNAL,
                        fulfillerIdentity,
                        Optional.empty()
                ))
        );
    }

    private static FulfilmentContractRegistrySnapshot fulfilmentRegistry(
            String release,
            Set<String> requiredObligations
    ) {
        return new FulfilmentContractRegistrySnapshot(
                release,
                Set.of(new FulfilmentRoleDefinition(
                        STORAGE,
                        Set.of("persist-reservation", "release-reservation"),
                        "booking-storage-authority-v1",
                        "booking-storage-evidence-v1",
                        "booking-storage-failure-v1"
                )),
                Set.of(),
                Set.of(new FulfilmentRequirementDefinition(
                        new FulfilmentRequirementIdentity(
                                "booking",
                                "reservation-storage"
                        ),
                        STORAGE,
                        Optional.empty(),
                        requiredObligations,
                        new AlwaysFulfilmentRequirementApplicability()
                ))
        );
    }

    private static SemanticReleaseAssembly assembly(
            String release,
            FulfilmentContractRegistrySnapshot fulfilmentRegistry
    ) {
        return new SemanticReleaseAssembly(
                snapshot(release),
                new SurfaceContributionRegistrySnapshot(release, Set.of()),
                fulfilmentRegistry,
                new ExposureElementContractRegistrySnapshot(release, Set.of())
        );
    }

    private static ConfigurationCompiler compiler(SemanticRegistrySnapshot... snapshots) {
        var registry = new InMemorySemanticRegistry();
        Arrays.stream(snapshots).forEach(registry::publish);
        return new ConfigurationCompiler(registry);
    }

    private static SemanticRegistrySnapshot snapshot(String release) {
        return new SemanticRegistrySnapshot(
                release,
                Set.of(new RegisteredCapability("booking", List.of(), List.of()))
        );
    }
}
