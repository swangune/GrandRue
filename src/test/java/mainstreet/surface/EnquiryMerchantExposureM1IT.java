package mainstreet.surface;

import mainstreet.application.MerchantScope;
import mainstreet.enquiry.EnquiryMerchantExposureCandidateSource;
import mainstreet.enquiry.EnquiryMerchantExposureContractPortfolio;
import mainstreet.enquiry.EnquiryMerchantExposureReferences;
import mainstreet.enquiry.EnquiryMerchantExposureRequirementEvaluator;
import mainstreet.infrastructure.persistence.enquiry.JooqEnquirySubmissionStore;
import org.flywaydb.core.Flyway;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import java.util.List;

import static mainstreet.surface.EnquiryMerchantExposureM1Test.*;
import static org.junit.jupiter.api.Assertions.*;

class EnquiryMerchantExposureM1IT {
    private JooqEnquirySubmissionStore store;
    private Fixture fixture;

    @BeforeEach
    void setUp() {
        var source = new DriverManagerDataSource(env("MAINSTREET_TEST_POSTGRES_URL"),
                env("MAINSTREET_TEST_POSTGRES_USER"), env("MAINSTREET_TEST_POSTGRES_PASSWORD"));
        Flyway.configure().dataSource(source).locations("classpath:db/migration").load().migrate();
        var dsl = DSL.using(new TransactionAwareDataSourceProxy(source), SQLDialect.POSTGRES);
        dsl.execute("truncate table enquiry_submission_application_request, enquiry_submission");
        store = new JooqEnquirySubmissionStore(dsl);
        fixture = new Fixture();
    }

    @Test
    void durable_submission_yields_only_authorised_owner_candidates_and_revocation_is_current() {
        assertTrue(new EnquiryMerchantExposureCandidateSource(store).currentCandidates(SCOPE, "E1").isEmpty());
        store.append(submission(true));
        assertEquals(3, new EnquiryMerchantExposureCandidateSource(store).currentCandidates(SCOPE, "E1").size());
        assertEquals(3, resolve(SCOPE).size());
        fixture.authorised.set(false);
        assertTrue(resolve(SCOPE).isEmpty());
        assertEquals(submission(true), store.submission(SCOPE, "E1").orElseThrow());
    }

    @Test
    void even_authorised_other_merchant_cannot_expose_a_stored_foreign_enquiry() {
        store.append(submission(true));
        fixture.actor = (scope, principal, privilege) -> true;
        var other = new MerchantScope("other");
        assertTrue(new EnquiryMerchantExposureCandidateSource(store).currentCandidates(other, "E1").isEmpty());
        assertTrue(resolve(other).isEmpty());
        assertEquals(3, resolve(SCOPE).size());
    }

    private List<ResolvedExposedElement> resolve(MerchantScope scope) {
        var context = fixture.context(scope, SurfaceAudience.MERCHANT);
        return new ExposureResolver().resolve(context, fixture.admission().evaluate(context), fixture.candidates(),
                EnquiryMerchantExposureContractPortfolio.forRelease(RELEASE),
                new ExposureRequirementEvaluatorBindingSnapshot(RELEASE, List.of(
                        new ExposureRequirementEvaluatorBinding(EnquiryMerchantExposureReferences.OBSERVATION_REQUIREMENT,
                                new EnquiryMerchantExposureRequirementEvaluator(store, fixture.actor, fixture.privileges)))),
                new MerchantExposureChoiceEvaluatorBindingSnapshot(RELEASE, List.of()));
    }

    private static String env(String name) {
        var value = System.getenv(name);
        if (value == null || value.isBlank()) throw new IllegalStateException("Missing " + name);
        return value;
    }
}
