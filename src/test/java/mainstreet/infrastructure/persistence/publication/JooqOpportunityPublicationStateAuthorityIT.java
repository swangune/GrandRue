package mainstreet.infrastructure.persistence.publication;

import mainstreet.application.MerchantScope;
import mainstreet.publication.OpportunityPublicationMaterialRevision;
import mainstreet.publication.OpportunityPublicationState;
import mainstreet.publication.PublicationLifecycle;
import mainstreet.publication.PublicationRevisionConflictException;
import mainstreet.publication.PublicationStateConflictException;
import org.flywaydb.core.Flyway;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqOpportunityPublicationStateAuthorityIT {

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
    void draft_establishment_is_durable_and_isolated_by_merchant_scope() {
        JooqOpportunityPublicationStateAuthority authority = authority();
        OpportunityPublicationState acme = draft("merchant-acme", "opp-1", "R1");
        OpportunityPublicationState beta = draft("merchant-beta", "opp-1", "R1");

        assertEquals(acme, establish(authority, acme));
        assertEquals(beta, establish(authority, beta));
        assertEquals(acme, authority().current(
                new MerchantScope("merchant-acme"), "opp-1"
        ).orElseThrow());
        assertEquals(beta, authority().current(
                new MerchantScope("merchant-beta"), "opp-1"
        ).orElseThrow());
        assertEquals(2, dsl.fetchCount(DSL.table(DSL.name(
                "opportunity_publication_current"
        ))));
        assertEquals(2, dsl.fetchCount(DSL.table(DSL.name(
                "opportunity_publication_revision_identity"
        ))));
    }

    @Test
    void revision_advancement_preserves_prior_revision_identity_and_rejects_stale_currentness() {
        JooqOpportunityPublicationStateAuthority authority = authority();
        OpportunityPublicationState first = draft("merchant-acme", "opp-1", "R1");
        establish(authority, first);

        OpportunityPublicationState second = first.revise("R1", "R2");
        assertEquals(second, advance(authority, first, second));

        OpportunityPublicationState stale = first.revise("R1", "R-stale");
        assertThrows(
                PublicationRevisionConflictException.class,
                () -> advance(authority, first, stale)
        );
        assertEquals(second, authority.current(
                new MerchantScope("merchant-acme"), "opp-1"
        ).orElseThrow());
        assertEquals(
                List.of("R1", "R2"),
                dsl.select(DSL.field(DSL.name("revision_identity"), String.class))
                        .from(DSL.table(DSL.name(
                                "opportunity_publication_revision_identity"
                        )))
                        .where(DSL.field(
                                DSL.name("merchant_identifier"),
                                String.class
                        ).eq("merchant-acme"))
                        .and(DSL.field(
                                DSL.name("opportunity_identity"),
                                String.class
                        ).eq("opp-1"))
                        .orderBy(DSL.field(DSL.name("revision_identity")))
                        .fetch(DSL.field(
                                DSL.name("revision_identity"),
                                String.class
                        ))
        );
    }

    @Test
    void lifecycle_and_last_published_revision_round_trip_without_collapsing_current_revision() {
        JooqOpportunityPublicationStateAuthority authority = authority();
        OpportunityPublicationState draft = draft("merchant-acme", "opp-1", "R1");
        establish(authority, draft);

        OpportunityPublicationState published = draft.publish("R1");
        advance(authority, draft, published);
        OpportunityPublicationState revised = published.revise("R1", "R2");
        advance(authority, published, revised);
        OpportunityPublicationState withdrawn = revised.withdraw("R2");
        advance(authority, revised, withdrawn);

        OpportunityPublicationState reloaded = authority().current(
                new MerchantScope("merchant-acme"), "opp-1"
        ).orElseThrow();
        assertEquals(PublicationLifecycle.WITHDRAWN, reloaded.lifecycle());
        assertEquals("R2", reloaded.currentRevisionIdentity());
        assertEquals("R1", reloaded.publishedRevisionIdentity().orElseThrow());
        assertEquals(2, revisionIdentityCount("merchant-acme", "opp-1"));
    }

    @Test
    void stale_same_revision_lifecycle_state_cannot_overwrite_withdrawal() {
        JooqOpportunityPublicationStateAuthority authority = authority();
        OpportunityPublicationState draft = draft("merchant-acme", "opp-1", "R1");
        establish(authority, draft);
        OpportunityPublicationState published = draft.publish("R1");
        advance(authority, draft, published);

        OpportunityPublicationState stalePublish = published.publish("R1");
        OpportunityPublicationState withdrawn = published.withdraw("R1");
        advance(authority, published, withdrawn);

        assertThrows(
                PublicationStateConflictException.class,
                () -> advance(authority, published, stalePublish)
        );
        assertEquals(withdrawn, authority.current(
                new MerchantScope("merchant-acme"), "opp-1"
        ).orElseThrow());
    }

    @Test
    void another_merchant_cannot_read_or_advance_the_same_opportunity_identity() {
        JooqOpportunityPublicationStateAuthority authority = authority();
        OpportunityPublicationState acme = draft("merchant-acme", "opp-1", "R1");
        establish(authority, acme);

        assertTrue(authority.current(
                new MerchantScope("merchant-beta"), "opp-1"
        ).isEmpty());
        OpportunityPublicationState betaCurrent = draft(
                "merchant-beta", "opp-1", "R1"
        );
        OpportunityPublicationState betaRevision = betaCurrent.revise("R1", "R2");
        assertThrows(
                IllegalStateException.class,
                () -> advance(authority, betaCurrent, betaRevision)
        );
        assertEquals(acme, authority.current(
                new MerchantScope("merchant-acme"), "opp-1"
        ).orElseThrow());
    }

    @Test
    void previously_recorded_revision_identity_cannot_be_reused_as_a_new_revision() {
        JooqOpportunityPublicationStateAuthority authority = authority();
        OpportunityPublicationState first = draft("merchant-acme", "opp-1", "R1");
        establish(authority, first);
        OpportunityPublicationState second = first.revise("R1", "R2");
        advance(authority, first, second);

        OpportunityPublicationState attemptedReuse = second.revise("R2", "R1");
        assertThrows(
                IllegalStateException.class,
                () -> advance(authority, second, attemptedReuse)
        );
        assertEquals(second, authority.current(
                new MerchantScope("merchant-acme"), "opp-1"
        ).orElseThrow());
        assertEquals(2, revisionIdentityCount("merchant-acme", "opp-1"));
    }

    @Test
    void concurrent_advances_from_one_expected_revision_allow_exactly_one_winner()
            throws Exception {
        JooqOpportunityPublicationStateAuthority authority = authority();
        OpportunityPublicationState first = draft("merchant-acme", "opp-1", "R1");
        establish(authority, first);
        OpportunityPublicationState nextA = first.revise("R1", "R2-A");
        OpportunityPublicationState nextB = first.revise("R1", "R2-B");
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<Object> a = executor.submit(() -> advanceAfterBarrier(
                    ready, start, first, nextA
            ));
            Future<Object> b = executor.submit(() -> advanceAfterBarrier(
                    ready, start, first, nextB
            ));
            ready.await();
            start.countDown();

            List<Object> outcomes = List.of(a.get(), b.get());
            assertEquals(1, outcomes.stream()
                    .filter(OpportunityPublicationState.class::isInstance)
                    .count());
            assertEquals(1, outcomes.stream()
                    .filter(PublicationRevisionConflictException.class::isInstance)
                    .count());
            assertInstanceOf(
                    OpportunityPublicationState.class,
                    outcomes.stream()
                            .filter(OpportunityPublicationState.class::isInstance)
                            .findFirst()
                            .orElseThrow()
            );
        }

        OpportunityPublicationState current = authority.current(
                new MerchantScope("merchant-acme"), "opp-1"
        ).orElseThrow();
        assertTrue(current.currentRevisionIdentity().equals("R2-A")
                || current.currentRevisionIdentity().equals("R2-B"));
        assertEquals(2, revisionIdentityCount("merchant-acme", "opp-1"));
    }

    private Object advanceAfterBarrier(
            CountDownLatch ready,
            CountDownLatch start,
            OpportunityPublicationState expectedState,
            OpportunityPublicationState nextState
    ) throws InterruptedException {
        ready.countDown();
        start.await();
        try {
            return advance(authority(), expectedState, nextState);
        } catch (PublicationRevisionConflictException conflict) {
            return conflict;
        }
    }

    private OpportunityPublicationState establish(
            JooqOpportunityPublicationStateAuthority authority,
            OpportunityPublicationState state
    ) {
        return authority.establish(state, material(state));
    }

    private OpportunityPublicationState advance(
            JooqOpportunityPublicationStateAuthority authority,
            OpportunityPublicationState expected,
            OpportunityPublicationState next
    ) {
        Optional<OpportunityPublicationMaterialRevision> material =
                expected.currentRevisionIdentity().equals(next.currentRevisionIdentity())
                        ? Optional.empty()
                        : Optional.of(material(next));
        return authority.compareAndSet(expected, next, material);
    }

    private OpportunityPublicationMaterialRevision material(
            OpportunityPublicationState state
    ) {
        return new OpportunityPublicationMaterialRevision(
                state.merchantScope(),
                state.opportunityIdentity(),
                state.currentRevisionIdentity(),
                state.currentRevisionIdentity(),
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

    private int revisionIdentityCount(String merchant, String opportunity) {
        return dsl.fetchCount(
                DSL.table(DSL.name("opportunity_publication_revision_identity")),
                DSL.field(DSL.name("merchant_identifier"), String.class)
                        .eq(merchant)
                        .and(DSL.field(
                                DSL.name("opportunity_identity"),
                                String.class
                        ).eq(opportunity))
        );
    }

    private JooqOpportunityPublicationStateAuthority authority() {
        return new JooqOpportunityPublicationStateAuthority(dsl, transactionManager);
    }

    private static OpportunityPublicationState draft(
            String merchant,
            String opportunity,
            String revision
    ) {
        return OpportunityPublicationState.draft(
                new MerchantScope(merchant),
                opportunity,
                revision
        );
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
