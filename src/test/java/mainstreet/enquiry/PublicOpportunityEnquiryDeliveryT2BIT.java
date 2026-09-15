package mainstreet.enquiry;

import mainstreet.infrastructure.persistence.enquiry.*;
import mainstreet.infrastructure.persistence.publication.JooqOpportunityPublicationStateAuthority;
import mainstreet.publication.*;
import org.flywaydb.core.Flyway;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.*;
import org.springframework.test.web.servlet.MockMvc;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import static mainstreet.enquiry.PublicOpportunityEnquiryDeliveryT2BTest.*;
import static mainstreet.enquiry.OpportunityEnquirySubmissionPreparationTest.SCOPE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PublicOpportunityEnquiryDeliveryT2BIT {
    private DSLContext dsl;
    private JooqEnquirySubmissionApplicationService application;
    private JooqOpportunityPublicationStateAuthority owner;
    private OpportunityPublicationState published;
    private Fixture fixture;

    @BeforeEach
    void setUp() {
        var source = new DriverManagerDataSource(env("MAINSTREET_TEST_POSTGRES_URL"),
                env("MAINSTREET_TEST_POSTGRES_USER"), env("MAINSTREET_TEST_POSTGRES_PASSWORD"));
        Flyway.configure().dataSource(source).locations("classpath:db/migration").load().migrate();
        dsl = DSL.using(new TransactionAwareDataSourceProxy(source), SQLDialect.POSTGRES);
        dsl.execute("truncate table enquiry_submission_application_request, enquiry_submission, "
                + "opportunity_publication_application_request, opportunity_publication_lifecycle_history, "
                + "opportunity_publication_revision_field_value, opportunity_publication_revision_material, "
                + "opportunity_publication_current, opportunity_publication_revision_identity");
        var transactions = new DataSourceTransactionManager(source);
        application = new JooqEnquirySubmissionApplicationService(dsl, transactions);
        owner = new JooqOpportunityPublicationStateAuthority(dsl, transactions);
        fixture = new Fixture();
        var material = fixture.e3.publication.revision(SCOPE, "O1", "R7").orElseThrow();
        var draft = new OpportunityPublicationState(SCOPE, "O1", "R7", PublicationLifecycle.DRAFT, Optional.empty());
        owner.establish(draft, material);
        published = draft.publish("R7");
        owner.compareAndSet(draft, published, Optional.empty());
        when(fixture.e3.publication.current(any(), anyString())).thenAnswer(c -> owner.current(c.getArgument(0), c.getArgument(1)));
        when(fixture.e3.publication.revision(any(), anyString(), anyString())).thenAnswer(
                c -> owner.revision(c.getArgument(0), c.getArgument(1), c.getArgument(2)));
        when(fixture.e3.lock.lockCurrent(any(), anyString())).thenAnswer(c -> owner.lockCurrent(c.getArgument(0), c.getArgument(1)));
    }

    @Test
    void committed_retry_survives_withdrawal_and_disabled_interaction_but_new_request_fails() throws Exception {
        var token = fixture.binding();
        var mvc = fixture.mvc(application);
        submit(mvc, "k", body(token)).andExpect(status().isOk());
        owner.compareAndSet(published, published.withdraw("R7"), Optional.empty());
        when(fixture.e3.activation.current(SCOPE.merchantIdentifier())).thenReturn(Optional.of(fixture.e3.release(false)));
        submit(mvc, "k", body(token)).andExpect(status().isOk());
        submit(mvc, "new", body(token)).andExpect(status().isUnprocessableEntity());
        assertEquals(1, count());
        assertEquals(1, fixture.preparations.get());
        var stored = new JooqEnquirySubmissionStore(dsl).submission(SCOPE, "E1").orElseThrow();
        assertEquals(fixture.e3.intent(true).subjectRevision(), stored.subjectRevision());
        assertEquals(Optional.empty(), stored.customerContextIdentity());
    }

    @Test
    void equivalent_reissued_binding_replays_changed_intent_conflicts_and_intentional_repeat_appends() throws Exception {
        var first = fixture.binding();
        var second = fixture.binding();
        var mvc = fixture.mvc(application);
        submit(mvc, "k", body(first)).andExpect(status().isOk());
        submit(mvc, "k", body(second)).andExpect(status().isOk());
        submit(mvc, "k", body(first).replace("Question", "Changed")).andExpect(status().isConflict());
        submit(mvc, "new", body(second)).andExpect(status().isOk());
        assertEquals(2, count());
        assertEquals(2, fixture.preparations.get());
    }

    @Test
    void post_commit_lost_response_is_uncertain_and_reconciles_original_subject_after_withdrawal() throws Exception {
        var token = fixture.binding();
        var first = new AtomicBoolean(true);
        EnquirySubmissionApplicationService interrupted = (key, intent, preparation) -> {
            var result = application.submit(key, intent, preparation);
            if (first.getAndSet(false)) throw new IllegalStateException("Lost result");
            return result;
        };
        var mvc = fixture.mvc(interrupted);
        submit(mvc, "k", body(token)).andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.outcome").value("OUTCOME_UNCERTAIN"));
        owner.compareAndSet(published, published.withdraw("R7"), Optional.empty());
        submit(mvc, "k", body(token)).andExpect(status().isOk());
        assertEquals(1, count());
        assertEquals(1, fixture.preparations.get());
    }

    @Test
    void new_published_revision_cannot_change_existing_retry_meaning() throws Exception {
        var oldToken = fixture.binding();
        var mvc = fixture.mvc(application);
        submit(mvc, "k", body(oldToken)).andExpect(status().isOk());
        var revised = published.revise("R7", "R8");
        owner.compareAndSet(published, revised, Optional.of(new OpportunityPublicationMaterialRevision(
                SCOPE, "O1", "R8", "New title", Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), List.of(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())));
        owner.compareAndSet(revised, revised.publish("R8"), Optional.empty());
        var newToken = fixture.binding();
        submit(mvc, "k", body(newToken)).andExpect(status().isConflict());
        submit(mvc, "k", body(oldToken)).andExpect(status().isOk());
        submit(mvc, "new-stale", body(oldToken)).andExpect(status().isUnprocessableEntity());
        submit(mvc, "new", body(newToken)).andExpect(status().isOk());
        assertEquals(2, count());
        var store = new JooqEnquirySubmissionStore(dsl);
        assertEquals("R7", store.submission(SCOPE, "E1").orElseThrow().subjectRevision().orElseThrow().revisionIdentity());
        assertEquals("R8", store.submission(SCOPE, "E2").orElseThrow().subjectRevision().orElseThrow().revisionIdentity());
    }

    @Test
    void stale_binding_rejects_atomically_without_claiming_retry_key() throws Exception {
        var token = fixture.binding();
        owner.compareAndSet(published, published.withdraw("R7"), Optional.empty());
        var mvc = fixture.mvc(application);
        submit(mvc, "k", body(token)).andExpect(status().isUnprocessableEntity());
        assertEquals(0, count());
        assertEquals(0, dsl.fetchCount(DSL.table("enquiry_submission_application_request")));
        var withdrawn = owner.current(SCOPE, "O1").orElseThrow();
        owner.compareAndSet(withdrawn, withdrawn.republish("R7"), Optional.empty());
        submit(mvc, "k", body(token)).andExpect(status().isOk());
        assertEquals(1, count());
    }

    private org.springframework.test.web.servlet.ResultActions submit(MockMvc mvc, String key, String body) throws Exception {
        return mvc.perform(post(SUBMIT_PATH).header("Idempotency-Key", key)
                .contentType(MediaType.APPLICATION_JSON).content(body));
    }
    private int count() { return dsl.fetchCount(DSL.table("enquiry_submission")); }
    private static String env(String name) {
        var value = System.getenv(name);
        if (value == null || value.isBlank()) throw new IllegalStateException("Missing " + name);
        return value;
    }
}
