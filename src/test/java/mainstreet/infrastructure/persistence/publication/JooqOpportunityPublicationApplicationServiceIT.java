package mainstreet.infrastructure.persistence.publication;

import mainstreet.application.ApplicationRequestIdentity;
import mainstreet.application.MerchantScope;
import mainstreet.publication.OpportunityPublicationMaterialRevision;
import mainstreet.publication.OpportunityPublicationState;
import mainstreet.publication.PublicationApplicationRequestConflictException;
import mainstreet.publication.PublicationLifecycle;
import mainstreet.publication.PublicationRevisionConflictException;
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
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JooqOpportunityPublicationApplicationServiceIT {

    private static final String ATOMICITY_PROBE_CONSTRAINT =
            "ck_opportunity_publication_application_atomicity_probe";
    private static final String ATOMICITY_PROBE_REQUEST = "request-atomicity-probe";

    private DSLContext dsl;
    private PlatformTransactionManager transactionManager;

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
                "truncate table opportunity_publication_application_request, "
                        + "opportunity_publication_lifecycle_history, "
                        + "opportunity_publication_revision_field_value, "
                        + "opportunity_publication_revision_material, "
                        + "opportunity_publication_current, "
                        + "opportunity_publication_revision_identity"
        );
    }

    @Test
    void same_logical_draft_establishment_replays_one_committed_result_without_duplicate_effect() {
        JooqOpportunityPublicationApplicationService service = service();
        OpportunityPublicationMaterialRevision material = material("R1", "Scholarship");
        ApplicationRequestIdentity request = request("request-create-1");

        OpportunityPublicationState first = service.establishDraft(request, material);
        OpportunityPublicationState replay = service.establishDraft(request, material);

        assertEquals(first, replay);
        assertEquals(PublicationLifecycle.DRAFT, replay.lifecycle());
        assertEquals(1, count("opportunity_publication_current"));
        assertEquals(1, count("opportunity_publication_revision_material"));
        assertEquals(0, count("opportunity_publication_lifecycle_history"));
        assertEquals(1, count("opportunity_publication_application_request"));
    }

    @Test
    void concurrent_same_logical_deliveries_reconcile_to_one_committed_result()
            throws Exception {
        OpportunityPublicationMaterialRevision material = material("R1", "Scholarship");
        ApplicationRequestIdentity request = request("request-concurrent-create-1");
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<OpportunityPublicationState> first = executor.submit(() -> {
                ready.countDown();
                start.await();
                return service().establishDraft(request, material);
            });
            Future<OpportunityPublicationState> second = executor.submit(() -> {
                ready.countDown();
                start.await();
                return service().establishDraft(request, material);
            });

            ready.await();
            start.countDown();

            assertEquals(first.get(), second.get());
        }

        assertEquals(1, count("opportunity_publication_current"));
        assertEquals(1, count("opportunity_publication_revision_identity"));
        assertEquals(1, count("opportunity_publication_revision_material"));
        assertEquals(0, count("opportunity_publication_lifecycle_history"));
        assertEquals(1, count("opportunity_publication_application_request"));
    }

    @Test
    void request_evidence_failure_rolls_back_publication_mutation_atomically() {
        JooqOpportunityPublicationApplicationService service = service();
        dsl.execute(
                "alter table opportunity_publication_application_request "
                        + "drop constraint if exists " + ATOMICITY_PROBE_CONSTRAINT
        );
        dsl.execute(
                "alter table opportunity_publication_application_request "
                        + "add constraint " + ATOMICITY_PROBE_CONSTRAINT + " "
                        + "check (application_request_identity <> ?)",
                ATOMICITY_PROBE_REQUEST
        );

        try {
            assertThrows(
                    DataAccessException.class,
                    () -> service.establishDraft(
                            request(ATOMICITY_PROBE_REQUEST),
                            material("R1", "Scholarship")
                    )
            );

            assertEquals(Optional.empty(), authority().current(scope(), "opp-1"));
            assertEquals(0, count("opportunity_publication_current"));
            assertEquals(0, count("opportunity_publication_revision_identity"));
            assertEquals(0, count("opportunity_publication_revision_material"));
            assertEquals(0, count("opportunity_publication_revision_field_value"));
            assertEquals(0, count("opportunity_publication_lifecycle_history"));
            assertEquals(0, count("opportunity_publication_application_request"));
        } finally {
            dsl.execute(
                    "alter table opportunity_publication_application_request "
                            + "drop constraint if exists " + ATOMICITY_PROBE_CONSTRAINT
            );
        }
    }

    @Test
    void request_identity_reuse_for_different_intent_is_rejected_without_mutating_authoritative_state() {
        JooqOpportunityPublicationApplicationService service = service();
        ApplicationRequestIdentity request = request("request-create-1");
        OpportunityPublicationMaterialRevision original = material("R1", "Scholarship");
        service.establishDraft(request, original);

        OpportunityPublicationMaterialRevision changedIntent = material(
                "R1", "Different Scholarship"
        );

        assertThrows(
                PublicationApplicationRequestConflictException.class,
                () -> service.establishDraft(request, changedIntent)
        );
        assertEquals(original, authority().revision(scope(), "opp-1", "R1").orElseThrow());
        assertEquals(1, count("opportunity_publication_revision_material"));
        assertEquals(1, count("opportunity_publication_application_request"));
    }

    @Test
    void same_logical_publish_retry_creates_one_publication_history_entry() {
        JooqOpportunityPublicationApplicationService service = service();
        service.establishDraft(request("request-create-1"), material("R1", "Scholarship"));
        ApplicationRequestIdentity publishRequest = request("request-publish-1");

        OpportunityPublicationState first = service.publish(
                publishRequest, scope(), "opp-1", "R1"
        );
        OpportunityPublicationState replay = service.publish(
                publishRequest, scope(), "opp-1", "R1"
        );

        assertEquals(first, replay);
        assertEquals(PublicationLifecycle.PUBLISHED, replay.lifecycle());
        assertEquals(List.of("PUBLISH"), dsl.fetch(
                "select transition_kind from opportunity_publication_lifecycle_history "
                        + "where merchant_identifier = ? and opportunity_identity = ? "
                        + "order by transition_sequence",
                scope().merchantIdentifier(), "opp-1"
        ).getValues("transition_kind", String.class));
        assertEquals(2, count("opportunity_publication_application_request"));
    }

    @Test
    void distinct_stale_revision_request_remains_an_authoritative_revision_conflict_not_a_retry() {
        JooqOpportunityPublicationApplicationService service = service();
        service.establishDraft(request("request-create-1"), material("R1", "Scholarship"));
        service.revise(
                request("request-revise-1"),
                "R1",
                material("R2", "Corrected Scholarship")
        );

        assertThrows(
                PublicationRevisionConflictException.class,
                () -> service.revise(
                        request("request-revise-stale"),
                        "R1",
                        material("R3", "Stale Scholarship")
                )
        );
        assertEquals("R2", authority().current(scope(), "opp-1")
                .orElseThrow().currentRevisionIdentity());
        assertEquals(2, count("opportunity_publication_revision_material"));
    }

    @Test
    void replay_returns_original_committed_result_even_after_later_operations_change_current_state() {
        JooqOpportunityPublicationApplicationService service = service();
        ApplicationRequestIdentity createRequest = request("request-create-1");
        OpportunityPublicationMaterialRevision material = material("R1", "Scholarship");
        OpportunityPublicationState original = service.establishDraft(createRequest, material);
        service.publish(request("request-publish-1"), scope(), "opp-1", "R1");

        OpportunityPublicationState replay = service.establishDraft(createRequest, material);

        assertEquals(original, replay);
        assertEquals(PublicationLifecycle.DRAFT, replay.lifecycle());
        assertEquals(PublicationLifecycle.PUBLISHED, authority().current(scope(), "opp-1")
                .orElseThrow().lifecycle());
        assertEquals(1, count("opportunity_publication_lifecycle_history"));
    }

    private JooqOpportunityPublicationApplicationService service() {
        return new JooqOpportunityPublicationApplicationService(dsl, transactionManager);
    }

    private JooqOpportunityPublicationStateAuthority authority() {
        return new JooqOpportunityPublicationStateAuthority(dsl, transactionManager);
    }

    private OpportunityPublicationMaterialRevision material(String revision, String title) {
        return new OpportunityPublicationMaterialRevision(
                scope(),
                "opp-1",
                revision,
                title,
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                List.of(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );
    }

    private static MerchantScope scope() {
        return new MerchantScope("merchant-acme");
    }

    private static ApplicationRequestIdentity request(String value) {
        return new ApplicationRequestIdentity(value);
    }

    private int count(String table) {
        return dsl.fetchCount(DSL.table(DSL.name(table)));
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
