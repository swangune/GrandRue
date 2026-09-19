package grandrue.infrastructure.persistence.enquiry;

import grandrue.application.ApplicationRequestIdentity;
import grandrue.application.MerchantScope;
import grandrue.enquiry.*;
import grandrue.semantic.registry.OwnedOperationalObjectTypeReference;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class JooqEnquirySubmissionApplicationServiceIT {
    private DSLContext dsl;
    private DataSourceTransactionManager transactionManager;

    @BeforeEach
    void setUp() {
        var dataSource = new DriverManagerDataSource(env("MAINSTREET_TEST_POSTGRES_URL"),
                env("MAINSTREET_TEST_POSTGRES_USER"), env("MAINSTREET_TEST_POSTGRES_PASSWORD"));
        Flyway.configure().dataSource(dataSource).locations("classpath:db/migration").load().migrate();
        transactionManager = new DataSourceTransactionManager(dataSource);
        dsl = DSL.using(new TransactionAwareDataSourceProxy(dataSource), SQLDialect.POSTGRES);
        dsl.execute("truncate table enquiry_submission_application_request, enquiry_submission");
    }

    @Test
    void replay_returns_original_result_without_repreparation_after_context_changes() {
        var intent = intent("merchant-a", "Question", true);
        var first = service().submit(request("request-1"), intent, i -> prepared(i, "E1"));
        var replay = service().submit(request("request-1"), intent,
                i -> { throw new AssertionError("Committed retries must not re-resolve current authority"); });
        assertEquals(first, replay);
        assertEquals(1, count("enquiry_submission"));
        assertEquals(1, count("enquiry_submission_application_request"));
    }

    @Test
    void concurrent_deliveries_prepare_and_commit_exactly_once() throws Exception {
        var intent = intent("merchant-a", "Question", true);
        var calls = new AtomicInteger();
        var ready = new CountDownLatch(2);
        var start = new CountDownLatch(1);
        try (var executor = Executors.newFixedThreadPool(2)) {
            java.util.concurrent.Callable<EnquirySubmission> delivery = () -> {
                ready.countDown();
                assertTrue(start.await(10, TimeUnit.SECONDS));
                return service().submit(request("same-request"), intent,
                        i -> prepared(i, "E" + calls.incrementAndGet()));
            };
            var first = executor.submit(delivery);
            var second = executor.submit(delivery);
            assertTrue(ready.await(10, TimeUnit.SECONDS));
            start.countDown();
            assertEquals(first.get(20, TimeUnit.SECONDS), second.get(20, TimeUnit.SECONDS));
        }
        assertEquals(1, calls.get());
        assertEquals(1, count("enquiry_submission"));
        assertEquals(1, count("enquiry_submission_application_request"));
    }

    @Test
    void same_key_with_changed_scope_content_contact_subject_or_customer_fails_closed() {
        var intent = intent("merchant-a", "Question", true);
        var original = service().submit(request("request-1"), intent, i -> prepared(i, "E1"));
        var variants = List.of(
                intent("merchant-b", "Question", true),
                intent("merchant-a", "Different question", true),
                intent("merchant-a", "Question", false),
                new EnquirySubmissionIntent(intent.merchantScope(), intent.question(),
                        new EnquirySubmittedContact(Optional.empty(), Optional.empty(), Optional.empty()),
                        intent.subjectRevision(), intent.customerContextIdentity()),
                new EnquirySubmissionIntent(intent.merchantScope(), intent.question(), intent.contact(),
                        Optional.of(new EnquiryRevisionProvenance(
                                new OwnedOperationalObjectTypeReference("publication", "opportunity"), "O2", "R7")),
                        intent.customerContextIdentity()),
                new EnquirySubmissionIntent(intent.merchantScope(), intent.question(), intent.contact(),
                        intent.subjectRevision(), Optional.of("another-customer")));
        for (var changed : variants) {
            assertThrows(EnquiryApplicationRequestConflictException.class,
                    () -> service().submit(request("request-1"), changed,
                            i -> { throw new AssertionError("Conflict must precede preparation"); }));
        }
        assertEquals(original, store().submission(intent.merchantScope(), "E1").orElseThrow());
        assertEquals(1, count("enquiry_submission"));
    }

    @Test
    void independent_logical_requests_with_identical_content_remain_distinct() {
        var intent = intent("merchant-a", "Question", false);
        var a = service().submit(request("request-1"), intent, i -> prepared(i, "E1"));
        var b = service().submit(request("request-2"), intent, i -> prepared(i, "E2"));
        assertNotEquals(a.enquiryIdentity(), b.enquiryIdentity());
        assertEquals(2, count("enquiry_submission"));
        assertEquals(2, count("enquiry_submission_application_request"));
    }

    @Test
    void receipt_failure_rolls_back_submission_and_allows_clean_retry() {
        var intent = intent("merchant-a", "Question", true);
        dsl.execute("alter table enquiry_submission_application_request add constraint e2_atomicity_probe "
                + "check (application_request_identity <> 'blocked-request')");
        try {
            assertThrows(org.jooq.exception.DataAccessException.class,
                    () -> service().submit(request("blocked-request"), intent, i -> prepared(i, "E1")));
            assertEquals(0, count("enquiry_submission"));
            assertEquals(0, count("enquiry_submission_application_request"));
        } finally {
            dsl.execute("alter table enquiry_submission_application_request drop constraint e2_atomicity_probe");
        }
        assertEquals("E1", service().submit(request("blocked-request"), intent,
                i -> prepared(i, "E1")).enquiryIdentity());
    }

    @Test
    void submission_failure_leaves_no_retry_receipt_or_overwrite() {
        var intent = intent("merchant-a", "Question", true);
        store().append(prepared(intent, "occupied"));
        assertThrows(EnquiryIdentityConflictException.class,
                () -> service().submit(request("request-1"), intent, i -> prepared(i, "occupied")));
        assertEquals(1, count("enquiry_submission"));
        assertEquals(0, count("enquiry_submission_application_request"));
        assertEquals("E2", service().submit(request("request-1"), intent,
                i -> prepared(i, "E2")).enquiryIdentity());
    }

    @Test
    void rejected_preparation_and_subject_downgrade_commit_neither_fact_nor_receipt() {
        var intent = intent("merchant-a", "Question", true);
        assertThrows(IllegalStateException.class, () -> service().submit(request("request-1"), intent,
                i -> { throw new IllegalStateException("Subject no longer eligible"); }));
        assertThrows(IllegalArgumentException.class, () -> service().submit(request("request-1"), intent,
                i -> prepared(intent("merchant-a", "Question", false), "E1")));
        assertEquals(0, count("enquiry_submission"));
        assertEquals(0, count("enquiry_submission_application_request"));
    }

    @Test
    void caller_rollback_removes_both_submission_and_receipt() {
        var intent = intent("merchant-a", "Question", false);
        new TransactionTemplate(transactionManager).executeWithoutResult(status -> {
            service().submit(request("request-1"), intent, i -> prepared(i, "E1"));
            assertEquals(1, count("enquiry_submission_application_request"));
            status.setRollbackOnly();
        });
        assertEquals(0, count("enquiry_submission"));
        assertEquals(0, count("enquiry_submission_application_request"));
    }

    @Test
    void distinct_requests_keep_same_business_identity_isolated_by_merchant() {
        var a = service().submit(request("request-a"), intent("merchant-a", "Question", false),
                i -> prepared(i, "E1"));
        var b = service().submit(request("request-b"), intent("merchant-b", "Question", false),
                i -> prepared(i, "E1"));
        assertEquals(a, store().submission(a.merchantScope(), "E1").orElseThrow());
        assertEquals(b, store().submission(b.merchantScope(), "E1").orElseThrow());
        assertEquals(2, count("enquiry_submission_application_request"));
    }

    private JooqEnquirySubmissionApplicationService service() {
        return new JooqEnquirySubmissionApplicationService(dsl, transactionManager);
    }
    private JooqEnquirySubmissionStore store() { return new JooqEnquirySubmissionStore(dsl); }
    private int count(String table) { return dsl.fetchCount(DSL.table(DSL.name(table))); }
    private ApplicationRequestIdentity request(String value) { return new ApplicationRequestIdentity(value); }

    private EnquirySubmissionIntent intent(String merchant, String question, boolean subject) {
        return new EnquirySubmissionIntent(new MerchantScope(merchant), question,
                new EnquirySubmittedContact(Optional.of("Alex"), Optional.of("alex@example.test"), Optional.empty()),
                subject ? Optional.of(new EnquiryRevisionProvenance(
                        new OwnedOperationalObjectTypeReference("publication", "opportunity"), "O1", "R7"))
                        : Optional.empty(), Optional.empty());
    }

    private EnquirySubmission prepared(EnquirySubmissionIntent intent, String identity) {
        return new EnquirySubmission(intent.merchantScope(), identity,
                Instant.parse("2026-09-05T19:00:00.123456789Z"), intent.question(), intent.contact(),
                new EnquirySemanticContext("release-1", "model-1", 1), intent.subjectRevision(),
                intent.customerContextIdentity());
    }

    private static String env(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) throw new IllegalStateException("Missing " + name);
        return value;
    }
}
