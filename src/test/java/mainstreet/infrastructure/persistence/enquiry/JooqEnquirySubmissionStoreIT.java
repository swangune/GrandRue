package mainstreet.infrastructure.persistence.enquiry;

import mainstreet.application.MerchantScope;
import mainstreet.enquiry.*;
import mainstreet.infrastructure.persistence.publication.JooqOpportunityPublicationStateAuthority;
import mainstreet.publication.OpportunityPublicationMaterialRevision;
import mainstreet.publication.OpportunityPublicationState;
import mainstreet.semantic.registry.OwnedOperationalObjectTypeReference;
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
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JooqEnquirySubmissionStoreIT {
    private DSLContext dsl;
    private TransactionTemplate transactions;
    private DataSourceTransactionManager transactionManager;

    @BeforeEach
    void setUp() {
        var dataSource = new DriverManagerDataSource(env("MAINSTREET_TEST_POSTGRES_URL"),
                env("MAINSTREET_TEST_POSTGRES_USER"), env("MAINSTREET_TEST_POSTGRES_PASSWORD"));
        Flyway.configure().dataSource(dataSource).locations("classpath:db/migration").load().migrate();
        transactionManager = new DataSourceTransactionManager(dataSource);
        transactions = new TransactionTemplate(transactionManager);
        dsl = DSL.using(new TransactionAwareDataSourceProxy(dataSource), SQLDialect.POSTGRES);
        dsl.execute("truncate table enquiry_submission_application_request, enquiry_submission");
    }

    @Test
    void exact_submission_survives_new_adapter_with_nanosecond_time_and_revision_evidence() {
        var original = submission("merchant-a", "E1", true);
        store().append(original);
        assertEquals(original, store().submission(original.merchantScope(), "E1").orElseThrow());
    }

    @Test
    void general_submission_and_missing_contact_remain_explicitly_absent() {
        var original = submission("merchant-a", "E1", false);
        store().append(original);
        assertEquals(original, store().submission(original.merchantScope(), "E1").orElseThrow());
        assertTrue(store().submission(original.merchantScope(), "missing").isEmpty());
    }

    @Test
    void merchant_scope_is_part_of_identity_and_read_boundary() {
        var a = submission("merchant-a", "E1", true);
        var b = submission("merchant-b", "E1", false);
        store().append(a);
        assertTrue(store().submission(b.merchantScope(), "E1").isEmpty());
        store().append(b);
        assertEquals(a, store().submission(a.merchantScope(), "E1").orElseThrow());
        assertEquals(b, store().submission(b.merchantScope(), "E1").orElseThrow());
    }

    @Test
    void existing_identity_cannot_rewrite_any_initial_evidence() {
        var original = submission("merchant-a", "E1", true);
        store().append(original);
        assertThrows(EnquiryIdentityConflictException.class, () -> store().append(original));
        assertThrows(EnquiryIdentityConflictException.class,
                () -> store().append(submission("merchant-a", "E1", false)));
        assertEquals(original, store().submission(original.merchantScope(), "E1").orElseThrow());
        assertEquals(1, dsl.fetchCount(DSL.table("enquiry_submission")));
    }

    @Test
    void separate_human_submissions_with_identical_content_are_not_deduplicated() {
        store().append(submission("merchant-a", "E1", true));
        store().append(submission("merchant-a", "E2", true));
        assertEquals(2, dsl.fetchCount(DSL.table("enquiry_submission")));
    }

    @Test
    void append_participates_in_callers_transaction_without_leaving_partial_evidence() {
        var original = submission("merchant-a", "E1", true);
        transactions.executeWithoutResult(status -> {
            store().append(original);
            assertTrue(store().submission(original.merchantScope(), "E1").isPresent());
            status.setRollbackOnly();
        });
        assertTrue(store().submission(original.merchantScope(), "E1").isEmpty());
    }

    @Test
    void database_rejects_partial_subject_revision_provenance() {
        var original = submission("merchant-a", "E1", true);
        store().append(original);
        assertThrows(org.jooq.exception.DataAccessException.class,
                () -> dsl.execute("update enquiry_submission set subject_revision_identity = null"));
        assertEquals(original, store().submission(original.merchantScope(), "E1").orElseThrow());
    }

    private JooqEnquirySubmissionStore store() { return new JooqEnquirySubmissionStore(dsl); }

    @Test
    void subject_revision_remains_reconstructible_when_publication_advances_and_withdraws() {
        // Roll back the cross-owner fixture so other Publication tests retain their own isolation.
        transactions.executeWithoutResult(status -> {
            var authority = new JooqOpportunityPublicationStateAuthority(dsl, transactionManager);
            var original = submission("merchant-enquiry-provenance", "E1", true);
            var draft = OpportunityPublicationState.draft(original.merchantScope(), "O1", "R7");
            authority.establish(draft, material(draft));
            var published = draft.publish("R7");
            authority.compareAndSet(draft, published, Optional.empty());
            store().append(original);
            var revised = published.revise("R7", "R8");
            authority.compareAndSet(published, revised, Optional.of(material(revised)));
            authority.compareAndSet(revised, revised.withdraw("R8"), Optional.empty());

            var retained = store().submission(original.merchantScope(), "E1").orElseThrow();
            assertEquals(original, retained);
            var reference = retained.subjectRevision().orElseThrow();
            assertEquals("R7", reference.revisionIdentity());
            assertEquals("Title R7", authority.revision(retained.merchantScope(),
                    reference.subjectIdentity(), reference.revisionIdentity()).orElseThrow().title());
            assertEquals("R8", authority.current(retained.merchantScope(), "O1")
                    .orElseThrow().currentRevisionIdentity());
            status.setRollbackOnly();
        });
    }

    private OpportunityPublicationMaterialRevision material(OpportunityPublicationState state) {
        return new OpportunityPublicationMaterialRevision(state.merchantScope(), state.opportunityIdentity(),
                state.currentRevisionIdentity(), "Title " + state.currentRevisionIdentity(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), List.of(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    }

    private EnquirySubmission submission(String merchant, String identity, boolean withSubject) {
        return new EnquirySubmission(new MerchantScope(merchant), identity,
                Instant.parse("2026-09-05T12:00:00.123456789Z"), "  Can I apply?\nThank you.  ",
                withSubject ? new EnquirySubmittedContact(Optional.of("  Alex  "),
                        Optional.of("Alex@example.test"), Optional.of("+44 1234 567890"))
                        : new EnquirySubmittedContact(Optional.empty(), Optional.empty(), Optional.empty()),
                new EnquirySemanticContext("release-1", "model-1", 7),
                withSubject ? Optional.of(new EnquiryRevisionProvenance(
                        new OwnedOperationalObjectTypeReference("publication", "opportunity"), "O1", "R7"))
                        : Optional.empty(),
                withSubject ? Optional.of("trusted-customer-reference") : Optional.empty());
    }

    private static String env(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) throw new IllegalStateException("Missing " + name);
        return value;
    }
}
