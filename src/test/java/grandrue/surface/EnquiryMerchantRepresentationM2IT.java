package grandrue.surface;

import grandrue.application.MerchantScope;
import grandrue.enquiry.AuthorityBackedEnquiryMerchantRepresentationProjectionReadPort;
import grandrue.enquiry.EnquiryMerchantExposureReferences;
import grandrue.enquiry.EnquiryMerchantRepresentation;
import grandrue.enquiry.EnquiryMerchantRepresentationProjectionFragment;
import grandrue.infrastructure.persistence.enquiry.JooqEnquirySubmissionStore;
import org.flywaydb.core.Flyway;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import static grandrue.surface.EnquiryMerchantExposureM1Test.*;
import static grandrue.surface.EnquiryMerchantRepresentationM2Test.serviceability;
import static org.junit.jupiter.api.Assertions.*;

class EnquiryMerchantRepresentationM2IT {
    private JooqEnquirySubmissionStore store;
    private EnquiryMerchantRepresentationM2Test.Fixture fixture;

    @BeforeEach
    void setUp() {
        var source = new DriverManagerDataSource(env("GRANDRUE_TEST_POSTGRES_URL"),
                env("GRANDRUE_TEST_POSTGRES_USER"), env("GRANDRUE_TEST_POSTGRES_PASSWORD"));
        Flyway.configure().dataSource(source).locations("classpath:db/migration").load().migrate();
        var dsl = DSL.using(new TransactionAwareDataSourceProxy(source), SQLDialect.POSTGRES);
        dsl.execute("truncate table enquiry_submission_application_request, enquiry_submission");
        store = new JooqEnquirySubmissionStore(dsl);
        fixture = new EnquiryMerchantRepresentationM2Test.Fixture();
    }

    @Test
    void durable_material_is_selected_by_current_family_privilege_without_contact_leakage() {
        store.append(submission(true));
        var read = new AuthorityBackedEnquiryMerchantRepresentationProjectionReadPort(store)
                .observe(SCOPE, "E1", NOW).toBoundedRead(fixture.request);
        fixture.m1.actor = (scope, principal, privilege) ->
                !privilege.equals(fixture.m1.privileges.get(EnquiryMerchantExposureReferences.SUBMITTED_CONTACT));
        var result = new MerchantProjectionAssemblyService().assemble(read, serviceability(read, true),
                fixture.exposure(read, store));
        assertEquals(2, result.selectedFragments().size());
        var content = (EnquiryMerchantRepresentation.SubmissionContent)
                ((EnquiryMerchantRepresentationProjectionFragment) result.selectedFragments().getFirst()).representation();
        assertEquals("Original question", content.question());
        var subject = (EnquiryMerchantRepresentation.SubjectContext)
                ((EnquiryMerchantRepresentationProjectionFragment) result.selectedFragments().getLast()).representation();
        assertEquals("R7", subject.submissionTimeSubject().revisionIdentity());
        fixture.m1.actor = (scope, principal, privilege) -> false;
        assertTrue(new MerchantProjectionAssemblyService().assemble(read, serviceability(read, true),
                fixture.exposure(read, store)).selectedFragments().isEmpty());
        assertEquals(submission(true), store.submission(SCOPE, "E1").orElseThrow());
    }

    @Test
    void absent_optional_material_and_foreign_scope_remain_absent_in_durable_reads() {
        store.append(submission(false));
        var port = new AuthorityBackedEnquiryMerchantRepresentationProjectionReadPort(store);
        var read = port.observe(SCOPE, "E1", NOW).toBoundedRead(fixture.request);
        assertEquals(1, new MerchantProjectionAssemblyService().assemble(read, serviceability(read, true),
                fixture.exposure(read, store)).selectedFragments().size());
        var other = new MerchantScope("other");
        var foreign = port.observe(other, "E1", NOW).toBoundedRead(
                EnquiryMerchantRepresentationM2Test.request(other, grandrue.api.ApiSurfaceClass.MERCHANT_OPERATIONAL));
        assertTrue(foreign.fragments().isEmpty());
        assertFalse(foreign.sourceEvidence().iterator().next().isCurrent());
    }

    private static String env(String name) {
        var value = System.getenv(name);
        if (value == null || value.isBlank()) throw new IllegalStateException("Missing " + name);
        return value;
    }
}
