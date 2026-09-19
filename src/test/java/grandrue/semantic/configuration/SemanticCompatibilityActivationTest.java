package grandrue.semantic.configuration;

import grandrue.semantic.executable.ExecutableMerchantModel;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SemanticCompatibilityActivationTest {

    private static final Instant ACTIVATED_AT =
            Instant.parse("2026-08-25T09:30:00Z");
    private static final String PRINCIPAL = "merchant-controller-1";

    @Test
    void cross_registry_transition_without_compatibility_evidence_fails_closed() {
        ConfigurationPublication publication = new InMemoryConfigurationPublication();
        ConfigurationRelease source = release(
                "merchant-a",
                1,
                "semantic-registry-1",
                Optional.empty()
        );
        ConfigurationRelease target = release(
                "merchant-a",
                2,
                "semantic-registry-2",
                Optional.of(source.configurationIdentifier())
        );
        publication.publish(source);
        publication.publish(target);
        InMemorySemanticCompatibilityAuthority compatibility =
                new InMemorySemanticCompatibilityAuthority();
        ConfigurationReleaseActivation activation = allowedActivation(
                publication,
                compatibility
        );

        assertEquals(
                ConfigurationActivationStatus.SUCCESS,
                activation.activate(request("activate-source", source, Optional.empty())).status()
        );

        assertEquals(
                ConfigurationActivationStatus.VALIDATION_REJECTION,
                activation.activate(request(
                        "activate-target",
                        target,
                        Optional.of(source.configurationIdentifier())
                )).status()
        );
        assertSame(
                source,
                activation.current("merchant-a").orElseThrow().release()
        );
    }

    @Test
    void compatibility_evidence_is_bound_to_exact_reference_scope() {
        ConfigurationPublication publication = new InMemoryConfigurationPublication();
        ConfigurationRelease source = release(
                "merchant-a",
                1,
                "semantic-registry-1",
                Optional.empty()
        );
        ConfigurationRelease target = release(
                "merchant-a",
                2,
                "semantic-registry-2",
                Optional.of(source.configurationIdentifier())
        );
        publication.publish(source);
        publication.publish(target);
        InMemorySemanticCompatibilityAuthority compatibility =
                new InMemorySemanticCompatibilityAuthority();
        compatibility.record(new SemanticCompatibilityEvidence(
                source.merchantIdentifier(),
                source.releaseIdentifier(),
                target.releaseIdentifier(),
                source.semanticRegistryVersion(),
                target.semanticRegistryVersion(),
                Set.of("capability:not-the-source-scope"),
                SemanticCompatibilityDisposition.SEMANTICALLY_EQUIVALENT,
                Optional.empty(),
                Optional.empty()
        ));
        ConfigurationReleaseActivation activation = allowedActivation(
                publication,
                compatibility
        );
        activation.activate(request("activate-source", source, Optional.empty()));

        assertEquals(
                ConfigurationActivationStatus.VALIDATION_REJECTION,
                activation.activate(request(
                        "activate-target",
                        target,
                        Optional.of(source.configurationIdentifier())
                )).status()
        );
        assertSame(
                source,
                activation.current("merchant-a").orElseThrow().release()
        );
    }

    @Test
    void exact_semantically_equivalent_evidence_allows_ordinary_activation() {
        ConfigurationPublication publication = new InMemoryConfigurationPublication();
        ConfigurationRelease source = release(
                "merchant-a",
                1,
                "semantic-registry-1",
                Optional.empty()
        );
        ConfigurationRelease target = release(
                "merchant-a",
                2,
                "semantic-registry-2",
                Optional.of(source.configurationIdentifier())
        );
        publication.publish(source);
        publication.publish(target);
        InMemorySemanticCompatibilityAuthority compatibility =
                new InMemorySemanticCompatibilityAuthority();
        compatibility.record(evidence(
                source,
                target,
                SemanticCompatibilityDisposition.SEMANTICALLY_EQUIVALENT,
                Optional.empty(),
                Optional.empty()
        ));
        ConfigurationReleaseActivation activation = allowedActivation(
                publication,
                compatibility
        );
        activation.activate(request("activate-source", source, Optional.empty()));

        assertEquals(
                ConfigurationActivationStatus.SUCCESS,
                activation.activate(request(
                        "activate-target",
                        target,
                        Optional.of(source.configurationIdentifier())
                )).status()
        );
        assertSame(
                target,
                activation.current("merchant-a").orElseThrow().release()
        );
    }

    @Test
    void incompatible_evidence_cannot_authorize_activation() {
        ConfigurationPublication publication = new InMemoryConfigurationPublication();
        ConfigurationRelease source = release(
                "merchant-a",
                1,
                "semantic-registry-1",
                Optional.empty()
        );
        ConfigurationRelease target = release(
                "merchant-a",
                2,
                "semantic-registry-2",
                Optional.of(source.configurationIdentifier())
        );
        publication.publish(source);
        publication.publish(target);
        InMemorySemanticCompatibilityAuthority compatibility =
                new InMemorySemanticCompatibilityAuthority();
        compatibility.record(evidence(
                source,
                target,
                SemanticCompatibilityDisposition.INCOMPATIBLE,
                Optional.empty(),
                Optional.empty()
        ));
        ConfigurationReleaseActivation activation = allowedActivation(
                publication,
                compatibility
        );
        activation.activate(request("activate-source", source, Optional.empty()));

        assertEquals(
                ConfigurationActivationStatus.VALIDATION_REJECTION,
                activation.activate(request(
                        "activate-target",
                        target,
                        Optional.of(source.configurationIdentifier())
                )).status()
        );
        assertSame(
                source,
                activation.current("merchant-a").orElseThrow().release()
        );
    }

    @Test
    void migration_dispositions_require_their_governing_evidence() {
        ConfigurationRelease source = release(
                "merchant-a",
                1,
                "semantic-registry-1",
                Optional.empty()
        );
        ConfigurationRelease target = release(
                "merchant-a",
                2,
                "semantic-registry-2",
                Optional.of(source.configurationIdentifier())
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> evidence(
                        source,
                        target,
                        SemanticCompatibilityDisposition.MIGRATABLE_PRESERVING_INTENT,
                        Optional.empty(),
                        Optional.empty()
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> evidence(
                        source,
                        target,
                        SemanticCompatibilityDisposition.MIGRATION_REQUIRES_DECISION,
                        Optional.of("migration-evidence-1"),
                        Optional.empty()
                )
        );
    }

    private static SemanticCompatibilityEvidence evidence(
            ConfigurationRelease source,
            ConfigurationRelease target,
            SemanticCompatibilityDisposition disposition,
            Optional<String> migrationEvidenceIdentifier,
            Optional<String> decisionEvidenceIdentifier
    ) {
        return new SemanticCompatibilityEvidence(
                source.merchantIdentifier(),
                source.releaseIdentifier(),
                target.releaseIdentifier(),
                source.semanticRegistryVersion(),
                target.semanticRegistryVersion(),
                SemanticCompatibilityReferenceScope.forSource(source),
                disposition,
                migrationEvidenceIdentifier,
                decisionEvidenceIdentifier
        );
    }

    private static ConfigurationReleaseActivation allowedActivation(
            ConfigurationPublication publication,
            SemanticCompatibilityAuthority compatibilityAuthority
    ) {
        return new InMemoryConfigurationReleaseActivation(
                publication,
                approvalAuthority(),
                (principal, merchant, revision) -> true,
                compatibilityAuthority,
                fixedClock()
        );
    }

    private static ConfigurationRevisionApprovalAuthority approvalAuthority() {
        return (merchant, revision) -> Optional.of(
                new ConfigurationRevisionApproval(
                        merchant,
                        revision,
                        PRINCIPAL,
                        ACTIVATED_AT.minusSeconds(60)
                )
        );
    }

    private static Clock fixedClock() {
        return Clock.fixed(ACTIVATED_AT, ZoneOffset.UTC);
    }

    private static ConfigurationActivationRequest request(
            String requestIdentifier,
            ConfigurationRelease release,
            Optional<String> expectedCurrent
    ) {
        return new ConfigurationActivationRequest(
                requestIdentifier,
                release.releaseIdentifier(),
                expectedCurrent,
                PRINCIPAL
        );
    }

    private static ConfigurationRelease release(
            String merchantIdentifier,
            long version,
            String semanticRegistryVersion,
            Optional<String> baseConfigurationIdentifier
    ) {
        MerchantConfiguration configuration = new MerchantConfiguration(
                merchantIdentifier,
                "configuration-" + version,
                version,
                semanticRegistryVersion,
                Set.of("booking"),
                Set.of(),
                baseConfigurationIdentifier
        );
        return new ConfigurationRelease(
                "release-" + merchantIdentifier + "-" + version,
                configuration,
                new ExecutableMerchantModel(
                        merchantIdentifier,
                        configuration.configurationIdentifier(),
                        version,
                        semanticRegistryVersion,
                        Set.of("booking"),
                        List.of(),
                        List.of()
                ),
                "grandrue-compiler-1",
                ACTIVATED_AT.minusSeconds(120)
        );
    }
}
