package grandrue.infrastructure.persistence.configuration;

import grandrue.application.MerchantScope;
import grandrue.runtime.AuthenticationProvenance;
import grandrue.runtime.ExecutionPrincipal;
import grandrue.runtime.TrustedExecutionContext;
import grandrue.semantic.configuration.ApproveNonInitialConfigurationRevisionCommand;
import grandrue.semantic.configuration.ConfigurationRevisionApproval;
import grandrue.semantic.configuration.ConfigurationRevisionApprovalFailureCategory;
import grandrue.semantic.configuration.ConfigurationRevisionApprovalPersistenceException;
import grandrue.semantic.configuration.NonInitialConfigurationRevisionApproval;
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
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JooqNonInitialConfigurationRevisionApprovalAuthorityIT {

    private static final Instant NOW =
            Instant.parse("2026-09-11T16:00:00Z");

    private DSLContext dsl;
    private DataSourceTransactionManager transactions;

    @BeforeEach
    void setUp() {
        DataSource source = new DriverManagerDataSource(
                required("GRANDRUE_TEST_POSTGRES_URL"),
                required("GRANDRUE_TEST_POSTGRES_USER"),
                required("GRANDRUE_TEST_POSTGRES_PASSWORD")
        );

        transactions = new DataSourceTransactionManager(source);

        Flyway.configure()
                .dataSource(source)
                .locations("classpath:db/migration")
                .load()
                .migrate();

        dsl = DSL.using(
                new TransactionAwareDataSourceProxy(source),
                SQLDialect.POSTGRES
        );

        dsl.execute(
                "truncate table "
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
                        + "onboarding_start_request, "
                        + "onboarding_case, "
                        + "merchant_controller_relationship, "
                        + "merchant_account cascade"
        );

        seed();
    }

    @Test
    void current_authenticated_controller_approves_exact_non_initial_evidence() {
        var authority = authority();

        NonInitialConfigurationRevisionApproval approval =
                authority.approve(
                        command("approval-1", "controller-a"),
                        context("controller-a", true)
                );

        assertEquals(
                "approval-1",
                approval.logicalApprovalRequestIdentifier()
        );
        assertEquals(
                "configuration-2",
                approval.configurationRevisionIdentifier()
        );
        assertEquals(
                "validation-2",
                approval.validationEvidenceIdentifier()
        );
        assertEquals(
                "impact-2",
                approval.impactReviewEvidenceIdentifier()
        );
        assertEquals(
                "package-2",
                approval.resolvedPackageEvidenceIdentifier()
        );
        assertEquals(
                "controller-rel-a",
                approval.controllerRelationshipIdentifier()
        );

        assertEquals(
                approval,
                authority.approvalByRequest("approval-1").orElseThrow()
        );

        ConfigurationRevisionApproval applicable =
                authority.approvalFor(
                                "merchant-acme",
                                "configuration-2"
                        )
                        .orElseThrow();

        assertEquals("merchant-acme", applicable.merchantIdentifier());
        assertEquals(
                "configuration-2",
                applicable.configurationRevisionIdentifier()
        );
        assertEquals(
                "controller-a",
                applicable.approvingPrincipalIdentifier()
        );
        assertEquals(NOW, applicable.approvedAt());

        assertEquals(
                1,
                dsl.fetchCount(
                        DSL.table(
                                DSL.name("configuration_revision_approval")
                        )
                )
        );
    }

    @Test
    void initial_revision_is_not_admitted_through_non_initial_authority() {
        var failure = assertThrows(
                ConfigurationRevisionApprovalPersistenceException.class,
                () -> authority().approve(
                        commandForRevision(
                                "approval-initial",
                                "configuration-1",
                                "controller-a"
                        ),
                        context("controller-a", true)
                )
        );

        assertEquals(
                ConfigurationRevisionApprovalFailureCategory
                        .REVISION_NOT_APPLICABLE_NON_INITIAL,
                failure.category()
        );
    }

    @Test
    void rejects_unauthenticated_or_non_controller_principal() {
        var unauthenticated = assertThrows(
                ConfigurationRevisionApprovalPersistenceException.class,
                () -> authority().approve(
                        command("approval-no-auth", "controller-a"),
                        context("controller-a", false)
                )
        );

        assertEquals(
                ConfigurationRevisionApprovalFailureCategory
                        .AUTHENTICATED_PRINCIPAL_REQUIRED,
                unauthenticated.category()
        );

        var wrong = assertThrows(
                ConfigurationRevisionApprovalPersistenceException.class,
                () -> authority().approve(
                        command("approval-wrong", "staff-b"),
                        context("staff-b", true)
                )
        );

        assertEquals(
                ConfigurationRevisionApprovalFailureCategory
                        .APPROVING_PRINCIPAL_NOT_CURRENT_CONTROLLER,
                wrong.category()
        );
    }

    @Test
    void blocking_impact_prevents_non_initial_approval() {
        dsl.execute(
                "insert into configuration_impact_review_finding "
                        + "(merchant_identifier, "
                        + "impact_review_evidence_identifier, "
                        + "finding_sequence, "
                        + "impact_classification, "
                        + "business_facing_finding) "
                        + "values "
                        + "('merchant-acme','impact-2',0,"
                        + "'BLOCKING','Required configuration is missing')"
        );

        var failure = assertThrows(
                ConfigurationRevisionApprovalPersistenceException.class,
                () -> authority().approve(
                        command("approval-blocked", "controller-a"),
                        context("controller-a", true)
                )
        );

        assertEquals(
                ConfigurationRevisionApprovalFailureCategory
                        .BLOCKING_IMPACT_REMAINS,
                failure.category()
        );

        assertEquals(
                0,
                dsl.fetchCount(
                        DSL.table(
                                DSL.name("configuration_revision_approval")
                        )
                )
        );
    }

    @Test
    void retry_is_exact_and_request_identity_cannot_change_intent() {
        var authority = authority();

        var first = authority.approve(
                command("approval-retry", "controller-a"),
                context("controller-a", true)
        );

        assertEquals(
                first,
                authority.approve(
                        command("approval-retry", "controller-a"),
                        context("controller-a", true)
                )
        );

        var conflict = assertThrows(
                ConfigurationRevisionApprovalPersistenceException.class,
                () -> authority.approve(
                        command(
                                "approval-retry",
                                "validation-2",
                                "impact-other",
                                "controller-a"
                        ),
                        context("controller-a", true)
                )
        );

        assertEquals(
                ConfigurationRevisionApprovalFailureCategory
                        .APPROVAL_REQUEST_IDENTITY_CONFLICT,
                conflict.category()
        );
    }

    @Test
    void historical_controller_approval_does_not_revive_when_same_identity_returns() {
        var authority = authority();

        var historical = authority.approve(
                command("approval-a", "controller-a"),
                context("controller-a", true)
        );

        dsl.execute(
                "update merchant_controller_relationship "
                        + "set lifecycle='ENDED' "
                        + "where controller_relationship_identifier="
                        + "'controller-rel-a'"
        );

        dsl.execute(
                "insert into merchant_controller_relationship "
                        + "(controller_relationship_identifier, "
                        + "merchant_identifier, identity_identifier, lifecycle) "
                        + "values "
                        + "('controller-rel-b','merchant-acme',"
                        + "'controller-b','ACTIVE')"
        );

        assertEquals(
                Optional.empty(),
                authority.approvalFor(
                        "merchant-acme",
                        "configuration-2"
                )
        );

        dsl.execute(
                "update merchant_controller_relationship "
                        + "set lifecycle='ENDED' "
                        + "where controller_relationship_identifier="
                        + "'controller-rel-b'"
        );

        dsl.execute(
                "insert into merchant_controller_relationship "
                        + "(controller_relationship_identifier, "
                        + "merchant_identifier, identity_identifier, lifecycle) "
                        + "values "
                        + "('controller-rel-c','merchant-acme',"
                        + "'controller-a','ACTIVE')"
        );

        /*
         * Same human identity, different Controller Relationship:
         * the historical approval must remain inapplicable.
         */
        assertEquals(
                Optional.empty(),
                authority.approvalFor(
                        "merchant-acme",
                        "configuration-2"
                )
        );

        var current = authority.approve(
                command("approval-c", "controller-a"),
                context("controller-a", true)
        );

        assertEquals(
                "controller-rel-c",
                current.controllerRelationshipIdentifier()
        );

        assertEquals(
                historical,
                authority.approvalByRequest("approval-a").orElseThrow()
        );

        assertEquals(
                2,
                dsl.fetchCount(
                        DSL.table(
                                DSL.name("configuration_revision_approval")
                        )
                )
        );
    }

    @Test
    void concurrent_duplicate_delivery_commits_one_logical_fact()
            throws Exception {
        var authority = authority();
        var command = command(
                "approval-concurrent",
                "controller-a"
        );

        try (var executor = Executors.newFixedThreadPool(2)) {
            var first = executor.submit(
                    () -> authority.approve(
                            command,
                            context("controller-a", true)
                    )
            );

            var second = executor.submit(
                    () -> authority.approve(
                            command,
                            context("controller-a", true)
                    )
            );

            assertEquals(first.get(), second.get());
        }

        assertEquals(
                1,
                dsl.fetchCount(
                        DSL.table(
                                DSL.name("configuration_revision_approval")
                        )
                )
        );
    }

    private JooqNonInitialConfigurationRevisionApprovalAuthority authority() {
        return new JooqNonInitialConfigurationRevisionApprovalAuthority(
                dsl,
                transactions
        );
    }

    private static ApproveNonInitialConfigurationRevisionCommand command(
            String request,
            String principal
    ) {
        return command(
                request,
                "validation-2",
                "impact-2",
                principal
        );
    }

    private static ApproveNonInitialConfigurationRevisionCommand command(
            String request,
            String validation,
            String impact,
            String principal
    ) {
        return new ApproveNonInitialConfigurationRevisionCommand(
                request,
                new MerchantScope("merchant-acme"),
                "configuration-2",
                validation,
                impact,
                principal,
                NOW
        );
    }

    private static ApproveNonInitialConfigurationRevisionCommand
            commandForRevision(
                    String request,
                    String revision,
                    String principal
            ) {
        return new ApproveNonInitialConfigurationRevisionCommand(
                request,
                new MerchantScope("merchant-acme"),
                revision,
                "validation-2",
                "impact-2",
                principal,
                NOW
        );
    }

    private static TrustedExecutionContext context(
            String principal,
            boolean authenticated
    ) {
        return new TrustedExecutionContext(
                new MerchantScope("merchant-acme"),
                new ExecutionPrincipal(principal),
                authenticated
                        ? Optional.of(
                                new AuthenticationProvenance(
                                        "session-1",
                                        principal,
                                        NOW.minusSeconds(1)
                                )
                        )
                        : Optional.empty()
        );
    }

    private void seed() {
        dsl.execute(
                "insert into merchant_account "
                        + "(merchant_identifier) "
                        + "values ('merchant-acme')"
        );

        dsl.execute(
                "insert into merchant_controller_relationship "
                        + "(controller_relationship_identifier, "
                        + "merchant_identifier, identity_identifier, lifecycle) "
                        + "values "
                        + "('controller-rel-a','merchant-acme',"
                        + "'controller-a','ACTIVE')"
        );

        dsl.execute(
                "insert into onboarding_case "
                        + "(onboarding_case_identity, merchant_identifier, "
                        + "purpose, lifecycle, current_revision, started_at) "
                        + "values "
                        + "('case-1','merchant-acme',"
                        + "'INITIAL_CONFIGURATION','COMPLETED',"
                        + "'complete-revision',cast(? as timestamptz))",
                NOW.toString()
        );

        dsl.execute(
                "insert into onboarding_case_revision "
                        + "(onboarding_case_identity, revision_identity, "
                        + "prior_revision_identity, mutation_request_identifier, "
                        + "mutation_kind, resulting_lifecycle, "
                        + "principal_reference, origin_identifier, committed_at) "
                        + "values "
                        + "('case-1','reviewed-revision',null,'start-1',"
                        + "'START','IN_PROGRESS','controller-a','browser',"
                        + "cast(? as timestamptz)),"
                        + "('case-1','submitted-revision','reviewed-revision',"
                        + "'submit-1','SUBMIT','SUBMITTED','controller-a',"
                        + "'browser',cast(? as timestamptz)),"
                        + "('case-1','complete-revision','submitted-revision',"
                        + "'complete-1','COMPLETE','COMPLETED','system',"
                        + "'configuration',cast(? as timestamptz))",
                NOW.toString(),
                NOW.toString(),
                NOW.toString()
        );

        dsl.execute(
                "insert into initial_configuration_intent "
                        + "(intent_identity, submission_request_identifier, "
                        + "merchant_identifier, source_onboarding_case_identity, "
                        + "source_onboarding_case_revision, "
                        + "submitted_case_revision, submitted_by, "
                        + "origin_identifier, submitted_at) "
                        + "values "
                        + "('intent-1','submit-1','merchant-acme','case-1',"
                        + "'reviewed-revision','submitted-revision',"
                        + "'controller-a','browser',cast(? as timestamptz))",
                NOW.toString()
        );

        dsl.execute(
                "insert into initial_configuration_intent_semantic_seed "
                        + "(intent_identity, seed_namespace, seed_identifier) "
                        + "values "
                        + "('intent-1','mainstreet.semantic','booking')"
        );

        dsl.execute(
                "insert into initial_configuration_intent_provenance "
                        + "(intent_identity, provenance_sequence, "
                        + "provenance_reference) "
                        + "values ('intent-1',0,'answer:1')"
        );

        /*
         * Initial base revision.
         */
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
                        + "materialised_by, origin_identifier, materialised_at, "
                        + "onboarding_completion_revision) "
                        + "values "
                        + "('merchant-acme','configuration-1',1,'release-21',"
                        + "null,null,null,'intent-1','case-1',"
                        + "'reviewed-revision','system','onboarding',"
                        + "cast(? as timestamptz),'complete-revision')",
                NOW.toString()
        );

        /*
         * Real non-initial revision shape established by V61.
         */
        dsl.execute(
                "insert into merchant_configuration_revision "
                        + "(merchant_identifier, "
                        + "configuration_revision_identifier, "
                        + "configuration_version, "
                        + "semantic_registry_release_identifier, "
                        + "base_configuration_revision_identifier, "
                        + "fulfilment_binding_set_identifier, "
                        + "fulfilment_binding_set_revision, "
                        + "materialised_by, materialised_at, "
                        + "source_change_set_identifier, "
                        + "change_origin, change_source_identifier, "
                        + "change_proposed_at) "
                        + "values "
                        + "('merchant-acme','configuration-2',2,'release-21',"
                        + "'configuration-1',null,null,'controller-a',"
                        + "cast(? as timestamptz),'change-1',"
                        + "'MERCHANT_INITIATED','merchant-settings',?)",
                NOW.toString(),
                NOW.toString()
        );

        dsl.execute(
                "insert into configuration_validation_evidence "
                        + "(merchant_identifier, "
                        + "validation_evidence_identifier, "
                        + "configuration_revision_identifier, "
                        + "semantic_registry_release_identifier, "
                        + "resolved_package_evidence_identifier, "
                        + "validation_outcome, compiler_identifier, "
                        + "package_generated_at, evidence_produced_at) "
                        + "values "
                        + "('merchant-acme','validation-2',"
                        + "'configuration-2','release-21','package-2',"
                        + "'SUCCEEDED','compiler-1',"
                        + "cast(? as timestamptz),cast(? as timestamptz))",
                NOW.toString(),
                NOW.toString()
        );

        dsl.execute(
                "insert into configuration_impact_review_evidence "
                        + "(merchant_identifier, "
                        + "impact_review_evidence_identifier, "
                        + "configuration_revision_identifier, "
                        + "semantic_registry_release_identifier, "
                        + "validation_evidence_identifier, "
                        + "resolved_package_evidence_identifier, "
                        + "impact_analysis_completed_at) "
                        + "values "
                        + "('merchant-acme','impact-2','configuration-2',"
                        + "'release-21','validation-2','package-2',"
                        + "cast(? as timestamptz))",
                NOW.toString()
        );

        dsl.execute(
                "insert into configuration_impact_review_effect "
                        + "(merchant_identifier, "
                        + "impact_review_evidence_identifier, "
                        + "effect_sequence, business_facing_effect) "
                        + "values "
                        + "('merchant-acme','impact-2',0,"
                        + "'New customer enquiries can be accepted')"
        );
    }

    private static String required(String name) {
        String value = System.getenv(name);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing " + name);
        }

        return value;
    }
}