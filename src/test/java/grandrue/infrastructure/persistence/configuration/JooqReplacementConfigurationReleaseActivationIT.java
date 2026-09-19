package grandrue.infrastructure.persistence.configuration;

import grandrue.application.MerchantScope;
import grandrue.infrastructure.persistence.deployment
        .JooqServingDeploymentAdmissionSnapshotAuthority;
import grandrue.infrastructure.persistence.onboarding
        .JooqOnboardingCaseEvidenceStore;
import grandrue.infrastructure.persistence.release
        .JooqSemanticReleaseAdmissionAuthority;
import grandrue.runtime.AuthenticationProvenance;
import grandrue.runtime.ExecutionPrincipal;
import grandrue.runtime.TrustedExecutionContext;
import grandrue.semantic.configuration.ApproveConfigurationReinstatementCommand;
import grandrue.semantic.configuration.ApproveNonInitialConfigurationRevisionCommand;
import grandrue.semantic.configuration.ConfigurationActivation;
import grandrue.semantic.configuration.ConfigurationActivationAdmissionAuthority;
import grandrue.semantic.configuration.ConfigurationActivationRequest;
import grandrue.semantic.configuration.ConfigurationActivationStatus;
import grandrue.semantic.configuration.ConfigurationImpactAnalysisResult;
import grandrue.semantic.configuration.ConfigurationNewActivityRequirementSetIdentity;
import grandrue.semantic.configuration.ConfigurationPublication;
import grandrue.semantic.configuration.ConfigurationRelease;
import grandrue.semantic.configuration.ConfigurationRevisionApproval;
import grandrue.semantic.configuration.InMemoryConfigurationPublication;
import grandrue.semantic.configuration.MerchantConfiguration;
import grandrue.semantic.configuration.OrdinaryNewConfigurationSemanticReleaseAuthority;
import grandrue.semantic.configuration.RecordConfigurationImpactReviewEvidenceCommand;
import grandrue.semantic.configuration.RecordConfigurationValidationEvidenceCommand;
import grandrue.semantic.executable.ExecutableMerchantModel;
import grandrue.semantic.release.RecordSemanticReleasePurposeAdmissionDecisionCommand;
import grandrue.semantic.release.SemanticReleaseAdmissionDisposition;
import grandrue.semantic.release.SemanticReleasePurpose;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class JooqReplacementConfigurationReleaseActivationIT {

    private static final MerchantScope SCOPE =
            new MerchantScope("merchant-acme");

    private static final Instant NOW =
            Instant.parse("2026-09-11T16:30:00Z");

    private static final String CONTROLLER = "controller-a";
    private static final String SEMANTIC_RELEASE = "release-21";

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

        clear();
        seedConfigurationAuthority();
        seedServingAdmission();
    }

    @Test
    void replacement_activation_uses_exact_current_approval_package() {
        ConfigurationPublication publication =
                new InMemoryConfigurationPublication();

        ConfigurationRelease first =
                release(1, Optional.empty());

        ConfigurationRelease second =
                release(
                        2,
                        Optional.of("configuration-1")
                );

        publication.publish(first);
        publication.publish(second);

        seedCurrentActivation(first.releaseIdentifier());

        JooqNonInitialConfigurationRevisionApprovalAuthority approvals =
                new JooqNonInitialConfigurationRevisionApprovalAuthority(
                        dsl,
                        transactions
                );

        approvals.approve(
                approvalCommand("approval-2"),
                trustedContext()
        );

        JooqConfigurationReleaseActivation activation =
                exactReplacementActivation(
                        publication,
                        approvals
                );

        ConfigurationActivation committed = activation.activate(
                        new ConfigurationActivationRequest(
                                "activate-2",
                                second.releaseIdentifier(),
                                Optional.of("configuration-1"),
                                CONTROLLER
                        )
                )
                .activation()
                .orElseThrow();

        assertEquals(
                "configuration-2",
                committed.configurationRevisionIdentifier()
        );

        assertEquals(
                Optional.of("configuration-1"),
                committed.replacedConfigurationRevisionIdentifier()
        );

        assertEquals(
                "package-2",
                committed.admissionEvidence()
                        .orElseThrow()
                        .resolvedPackageEvidenceIdentifier()
        );

        assertEquals(
                second,
                activation.current("merchant-acme")
                        .orElseThrow()
                        .release()
        );

        assertEquals(
                2,
                dsl.fetchCount(
                        DSL.table(
                                DSL.name("configuration_activation")
                        )
                )
        );
    }

    @Test
    void historical_activation_request_cannot_be_reused_after_configuration_cycle_moves_on() {
        ConfigurationPublication publication =
                new InMemoryConfigurationPublication();

        ConfigurationRelease first =
                release(1, Optional.empty());

        ConfigurationRelease second =
                release(
                        2,
                        Optional.of("configuration-1")
                );

        publication.publish(first);
        publication.publish(second);

        seedCurrentActivation(first.releaseIdentifier());

        JooqNonInitialConfigurationRevisionApprovalAuthority approvals =
                new JooqNonInitialConfigurationRevisionApprovalAuthority(
                        dsl,
                        transactions
                );

        approvals.approve(
                approvalCommand("approval-2"),
                trustedContext()
        );

        JooqConfigurationReleaseActivation activation =
                exactReplacementActivation(
                        publication,
                        approvals
                );

        ConfigurationActivationRequest originalReplacement =
                new ConfigurationActivationRequest(
                        "activate-2",
                        second.releaseIdentifier(),
                        Optional.of("configuration-1"),
                        CONTROLLER
                );

        assertEquals(
                ConfigurationActivationStatus.SUCCESS,
                activation.activate(originalReplacement).status()
        );

        /*
         * Historical-state fixture only: represent a later independently
         * authorised B -> A reinstatement. This test isolates replay semantics;
         * it does not claim direct SQL as the production reinstatement path.
         */
        seedHistoricalReinstatement(
                first.releaseIdentifier()
        );

        assertEquals(
                first,
                activation.current("merchant-acme")
                        .orElseThrow()
                        .release()
        );

        /*
         * activate-2 belongs to the earlier A -> B mutation. The fact that A
         * later became current again must not make that historical request a
         * lost-acknowledgement replay of a new A -> B decision.
         */
        assertNotEquals(
                ConfigurationActivationStatus.SUCCESS,
                activation.activate(originalReplacement).status()
        );

        assertEquals(
                first,
                activation.current("merchant-acme")
                        .orElseThrow()
                        .release()
        );

        assertEquals(
                3,
                dsl.fetchCount(
                        DSL.table(
                                DSL.name("configuration_activation")
                        )
                )
        );
    }

    @Test
    void superseded_initial_revision_without_fresh_reinstatement_approval_is_approval_required() {
        ConfigurationPublication publication =
                new InMemoryConfigurationPublication();

        ConfigurationRelease first =
                release(1, Optional.empty());

        ConfigurationRelease second =
                release(
                        2,
                        Optional.of("configuration-1")
                );

        publication.publish(first);
        publication.publish(second);

        seedCurrentActivation(first.releaseIdentifier());

        JooqNonInitialConfigurationRevisionApprovalAuthority approvals =
                new JooqNonInitialConfigurationRevisionApprovalAuthority(
                        dsl,
                        transactions
                );

        approvals.approve(
                approvalCommand("approval-2-for-initial-reinstatement-proof"),
                trustedContext()
        );

        JooqConfigurationReleaseActivation activation =
                exactReplacementActivation(
                        publication,
                        approvals
                );

        assertEquals(
                ConfigurationActivationStatus.SUCCESS,
                activation.activate(
                        new ConfigurationActivationRequest(
                                "activate-2-before-initial-reinstatement-proof",
                                second.releaseIdentifier(),
                                Optional.of("configuration-1"),
                                CONTROLLER
                        )
                ).status()
        );

        assertEquals(
                second,
                activation.current("merchant-acme")
                        .orElseThrow()
                        .release()
        );

        /*
         * Revision 1 has a prior committed activation and is now superseded.
         * Under MS-PROT-040 v1.8 it is therefore a reinstatement candidate even
         * though its immutable historical base is empty. No fresh bounded
         * reinstatement approval is supplied in this fixture, so the activation
         * must fail closed as approval-required rather than being misclassified
         * as a forward-replacement base conflict.
         */
        assertEquals(
                ConfigurationActivationStatus.APPROVAL_REQUIRED,
                activation.activate(
                        new ConfigurationActivationRequest(
                                "activate-reinstate-initial-without-fresh-approval",
                                first.releaseIdentifier(),
                                Optional.of("configuration-2"),
                                CONTROLLER
                        )
                ).status()
        );

        assertEquals(
                second,
                activation.current("merchant-acme")
                        .orElseThrow()
                        .release()
        );

        assertEquals(
                2,
                dsl.fetchCount(
                        DSL.table(
                                DSL.name("configuration_activation")
                        )
                )
        );
    }

    @Test
    void fresh_basis_affined_approval_reinstates_superseded_initial_revision() {
        ConfigurationPublication publication =
                new InMemoryConfigurationPublication();

        ConfigurationRelease first =
                release(1, Optional.empty());

        ConfigurationRelease second =
                release(
                        2,
                        Optional.of("configuration-1")
                );

        publication.publish(first);
        publication.publish(second);

        seedCurrentActivation(first.releaseIdentifier());

        JooqNonInitialConfigurationRevisionApprovalAuthority replacements =
                new JooqNonInitialConfigurationRevisionApprovalAuthority(
                        dsl,
                        transactions
                );

        replacements.approve(
                approvalCommand("approval-2-for-reinstatement"),
                trustedContext()
        );

        JooqConfigurationReleaseActivation replacementActivation =
                exactReplacementActivation(
                        publication,
                        replacements
                );

        assertEquals(
                ConfigurationActivationStatus.SUCCESS,
                replacementActivation.activate(
                        new ConfigurationActivationRequest(
                                "activate-2-for-reinstatement",
                                second.releaseIdentifier(),
                                Optional.of("configuration-1"),
                                CONTROLLER
                        )
                ).status()
        );

        seedFreshReinstatementEvidence(
                first,
                "activate-2-for-reinstatement"
        );

        JooqConfigurationReinstatementApprovalAuthority reinstatements =
                new JooqConfigurationReinstatementApprovalAuthority(
                        dsl,
                        transactions
                );

        reinstatements.approve(
                reinstatementApprovalCommand(
                        "activate-2-for-reinstatement"
                ),
                trustedContext()
        );

        JooqConfigurationReleaseActivation reinstatementActivation =
                exactReinstatementActivation(
                        publication,
                        replacements,
                        reinstatements
                );

        ConfigurationActivation committed =
                reinstatementActivation.activate(
                                new ConfigurationActivationRequest(
                                        "activate-reinstate-1",
                                        first.releaseIdentifier(),
                                        Optional.of("configuration-2"),
                                        CONTROLLER
                                )
                        )
                        .activation()
                        .orElseThrow();

        assertEquals(
                "configuration-1",
                committed.configurationRevisionIdentifier()
        );

        assertEquals(
                Optional.of("configuration-2"),
                committed.replacedConfigurationRevisionIdentifier()
        );

        assertEquals(
                "package-reinstate-1",
                committed.admissionEvidence()
                        .orElseThrow()
                        .resolvedPackageEvidenceIdentifier()
        );

        assertEquals(
                first,
                reinstatementActivation.current("merchant-acme")
                        .orElseThrow()
                        .release()
        );

        assertEquals(
                3,
                dsl.fetchCount(
                        DSL.table(
                                DSL.name("configuration_activation")
                        )
                )
        );
    }

    @Test
    void never_activated_stale_candidate_is_not_classified_as_reinstatement() {
        ConfigurationPublication publication =
                new InMemoryConfigurationPublication();

        ConfigurationRelease first =
                release(1, Optional.empty());

        ConfigurationRelease second =
                release(
                        2,
                        Optional.of("configuration-1")
                );

        ConfigurationRelease staleNeverActivated =
                release(
                        3,
                        Optional.of("configuration-1")
                );

        publication.publish(first);
        publication.publish(second);
        publication.publish(staleNeverActivated);

        seedCurrentActivation(first.releaseIdentifier());

        JooqNonInitialConfigurationRevisionApprovalAuthority approvals =
                new JooqNonInitialConfigurationRevisionApprovalAuthority(
                        dsl,
                        transactions
                );

        approvals.approve(
                approvalCommand("approval-2-before-stale-candidate"),
                trustedContext()
        );

        JooqConfigurationReleaseActivation activation =
                exactReplacementActivation(
                        publication,
                        approvals
                );

        assertEquals(
                ConfigurationActivationStatus.SUCCESS,
                activation.activate(
                        new ConfigurationActivationRequest(
                                "activate-2-before-stale-candidate",
                                second.releaseIdentifier(),
                                Optional.of("configuration-1"),
                                CONTROLLER
                        )
                ).status()
        );

        /*
         * The target has never had a committed activation, so it cannot enter
         * the reinstatement path. Its immutable base is also no longer the
         * current revision, making the request an activation conflict.
         *
         * Authority: MS-PROT-040 v1.8,
         * designs/MS-PROT-040 v1.8 — Configuration Reinstatement Decision
         * Affinity Amendment.md, §§10 and 17.
         */
        assertEquals(
                ConfigurationActivationStatus.ACTIVATION_CONFLICT,
                activation.activate(
                        new ConfigurationActivationRequest(
                                "activate-never-active-stale-candidate",
                                staleNeverActivated.releaseIdentifier(),
                                Optional.of("configuration-2"),
                                CONTROLLER
                        )
                ).status()
        );

        assertEquals(
                second,
                activation.current("merchant-acme")
                        .orElseThrow()
                        .release()
        );

        assertEquals(
                2,
                dsl.fetchCount(
                        DSL.table(
                                DSL.name("configuration_activation")
                        )
                )
        );
    }

    @Test
    void weak_generic_approval_cannot_admit_real_replacement() {
        ConfigurationPublication publication =
                new InMemoryConfigurationPublication();

        ConfigurationRelease first =
                release(1, Optional.empty());

        ConfigurationRelease second =
                release(
                        2,
                        Optional.of("configuration-1")
                );

        publication.publish(first);
        publication.publish(second);

        seedCurrentActivation(first.releaseIdentifier());

        JooqConfigurationReleaseActivation activation =
                weakReplacementActivation(publication);

        assertEquals(
                ConfigurationActivationStatus.APPROVAL_REQUIRED,
                activation.activate(
                        new ConfigurationActivationRequest(
                                "activate-weak-2",
                                second.releaseIdentifier(),
                                Optional.of("configuration-1"),
                                CONTROLLER
                        )
                ).status()
        );

        assertEquals(
                first,
                activation.current("merchant-acme")
                        .orElseThrow()
                        .release()
        );

        assertEquals(
                1,
                dsl.fetchCount(
                        DSL.table(
                                DSL.name("configuration_activation")
                        )
                )
        );
    }

    @Test
    void transferred_controller_makes_historical_replacement_approval_inapplicable() {
        ConfigurationPublication publication =
                new InMemoryConfigurationPublication();

        ConfigurationRelease first =
                release(1, Optional.empty());

        ConfigurationRelease second =
                release(
                        2,
                        Optional.of("configuration-1")
                );

        publication.publish(first);
        publication.publish(second);

        seedCurrentActivation(first.releaseIdentifier());

        JooqNonInitialConfigurationRevisionApprovalAuthority approvals =
                new JooqNonInitialConfigurationRevisionApprovalAuthority(
                        dsl,
                        transactions
                );

        approvals.approve(
                approvalCommand("approval-historical"),
                trustedContext()
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

        JooqConfigurationReleaseActivation activation =
                exactReplacementActivation(
                        publication,
                        approvals
                );

        assertEquals(
                ConfigurationActivationStatus.APPROVAL_REQUIRED,
                activation.activate(
                        new ConfigurationActivationRequest(
                                "activate-after-transfer",
                                second.releaseIdentifier(),
                                Optional.of("configuration-1"),
                                CONTROLLER
                        )
                ).status()
        );

        assertEquals(
                first,
                activation.current("merchant-acme")
                        .orElseThrow()
                        .release()
        );
    }

    private JooqConfigurationReleaseActivation exactReplacementActivation(
            ConfigurationPublication publication,
            JooqNonInitialConfigurationRevisionApprovalAuthority
                    nonInitialApprovals
    ) {
        return new JooqConfigurationReleaseActivation(
                publication,
                (merchant, revision) -> Optional.empty(),
                (scope, revision) -> Optional.empty(),
                nonInitialApprovals,
                (principal, merchant, revision) -> true,
                (merchant, source, target) -> Optional.empty(),
                activationAdmission(),
                Clock.fixed(NOW, ZoneOffset.UTC),
                dsl,
                transactions,
                () -> UUID.randomUUID().toString()
        );
    }

    private JooqConfigurationReleaseActivation exactReinstatementActivation(
            ConfigurationPublication publication,
            JooqNonInitialConfigurationRevisionApprovalAuthority replacements,
            JooqConfigurationReinstatementApprovalAuthority reinstatements
    ) {
        return new JooqConfigurationReleaseActivation(
                publication,
                (merchant, revision) -> Optional.empty(),
                (scope, revision) -> Optional.empty(),
                replacements,
                reinstatements,
                (principal, merchant, revision) -> true,
                (merchant, source, target) -> Optional.empty(),
                activationAdmission(),
                Clock.fixed(NOW, ZoneOffset.UTC),
                dsl,
                transactions,
                () -> UUID.randomUUID().toString()
        );
    }

    private JooqConfigurationReleaseActivation weakReplacementActivation(
            ConfigurationPublication publication
    ) {
        return new JooqConfigurationReleaseActivation(
                publication,
                (merchant, revision) -> Optional.of(
                        new ConfigurationRevisionApproval(
                                merchant,
                                revision,
                                CONTROLLER,
                                NOW.minusSeconds(60)
                        )
                ),
                (scope, revision) -> Optional.empty(),
                (principal, merchant, revision) -> true,
                (merchant, source, target) -> Optional.empty(),
                activationAdmission(),
                Clock.fixed(NOW, ZoneOffset.UTC),
                dsl,
                transactions,
                () -> UUID.randomUUID().toString()
        );
    }

    private ConfigurationActivationAdmissionAuthority activationAdmission() {
        var requirementSets =
                new JooqConfigurationNewActivityRequirementSetAuthority(
                        dsl,
                        transactions,
                        resolvedPackage -> Set.of()
                );

        var snapshots =
                new JooqServingDeploymentAdmissionSnapshotAuthority(
                        dsl,
                        transactions
                );

        return new JooqConfigurationActivationAdmissionAuthority(
                dsl,
                requirementSets,
                snapshots
        );
    }

    private ApproveNonInitialConfigurationRevisionCommand approvalCommand(
            String requestIdentifier
    ) {
        return new ApproveNonInitialConfigurationRevisionCommand(
                requestIdentifier,
                SCOPE,
                "configuration-2",
                "validation-2",
                "impact-2",
                CONTROLLER,
                NOW.minusSeconds(30)
        );
    }

    private ApproveConfigurationReinstatementCommand
            reinstatementApprovalCommand(
                    String basisActivationRequestIdentifier
            ) {
        return new ApproveConfigurationReinstatementCommand(
                "approval-reinstate-1",
                SCOPE,
                "configuration-1",
                basisActivationRequestIdentifier,
                "validation-reinstate-1",
                "impact-reinstate-1",
                CONTROLLER,
                NOW.minusSeconds(15)
        );
    }

    private void seedFreshReinstatementEvidence(
            ConfigurationRelease target,
            String basisActivationRequestIdentifier
    ) {
        JooqConfigurationValidationEvidenceAuthority validations =
                new JooqConfigurationValidationEvidenceAuthority(
                        dsl,
                        transactions,
                        revisionAuthority()
                );

        validations.recordSuccessfulValidation(
                new RecordConfigurationValidationEvidenceCommand(
                        "validation-reinstate-1",
                        "package-reinstate-1",
                        target.resolvedPackage(),
                        NOW.minusSeconds(40),
                        Optional.of(basisActivationRequestIdentifier)
                )
        );

        insertRequirementSet(
                "package-reinstate-1",
                "configuration-1",
                ConfigurationNewActivityRequirementSetIdentity
                        .derive(Set.of())
        );

        JooqConfigurationImpactReviewEvidenceAuthority reviews =
                new JooqConfigurationImpactReviewEvidenceAuthority(
                        dsl,
                        transactions,
                        validations
                );

        reviews.recordCompletedReview(
                new RecordConfigurationImpactReviewEvidenceCommand(
                        "impact-reinstate-1",
                        new ConfigurationImpactAnalysisResult(
                                "merchant-acme",
                                "configuration-1",
                                SEMANTIC_RELEASE,
                                "validation-reinstate-1",
                                "package-reinstate-1",
                                List.of(
                                        "Previous configuration is reinstated"
                                ),
                                List.of(),
                                NOW.minusSeconds(30)
                        ),
                        Optional.of(basisActivationRequestIdentifier)
                )
        );
    }

    private JooqConfigurationRevisionAuthority revisionAuthority() {
        OrdinaryNewConfigurationSemanticReleaseAuthority releaseAuthority =
                () -> SEMANTIC_RELEASE;

        return new JooqConfigurationRevisionAuthority(
                dsl,
                transactions,
                new JooqOnboardingCaseEvidenceStore(
                        dsl,
                        transactions
                ),
                releaseAuthority
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

    private void seedCurrentActivation(String firstReleaseIdentifier) {
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
                        + "values "
                        + "('seed-activate-1','merchant-acme',"
                        + "'configuration-1',?,null,'controller-a',"
                        + "cast(? as timestamptz),null)",
                firstReleaseIdentifier,
                NOW.minusSeconds(300).toString()
        );

        dsl.execute(
                "insert into merchant_current_configuration_activation "
                        + "(merchant_identifier, "
                        + "activation_request_identifier, "
                        + "configuration_revision_identifier, "
                        + "release_identifier) "
                        + "values "
                        + "('merchant-acme','seed-activate-1',"
                        + "'configuration-1',?)",
                firstReleaseIdentifier
        );
    }

    private void seedHistoricalReinstatement(
            String reinstatedReleaseIdentifier
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
                        + "values "
                        + "('activate-reinstate-1','merchant-acme',"
                        + "'configuration-1',?,'configuration-2',"
                        + "'controller-a',cast(? as timestamptz),"
                        + "'configuration-2')",
                reinstatedReleaseIdentifier,
                NOW.plusSeconds(60).toString()
        );

        int updated =
                dsl.execute(
                        "update merchant_current_configuration_activation "
                                + "set activation_request_identifier="
                                + "'activate-reinstate-1', "
                                + "configuration_revision_identifier="
                                + "'configuration-1', "
                                + "release_identifier=? "
                                + "where merchant_identifier='merchant-acme' "
                                + "and configuration_revision_identifier="
                                + "'configuration-2'",
                        reinstatedReleaseIdentifier
                );

        assertEquals(
                1,
                updated
        );
    }

    private void seedConfigurationAuthority() {
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
                        + "purpose, lifecycle, current_revision,started_at) "
                        + "values "
                        + "('case-1','merchant-acme',"
                        + "'INITIAL_CONFIGURATION','COMPLETED',"
                        + "'complete-revision',cast(? as timestamptz))",
                NOW.toString()
        );

        dsl.execute(
                "insert into onboarding_case_revision "
                        + "(onboarding_case_identity, revision_identity, "
                        + "prior_revision_identity, "
                        + "mutation_request_identifier, mutation_kind, "
                        + "resulting_lifecycle, principal_reference, "
                        + "origin_identifier, committed_at) "
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
                        + "merchant_identifier, "
                        + "source_onboarding_case_identity, "
                        + "source_onboarding_case_revision, "
                        + "submitted_case_revision, submitted_by, "
                        + "origin_identifier, submitted_at) "
                        + "values "
                        + "('intent-1','submit-1','merchant-acme',"
                        + "'case-1','reviewed-revision',"
                        + "'submitted-revision','controller-a',"
                        + "'browser',cast(? as timestamptz))",
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

        dsl.execute(
                "insert into merchant_configuration_revision "
                        + "(merchant_identifier, "
                        + "configuration_revision_identifier, "
                        + "configuration_version, "
                        + "semantic_registry_release_identifier,"
                        + "base_configuration_revision_identifier, "
                        + "fulfilment_binding_set_identifier, "
                        + "fulfilment_binding_set_revision, "
                        + "source_initial_configuration_intent_identity, "
                        + "source_onboarding_case_identity, "
                        + "source_onboarding_case_revision, "
                        + "materialised_by, origin_identifier, "
                        + "materialised_at, "
                        + "onboarding_completion_revision) "
                        + "values "
                        + "('merchant-acme','configuration-1',1,?,"
                        + "null,null,null,'intent-1','case-1',"
                        + "'reviewed-revision','system','onboarding',"
                        + "cast(? as timestamptz),'complete-revision')",
                SEMANTIC_RELEASE,
                NOW.toString()
        );

        dsl.execute(
                "insert into merchant_configuration_revision "
                        + "(merchant_identifier, "
                        + "configuration_revision_identifier, "
                        + "configuration_version, "
                        + "semantic_registry_release_identifier,"
                        + "base_configuration_revision_identifier, "
                        + "fulfilment_binding_set_identifier, "
                        + "fulfilment_binding_set_revision, "
                        + "materialised_by, materialised_at, "
                        + "source_change_set_identifier, change_origin, "
                        + "change_source_identifier, change_proposed_at) "
                        + "values "
                        + "('merchant-acme','configuration-2',2,?,"
                        + "'configuration-1',null,null,'controller-a',"
                        + "cast(? as timestamptz),'change-1',"
                        + "'MERCHANT_INITIATED','merchant-settings',?)",
                SEMANTIC_RELEASE,
                NOW.toString(),
                NOW.toString()
        );

        insertValidation(
                "validation-2",
                "package-2"
        );

        /*
         * Deliberate decoy package for the same revision/release.
         * Exact approval affinity must select package-2 rather than allowing
         * serving admission to choose an arbitrary package.
         */
        insertValidation(
                "validation-decoy",
                "package-decoy"
        );

        dsl.execute(
                "insert into configuration_impact_review_evidence "
                        + "(merchant_identifier, "
                        + "impact_review_evidence_identifier, "
                        + "configuration_revision_identifier, "
                        + "semantic_registry_release_identifier,"
                        + "validation_evidence_identifier, "
                        + "resolved_package_evidence_identifier,"
                        + "impact_analysis_completed_at) "
                        + "values "
                        + "('merchant-acme','impact-2',"
                        + "'configuration-2',?,'validation-2',"
                        + "'package-2',cast(? as timestamptz))",
                SEMANTIC_RELEASE,
                NOW.toString()
        );

        dsl.execute(
                "insert into configuration_impact_review_effect "
                        + "(merchant_identifier, "
                        + "impact_review_evidence_identifier, "
                        + "effect_sequence, business_facing_effect) "
                        + "values "
                        + "('merchant-acme','impact-2',0,"
                        + "'Customer-facing configuration changes')"
        );
    }

    private void insertValidation(
            String validationIdentifier,
            String packageIdentifier
    ) {
        dsl.execute(
                "insert into configuration_validation_evidence "
                        + "(merchant_identifier, "
                        + "validation_evidence_identifier, "
                        + "configuration_revision_identifier, "
                        + "semantic_registry_release_identifier,"
                        + "resolved_package_evidence_identifier,"
                        + "validation_outcome, compiler_identifier, "
                        + "package_generated_at, evidence_produced_at) "
                        + "values "
                        + "('merchant-acme',?,'configuration-2',?,?,"
                        + "'SUCCEEDED','compiler-1',"
                        + "cast(? as timestamptz),"
                        + "cast(? as timestamptz))",
                validationIdentifier,
                SEMANTIC_RELEASE,
                packageIdentifier,
                NOW.toString(),
                NOW.toString()
        );
    }

    private void seedServingAdmission() {
        JooqSemanticReleaseAdmissionAuthority releases =
                new JooqSemanticReleaseAdmissionAuthority(
                        dsl,
                        transactions
                );

        releases.recordDecision(
                admissionDecision(
                        "admit-validation-release-21",
                        SemanticReleasePurpose
                                .NEW_CONFIGURATION_VALIDATION
                )
        );

        releases.recordDecision(
                admissionDecision(
                        "admit-activity-release-21",
                        SemanticReleasePurpose
                                .NEW_BUSINESS_ACTIVITY
                )
        );

        String emptyRequirementSet =
                ConfigurationNewActivityRequirementSetIdentity
                        .derive(Set.of());

        insertRequirementSet(
                "package-2",
                "configuration-2",
                emptyRequirementSet
        );

        insertRequirementSet(
                "package-decoy",
                "configuration-2",
                emptyRequirementSet
        );

        dsl.execute(
                "insert into serving_deployment_admission_snapshot "
                        + "(generation_identifier, cohort_identifier, "
                        + "evidence_recorded_at) "
                        + "values "
                        + "('generation-21','ORDINARY',"
                        + "cast(? as timestamptz))",
                NOW.toString()
        );

        dsl.execute(
                "insert into serving_deployment_materialised_release "
                        + "(generation_identifier, "
                        + "semantic_registry_release_identifier,"
                        + "packaged_bundle_content_digest) "
                        + "values "
                        + "('generation-21',?,"
                        + "'sha256:bundle-release-21')",
                SEMANTIC_RELEASE
        );

        dsl.execute(
                "insert into ordinary_serving_admission_control "
                        + "(cohort_identifier, lifecycle_state, "
                        + "promotion_epoch, "
                        + "current_generation_identifier, "
                        + "last_transition_at) "
                        + "values "
                        + "('ORDINARY','STABLE',7,'generation-21',"
                        + "cast(? as timestamptz))",
                NOW.toString()
        );
    }

    private void insertRequirementSet(
            String packageIdentifier,
            String configurationRevisionIdentifier,
            String requirementSetIdentifier
    ) {
        dsl.execute(
                "insert into configuration_new_activity_requirement_set "
                        + "(merchant_identifier, "
                        + "resolved_package_evidence_identifier,"
                        + "configuration_revision_identifier, "
                        + "semantic_registry_release_identifier,"
                        + "canonicalization_version, "
                        + "requirement_set_identifier, "
                        + "evidence_produced_at) "
                        + "values "
                        + "('merchant-acme',?,?,?,"
                        + "1,?,cast(? as timestamptz))",
                packageIdentifier,
                configurationRevisionIdentifier,
                SEMANTIC_RELEASE,
                requirementSetIdentifier,
                NOW.toString()
        );
    }

    private RecordSemanticReleasePurposeAdmissionDecisionCommand
            admissionDecision(
                    String decisionIdentifier,
                    SemanticReleasePurpose purpose
            ) {
        return new RecordSemanticReleasePurposeAdmissionDecisionCommand(
                decisionIdentifier,
                SEMANTIC_RELEASE,
                purpose,
                SemanticReleaseAdmissionDisposition.ADMITTED,
                "platform-release-controller",
                "change-ticket-21",
                NOW.minusSeconds(180)
        );
    }

    private static ConfigurationRelease release(
            long version,
            Optional<String> base
    ) {
        MerchantConfiguration configuration =
                new MerchantConfiguration(
                        "merchant-acme",
                        "configuration-" + version,
                        version,
                        SEMANTIC_RELEASE,
                        Set.of("booking"),
                        Set.of(),
                        base
                );

        return new ConfigurationRelease(
                "release-merchant-acme-" + version,
                configuration,
                new ExecutableMerchantModel(
                        "merchant-acme",
                        "configuration-" + version,
                        version,
                        SEMANTIC_RELEASE,
                        Set.of("booking"),
                        List.of(),
                        List.of()
                ),
                "grandrue-compiler-1",
                NOW.minusSeconds(120)
        );
    }

    private void clear() {
        dsl.execute(
                "truncate table "
                        + "configuration_activation_publication_intent, "
                        + "merchant_current_configuration_activation, "
                        + "configuration_activation, "
                        + "ordinary_new_configuration_semantic_release_pointer, "
                        + "ordinary_new_configuration_semantic_release_reference_revision, "
                        + "semantic_release_purpose_current_admission, "
                        + "semantic_release_purpose_admission_decision, "
                        + "ordinary_serving_generation_promotion, "
                        + "ordinary_serving_admission_control, "
                        + "serving_deployment_executable_support_contract, "
                        + "serving_deployment_executable_support_manifest, "
                        + "serving_deployment_materialised_release, "
                        + "serving_deployment_admission_snapshot, "
                        + "configuration_new_activity_required_contract, "
                        + "configuration_new_activity_requirement, "
                        + "configuration_new_activity_requirement_set, "
                        + "configuration_revision_approval, "
                        + "configuration_impact_review_finding, "
                        + "configuration_impact_review_effect, "
                        + "configuration_impact_review_evidence,"
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
