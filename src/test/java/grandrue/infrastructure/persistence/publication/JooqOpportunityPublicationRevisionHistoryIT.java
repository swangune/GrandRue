package grandrue.infrastructure.persistence.publication;

import grandrue.application.MerchantScope;
import grandrue.publication.OpportunityCalendarDateBoundary;
import grandrue.publication.OpportunityExactInstantBoundary;
import grandrue.publication.OpportunityExternalLink;
import grandrue.publication.OpportunityExternalLinkRole;
import grandrue.publication.OpportunityPublicationHistoryEntry;
import grandrue.publication.OpportunityPublicationMaterialRevision;
import grandrue.publication.OpportunityPublicationState;
import grandrue.publication.PublicationHistoryOperationKind;
import grandrue.publication.PublicationRevisionConflictException;
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
import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JooqOpportunityPublicationRevisionHistoryIT {

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
    void typed_material_revisions_round_trip_exact_presence_temporal_and_link_evidence() {
        JooqOpportunityPublicationStateAuthority authority = authority();
        OpportunityPublicationState first = draft("merchant-acme", "opp-1", "R1");
        OpportunityPublicationMaterialRevision firstMaterial = minimalMaterial(
                first,
                "Commonwealth Scholarship"
        );
        authority.establish(first, firstMaterial);

        OpportunityPublicationState second = first.revise("R1", "R2");
        OpportunityPublicationMaterialRevision secondMaterial = new OpportunityPublicationMaterialRevision(
                second.merchantScope(),
                second.opportunityIdentity(),
                second.currentRevisionIdentity(),
                "Commonwealth Scholarship",
                Optional.of("Postgraduate funding opportunity"),
                Optional.of("Eligible postgraduate applicants"),
                Optional.of("External Provider"),
                Optional.of("Official source"),
                List.of(
                        new OpportunityExternalLink(
                                OpportunityExternalLinkRole.OFFICIAL_APPLICATION,
                                URI.create("https://example.test/apply"),
                                Optional.of("Apply")
                        ),
                        new OpportunityExternalLink(
                                OpportunityExternalLinkRole.SOURCE,
                                URI.create("https://example.test/source"),
                                Optional.empty()
                        )
                ),
                Optional.of(new OpportunityCalendarDateBoundary(
                        LocalDate.of(2026, 9, 1),
                        ZoneId.of("Europe/London")
                )),
                Optional.of(new OpportunityExactInstantBoundary(
                        Instant.parse("2026-10-31T23:00:00Z")
                )),
                Optional.of(new OpportunityExactInstantBoundary(
                        Instant.parse("2026-08-01T08:00:00Z")
                )),
                Optional.of(new OpportunityCalendarDateBoundary(
                        LocalDate.of(2026, 10, 31),
                        ZoneId.of("Europe/London")
                ))
        );
        authority.compareAndSet(first, second, Optional.of(secondMaterial));

        assertEquals(firstMaterial, authority.revision(
                first.merchantScope(), first.opportunityIdentity(), "R1"
        ).orElseThrow());
        assertEquals(secondMaterial, authority.revision(
                second.merchantScope(), second.opportunityIdentity(), "R2"
        ).orElseThrow());
        assertEquals(second, authority.current(
                second.merchantScope(), second.opportunityIdentity()
        ).orElseThrow());
    }

    @Test
    void draft_establishment_creates_material_but_no_publication_history() {
        JooqOpportunityPublicationStateAuthority authority = authority();
        OpportunityPublicationState draft = draft("merchant-acme", "opp-1", "R1");
        OpportunityPublicationMaterialRevision material = minimalMaterial(draft, "Opportunity");

        authority.establish(draft, material);

        assertEquals(material, authority.revision(
                draft.merchantScope(), draft.opportunityIdentity(), "R1"
        ).orElseThrow());
        assertTrue(authority.publicationHistory(
                draft.merchantScope(), draft.opportunityIdentity()
        ).isEmpty());
    }

    @Test
    void publication_history_preserves_publish_new_revision_withdraw_and_republish() {
        JooqOpportunityPublicationStateAuthority authority = authority();
        OpportunityPublicationState draft = draft("merchant-acme", "opp-1", "R1");
        authority.establish(draft, minimalMaterial(draft, "Scholarship"));

        OpportunityPublicationState firstPublished = draft.publish("R1");
        authority.compareAndSet(draft, firstPublished, Optional.empty());

        OpportunityPublicationState revised = firstPublished.revise("R1", "R2");
        authority.compareAndSet(
                firstPublished,
                revised,
                Optional.of(deadlineMaterial(revised, "Scholarship", LocalDate.of(2026, 11, 15)))
        );
        assertEquals(1, authority.publicationHistory(
                revised.merchantScope(), revised.opportunityIdentity()
        ).size());

        OpportunityPublicationState secondPublished = revised.publish("R2");
        authority.compareAndSet(revised, secondPublished, Optional.empty());

        OpportunityPublicationState withdrawn = secondPublished.withdraw("R2");
        authority.compareAndSet(secondPublished, withdrawn, Optional.empty());

        OpportunityPublicationState withdrawnRevision = withdrawn.revise("R2", "R3");
        authority.compareAndSet(
                withdrawn,
                withdrawnRevision,
                Optional.of(deadlineMaterial(
                        withdrawnRevision,
                        "Scholarship 2027",
                        LocalDate.of(2027, 10, 31)
                ))
        );

        OpportunityPublicationState republished = withdrawnRevision.republish("R3");
        authority.compareAndSet(withdrawnRevision, republished, Optional.empty());

        List<OpportunityPublicationHistoryEntry> history = authority.publicationHistory(
                republished.merchantScope(), republished.opportunityIdentity()
        );

        assertEquals(
                List.of(
                        PublicationHistoryOperationKind.PUBLISH,
                        PublicationHistoryOperationKind.PUBLISH,
                        PublicationHistoryOperationKind.WITHDRAW,
                        PublicationHistoryOperationKind.REPUBLISH
                ),
                history.stream().map(OpportunityPublicationHistoryEntry::operationKind).toList()
        );
        assertEquals("R1", history.get(0).currentRevisionIdentity());
        assertEquals("R1", history.get(0).publishedRevisionIdentity().orElseThrow());
        assertEquals("R2", history.get(1).currentRevisionIdentity());
        assertEquals("R2", history.get(1).publishedRevisionIdentity().orElseThrow());
        assertEquals("R2", history.get(2).currentRevisionIdentity());
        assertEquals("R2", history.get(2).publishedRevisionIdentity().orElseThrow());
        assertEquals("R3", history.get(3).currentRevisionIdentity());
        assertEquals("R3", history.get(3).publishedRevisionIdentity().orElseThrow());
    }

    @Test
    void withdrawal_preserves_previously_published_revision_when_current_material_differs() {
        JooqOpportunityPublicationStateAuthority authority = authority();
        OpportunityPublicationState draft = draft("merchant-acme", "opp-1", "R1");
        authority.establish(draft, minimalMaterial(draft, "Scholarship"));
        OpportunityPublicationState published = draft.publish("R1");
        authority.compareAndSet(draft, published, Optional.empty());

        OpportunityPublicationState revised = published.revise("R1", "R2");
        authority.compareAndSet(
                published,
                revised,
                Optional.of(minimalMaterial(revised, "Corrected Scholarship"))
        );
        OpportunityPublicationState withdrawn = revised.withdraw("R2");
        authority.compareAndSet(revised, withdrawn, Optional.empty());

        OpportunityPublicationHistoryEntry withdrawal = authority.publicationHistory(
                withdrawn.merchantScope(), withdrawn.opportunityIdentity()
        ).getLast();
        assertEquals(PublicationHistoryOperationKind.WITHDRAW, withdrawal.operationKind());
        assertEquals("R2", withdrawal.currentRevisionIdentity());
        assertEquals("R1", withdrawal.publishedRevisionIdentity().orElseThrow());
    }

    @Test
    void stale_revision_failure_rolls_back_candidate_material() {
        JooqOpportunityPublicationStateAuthority authority = authority();
        OpportunityPublicationState first = draft("merchant-acme", "opp-1", "R1");
        authority.establish(first, minimalMaterial(first, "Scholarship"));

        OpportunityPublicationState second = first.revise("R1", "R2");
        authority.compareAndSet(
                first,
                second,
                Optional.of(deadlineMaterial(second, "Scholarship", LocalDate.of(2026, 11, 15)))
        );

        OpportunityPublicationState stale = first.revise("R1", "R-stale");
        assertThrows(
                PublicationRevisionConflictException.class,
                () -> authority.compareAndSet(
                        first,
                        stale,
                        Optional.of(minimalMaterial(stale, "Stale"))
                )
        );

        assertTrue(authority.revision(
                first.merchantScope(), first.opportunityIdentity(), "R-stale"
        ).isEmpty());
        assertEquals(second, authority.current(
                second.merchantScope(), second.opportunityIdentity()
        ).orElseThrow());
        assertEquals(2, dsl.fetchCount(
                DSL.table(DSL.name("opportunity_publication_revision_material"))
        ));
    }

    @Test
    void typed_revision_and_publication_history_remain_merchant_scoped() {
        JooqOpportunityPublicationStateAuthority authority = authority();
        OpportunityPublicationState acme = draft("merchant-acme", "opp-1", "R1");
        OpportunityPublicationState beta = draft("merchant-beta", "opp-1", "R1");
        OpportunityPublicationMaterialRevision acmeMaterial = minimalMaterial(
                acme, "Acme Scholarship"
        );
        OpportunityPublicationMaterialRevision betaMaterial = minimalMaterial(
                beta, "Beta Scholarship"
        );
        authority.establish(acme, acmeMaterial);
        authority.establish(beta, betaMaterial);
        authority.compareAndSet(acme, acme.publish("R1"), Optional.empty());

        assertEquals(acmeMaterial, authority.revision(
                acme.merchantScope(), acme.opportunityIdentity(), "R1"
        ).orElseThrow());
        assertEquals(betaMaterial, authority.revision(
                beta.merchantScope(), beta.opportunityIdentity(), "R1"
        ).orElseThrow());
        assertEquals(1, authority.publicationHistory(
                acme.merchantScope(), acme.opportunityIdentity()
        ).size());
        assertTrue(authority.publicationHistory(
                beta.merchantScope(), beta.opportunityIdentity()
        ).isEmpty());
    }

    @Test
    void concurrent_revisions_from_one_expected_state_persist_exactly_one_new_material_revision()
            throws Exception {
        JooqOpportunityPublicationStateAuthority authority = authority();
        OpportunityPublicationState first = draft("merchant-acme", "opp-1", "R1");
        authority.establish(first, minimalMaterial(first, "Scholarship"));

        OpportunityPublicationState nextA = first.revise("R1", "R2-A");
        OpportunityPublicationState nextB = first.revise("R1", "R2-B");
        OpportunityPublicationMaterialRevision materialA = minimalMaterial(nextA, "Scholarship A");
        OpportunityPublicationMaterialRevision materialB = minimalMaterial(nextB, "Scholarship B");
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<Object> a = executor.submit(() -> advanceAfterBarrier(
                    ready, start, first, nextA, materialA
            ));
            Future<Object> b = executor.submit(() -> advanceAfterBarrier(
                    ready, start, first, nextB, materialB
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
        }

        OpportunityPublicationState current = authority.current(
                first.merchantScope(), first.opportunityIdentity()
        ).orElseThrow();
        String loser = current.currentRevisionIdentity().equals("R2-A") ? "R2-B" : "R2-A";
        assertTrue(authority.revision(
                first.merchantScope(), first.opportunityIdentity(), current.currentRevisionIdentity()
        ).isPresent());
        assertTrue(authority.revision(
                first.merchantScope(), first.opportunityIdentity(), loser
        ).isEmpty());
        assertEquals(2, dsl.fetchCount(
                DSL.table(DSL.name("opportunity_publication_revision_material"))
        ));
    }

    @Test
    void persistence_requires_new_material_exactly_when_revision_identity_changes() {
        JooqOpportunityPublicationStateAuthority authority = authority();
        OpportunityPublicationState first = draft("merchant-acme", "opp-1", "R1");
        authority.establish(first, minimalMaterial(first, "Scholarship"));

        OpportunityPublicationState second = first.revise("R1", "R2");
        assertThrows(
                IllegalArgumentException.class,
                () -> authority.compareAndSet(first, second, Optional.empty())
        );

        OpportunityPublicationState published = first.publish("R1");
        assertThrows(
                IllegalArgumentException.class,
                () -> authority.compareAndSet(
                        first,
                        published,
                        Optional.of(minimalMaterial(first, "Scholarship"))
                )
        );

        OpportunityPublicationMaterialRevision wrongAffinity = minimalMaterial(
                draft("merchant-other", "opp-1", "R2"),
                "Wrong"
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> authority.compareAndSet(first, second, Optional.of(wrongAffinity))
        );
    }

    private Object advanceAfterBarrier(
            CountDownLatch ready,
            CountDownLatch start,
            OpportunityPublicationState expectedState,
            OpportunityPublicationState nextState,
            OpportunityPublicationMaterialRevision material
    ) throws InterruptedException {
        ready.countDown();
        start.await();
        try {
            return authority().compareAndSet(
                    expectedState,
                    nextState,
                    Optional.of(material)
            );
        } catch (PublicationRevisionConflictException conflict) {
            return conflict;
        }
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
                new MerchantScope(merchant), opportunity, revision
        );
    }

    private static OpportunityPublicationMaterialRevision minimalMaterial(
            OpportunityPublicationState state,
            String title
    ) {
        return new OpportunityPublicationMaterialRevision(
                state.merchantScope(),
                state.opportunityIdentity(),
                state.currentRevisionIdentity(),
                title,
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

    private static OpportunityPublicationMaterialRevision deadlineMaterial(
            OpportunityPublicationState state,
            String title,
            LocalDate deadline
    ) {
        return new OpportunityPublicationMaterialRevision(
                state.merchantScope(),
                state.opportunityIdentity(),
                state.currentRevisionIdentity(),
                title,
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                List.of(),
                Optional.empty(),
                Optional.of(new OpportunityCalendarDateBoundary(
                        deadline,
                        ZoneId.of("Europe/London")
                )),
                Optional.empty(),
                Optional.empty()
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
