package grandrue.semantic.configuration;

import grandrue.application.MerchantScope;
import grandrue.semantic.compiler.ConfigurationCompiler;
import grandrue.semantic.registry.InMemorySemanticRegistry;
import grandrue.semantic.registry.SemanticRegistrySnapshot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Authority: designs/MS-PROT-040 v1.3 — Configuration Validation, Impact Review & Approval Evidence Amendment.md,
 * §§9–11 exact-result affinity and completed impact analysis;
 * designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md,
 * v1.0 §§20–24 base/candidate comparison, classifications and protected commitments.
 * Assessors here are fixtures; concrete business-effect/commitment coverage is separate R4B work.
 */
class ConfigurationImpactAnalyzerTest {
    private static final MerchantScope SCOPE = new MerchantScope("merchant-a");
    private static final Instant GENERATED = Instant.parse("2026-09-06T11:00:00Z");
    private static final Instant COMPLETED = GENERATED.plusSeconds(2);
    private final ConfigurationRevisionAuthority revisions = mock(ConfigurationRevisionAuthority.class);
    private final ConfigurationValidationEvidenceAuthority validations = mock(ConfigurationValidationEvidenceAuthority.class);
    private final MerchantConfiguration initial = configuration("merchant-a", "c1", "release-1", Optional.empty());

    @BeforeEach
    void setUp() {
        register(initial);
    }

    @Test
    void assembles_initial_analysis_only_after_all_assessments_complete() {
        AtomicInteger calls = new AtomicInteger();
        var finding = new ConfigurationImpactFinding(ConfigurationImpactClassification.INFORMATIONAL,
                "Existing customer commitments remain unchanged.");
        var analyzer = analyzer(context -> {
            assertEquals(initial, context.candidate());
            assertEquals(Optional.empty(), context.base());
            calls.incrementAndGet();
            return new ConfigurationImpactContribution(List.of("No customer services are selected."), List.of());
        }, context -> {
            assertEquals(1, calls.get());
            calls.incrementAndGet();
            return new ConfigurationImpactContribution(List.of(), List.of(finding));
        });

        var result = analyze(analyzer, packageFor(initial, "compiler-1", GENERATED));

        assertEquals(2, calls.get());
        assertEquals(new ConfigurationImpactAnalysisResult("merchant-a", "c1", "release-1", "v1", "p1",
                List.of("No customer services are selected."), List.of(finding), COMPLETED), result);
        verify(validations, never()).recordSuccessfulValidation(any());
        verify(revisions, never()).materialiseInitial(any());
    }

    @Test
    void replacement_assessment_receives_its_exact_historical_base() {
        var candidate = configuration("merchant-a", "c2", "release-2", Optional.of("c1"));
        register(candidate);
        var conflict = new ConfigurationImpactFinding(ConfigurationImpactClassification.EXISTING_COMMITMENT_CONFLICT,
                "An existing appointment falls outside the proposed opening hours; it remains booked.");
        var analyzer = analyzer(context -> {
            assertEquals(Optional.of(initial), context.base());
            assertEquals(candidate, context.candidate());
            return new ConfigurationImpactContribution(List.of("New appointments use the proposed opening hours."),
                    List.of(conflict));
        });

        var result = analyze(analyzer, packageFor(candidate, "compiler-1", GENERATED));

        assertEquals(List.of(conflict), result.findings());
        assertEquals("release-2", result.semanticRegistryReleaseIdentifier());
        assertEquals("release-1", initial.semanticRegistryVersion());
    }

    @Test
    void missing_validation_prevents_every_assessment() {
        when(validations.evidence(SCOPE, "v1")).thenReturn(Optional.empty());
        ConfigurationImpactAssessment assessment = mock(ConfigurationImpactAssessment.class);

        assertThrows(IllegalArgumentException.class,
                () -> analyze(analyzer(assessment), packageFor(initial, "compiler-1", GENERATED)));
        verifyNoInteractions(assessment);
    }

    @Test
    void wrong_package_reference_prevents_every_assessment() {
        ConfigurationImpactAssessment assessment = mock(ConfigurationImpactAssessment.class);

        assertThrows(IllegalArgumentException.class, () -> analyzer(assessment).analyze(SCOPE, "v1", "other-package",
                packageFor(initial, "compiler-1", GENERATED), COMPLETED));
        verifyNoInteractions(assessment);
    }

