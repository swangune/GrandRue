package mainstreet.infrastructure.persistence.configuration;

import mainstreet.application.MerchantScope;
import mainstreet.runtime.AuthenticationProvenance;
import mainstreet.runtime.ExecutionPrincipal;
import mainstreet.runtime.TrustedExecutionContext;
import mainstreet.semantic.configuration.ApproveConfigurationReinstatementCommand;
import mainstreet.semantic.configuration.ConfigurationReinstatementApproval;
import mainstreet.semantic.configuration.ConfigurationRevisionApprovalFailureCategory;
import mainstreet.semantic.configuration.ConfigurationRevisionApprovalPersistenceException;
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
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqConfigurationReinstatementApprovalAuthorityIT {

    private static final MerchantScope SCOPE =
            new MerchantScope("merchant-acme");

    private static final String MERCHANT = "merchant-acme";
    private static final String CONTROLLER = "controller-a";
    private static final String CONTROLLER_RELATIONSHIP =
            "controller-rel-a";

    private static final String SEMANTIC_RELEASE = "release-21";

    private static final String INITIAL_REVISION =
            "configuration-1";

    private static final String SECOND_REVISION =
            "configuration-2";

    private static final String THIRD_REVISION =
            "configuration-3";

    private static final String INITIAL_RELEASE =
            "release-merchant-acme-1";

    private static final String SECOND_RELEASE =
            "release-merchant-acme-2";

    private static final String THIRD_RELEASE =
            "release-merchant-acme-3";

    private static final String INITIAL_ACTIVATION =
            "activate-1";

    private static final String SECOND_ACTIVATION =
            "activate-2";

    private static final Instant NOW =
            Instant.parse("2026-09-12T08:00:00Z");

    private DSLContext dsl;
    private DataSourceTransactionManager transactions;

    @BeforeEach
    void setUp() {
        DataSource source =
                new DriverManagerDataSource(
                        required("MAINSTREET_TEST_POSTGRES_URL"),
                        required("MAINSTREET_TEST_POSTGRES_USER"),
                        required("MAINSTREET_TEST_POSTGRES_PASSWORD")
                );

        transactions =
                new DataSourceTransactionManager(source);

        Flyway.configure()
                .dataSource(source)
                .locations("classpath:db/migration")
                .load()
                .migrate();

        dsl =
                DSL.using(
                        new TransactionAwareDataSourceProxy(source),
                        SQLDialect.POSTGRES
                );

        clear();

        seedConfigurationAuthority();
        seedActivationHistory();
        seedHistoricalInitialApproval();
        seedFreshReinstatementEvidence(
                SECOND_ACTIVATION
        );
    }

    @Test
    void superseded_initial_revision_requires_new_exact_basis_affined_approval() {
        JooqConfigurationReinstatementApprovalAuthority authority =
                new JooqConfigurationReinstatementApprovalAuthority(
                        dsl,
                        transactions
                );

        /*
         * The original approval for Configuration Revision 1 still exists,
         * but it has no Reinstatement Basis Activation. Historical first
         * approval must therefore not become current reinstatement authority.
         */
        assertTrue(
                authority.currentApplicableApproval(
                        SCOPE,
                        INITIAL_REVISION
                ).isEmpty()
        );

        ConfigurationReinstatementApproval approval =
                authority.approve(
                        command(
                                "approval-reinstate-1",
                                SECOND_ACTIVATION
                        ),
                        trustedContext()
                );

        assertEquals(
                "approval-reinstate-1",
                approval.logicalApprovalRequestIdentifier()
        );

        assertEquals(
                MERCHANT,
                approval.merchantIdentifier()
        );

        assertEquals(
                INITIAL_REVISION,
                approval.configurationRevisionIdentifier()
        );

        assertEquals(
                SEMANTIC_RELEASE,
                approval.semanticRegistryReleaseIdentifier()
        );

        assertEquals(
                SECOND_ACTIVATION,
                approval.reinstatementBasisActivationRequestIdentifier()
        );

        assertEquals(
                "validation-reinstate-1",
                approval.validationEvidenceIdentifier()
        );

        assertEquals(
                "impact-reinstate-1",
                approval.impactReviewEvidenceIdentifier()
        );

        assertEquals(
                "package-reinstate-1",
                approval.resolvedPackageEvidenceIdentifier()
        );

        assertEquals(
                CONTROLLER,
                approval.approvingPrincipalIdentifier()
        );

        assertEquals(
                CONTROLLER_RELATIONSHIP,
                approval.controllerRelationshipIdentifier()
        );

        assertEquals(
                NOW.minusSeconds(30),
                approval.approvedAt()
        );

        assertEquals(
                Optional.of(approval),
                authority.currentApplicableApproval(
                        SCOPE,
                        INITIAL_REVISION
                )
        );

        assertEquals(
                Optional.of(approval),
                authority.approvalByRequest(
                        "approval-reinstate-1"
                )
        );
    }

    @Test
    void same_revision_later_activation_cycle_does_not_revive_old_reinstatement_approval() {
        JooqConfigurationReinstatementApprovalAuthority authority =
                new JooqConfigurationReinstatementApprovalAuthority(
                        dsl,
                        transactions
                );

        ConfigurationReinstatementApproval approval =
                authority.approve(
                        command(
                                "approval-reinstate-cycle-proof",
                                SECOND_ACTIVATION
                        ),
                        trustedContext()
                );

        assertEquals(
                Optional.of(approval),
                authority.currentApplicableApproval(
                        SCOPE,
                        INITIAL_REVISION
                )
        );

        /*
         * B1 -> C -> B2.
         *
         * B2 activates the same immutable Configuration Revision as B1,
         * but it is a different committed Configuration Activation.
         *
         * Revision identity therefore cannot revive an approval whose exact
         * Reinstatement Basis Activation was B1.
         */
        moveCurrentThroughThirdAndBackToSecond();

        assertEquals(
                SECOND_REVISION,
                currentRevision()
        );

        assertEquals(
                "activate-2-cycle-2",
                currentActivationRequestIdentifier()
        );

        assertTrue(
                authority.currentApplicableApproval(
                        SCOPE,
                        INITIAL_REVISION
                ).isEmpty()
        );

        /*
         * The immutable historical approval remains available by its own
         * request identity. What disappeared is current applicability.
         */
        assertEquals(
                Optional.of(approval),
                authority.approvalByRequest(
                        "approval-reinstate-cycle-proof"
                )
        );
    }

    @Test
    void exact_request_retry_returns_one_committed_reinstatement_approval() {
        JooqConfigurationReinstatementApprovalAuthority authority =
                new JooqConfigurationReinstatementApprovalAuthority(
                        dsl,
                        transactions
                );

        ApproveConfigurationReinstatementCommand command =
                command(
                        "approval-reinstate-retry",
                        SECOND_ACTIVATION
                );

        ConfigurationReinstatementApproval committed =
                authority.approve(
                        command,
                        trustedContext()
                );

        assertEquals(
                committed,
                authority.approve(
                        command,
                        trustedContext()
                )
        );

        assertEquals(
                1,
                dsl.fetchOne(
                        "select count(*) as approval_count "
                                + "from configuration_revision_approval "
                                + "where logical_approval_request_identifier = ?",
                        command.logicalApprovalRequestIdentifier()
                ).get(
                        "approval_count",
                        Integer.class
                )
        );
    }

    @Test
    void request_retry_with_different_reinstatement_basis_is_conflict() {
        JooqConfigurationReinstatementApprovalAuthority authority =
                new JooqConfigurationReinstatementApprovalAuthority(
                        dsl,
                        transactions
                );

        authority.approve(
                command(
                        "approval-reinstatement-basis-conflict",
                        SECOND_ACTIVATION
                ),
                trustedContext()
        );

        ConfigurationRevisionApprovalPersistenceException failure =
                assertThrows(
                        ConfigurationRevisionApprovalPersistenceException.class,
                        () -> authority.approve(
                                command(
                                        "approval-reinstatement-basis-conflict",
                                        INITIAL_ACTIVATION
                                ),
                                trustedContext()
                        )
                );

        assertEquals(
                ConfigurationRevisionApprovalFailureCategory
                        .APPROVAL_REQUEST_IDENTITY_CONFLICT,
                failure.category()
        );
    }

    @Test
    void controller_transfer_makes_reinstatement_approval_inapplicable() {
        JooqConfigurationReinstatementApprovalAuthority authority =
                new JooqConfigurationReinstatementApprovalAuthority(
                        dsl,
                        transactions
                );

        ConfigurationReinstatementApproval historical =
                authority.approve(
                        command(
                                "approval-before-controller-transfer",
                                SECOND_ACTIVATION
                        ),
                        trustedContext()
                );

        dsl.execute(
                "update merchant_controller_relationship "
                        + "set lifecycle = 'ENDED' "
                        + "where controller_relationship_identifier = ?",
                CONTROLLER_RELATIONSHIP
        );

        dsl.execute(
                "insert into merchant_controller_relationship "
                        + "(controller_relationship_identifier, "
                        + "merchant_identifier, identity_identifier, lifecycle) "
                        + "values ('controller-rel-b', ?, "
                        + "'controller-b', 'ACTIVE')",
                MERCHANT
        );

        assertTrue(
                authority.currentApplicableApproval(
                        SCOPE,
                        INITIAL_REVISION
                ).isEmpty()
        );

        assertEquals(
                Optional.of(historical),
                authority.approvalByRequest(
                        historical.logicalApprovalRequestIdentifier()
                )
        );
    }

    private ApproveConfigurationReinstatementCommand command(
            String requestIdentifier,
            String reinstatementBasisActivationRequestIdentifier
    ) {
        return new ApproveConfigurationReinstatementCommand(
                requestIdentifier,
                SCOPE,
                INITIAL_REVISION,
                reinstatementBasisActivationRequestIdentifier,
                "validation-reinstate-1",
                "impact-reinstate-1",
                CONTROLLER,
                NOW.minusSeconds(30)
        );
    }

    private TrustedExecutionContext trustedContext() {
        return new TrustedExecutionContext(
                SCOPE,
                new ExecutionPrincipal(CONTROLLER),
                Optional.of(
                        new AuthenticationProvenance(
                                "session-controller-a",
                                CONTROLLER,
                                NOW.minusSeconds(120)
                        )
                )
        );
    }

    private void seedConfigurationAuthority() {
        dsl.execute(
                "insert into merchant_account "
                        + "(merchant_identifier) "
                        + "values (?)",
                MERCHANT
        );

        dsl.execute(
                "insert into merchant_controller_relationship "
                        + "(controller_relationship_identifier, "
                        + "merchant_identifier, "
                        + "identity_identifier, "
                        + "lifecycle) "
                        + "values (?, ?, ?, 'ACTIVE')",
                CONTROLLER_RELATIONSHIP,
                MERCHANT,
                CONTROLLER
        );

        dsl.execute(
                "insert into onboarding_case "
                        + "(onboarding_case_identity, "
                        + "merchant_identifier, "
                        + "purpose, "
                        + "lifecycle, "
                        + "current_revision, "
                        + "started_at) "
                        + "values "
                        + "('case-1', ?, "
                        + "'INITIAL_CONFIGURATION', "
                        + "'COMPLETED', "
                        + "'complete-revision', "
                        + "cast(? as timestamptz))",
                MERCHANT,
                NOW.minusSeconds(1000).toString()
        );

        dsl.execute(
                "insert into onboarding_case_revision "
                        + "(onboarding_case_identity, "
                        + "revision_identity, "
                        + "prior_revision_identity, "
                        + "mutation_request_identifier, "
                        + "mutation_kind, "
                        + "resulting_lifecycle, "
                        + "principal_reference, "
                        + "origin_identifier, "
                        + "committed_at) "
                        + "values "
                        + "('case-1','reviewed-revision',null,"
                        + "'start-1','START','IN_PROGRESS',"
                        + "?, 'browser', cast(? as timestamptz)), "
                        + "('case-1','submitted-revision',"
                        + "'reviewed-revision','submit-1',"
                        + "'SUBMIT','SUBMITTED',?,"
                        + "'browser',cast(? as timestamptz)), "
                        + "('case-1','complete-revision',"
                        + "'submitted-revision','complete-1',"
                        + "'COMPLETE','COMPLETED','system',"
                        + "'configuration',cast(? as timestamptz))",
                CONTROLLER,
                NOW.minusSeconds(990).toString(),
                CONTROLLER,
                NOW.minusSeconds(980).toString(),
                NOW.minusSeconds(970).toString()
        );

        dsl.execute(
                "insert into initial_configuration_intent "
                        + "(intent_identity, "
                        + "submission_request_identifier, "
                        + "merchant_identifier, "
                        + "source_onboarding_case_identity, "
                        + "source_onboarding_case_revision, "
                        + "submitted_case_revision, "
                        + "submitted_by, "
                        + "origin_identifier, "
                        + "submitted_at) "
                        + "values "
                        + "('intent-1','submit-1',?,"
                        + "'case-1','reviewed-revision',"
                        + "'submitted-revision',?,"
                        + "'browser',cast(? as timestamptz))",
                MERCHANT,
                CONTROLLER,
                NOW.minusSeconds(980).toString()
        );

        dsl.execute(
                "insert into initial_configuration_intent_semantic_seed "
                        + "(intent_identity, "
                        + "seed_namespace, "
                        + "seed_identifier) "
                        + "values "
                        + "('intent-1','mainstreet.semantic','booking')"
        );

        dsl.execute(
                "insert into initial_configuration_intent_provenance "
                        + "(intent_identity, "
                        + "provenance_sequence, "
                        + "provenance_reference) "
                        + "values "
                        + "('intent-1',0,'answer:1')"
        );

        dsl.execute(
                "insert into merchant_configuration_revision "
                        + "(merchant_identifier, "
                        + "configuration_revision_identifier, "
                        + "configuration_version, "
                        + "semantic_registry_release_identifier, "
                        + "base_configuration_revision_identifier, "
                        + "fulfilment_binding_set_identifier, "
                        + "fulfilment_binding_set_revision, "
                        + "source_initial_configuration_intent_identity, "
                        + "source_onboarding_case_identity, "
                        + "source_onboarding_case_revision, "
                        + "materialised_by, "
                        + "origin_identifier, "
                        + "materialised_at, "
                        + "onboarding_completion_revision) "
                        + "values "
                        + "(?, ?, 1, ?, null, null, null,"
                        + "'intent-1','case-1','reviewed-revision',"
                        + "'system','onboarding',"
                        + "cast(? as timestamptz),"
                        + "'complete-revision')",
                MERCHANT,
                INITIAL_REVISION,
                SEMANTIC_RELEASE,
                NOW.minusSeconds(900).toString()
        );

        insertLaterRevision(
                SECOND_REVISION,
                2,
                INITIAL_REVISION,
                "change-2"
        );

        insertLaterRevision(
                THIRD_REVISION,
                3,
                SECOND_REVISION,
                "change-3"
        );
    }

    private void insertLaterRevision(
            String revisionIdentifier,
            long version,
            String baseRevisionIdentifier,
            String changeSetIdentifier
    ) {
        dsl.execute(
                "insert into merchant_configuration_revision "
                        + "(merchant_identifier, "
                        + "configuration_revision_identifier, "
                        + "configuration_version, "
                        + "semantic_registry_release_identifier, "
                        + "base_configuration_revision_identifier, "
                        + "fulfilment_binding_set_identifier, "
                        + "fulfilment_binding_set_revision, "
                        + "materialised_by, "
                        + "materialised_at, "
                        + "source_change_set_identifier, "
                        + "change_origin, "
                        + "change_source_identifier, "
                        + "change_proposed_at) "
                        + "values "
                        + "(?, ?, ?, ?, ?, null, null, ?, "
                        + "cast(? as timestamptz), ?, "
                        + "'MERCHANT_INITIATED', "
                        + "'merchant-settings', "
                        + "cast(? as timestamptz))",
                MERCHANT,
                revisionIdentifier,
                version,
                SEMANTIC_RELEASE,
                baseRevisionIdentifier,
                CONTROLLER,
                NOW.minusSeconds(800 - version).toString(),
                changeSetIdentifier,
                NOW.minusSeconds(810 - version).toString()
        );
    }

    private void seedActivationHistory() {
        insertActivation(
                INITIAL_ACTIVATION,
                INITIAL_REVISION,
                INITIAL_RELEASE,
                null,
                null,
                NOW.minusSeconds(700)
        );

        insertActivation(
                SECOND_ACTIVATION,
                SECOND_REVISION,
                SECOND_RELEASE,
                INITIAL_REVISION,
                INITIAL_REVISION,
                NOW.minusSeconds(600)
        );

        dsl.execute(
                "insert into merchant_current_configuration_activation "
                        + "(merchant_identifier, "
                        + "activation_request_identifier, "
                        + "configuration_revision_identifier, "
                        + "release_identifier) "
                        + "values (?, ?, ?, ?)",
                MERCHANT,
                SECOND_ACTIVATION,
                SECOND_REVISION,
                SECOND_RELEASE
        );
    }

    private void insertActivation(
            String activationRequestIdentifier,
            String configurationRevisionIdentifier,
            String releaseIdentifier,
            String expectedCurrentConfigurationIdentifier,
            String replacedConfigurationRevisionIdentifier,
            Instant activatedAt
    ) {
        dsl.execute(
                "insert into configuration_activation "
                        + "(activation_request_identifier, "
                        + "merchant_identifier, "
                        + "configuration_revision_identifier, "
                        + "release_identifier, "
                        + "expected_current_configuration_identifier, "
                        + "initiating_principal_identifier, "
                        + "activated_at, "
                        + "replaced_configuration_revision_identifier) "
                        + "values (?, ?, ?, ?, ?, ?, "
                        + "cast(? as timestamptz), ?)",
                activationRequestIdentifier,
                MERCHANT,
                configurationRevisionIdentifier,
                releaseIdentifier,
                expectedCurrentConfigurationIdentifier,
                CONTROLLER,
                activatedAt.toString(),
                replacedConfigurationRevisionIdentifier
        );
    }

    private void seedHistoricalInitialApproval() {
        /*
         * Original first-activation evidence has no reinstatement basis.
         */
        insertValidationEvidence(
                "validation-initial-1",
                "package-initial-1",
                null
        );

        insertImpactEvidence(
                "impact-initial-1",
                "validation-initial-1",
                "package-initial-1",
                null
        );

        dsl.execute(
                "insert into configuration_revision_approval "
                        + "(logical_approval_request_identifier, "
                        + "merchant_identifier, "
                        + "configuration_revision_identifier, "
                        + "semantic_registry_release_identifier, "
                        + "validation_evidence_identifier, "
                        + "impact_review_evidence_identifier, "
                        + "resolved_package_evidence_identifier, "
                        + "approving_principal_identifier, "
                        + "controller_relationship_identifier, "
                        + "approved_at, "
                        + "reinstatement_basis_activation_request_identifier) "
                        + "values "
                        + "('approval-initial-1', ?, ?, ?, "
                        + "'validation-initial-1', "
                        + "'impact-initial-1', "
                        + "'package-initial-1', "
                        + "?, ?, cast(? as timestamptz), null)",
                MERCHANT,
                INITIAL_REVISION,
                SEMANTIC_RELEASE,
                CONTROLLER,
                CONTROLLER_RELATIONSHIP,
                NOW.minusSeconds(750).toString()
        );
    }

    private void seedFreshReinstatementEvidence(
            String basisActivationRequestIdentifier
    ) {
        insertValidationEvidence(
                "validation-reinstate-1",
                "package-reinstate-1",
                basisActivationRequestIdentifier
        );

        insertImpactEvidence(
                "impact-reinstate-1",
                "validation-reinstate-1",
                "package-reinstate-1",
                basisActivationRequestIdentifier
        );

        dsl.execute(
                "insert into configuration_impact_review_effect "
                        + "(merchant_identifier, "
                        + "impact_review_evidence_identifier, "
                        + "effect_sequence, "
                        + "business_facing_effect) "
                        + "values "
                        + "(?, 'impact-reinstate-1', 0, "
                        + "'Reinstating a previous configuration "
                        + "changes the active business configuration')",
                MERCHANT
        );
    }

    private void insertValidationEvidence(
            String validationEvidenceIdentifier,
            String resolvedPackageEvidenceIdentifier,
            String basisActivationRequestIdentifier
    ) {
        dsl.execute(
                "insert into configuration_validation_evidence "
                        + "(merchant_identifier, "
                        + "validation_evidence_identifier, "
                        + "configuration_revision_identifier, "
                        + "semantic_registry_release_identifier, "
                        + "resolved_package_evidence_identifier, "
                        + "validation_outcome, "
                        + "compiler_identifier, "
                        + "package_generated_at, "
                        + "evidence_produced_at, "
                        + "reinstatement_basis_activation_request_identifier) "
                        + "values "
                        + "(?, ?, ?, ?, ?, 'SUCCEEDED', "
                        + "'compiler-1', "
                        + "cast(? as timestamptz), "
                        + "cast(? as timestamptz), ?)",
                MERCHANT,
                validationEvidenceIdentifier,
                INITIAL_REVISION,
                SEMANTIC_RELEASE,
                resolvedPackageEvidenceIdentifier,
                NOW.minusSeconds(100).toString(),
                NOW.minusSeconds(90).toString(),
                basisActivationRequestIdentifier
        );
    }

    private void insertImpactEvidence(
            String impactReviewEvidenceIdentifier,
            String validationEvidenceIdentifier,
            String resolvedPackageEvidenceIdentifier,
            String basisActivationRequestIdentifier
    ) {
        dsl.execute(
                "insert into configuration_impact_review_evidence "
                        + "(merchant_identifier, "
                        + "impact_review_evidence_identifier, "
                        + "configuration_revision_identifier, "
                        + "semantic_registry_release_identifier, "
                        + "validation_evidence_identifier, "
                        + "resolved_package_evidence_identifier, "
                        + "impact_analysis_completed_at, "
                        + "reinstatement_basis_activation_request_identifier) "
                        + "values "
                        + "(?, ?, ?, ?, ?, ?, "
                        + "cast(? as timestamptz), ?)",
                MERCHANT,
                impactReviewEvidenceIdentifier,
                INITIAL_REVISION,
                SEMANTIC_RELEASE,
                validationEvidenceIdentifier,
                resolvedPackageEvidenceIdentifier,
                NOW.minusSeconds(60).toString(),
                basisActivationRequestIdentifier
        );
    }

    private void moveCurrentThroughThirdAndBackToSecond() {
        insertActivation(
                "activate-3",
                THIRD_REVISION,
                THIRD_RELEASE,
                SECOND_REVISION,
                SECOND_REVISION,
                NOW.plusSeconds(60)
        );

        int movedToThird =
                dsl.execute(
                        "update merchant_current_configuration_activation "
                                + "set activation_request_identifier = ?, "
                                + "configuration_revision_identifier = ?, "
                                + "release_identifier = ? "
                                + "where merchant_identifier = ? "
                                + "and activation_request_identifier = ?",
                        "activate-3",
                        THIRD_REVISION,
                        THIRD_RELEASE,
                        MERCHANT,
                        SECOND_ACTIVATION
                );

        assertEquals(
                1,
                movedToThird
        );

        insertActivation(
                "activate-2-cycle-2",
                SECOND_REVISION,
                SECOND_RELEASE,
                THIRD_REVISION,
                THIRD_REVISION,
                NOW.plusSeconds(120)
        );

        int movedBackToSecond =
                dsl.execute(
                        "update merchant_current_configuration_activation "
                                + "set activation_request_identifier = ?, "
                                + "configuration_revision_identifier = ?, "
                                + "release_identifier = ? "
                                + "where merchant_identifier = ? "
                                + "and activation_request_identifier = ?",
                        "activate-2-cycle-2",
                        SECOND_REVISION,
                        SECOND_RELEASE,
                        MERCHANT,
                        "activate-3"
                );

        assertEquals(
                1,
                movedBackToSecond
        );
    }

    private String currentRevision() {
        return dsl.fetchOne(
                "select configuration_revision_identifier "
                        + "from merchant_current_configuration_activation "
                        + "where merchant_identifier = ?",
                MERCHANT
        ).get(0, String.class);
    }

    private String currentActivationRequestIdentifier() {
        return dsl.fetchOne(
                "select activation_request_identifier "
                        + "from merchant_current_configuration_activation "
                        + "where merchant_identifier = ?",
                MERCHANT
        ).get(0, String.class);
    }

    private void clear() {
        dsl.execute(
                "truncate table "
                        + "configuration_activation_publication_intent, "
                        + "merchant_current_configuration_activation, "
                        + "configuration_activation, "
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
                        + "onboarding_start_request, "
                        + "onboarding_case, "
                        + "merchant_controller_relationship, "
                        + "merchant_account cascade"
        );
    }

    private static String required(String name) {
        String value = System.getenv(name);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing " + name
            );
        }

        return value;
    }
}
