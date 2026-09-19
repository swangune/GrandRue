package grandrue.infrastructure.persistence.configuration;

import grandrue.application.MerchantScope;
import grandrue.infrastructure.persistence.onboarding.JooqOnboardingCaseEvidenceStore;
import grandrue.onboarding.InitialConfigurationIntentIdentity;
import grandrue.onboarding.OnboardingCaseIdentity;
import grandrue.onboarding.OnboardingCaseLifecycle;
import grandrue.onboarding.OnboardingCaseRevision;
import grandrue.semantic.configuration.ConfigurationRevisionFailureCategory;
import grandrue.semantic.configuration.ConfigurationRevisionPersistenceException;
import grandrue.semantic.configuration.MaterialiseInitialConfigurationRevisionCommand;
import grandrue.semantic.configuration.MerchantConfigurationRevision;
import grandrue.semantic.configuration.OrdinaryNewConfigurationSemanticReleaseAuthority;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterEach;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import grandrue.runtime.ExecutionPrincipal;
import grandrue.runtime.TrustedExecutionContext;
import grandrue.semantic.configuration.ConfigurationChangeSet;
import grandrue.semantic.configuration.MaterialiseConfigurationChangeCommand;
import grandrue.semantic.configuration.ConfigurationChangeAuthorizationAuthority;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.support.TransactionSynchronizationManager;

class JooqConfigurationRevisionAuthorityIT {

    private static final Instant NOW =
            Instant.parse("2026-08-29T05:00:00Z");

