package grandrue.infrastructure.persistence.commercial;

import grandrue.application.MerchantScope;
import grandrue.commercial.BillingCadence;
import grandrue.commercial.CommercialAcceptanceProvenance;
import grandrue.commercial.CommercialEntitlementIdentity;
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
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JooqMerchantCommercialAgreementReplayIT {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final Instant T0 = Instant.parse("2026-08-24T05:15:00Z");
    private static final Instant T1 = Instant.parse("2026-09-01T00:00:00Z");

    private DSLContext dsl;
    private JooqMerchantCommercialAgreementStore store;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new DriverManagerDataSource(
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_URL"),
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_USER"),
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_PASSWORD")
        );
        Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load()
                .migrate();
        dsl = DSL.using(
                new TransactionAwareDataSourceProxy(dataSource),
                SQLDialect.POSTGRES
        );
        store = new JooqMerchantCommercialAgreementStore(
                dsl,
                new DataSourceTransactionManager(dataSource)
        );
        clearCommercialAgreementTables();
        dsl.insertInto(
                        DSL.table(DSL.name("merchant_account")),
                        DSL.field(DSL.name("merchant_identifier"))
                )
                .values(MERCHANT.merchantIdentifier())
                .onConflictDoNothing()
                .execute();
    }

    @AfterEach
    void tearDown() {
        clearCommercialAgreementTables();
    }

    @Test
    void replay_of_old_request_returns_historical_agreement_without_reinstating_it() {
        MerchantCommercialAgreement first = agreement(
                "agreement-a1",
                StandardPlanLevel.BUSINESS,
                "business-v7",
                T0,
                "acceptance-a1"
        );
        MerchantCommercialAgreementTransition firstTransition =
                new MerchantCommercialAgreementTransition(
                        "request-a1",
                        Optional.empty(),
                        first
                );
        store.apply(firstTransition);

        MerchantCommercialAgreement successor = agreement(
                "agreement-a2",
                StandardPlanLevel.GROWTH,
                "growth-v4",
                T1,
                "acceptance-a2"
        );
        store.apply(new MerchantCommercialAgreementTransition(
                "request-a2",
                Optional.of("agreement-a1"),
                successor
        ));

        MerchantCommercialAgreement replayed = store.apply(firstTransition);

        assertEquals("agreement-a1", replayed.commercialAgreementIdentity());
        assertEquals(Optional.of(T1), replayed.effectiveUntilExclusive());
        assertEquals(
                "agreement-a2",
                store.effectiveAgreement(MERCHANT, T1)
                        .orElseThrow()
                        .commercialAgreementIdentity()
        );
    }

    private static MerchantCommercialAgreement agreement(
            String agreementIdentity,
            StandardPlanLevel level,
            String revisionIdentity,
            Instant effectiveFrom,
            String acceptanceIdentity
    ) {
        return new MerchantCommercialAgreement(
                agreementIdentity,
                MERCHANT,
                new StandardPlanRevision(
                        level,
                        revisionIdentity,
                        Set.of(new CommercialEntitlementIdentity("booking.new-activity"))
                ),
                BillingCadence.MONTHLY,
                effectiveFrom,
                Optional.empty(),
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
