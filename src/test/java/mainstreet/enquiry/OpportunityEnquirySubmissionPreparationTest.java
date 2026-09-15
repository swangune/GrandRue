package mainstreet.enquiry;

import mainstreet.application.MerchantScope;
import mainstreet.publication.*;
import mainstreet.semantic.compiler.ConfigurationCompiler;
import mainstreet.semantic.configuration.*;
import mainstreet.semantic.registry.*;
import mainstreet.surface.*;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OpportunityEnquirySubmissionPreparationTest {
    static final MerchantScope SCOPE = new MerchantScope("merchant-a");
    static final Instant NOW = Instant.parse("2026-09-05T20:00:00Z");
    static final String RELEASE = "semantic-1";

    @Test
    void exact_published_subject_is_revalidated_and_captured_model_owns_provenance() {
        var f = new Fixture();
        var result = f.preparation().prepare(f.intent(true));
        assertEquals(f.intent(true), EnquirySubmissionIntent.from(result));
        assertEquals(new EnquirySemanticContext(RELEASE, "configuration-1", 1), result.semanticContext());
        assertEquals(NOW, result.submittedAt());
        assertEquals(1, f.preparations.get());
        verify(f.lock).lockCurrent(SCOPE, "O1");
    }

    @Test
    void general_enquiry_requires_active_interaction_but_does_not_require_a_subject_source() {
        var f = new Fixture();
        f.definition = Optional.empty();
        assertTrue(f.preparation().prepare(f.intent(false)).subjectRevision().isEmpty());
        verifyNoInteractions(f.lock);
    }

    @Test
    void missing_or_foreign_active_scope_or_release_fails_closed() {
        var f = new Fixture();
        when(f.activation.current(SCOPE.merchantIdentifier())).thenReturn(Optional.empty());
        assertThrows(EnquirySubmissionRevalidationException.class, () -> f.preparation().prepare(f.intent(false)));
        assertEquals(0, f.preparations.get());
        var foreign = new EnquirySubmissionIntent(new MerchantScope("other"), "Question",
                f.intent(false).contact(), Optional.empty(), Optional.empty());
        assertThrows(EnquirySubmissionRevalidationException.class, () -> f.preparation().prepare(foreign));
    }

    @Test
    void absent_interaction_or_participation_is_not_inferred_from_exposure() {
        var f = new Fixture();
        when(f.activation.current(SCOPE.merchantIdentifier())).thenReturn(Optional.of(f.release(false)));
        assertThrows(EnquirySubmissionRevalidationException.class, () -> f.preparation().prepare(f.intent(true)));
        when(f.activation.current(SCOPE.merchantIdentifier())).thenReturn(Optional.of(f.release(true)));
        f.definition = Optional.empty();
        assertThrows(EnquirySubmissionRevalidationException.class, () -> f.preparation().prepare(f.intent(true)));
        assertEquals(0, f.preparations.get());
    }

    @Test
    void forged_owner_type_or_revision_never_downgrades_to_general() {
        var f = new Fixture();
        for (var reference : List.of(
                new EnquiryRevisionProvenance(new OwnedOperationalObjectTypeReference("other", "opportunity"), "O1", "R7"),
                new EnquiryRevisionProvenance(new OwnedOperationalObjectTypeReference("publication", "other"), "O1", "R7"),
                new EnquiryRevisionProvenance(new OwnedOperationalObjectTypeReference("publication", "opportunity"), "O1", "R6"))) {
            var intent = new EnquirySubmissionIntent(SCOPE, "Question", f.intent(true).contact(),
                    Optional.of(reference), Optional.empty());
            assertThrows(EnquirySubmissionRevalidationException.class, () -> f.preparation().prepare(intent));
        }
        assertEquals(0, f.preparations.get());
    }

    @Test
    void missing_draft_withdrawn_and_republished_subjects_are_rejected() {
        var f = new Fixture();
        for (var state : List.of(Optional.<OpportunityPublicationState>empty(),
                Optional.of(OpportunityPublicationState.draft(SCOPE, "O1", "R7")),
                Optional.of(f.state.withdraw("R8")),
                Optional.of(new OpportunityPublicationState(SCOPE, "O1", "R8",
                        PublicationLifecycle.PUBLISHED, Optional.of("R8"))))) {
            when(f.lock.lockCurrent(SCOPE, "O1")).thenReturn(state);
            assertThrows(EnquirySubmissionRevalidationException.class, () -> f.preparation().prepare(f.intent(true)));
        }
        assertEquals(0, f.preparations.get());
    }

    @Test
    void publication_window_rechecks_current_time_including_exclusive_upper_boundary() {
        var f = new Fixture();
        f.clock = Clock.fixed(NOW.minusSeconds(1), ZoneOffset.UTC);
        assertThrows(EnquirySubmissionRevalidationException.class, () -> f.preparation().prepare(f.intent(true)));
        f.clock = Clock.fixed(NOW.plusSeconds(60), ZoneOffset.UTC);
        assertThrows(EnquirySubmissionRevalidationException.class, () -> f.preparation().prepare(f.intent(true)));
        assertEquals(0, f.preparations.get());
    }

    @Test
    void wrong_authority_affinity_and_unsatisfied_enquiry_requirements_fail_closed() {
        var f = new Fixture();
        when(f.lock.lockCurrent(SCOPE, "O1")).thenReturn(Optional.of(
                new OpportunityPublicationState(new MerchantScope("other"), "O1", "R7",
                        PublicationLifecycle.PUBLISHED, Optional.of("R7"))));
        assertThrows(EnquirySubmissionRevalidationException.class, () -> f.preparation().prepare(f.intent(true)));
        when(f.lock.lockCurrent(SCOPE, "O1")).thenReturn(Optional.of(f.state));
        f.additional = intent -> { throw new IllegalStateException("Unresolved Enquiry requirement"); };
        assertThrows(IllegalStateException.class, () -> f.preparation().prepare(f.intent(true)));
    }

    @Test
    void downstream_preparation_cannot_change_supplied_subject_or_customer_meaning() {
        var f = new Fixture();
        f.additional = intent -> f.prepared(f.intent(false));
        assertThrows(EnquirySubmissionRevalidationException.class, () -> f.preparation().prepare(f.intent(true)));
    }

    @Test
    void unsupported_semantic_release_and_late_window_expiry_are_rejected() {
        var f = new Fixture();
        var unsupported = new OpportunityEnquirySubmissionPreparation(SCOPE, f.activation,
                new SemanticRegistrySnapshot("other-release", Set.of()), f.definition,
                f.publication, f.lock, f.additional, f.clock);
        assertThrows(EnquirySubmissionRevalidationException.class, () -> unsupported.prepare(f.intent(true)));
        var advancing = mock(Clock.class);
        when(advancing.instant()).thenReturn(NOW, NOW.plusSeconds(60));
        f.clock = advancing;
        assertThrows(EnquirySubmissionRevalidationException.class, () -> f.preparation().prepare(f.intent(true)));
        assertEquals(1, f.preparations.get());
    }

    @Test
    void additional_contextual_interaction_gate_is_not_treated_as_satisfied() {
        var f = new Fixture();
        var gated = new StaticSurfaceContribution(new SurfaceContributionIdentity("enquiry", "send-enquiry"),
                SurfaceAudience.PUBLIC, SurfaceContributionKind.PUBLIC_INTERACTION, Optional.empty(),
                Set.of(), Set.of("send-enquiry"), Set.of(), SurfaceEligibilityContract.activeOnly("private-access"));
        var active = new ActiveRelease(new ConfigurationRelease("active-gated", f.configuration,
                ResolvedConfigurationPackage.currentFoundation(f.configuration, f.model,
                        new StaticSurfaceContributionCatalogue(List.of(gated)),
                        new ResolvedConfigurationProvenance("compiler", NOW))));
        when(f.activation.current(SCOPE.merchantIdentifier())).thenReturn(Optional.of(active));
        assertThrows(EnquirySubmissionRevalidationException.class, () -> f.preparation().prepare(f.intent(false)));
        assertEquals(0, f.preparations.get());
    }

    static final class Fixture {
        final SemanticRegistrySnapshot registry = new SemanticRegistrySnapshot(RELEASE, Set.of(
                new RegisteredCapability("publication", List.of(new OwnedOperationalObjectDefinition(
                        "opportunity", Set.of(), (String) null)), List.of()),
                new RegisteredCapability("enquiry", List.of(new OwnedOperationalObjectDefinition(
                        "enquiry", Set.of(), (String) null)), List.of(OwnedOperationDefinition.creation(
                        "send-enquiry", "enquiry", null, "enquiry-submitted", "submit-enquiry")))));
        final MerchantConfiguration configuration = new MerchantConfiguration(
                SCOPE.merchantIdentifier(), "configuration-1", 1, RELEASE,
                Set.of("publication", "enquiry"), Set.of(), Optional.empty());
        final mainstreet.semantic.executable.ExecutableMerchantModel model = new ConfigurationCompiler(
                version -> version.equals(RELEASE) ? Optional.of(registry) : Optional.empty()).compile(configuration);
        final ConfigurationReleaseActivation activation = mock(ConfigurationReleaseActivation.class);
        final OpportunityPublicationStateAuthority publication = mock(OpportunityPublicationStateAuthority.class);
        final OpportunityPublicationSubmissionLock lock = mock(OpportunityPublicationSubmissionLock.class);
        final OpportunityPublicationState state = new OpportunityPublicationState(
                SCOPE, "O1", "R8", PublicationLifecycle.PUBLISHED, Optional.of("R7"));
        Optional<OpportunityEnquiryParticipationDefinition> definition =
                Optional.of(OpportunityEnquiryParticipationDefinition.forRelease(registry));
        Clock clock = Clock.fixed(NOW, ZoneOffset.UTC);
        final AtomicInteger preparations = new AtomicInteger();
        EnquirySubmissionPreparation additional = intent -> {
            preparations.incrementAndGet();
            return prepared(intent);
        };

        Fixture() {
            when(activation.current(SCOPE.merchantIdentifier())).thenReturn(Optional.of(release(true)));
            when(lock.lockCurrent(SCOPE, "O1")).thenReturn(Optional.of(state));
            when(publication.current(SCOPE, "O1")).thenReturn(Optional.of(state));
            when(publication.revision(SCOPE, "O1", "R7")).thenReturn(Optional.of(
                    new OpportunityPublicationMaterialRevision(SCOPE, "O1", "R7", "Title",
                            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), List.of(),
                            Optional.empty(), Optional.empty(), Optional.of(new OpportunityExactInstantBoundary(NOW)),
                            Optional.of(new OpportunityExactInstantBoundary(NOW.plusSeconds(60))))));
        }

        ActiveRelease release(boolean interaction) {
            var catalogue = interaction ? new StaticSurfaceContributionCatalogue(List.of(
                    new StaticSurfaceContribution(new SurfaceContributionIdentity("enquiry", "send-enquiry"),
                            SurfaceAudience.PUBLIC, SurfaceContributionKind.PUBLIC_INTERACTION,
                            Optional.empty(), Set.of(), Set.of("send-enquiry"))))
                    : StaticSurfaceContributionCatalogue.empty();
            return new ActiveRelease(new ConfigurationRelease("active-1", configuration,
                    ResolvedConfigurationPackage.currentFoundation(configuration, model, catalogue,
                            new ResolvedConfigurationProvenance("compiler", NOW))));
        }

        OpportunityEnquirySubmissionPreparation preparation() {
            return new OpportunityEnquirySubmissionPreparation(SCOPE, activation, registry,
                    definition, publication, lock, additional, clock);
        }

        EnquirySubmissionIntent intent(boolean subject) {
            return new EnquirySubmissionIntent(SCOPE, "Question",
                    new EnquirySubmittedContact(Optional.empty(), Optional.empty(), Optional.empty()),
                    subject ? Optional.of(new EnquiryRevisionProvenance(
                            new OwnedOperationalObjectTypeReference("publication", "opportunity"), "O1", "R7"))
                            : Optional.empty(), Optional.empty());
        }

        EnquirySubmission prepared(EnquirySubmissionIntent intent) {
            return new EnquirySubmission(intent.merchantScope(), "E1", NOW.minusSeconds(10),
                    intent.question(), intent.contact(), new EnquirySemanticContext("untrusted", "old-model", 0),
                    intent.subjectRevision(), intent.customerContextIdentity());
        }
    }
}