    private DSLContext dsl;
    private PlatformTransactionManager transactionManager;
    private AtomicReference<String> ordinaryRelease;

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
                        + "configuration_new_activity_required_contract, "
                        + "configuration_new_activity_requirement, "
                        + "configuration_new_activity_requirement_set, "
                        + "configuration_revision_approval, "
                        + "merchant_current_configuration_activation, "
                        + "configuration_activation, "
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
                        + "onboarding_case cascade"
        );
        ordinaryRelease = new AtomicReference<>("semantic-release-21");
        seedIntent("merchant-acme", "case-1", "intent-1");
    }

    @AfterEach
    void tearDown() {
        if (dsl != null) {
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
                            + "merchant_configuration_revision"
            );
        }
    }

    @Test
    void materialises_and_persists_one_immutable_first_revision() {
        JooqConfigurationRevisionAuthority authority = authority();

        MerchantConfigurationRevision revision = authority.materialiseInitial(
                command("intent-1", "configuration-1")
        );

        assertEquals("merchant-acme", revision.configuration().merchantIdentifier());
        assertEquals(
                "configuration-1",
                revision.configuration().configurationIdentifier()
        );
        assertEquals(1, revision.configuration().version());
        assertEquals(
                "semantic-release-21",
                revision.configuration().semanticRegistryVersion()
        );
        assertEquals(
                java.util.Set.of("publication", "enquiry"),
                revision.configuration().capabilityIdentifiers()
        );
        assertTrue(revision.configuration().baseConfigurationIdentifier().isEmpty());
        assertTrue(revision.configuration().policySelections().isEmpty());
        assertTrue(
                revision.configuration()
                        .fulfilmentBindingSetRevisionReference()
                        .isEmpty()
        );
        assertEquals(
                new InitialConfigurationIntentIdentity("intent-1"),
                revision.sourceInitialConfigurationIntentIdentity()
        );
        assertEquals(
                revision,
                authority.revision(
                        new MerchantScope("merchant-acme"),
                        "configuration-1"
                ).orElseThrow()
        );
        assertEquals(
                revision,
                authority.revisionForInitialIntent(
                        new InitialConfigurationIntentIdentity("intent-1")
                ).orElseThrow()
        );
        assertEquals(1, dsl.fetchCount(
                DSL.table(DSL.name("merchant_configuration_revision"))
        ));
        assertEquals(2, dsl.fetchCount(
                DSL.table(DSL.name(
                        "merchant_configuration_revision_capability"
                ))
        ));
        assertEquals(0, dsl.fetchCount(
                DSL.table(DSL.name("merchant_current_configuration_activation"))
        ));
        assertEquals(
                OnboardingCaseLifecycle.COMPLETED,
                onboardingStore().caseByIdentity(
                        new OnboardingCaseIdentity("case-1")
                ).orElseThrow().lifecycle()
        );
        assertEquals(
                new OnboardingCaseRevision("completed-configuration-1"),
                onboardingStore().caseByIdentity(
                        new OnboardingCaseIdentity("case-1")
                ).orElseThrow().currentRevision()
        );
    }

    @Test
    void retry_returns_original_revision_after_ordinary_release_advances() {
        JooqConfigurationRevisionAuthority authority = authority();
        MaterialiseInitialConfigurationRevisionCommand command =
                command("intent-1", "configuration-1");
        MerchantConfigurationRevision first =
                authority.materialiseInitial(command);

        ordinaryRelease.set("semantic-release-22");
        MerchantConfigurationRevision retry =
                authority.materialiseInitial(command);

        assertEquals(first, retry);
        assertEquals(
                "semantic-release-21",
                retry.configuration().semanticRegistryVersion()
        );
        assertEquals(1, dsl.fetchCount(
                DSL.table(DSL.name("merchant_configuration_revision"))
        ));
        assertEquals(
                1,
                dsl.fetchCount(
                        DSL.table(DSL.name("onboarding_case_revision")),
                        DSL.field(DSL.name("mutation_kind"), String.class)
                                .eq("COMPLETE")
                )
        );
    }

    @Test
    void unavailable_ordinary_release_rolls_back_without_completing_case() {
        ordinaryRelease.set(" ");

        ConfigurationRevisionPersistenceException failure = assertThrows(
                ConfigurationRevisionPersistenceException.class,
                () -> authority().materialiseInitial(command(
                        "intent-1",
                        "configuration-1"
                ))
        );

        assertEquals(
                ConfigurationRevisionFailureCategory
                        .ORDINARY_SEMANTIC_RELEASE_UNAVAILABLE,
                failure.category()
        );
        assertEquals(0, dsl.fetchCount(
                DSL.table(DSL.name("merchant_configuration_revision"))
        ));
        assertEquals(
                OnboardingCaseLifecycle.SUBMITTED,
                onboardingStore().caseByIdentity(
                        new OnboardingCaseIdentity("case-1")
                ).orElseThrow().lifecycle()
        );
    }

    @Test
    void one_intent_cannot_materialise_a_different_revision_identity() {
        JooqConfigurationRevisionAuthority authority = authority();
        authority.materialiseInitial(command("intent-1", "configuration-1"));

        ConfigurationRevisionPersistenceException conflict = assertThrows(
                ConfigurationRevisionPersistenceException.class,
                () -> authority.materialiseInitial(command(
                        "intent-1",
                        "configuration-different"
                ))
        );

        assertEquals(
                ConfigurationRevisionFailureCategory
                        .INITIAL_INTENT_IDENTITY_CONFLICT,
                conflict.category()
        );
        assertEquals(1, dsl.fetchCount(
                DSL.table(DSL.name("merchant_configuration_revision"))
        ));
    }

    @Test
    void concurrent_handoff_delivery_commits_at_most_one_revision()
            throws Exception {
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<Object> first = executor.submit(() -> materialiseAfterBarrier(
                    ready,
                    start,
                    command("intent-1", "configuration-a")
            ));
            Future<Object> second = executor.submit(() -> materialiseAfterBarrier(
                    ready,
                    start,
                    command("intent-1", "configuration-b")
            ));
            ready.await();
            start.countDown();

            List<Object> outcomes = List.of(first.get(), second.get());
            assertEquals(
                    1,
                    outcomes.stream()
                            .filter(MerchantConfigurationRevision.class::isInstance)
                            .count()
            );
            assertEquals(
                    1,
                    outcomes.stream()
                            .filter(ConfigurationRevisionPersistenceException
                                    .class::isInstance)
                            .map(ConfigurationRevisionPersistenceException
                                    .class::cast)
                            .filter(failure -> failure.category()
                                    == ConfigurationRevisionFailureCategory
                                            .INITIAL_INTENT_IDENTITY_CONFLICT)
                            .count()
            );
        }

        assertEquals(1, dsl.fetchCount(
                DSL.table(DSL.name("merchant_configuration_revision"))
        ));
    }

    @Test
    void unregistered_seed_namespace_is_not_invented_as_configuration() {
        seedIntent("merchant-beta", "case-2", "intent-unsupported");
        dsl.update(DSL.table(DSL.name(
                        "initial_configuration_intent_semantic_seed"
                )))
                .set(
                        DSL.field(DSL.name("seed_namespace"), String.class),
                        "unsupported.namespace"
                )
                .where(DSL.field(DSL.name("intent_identity"), String.class)
                        .eq("intent-unsupported"))
                .execute();

        ConfigurationRevisionPersistenceException failure = assertThrows(
                ConfigurationRevisionPersistenceException.class,
                () -> authority().materialiseInitial(command(
                        "intent-unsupported",
                        "configuration-unsupported"
                ))
        );

        assertEquals(
                ConfigurationRevisionFailureCategory
                        .UNSUPPORTED_INITIAL_INTENT,
                failure.category()
        );
        assertTrue(authority().revisionForInitialIntent(
                new InitialConfigurationIntentIdentity("intent-unsupported")
        ).isEmpty());
    }

    /**
     * Authority: MS-PROT-040 v1.0,
     * designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md,
     * §13 — Change set; §14 — Change-set provenance; §19 — Candidate generation; §49 — Configuration history.
     * MS-PROT-040 v1.5,
     * designs/MS-PROT-040 v1.5 — Release-Purpose Admission & Ordinary Release Reference Amendment.md,
     * §9 — Configuration-Revision Creation and Validation.
     */
    @Test
    void replacement_retains_truthful_change_provenance_and_common_content_without_onboarding_fabrication() {
        seedBase();
        var command = change("change-1", "replacement-1", "initial-1");
        var result = changes().materialise(command, changeContext());
        assertEquals(2, result.configuration().version());
        assertEquals(Optional.of("initial-1"), result.configuration().baseConfigurationIdentifier());
        assertEquals(command.changeSet(), result.changeSet());
        assertEquals(result.configuration(), authority().configuration(new MerchantScope("merchant-acme"), "replacement-1").orElseThrow());
        assertTrue(authority().revision(new MerchantScope("merchant-acme"), "replacement-1").isEmpty());
        assertTrue(authority().revision(new MerchantScope("merchant-acme"), "initial-1").isPresent());
        assertEquals(result, changes().revisionForChange(new MerchantScope("merchant-acme"), "change-1").orElseThrow());
        assertEquals(0, dsl.fetchCount(dsl.selectFrom("merchant_configuration_revision")
                .where("configuration_revision_identifier = 'replacement-1' and source_initial_configuration_intent_identity is not null")));
        assertEquals(0, dsl.fetchCount(dsl.selectFrom("configuration_activation")));
    }

    @Test
    void replacement_retry_preserves_pinned_release_and_original_commit_time() {
        seedBase();
        var command = change("change-1", "replacement-1", "initial-1");
        var original = changes().materialise(command, changeContext());
        ordinaryRelease.set("later-release");
        var retry = new MaterialiseConfigurationChangeCommand(command.changeSet(), "replacement-1", NOW.plusSeconds(60));
        assertEquals(original, changes().materialise(retry, changeContext()));
        assertEquals("semantic-release-21", original.configuration().semanticRegistryVersion());
    }

    @Test
    void replacement_rejects_changed_intent_and_duplicate_revision_identity() {
        seedBase();
        changes().materialise(change("change-1", "replacement-1", "initial-1"), changeContext());
        assertEquals(ConfigurationRevisionFailureCategory.CHANGE_SET_IDENTITY_CONFLICT,
                assertThrows(ConfigurationRevisionPersistenceException.class,
                        () -> changes().materialise(change("change-1", "replacement-2", "initial-1"), changeContext())).category());
        assertEquals(ConfigurationRevisionFailureCategory.CONFIGURATION_REVISION_IDENTITY_CONFLICT,
                assertThrows(ConfigurationRevisionPersistenceException.class,
                        () -> changes().materialise(change("change-2", "replacement-1", "initial-1"), changeContext())).category());
        var original = change("change-1", "replacement-1", "initial-1").changeSet();
        var different = new ConfigurationChangeSet(original.merchantScope(), original.changeSetIdentifier(),
                original.baseConfigurationRevisionIdentifier(), Set.of("publication"), Set.of(), Optional.empty(), original.provenance());
        assertEquals(ConfigurationRevisionFailureCategory.CHANGE_SET_IDENTITY_CONFLICT,
                assertThrows(ConfigurationRevisionPersistenceException.class, () -> changes().materialise(
                        new MaterialiseConfigurationChangeCommand(different, "replacement-1", NOW), changeContext())).category());
    }

    @Test
    void replacement_requires_exact_stored_base_and_available_ordinary_release() {
        seedBase();
        assertEquals(ConfigurationRevisionFailureCategory.BASE_CONFIGURATION_REVISION_NOT_FOUND,
                assertThrows(ConfigurationRevisionPersistenceException.class,
                        () -> changes().materialise(change("change-1", "replacement-1", "missing"), changeContext())).category());
        ordinaryRelease.set(null);
        assertEquals(ConfigurationRevisionFailureCategory.ORDINARY_SEMANTIC_RELEASE_UNAVAILABLE,
                assertThrows(ConfigurationRevisionPersistenceException.class,
                        () -> changes().materialise(change("change-1", "replacement-1", "initial-1"), changeContext())).category());
        assertTrue(changes().revisionForChange(new MerchantScope("merchant-acme"), "change-1").isEmpty());
    }

    @Test
    void replacement_authorization_is_transactional_and_scope_and_principal_cannot_be_forged() {
        seedBase();
        var command = change("change-1", "replacement-1", "initial-1");
        var denied = changes((change, context) -> {
            assertTrue(TransactionSynchronizationManager.isActualTransactionActive());
            return false;
        });
        assertEquals(ConfigurationRevisionFailureCategory.CHANGE_AUTHORIZATION_REJECTED,
                assertThrows(ConfigurationRevisionPersistenceException.class,
                        () -> denied.materialise(command, changeContext())).category());
        for (var context : List.of(
                new TrustedExecutionContext(new MerchantScope("foreign"), new ExecutionPrincipal("identity-42"), Optional.empty()),
                new TrustedExecutionContext(new MerchantScope("merchant-acme"), new ExecutionPrincipal("foreign"), Optional.empty()))) {
            assertEquals(ConfigurationRevisionFailureCategory.CHANGE_AUTHORIZATION_REJECTED,
                    assertThrows(ConfigurationRevisionPersistenceException.class,
                            () -> changes().materialise(command, context)).category());
        }
        assertTrue(changes().revisionForChange(new MerchantScope("merchant-acme"), "change-1").isEmpty());
    }

    @Test
    void replacement_rows_and_selections_roll_back_with_enclosing_transaction() {
        seedBase();
        assertThrows(IllegalStateException.class, () -> new TransactionTemplate(transactionManager).execute(status -> {
            changes().materialise(change("change-1", "replacement-1", "initial-1"), changeContext());
            throw new IllegalStateException("force enclosing rollback");
        }));
        assertTrue(changes().revisionForChange(new MerchantScope("merchant-acme"), "change-1").isEmpty());
        assertEquals(0, dsl.fetchCount(dsl.selectFrom("merchant_configuration_revision_capability")
                .where("configuration_revision_identifier = 'replacement-1'")));
    }

    @Test
    void concurrent_replacement_delivery_converges_on_one_revision() throws Exception {
        seedBase();
        var ready = new CountDownLatch(2);
        var start = new CountDownLatch(1);
        try (var executor = Executors.newFixedThreadPool(2)) {
            java.util.concurrent.Callable<Object> task = () -> {
                ready.countDown();
                if (!start.await(10, java.util.concurrent.TimeUnit.SECONDS)) throw new IllegalStateException("barrier");
                return changes().materialise(change("change-1", "replacement-1", "initial-1"), changeContext());
            };
            var first = executor.submit(task);
            var second = executor.submit(task);
            assertTrue(ready.await(10, java.util.concurrent.TimeUnit.SECONDS));
            start.countDown();
            assertEquals(first.get(15, java.util.concurrent.TimeUnit.SECONDS), second.get(15, java.util.concurrent.TimeUnit.SECONDS));
        }
        assertEquals(1, dsl.fetchCount(dsl.selectFrom("merchant_configuration_revision").where("source_change_set_identifier = 'change-1'")));
    }

    @Test
    void candidates_sharing_a_base_get_distinct_versions_without_merging() {
        seedBase();
        var first = changes().materialise(change("change-1", "replacement-1", "initial-1"), changeContext());
        var second = changes().materialise(change("change-2", "replacement-2", "initial-1"), changeContext());
        assertEquals(2, first.configuration().version());
        assertEquals(3, second.configuration().version());
        assertEquals(first.configuration().baseConfigurationIdentifier(), second.configuration().baseConfigurationIdentifier());
    }

    @Test
    void change_origin_policy_binding_and_nanosecond_proposal_evidence_round_trip() {
        seedBase();
        for (var origin : ConfigurationChangeSet.Origin.values()) {
            String id = "change-" + origin;
            var input = new ConfigurationChangeSet(new MerchantScope("merchant-acme"), id, "initial-1",
                    Set.of("enquiry"), Set.of(new grandrue.semantic.configuration.PolicySelection("enquiry", "mode", "general")),
                    Optional.of(new grandrue.fulfilment.FulfilmentBindingSetRevisionReference("bindings", 2)),
                    new ConfigurationChangeSet.Provenance(origin, "source-" + id, "identity-42", NOW.plusNanos(123456789)));
            var command = new MaterialiseConfigurationChangeCommand(input, "revision-" + origin, NOW);
            var stored = changes().materialise(command, changeContext());
            assertEquals(input, stored.changeSet());
            assertEquals(stored, changes().materialise(command, changeContext()));
        }
    }

    @Test
    void schema_rejects_mixed_or_missing_provenance_for_initial_and_changed_revisions() {
        seedBase();
        changes().materialise(change("change-1", "replacement-1", "initial-1"), changeContext());
        assertThrows(org.jooq.exception.DataAccessException.class, () -> dsl.execute(
                "update merchant_configuration_revision set source_change_set_identifier = 'invalid' "
                        + "where merchant_identifier = 'merchant-acme' and configuration_revision_identifier = 'initial-1'"));
        assertThrows(org.jooq.exception.DataAccessException.class, () -> dsl.execute(
                "update merchant_configuration_revision set change_origin = null "
                        + "where merchant_identifier = 'merchant-acme' and configuration_revision_identifier = 'replacement-1'"));
        assertThrows(org.jooq.exception.DataAccessException.class, () -> dsl.execute(
                "update merchant_configuration_revision set onboarding_completion_revision = 'fabricated' "
                        + "where merchant_identifier = 'merchant-acme' and configuration_revision_identifier = 'replacement-1'"));
        assertThrows(org.jooq.exception.DataAccessException.class, () -> dsl.execute(
                "update merchant_configuration_revision set fulfilment_binding_set_identifier = 'binding', "
                        + "fulfilment_binding_set_revision = null where merchant_identifier = 'merchant-acme' "
                        + "and configuration_revision_identifier = 'replacement-1'"));
        assertThrows(org.jooq.exception.DataAccessException.class, () -> dsl.execute(
                "update merchant_configuration_revision set fulfilment_binding_set_identifier = null, "
                        + "fulfilment_binding_set_revision = 2 where merchant_identifier = 'merchant-acme' "
                        + "and configuration_revision_identifier = 'replacement-1'"));
    }

    private void seedBase() {
        authority().materialiseInitial(command("intent-1", "initial-1"));
    }

    private JooqConfigurationChangeAuthority changes() {
        return changes((change, context) -> true);
    }

    private JooqConfigurationChangeAuthority changes(ConfigurationChangeAuthorizationAuthority authorization) {
        return new JooqConfigurationChangeAuthority(dsl, transactionManager, authority(), ordinaryRelease::get, authorization);
    }

    private static TrustedExecutionContext changeContext() {
        return new TrustedExecutionContext(new MerchantScope("merchant-acme"), new ExecutionPrincipal("identity-42"), Optional.empty());
    }

    private static MaterialiseConfigurationChangeCommand change(String id, String revision, String base) {
        return new MaterialiseConfigurationChangeCommand(new ConfigurationChangeSet(new MerchantScope("merchant-acme"),
                id, base, Set.of("enquiry"), Set.of(), Optional.empty(), new ConfigurationChangeSet.Provenance(
                ConfigurationChangeSet.Origin.MERCHANT_INITIATED, "request-" + id, "identity-42", NOW)), revision, NOW);
    }

    private Object materialiseAfterBarrier(
            CountDownLatch ready,
            CountDownLatch start,
            MaterialiseInitialConfigurationRevisionCommand command
    ) throws InterruptedException {
        ready.countDown();
        start.await();
        try {
            return authority().materialiseInitial(command);
        } catch (ConfigurationRevisionPersistenceException failure) {
            return failure;
        }
    }

    private JooqConfigurationRevisionAuthority authority() {
        OrdinaryNewConfigurationSemanticReleaseAuthority releaseAuthority =
                ordinaryRelease::get;
        return new JooqConfigurationRevisionAuthority(
                dsl,
                transactionManager,
                onboardingStore(),
                releaseAuthority
        );
    }

    private JooqOnboardingCaseEvidenceStore onboardingStore() {
        return new JooqOnboardingCaseEvidenceStore(dsl, transactionManager);
    }

    private static MaterialiseInitialConfigurationRevisionCommand command(
            String intent,
            String configuration
    ) {
        return new MaterialiseInitialConfigurationRevisionCommand(
                new InitialConfigurationIntentIdentity(intent),
                configuration,
                new OnboardingCaseRevision("completed-" + configuration),
                "configuration-bootstrap",
                Optional.of("onboarding-handoff"),
                NOW
        );
    }

    private void seedIntent(
            String merchant,
            String caseIdentity,
            String intentIdentity
    ) {
        dsl.execute(
                "insert into merchant_account (merchant_identifier) "
                        + "values (?) on conflict do nothing",
                merchant
        );
        dsl.execute(
                "insert into onboarding_case "
                        + "(onboarding_case_identity, merchant_identifier, "
                        + "purpose, lifecycle, current_revision, "
                        + "started_at) values (?, ?, 'INITIAL_CONFIGURATION', "
                        + "'SUBMITTED', ?, cast(? as timestamptz))",
                caseIdentity,
                merchant,
                "submitted-revision",
                NOW.toString()
        );
        dsl.execute(
                "insert into onboarding_case_revision "
                        + "(onboarding_case_identity, revision_identity, "
                        + "prior_revision_identity, mutation_request_identifier, "
                        + "mutation_kind, resulting_lifecycle, principal_reference, "
                        + "origin_identifier, committed_at) values "
                        + "(?, ?, null, ?, 'START', 'IN_PROGRESS', ?, ?, "
                        + "cast(? as timestamptz)), "
                        + "(?, ?, ?, ?, 'SUBMIT', 'SUBMITTED', ?, ?, "
                        + "cast(? as timestamptz))",
                caseIdentity,
                "reviewed-revision",
                "start-" + intentIdentity,
                "identity-42",
                "privileged-browser",
                NOW.toString(),
                caseIdentity,
                "submitted-revision",
                "reviewed-revision",
                "submit-" + intentIdentity,
                "identity-42",
                "privileged-browser",
                NOW.toString()
        );
        dsl.execute(
                "insert into initial_configuration_intent "
                        + "(intent_identity, submission_request_identifier, "
                        + "merchant_identifier, source_onboarding_case_identity, "
                        + "source_onboarding_case_revision, submitted_case_revision, "
                        + "submitted_by, origin_identifier, submitted_at) "
                        + "values (?, ?, ?, ?, ?, ?, ?, ?, "
                        + "cast(? as timestamptz))",
                intentIdentity,
                "submit-" + intentIdentity,
                merchant,
                caseIdentity,
                "reviewed-revision",
                "submitted-revision",
                "identity-42",
                "privileged-browser",
                NOW.toString()
        );
        dsl.execute(
                "insert into initial_configuration_intent_semantic_seed "
                        + "(intent_identity, seed_namespace, seed_identifier) "
                        + "values (?, 'mainstreet.semantic', 'publication'), "
                        + "(?, 'mainstreet.semantic', 'enquiry')",
                intentIdentity,
                intentIdentity
        );
        dsl.execute(
                "insert into initial_configuration_intent_provenance "
                        + "(intent_identity, provenance_sequence, "
                        + "provenance_reference) values (?, 0, ?)",
                intentIdentity,
                "answer:" + intentIdentity
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
