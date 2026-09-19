package grandrue.surface;

import grandrue.enquiry.*;
import grandrue.infrastructure.persistence.enquiry.JooqEnquirySubmissionStore;
import grandrue.infrastructure.persistence.runtime.JooqSessionRecordStore;
import org.flywaydb.core.Flyway;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.*;
import org.springframework.jdbc.datasource.*;
import static grandrue.surface.EnquiryMerchantExposureM1Test.*;
import static grandrue.surface.MerchantEnquiryQueryT3Test.PATH;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MerchantEnquiryQueryT3IT {
    private DSLContext dsl;
    private JooqEnquirySubmissionStore submissions;
    private JooqSessionRecordStore sessions;
    private MerchantEnquiryQueryT3Test.Fixture fixture;

    @BeforeEach
    void setUp() {
        var source = new DriverManagerDataSource(env("MAINSTREET_TEST_POSTGRES_URL"),
                env("MAINSTREET_TEST_POSTGRES_USER"), env("MAINSTREET_TEST_POSTGRES_PASSWORD"));
        Flyway.configure().dataSource(source).locations("classpath:db/migration").load().migrate();
        dsl = DSL.using(new TransactionAwareDataSourceProxy(source), SQLDialect.POSTGRES);
        dsl.execute("truncate table enquiry_submission_application_request, enquiry_submission, authentication_session_record");
        submissions = new JooqEnquirySubmissionStore(dsl);
        submissions.append(submission(true));
        sessions = new JooqSessionRecordStore(dsl);
        fixture = new MerchantEnquiryQueryT3Test.Fixture(submissions, sessions);
    }

    @Test
    void durable_credential_and_submission_deliver_exact_original_evidence_without_mutation() throws Exception {
        var session = sessions.sessionByIdentity("session").orElseThrow();
        var original = submissions.submission(SCOPE, "E1").orElseThrow();
        fixture.mvc().perform(get(PATH).header("Authorization", fixture.authorization()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.submission.question").value(original.question()))
                .andExpect(jsonPath("$.subjectContext.submissionRevisionIdentity").value("R7"));
        assertEquals(original, new JooqEnquirySubmissionStore(dsl).submission(SCOPE, "E1").orElseThrow());
        assertEquals(session, new JooqSessionRecordStore(dsl).sessionByIdentity("session").orElseThrow());
        assertEquals(1, dsl.fetchCount(DSL.table("enquiry_submission")));
        assertEquals(0, dsl.fetchCount(DSL.table("enquiry_submission_application_request")));
    }

    @Test
    void persisted_session_revocation_blocks_the_same_http_credential_on_next_request() throws Exception {
        var mvc = fixture.mvc();
        mvc.perform(get(PATH).header("Authorization", fixture.authorization())).andExpect(status().isOk());
        new JooqSessionRecordStore(dsl).revoke("session", NOW, "revoked");
        mvc.perform(get(PATH).header("Authorization", fixture.authorization())).andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.submission").doesNotExist()).andExpect(jsonPath("$.contact").doesNotExist());
        assertEquals(submission(true), submissions.submission(SCOPE, "E1").orElseThrow());
    }

    @Test
    void family_revocation_withholds_contact_while_preserving_other_selected_values() throws Exception {
        fixture.m1.actor = (scope, principal, privilege) -> scope.equals(SCOPE)
                && !privilege.equals(fixture.m1.privileges.get(EnquiryMerchantExposureReferences.SUBMITTED_CONTACT));
        var body = fixture.mvc().perform(get(PATH).header("Authorization", fixture.authorization()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.contact").doesNotExist())
                .andExpect(jsonPath("$.submission.question").value("Original question"))
                .andExpect(jsonPath("$.subjectContext.submissionRevisionIdentity").value("R7"))
                .andReturn().getResponse().getContentAsString();
        assertFalse(body.contains("Alex"));
        assertEquals(submission(true), submissions.submission(SCOPE, "E1").orElseThrow());
    }

    @Test
    void foreign_scope_and_absent_record_have_same_safe_response_and_lost_membership_denies() throws Exception {
        var mvc = fixture.mvc();
        var absent = mvc.perform(get(PATH.replace("E1", "missing")).header("Authorization", fixture.authorization()))
                .andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();
        mvc.perform(get(PATH.replace("shop", "other")).header("Authorization", fixture.authorization()))
                .andExpect(status().isNotFound()).andExpect(content().string(absent));
        fixture.member.set(false);
        mvc.perform(get(PATH).header("Authorization", fixture.authorization())).andExpect(status().isForbidden());
        assertEquals(1, dsl.fetchCount(DSL.table("enquiry_submission")));
    }

    private static String env(String name) {
        var value = System.getenv(name);
        if (value == null || value.isBlank()) throw new IllegalStateException("Missing " + name);
        return value;
    }
}
