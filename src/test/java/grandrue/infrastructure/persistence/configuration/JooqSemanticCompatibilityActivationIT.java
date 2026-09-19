package grandrue.infrastructure.persistence.configuration;

import grandrue.semantic.configuration.ConfigurationActivationRequest;
import grandrue.semantic.configuration.ConfigurationActivationStatus;
import grandrue.semantic.configuration.ConfigurationPublication;
import grandrue.semantic.configuration.ConfigurationRelease;
import grandrue.semantic.configuration.ConfigurationRevisionApproval;
import grandrue.semantic.configuration.InMemoryConfigurationPublication;
import grandrue.semantic.configuration.MerchantConfiguration;
import grandrue.semantic.configuration.SemanticCompatibilityDisposition;
import grandrue.semantic.configuration.SemanticCompatibilityEvidence;
import grandrue.semantic.configuration.SemanticCompatibilityReferenceScope;
import grandrue.semantic.executable.ExecutableMerchantModel;
import grandrue.testing.ConfigurationActivationApprovals;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JooqSemanticCompatibilityActivationIT {

    private static final Instant ACTIVATED_AT =
            Instant.parse("2026-08-25T10:30:00Z");
    private static final String PRINCIPAL = "merchant-controller-1";

    private DataSource authoritativeDataSource;
    private DSLContext dsl;

    @BeforeEach
    void setUp() {
        authoritativeDataSource = new DriverManagerDataSource(
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_URL"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_USER"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_PASSWORD")
        );

        Flyway.configure()
                .dataSource(authoritativeDataSource)
                .locations("classpath:db/migration")
                .load()
                .migrate();

        TransactionAwareDataSourceProxy transactionAwareDataSource =
                new TransactionAwareDataSourceProxy(authoritativeDataSource);
        dsl = DSL.using(transactionAwareDataSource, SQLDialect.POSTGRES);

        clearTables();
    }

    @Test
    void production_activation_fails_closed_across_registry_without_evidence() {
        ConfigurationPublication publication = new InMemoryConfigurationPublication();
        ConfigurationRelease source = release(
                "merchant-compatibility",
                1,
                "semantic-registry-1",
                Optional.empty()
        );
        ConfigurationRelease target = release(
                "merchant-compatibility",
                2,
                "semantic-registry-2",
                Optional.of(source.configurationIdentifier())
        );
        publication.publish(source);
        publication.publish(target);
        JooqConfigurationReleaseActivation activation = activation(publication);

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
        assertEquals(
                source,
                activation.current(source.merchantIdentifier()).orElseThrow().release()
        );
        assertEquals(1, count("configuration_activation"));
    }

    @Test
    void exact_durable_evidence_survives_authority_restart_and_permits_activation() {
        ConfigurationPublication publication = new InMemoryConfigurationPublication();
        ConfigurationRelease source = release(
                "merchant-compatibility",
                1,
                "semantic-registry-1",
                Optional.empty()
        );
        ConfigurationRelease target = release(
                "merchant-compatibility",
                2,
                "semantic-registry-2",
                Optional.of(source.configurationIdentifier())
        );
        publication.publish(source);
        publication.publish(target);

        JooqConfigurationReleaseActivation firstProcess = activation(publication);
        assertEquals(
                ConfigurationActivationStatus.SUCCESS,
                firstProcess.activate(request("activate-source", source, Optional.empty())).status()
        );

        JooqSemanticCompatibilityAuthority writer =
                new JooqSemanticCompatibilityAuthority(dsl);
        writer.record(evidence(
                source,
                target,
                SemanticCompatibilityDisposition.SEMANTICALLY_EQUIVALENT,
                SemanticCompatibilityReferenceScope.forSource(source)
        ));

        JooqSemanticCompatibilityAuthority restartedAuthority =
                new JooqSemanticCompatibilityAuthority(dsl);
        JooqConfigurationReleaseActivation restartedActivation = activation(
                publication,
                restartedAuthority
        );

        assertEquals(
                ConfigurationActivationStatus.SUCCESS,
                restartedActivation.activate(request(
                        "activate-target",
                        target,
                        Optional.of(source.configurationIdentifier())
                )).status()
        );
        assertEquals(
                target,
                restartedActivation.current(target.merchantIdentifier()).orElseThrow().release()
        );
        assertEquals(1, count("semantic_compatibility_evidence"));
        assertEquals(2, count("configuration_activation"));
    }

    @Test
    void durable_evidence_with_wrong_reference_scope_cannot_authorize_transition() {
        ConfigurationPublication publication = new InMemoryConfigurationPublication();
        ConfigurationRelease source = release(
                "merchant-compatibility",
                1,
                "semantic-registry-1",
                Optional.empty()
        );
        ConfigurationRelease target = release(
                "merchant-compatibility",
                2,
                "semantic-registry-2",
                Optional.of(source.configurationIdentifier())
        );
        publication.publish(source);
        publication.publish(target);

        JooqSemanticCompatibilityAuthority compatibility =
                new JooqSemanticCompatibilityAuthority(dsl);
        compatibility.record(evidence(
                source,
                target,
                SemanticCompatibilityDisposition.SEMANTICALLY_EQUIVALENT,
                Set.of("capability:not-source-scope")
        ));
        JooqConfigurationReleaseActivation activation = activation(
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
        assertEquals(
                source,
                activation.current(source.merchantIdentifier()).orElseThrow().release()
        );
    }

    @Test
    void exact_transition_identity_cannot_be_rebound_to_different_evidence() {
        ConfigurationRelease source = release(
                "merchant-compatibility",
                1,
                "semantic-registry-1",
                Optional.empty()
        );
        ConfigurationRelease target = release(
                "merchant-compatibility",
                2,
                "semantic-registry-2",
                Optional.of(source.configurationIdentifier())
        );
        JooqSemanticCompatibilityAuthority authority =
                new JooqSemanticCompatibilityAuthority(dsl);
        SemanticCompatibilityEvidence accepted = evidence(
                source,
                target,
                SemanticCompatibilityDisposition.SEMANTICALLY_EQUIVALENT,
                SemanticCompatibilityReferenceScope.forSource(source)
        );
        authority.record(accepted);
        authority.record(accepted);

        assertThrows(
                IllegalArgumentException.class,
                () -> authority.record(evidence(
                        source,
                        target,
                        SemanticCompatibilityDisposition.INCOMPATIBLE,
                        SemanticCompatibilityReferenceScope.forSource(source)
                ))
        );
        assertEquals(Optional.of(accepted), authority.evidenceFor(
                source.merchantIdentifier(),
                source.releaseIdentifier(),
                target.releaseIdentifier()
        ));
    }

    private JooqConfigurationReleaseActivation activation(
            ConfigurationPublication publication
    ) {
        return new JooqConfigurationReleaseActivation(
                publication,
                approvalAuthority(),
                ConfigurationActivationApprovals.currentInitialApproval(
                        publication,
                        PRINCIPAL,
                        ACTIVATED_AT.minusSeconds(60)
                ),
                (principal, merchant, revision) -> true,
                fixedClock(),
                dsl,
                new DataSourceTransactionManager(authoritativeDataSource)
        );
    }

    private JooqConfigurationReleaseActivation activation(
            ConfigurationPublication publication,
            JooqSemanticCompatibilityAuthority compatibilityAuthority
    ) {
        return new JooqConfigurationReleaseActivation(
                publication,
                approvalAuthority(),
                ConfigurationActivationApprovals.currentInitialApproval(
                        publication,
                        PRINCIPAL,
                        ACTIVATED_AT.minusSeconds(60)
                ),
                (principal, merchant, revision) -> true,
                compatibilityAuthority,
                fixedClock(),
                dsl,
                new DataSourceTransactionManager(authoritativeDataSource)
        );
    }

    private static grandrue.semantic.configuration.ConfigurationRevisionApprovalAuthority
    approvalAuthority() {
        return (merchant, revision) -> Optional.of(
                new ConfigurationRevisionApproval(
                        merchant,
                        revision,
                        PRINCIPAL,
                        ACTIVATED_AT.minusSeconds(60)
                )
        );
    }

    private static SemanticCompatibilityEvidence evidence(
            ConfigurationRelease source,
            ConfigurationRelease target,
            SemanticCompatibilityDisposition disposition,
            Set<String> referenceScope
    ) {
        return new SemanticCompatibilityEvidence(
                source.merchantIdentifier(),
                source.releaseIdentifier(),
                target.releaseIdentifier(),
                source.semanticRegistryVersion(),
                target.semanticRegistryVersion(),
                referenceScope,
                disposition,
                Optional.empty(),
                Optional.empty()
        );
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
                "mainstreet-compiler-1",
                ACTIVATED_AT.minusSeconds(120)
        );
    }

    private int count(String tableName) {
        return dsl.fetchCount(DSL.table(DSL.name(tableName)));
    }

    private void clearTables() {
        dsl.deleteFrom(DSL.table(DSL.name("configuration_activation_publication_intent")))
                .execute();
        dsl.deleteFrom(DSL.table(DSL.name("merchant_current_configuration_activation")))
                .execute();
        dsl.deleteFrom(DSL.table(DSL.name("configuration_activation")))
                .execute();
        dsl.deleteFrom(DSL.table(DSL.name("semantic_compatibility_evidence")))
                .execute();
    }

    private static Clock fixedClock() {
        return Clock.fixed(ACTIVATED_AT, ZoneOffset.UTC);
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing required PostgreSQL integration-test environment variable: " + name
            );
        }
        return value;
    }
}
