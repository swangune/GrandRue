package grandrue.infrastructure.persistence.configuration;

import grandrue.application.MerchantScope;
import grandrue.infrastructure.persistence.onboarding.JooqOnboardingCaseEvidenceStore;
import grandrue.onboarding.InitialConfigurationIntentIdentity;
import grandrue.onboarding.OnboardingCaseRevision;
import grandrue.semantic.compiler.ConfigurationCompiler;
import grandrue.semantic.configuration.ConfigurationNewActivityRequirementSet;
import grandrue.semantic.configuration.ConfigurationNewActivityRequirementSetFailureCategory;
import grandrue.semantic.configuration.ConfigurationNewActivityRequirementSetPersistenceException;
import grandrue.semantic.configuration.ConfigurationOperationExecutionRequirement;
import grandrue.semantic.configuration.ConfigurationPackageResolver;
import grandrue.semantic.configuration.MaterialiseInitialConfigurationRevisionCommand;
import grandrue.semantic.configuration.MerchantConfiguration;
import grandrue.semantic.configuration.OrdinaryNewConfigurationSemanticReleaseAuthority;
import grandrue.semantic.configuration.RecordConfigurationNewActivityRequirementSetCommand;
import grandrue.semantic.configuration.RecordConfigurationValidationEvidenceCommand;
import grandrue.semantic.configuration.RegisteredConfigurationNewActivityRequirementResolver;
import grandrue.semantic.configuration.ResolvedConfigurationPackage;
import grandrue.semantic.execution.ExecutableSupportRequirement;
import grandrue.semantic.execution.SemanticExecutionContractReference;
import grandrue.semantic.registry.InMemorySemanticRegistry;
import grandrue.semantic.registry.OwnedOperationDefinition;
import grandrue.semantic.registry.OwnedOperationalObjectDefinition;
import grandrue.semantic.registry.RegisteredCapability;
import grandrue.semantic.registry.SemanticRegistrySnapshot;
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
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JooqConfigurationNewActivityRequirementSetAuthorityIT {

    private static final String RELEASE = "semantic-release-21";
    private static final Instant PACKAGE_GENERATED_AT =
            Instant.parse("2026-08-29T11:00:00Z");
    private static final Instant EVIDENCE_PRODUCED_AT =
            Instant.parse("2026-08-29T11:00:02Z");

    private DSLContext dsl;
    private PlatformTransactionManager transactionManager;
    private ResolvedConfigurationPackage resolvedPackage;

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
                "truncate table configuration_new_activity_required_contract, "
                        + "configuration_new_activity_requirement, "
                        + "configuration_new_activity_requirement_set, "
                        + "configuration_revision_approval, "
                        + "configuration_impact_review_finding, "
                        + "configuration_impact_review_effect, "
                        + "configuration_impact_review_evidence, "
                        + "configuration_validation_evidence, "
                        + "merchant_configuration_revision_policy, "
                        + "merchant_configuration_revision_capability, "
                        + "merchant_configuration_revision, "
                        + "initial_configuration_intent_unresolved_prompt, "
                        + "initial_configuration_intent_provenance, "
                        + "initial_configuration_intent_semantic_seed, "
                        + "initial_configuration_intent, "
                        + "onboarding_effective_answer, "
                        + "onboarding_answer_evidence, "
                        + "onboarding_case_revision, "
                        + "onboarding_start_request, onboarding_case cascade"
        );
        seedIntent();
        MerchantConfiguration configuration = revisionAuthority()
                .materialiseInitial(
                        new MaterialiseInitialConfigurationRevisionCommand(
                                new InitialConfigurationIntentIdentity("intent-1"),
                                "configuration-1",
                                new OnboardingCaseRevision(
                                        "completed-configuration-1"
                                ),
                                "configuration-bootstrap",
                                Optional.of("onboarding-handoff"),
                                EVIDENCE_PRODUCED_AT
                        )
                )
                .configuration();
        resolvedPackage = resolvedPackage(configuration);
    }

    @Test
    void records_and_reconstructs_normalized_exact_requirements() {
        retainValidation("resolved-package-1");
        JooqConfigurationNewActivityRequirementSetAuthority authority =
                authority();

        ConfigurationNewActivityRequirementSet evidence = authority.record(
                command("resolved-package-1", EVIDENCE_PRODUCED_AT)
        );

        assertEquals("merchant-acme", evidence.merchantIdentifier());
        assertEquals("configuration-1", evidence.configurationRevisionIdentifier());
        assertEquals(RELEASE, evidence.semanticRegistryReleaseIdentifier());
        assertEquals(1, evidence.canonicalizationVersion());
        assertEquals(requirement(), evidence.requirements().iterator().next());
        assertEquals(
                evidence,
                authority.evidenceForPackage(
                        new MerchantScope("merchant-acme"),
                        "resolved-package-1"
                ).orElseThrow()
        );
        assertEquals(1, count("configuration_new_activity_requirement_set"));
        assertEquals(1, count("configuration_new_activity_requirement"));
        assertEquals(2, count("configuration_new_activity_required_contract"));
    }

    @Test
    void exact_retry_returns_the_original_immutable_evidence() {
        retainValidation("resolved-package-1");
        JooqConfigurationNewActivityRequirementSetAuthority authority =
                authority();
        RecordConfigurationNewActivityRequirementSetCommand command =
                command("resolved-package-1", EVIDENCE_PRODUCED_AT);

        assertEquals(authority.record(command), authority.record(command));
        assertEquals(1, count("configuration_new_activity_requirement_set"));
        assertEquals(1, count("configuration_new_activity_requirement"));
        assertEquals(2, count("configuration_new_activity_required_contract"));
    }

    @Test
    void rejects_an_rcp_without_exact_validation_package_evidence() {
        ConfigurationNewActivityRequirementSetPersistenceException failure =
                assertThrows(
                        ConfigurationNewActivityRequirementSetPersistenceException.class,
                        () -> authority().record(command(
                                "unvalidated-package",
                                EVIDENCE_PRODUCED_AT
                        ))
                );

        assertEquals(
                ConfigurationNewActivityRequirementSetFailureCategory
                        .VALIDATION_PACKAGE_EVIDENCE_NOT_FOUND,
                failure.category()
        );
    }

    @Test
    void rejects_reuse_of_package_evidence_for_changed_immutable_intent() {
        retainValidation("resolved-package-1");
        JooqConfigurationNewActivityRequirementSetAuthority authority =
                authority();
        authority.record(command("resolved-package-1", EVIDENCE_PRODUCED_AT));

        ConfigurationNewActivityRequirementSetPersistenceException failure =
                assertThrows(
                        ConfigurationNewActivityRequirementSetPersistenceException.class,
                        () -> authority.record(command(
                                "resolved-package-1",
                                EVIDENCE_PRODUCED_AT.plusSeconds(1)
                        ))
                );

        assertEquals(
                ConfigurationNewActivityRequirementSetFailureCategory
                        .RESOLVED_PACKAGE_EVIDENCE_IDENTITY_CONFLICT,
                failure.category()
        );
    }

    private JooqConfigurationNewActivityRequirementSetAuthority authority() {
        return new JooqConfigurationNewActivityRequirementSetAuthority(
                dsl,
                transactionManager,
                new RegisteredConfigurationNewActivityRequirementResolver(
                        List.of(new ConfigurationOperationExecutionRequirement(
                                RELEASE,
                                "booking.create",
                                requirement()
                        ))
                )
        );
    }

    private void retainValidation(String packageIdentifier) {
        new JooqConfigurationValidationEvidenceAuthority(
                dsl,
                transactionManager,
                revisionAuthority()
        ).recordSuccessfulValidation(
                new RecordConfigurationValidationEvidenceCommand(
                        "validation-1",
                        packageIdentifier,
                        resolvedPackage,
                        EVIDENCE_PRODUCED_AT.minusSeconds(1)
                )
        );
    }

    private RecordConfigurationNewActivityRequirementSetCommand command(
            String packageIdentifier,
            Instant producedAt
    ) {
        return new RecordConfigurationNewActivityRequirementSetCommand(
                packageIdentifier,
                resolvedPackage,
                producedAt
        );
    }

    private static ExecutableSupportRequirement requirement() {
        return new ExecutableSupportRequirement(
                reference("booking.create"),
                Set.of(
                        reference("booking.object"),
                        reference("notification.intent")
                )
        );
    }

    private static SemanticExecutionContractReference reference(String contract) {
        return new SemanticExecutionContractReference(RELEASE, contract);
    }

    private int count(String table) {
        return dsl.fetchCount(DSL.table(DSL.name(table)));
    }

    private JooqConfigurationRevisionAuthority revisionAuthority() {
        OrdinaryNewConfigurationSemanticReleaseAuthority releaseAuthority =
                () -> RELEASE;
        return new JooqConfigurationRevisionAuthority(
                dsl,
                transactionManager,
                new JooqOnboardingCaseEvidenceStore(dsl, transactionManager),
                releaseAuthority
        );
    }

    private static ResolvedConfigurationPackage resolvedPackage(
            MerchantConfiguration source
    ) {
        InMemorySemanticRegistry registry = new InMemorySemanticRegistry();
        registry.publish(new SemanticRegistrySnapshot(
                source.semanticRegistryVersion(),
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
        ).resolve(source, "grandrue-compiler-1", PACKAGE_GENERATED_AT);
    }

    private void seedIntent() {
        dsl.execute(
                "insert into merchant_account (merchant_identifier) "
                        + "values (?) on conflict do nothing",
                "merchant-acme"
        );
        dsl.execute(
                "insert into onboarding_case "
                        + "(onboarding_case_identity, merchant_identifier, purpose, "
                        + "lifecycle, current_revision, started_at) values "
                        + "('case-1', 'merchant-acme', 'INITIAL_CONFIGURATION', "
                        + "'SUBMITTED', 'submitted-revision', cast(? as timestamptz))",
                EVIDENCE_PRODUCED_AT.toString()
        );
        dsl.execute(
                "insert into onboarding_case_revision "
                        + "(onboarding_case_identity, revision_identity, "
                        + "prior_revision_identity, mutation_request_identifier, "
                        + "mutation_kind, resulting_lifecycle, principal_reference, "
                        + "origin_identifier, committed_at) values "
                        + "('case-1', 'reviewed-revision', null, 'start-intent-1', "
                        + "'START', 'IN_PROGRESS', 'identity-42', "
                        + "'privileged-browser', cast(? as timestamptz)), "
                        + "('case-1', 'submitted-revision', 'reviewed-revision', "
                        + "'submit-intent-1', 'SUBMIT', 'SUBMITTED', 'identity-42', "
                        + "'privileged-browser', cast(? as timestamptz))",
                EVIDENCE_PRODUCED_AT.toString(),
                EVIDENCE_PRODUCED_AT.toString()
        );
        dsl.execute(
                "insert into initial_configuration_intent "
                        + "(intent_identity, submission_request_identifier, "
                        + "merchant_identifier, source_onboarding_case_identity, "
                        + "source_onboarding_case_revision, submitted_case_revision, "
                        + "submitted_by, origin_identifier, submitted_at) values "
                        + "('intent-1', 'submit-intent-1', 'merchant-acme', "
                        + "'case-1', 'reviewed-revision', 'submitted-revision', "
                        + "'identity-42', 'privileged-browser', "
                        + "cast(? as timestamptz))",
                EVIDENCE_PRODUCED_AT.toString()
        );
        dsl.execute(
                "insert into initial_configuration_intent_semantic_seed "
                        + "(intent_identity, seed_namespace, seed_identifier) "
                        + "values ('intent-1', 'mainstreet.semantic', 'booking')"
        );
        dsl.execute(
                "insert into initial_configuration_intent_provenance "
                        + "(intent_identity, provenance_sequence, "
                        + "provenance_reference) values "
                        + "('intent-1', 0, 'answer:intent-1')"
        );
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
