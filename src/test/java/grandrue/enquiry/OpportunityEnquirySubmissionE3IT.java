package grandrue.enquiry;

import grandrue.application.ApplicationRequestIdentity;
import grandrue.infrastructure.persistence.enquiry.JooqEnquirySubmissionApplicationService;
import grandrue.infrastructure.persistence.enquiry.JooqEnquirySubmissionStore;
import grandrue.infrastructure.persistence.publication.JooqOpportunityPublicationStateAuthority;
import grandrue.publication.*;
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
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static grandrue.enquiry.OpportunityEnquirySubmissionPreparationTest.*;
import static org.junit.jupiter.api.Assertions.*;

class OpportunityEnquirySubmissionE3IT {
    private DSLContext dsl;
    private DataSourceTransactionManager transactions;
    private JooqOpportunityPublicationStateAuthority publication;
    private Fixture fixture;

    @BeforeEach
    void setUp() {
        var source = new DriverManagerDataSource(env("MAINSTREET_TEST_POSTGRES_URL"),
                env("MAINSTREET_TEST_POSTGRES_USER"), env("MAINSTREET_TEST_POSTGRES_PASSWORD"));
        Flyway.configure().dataSource(source).locations("classpath:db/migration").load().migrate();
        transactions = new DataSourceTransactionManager(source);
        dsl = DSL.using(new TransactionAwareDataSourceProxy(source), SQLDialect.POSTGRES);
        dsl.execute("truncate table enquiry_submission_application_request, enquiry_submission, "
                + "opportunity_publication_application_request, opportunity_publication_lifecycle_history, "
                + "opportunity_publication_revision_field_value, opportunity_publication_revision_material, "
                + "opportunity_publication_current, opportunity_publication_revision_identity");
        publication = new JooqOpportunityPublicationStateAuthority(dsl, transactions);
        fixture = new Fixture();
        var draft = OpportunityPublicationState.draft(SCOPE, "O1", "R7");
        publication.establish(draft, material("R7"));
        publication.compareAndSet(draft, draft.publish("R7"), Optional.empty());
    }

    @Test
    void real_revalidation_commits_exact_provenance_and_retry_survives_later_withdrawal() {
        var request = new ApplicationRequestIdentity("request-1");
        var first = service().submit(request, fixture.intent(true), preparation(fixture.additional));
        var current = publication.current(SCOPE, "O1").orElseThrow();
        publication.compareAndSet(current, current.withdraw("R7"), Optional.empty());
        assertEquals(first, service().submit(request, fixture.intent(true), preparation(fixture.additional)));
        assertEquals("R7", first.subjectRevision().orElseThrow().revisionIdentity());
        assertEquals(first, new JooqEnquirySubmissionStore(dsl).submission(SCOPE, "E1").orElseThrow());
        assertEquals(1, fixture.preparations.get());
        assertEquals(1, count("enquiry_submission_application_request"));
    }

    @Test
    void withdrawn_subject_rejects_new_submission_without_fact_or_receipt() {
        var current = publication.current(SCOPE, "O1").orElseThrow();
        publication.compareAndSet(current, current.withdraw("R7"), Optional.empty());
        assertThrows(EnquirySubmissionRevalidationException.class, () -> service().submit(
                new ApplicationRequestIdentity("request-1"), fixture.intent(true), preparation(fixture.additional)));
        assertEquals(0, count("enquiry_submission"));
        assertEquals(0, count("enquiry_submission_application_request"));
    }

    @Test
    void changed_published_revision_rejects_old_selection_but_independent_general_enquiry_is_valid() {
        var old = publication.current(SCOPE, "O1").orElseThrow();
        var revised = old.revise("R7", "R8");
        publication.compareAndSet(old, revised, Optional.of(material("R8")));
        publication.compareAndSet(revised, revised.publish("R8"), Optional.empty());
        assertThrows(EnquirySubmissionRevalidationException.class, () -> service().submit(
                new ApplicationRequestIdentity("request-stale"), fixture.intent(true), preparation(fixture.additional)));
        assertEquals(0, count("enquiry_submission"));
        var general = service().submit(new ApplicationRequestIdentity("request-general"),
                fixture.intent(false), preparation(fixture.additional));
        assertTrue(general.subjectRevision().isEmpty());
        assertEquals(1, count("enquiry_submission_application_request"));
    }

    @Test
    void publication_submission_lock_requires_enclosing_transaction() {
        assertThrows(IllegalStateException.class, () -> publication.lockCurrent(SCOPE, "O1"));
    }

    @Test
    void withdrawal_cannot_cross_validated_submission_transaction() throws Exception {
        var entered = new CountDownLatch(1);
        var release = new CountDownLatch(1);
        EnquirySubmissionPreparation requirements = intent -> {
            entered.countDown();
            try {
                if (!release.await(10, TimeUnit.SECONDS)) throw new IllegalStateException("Test did not release lock");
            } catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(interrupted);
            }
            return fixture.prepared(intent);
        };
        try (var executor = Executors.newSingleThreadExecutor()) {
            var submission = executor.submit(() -> service().submit(new ApplicationRequestIdentity("request-race"),
                    fixture.intent(true), preparation(requirements)));
            try {
                assertTrue(entered.await(10, TimeUnit.SECONDS));
                DataAccessException blocked = assertThrows(DataAccessException.class,
                        () -> new TransactionTemplate(transactions).executeWithoutResult(status -> {
                            // A bounded database lock timeout proves actual lock contention; no test sleep.
                            dsl.execute("set local lock_timeout = '200ms'");
                            var current = publication.current(SCOPE, "O1").orElseThrow();
                            publication.compareAndSet(current, current.withdraw("R7"), Optional.empty());
                        }));
                assertEquals("55P03", blocked.sqlState());
            } finally {
                release.countDown();
            }
            assertEquals("R7", submission.get(10, TimeUnit.SECONDS).subjectRevision().orElseThrow().revisionIdentity());
        }
        var current = publication.current(SCOPE, "O1").orElseThrow();
        assertEquals(PublicationLifecycle.PUBLISHED, current.lifecycle());
        publication.compareAndSet(current, current.withdraw("R7"), Optional.empty());
        assertEquals(1, count("enquiry_submission"));
    }

    private OpportunityEnquirySubmissionPreparation preparation(EnquirySubmissionPreparation remaining) {
        return new OpportunityEnquirySubmissionPreparation(SCOPE, fixture.activation, fixture.registry,
                fixture.definition, publication, publication, remaining, fixture.clock);
    }
    private JooqEnquirySubmissionApplicationService service() {
        return new JooqEnquirySubmissionApplicationService(dsl, transactions);
    }
    private int count(String table) { return dsl.fetchCount(DSL.table(DSL.name(table))); }
    private OpportunityPublicationMaterialRevision material(String revision) {
        return new OpportunityPublicationMaterialRevision(SCOPE, "O1", revision, "Title " + revision,
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), List.of(),
                Optional.empty(), Optional.empty(), Optional.of(new OpportunityExactInstantBoundary(NOW)),
                Optional.of(new OpportunityExactInstantBoundary(NOW.plusSeconds(60))));
    }
    private static String env(String name) {
        var value = System.getenv(name);
        if (value == null || value.isBlank()) throw new IllegalStateException("Missing " + name);
        return value;
    }
}
