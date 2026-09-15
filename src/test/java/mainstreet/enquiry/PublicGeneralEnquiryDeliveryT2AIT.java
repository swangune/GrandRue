package mainstreet.enquiry;

import mainstreet.infrastructure.persistence.enquiry.*;
import org.flywaydb.core.Flyway;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.*;
import java.util.Optional;
import java.util.concurrent.atomic.*;
import static mainstreet.enquiry.PublicGeneralEnquiryDeliveryT2ATest.*;
import static mainstreet.enquiry.OpportunityEnquirySubmissionPreparationTest.SCOPE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PublicGeneralEnquiryDeliveryT2AIT {
    private DSLContext dsl;
    private JooqEnquirySubmissionApplicationService application;
    private Fixture fixture;

    @BeforeEach
    void setUp() {
        var source = new DriverManagerDataSource(env("MAINSTREET_TEST_POSTGRES_URL"),
                env("MAINSTREET_TEST_POSTGRES_USER"), env("MAINSTREET_TEST_POSTGRES_PASSWORD"));
        Flyway.configure().dataSource(source).locations("classpath:db/migration").load().migrate();
        dsl = DSL.using(new TransactionAwareDataSourceProxy(source), SQLDialect.POSTGRES);
        dsl.execute("truncate table enquiry_submission_application_request, enquiry_submission");
        application = new JooqEnquirySubmissionApplicationService(dsl, new DataSourceTransactionManager(source));
        fixture = new Fixture();
    }

    @Test
    void retry_reconciles_original_after_interaction_changes_without_fresh_preparation() throws Exception {
        var mvc = fixture.mvc(application);
        mvc.perform(post(PATH).header("Idempotency-Key", "k").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk());
        when(fixture.e3.activation.current(SCOPE.merchantIdentifier())).thenReturn(Optional.of(fixture.e3.release(false)));
        mvc.perform(post(PATH).header("Idempotency-Key", "k").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk()).andExpect(jsonPath("$.outcome").value("COMPLETED"));
        assertEquals(1, fixture.preparations.get());
        assertEquals(2, fixture.admissions.get());
        assertEquals(1, count());
        var stored = new JooqEnquirySubmissionStore(dsl).submission(SCOPE, "E1").orElseThrow();
        assertEquals(Optional.empty(), stored.subjectRevision());
        assertEquals(Optional.empty(), stored.customerContextIdentity());
        assertEquals(Optional.of("supplied@example.com"), stored.contact().email());
    }

    @Test
    void changed_intent_conflicts_but_new_logical_request_can_repeat_same_content() throws Exception {
        var mvc = fixture.mvc(application);
        mvc.perform(post(PATH).header("Idempotency-Key", "k").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk());
        mvc.perform(post(PATH).header("Idempotency-Key", "k").contentType(MediaType.APPLICATION_JSON)
                .content(BODY.replace("Original question", "Changed question"))).andExpect(status().isConflict());
        mvc.perform(post(PATH).header("Idempotency-Key", "new-k").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk());
        assertEquals(2, count());
        assertEquals(2, fixture.preparations.get());
    }

    @Test
    void lost_application_result_is_uncertain_and_same_key_retry_does_not_duplicate_commit() throws Exception {
        var first = new AtomicBoolean(true);
        EnquirySubmissionApplicationService interruptedResult = (key, intent, preparation) -> {
            var result = application.submit(key, intent, preparation);
            if (first.getAndSet(false)) throw new IllegalStateException("simulated failure after commit");
            return result;
        };
        var mvc = fixture.mvc(interruptedResult);
        mvc.perform(post(PATH).header("Idempotency-Key", "k").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isServiceUnavailable()).andExpect(jsonPath("$.outcome").value("OUTCOME_UNCERTAIN"));
        mvc.perform(post(PATH).header("Idempotency-Key", "k").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk());
        assertEquals(1, count());
        assertEquals(1, fixture.preparations.get());
    }

    @Test
    void denied_retry_cannot_use_key_to_bypass_current_admission() throws Exception {
        var calls = new AtomicInteger();
        EnquirySubmissionApplicationService counted = (key, intent, preparation) -> {
            calls.incrementAndGet();
            return application.submit(key, intent, preparation);
        };
        var mvc = fixture.mvc(counted);
        mvc.perform(post(PATH).header("Idempotency-Key", "k").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isOk());
        fixture.allowed.set(false);
        mvc.perform(post(PATH).header("Idempotency-Key", "k").contentType(MediaType.APPLICATION_JSON).content(BODY))
                .andExpect(status().isForbidden());
        assertEquals(1, calls.get());
        assertEquals(1, count());
    }

    private int count() { return dsl.fetchCount(DSL.table("enquiry_submission")); }
    private static String env(String name) {
        var value = System.getenv(name);
        if (value == null || value.isBlank()) throw new IllegalStateException("Missing " + name);
        return value;
    }
}
