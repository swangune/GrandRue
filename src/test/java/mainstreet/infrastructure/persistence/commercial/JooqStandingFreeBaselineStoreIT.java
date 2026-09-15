package mainstreet.infrastructure.persistence.commercial;

import mainstreet.application.MerchantScope;
import mainstreet.application.StandingFreeFromMerchantAccountEstablishedHandler;
import mainstreet.application.TrustedPlatformHumanPrincipal;
import mainstreet.commercial.CommercialEntitlementIdentity;
import mainstreet.commercial.StandardPlanLevel;
import mainstreet.commercial.StandardPlanRevision;
import mainstreet.commercial.StandingFreeBaseline;
import mainstreet.commercial.StandingFreeBaselineGrantAuthority;
import mainstreet.infrastructure.persistence.merchantaccount.JooqMerchantAccountBootstrapStore;
import mainstreet.infrastructure.persistence.merchantaccount.JooqMerchantAccountEstablishmentPublicationOutbox;
import mainstreet.merchantaccount.MerchantAccountEstablished;
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

import javax.sql.DataSource;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqStandingFreeBaselineStoreIT {

    private static final Instant T0 = Instant.parse("2026-08-24T10:00:00Z");
    private static final CommercialEntitlementIdentity ENQUIRY =
            new CommercialEntitlementIdentity("entitlement-enquiry");

    private DSLContext dsl;
    private DataSourceTransactionManager transactionManager;

    @BeforeEach
    void setUp() {
        DataSource authoritativeDataSource = new DriverManagerDataSource(
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
        dsl.execute("truncate table merchant_account cascade");
    }

    @Test
    void delayed_reaction_uses_authoritative_establishment_time_and_exact_free_revision() {
        MerchantAccountEstablished event = establishMerchant(
                "request-1",
                "establishment-1",
                "publication-1"
        );
        JooqStandingFreeBaselineStore store =
                new JooqStandingFreeBaselineStore(dsl, transactionManager);
        StandingFreeFromMerchantAccountEstablishedHandler handler = handler(
                store,
                new AtomicInteger()
        );

        StandingFreeBaseline baseline = handler.handle(event);

        assertEquals(T0, baseline.effectiveFrom());
        assertEquals("free-r7", baseline.freePlanRevisionIdentity());
        assertEquals(Set.of(ENQUIRY), baseline.entitlementSnapshot());
        assertEquals("establishment-1", baseline.originatingMerchantAccountEstablishmentIdentity());
        assertEquals(1, count("standing_free_baseline"));
        assertEquals(1, count("standing_free_baseline_entitlement"));
    }

    @Test
    void duplicate_delivery_returns_original_baseline_without_rebinding_identity() {
        MerchantAccountEstablished event = establishMerchant(
                "request-1",
                "establishment-1",
                "publication-1"
        );
        JooqStandingFreeBaselineStore store =
                new JooqStandingFreeBaselineStore(dsl, transactionManager);
        AtomicInteger sequence = new AtomicInteger();
        StandingFreeFromMerchantAccountEstablishedHandler handler =
                handler(store, sequence);

        StandingFreeBaseline first = handler.handle(event);
        StandingFreeBaseline replay = handler.handle(event);

        assertEquals(first, replay);
        assertEquals("standing-free-1", replay.baselineIdentity());
        assertEquals(1, sequence.get());
        assertEquals(1, count("standing_free_baseline"));
        assertEquals(1, count("standing_free_baseline_entitlement"));
    }

    @Test
    void committed_baseline_recovers_after_store_recreation_with_catalogue_unavailable() {
        var event = establishMerchant("request-1", "establishment-1", "publication-1");
        var original = handler(new JooqStandingFreeBaselineStore(dsl, transactionManager),
                new AtomicInteger()).handle(event);
        var recovering = new StandingFreeFromMerchantAccountEstablishedHandler(
                at -> { throw new AssertionError("Catalogue is unavailable during recovery"); },
                new JooqStandingFreeBaselineStore(dsl, transactionManager),
                e -> { throw new AssertionError("No replacement identity may be generated"); });
        assertEquals(original, recovering.handle(event));
        assertEquals(1, count("standing_free_baseline"));
        assertEquals(1, count("standing_free_baseline_entitlement"));
    }

    @Test
    void concurrent_duplicate_delivery_converges_to_one_baseline() throws Exception {
        MerchantAccountEstablished event = establishMerchant(
                "request-1",
                "establishment-1",
                "publication-1"
        );
        JooqStandingFreeBaselineStore store =
                new JooqStandingFreeBaselineStore(dsl, transactionManager);
        AtomicInteger sequence = new AtomicInteger();
        StandingFreeFromMerchantAccountEstablishedHandler handler =
                handler(store, sequence);
        CountDownLatch start = new CountDownLatch(1);

        try (var executor = Executors.newFixedThreadPool(2)) {
            Future<StandingFreeBaseline> first = executor.submit(() -> {
                start.await();
                return handler.handle(event);
            });
            Future<StandingFreeBaseline> second = executor.submit(() -> {
                start.await();
                return handler.handle(event);
            });
            start.countDown();

            assertEquals(first.get(), second.get());
        }

        assertEquals(1, count("standing_free_baseline"));
        assertEquals(1, count("standing_free_baseline_entitlement"));
    }

    @Test
    void cross_merchant_originating_establishment_is_rejected_by_postgresql() {
        MerchantAccountEstablished merchantA = establishMerchant(
                "request-a",
                "establishment-a",
                "publication-a"
        );
        MerchantAccountEstablished merchantB = establishMerchant(
                "request-b",
                "establishment-b",
                "publication-b"
        );
        JooqStandingFreeBaselineStore store =
                new JooqStandingFreeBaselineStore(dsl, transactionManager);

        StandingFreeBaseline invalid = new StandingFreeBaseline(
                "standing-free-invalid",
                merchantB.merchantScope(),
                merchantA.establishmentIdentity(),
                merchantA.occurredAt(),
                "free-r7",
                Set.of(ENQUIRY)
        );

        assertThrows(DataAccessException.class, () -> store.establishIfAbsent(invalid));
        assertEquals(0, count("standing_free_baseline"));
    }

    @Test
    void standing_free_grant_survives_store_recreation_and_is_effective_from_t0() {
        MerchantAccountEstablished event = establishMerchant(
                "request-1",
                "establishment-1",
                "publication-1"
        );
        JooqStandingFreeBaselineStore firstStore =
                new JooqStandingFreeBaselineStore(dsl, transactionManager);
        handler(firstStore, new AtomicInteger()).handle(event);

        JooqStandingFreeBaselineStore recreatedStore =
                new JooqStandingFreeBaselineStore(dsl, transactionManager);
        StandingFreeBaselineGrantAuthority authority =
                new StandingFreeBaselineGrantAuthority(recreatedStore);

        assertTrue(authority.effectiveGrants(
                event.merchantScope(),
                ENQUIRY,
                T0
        ).size() == 1);
        assertTrue(authority.effectiveGrants(
                event.merchantScope(),
                ENQUIRY,
                T0.minusSeconds(1)
        ).isEmpty());
        assertTrue(authority.effectiveGrants(
                new MerchantScope("merchant-other"),
                ENQUIRY,
                T0.plusSeconds(1)
        ).isEmpty());
        assertFalse(recreatedStore.baselineFor(event.merchantScope()).isEmpty());
    }

    private MerchantAccountEstablished establishMerchant(
            String requestIdentity,
            String establishmentIdentity,
            String publicationIdentity) {
        JooqMerchantAccountBootstrapStore bootstrapStore =
                new JooqMerchantAccountBootstrapStore(
                        dsl,
                        transactionManager,
                        Clock.fixed(T0, ZoneOffset.UTC),
                        () -> establishmentIdentity,
                        () -> publicationIdentity
                );
        bootstrapStore.establishIfAbsent(
                requestIdentity,
                new TrustedPlatformHumanPrincipal("identity-" + requestIdentity)
        );

        return new JooqMerchantAccountEstablishmentPublicationOutbox(dsl)
                .intentForEstablishment(establishmentIdentity)
                .orElseThrow()
                .event();
    }

    private StandingFreeFromMerchantAccountEstablishedHandler handler(
            JooqStandingFreeBaselineStore store,
            AtomicInteger sequence) {
        return new StandingFreeFromMerchantAccountEstablishedHandler(
                instant -> new StandardPlanRevision(
                        StandardPlanLevel.FREE,
                        "free-r7",
                        Set.of(ENQUIRY)
                ),
                store,
                event -> "standing-free-" + sequence.incrementAndGet()
        );
    }

    private int count(String tableName) {
        return dsl.fetchCount(DSL.table(DSL.name(tableName)));
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
