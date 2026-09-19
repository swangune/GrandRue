package grandrue.publication;

import grandrue.application.MerchantScope;
import grandrue.semantic.registry.OwnedSchemaReference;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** MS-PROT-046 v1.3 executable contract for typed Opportunity material. */
class OpportunityPublicationV13ContractTest {

    @Test
    void opportunity_v1_material_is_typed_and_schema_affinity_is_fixed() {
        OpportunityCalendarDateBoundary opening = new OpportunityCalendarDateBoundary(
                LocalDate.of(2026, 9, 1),
                ZoneId.of("Europe/London")
        );
        OpportunityExactInstantBoundary deadline = new OpportunityExactInstantBoundary(
                Instant.parse("2026-10-31T23:00:00Z")
        );
        OpportunityExternalLink application = new OpportunityExternalLink(
                OpportunityExternalLinkRole.OFFICIAL_APPLICATION,
                URI.create("https://example.test/apply"),
                Optional.of("Apply")
        );

        OpportunityPublicationMaterialRevision revision = new OpportunityPublicationMaterialRevision(
                new MerchantScope("merchant-acme"),
                "opp-1",
                "R1",
                "Commonwealth Scholarship",
                Optional.of("Postgraduate funding opportunity"),
                Optional.of("Eligible postgraduate applicants"),
                Optional.of("External Provider"),
                Optional.of("Official source"),
                List.of(application),
                Optional.of(opening),
                Optional.of(deadline),
                Optional.empty(),
                Optional.of(new OpportunityCalendarDateBoundary(
                        LocalDate.of(2026, 10, 31),
                        ZoneId.of("Europe/London")
                ))
        );

        assertEquals(new OwnedSchemaReference("opportunity", 1), revision.schemaReference());
        assertEquals("Commonwealth Scholarship", revision.title());
        assertEquals(opening, revision.applicationsOpen().orElseThrow());
        assertEquals(deadline, revision.applicationDeadline().orElseThrow());
        assertEquals(application, revision.externalLinks().getFirst());
        assertTrue(revision.publishUntil().isPresent());
    }

    @Test
    void title_is_required_but_optional_material_remains_absent_without_fabrication() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new OpportunityPublicationMaterialRevision(
                        new MerchantScope("merchant-acme"),
                        "opp-1",
                        "R1",
                        "   ",
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        List.of(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()
                )
        );

        OpportunityPublicationMaterialRevision minimal = new OpportunityPublicationMaterialRevision(
                new MerchantScope("merchant-acme"),
                "opp-1",
                "R1",
                "Local vacancy",
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

        assertTrue(minimal.externalProviderName().isEmpty());
        assertTrue(minimal.sourceName().isEmpty());
        assertTrue(minimal.applicationDeadline().isEmpty());
    }

    @Test
    void external_links_are_role_qualified_and_require_absolute_uris() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new OpportunityExternalLink(
                        OpportunityExternalLinkRole.SOURCE,
                        URI.create("/relative"),
                        Optional.empty()
                )
        );

        OpportunityExternalLink first = new OpportunityExternalLink(
                OpportunityExternalLinkRole.SOURCE,
                URI.create("https://example.test/source-a"),
                Optional.empty()
        );
        OpportunityExternalLink second = new OpportunityExternalLink(
                OpportunityExternalLinkRole.SOURCE,
                URI.create("https://example.test/source-b"),
                Optional.of("Second source")
        );

        OpportunityPublicationMaterialRevision revision = new OpportunityPublicationMaterialRevision(
                new MerchantScope("merchant-acme"),
                "opp-1",
                "R1",
                "Opportunity",
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                List.of(first, second),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );

        assertEquals(List.of(first, second), revision.externalLinks());
    }

    @Test
    void calendar_date_boundary_retains_explicit_interpretation_zone() {
        OpportunityCalendarDateBoundary boundary = new OpportunityCalendarDateBoundary(
                LocalDate.of(2026, 10, 31),
                ZoneId.of("Europe/London")
        );

        assertEquals(LocalDate.of(2026, 10, 31), boundary.date());
        assertEquals(ZoneId.of("Europe/London"), boundary.zoneId());
        assertEquals(
                LocalDate.of(2026, 11, 1)
                        .atStartOfDay(ZoneId.of("Europe/London"))
                        .toInstant(),
                boundary.inclusiveUpperCutoff()
        );
    }
}
