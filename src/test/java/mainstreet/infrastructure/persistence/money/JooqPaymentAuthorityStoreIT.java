package mainstreet.infrastructure.persistence.money;

import mainstreet.application.MerchantScope;
import mainstreet.money.CurrencyIdentity;
import mainstreet.money.MonetaryAmount;
import mainstreet.money.PaymentApplication;
import mainstreet.money.PaymentObligation;
import mainstreet.money.ProviderPaymentEvidence;
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
import java.math.BigInteger;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqPaymentAuthorityStoreIT {

    private static final MerchantScope MERCHANT_A = new MerchantScope("merchant-a");
    private static final MerchantScope MERCHANT_B = new MerchantScope("merchant-b");
    private static final CurrencyIdentity GBP = new CurrencyIdentity("GBP");
    private static final CurrencyIdentity EUR = new CurrencyIdentity("EUR");
    private static final Instant T0 = Instant.parse("2026-08-24T16:00:00Z");

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
        dsl.execute("truncate table payment_application, provider_payment_evidence, payment_obligation");
        dsl.execute("insert into merchant_account (merchant_identifier) values ('merchant-a'), ('merchant-b') on conflict do nothing");
    }

    @Test
    void obligation_and_provider_evidence_survive_restart_without_rewriting_each_other() {
        JooqPaymentAuthorityStore store = store();
        PaymentObligation obligation = obligation(MERCHANT_A, "obligation-1", GBP, 10_000);
        ProviderPaymentEvidence evidence = evidence(MERCHANT_A, "evidence-1", GBP, 4_000, "CAPTURED");

        store.registerObligation(obligation);
        store.recordProviderEvidence(evidence);

        JooqPaymentAuthorityStore restarted = store();
        assertEquals(obligation, restarted.obligation(MERCHANT_A, "obligation-1").orElseThrow());
        assertEquals(evidence, restarted.providerEvidence(MERCHANT_A, "evidence-1").orElseThrow());
        assertEquals(BigInteger.valueOf(10_000), restarted.currentAmountDue(MERCHANT_A, "obligation-1").minorUnitAmount());
    }

    @Test
    void partial_payment_reduces_derived_amount_due_without_mutating_obligation() {
        JooqPaymentAuthorityStore store = store();
        PaymentObligation obligation = obligation(MERCHANT_A, "obligation-1", GBP, 10_000);
        ProviderPaymentEvidence evidence = evidence(MERCHANT_A, "evidence-1", GBP, 4_000, "CAPTURED");
        store.registerObligation(obligation);
        store.recordProviderEvidence(evidence);
        store.applyPayment(application(MERCHANT_A, "application-1", "obligation-1", "evidence-1", GBP, 4_000));

        assertEquals(BigInteger.valueOf(6_000), store.currentAmountDue(MERCHANT_A, "obligation-1").minorUnitAmount());
        assertEquals(BigInteger.valueOf(10_000), store.obligation(MERCHANT_A, "obligation-1")
                .orElseThrow().obligationAmount().minorUnitAmount());
    }

    @Test
    void provider_overpayment_evidence_does_not_increase_the_obligation() {
        JooqPaymentAuthorityStore store = store();
        store.registerObligation(obligation(MERCHANT_A, "obligation-1", GBP, 10_000));
        store.recordProviderEvidence(evidence(MERCHANT_A, "evidence-1", GBP, 11_000, "CAPTURED"));

        assertThrows(IllegalStateException.class, () -> store.applyPayment(
                application(MERCHANT_A, "application-1", "obligation-1", "evidence-1", GBP, 11_000)
        ));
        assertEquals(BigInteger.valueOf(10_000), store.currentAmountDue(MERCHANT_A, "obligation-1").minorUnitAmount());
    }

    @Test
    void application_cannot_exceed_available_provider_evidence() {
        JooqPaymentAuthorityStore store = store();
        store.registerObligation(obligation(MERCHANT_A, "obligation-1", GBP, 10_000));
        store.recordProviderEvidence(evidence(MERCHANT_A, "evidence-1", GBP, 4_000, "CAPTURED"));

        assertThrows(IllegalStateException.class, () -> store.applyPayment(
                application(MERCHANT_A, "application-1", "obligation-1", "evidence-1", GBP, 5_000)
        ));
        assertTrue(store.applications(MERCHANT_A, "obligation-1").isEmpty());
    }

    @Test
    void payment_application_requires_exact_currency_match() {
        JooqPaymentAuthorityStore store = store();
        store.registerObligation(obligation(MERCHANT_A, "obligation-1", GBP, 10_000));
        store.recordProviderEvidence(evidence(MERCHANT_A, "evidence-1", EUR, 10_000, "CAPTURED"));

        assertThrows(IllegalArgumentException.class, () -> store.applyPayment(
                application(MERCHANT_A, "application-1", "obligation-1", "evidence-1", GBP, 10_000)
        ));
    }

    @Test
    void identities_are_idempotent_but_cannot_be_reused_for_different_intent() {
        JooqPaymentAuthorityStore store = store();
        PaymentObligation obligation = obligation(MERCHANT_A, "obligation-1", GBP, 10_000);
        ProviderPaymentEvidence evidence = evidence(MERCHANT_A, "evidence-1", GBP, 4_000, "CAPTURED");
        PaymentApplication application = application(MERCHANT_A, "application-1", "obligation-1", "evidence-1", GBP, 4_000);

        assertEquals(obligation, store.registerObligation(obligation));
        assertEquals(obligation, store.registerObligation(obligation));
        assertEquals(evidence, store.recordProviderEvidence(evidence));
        assertEquals(evidence, store.recordProviderEvidence(evidence));
        assertEquals(application, store.applyPayment(application));
        assertEquals(application, store.applyPayment(application));

        PaymentApplication conflicting = application(
                MERCHANT_A,
                "application-1",
                "obligation-1",
                "evidence-1",
                GBP,
                3_000
        );
        assertThrows(IllegalStateException.class, () -> store.applyPayment(conflicting));
        assertEquals(1, store.applications(MERCHANT_A, "obligation-1").size());
    }

    @Test
    void payment_authority_is_merchant_scope_isolated() {
        JooqPaymentAuthorityStore store = store();
        store.registerObligation(obligation(MERCHANT_A, "obligation-a", GBP, 10_000));
        store.recordProviderEvidence(evidence(MERCHANT_B, "evidence-b", GBP, 10_000, "CAPTURED"));

        assertThrows(IllegalStateException.class, () -> store.applyPayment(
                application(MERCHANT_A, "application-1", "obligation-a", "evidence-b", GBP, 10_000)
        ));
        assertTrue(store.providerEvidence(MERCHANT_A, "evidence-b").isEmpty());
    }

    @Test
    void concurrent_evidence_applications_cannot_over_discharge_one_obligation() throws Exception {
        store().registerObligation(obligation(MERCHANT_A, "obligation-1", GBP, 10_000));
        store().recordProviderEvidence(evidence(MERCHANT_A, "evidence-1", GBP, 7_000, "CAPTURED"));
        store().recordProviderEvidence(evidence(MERCHANT_A, "evidence-2", GBP, 7_000, "CAPTURED"));
        CountDownLatch start = new CountDownLatch(1);

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<Boolean> first = executor.submit(() -> attemptApplication(
                    start,
                    application(MERCHANT_A, "application-1", "obligation-1", "evidence-1", GBP, 7_000)
            ));
            Future<Boolean> second = executor.submit(() -> attemptApplication(
                    start,
                    application(MERCHANT_A, "application-2", "obligation-1", "evidence-2", GBP, 7_000)
            ));
            start.countDown();

            int successes = (first.get() ? 1 : 0) + (second.get() ? 1 : 0);
            assertEquals(1, successes);
        }
        assertEquals(BigInteger.valueOf(3_000), store().currentAmountDue(MERCHANT_A, "obligation-1").minorUnitAmount());
    }

    @Test
    void concurrent_reuse_of_one_evidence_cannot_over_apply_provider_amount() throws Exception {
        store().registerObligation(obligation(MERCHANT_A, "obligation-1", GBP, 7_000));
        store().registerObligation(obligation(MERCHANT_A, "obligation-2", GBP, 7_000));
        store().recordProviderEvidence(evidence(MERCHANT_A, "evidence-1", GBP, 10_000, "CAPTURED"));
        CountDownLatch start = new CountDownLatch(1);

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<Boolean> first = executor.submit(() -> attemptApplication(
                    start,
                    application(MERCHANT_A, "application-1", "obligation-1", "evidence-1", GBP, 7_000)
            ));
            Future<Boolean> second = executor.submit(() -> attemptApplication(
                    start,
                    application(MERCHANT_A, "application-2", "obligation-2", "evidence-1", GBP, 7_000)
            ));
            start.countDown();

            int successes = (first.get() ? 1 : 0) + (second.get() ? 1 : 0);
            assertEquals(1, successes);
        }
        assertEquals(1, dsl.fetchCount(DSL.table(DSL.name("payment_application"))));
    }

    private boolean attemptApplication(CountDownLatch start, PaymentApplication application)
            throws InterruptedException {
        start.await();
        try {
            independentStore().applyPayment(application);
            return true;
        } catch (IllegalStateException expectedConflict) {
            return false;
        }
    }

    private JooqPaymentAuthorityStore store() {
        return new JooqPaymentAuthorityStore(dsl, transactionManager);
    }

    private JooqPaymentAuthorityStore independentStore() {
        return new JooqPaymentAuthorityStore(
                DSL.using(
                        new TransactionAwareDataSourceProxy(authoritativeDataSource),
                        SQLDialect.POSTGRES
                ),
                new DataSourceTransactionManager(authoritativeDataSource)
        );
    }

    private static PaymentObligation obligation(
            MerchantScope merchant,
            String identity,
            CurrencyIdentity currency,
            long minorUnits
    ) {
        return new PaymentObligation(
                identity,
                merchant,
                "commercial-subject-1",
                amount(currency, minorUnits),
                "commercial-commitment-1",
                "DUE_NOW",
                "payment-policy-v1",
                T0
        );
    }

    private static ProviderPaymentEvidence evidence(
            MerchantScope merchant,
            String identity,
            CurrencyIdentity currency,
            long minorUnits,
            String providerResult
    ) {
        return new ProviderPaymentEvidence(
                identity,
                merchant,
                "provider-a",
                "provider-tx-" + identity,
                "payment-request-1",
                amount(currency, minorUnits),
                "CARD",
                providerResult,
                T0.plusSeconds(1),
                Optional.empty()
        );
    }

    private static PaymentApplication application(
            MerchantScope merchant,
            String identity,
            String obligationIdentity,
            String evidenceIdentity,
            CurrencyIdentity currency,
            long minorUnits
    ) {
        return new PaymentApplication(
                identity,
                merchant,
                obligationIdentity,
                evidenceIdentity,
                amount(currency, minorUnits),
                T0.plusSeconds(2),
                "provider-reconciliation-v1"
        );
    }

    private static MonetaryAmount amount(CurrencyIdentity currency, long minorUnits) {
        return new MonetaryAmount(currency, BigInteger.valueOf(minorUnits));
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
