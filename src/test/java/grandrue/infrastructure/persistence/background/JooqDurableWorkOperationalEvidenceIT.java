package grandrue.infrastructure.persistence.background;

import grandrue.application.MerchantScope;
import grandrue.application.StandingFreeBackgroundWorkContract;
import grandrue.background.BackgroundExecutionScope;
import grandrue.background.BackgroundWorkContractAffinity;
import grandrue.background.BackgroundWorkResultClassification;
import grandrue.background.DurableWorkInstruction;
import grandrue.background.DurableWorkOperationalEvidenceSource;
import grandrue.background.DurableWorkProgressEvidence;
import grandrue.background.OverdueHandling;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqDurableWorkOperationalEvidenceIT {

    private static final MerchantScope MERCHANT =
            new MerchantScope("operational-evidence-merchant");
    private static final Instant T0 =
            Instant.parse("2026-09-19T18:00:00Z");

    private DataSource authoritativeDataSource;
    private DSLContext dsl;
    private DataSourceTransactionManager transactionManager;

    @BeforeEach
    void setUp() {
        authoritativeDataSource = new DriverManagerDataSource(
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_URL"),
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_USER"),
                requiredEnvironment("GRANDRUE_TEST_POSTGRES_PASSWORD")
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
        transactionManager =
                new DataSourceTransactionManager(authoritativeDataSource);

        dsl.execute(
                "truncate table durable_work_attempt, durable_work_instruction"
        );
        dsl.execute(
                "insert into merchant_account (merchant_identifier) "
                        + "values (?) on conflict do nothing",
                MERCHANT.merchantIdentifier()
        );
    }

    @Test
    void outstanding_evidence_is_contract_exact_oldest_first_and_non_mutating() {
        JooqDurableWorkStore store = store();

        store.schedule(work(
                "current-oldest",
                StandingFreeBackgroundWorkContract.AFFINITY,
                T0.minusSeconds(30),
                T0.minusSeconds(120)
        ));
        store.schedule(work(
                "current-newer",
                StandingFreeBackgroundWorkContract.AFFINITY,
                T0.minusSeconds(20),
                T0.minusSeconds(60)
        ));
        store.schedule(work(
                "other-release",
                new BackgroundWorkContractAffinity(
                        StandingFreeBackgroundWorkContract.IDENTITY,
                        "historical-release"
                ),
                T0.minusSeconds(40),
                T0.minusSeconds(180)
        ));
        store.schedule(legacyWork("legacy-no-affinity"));

        List<DurableWorkProgressEvidence> evidence =
                evidenceSource().outstanding(
                        StandingFreeBackgroundWorkContract.AFFINITY,
                        10
                );

        assertEquals(
                List.of("current-oldest", "current-newer"),
                evidence.stream()
                        .map(DurableWorkProgressEvidence::workIdentity)
                        .toList()
        );
        assertEquals(
                StandingFreeBackgroundWorkContract.AFFINITY,
                evidence.getFirst().contractAffinity()
        );
        assertEquals(
                Optional.of(MERCHANT),
                evidence.getFirst().merchantScope()
        );
        assertTrue(evidence.getFirst().claimExpiresAt().isEmpty());
        assertEquals(0, evidence.getFirst().attemptCount());
        assertTrue(evidence.getFirst().latestAttemptIdentity().isEmpty());
        assertTrue(evidence.getFirst().latestAttemptedAt().isEmpty());
        assertTrue(
                evidence.getFirst()
                        .latestResultClassification()
                        .isEmpty()
        );

        assertEquals(
                0,
                dsl.fetchCount(
                        DSL.table(DSL.name("durable_work_instruction")),
                        DSL.field(
                                DSL.name("claimed_by"),
                                String.class
                        ).isNotNull()
                )
        );
        assertEquals(
                0,
                dsl.fetchCount(
                        DSL.table(DSL.name("durable_work_instruction")),
                        DSL.field(
                                DSL.name("finalised_at"),
                                Instant.class
                        ).isNotNull()
                )
        );
    }

    @Test
    void attempt_classification_is_visible_without_resolving_the_work() {
        JooqDurableWorkStore store = store();
        store.schedule(work(
                "reconciliation-needed",
                StandingFreeBackgroundWorkContract.AFFINITY,
                T0.minusSeconds(10),
                T0.minusSeconds(120)
        ));

        store.claimDue(
                "worker-1",
                T0,
                T0.plusSeconds(60),
                1
        );
        store.startAttempt(
                "attempt-1",
                "reconciliation-needed",
                "worker-1",
                T0.plusSeconds(1),
                "scheduled-principal"
        );
        store.recordAttemptOutcome(
                "attempt-1",
                "worker-1",
                BackgroundWorkResultClassification.RECONCILIATION_REQUIRED,
                Optional.of("bounded-evidence-reference")
        );

        DurableWorkProgressEvidence evidence =
                evidenceSource().outstanding(
                        StandingFreeBackgroundWorkContract.AFFINITY,
                        1
                ).getFirst();

        assertEquals(1, evidence.attemptCount());
        assertEquals(
                Optional.of("attempt-1"),
                evidence.latestAttemptIdentity()
        );
        assertEquals(
                Optional.of(T0.plusSeconds(1)),
                evidence.latestAttemptedAt()
        );
        assertEquals(
                Optional.of(
                        BackgroundWorkResultClassification
                                .RECONCILIATION_REQUIRED
                ),
                evidence.latestResultClassification()
        );
        assertEquals(
                Optional.of(T0.plusSeconds(60)),
                evidence.claimExpiresAt()
        );

        assertTrue(
                store.instruction("reconciliation-needed").isPresent()
        );
        assertEquals(
                0,
                dsl.fetchCount(
                        DSL.table(DSL.name("durable_work_instruction")),
                        DSL.field(
                                DSL.name("finalised_at"),
                                Instant.class
                        ).isNotNull()
                )
        );
    }

    @Test
    void operational_evidence_read_is_bounded() {
        assertThrows(
                IllegalArgumentException.class,
                () -> evidenceSource().outstanding(
                        StandingFreeBackgroundWorkContract.AFFINITY,
                        0
                )
        );
    }

    private JooqDurableWorkStore store() {
        return new JooqDurableWorkStore(
                dsl,
                transactionManager
        );
    }

    private DurableWorkOperationalEvidenceSource evidenceSource() {
        return store();
    }

    private static DurableWorkInstruction work(
            String workIdentity,
            BackgroundWorkContractAffinity affinity,
            Instant dueAt,
            Instant createdAt
    ) {
        return new DurableWorkInstruction(
                workIdentity,
                StandingFreeBackgroundWorkContract
                        .IDENTITY
                        .ownerIdentifier(),
                BackgroundExecutionScope.MERCHANT,
                Optional.of(MERCHANT),
                dueAt,
                StandingFreeBackgroundWorkContract
                        .DEFINITION
                        .target()
                        .targetIdentifier(),
                "merchant-account-establishment",
                Optional.empty(),
                Optional.empty(),
                StandingFreeBackgroundWorkContract
                        .DEFINITION
                        .retryContractReference(),
                StandingFreeBackgroundWorkContract
                        .DEFINITION
                        .overdueHandling(),
                createdAt,
                Optional.of(affinity)
        );
    }

    private static DurableWorkInstruction legacyWork(
            String workIdentity
    ) {
        return new DurableWorkInstruction(
                workIdentity,
                StandingFreeBackgroundWorkContract
                        .IDENTITY
                        .ownerIdentifier(),
                BackgroundExecutionScope.MERCHANT,
                Optional.of(MERCHANT),
                T0.minusSeconds(50),
                StandingFreeBackgroundWorkContract
                        .DEFINITION
                        .target()
                        .targetIdentifier(),
                "legacy-correlation",
                Optional.empty(),
                Optional.empty(),
                StandingFreeBackgroundWorkContract
                        .DEFINITION
                        .retryContractReference(),
                OverdueHandling.RE_EVALUATE_CURRENT_STATE,
                T0.minusSeconds(240)
        );
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing required PostgreSQL integration-test "
                            + "environment variable: "
                            + name
            );
        }
        return value;
    }
}
