package grandrue.infrastructure.persistence.configuration;

import grandrue.application.MerchantScope;
import grandrue.runtime.ExecutionPrincipal;
import grandrue.runtime.TrustedExecutionContext;
import grandrue.semantic.configuration.ConfigurationChangeSet;
import grandrue.semantic.configuration.MaterialiseConfigurationChangeCommand;
import grandrue.infrastructure.persistence.onboarding.JooqOnboardingCaseEvidenceStore;
import grandrue.onboarding.InitialConfigurationIntentIdentity;
import grandrue.onboarding.OnboardingCaseRevision;
import grandrue.semantic.compiler.ConfigurationCompiler;
import grandrue.semantic.configuration.ConfigurationPackageResolver;
import grandrue.semantic.configuration.ConfigurationValidationEvidence;
import grandrue.semantic.configuration.ConfigurationValidationEvidenceFailureCategory;
import grandrue.semantic.configuration.ConfigurationValidationEvidencePersistenceException;
import grandrue.semantic.configuration.MaterialiseInitialConfigurationRevisionCommand;
import grandrue.semantic.configuration.MerchantConfiguration;
import grandrue.semantic.configuration.OrdinaryNewConfigurationSemanticReleaseAuthority;
import grandrue.semantic.configuration.RecordConfigurationValidationEvidenceCommand;
import grandrue.semantic.configuration.ResolvedConfigurationPackage;
import grandrue.semantic.registry.InMemorySemanticRegistry;
import grandrue.semantic.registry.OwnedOperationalObjectDefinition;
import grandrue.semantic.registry.OwnedOperationDefinition;
import grandrue.semantic.registry.RegisteredCapability;
import grandrue.semantic.registry.SemanticRegistrySnapshot;
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
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JooqConfigurationValidationEvidenceAuthorityIT {

    private static final Instant PACKAGE_GENERATED_AT =
            Instant.parse("2026-08-29T11:00:00Z");
    private static final Instant EVIDENCE_PRODUCED_AT =
            Instant.parse("2026-08-29T11:00:01Z");

    private DSLContext dsl;
    private PlatformTransactionManager transactionManager;
    private MerchantConfiguration configuration;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new DriverManagerDataSource(
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_URL"),
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_USER"),
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_PASSWORD")
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
                        + "configuration_activation_publication_intent, "
                        + "merchant_current_configuration_activation, "
                        + "configuration_activation, "
                        + "configuration_new_activity_required_contract, "
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
        configuration = revisionAuthority()
                .materialiseInitial(new MaterialiseInitialConfigurationRevisionCommand(
                        new InitialConfigurationIntentIdentity("intent-1"),
                        "configuration-1",
                        new OnboardingCaseRevision("completed-configuration-1"),
                        "configuration-bootstrap",
                        Optional.of("onboarding-handoff"),
                        EVIDENCE_PRODUCED_AT
                ))
                .configuration();
    }

    @Test
    void records_and_reads_exact_immutable_validation_package_evidence() {
        JooqConfigurationValidationEvidenceAuthority authority = authority();

        ConfigurationValidationEvidence evidence =
                authority.recordSuccessfulValidation(command(
                        "validation-evidence-1",
                        "resolved-package-1",
                        resolvedPackage(configuration)
                ));

        assertEquals("validation-evidence-1", evidence.validationEvidenceIdentifier());
        assertEquals("merchant-acme", evidence.merchantIdentifier());
        assertEquals("configuration-1", evidence.configurationRevisionIdentifier());
        assertEquals("semantic-release-21", evidence.semanticRegistryReleaseIdentifier());
        assertEquals("resolved-package-1", evidence.resolvedPackageEvidenceIdentifier());
        assertEquals("grandrue-compiler-1", evidence.compilerIdentifier());
        assertEquals(PACKAGE_GENERATED_AT, evidence.packageGeneratedAt());
        assertEquals(EVIDENCE_PRODUCED_AT, evidence.evidenceProducedAt());
        assertEquals(
                evidence,
                authority.evidence(
                        new MerchantScope("merchant-acme"),
                        "validation-evidence-1"
                ).orElseThrow()
        );
        assertEquals(1, dsl.fetchCount(
                DSL.table(DSL.name("configuration_validation_evidence"))
        ));
    }

    @Test
    void records_and_reconstructs_exact_reinstatement_basis_affinity() {
        seedActivation("activate-basis-1");

        JooqConfigurationValidationEvidenceAuthority authority = authority();

        ConfigurationValidationEvidence evidence =
                authority.recordSuccessfulValidation(
                        command(
                                "validation-reinstatement-1",
                                "resolved-package-reinstatement-1",
                                resolvedPackage(configuration),
                                Optional.of("activate-basis-1")
                        )
                );

        assertEquals(
                Optional.of("activate-basis-1"),
                evidence.reinstatementBasisActivationRequestIdentifier()
        );

        assertEquals(
                evidence,
                authority.evidence(
                        new MerchantScope("merchant-acme"),
                        "validation-reinstatement-1"
                ).orElseThrow()
        );
    }

    @Test
    void exact_retry_returns_the_original_evidence() {
        JooqConfigurationValidationEvidenceAuthority authority = authority();
        RecordConfigurationValidationEvidenceCommand command = command(
                "validation-evidence-1",
                "resolved-package-1",
                resolvedPackage(configuration)
        );

        ConfigurationValidationEvidence first =
                authority.recordSuccessfulValidation(command);
        ConfigurationValidationEvidence retry =
                authority.recordSuccessfulValidation(command);

        assertEquals(first, retry);
        assertEquals(1, dsl.fetchCount(
                DSL.table(DSL.name("configuration_validation_evidence"))
        ));
    }

    @Test
    void rejects_a_package_that_does_not_match_the_stored_revision_release() {
        MerchantConfiguration wrongRelease = new MerchantConfiguration(
                configuration.merchantIdentifier(),
                configuration.configurationIdentifier(),
                configuration.version(),
                "semantic-release-22",
                configuration.capabilityIdentifiers()
        );

        ConfigurationValidationEvidencePersistenceException failure =
                assertThrows(
                        ConfigurationValidationEvidencePersistenceException.class,
                        () -> authority().recordSuccessfulValidation(command(
                                "validation-evidence-wrong",
                                "resolved-package-wrong",
                                resolvedPackage(wrongRelease)
                        ))
                );

        assertEquals(
                ConfigurationValidationEvidenceFailureCategory
                        .RESOLVED_PACKAGE_AFFINITY_MISMATCH,
                failure.category()
        );
        assertEquals(0, dsl.fetchCount(
                DSL.table(DSL.name("configuration_validation_evidence"))
        ));
    }

    @Test
    void rejects_reuse_of_an_evidence_identity_for_different_package_intent() {
        JooqConfigurationValidationEvidenceAuthority authority = authority();
        authority.recordSuccessfulValidation(command(
                "validation-evidence-1",
                "resolved-package-1",
                resolvedPackage(configuration)
        ));

        ConfigurationValidationEvidencePersistenceException failure =
                assertThrows(
                        ConfigurationValidationEvidencePersistenceException.class,
                        () -> authority.recordSuccessfulValidation(command(
                                "validation-evidence-1",
                                "resolved-package-2",
                                resolvedPackage(configuration)
                        ))
                );

        assertEquals(
                ConfigurationValidationEvidenceFailureCategory
                        .VALIDATION_EVIDENCE_IDENTITY_CONFLICT,
                failure.category()
        );
    }

    @Test
    void database_rejects_reuse_of_exact_package_reference() {
        JooqConfigurationValidationEvidenceAuthority authority = authority();
        authority.recordSuccessfulValidation(command(
                "validation-evidence-1",
                "resolved-package-1",
                resolvedPackage(configuration)
        ));

        assertThrows(
                DataAccessException.class,
                () -> dsl.execute(
                        "insert into configuration_validation_evidence "
                                + "(merchant_identifier, validation_evidence_identifier, "
                                + "configuration_revision_identifier, "
                                + "semantic_registry_release_identifier, "
                                + "resolved_package_evidence_identifier, "
                                + "validation_outcome, compiler_identifier, "
                                + "package_generated_at, evidence_produced_at) "
                                + "select merchant_identifier, ?, "
                                + "configuration_revision_identifier, "
                                + "semantic_registry_release_identifier, "
                                + "resolved_package_evidence_identifier, "
                                + "validation_outcome, compiler_identifier, "
                                + "package_generated_at, evidence_produced_at "
                                + "from configuration_validation_evidence "
                                + "where merchant_identifier = ? "
                                + "and validation_evidence_identifier = ?",
                        "validation-evidence-2",
                        "merchant-acme",
                        "validation-evidence-1"
                )
        );
    }

    @Test
    void validates_a_real_stored_replacement_without_initial_provenance() {
        MerchantConfiguration replacement = storedReplacement(Set.of("booking"));
        assertEquals(Optional.of("configuration-1"), replacement.baseConfigurationIdentifier());
        assertEquals(Optional.empty(), revisionAuthority().revision(
                new MerchantScope("merchant-acme"), replacement.configurationIdentifier()));

        RecordConfigurationValidationEvidenceCommand intent = command(
                "replacement-validation", "replacement-package", resolvedPackage(replacement));
        ConfigurationValidationEvidence evidence = authority().recordSuccessfulValidation(intent);

        assertEquals(replacement.configurationIdentifier(), evidence.configurationRevisionIdentifier());
        assertEquals(replacement.semanticRegistryVersion(), evidence.semanticRegistryReleaseIdentifier());
        assertEquals(evidence, authority().recordSuccessfulValidation(intent));
        assertEquals(evidence, authority().evidence(new MerchantScope("merchant-acme"),
                "replacement-validation").orElseThrow());
        assertEquals(1, dsl.fetchCount(DSL.table("configuration_validation_evidence")));
        assertEquals(0, dsl.fetchCount(DSL.table("configuration_revision_approval")));
        assertEquals(configuration, revisionAuthority().configuration(
                new MerchantScope("merchant-acme"), "configuration-1").orElseThrow());
    }

    @Test
    void rejects_wrong_release_for_a_real_stored_replacement() {
        MerchantConfiguration replacement = storedReplacement(Set.of("booking"));
        MerchantConfiguration wrongRelease = new MerchantConfiguration(
                replacement.merchantIdentifier(), replacement.configurationIdentifier(),
                replacement.version(), "semantic-release-22", replacement.capabilityIdentifiers(),
                replacement.policySelections(), replacement.baseConfigurationIdentifier(),
                replacement.fulfilmentBindingSetRevisionReference());

        ConfigurationValidationEvidencePersistenceException failure = assertThrows(
                ConfigurationValidationEvidencePersistenceException.class,
                () -> authority().recordSuccessfulValidation(command("wrong-replacement-validation",
                        "wrong-replacement-package", resolvedPackage(wrongRelease))));

        assertEquals(ConfigurationValidationEvidenceFailureCategory.RESOLVED_PACKAGE_AFFINITY_MISMATCH,
                failure.category());
        assertEquals(0, dsl.fetchCount(DSL.table("configuration_validation_evidence")));
    }

    @Test
    void durable_replacement_creation_does_not_bypass_compiler_validation() {
        MerchantConfiguration replacement = storedReplacement(Set.of("unregistered-capability"));

        assertThrows(IllegalArgumentException.class, () -> resolvedPackage(replacement));
        assertEquals(0, dsl.fetchCount(DSL.table("configuration_validation_evidence")));
    }

    private MerchantConfiguration storedReplacement(Set<String> capabilities) {
        MerchantScope scope = new MerchantScope("merchant-acme");
        ConfigurationChangeSet change = new ConfigurationChangeSet(scope, "change-1",
                "configuration-1", capabilities, Set.of(), Optional.empty(),
                new ConfigurationChangeSet.Provenance(ConfigurationChangeSet.Origin.MERCHANT_INITIATED,
                        "change-request-1", "identity-42", EVIDENCE_PRODUCED_AT));
        new JooqConfigurationChangeAuthority(dsl, transactionManager, revisionAuthority(),
                () -> "semantic-release-21", (intent, context) -> true)
                .materialise(new MaterialiseConfigurationChangeCommand(change, "configuration-2",
                                EVIDENCE_PRODUCED_AT),
                        new TrustedExecutionContext(scope, new ExecutionPrincipal("identity-42"), Optional.empty()));
        return revisionAuthority().configuration(scope, "configuration-2").orElseThrow();
    }

    private JooqConfigurationValidationEvidenceAuthority authority() {
        return new JooqConfigurationValidationEvidenceAuthority(
                dsl,
                transactionManager,
                revisionAuthority()
        );
    }

    private JooqConfigurationRevisionAuthority revisionAuthority() {
        OrdinaryNewConfigurationSemanticReleaseAuthority releaseAuthority =
                () -> "semantic-release-21";
        return new JooqConfigurationRevisionAuthority(
                dsl,
                transactionManager,
                new JooqOnboardingCaseEvidenceStore(dsl, transactionManager),
                releaseAuthority
        );
    }

    private static RecordConfigurationValidationEvidenceCommand command(
            String validationEvidenceIdentifier,
            String resolvedPackageEvidenceIdentifier,
            ResolvedConfigurationPackage resolvedPackage
    ) {
        return command(
                validationEvidenceIdentifier,
                resolvedPackageEvidenceIdentifier,
                resolvedPackage,
                Optional.empty()
        );
    }

    private static RecordConfigurationValidationEvidenceCommand command(
            String validationEvidenceIdentifier,
            String resolvedPackageEvidenceIdentifier,
            ResolvedConfigurationPackage resolvedPackage,
            Optional<String> reinstatementBasisActivationRequestIdentifier
    ) {
        return new RecordConfigurationValidationEvidenceCommand(
                validationEvidenceIdentifier,
                resolvedPackageEvidenceIdentifier,
                resolvedPackage,
                EVIDENCE_PRODUCED_AT,
                reinstatementBasisActivationRequestIdentifier
        );
    }

    private void seedActivation(String activationRequestIdentifier) {
        dsl.execute(
                "insert into configuration_activation "
                        + "(activation_request_identifier, "
                        + "merchant_identifier, "
                        + "configuration_revision_identifier, "
                        + "release_identifier, "
                        + "initiating_principal_identifier, activated_at) "
                        + "values (?, 'merchant-acme', 'configuration-1', "
                        + "'release-merchant-acme-1', 'controller-a', "
                        + "cast(? as timestamptz))",
                activationRequestIdentifier,
                EVIDENCE_PRODUCED_AT.minusSeconds(60).toString()
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
        ).resolve(
                source,
                "grandrue-compiler-1",
                PACKAGE_GENERATED_AT
        );
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
