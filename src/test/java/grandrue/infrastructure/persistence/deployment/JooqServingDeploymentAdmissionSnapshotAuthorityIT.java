package grandrue.infrastructure.persistence.deployment;

import grandrue.deployment.RecordServingDeploymentAdmissionSnapshotCommand;
import grandrue.deployment.ServingDeploymentAdmissionSnapshot;
import grandrue.deployment.ServingDeploymentAdmissionSnapshotFailureCategory;
import grandrue.deployment.ServingDeploymentAdmissionSnapshotPersistenceException;
import grandrue.semantic.execution.ExecutableSupportManifest;
import grandrue.semantic.execution.ExecutableSupportRequirement;
import grandrue.semantic.execution.SemanticExecutionContractReference;
import grandrue.semantic.release.DeploymentSemanticMaterialisationSet;
import grandrue.semantic.release.PackagedSemanticDefinitionBundle;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqServingDeploymentAdmissionSnapshotAuthorityIT {

    private static final String RELEASE = "semantic-release-21";
    private static final Instant RECORDED_AT =
            Instant.parse("2026-08-29T18:00:00Z");

    private DSLContext dsl;
    private PlatformTransactionManager transactionManager;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new DriverManagerDataSource(
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_URL"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_USER"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_PASSWORD")
        );
        transactionManager = new DataSourceTransactionManager(dataSource);
        Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load()
                .migrate();
        dsl = DSL.using(
                new TransactionAwareDataSourceProxy(dataSource),
                SQLDialect.POSTGRES
        );
        dsl.execute(
                "truncate table "
                        + "serving_deployment_executable_support_contract, "
                        + "serving_deployment_executable_support_manifest, "
                        + "serving_deployment_materialised_release, "
                        + "serving_deployment_admission_snapshot cascade"
        );
    }

    @Test
    void records_and_reconstructs_exact_normalized_generation_evidence() {
        JooqServingDeploymentAdmissionSnapshotAuthority authority = authority();

        ServingDeploymentAdmissionSnapshot snapshot = authority.record(command(
                "generation-42",
                RECORDED_AT
        ));

        String digest = bundle(RELEASE).contentDigest();
        assertTrue(snapshot.materialises(RELEASE, digest));
        assertEquals(Set.of(), snapshot.uncoveredRequirements(Set.of(
                new ExecutableSupportRequirement(
                        reference("booking.create"),
                        Set.of(reference("notification.intent"))
                )
        )));
        assertEquals(
                snapshot,
                authority.snapshot("generation-42").orElseThrow()
        );
        assertEquals(1, count("serving_deployment_admission_snapshot"));
        assertEquals(1, count("serving_deployment_materialised_release"));
        assertEquals(1, count(
                "serving_deployment_executable_support_manifest"
        ));
        assertEquals(2, count(
                "serving_deployment_executable_support_contract"
        ));
    }

    @Test
    void exact_retry_returns_the_original_snapshot() {
        JooqServingDeploymentAdmissionSnapshotAuthority authority = authority();
        RecordServingDeploymentAdmissionSnapshotCommand command = command(
                "generation-42",
                RECORDED_AT
        );

        assertEquals(authority.record(command), authority.record(command));
        assertEquals(1, count("serving_deployment_admission_snapshot"));
    }

    @Test
    void changed_intent_cannot_reuse_a_generation_identity() {
        JooqServingDeploymentAdmissionSnapshotAuthority authority = authority();
        authority.record(command("generation-42", RECORDED_AT));

        ServingDeploymentAdmissionSnapshotPersistenceException failure =
                assertThrows(
                        ServingDeploymentAdmissionSnapshotPersistenceException.class,
                        () -> authority.record(command(
                                "generation-42",
                                RECORDED_AT.plusSeconds(1)
                        ))
                );

        assertEquals(
                ServingDeploymentAdmissionSnapshotFailureCategory
                        .GENERATION_IDENTITY_CONFLICT,
                failure.category()
        );
    }

    @Test
    void database_rejects_contract_evidence_without_its_exact_manifest() {
        authority().record(command("generation-42", RECORDED_AT));

        assertThrows(
                DataAccessException.class,
                () -> dsl.execute(
                        "insert into "
                                + "serving_deployment_executable_support_contract "
                                + "(generation_identifier, "
                                + "implementation_path_identifier, "
                                + "semantic_registry_release_identifier, "
                                + "contract_identifier) values (?, ?, ?, ?)",
                        "generation-42",
                        "absent-path",
                        RELEASE,
                        "booking.create"
                )
        );
    }

    private JooqServingDeploymentAdmissionSnapshotAuthority authority() {
        return new JooqServingDeploymentAdmissionSnapshotAuthority(
                dsl,
                transactionManager
        );
    }

    private static RecordServingDeploymentAdmissionSnapshotCommand command(
            String generationIdentifier,
            Instant recordedAt
    ) {
        return new RecordServingDeploymentAdmissionSnapshotCommand(
                generationIdentifier,
                new DeploymentSemanticMaterialisationSet(List.of(
                        bundle(RELEASE)
                )),
                List.of(new ExecutableSupportManifest(
                        "booking-path",
                        Set.of(
                                reference("booking.create"),
                                reference("notification.intent")
                        )
                )),
                recordedAt
        );
    }

    private static PackagedSemanticDefinitionBundle bundle(String release) {
        byte[] semantic = bytes("semantic:" + release);
        byte[] surface = bytes("surface:" + release);
        byte[] fulfilment = bytes("fulfilment:" + release);
        byte[] exposure = new byte[0];
        String provenance = "publication-2026-08-29";
        String digest = PackagedSemanticDefinitionBundle.computeDigest(
                PackagedSemanticDefinitionBundle.CURRENT_FORMAT,
                release,
                provenance,
                semantic,
                surface,
                fulfilment,
                exposure
        );
        return new PackagedSemanticDefinitionBundle(
                PackagedSemanticDefinitionBundle.CURRENT_FORMAT,
                release,
                provenance,
                digest,
                semantic,
                surface,
                fulfilment,
                exposure
        );
    }

    private static SemanticExecutionContractReference reference(
            String contract
    ) {
        return new SemanticExecutionContractReference(RELEASE, contract);
    }

    private static byte[] bytes(String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }

    private int count(String table) {
        return dsl.fetchCount(DSL.table(DSL.name(table)));
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Required environment variable is missing: " + name
            );
        }
        return value;
    }
}
