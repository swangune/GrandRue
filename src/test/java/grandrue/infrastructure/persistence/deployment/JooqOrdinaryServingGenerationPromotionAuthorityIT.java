package grandrue.infrastructure.persistence.deployment;

import grandrue.application.MerchantScope;
import grandrue.deployment.InitializeOrdinaryServingAdmissionControlCommand;
import grandrue.deployment.ObservedServingGeneration;
import grandrue.deployment.OrdinaryServingAdmissionLifecycle;
import grandrue.deployment.PrepareServingGenerationPromotionCommand;
import grandrue.deployment.ReconcileServingGenerationPromotionCommand;
import grandrue.deployment.RecordServingDeploymentAdmissionSnapshotCommand;
import grandrue.deployment.ServingGenerationPromotionException;
import grandrue.deployment.ServingGenerationPromotionFailureCategory;
import grandrue.deployment.ServingGenerationPromotionStatus;
import grandrue.semantic.configuration.ConfigurationNewActivityRequirementSet;
import grandrue.semantic.configuration.ConfigurationNewActivityRequirementSetAuthority;
import grandrue.semantic.configuration.RecordConfigurationNewActivityRequirementSetCommand;
import grandrue.semantic.execution.ExecutableSupportManifest;
import grandrue.semantic.execution.SemanticExecutionContractReference;
import grandrue.semantic.release.DeploymentSemanticMaterialisationSet;
import grandrue.semantic.release.PackagedSemanticDefinitionBundle;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JooqOrdinaryServingGenerationPromotionAuthorityIT {
    private static final String RELEASE = "semantic-release-21";
    private static final Instant INITIALIZED = Instant.parse("2026-08-29T18:00:00Z");
    private static final Instant PREPARED = INITIALIZED.plusSeconds(1);
    private static final Instant OBSERVED = PREPARED.plusSeconds(1);

    private DSLContext dsl;
    private PlatformTransactionManager transactionManager;
    private JooqServingDeploymentAdmissionSnapshotAuthority snapshots;

    @BeforeEach
    void setUp() {
        DataSource source = new DriverManagerDataSource(
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_URL"),
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_USER"),
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_PASSWORD"));
        transactionManager = new DataSourceTransactionManager(source);
        Flyway.configure().dataSource(source).locations("classpath:db/migration").load().migrate();
        dsl = DSL.using(new TransactionAwareDataSourceProxy(source), SQLDialect.POSTGRES);
        dsl.execute("truncate table ordinary_serving_admission_control, "
                + "ordinary_serving_generation_promotion, "
                + "merchant_current_configuration_activation, "
                + "configuration_activation, "
                + "serving_deployment_executable_support_contract, "
                + "serving_deployment_executable_support_manifest, "
                + "serving_deployment_materialised_release, "
                + "serving_deployment_admission_snapshot cascade");
        snapshots = new JooqServingDeploymentAdmissionSnapshotAuthority(dsl, transactionManager);
        snapshots.record(snapshot("generation-prior", INITIALIZED.minusSeconds(2)));
        snapshots.record(snapshot("generation-target", INITIALIZED.minusSeconds(1)));
    }

    @Test
    void prepare_is_durable_and_unknown_state_fails_closed_until_target_is_observed() {
        JooqOrdinaryServingGenerationPromotionAuthority authority = authority();
        authority.initialize(new InitializeOrdinaryServingAdmissionControlCommand("generation-prior", INITIALIZED));
        PrepareServingGenerationPromotionCommand prepare = new PrepareServingGenerationPromotionCommand(
                "promotion-1", "generation-prior", "generation-target", PREPARED);

        assertEquals(ServingGenerationPromotionStatus.PROMOTING, authority.prepare(prepare).status());
        assertEquals(authority.prepare(prepare), authority.transition("promotion-1").orElseThrow());
        assertEquals(OrdinaryServingAdmissionLifecycle.PROMOTING, authority.control().orElseThrow().lifecycle());
        assertEquals(1, authority.control().orElseThrow().epoch());

        ServingGenerationPromotionException uncertain = assertThrows(
                ServingGenerationPromotionException.class,
                () -> authority.reconcile(new ReconcileServingGenerationPromotionCommand(
                        "promotion-1", ObservedServingGeneration.unknown(), OBSERVED)));
        assertEquals(ServingGenerationPromotionFailureCategory.RECONCILIATION_REQUIRED, uncertain.category());
        assertEquals(OrdinaryServingAdmissionLifecycle.PROMOTING, authority.control().orElseThrow().lifecycle());

        ReconcileServingGenerationPromotionCommand reconcile = new ReconcileServingGenerationPromotionCommand(
                "promotion-1", ObservedServingGeneration.homogeneous("generation-target"), OBSERVED);
        assertEquals(ServingGenerationPromotionStatus.FINALIZED, authority.reconcile(reconcile).status());
        assertEquals(authority.reconcile(reconcile), authority.transition("promotion-1").orElseThrow());
        assertEquals("generation-target", authority.control().orElseThrow().currentGenerationIdentifier());
        assertEquals(OrdinaryServingAdmissionLifecycle.STABLE, authority.control().orElseThrow().lifecycle());
    }

    @Test
    void homogeneous_prior_generation_safely_aborts_promotion() {
        JooqOrdinaryServingGenerationPromotionAuthority authority = preparedAuthority();
        assertEquals(ServingGenerationPromotionStatus.ABORTED,
                authority.reconcile(new ReconcileServingGenerationPromotionCommand(
                        "promotion-1", ObservedServingGeneration.homogeneous("generation-prior"), OBSERVED)).status());
        assertEquals("generation-prior", authority.control().orElseThrow().currentGenerationIdentifier());
    }

    @Test
    void transition_identity_cannot_be_reused_for_changed_intent() {
        JooqOrdinaryServingGenerationPromotionAuthority authority = preparedAuthority();
        ServingGenerationPromotionException failure = assertThrows(ServingGenerationPromotionException.class,
                () -> authority.prepare(new PrepareServingGenerationPromotionCommand(
                        "promotion-1", "generation-prior", "generation-target", PREPARED.plusSeconds(1))));
        assertEquals(ServingGenerationPromotionFailureCategory.REQUEST_IDENTITY_CONFLICT, failure.category());
    }

    @Test
    void every_current_configuration_requires_exact_requirement_set_evidence() {
        dsl.execute("insert into configuration_activation "
                        + "(activation_request_identifier, merchant_identifier, configuration_revision_identifier, "
                        + "release_identifier, initiating_principal_identifier, activated_at) "
                        + "values (?, ?, ?, ?, ?, ?::timestamptz)",
                "activation-1", "merchant-acme", "configuration-1", RELEASE, "controller-1", INITIALIZED.toString());
        dsl.execute("insert into merchant_current_configuration_activation "
                        + "(merchant_identifier, activation_request_identifier, configuration_revision_identifier, release_identifier) "
                        + "values (?, ?, ?, ?)",
                "merchant-acme", "activation-1", "configuration-1", RELEASE);
        JooqOrdinaryServingGenerationPromotionAuthority authority = authority();
        authority.initialize(new InitializeOrdinaryServingAdmissionControlCommand("generation-prior", INITIALIZED));

        ServingGenerationPromotionException failure = assertThrows(ServingGenerationPromotionException.class,
                () -> authority.prepare(new PrepareServingGenerationPromotionCommand(
                        "promotion-1", "generation-prior", "generation-target", PREPARED)));
        assertEquals(ServingGenerationPromotionFailureCategory.ACTIVE_REQUIREMENT_EVIDENCE_MISSING, failure.category());
        assertEquals(OrdinaryServingAdmissionLifecycle.STABLE, authority.control().orElseThrow().lifecycle());
    }

    private JooqOrdinaryServingGenerationPromotionAuthority preparedAuthority() {
        JooqOrdinaryServingGenerationPromotionAuthority authority = authority();
        authority.initialize(new InitializeOrdinaryServingAdmissionControlCommand("generation-prior", INITIALIZED));
        authority.prepare(new PrepareServingGenerationPromotionCommand(
                "promotion-1", "generation-prior", "generation-target", PREPARED));
        return authority;
    }

    private JooqOrdinaryServingGenerationPromotionAuthority authority() {
        return new JooqOrdinaryServingGenerationPromotionAuthority(
                dsl, transactionManager, snapshots, unusedRequirementAuthority());
    }

    private static ConfigurationNewActivityRequirementSetAuthority unusedRequirementAuthority() {
        return new ConfigurationNewActivityRequirementSetAuthority() {
            @Override
            public ConfigurationNewActivityRequirementSet record(RecordConfigurationNewActivityRequirementSetCommand command) {
                throw new UnsupportedOperationException();
            }
            @Override
            public Optional<ConfigurationNewActivityRequirementSet> evidenceForPackage(
                    MerchantScope merchantScope, String resolvedPackageEvidenceIdentifier) {
                return Optional.empty();
            }
        };
    }

    private static RecordServingDeploymentAdmissionSnapshotCommand snapshot(String generation, Instant at) {
        return new RecordServingDeploymentAdmissionSnapshotCommand(
                generation,
                new DeploymentSemanticMaterialisationSet(List.of(bundle())),
                List.of(new ExecutableSupportManifest("path-1", Set.of(reference("booking.create")))),
                at);
    }

    private static PackagedSemanticDefinitionBundle bundle() {
        byte[] semantic = bytes("semantic:" + RELEASE);
        byte[] surface = bytes("surface:" + RELEASE);
        byte[] fulfilment = bytes("fulfilment:" + RELEASE);
        byte[] exposure = new byte[0];
        String provenance = "publication-2026-08-29";
        return new PackagedSemanticDefinitionBundle(PackagedSemanticDefinitionBundle.CURRENT_FORMAT, RELEASE,
                provenance, PackagedSemanticDefinitionBundle.computeDigest(
                PackagedSemanticDefinitionBundle.CURRENT_FORMAT, RELEASE, provenance,
                semantic, surface, fulfilment, exposure),
                semantic, surface, fulfilment, exposure);
    }

    private static SemanticExecutionContractReference reference(String contract) {
        return new SemanticExecutionContractReference(RELEASE, contract);
    }
    private static byte[] bytes(String value) { return value.getBytes(StandardCharsets.UTF_8); }
    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) throw new IllegalStateException("Required environment variable is missing: " + name);
        return value;
    }
}
