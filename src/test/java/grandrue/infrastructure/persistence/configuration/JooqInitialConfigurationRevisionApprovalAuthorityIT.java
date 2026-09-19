package grandrue.infrastructure.persistence.configuration;

import grandrue.application.MerchantScope;
import grandrue.runtime.AuthenticationProvenance;
import grandrue.runtime.ExecutionPrincipal;
import grandrue.runtime.TrustedExecutionContext;
import grandrue.semantic.configuration.ApproveInitialConfigurationRevisionCommand;
import grandrue.semantic.configuration.ConfigurationRevisionApprovalFailureCategory;
import grandrue.semantic.configuration.ConfigurationRevisionApprovalPersistenceException;
import grandrue.semantic.configuration.InitialConfigurationRevisionApproval;
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

class JooqInitialConfigurationRevisionApprovalAuthorityIT {
    private static final Instant NOW = Instant.parse("2026-08-29T15:00:00Z");
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
        Flyway.configure().dataSource(source).locations("classpath:db/migration")
                .load().migrate();
        dsl = DSL.using(new TransactionAwareDataSourceProxy(source), SQLDialect.POSTGRES);
        dsl.execute("truncate table configuration_new_activity_required_contract, configuration_new_activity_requirement, configuration_new_activity_requirement_set, configuration_revision_approval, configuration_impact_review_finding, configuration_impact_review_effect, configuration_impact_review_evidence, configuration_validation_evidence, merchant_configuration_revision_policy, merchant_configuration_revision_capability, merchant_configuration_revision, initial_configuration_intent_unresolved_prompt, initial_configuration_intent_provenance, initial_configuration_intent_semantic_seed, initial_configuration_intent, onboarding_effective_answer, onboarding_answer_evidence, onboarding_case_revision, onboarding_start_request, onboarding_case, merchant_controller_relationship, merchant_account cascade");
        seed(false);
    }

    @Test
    void current_authenticated_controller_approves_exact_evidence() {
        InitialConfigurationRevisionApproval approval = authority().approve(command("approval-1", "controller-a"), context("controller-a", true));
        assertEquals("approval-1", approval.logicalApprovalRequestIdentifier());
        assertEquals("validation-1", approval.validationEvidenceIdentifier());
        assertEquals("impact-1", approval.impactReviewEvidenceIdentifier());
        assertEquals("package-1", approval.resolvedPackageEvidenceIdentifier());
        assertEquals("controller-rel-a", approval.controllerRelationshipIdentifier());
        assertEquals(approval, authority().approvalByRequest("approval-1").orElseThrow());
        assertEquals(approval, authority().currentApplicableApproval(new MerchantScope("merchant-acme"), "configuration-1").orElseThrow());
        assertEquals(1, dsl.fetchCount(DSL.table(DSL.name("configuration_revision_approval"))));
    }

    @Test
    void rejects_unauthenticated_or_non_controller_principal() {
        var unauthenticated = assertThrows(ConfigurationRevisionApprovalPersistenceException.class,
                () -> authority().approve(command("approval-no-auth", "controller-a"), context("controller-a", false)));
        assertEquals(ConfigurationRevisionApprovalFailureCategory.AUTHENTICATED_PRINCIPAL_REQUIRED, unauthenticated.category());
        var wrong = assertThrows(ConfigurationRevisionApprovalPersistenceException.class,
                () -> authority().approve(command("approval-wrong", "staff-b"), context("staff-b", true)));
        assertEquals(ConfigurationRevisionApprovalFailureCategory.APPROVING_PRINCIPAL_NOT_CURRENT_CONTROLLER, wrong.category());
    }

    @Test
    void blocking_impact_prevents_approval() {
        dsl.execute("insert into configuration_impact_review_finding (merchant_identifier, impact_review_evidence_identifier, finding_sequence, impact_classification, business_facing_finding) values ('merchant-acme','impact-1',0,'BLOCKING','Mandatory policy is missing')");
        var failure = assertThrows(ConfigurationRevisionApprovalPersistenceException.class,
                () -> authority().approve(command("approval-blocked", "controller-a"), context("controller-a", true)));
        assertEquals(ConfigurationRevisionApprovalFailureCategory.BLOCKING_IMPACT_REMAINS, failure.category());
        assertEquals(0, dsl.fetchCount(DSL.table(DSL.name("configuration_revision_approval"))));
    }

    @Test
    void missing_validation_or_impact_evidence_is_rejected_observably() {
        var validationFailure = assertThrows(
                ConfigurationRevisionApprovalPersistenceException.class,
                () -> authority().approve(
                        command("approval-no-validation", "validation-missing", "impact-1", "controller-a"),
                        context("controller-a", true)
                )
        );
        assertEquals(
                ConfigurationRevisionApprovalFailureCategory
                        .VALIDATION_EVIDENCE_NOT_FOUND_OR_AFFINITY_MISMATCH,
                validationFailure.category()
        );

        var impactFailure = assertThrows(
                ConfigurationRevisionApprovalPersistenceException.class,
                () -> authority().approve(
                        command("approval-no-impact", "validation-1", "impact-missing", "controller-a"),
                        context("controller-a", true)
                )
        );
        assertEquals(
                ConfigurationRevisionApprovalFailureCategory
                        .IMPACT_REVIEW_EVIDENCE_NOT_FOUND_OR_AFFINITY_MISMATCH,
                impactFailure.category()
        );
        assertEquals(0, dsl.fetchCount(DSL.table(DSL.name("configuration_revision_approval"))));
    }

    @Test
    void retry_is_exact_and_request_identity_cannot_change_intent() {
        var authority = authority();
        var first = authority.approve(command("approval-1", "controller-a"), context("controller-a", true));
        assertEquals(first, authority.approve(command("approval-1", "controller-a"), context("controller-a", true)));
        var conflict = assertThrows(ConfigurationRevisionApprovalPersistenceException.class,
                () -> authority.approve(command("approval-1", "controller-b"), context("controller-b", true)));
        assertEquals(ConfigurationRevisionApprovalFailureCategory.APPROVAL_REQUEST_IDENTITY_CONFLICT, conflict.category());
    }

    @Test
    void transfer_preserves_history_and_requires_new_current_controller_approval() {
        var authority = authority();
        var historical = authority.approve(command("approval-a", "controller-a"), context("controller-a", true));
        dsl.execute("update merchant_controller_relationship set lifecycle='ENDED' where controller_relationship_identifier='controller-rel-a'");
        dsl.execute("insert into merchant_controller_relationship (controller_relationship_identifier, merchant_identifier, identity_identifier, lifecycle) values ('controller-rel-b','merchant-acme','controller-b','ACTIVE')");
        assertEquals(Optional.empty(), authority.currentApplicableApproval(new MerchantScope("merchant-acme"), "configuration-1"));
        var current = authority.approve(command("approval-b", "controller-b"), context("controller-b", true));
        assertEquals("controller-b", current.approvingPrincipalIdentifier());
        assertEquals(historical, authority.approvalByRequest("approval-a").orElseThrow());
        assertEquals(2, dsl.fetchCount(DSL.table(DSL.name("configuration_revision_approval"))));
    }

    @Test
    void concurrent_duplicate_delivery_commits_one_fact() throws Exception {
        var authority = authority();
        var command = command("approval-concurrent", "controller-a");
        try (var executor = Executors.newFixedThreadPool(2)) {
            var first = executor.submit(() -> authority.approve(command, context("controller-a", true)));
            var second = executor.submit(() -> authority.approve(command, context("controller-a", true)));
            assertEquals(first.get(), second.get());
        }
        assertEquals(1, dsl.fetchCount(DSL.table(DSL.name("configuration_revision_approval"))));
    }

    private JooqInitialConfigurationRevisionApprovalAuthority authority() {
        return new JooqInitialConfigurationRevisionApprovalAuthority(dsl, transactions);
    }

    private static ApproveInitialConfigurationRevisionCommand command(String request, String principal) {
        return command(request, "validation-1", "impact-1", principal);
    }

    private static ApproveInitialConfigurationRevisionCommand command(
            String request,
            String validation,
            String impact,
            String principal
    ) {
        return new ApproveInitialConfigurationRevisionCommand(
                request,
                new MerchantScope("merchant-acme"),
                "configuration-1",
                validation,
                impact,
                principal,
                NOW
        );
    }

    private static TrustedExecutionContext context(String principal, boolean authenticated) {
        return new TrustedExecutionContext(new MerchantScope("merchant-acme"), new ExecutionPrincipal(principal), authenticated
                ? Optional.of(new AuthenticationProvenance("session-1", principal, NOW.minusSeconds(1)))
                : Optional.empty());
    }

    private void seed(boolean blocking) {
        dsl.execute("insert into merchant_account (merchant_identifier) values ('merchant-acme')");
        dsl.execute("insert into merchant_controller_relationship (controller_relationship_identifier, merchant_identifier, identity_identifier, lifecycle) values ('controller-rel-a','merchant-acme','controller-a','ACTIVE')");
        dsl.execute("insert into onboarding_case (onboarding_case_identity, merchant_identifier, purpose, lifecycle, current_revision, started_at) values ('case-1','merchant-acme','INITIAL_CONFIGURATION','COMPLETED','complete-revision',cast(? as timestamptz))", NOW.toString());
        dsl.execute("insert into onboarding_case_revision (onboarding_case_identity, revision_identity, prior_revision_identity, mutation_request_identifier, mutation_kind, resulting_lifecycle, principal_reference, origin_identifier, committed_at) values ('case-1','reviewed-revision',null,'start-1','START','IN_PROGRESS','controller-a','browser',cast(? as timestamptz)),('case-1','submitted-revision','reviewed-revision','submit-1','SUBMIT','SUBMITTED','controller-a','browser',cast(? as timestamptz)),('case-1','complete-revision','submitted-revision','complete-1','COMPLETE','COMPLETED','system','configuration',cast(? as timestamptz))", NOW.toString(), NOW.toString(), NOW.toString());
        dsl.execute("insert into initial_configuration_intent (intent_identity, submission_request_identifier, merchant_identifier, source_onboarding_case_identity, source_onboarding_case_revision, submitted_case_revision, submitted_by, origin_identifier, submitted_at) values ('intent-1','submit-1','merchant-acme','case-1','reviewed-revision','submitted-revision','controller-a','browser',cast(? as timestamptz))", NOW.toString());
        dsl.execute("insert into initial_configuration_intent_semantic_seed (intent_identity, seed_namespace, seed_identifier) values ('intent-1','mainstreet.semantic','booking')");
        dsl.execute("insert into initial_configuration_intent_provenance (intent_identity, provenance_sequence, provenance_reference) values ('intent-1',0,'answer:1')");
        dsl.execute("insert into merchant_configuration_revision (merchant_identifier, configuration_revision_identifier, configuration_version, semantic_registry_release_identifier, base_configuration_revision_identifier, fulfilment_binding_set_identifier, fulfilment_binding_set_revision, source_initial_configuration_intent_identity, source_onboarding_case_identity, source_onboarding_case_revision, materialised_by, origin_identifier, materialised_at, onboarding_completion_revision) values ('merchant-acme','configuration-1',1,'release-21',null,null,null,'intent-1','case-1','reviewed-revision','system','onboarding',cast(? as timestamptz),'complete-revision')", NOW.toString());
        dsl.execute("insert into configuration_validation_evidence (merchant_identifier, validation_evidence_identifier, configuration_revision_identifier, semantic_registry_release_identifier, resolved_package_evidence_identifier, validation_outcome, compiler_identifier, package_generated_at, evidence_produced_at) values ('merchant-acme','validation-1','configuration-1','release-21','package-1','SUCCEEDED','compiler-1',cast(? as timestamptz),cast(? as timestamptz))", NOW.toString(), NOW.toString());
        dsl.execute("insert into configuration_impact_review_evidence (merchant_identifier, impact_review_evidence_identifier, configuration_revision_identifier, semantic_registry_release_identifier, validation_evidence_identifier, resolved_package_evidence_identifier, impact_analysis_completed_at) values ('merchant-acme','impact-1','configuration-1','release-21','validation-1','package-1',cast(? as timestamptz))", NOW.toString());
        dsl.execute("insert into configuration_impact_review_effect (merchant_identifier, impact_review_evidence_identifier, effect_sequence, business_facing_effect) values ('merchant-acme','impact-1',0,'Customers can book appointments online')");
        if (blocking) dsl.execute("insert into configuration_impact_review_finding (merchant_identifier, impact_review_evidence_identifier, finding_sequence, impact_classification, business_facing_finding) values ('merchant-acme','impact-1',0,'BLOCKING','Blocked')");
    }

    private static String required(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) throw new IllegalStateException("Missing " + name);
        return value;
    }
}
