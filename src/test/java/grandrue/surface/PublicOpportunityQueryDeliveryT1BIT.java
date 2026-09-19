package grandrue.surface;

import grandrue.infrastructure.persistence.publication.JooqOpportunityPublicationStateAuthority;
import grandrue.publication.*;
import org.flywaydb.core.Flyway;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.*;

import java.util.Optional;

import static grandrue.surface.PublicOpportunityQueryResponseT1ATest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PublicOpportunityQueryDeliveryT1BIT {
    private JooqOpportunityPublicationStateAuthority owner;
    private OpportunityPublicationState published;

    @BeforeEach
    void setUp() {
        var source = new DriverManagerDataSource(env("GRANDRUE_TEST_POSTGRES_URL"),
                env("GRANDRUE_TEST_POSTGRES_USER"), env("GRANDRUE_TEST_POSTGRES_PASSWORD"));
        Flyway.configure().dataSource(source).locations("classpath:db/migration").load().migrate();
        var dsl = DSL.using(new TransactionAwareDataSourceProxy(source), SQLDialect.POSTGRES);
        dsl.execute("truncate table opportunity_publication_application_request, "
                + "opportunity_publication_lifecycle_history, opportunity_publication_revision_field_value, "
                + "opportunity_publication_revision_material, opportunity_publication_current, "
                + "opportunity_publication_revision_identity");
        owner = new JooqOpportunityPublicationStateAuthority(dsl, new DataSourceTransactionManager(source));
        var material = new Fixture().owner.revision(SCOPE, "O1", "secret-revision").orElseThrow();
        var draft = new OpportunityPublicationState(SCOPE, "O1", "secret-revision",
                PublicationLifecycle.DRAFT, Optional.empty());
        owner.establish(draft, material);
        published = draft.publish("secret-revision");
        owner.compareAndSet(draft, published, Optional.empty());
    }

    @Test
    void durable_publication_is_delivered_through_real_query_pipeline_without_mutation() throws Exception {
        var history = owner.publicationHistory(SCOPE, "O1");
        var mvc = new PublicOpportunityQueryDeliveryT1BTest.DeliveryFixture(owner).mvc();
        mvc.perform(get("/api/public/storefronts/shop/opportunities/O1"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.title").value("Published title"))
                .andExpect(header().string("Cache-Control", "no-store"));
        assertEquals(Optional.of(published), owner.current(SCOPE, "O1"));
        assertEquals(history, owner.publicationHistory(SCOPE, "O1"));
    }

    @Test
    void durable_withdrawn_and_foreign_opportunities_are_not_disclosed() throws Exception {
        var mvc = new PublicOpportunityQueryDeliveryT1BTest.DeliveryFixture(owner).mvc();
        var foreign = mvc.perform(get("/api/public/storefronts/other-shop/opportunities/O1"))
                .andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();
        owner.compareAndSet(published, published.withdraw("secret-revision"), Optional.empty());
        var withdrawn = mvc.perform(get("/api/public/storefronts/shop/opportunities/O1"))
                .andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();
        assertEquals(foreign, withdrawn);
        assertFalse(withdrawn.contains("Published title"));
    }

    private static String env(String name) {
        var value = System.getenv(name);
        if (value == null || value.isBlank()) throw new IllegalStateException("Missing " + name);
        return value;
    }
}