    @Test
    void rejects_mismatched_package_merchant_revision_release_compiler_or_generation() {
        ConfigurationImpactAssessment assessment = mock(ConfigurationImpactAssessment.class);
        for (var wrong : List.of(
                packageFor(configuration("other-merchant", "c1", "release-1", Optional.empty()), "compiler-1", GENERATED),
                packageFor(configuration("merchant-a", "other-revision", "release-1", Optional.empty()), "compiler-1", GENERATED),
                packageFor(configuration("merchant-a", "c1", "other-release", Optional.empty()), "compiler-1", GENERATED),
                packageFor(initial, "other-compiler", GENERATED),
                packageFor(initial, "compiler-1", GENERATED.plusSeconds(1)))) {
            assertThrows(IllegalArgumentException.class, () -> analyze(analyzer(assessment), wrong));
        }
        verifyNoInteractions(assessment);
    }

    @Test
    void missing_candidate_or_exact_base_prevents_assessment() {
        ConfigurationImpactAssessment assessment = mock(ConfigurationImpactAssessment.class);
        when(revisions.configuration(SCOPE, "c1")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> analyze(analyzer(assessment), packageFor(initial, "compiler-1", GENERATED)));
        var candidate = configuration("merchant-a", "c2", "release-2", Optional.of("c1"));
        register(candidate);
        assertThrows(IllegalArgumentException.class,
                () -> analyze(analyzer(assessment), packageFor(candidate, "compiler-1", GENERATED)));
        verifyNoInteractions(assessment);
    }

    @Test
    void foreign_base_from_an_inconsistent_authority_is_rejected() {
        var candidate = configuration("merchant-a", "c2", "release-2", Optional.of("c1"));
        register(candidate);
        when(revisions.configuration(SCOPE, "c1")).thenReturn(Optional.of(
                configuration("other-merchant", "c1", "release-1", Optional.empty())));
        ConfigurationImpactAssessment assessment = mock(ConfigurationImpactAssessment.class);

        assertThrows(IllegalArgumentException.class,
                () -> analyze(analyzer(assessment), packageFor(candidate, "compiler-1", GENERATED)));
        verifyNoInteractions(assessment);
    }

    @Test
    void incomplete_assessment_cannot_produce_a_completed_result() {
        var failure = new IllegalStateException("Commitment owner is unavailable");
        var analyzer = analyzer(context -> new ConfigurationImpactContribution(List.of("Proposed effects"), List.of()),
                context -> { throw failure; });

        assertSame(failure, assertThrows(IllegalStateException.class,
                () -> analyze(analyzer, packageFor(initial, "compiler-1", GENERATED))));
        assertThrows(NullPointerException.class,
                () -> analyze(analyzer(context -> null), packageFor(initial, "compiler-1", GENERATED)));
    }

    @Test
    void absence_of_assessors_or_business_effects_is_not_a_completed_review() {
        assertThrows(IllegalArgumentException.class, () -> analyzer());
        assertThrows(IllegalArgumentException.class,
                () -> analyze(analyzer(context -> new ConfigurationImpactContribution(List.of(), List.of())),
                        packageFor(initial, "compiler-1", GENERATED)));
    }

    private void register(MerchantConfiguration configuration) {
        when(revisions.configuration(SCOPE, configuration.configurationIdentifier())).thenReturn(Optional.of(configuration));
        when(validations.evidence(SCOPE, "v1")).thenReturn(Optional.of(new ConfigurationValidationEvidence(
                "v1", "merchant-a", configuration.configurationIdentifier(), configuration.semanticRegistryVersion(),
                "p1", ConfigurationValidationOutcome.SUCCEEDED, "compiler-1", GENERATED, GENERATED.plusSeconds(1))));
    }

    private ConfigurationImpactAnalyzer analyzer(ConfigurationImpactAssessment... assessments) {
        return new ConfigurationImpactAnalyzer(revisions, validations, List.of(assessments));
    }

    private static ConfigurationImpactAnalysisResult analyze(ConfigurationImpactAnalyzer analyzer,
                                                            ResolvedConfigurationPackage resolvedPackage) {
        return analyzer.analyze(SCOPE, "v1", "p1", resolvedPackage, COMPLETED);
    }

    private static MerchantConfiguration configuration(String merchant, String id, String release, Optional<String> base) {
        return new MerchantConfiguration(merchant, id, base.isPresent() ? 2 : 1, release,
                Set.of(), Set.of(), base, Optional.empty());
    }

    private static ResolvedConfigurationPackage packageFor(MerchantConfiguration configuration, String compiler, Instant at) {
        var registry = new InMemorySemanticRegistry();
        registry.publish(new SemanticRegistrySnapshot(configuration.semanticRegistryVersion(), Set.of()));
        return new ConfigurationPackageResolver(new ConfigurationCompiler(registry)).resolve(configuration, compiler, at);
    }
}
