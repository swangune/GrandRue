package grandrue.infrastructure.persistence.configuration;

import grandrue.application.MerchantScope;
import grandrue.infrastructure.persistence.onboarding.JooqOnboardingCaseEvidenceStore;
import grandrue.onboarding.InitialConfigurationIntentIdentity;
import grandrue.onboarding.OnboardingCaseRevision;
import grandrue.semantic.compiler.ConfigurationCompiler;
import grandrue.semantic.configuration.ConfigurationImpactAnalysisResult;
import grandrue.semantic.configuration.ConfigurationImpactClassification;
import grandrue.semantic.configuration.ConfigurationImpactFinding;
import grandrue.semantic.configuration.ConfigurationImpactReviewEvidence;
import grandrue.semantic.configuration.ConfigurationImpactReviewEvidenceFailureCategory;
import grandrue.semantic.configuration.ConfigurationImpactReviewEvidencePersistenceException;
import grandrue.semantic.configuration.ConfigurationPackageResolver;
import grandrue.semantic.configuration.MaterialiseInitialConfigurationRevisionCommand;
import grandrue.semantic.configuration.MerchantConfiguration;
import grandrue.semantic.configuration.OrdinaryNewConfigurationSemanticReleaseAuthority;
import grandrue.semantic.configuration.RecordConfigurationImpactReviewEvidenceCommand;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqConfigurationImpactReviewEvidenceAuthorityIT {

    private static final Instant PACKAGE_GENERATED_AT =
            Instant.parse("2026-08-29T14:00:00Z");
    private static final Instant VALIDATION_PRODUCED_AT =
            Instant.parse("2026-08-29T14:00:01Z");
    private static final Instant IMPACT_COMPLETED_AT =
            Instant.parse("2026-08-29T14:00:02Z");

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
        MerchantConfiguration configuration = revisionAuthority()
                .materialiseInitial(new MaterialiseInitialConfigurationRevisionCommand(
                        new InitialConfigurationIntentIdentity("intent-1"),
                        "configuration-1",
                        new OnboardingCaseRevision("completed-configuration-1"),
                        "configuration-bootstrap",
                        Optional.of("onboarding-handoff"),
                        VALIDATION_PRODUCED_AT
                ))
                .configuration();
        ResolvedConfigurationPackage resolvedPackage =
                resolvedPackage(configuration);
        validationAuthority().recordSuccessfulValidation(
                new RecordConfigurationValidationEvidenceCommand(
                        "validation-evidence-1",
                        "resolved-package-1",
                        resolvedPackage,
                        VALIDATION_PRODUCED_AT
                )
        );
    }

    @Test
    void records_and_reconstructs_exact_business_facing_impact_evidence() {
        JooqConfigurationImpactReviewEvidenceAuthority authority = authority();

        ConfigurationImpactReviewEvidence evidence =
                authority.recordCompletedReview(command(
                        "impact-review-1",
                        analysis(
                                List.of(
                                        "Customers can book appointments online.",
                                        "Staff will manage new appointment requests."
                                ),
                                List.of(
                                        new ConfigurationImpactFinding(
                                                ConfigurationImpactClassification
                                                        .CONSEQUENTIAL,
                                                "Customer self-booking changes the appointment workflow."
                                        ),
                                        new ConfigurationImpactFinding(
                                                ConfigurationImpactClassification
                                                        .INFORMATIONAL,
                                                "No existing commitments are changed."
                                        )
                                )
                        )
                ));

        assertEquals("impact-review-1", evidence.impactReviewEvidenceIdentifier());
        assertEquals("merchant-acme", evidence.merchantIdentifier());
        assertEquals("configuration-1", evidence.configurationRevisionIdentifier());
        assertEquals("semantic-release-21", evidence.semanticRegistryReleaseIdentifier());
        assertEquals("validation-evidence-1", evidence.validationEvidenceIdentifier());
        assertEquals("resolved-package-1", evidence.resolvedPackageEvidenceIdentifier());
        assertEquals(2, evidence.businessFacingEffects().size());
        assertEquals(2, evidence.findings().size());
        assertEquals(IMPACT_COMPLETED_AT, evidence.impactAnalysisCompletedAt());
        assertEquals(
                evidence,
                authority.evidence(
                        new MerchantScope("merchant-acme"),
                        "impact-review-1"
                ).orElseThrow()
        );
    }

    @Test
    void records_and_reconstructs_matching_reinstatement_basis_affinity() {
        seedActivation("activate-basis-1");

        MerchantConfiguration configuration =
                revisionAuthority().configuration(
                        new MerchantScope("merchant-acme"),
                        "configuration-1"
                ).orElseThrow();

        validationAuthority().recordSuccessfulValidation(
                new RecordConfigurationValidationEvidenceCommand(
                        "validation-reinstatement-1",
                        "resolved-package-reinstatement-1",
                        resolvedPackage(configuration),
                        VALIDATION_PRODUCED_AT,
                        Optional.of("activate-basis-1")
                )
        );

        ConfigurationImpactAnalysisResult analysis =
                new ConfigurationImpactAnalysisResult(
                        "merchant-acme",
                        "configuration-1",
                        "semantic-release-21",
                        "validation-reinstatement-1",
                        "resolved-package-reinstatement-1",
                        List.of("A previous configuration can be used again."),
                        List.of(),
                        IMPACT_COMPLETED_AT
                );

        ConfigurationImpactReviewEvidence evidence =
                authority().recordCompletedReview(
                        new RecordConfigurationImpactReviewEvidenceCommand(
                                "impact-review-reinstatement-1",
                                analysis,
                                Optional.of("activate-basis-1")
                        )
                );

        assertEquals(
                Optional.of("activate-basis-1"),
                evidence.reinstatementBasisActivationRequestIdentifier()
        );

        assertEquals(
                evidence,
                authority().evidence(
                        new MerchantScope("merchant-acme"),
                        "impact-review-reinstatement-1"
                ).orElseThrow()
        );
    }

    @Test
    void rejects_review_basis_that_differs_from_validation_basis() {
        ConfigurationImpactReviewEvidencePersistenceException failure =
                assertThrows(
                        ConfigurationImpactReviewEvidencePersistenceException.class,
                        () -> authority().recordCompletedReview(
                                new RecordConfigurationImpactReviewEvidenceCommand(
                                        "impact-review-basis-mismatch",
                                        analysis(
                                                List.of(
                                                        "Booking remains available."
                                                ),
                                                List.of()
                                        ),
                                        Optional.of("activate-other")
                                )
                        )
                );

        assertEquals(
                ConfigurationImpactReviewEvidenceFailureCategory
                        .VALIDATION_EVIDENCE_AFFINITY_MISMATCH,
                failure.category()
        );

        assertEquals(
                0,
                dsl.fetchCount(
                        DSL.table(
                                DSL.name(
                                        "configuration_impact_review_evidence"
                                )
                        )
                )
        );
    }

    @Test
    void accepts_an_exact_completed_review_with_zero_findings() {
        ConfigurationImpactReviewEvidence evidence =
                authority().recordCompletedReview(command(
                        "impact-review-zero-findings",
                        analysis(
                                List.of("Booking is available to customers."),
                                List.of()
                        )
                ));

        assertEquals(List.of(), evidence.findings());
        assertEquals(1, dsl.fetchCount(
                DSL.table(DSL.name("configuration_impact_review_effect"))
        ));
        assertEquals(0, dsl.fetchCount(
                DSL.table(DSL.name("configuration_impact_review_finding"))
        ));
    }

    @Test
    void retains_blocking_and_existing_commitment_findings_in_review_order() {
        ConfigurationImpactReviewEvidence evidence =
                authority().recordCompletedReview(command(
                        "impact-review-blocking",
                        analysis(
                                List.of("Existing appointments remain unchanged."),
                                List.of(
                                        new ConfigurationImpactFinding(
                                                ConfigurationImpactClassification
                                                        .BLOCKING,
                                                "A mandatory booking policy is missing."
                                        ),
                                        new ConfigurationImpactFinding(
                                                ConfigurationImpactClassification
                                                        .EXISTING_COMMITMENT_CONFLICT,
                                                "Existing appointments retain their original terms."
                                        )
                                )
                        )
                ));

        assertEquals(
                List.of(
                        ConfigurationImpactClassification.BLOCKING,
                        ConfigurationImpactClassification
                                .EXISTING_COMMITMENT_CONFLICT
                ),
                evidence.findings().stream()
                        .map(ConfigurationImpactFinding::classification)
                        .toList()
        );
        assertTrue(evidence.hasBlockingFinding());
    }

    @Test
    void exact_retry_returns_the_original_immutable_review() {
        JooqConfigurationImpactReviewEvidenceAuthority authority = authority();
        RecordConfigurationImpactReviewEvidenceCommand command = command(
                "impact-review-1",
                analysis(
                        List.of("Customers can book appointments online."),
                        List.of(new ConfigurationImpactFinding(
                                ConfigurationImpactClassification.CONSEQUENTIAL,
                                "Customer self-booking changes the appointment workflow."
                        ))
                )
        );

        ConfigurationImpactReviewEvidence first =
                authority.recordCompletedReview(command);
        ConfigurationImpactReviewEvidence retry =
                authority.recordCompletedReview(command);

        assertEquals(first, retry);
        assertEquals(1, dsl.fetchCount(
                DSL.table(DSL.name("configuration_impact_review_evidence"))
        ));
    }

    @Test
    void rejects_missing_validation_evidence_without_persisting_review() {
        ConfigurationImpactAnalysisResult missing =
                new ConfigurationImpactAnalysisResult(
                        "merchant-acme",
                        "configuration-1",
                        "semantic-release-21",
                        "validation-evidence-missing",
                        "resolved-package-1",
                        List.of("Customers can book appointments online."),
                        List.of(),
                        IMPACT_COMPLETED_AT
                );

        ConfigurationImpactReviewEvidencePersistenceException failure =
                assertThrows(
                        ConfigurationImpactReviewEvidencePersistenceException.class,
                        () -> authority().recordCompletedReview(command(
                                "impact-review-missing",
                                missing
                        ))
                );

        assertEquals(
                ConfigurationImpactReviewEvidenceFailureCategory
                        .VALIDATION_EVIDENCE_NOT_FOUND,
                failure.category()
        );
        assertEquals(0, dsl.fetchCount(
                DSL.table(DSL.name("configuration_impact_review_evidence"))
        ));
    }

    @Test
    void rejects_validation_package_affinity_mismatch() {
        ConfigurationImpactAnalysisResult mismatch =
                new ConfigurationImpactAnalysisResult(
                        "merchant-acme",
                        "configuration-1",
                        "semantic-release-21",
                        "validation-evidence-1",
                        "resolved-package-other",
                        List.of("Customers can book appointments online."),
                        List.of(),
                        IMPACT_COMPLETED_AT
                );

        ConfigurationImpactReviewEvidencePersistenceException failure =
                assertThrows(
                        ConfigurationImpactReviewEvidencePersistenceException.class,
                        () -> authority().recordCompletedReview(command(
                                "impact-review-mismatch",
                                mismatch
                        ))
                );

        assertEquals(
                ConfigurationImpactReviewEvidenceFailureCategory
                        .VALIDATION_EVIDENCE_AFFINITY_MISMATCH,
                failure.category()
        );
        assertEquals(0, dsl.fetchCount(
                DSL.table(DSL.name("configuration_impact_review_evidence"))
        ));
    }

    @Test
    void rejects_reuse_of_review_identity_for_changed_effect_content() {
        JooqConfigurationImpactReviewEvidenceAuthority authority = authority();
        authority.recordCompletedReview(command(
                "impact-review-1",
                analysis(
                        List.of("Customers can book appointments online."),
                        List.of()
                )
        ));

        ConfigurationImpactReviewEvidencePersistenceException failure =
                assertThrows(
                        ConfigurationImpactReviewEvidencePersistenceException.class,
                        () -> authority.recordCompletedReview(command(
                                "impact-review-1",
                                analysis(
                                        List.of("Customers cannot book online."),
                                        List.of()
                                )
                        ))
                );

        assertEquals(
                ConfigurationImpactReviewEvidenceFailureCategory
                        .IMPACT_REVIEW_EVIDENCE_IDENTITY_CONFLICT,
                failure.category()
        );
    }

    @Test
    void database_rejects_an_unclassified_finding() {
        authority().recordCompletedReview(command(
                "impact-review-1",
                analysis(
                        List.of("Customers can book appointments online."),
                        List.of()
                )
        ));

        assertThrows(
                DataAccessException.class,
                () -> dsl.execute(
                        "insert into configuration_impact_review_finding "
                                + "(merchant_identifier, impact_review_evidence_identifier, "
                                + "finding_sequence, impact_classification, "
                                + "business_facing_finding) values (?, ?, ?, ?, ?)",
                        "merchant-acme",
                        "impact-review-1",
                        0,
                        "UNCLASSIFIED",
                        "An unclassified finding must not be retained."
                )
        );
    }

    private JooqConfigurationImpactReviewEvidenceAuthority authority() {
        return new JooqConfigurationImpactReviewEvidenceAuthority(
                dsl,
                transactionManager,
                validationAuthority()
        );
    }

    private JooqConfigurationValidationEvidenceAuthority validationAuthority() {
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

    private static RecordConfigurationImpactReviewEvidenceCommand command(
            String evidenceIdentifier,
            ConfigurationImpactAnalysisResult analysis
    ) {
        return new RecordConfigurationImpactReviewEvidenceCommand(
                evidenceIdentifier,
                analysis
        );
    }

    private static ConfigurationImpactAnalysisResult analysis(
            List<String> effects,
            List<ConfigurationImpactFinding> findings
    ) {
        return new ConfigurationImpactAnalysisResult(
                "merchant-acme",
                "configuration-1",
                "semantic-release-21",
                "validation-evidence-1",
                "resolved-package-1",
                effects,
                findings,
                IMPACT_COMPLETED_AT
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
                "mainstreet-compiler-1",
                PACKAGE_GENERATED_AT
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
                VALIDATION_PRODUCED_AT.minusSeconds(60).toString()
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
                VALIDATION_PRODUCED_AT.toString()
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
                VALIDATION_PRODUCED_AT.toString(),
                VALIDATION_PRODUCED_AT.toString()
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
                VALIDATION_PRODUCED_AT.toString()
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
