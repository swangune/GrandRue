package grandrue.infrastructure.persistence.commercial;

import grandrue.application.MerchantScope;
import grandrue.commercial.BillingCadence;
import grandrue.commercial.CommercialAcceptanceProvenance;
import grandrue.commercial.CommercialEntitlementGrant;
import grandrue.commercial.CommercialEntitlementIdentity;
import grandrue.commercial.CommercialTransitionConflictException;
import grandrue.commercial.MerchantCommercialAgreement;
import grandrue.commercial.MerchantCommercialAgreementTransition;
import grandrue.commercial.StandardPlanLevel;
import grandrue.commercial.StandardPlanRevision;
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

import javax.sql.DataSource;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqMerchantCommercialAgreementStoreIT {

    private static final MerchantScope MERCHANT_A = new MerchantScope("merchant-a");
    private static final MerchantScope MERCHANT_B = new MerchantScope("merchant-b");
    private static final Instant T0 = Instant.parse("2026-08-24T05:15:00Z");
    private static final Instant T1 = Instant.parse("2026-09-01T00:00:00Z");
    private static final Instant T2 = Instant.parse("2026-10-01T00:00:00Z");

    private DataSource dataSource;
    private DSLContext dsl;
    private JooqMerchantCommercialAgreementStore store;

    @BeforeEach
    void setUp() {
        dataSource = new DriverManagerDataSource(
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_URL"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_USER"),
                requiredEnvironment("MAINSTREET_TEST_POSTGRES_PASSWORD")
        );
        Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load()
                .migrate();
        TransactionAwareDataSourceProxy transactionAware =
                new TransactionAwareDataSourceProxy(dataSource);
        dsl = DSL.using(transactionAware, SQLDialect.POSTGRES);
        store = new JooqMerchantCommercialAgreementStore(
                dsl,
                new DataSourceTransactionManager(dataSource)
        );
        clearCommercialAgreementTables();
        dsl.insertInto(
                        DSL.table(DSL.name("merchant_account")),
                        DSL.field(DSL.name("merchant_identifier"))
                )
                .values(MERCHANT_A.merchantIdentifier())
                .values(MERCHANT_B.merchantIdentifier())
                .onConflictDoNothing()
                .execute();
    }

    @AfterEach
    void tearDown() {
        clearCommercialAgreementTables();
    }

    @Test
    void first_transition_persists_exact_plan_revision_entitlements_and_provenance() {
        MerchantCommercialAgreement candidate = agreement(
                "agreement-a1",
                MERCHANT_A,
                StandardPlanLevel.BUSINESS,
                "business-v7",
                BillingCadence.MONTHLY,
                T0,
                Optional.empty(),
                "acceptance-a1",
                Set.of("booking.new-activity", "analytics.advanced")
        );

        MerchantCommercialAgreement committed = store.apply(new MerchantCommercialAgreementTransition(
                "request-a1",
                Optional.empty(),
                candidate
        ));

        assertEquals(candidate, committed);
        assertEquals(Optional.of(candidate), store.effectiveAgreement(MERCHANT_A, T0));
        assertEquals(
                Set.of(
                        new CommercialEntitlementIdentity("booking.new-activity"),
                        new CommercialEntitlementIdentity("analytics.advanced")
                ),
                store.effectiveAgreement(MERCHANT_A, T0)
                        .orElseThrow()
                        .planRevision()
                        .entitlements()
        );

        List<CommercialEntitlementGrant> grants = store.effectiveGrants(
                MERCHANT_A,
                new CommercialEntitlementIdentity("booking.new-activity"),
                T0
        );
        assertEquals(1, grants.size());
        assertEquals(candidate.grantProvenance(), grants.getFirst().provenance());

        JooqMerchantCommercialAgreementStore restarted = new JooqMerchantCommercialAgreementStore(
                DSL.using(new TransactionAwareDataSourceProxy(dataSource), SQLDialect.POSTGRES),
                new DataSourceTransactionManager(dataSource)
        );
        assertEquals(Optional.of(candidate), restarted.effectiveAgreement(MERCHANT_A, T0));
    }

    @Test
    void replacement_closes_predecessor_at_exact_half_open_boundary() {
        MerchantCommercialAgreement first = agreement(
                "agreement-a1", MERCHANT_A, StandardPlanLevel.BUSINESS,
                "business-v7", BillingCadence.MONTHLY, T0, Optional.empty(),
                "acceptance-a1", Set.of("booking.new-activity")
        );
        store.apply(new MerchantCommercialAgreementTransition(
                "request-a1", Optional.empty(), first
        ));

        MerchantCommercialAgreement replacement = agreement(
                "agreement-a2", MERCHANT_A, StandardPlanLevel.GROWTH,
                "growth-v4", BillingCadence.MONTHLY, T1, Optional.empty(),
                "acceptance-a2", Set.of("booking.new-activity", "analytics.advanced")
        );
        store.apply(new MerchantCommercialAgreementTransition(
                "request-a2", Optional.of("agreement-a1"), replacement
        ));

        MerchantCommercialAgreement historical = store.effectiveAgreement(
                MERCHANT_A,
                T1.minusNanos(1_000)
        ).orElseThrow();
        assertEquals("agreement-a1", historical.commercialAgreementIdentity());
        assertEquals(Optional.of(T1), historical.effectiveUntilExclusive());
        assertEquals("agreement-a2", store.effectiveAgreement(MERCHANT_A, T1)
                .orElseThrow()
                .commercialAgreementIdentity());
    }

    @Test
    void stale_expected_head_is_rejected_without_overwriting_committed_state() {
        MerchantCommercialAgreement first = agreement(
                "agreement-a1", MERCHANT_A, StandardPlanLevel.BUSINESS,
                "business-v7", BillingCadence.MONTHLY, T0, Optional.empty(),
                "acceptance-a1", Set.of("booking.new-activity")
        );
        store.apply(new MerchantCommercialAgreementTransition(
                "request-a1", Optional.empty(), first
        ));

        MerchantCommercialAgreement stale = agreement(
                "agreement-a2", MERCHANT_A, StandardPlanLevel.GROWTH,
                "growth-v4", BillingCadence.MONTHLY, T1, Optional.empty(),
                "acceptance-a2", Set.of("booking.new-activity", "analytics.advanced")
        );

        assertThrows(
                CommercialTransitionConflictException.class,
                () -> store.apply(new MerchantCommercialAgreementTransition(
                        "request-a2",
                        Optional.empty(),
                        stale
                ))
        );
        assertEquals("agreement-a1", store.effectiveAgreement(MERCHANT_A, T1)
                .orElseThrow()
                .commercialAgreementIdentity());
    }

    @Test
    void successful_logical_request_replays_original_transition_without_duplication() {
        MerchantCommercialAgreement candidate = agreement(
                "agreement-a1", MERCHANT_A, StandardPlanLevel.BUSINESS,
                "business-v7", BillingCadence.ANNUAL, T0, Optional.empty(),
                "acceptance-a1", Set.of("booking.new-activity")
        );
        MerchantCommercialAgreementTransition transition =
                new MerchantCommercialAgreementTransition(
                        "request-a1", Optional.empty(), candidate
                );

        assertEquals(candidate, store.apply(transition));
        assertEquals(candidate, store.apply(transition));
        assertEquals(Optional.of(candidate), store.committedAgreement("request-a1"));
        assertEquals(1, dsl.fetchCount(DSL.table(DSL.name("commercial_agreement"))));
        assertEquals(1, dsl.fetchCount(DSL.table(DSL.name("commercial_agreement_transition_request"))));
    }

    @Test
    void logical_request_identity_cannot_be_reused_for_different_commercial_intent() {
        MerchantCommercialAgreement first = agreement(
                "agreement-a1", MERCHANT_A, StandardPlanLevel.BUSINESS,
                "business-v7", BillingCadence.MONTHLY, T0, Optional.empty(),
                "acceptance-a1", Set.of("booking.new-activity")
        );
        store.apply(new MerchantCommercialAgreementTransition(
                "request-a1", Optional.empty(), first
        ));

        MerchantCommercialAgreement different = agreement(
                "agreement-a2", MERCHANT_A, StandardPlanLevel.GROWTH,
                "growth-v4", BillingCadence.MONTHLY, T1, Optional.empty(),
                "acceptance-a2", Set.of("analytics.advanced")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> store.apply(new MerchantCommercialAgreementTransition(
                        "request-a1",
                        Optional.of("agreement-a1"),
                        different
                ))
        );
    }

    @Test
    void competing_transitions_based_on_same_head_allow_only_one_successor() throws Exception {
        MerchantCommercialAgreement first = agreement(
                "agreement-a1", MERCHANT_A, StandardPlanLevel.BUSINESS,
                "business-v7", BillingCadence.MONTHLY, T0, Optional.empty(),
                "acceptance-a1", Set.of("booking.new-activity")
        );
        store.apply(new MerchantCommercialAgreementTransition(
                "request-a1", Optional.empty(), first
        ));

        MerchantCommercialAgreement second = agreement(
                "agreement-a2", MERCHANT_A, StandardPlanLevel.GROWTH,
                "growth-v4", BillingCadence.MONTHLY, T1, Optional.empty(),
                "acceptance-a2", Set.of("booking.new-activity", "analytics.advanced")
        );
        MerchantCommercialAgreement third = agreement(
                "agreement-a3", MERCHANT_A, StandardPlanLevel.GROWTH,
                "growth-v5", BillingCadence.MONTHLY, T1, Optional.empty(),
                "acceptance-a3", Set.of("booking.new-activity", "custom-domain")
        );

        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            List<Callable<Boolean>> calls = List.of(
                    () -> succeeds(new MerchantCommercialAgreementTransition(
                            "request-a2", Optional.of("agreement-a1"), second
                    )),
                    () -> succeeds(new MerchantCommercialAgreementTransition(
                            "request-a3", Optional.of("agreement-a1"), third
                    ))
            );
            List<Future<Boolean>> results = executor.invokeAll(calls);
            long successes = 0;
            for (Future<Boolean> result : results) {
                if (result.get()) {
                    successes++;
                }
            }
            assertEquals(1, successes);
        } finally {
            executor.shutdownNow();
        }

        MerchantCommercialAgreement effective = store.effectiveAgreement(MERCHANT_A, T1)
                .orElseThrow();
        assertTrue(Set.of("agreement-a2", "agreement-a3")
                .contains(effective.commercialAgreementIdentity()));
        assertFalse(effective.commercialAgreementIdentity().equals("agreement-a1"));
    }

    @Test
    void agreement_and_grant_queries_are_merchant_scoped() {
        MerchantCommercialAgreement agreementA = agreement(
                "agreement-a1", MERCHANT_A, StandardPlanLevel.BUSINESS,
                "business-v7", BillingCadence.MONTHLY, T0, Optional.empty(),
                "acceptance-a1", Set.of("booking.new-activity")
        );
        MerchantCommercialAgreement agreementB = agreement(
                "agreement-b1", MERCHANT_B, StandardPlanLevel.GROWTH,
                "growth-v4", BillingCadence.MONTHLY, T0, Optional.empty(),
                "acceptance-b1", Set.of("analytics.advanced")
        );
        store.apply(new MerchantCommercialAgreementTransition(
                "request-a1", Optional.empty(), agreementA
        ));
        store.apply(new MerchantCommercialAgreementTransition(
                "request-b1", Optional.empty(), agreementB
        ));

        assertEquals("agreement-a1", store.effectiveAgreement(MERCHANT_A, T0)
                .orElseThrow().commercialAgreementIdentity());
        assertEquals("agreement-b1", store.effectiveAgreement(MERCHANT_B, T0)
                .orElseThrow().commercialAgreementIdentity());
        assertTrue(store.effectiveGrants(
                MERCHANT_A,
                new CommercialEntitlementIdentity("analytics.advanced"),
                T0
        ).isEmpty());
        assertTrue(store.effectiveGrants(
                MERCHANT_B,
                new CommercialEntitlementIdentity("booking.new-activity"),
                T0
        ).isEmpty());
    }

    private boolean succeeds(MerchantCommercialAgreementTransition transition)
            throws ExecutionException {
        try {
            store.apply(transition);
            return true;
        } catch (CommercialTransitionConflictException expected) {
            return false;
        } catch (RuntimeException other) {
            throw new ExecutionException(other);
        }
    }

    private static MerchantCommercialAgreement agreement(
            String agreementIdentity,
            MerchantScope merchantScope,
            StandardPlanLevel level,
            String planRevisionIdentity,
            BillingCadence cadence,
            Instant effectiveFrom,
            Optional<Instant> effectiveUntilExclusive,
            String acceptanceIdentity,
            Set<String> entitlements
    ) {
        return new MerchantCommercialAgreement(
                agreementIdentity,
                merchantScope,
                new StandardPlanRevision(
                        level,
                        planRevisionIdentity,
                        entitlements.stream()
                                .map(CommercialEntitlementIdentity::new)
                                .collect(java.util.stream.Collectors.toUnmodifiableSet())
                ),
                cadence,
                effectiveFrom,
                effectiveUntilExclusive,
                new CommercialAcceptanceProvenance(acceptanceIdentity)
        );
    }

    private void clearCommercialAgreementTables() {
        dsl.deleteFrom(DSL.table(DSL.name("commercial_agreement_transition_request"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("commercial_agreement_transition_head"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("commercial_agreement_entitlement"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("commercial_agreement"))).execute();
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
