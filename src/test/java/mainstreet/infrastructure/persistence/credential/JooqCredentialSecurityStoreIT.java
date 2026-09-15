package mainstreet.infrastructure.persistence.credential;

import mainstreet.application.MerchantScope;
import mainstreet.credential.CredentialBinding;
import mainstreet.credential.CredentialBindingScope;
import mainstreet.credential.CredentialGeneration;
import mainstreet.credential.CredentialGenerationPolicy;
import mainstreet.credential.CredentialGenerationState;
import mainstreet.credential.CredentialTechnicalUse;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqCredentialSecurityStoreIT {

    private static final MerchantScope MERCHANT_A = new MerchantScope("merchant-a");
    private static final MerchantScope MERCHANT_B = new MerchantScope("merchant-b");
    private static final Instant T0 = Instant.parse("2026-08-24T15:00:00Z");

    private DataSource authoritativeDataSource;
    private DSLContext dsl;
    private DataSourceTransactionManager transactionManager;

    @BeforeEach
    void setUp() {
        authoritativeDataSource = new DriverManagerDataSource(
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_URL"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_USER"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_PASSWORD")
        );
        Flyway.configure()
                .dataSource(authoritativeDataSource)
                .locations("classpath:db/migration")
                .load()
                .migrate();
        dsl = DSL.using(
                new TransactionAwareDataSourceProxy(authoritativeDataSource),
                SQLDialect.POSTGRES
        );
        transactionManager = new DataSourceTransactionManager(authoritativeDataSource);

        dsl.execute("truncate table credential_generation_policy, credential_generation, credential_binding");
        dsl.execute("insert into merchant_account (merchant_identifier) values ('merchant-a'), ('merchant-b') on conflict do nothing");
    }

    @Test
    void merchant_and_platform_bindings_survive_store_recreation_without_fake_scope() {
        CredentialBinding merchant = merchantBinding(
                "binding-a",
                MERCHANT_A,
                "provider-connection-a"
        );
        CredentialBinding platform = new CredentialBinding(
                "binding-platform",
                CredentialBindingScope.PLATFORM,
                Optional.empty(),
                "notification-provider",
                "platform-provider-account-1",
                "SEND_NOTIFICATION",
                T0
        );

        store().registerBinding(merchant);
        store().registerBinding(platform);

        JooqCredentialSecurityStore restarted = store();
        assertEquals(merchant, restarted.binding("binding-a").orElseThrow());
        assertEquals(platform, restarted.binding("binding-platform").orElseThrow());
        assertTrue(restarted.binding("binding-platform")
                .orElseThrow().merchantScope().isEmpty());
    }

    @Test
    void binding_identity_replay_is_idempotent_but_cannot_change_connection_context() {
        JooqCredentialSecurityStore store = store();
        CredentialBinding original = merchantBinding(
                "binding-a",
                MERCHANT_A,
                "provider-connection-a"
        );
        assertEquals(original, store.registerBinding(original));
        assertEquals(original, store.registerBinding(original));

        CredentialBinding conflicting = merchantBinding(
                "binding-a",
                MERCHANT_A,
                "provider-connection-b"
        );
        assertThrows(
                IllegalStateException.class,
                () -> store.registerBinding(conflicting)
        );
        assertEquals(1, count("credential_binding"));
    }

    @Test
    void same_provider_type_for_two_merchants_remains_two_exact_credential_contexts() {
        CredentialBinding first = merchantBinding(
                "binding-a",
                MERCHANT_A,
                "stripe-account-shared-label"
        );
        CredentialBinding second = merchantBinding(
                "binding-b",
                MERCHANT_B,
                "stripe-account-shared-label"
        );

        store().registerBinding(first);
        store().registerBinding(second);

        assertEquals(MERCHANT_A, store().binding("binding-a")
                .orElseThrow().merchantScope().orElseThrow());
        assertEquals(MERCHANT_B, store().binding("binding-b")
                .orElseThrow().merchantScope().orElseThrow());
        assertEquals(2, count("credential_binding"));
    }

    @Test
    void generation_and_initial_policy_survive_recreation_and_are_use_bounded() {
        JooqCredentialSecurityStore store = store();
        store.registerBinding(merchantBinding(
                "binding-a",
                MERCHANT_A,
                "provider-connection-a"
        ));
        CredentialGeneration generation = generation(
                "generation-1",
                "binding-a",
                "secret-reference-1",
                T0
        );
        CredentialGenerationPolicy initial = policy(
                "policy-1",
                "generation-1",
                1,
                CredentialGenerationState.USABLE,
                Set.of(
                        CredentialTechnicalUse.NEW_EXECUTION,
                        CredentialTechnicalUse.VERIFICATION
                ),
                T0
        );

        assertEquals(generation, store.registerGeneration(generation, initial));
        assertEquals(generation, store.registerGeneration(generation, initial));

        JooqCredentialSecurityStore restarted = store();
        assertEquals(initial, restarted.currentPolicy("generation-1").orElseThrow());
        assertEquals(
                List.of(generation),
                restarted.eligibleGenerations(
                        "binding-a",
                        CredentialTechnicalUse.NEW_EXECUTION,
                        T0
                )
        );
        assertTrue(restarted.eligibleGenerations(
                "binding-a",
                CredentialTechnicalUse.EXISTING_OBLIGATION,
                T0
        ).isEmpty());
        assertEquals(1, count("credential_generation"));
        assertEquals(1, count("credential_generation_policy"));
    }

    @Test
    void bounded_rotation_overlap_preserves_verification_but_removes_old_generation_from_new_execution() {
        JooqCredentialSecurityStore store = store();
        store.registerBinding(merchantBinding(
                "binding-a",
                MERCHANT_A,
                "provider-connection-a"
        ));
        CredentialGeneration oldGeneration = generation(
                "generation-old",
                "binding-a",
                "secret-reference-old",
                T0
        );
        store.registerGeneration(oldGeneration, policy(
                "policy-old-1",
                "generation-old",
                1,
                CredentialGenerationState.USABLE,
                Set.of(
                        CredentialTechnicalUse.NEW_EXECUTION,
                        CredentialTechnicalUse.EXISTING_OBLIGATION,
                        CredentialTechnicalUse.VERIFICATION
                ),
                T0
        ));

        Instant replacementRegisteredAt = T0.plusSeconds(90);
        CredentialGeneration replacement = generation(
                "generation-new",
                "binding-a",
                "secret-reference-new",
                replacementRegisteredAt
        );
        store.registerGeneration(replacement, policy(
                "policy-new-1",
                "generation-new",
                1,
                CredentialGenerationState.USABLE,
                Set.of(
                        CredentialTechnicalUse.NEW_EXECUTION,
                        CredentialTechnicalUse.EXISTING_OBLIGATION,
                        CredentialTechnicalUse.VERIFICATION
                ),
                replacementRegisteredAt
        ));

        Instant retiringAt = T0.plusSeconds(100);
        CredentialGenerationPolicy retiring = policy(
                "policy-old-2",
                "generation-old",
                2,
                CredentialGenerationState.RETIRING,
                Set.of(
                        CredentialTechnicalUse.EXISTING_OBLIGATION,
                        CredentialTechnicalUse.VERIFICATION
                ),
                retiringAt
        );
        store.transitionGeneration(
                "generation-old",
                CredentialGenerationState.USABLE,
                retiring
        );

        assertEquals(
                List.of(oldGeneration),
                store.eligibleGenerations(
                        "binding-a",
                        CredentialTechnicalUse.NEW_EXECUTION,
                        replacementRegisteredAt.minusSeconds(1)
                )
        );
        assertEquals(
                List.of(replacement),
                store.eligibleGenerations(
                        "binding-a",
                        CredentialTechnicalUse.NEW_EXECUTION,
                        retiringAt
                )
        );
        assertEquals(
                List.of(replacement, oldGeneration),
                store.eligibleGenerations(
                        "binding-a",
                        CredentialTechnicalUse.VERIFICATION,
                        retiringAt
                )
        );
    }

    @Test
    void revoked_generation_is_terminal_and_cannot_be_resolved_for_any_use() {
        JooqCredentialSecurityStore store = store();
        registerUsableGeneration(store, "generation-1");
        CredentialGenerationPolicy revoked = policy(
                "policy-2",
                "generation-1",
                2,
                CredentialGenerationState.REVOKED,
                Set.of(),
                T0.plusSeconds(10)
        );
        store.transitionGeneration(
                "generation-1",
                CredentialGenerationState.USABLE,
                revoked
        );

        for (CredentialTechnicalUse use : CredentialTechnicalUse.values()) {
            assertTrue(store.eligibleGenerations(
                    "binding-a",
                    use,
                    T0.plusSeconds(10)
            ).isEmpty());
        }
        assertThrows(IllegalStateException.class, () -> store.transitionGeneration(
                "generation-1",
                CredentialGenerationState.REVOKED,
                policy(
                        "policy-3",
                        "generation-1",
                        3,
                        CredentialGenerationState.USABLE,
                        Set.of(CredentialTechnicalUse.NEW_EXECUTION),
                        T0.plusSeconds(20)
                )
        ));
        assertEquals(CredentialGenerationState.REVOKED,
                store.currentPolicy("generation-1").orElseThrow().state());
    }

    @Test
    void compromise_removes_future_use_without_rewriting_historical_eligibility() {
        JooqCredentialSecurityStore store = store();
        CredentialGeneration generation = registerUsableGeneration(store, "generation-1");
        Instant compromisedAt = T0.plusSeconds(30);
        store.transitionGeneration(
                "generation-1",
                CredentialGenerationState.USABLE,
                policy(
                        "policy-2",
                        "generation-1",
                        2,
                        CredentialGenerationState.COMPROMISED,
                        Set.of(),
                        compromisedAt
                )
        );

        assertEquals(
                List.of(generation),
                store.eligibleGenerations(
                        "binding-a",
                        CredentialTechnicalUse.NEW_EXECUTION,
                        compromisedAt.minusNanos(1_000)
                )
        );
        assertTrue(store.eligibleGenerations(
                "binding-a",
                CredentialTechnicalUse.NEW_EXECUTION,
                compromisedAt
        ).isEmpty());
    }

    @Test
    void concurrent_stale_state_transitions_commit_exactly_one_policy_change() throws Exception {
        registerUsableGeneration(store(), "generation-1");
        CountDownLatch start = new CountDownLatch(1);

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<Boolean> retire = executor.submit(() -> {
                start.await();
                return transitionSucceeded(
                        independentStore(),
                        policy(
                                "policy-retire",
                                "generation-1",
                                2,
                                CredentialGenerationState.RETIRING,
                                Set.of(CredentialTechnicalUse.VERIFICATION),
                                T0.plusSeconds(10)
                        )
                );
            });
            Future<Boolean> revoke = executor.submit(() -> {
                start.await();
                return transitionSucceeded(
                        independentStore(),
                        policy(
                                "policy-revoke",
                                "generation-1",
                                2,
                                CredentialGenerationState.REVOKED,
                                Set.of(),
                                T0.plusSeconds(10)
                        )
                );
            });
            start.countDown();

            assertEquals(1, List.of(retire.get(), revoke.get()).stream()
                    .filter(Boolean::booleanValue)
                    .count());
        }

        assertEquals(2, count("credential_generation_policy"));
        assertFalse(store().currentPolicy("generation-1").isEmpty());
    }

    @Test
    void policy_identity_replay_is_idempotent_and_different_evidence_is_rejected() {
        JooqCredentialSecurityStore store = store();
        registerUsableGeneration(store, "generation-1");
        CredentialGenerationPolicy retiring = policy(
                "policy-2",
                "generation-1",
                2,
                CredentialGenerationState.RETIRING,
                Set.of(CredentialTechnicalUse.VERIFICATION),
                T0.plusSeconds(10)
        );

        assertEquals(retiring, store.transitionGeneration(
                "generation-1",
                CredentialGenerationState.USABLE,
                retiring
        ));
        assertEquals(retiring, store.transitionGeneration(
                "generation-1",
                CredentialGenerationState.USABLE,
                retiring
        ));

        CredentialGenerationPolicy conflicting = policy(
                "policy-2",
                "generation-1",
                2,
                CredentialGenerationState.RETIRING,
                Set.of(CredentialTechnicalUse.EXISTING_OBLIGATION),
                T0.plusSeconds(10)
        );
        assertThrows(IllegalStateException.class, () -> store.transitionGeneration(
                "generation-1",
                CredentialGenerationState.RETIRING,
                conflicting
        ));
        assertEquals(2, count("credential_generation_policy"));
    }

    private CredentialGeneration registerUsableGeneration(
            JooqCredentialSecurityStore store,
            String generationIdentity
    ) {
        store.registerBinding(merchantBinding(
                "binding-a",
                MERCHANT_A,
                "provider-connection-a"
        ));
        CredentialGeneration generation = generation(
                generationIdentity,
                "binding-a",
                "secret-reference-" + generationIdentity,
                T0
        );
        store.registerGeneration(generation, policy(
                "policy-1",
                generationIdentity,
                1,
                CredentialGenerationState.USABLE,
                Set.of(
                        CredentialTechnicalUse.NEW_EXECUTION,
                        CredentialTechnicalUse.EXISTING_OBLIGATION,
                        CredentialTechnicalUse.VERIFICATION
                ),
                T0
        ));
        return generation;
    }

    private boolean transitionSucceeded(
            JooqCredentialSecurityStore store,
            CredentialGenerationPolicy policy
    ) {
        try {
            store.transitionGeneration(
                    "generation-1",
                    CredentialGenerationState.USABLE,
                    policy
            );
            return true;
        } catch (IllegalStateException expectedStaleState) {
            return false;
        }
    }

    private JooqCredentialSecurityStore store() {
        return new JooqCredentialSecurityStore(dsl, transactionManager);
    }

    private JooqCredentialSecurityStore independentStore() {
        return new JooqCredentialSecurityStore(
                DSL.using(
                        new TransactionAwareDataSourceProxy(authoritativeDataSource),
                        SQLDialect.POSTGRES
                ),
                new DataSourceTransactionManager(authoritativeDataSource)
        );
    }

    private int count(String table) {
        return dsl.fetchCount(DSL.table(DSL.name(table)));
    }

    private static CredentialBinding merchantBinding(
            String bindingIdentity,
            MerchantScope merchantScope,
            String externalContextReference
    ) {
        return new CredentialBinding(
                bindingIdentity,
                CredentialBindingScope.MERCHANT,
                Optional.of(merchantScope),
                "payment-provider-connection",
                externalContextReference,
                "PAYMENT_PROVIDER_ACCESS",
                T0
        );
    }

    private static CredentialGeneration generation(
            String generationIdentity,
            String bindingIdentity,
            String protectedMaterialReference,
            Instant registeredAt
    ) {
        return new CredentialGeneration(
                generationIdentity,
                bindingIdentity,
                protectedMaterialReference,
                registeredAt
        );
    }

    private static CredentialGenerationPolicy policy(
            String policyIdentity,
            String generationIdentity,
            long sequence,
            CredentialGenerationState state,
            Set<CredentialTechnicalUse> permittedUses,
            Instant effectiveAt
    ) {
        return new CredentialGenerationPolicy(
                policyIdentity,
                generationIdentity,
                sequence,
                state,
                permittedUses,
                effectiveAt,
                Optional.of("audit-" + policyIdentity)
        );
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing required PostgreSQL integration-test environment variable: " + name
            );
        }
        return value;
    }
}
