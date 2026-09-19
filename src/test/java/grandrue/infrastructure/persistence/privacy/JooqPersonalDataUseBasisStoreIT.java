package grandrue.infrastructure.persistence.privacy;

import grandrue.application.MerchantScope;
import grandrue.privacy.DataSubjectReference;
import grandrue.privacy.DataUsePurpose;
import grandrue.privacy.PersonalDataUseBasis;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqPersonalDataUseBasisStoreIT {

    private static final Instant T0 = Instant.parse("2026-08-24T10:00:00Z");
    private static final MerchantScope MERCHANT_A = new MerchantScope("merchant-a");

    private DSLContext dsl;
    private DataSourceTransactionManager transactionManager;

    @BeforeEach
    void setUp() {
        DataSource authoritativeDataSource = new DriverManagerDataSource(
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
        transactionManager = new DataSourceTransactionManager(authoritativeDataSource);

        dsl.execute("truncate table merchant_account cascade");
        dsl.execute("insert into merchant_account (merchant_identifier) values ('merchant-a'), ('merchant-b')");
    }

    @Test
    void basis_survives_store_recreation_and_is_exactly_purpose_and_audience_bound() {
        JooqPersonalDataUseBasisStore first = store();
        PersonalDataUseBasis basis = basis("basis-1", MERCHANT_A, "subject-a");
        first.establish(basis);

        JooqPersonalDataUseBasisStore recreated = store();
        assertEquals(basis, recreated.basis("basis-1").orElseThrow());
        assertTrue(recreated.hasEffectiveBasis(
                basis.subject(),
                "media-1",
                DataUsePurpose.PUBLIC_PROMOTIONAL_EXPOSURE,
                "PUBLIC",
                T0
        ));
        assertFalse(recreated.hasEffectiveBasis(
                basis.subject(),
                "media-1",
                DataUsePurpose.OPERATIONAL_NEED,
                "PUBLIC",
                T0
        ));
        assertFalse(recreated.hasEffectiveBasis(
                basis.subject(),
                "media-1",
                DataUsePurpose.PUBLIC_PROMOTIONAL_EXPOSURE,
                "MERCHANT_PRIVATE",
                T0
        ));
    }

    @Test
    void cessation_removes_fresh_use_at_exact_half_open_boundary_without_deleting_evidence() {
        JooqPersonalDataUseBasisStore store = store();
        PersonalDataUseBasis basis = store.establish(
                basis("basis-1", MERCHANT_A, "subject-a")
        );
        Instant end = T0.plusSeconds(60);

        PersonalDataUseBasis ceased = store.cease("basis-1", end);

        assertTrue(store.hasEffectiveBasis(
                basis.subject(),
                "media-1",
                DataUsePurpose.PUBLIC_PROMOTIONAL_EXPOSURE,
                "PUBLIC",
                end.minusNanos(1_000)
        ));
        assertFalse(store.hasEffectiveBasis(
                basis.subject(),
                "media-1",
                DataUsePurpose.PUBLIC_PROMOTIONAL_EXPOSURE,
                "PUBLIC",
                end
        ));
        assertEquals(end, ceased.effectiveUntilExclusive().orElseThrow());
        assertEquals(1, dsl.fetchCount(DSL.table(DSL.name("personal_data_use_basis"))));
    }

    @Test
    void same_basis_identity_replays_only_for_same_evidence() {
        JooqPersonalDataUseBasisStore store = store();
        PersonalDataUseBasis original = basis("basis-1", MERCHANT_A, "subject-a");

        assertEquals(original, store.establish(original));
        assertEquals(original, store.establish(original));

        PersonalDataUseBasis conflicting = basis("basis-1", MERCHANT_A, "subject-b");
        assertThrows(IllegalStateException.class, () -> store.establish(conflicting));
        assertEquals(1, dsl.fetchCount(DSL.table(DSL.name("personal_data_use_basis"))));
    }

    @Test
    void merchant_scope_is_part_of_subject_use_authority() {
        JooqPersonalDataUseBasisStore store = store();
        PersonalDataUseBasis basis = store.establish(
                basis("basis-1", MERCHANT_A, "subject-a")
        );

        assertFalse(store.hasEffectiveBasis(
                new DataSubjectReference(
                        new MerchantScope("merchant-b"),
                        basis.subject().subjectReference()
                ),
                "media-1",
                DataUsePurpose.PUBLIC_PROMOTIONAL_EXPOSURE,
                "PUBLIC",
                T0
        ));
    }

    @Test
    void committed_cessation_cannot_be_silently_rewritten() {
        JooqPersonalDataUseBasisStore store = store();
        store.establish(basis("basis-1", MERCHANT_A, "subject-a"));
        Instant firstEnd = T0.plusSeconds(60);
        store.cease("basis-1", firstEnd);

        assertEquals(firstEnd, store.cease("basis-1", firstEnd)
                .effectiveUntilExclusive().orElseThrow());
        assertThrows(
                IllegalStateException.class,
                () -> store.cease("basis-1", firstEnd.plusSeconds(1))
        );
    }

    private JooqPersonalDataUseBasisStore store() {
        return new JooqPersonalDataUseBasisStore(dsl, transactionManager);
    }

    private static PersonalDataUseBasis basis(
            String identity,
            MerchantScope merchantScope,
            String subjectReference) {
        return new PersonalDataUseBasis(
                identity,
                new DataSubjectReference(merchantScope, subjectReference),
                "media-1",
                DataUsePurpose.PUBLIC_PROMOTIONAL_EXPOSURE,
                "PUBLIC",
                "governed-authority",
                "evidence-1",
                T0,
                Optional.empty()
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
