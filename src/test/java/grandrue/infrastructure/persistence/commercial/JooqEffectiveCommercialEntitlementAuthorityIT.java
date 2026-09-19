package grandrue.infrastructure.persistence.commercial;

import grandrue.application.MerchantScope;
import grandrue.commercial.BillingCadence;
import grandrue.commercial.CommercialAccessDecision;
import grandrue.commercial.CommercialAcceptanceProvenance;
import grandrue.commercial.CommercialEntitlementGrantProvenance;
import grandrue.commercial.CommercialEntitlementIdentity;
import grandrue.commercial.CompositeCommercialEntitlementGrantAuthority;
import grandrue.commercial.EffectiveCommercialEntitlementAuthority;
import grandrue.commercial.InitialFullExperienceTrial;
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
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqEffectiveCommercialEntitlementAuthorityIT {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final MerchantScope OTHER_MERCHANT = new MerchantScope("merchant-b");
    private static final CommercialEntitlementIdentity BOOKING =
            new CommercialEntitlementIdentity("booking.new-activity");
    private static final CommercialEntitlementIdentity ANALYTICS =
            new CommercialEntitlementIdentity("analytics.advanced");
    private static final Instant T0 = Instant.parse("2026-08-24T05:15:00Z");

    private DataSource dataSource;
    private DSLContext dsl;
    private DataSourceTransactionManager transactionManager;
    private JooqMerchantCommercialAgreementStore agreementStore;
    private JooqInitialFullExperienceTrialStore trialStore;
    private JooqInitialFullExperienceTrialGrantAuthority trialGrantAuthority;

    @BeforeEach
    void setUp() {
        dataSource = new DriverManagerDataSource(
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_URL"),
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_USER"),
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_PASSWORD")
        );
        Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load()
                .migrate();
        TransactionAwareDataSourceProxy transactionAware =
                new TransactionAwareDataSourceProxy(dataSource);
        dsl = DSL.using(transactionAware, SQLDialect.POSTGRES);
        transactionManager = new DataSourceTransactionManager(dataSource);
        agreementStore = new JooqMerchantCommercialAgreementStore(
                dsl,
                transactionManager
        );
        trialStore = new JooqInitialFullExperienceTrialStore(
                dsl,
                transactionManager
        );
        trialGrantAuthority = new JooqInitialFullExperienceTrialGrantAuthority(
                dsl,
                Set.of(BOOKING, ANALYTICS)
        );

        clearCommercialTables();
        dsl.insertInto(
                        DSL.table(DSL.name("merchant_account")),
                        DSL.field(DSL.name("merchant_identifier"))
                )
                .values(MERCHANT.merchantIdentifier())
                .values(OTHER_MERCHANT.merchantIdentifier())
                .onConflictDoNothing()
                .execute();
    }

    @AfterEach
    void tearDown() {
        clearCommercialTables();
    }

    @Test
    void overlapping_trial_and_paid_agreement_preserve_both_authoritative_sources() {
        establishTrial();
        establishPaidAgreement(T0.plus(Duration.ofDays(1)), T0.plus(Duration.ofDays(40)));

        CommercialAccessDecision decision = authorityAt(T0.plus(Duration.ofDays(2)))
                .decisionFor(MERCHANT, BOOKING);

        assertTrue(decision.permitted());
        assertEquals(
                Set.of(
                        new CommercialEntitlementGrantProvenance(
                                "initial-full-experience-trial",
                                "trial-a"
                        ),
                        new CommercialEntitlementGrantProvenance(
                                "merchant-commercial-agreement",
                                "agreement-a1"
                        )
                ),
                Set.copyOf(decision.effectiveGrantSources())
        );
    }

    @Test
    void trial_expiry_does_not_remove_independently_effective_paid_agreement_grant() {
        establishTrial();
        establishPaidAgreement(T0.plus(Duration.ofDays(1)), T0.plus(Duration.ofDays(40)));

        CommercialAccessDecision decision = authorityAt(T0.plus(Duration.ofDays(31)))
                .decisionFor(MERCHANT, BOOKING);

        assertTrue(decision.permitted());
        assertEquals(
                List.of(new CommercialEntitlementGrantProvenance(
                        "merchant-commercial-agreement",
                        "agreement-a1"
                )),
                decision.effectiveGrantSources()
        );
    }

    @Test
    void trial_can_grant_entitlement_not_packaged_by_paid_agreement() {
        establishTrial();
        establishPaidAgreement(T0.plus(Duration.ofDays(1)), T0.plus(Duration.ofDays(40)));

        CommercialAccessDecision decision = authorityAt(T0.plus(Duration.ofDays(2)))
                .decisionFor(MERCHANT, ANALYTICS);

        assertTrue(decision.permitted());
        assertEquals(
                List.of(new CommercialEntitlementGrantProvenance(
                        "initial-full-experience-trial",
                        "trial-a"
                )),
                decision.effectiveGrantSources()
        );
    }

    @Test
    void expired_independent_sources_produce_commercial_denial_without_cross_merchant_leakage() {
        establishTrial();
        establishPaidAgreement(T0.plus(Duration.ofDays(1)), T0.plus(Duration.ofDays(40)));

        EffectiveCommercialEntitlementAuthority authority =
                authorityAt(T0.plus(Duration.ofDays(41)));

        assertFalse(authority.isEntitled(MERCHANT, BOOKING));
        assertFalse(authority.isEntitled(OTHER_MERCHANT, BOOKING));
    }

    private EffectiveCommercialEntitlementAuthority authorityAt(Instant instant) {
        return new EffectiveCommercialEntitlementAuthority(
                new CompositeCommercialEntitlementGrantAuthority(List.of(
                        trialGrantAuthority,
                        agreementStore
                )),
                Clock.fixed(instant, ZoneOffset.UTC)
        );
    }

    private void establishTrial() {
        trialStore.establishIfAbsent(new InitialFullExperienceTrial(
                "trial-a",
                MERCHANT,
                "configuration-a1",
                "activation-a1",
                T0
        ));
    }

    private void establishPaidAgreement(Instant start, Instant end) {
        MerchantCommercialAgreement agreement = new MerchantCommercialAgreement(
                "agreement-a1",
                MERCHANT,
                new StandardPlanRevision(
                        StandardPlanLevel.BUSINESS,
                        "business-v7",
                        Set.of(BOOKING)
                ),
                BillingCadence.MONTHLY,
                start,
                Optional.of(end),
                new CommercialAcceptanceProvenance("acceptance-a1")
        );
        agreementStore.apply(new MerchantCommercialAgreementTransition(
                "commercial-request-a1",
                Optional.empty(),
                agreement
        ));
    }

    private void clearCommercialTables() {
        dsl.deleteFrom(DSL.table(DSL.name("commercial_agreement_transition_request"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("commercial_agreement_transition_head"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("commercial_agreement_entitlement"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("commercial_agreement"))).execute();
        dsl.deleteFrom(DSL.table(DSL.name("initial_full_experience_trial"))).execute();
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
