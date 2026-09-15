package mainstreet.publication;

import mainstreet.application.MerchantScope;
import mainstreet.semantic.executable.ExecutableMerchantModel;
import mainstreet.semantic.executable.ExecutableObjectCreationEffect;
import mainstreet.semantic.executable.ExecutableOperationDefinition;
import mainstreet.semantic.executable.ExecutableOperationalObjectDefinition;
import mainstreet.semantic.executable.ExecutableOperationalObjectTypeIdentity;
import mainstreet.semantic.registry.OwnedOperationalObjectDefinition;
import mainstreet.semantic.registry.OwnedOperationDefinition;
import mainstreet.semantic.registry.RegisteredCapability;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import mainstreet.surface.*;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** MS-PROT-046 v1.2 §§16–18; MS-PROT-049 v1.4 §§3–5, 9, 13, 15. */
class OpportunityEnquiryParticipationSourceTest {
    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final String RELEASE = "semantic-registry-1.0";
    private static final SurfaceContributionIdentity ENQUIRY =
            new SurfaceContributionIdentity("enquiry", "send-enquiry");

    @Test
    void explicit_definition_establishes_exact_owner_qualified_participation() {
        var authority = authority(PublicationLifecycle.PUBLISHED);
        var definition = definition();
        var model = model(Set.of("publication", "enquiry"), true);
        var source = new OpportunityEnquiryParticipationSource(
                definition, model, authority, Set.of("opportunity-1"));

        var facts = source.currentParticipation(request(model, contribution()));

        assertEquals(Set.of(new PublicInteractionParticipationFact(
                MERCHANT, RELEASE, "model-a", 7, ENQUIRY, "send-enquiry",
                OpportunityPublicExposureReferences.candidate("opportunity-1"),
                new PublicInteractionParticipationSourceIdentity(
                        "publication", "opportunity-enquiry"), Optional.empty())), facts);
        assertEquals(definition.sourceIdentity(), source.registration().identity());
        assertSame(source, source.registration().source());
        verify(authority).current(MERCHANT, "opportunity-1");
        verifyNoMoreInteractions(authority);
    }

    @Test
    void missing_instance_and_other_publication_identifiers_produce_no_fact() {
        var authority = authority(PublicationLifecycle.PUBLISHED);
        when(authority.current(MERCHANT, "announcement-1")).thenReturn(Optional.empty());
        when(authority.current(MERCHANT, "missing")).thenReturn(Optional.empty());
        var model = model(Set.of("publication", "enquiry"), true);
        var source = new OpportunityEnquiryParticipationSource(
                definition(), model, authority,
                Set.of("opportunity-1", "announcement-1", "missing"));
        var facts = source.currentParticipation(request(model, contribution()));
        assertEquals(Set.of(OpportunityPublicExposureReferences.candidate("opportunity-1")),
                facts.stream().map(PublicInteractionParticipationFact::subject)
                        .collect(java.util.stream.Collectors.toSet()));
    }

    @Test
    void participation_is_not_a_public_visibility_or_actionability_decision() {
        var model = model(Set.of("publication", "enquiry"), true);
        for (var lifecycle : PublicationLifecycle.values()) {
            var authority = authority(lifecycle);
            var source = new OpportunityEnquiryParticipationSource(
                    definition(), model, authority, Set.of("opportunity-1"));
            assertEquals(1, source.currentParticipation(request(model, contribution())).size());
            // Only existence/identity is read. No revision, deadline, window or business values.
            verify(authority).current(MERCHANT, "opportunity-1");
            verifyNoMoreInteractions(authority);
        }
    }

    @Test
    void inactive_capability_or_absent_resolved_operation_withholds_without_owner_reads() {
        for (var model : List.of(
                model(Set.of("publication"), true),
                model(Set.of("enquiry"), true),
                model(Set.of("publication", "enquiry"), false))) {
            var authority = mock(OpportunityPublicationStateAuthority.class);
            var source = new OpportunityEnquiryParticipationSource(
                    definition(), model, authority, Set.of("opportunity-1"));
            assertTrue(source.currentParticipation(request(model, contribution())).isEmpty());
            verifyNoInteractions(authority);
        }
    }

    @Test
    void registered_family_must_also_be_applicable_in_the_resolved_model() {
        var original = model(Set.of("publication", "enquiry"), true);
        var withoutOpportunity = new ExecutableMerchantModel(MERCHANT.merchantIdentifier(),
                "model-a", 7, RELEASE, original.capabilityIdentifiers(),
                List.of(), original.operations());
        var authority = authority(PublicationLifecycle.PUBLISHED);
        var source = new OpportunityEnquiryParticipationSource(
                definition(), withoutOpportunity, authority, Set.of("opportunity-1"));
        assertTrue(source.currentParticipation(request(withoutOpportunity, contribution())).isEmpty());
        verifyNoInteractions(authority);
    }

    @Test
    void unrelated_contribution_operation_or_audience_never_infers_participation() {
        var model = model(Set.of("publication", "enquiry"), true);
        var authority = mock(OpportunityPublicationStateAuthority.class);
        var source = new OpportunityEnquiryParticipationSource(
                definition(), model, authority, Set.of("opportunity-1"));
        for (var contribution : List.of(
                contribution(new SurfaceContributionIdentity("other", "send-enquiry"),
                        SurfaceAudience.PUBLIC, Set.of("send-enquiry")),
                contribution(new SurfaceContributionIdentity("enquiry", "other"),
                        SurfaceAudience.PUBLIC, Set.of("send-enquiry")),
                contribution(ENQUIRY, SurfaceAudience.PUBLIC, Set.of("other-operation")),
                contribution(ENQUIRY, SurfaceAudience.MERCHANT, Set.of("send-enquiry")))) {
            assertTrue(source.currentParticipation(request(model, contribution)).isEmpty());
        }
        verifyNoInteractions(authority);
    }

    @Test
    void wrong_merchant_release_model_or_version_is_rejected_before_owner_read() {
        var model = model(Set.of("publication", "enquiry"), true);
        var authority = mock(OpportunityPublicationStateAuthority.class);
        var source = new OpportunityEnquiryParticipationSource(
                definition(), model, authority, Set.of("opportunity-1"));
        for (var request : List.of(
                new PublicInteractionParticipationRequest(new MerchantScope("merchant-b"),
                        RELEASE, "model-a", 7, contribution()),
                new PublicInteractionParticipationRequest(MERCHANT,
                        "release-b", "model-a", 7, contribution()),
                new PublicInteractionParticipationRequest(MERCHANT,
                        RELEASE, "model-b", 7, contribution()),
                new PublicInteractionParticipationRequest(MERCHANT,
                        RELEASE, "model-a", 8, contribution()))) {
            assertThrows(IllegalStateException.class, () -> source.currentParticipation(request));
        }
        verifyNoInteractions(authority);
    }

    @Test
    void definition_and_resolved_model_must_share_the_same_semantic_release() {
        var model = model(Set.of("publication", "enquiry"), true);
        var other = new ExecutableMerchantModel(model.merchantIdentifier(), model.modelIdentifier(),
                model.version(), "other-release", model.capabilityIdentifiers(),
                model.operationalObjects(), model.operations());
        assertThrows(IllegalArgumentException.class, () -> new OpportunityEnquiryParticipationSource(
                definition(), other, mock(OpportunityPublicationStateAuthority.class), Set.of()));
    }

    @Test
    void authority_cannot_launder_another_merchant_or_opportunity_identity() {
        var model = model(Set.of("publication", "enquiry"), true);
        for (var wrong : List.of(
                OpportunityPublicationState.draft(new MerchantScope("merchant-b"),
                        "opportunity-1", "revision-1"),
                OpportunityPublicationState.draft(MERCHANT, "opportunity-2", "revision-1"))) {
            var authority = mock(OpportunityPublicationStateAuthority.class);
            when(authority.current(MERCHANT, "opportunity-1")).thenReturn(Optional.of(wrong));
            var source = new OpportunityEnquiryParticipationSource(
                    definition(), model, authority, Set.of("opportunity-1"));
            assertThrows(IllegalStateException.class,
                    () -> source.currentParticipation(request(model, contribution())));
        }
    }

    @Test
    void definition_requires_the_exact_registered_subject_family_and_operation_owner() {
        var publication = publicationCapability("opportunity");
        var enquiry = enquiryCapability("enquiry", "send-enquiry");
        for (var definitions : List.of(
                Set.of(publication), Set.of(enquiry),
                Set.of(publicationCapability("announcement"), enquiry),
                Set.of(publication, enquiryCapability("other", "send-enquiry")),
                Set.of(publication, enquiryCapability("enquiry", "other-operation")))) {
            assertThrows(IllegalArgumentException.class, () ->
                    OpportunityEnquiryParticipationDefinition.forRelease(
                            new SemanticRegistrySnapshot(RELEASE, new HashSet<>(definitions))));
        }
    }

    @Test
    void subject_bounds_are_immutable_and_repeated_reads_do_not_mutate_authority() {
        var ids = new HashSet<>(Set.of("opportunity-1"));
        var model = model(Set.of("publication", "enquiry"), true);
        var authority = authority(PublicationLifecycle.PUBLISHED);
        var source = new OpportunityEnquiryParticipationSource(definition(), model, authority, ids);
        ids.add("injected");
        var first = source.currentParticipation(request(model, contribution()));
        assertEquals(first, source.currentParticipation(request(model, contribution())));
        assertThrows(UnsupportedOperationException.class, first::clear);
        verify(authority, times(2)).current(MERCHANT, "opportunity-1");
        verifyNoMoreInteractions(authority);
    }

    private static OpportunityEnquiryParticipationDefinition definition() {
        return OpportunityEnquiryParticipationDefinition.forRelease(new SemanticRegistrySnapshot(
                RELEASE, Set.of(publicationCapability("opportunity"),
                        enquiryCapability("enquiry", "send-enquiry"))));
    }

    private static RegisteredCapability publicationCapability(String family) {
        return new RegisteredCapability("publication", List.of(
                new OwnedOperationalObjectDefinition(family, Set.of(), (String) null)), List.of());
    }

    private static RegisteredCapability enquiryCapability(String owner, String operation) {
        return new RegisteredCapability(owner, List.of(
                new OwnedOperationalObjectDefinition("enquiry", Set.of(), (String) null)),
                List.of(OwnedOperationDefinition.creation(operation, "enquiry", null,
                        "enquiry-submitted", "submit-enquiry")));
    }

    private static ExecutableMerchantModel model(Set<String> capabilities, boolean hasOperation) {
        return new ExecutableMerchantModel(MERCHANT.merchantIdentifier(), "model-a", 7, RELEASE,
                capabilities, capabilities.contains("publication") ? List.of(
                        new ExecutableOperationalObjectDefinition("publication", "opportunity",
                                Set.of(), null)) : List.of(),
                hasOperation ? List.of(new ExecutableOperationDefinition(
                        "send-enquiry", List.of(new ExecutableObjectCreationEffect(
                        new ExecutableOperationalObjectTypeIdentity("enquiry", "enquiry"),
                        Optional.empty())), Set.of(), "submit-enquiry")) : List.of());
    }

    private static StaticSurfaceContribution contribution() {
        return contribution(ENQUIRY, SurfaceAudience.PUBLIC, Set.of("send-enquiry"));
    }

    private static StaticSurfaceContribution contribution(SurfaceContributionIdentity identity,
            SurfaceAudience audience, Set<String> operations) {
        return new StaticSurfaceContribution(identity, audience,
                SurfaceContributionKind.PUBLIC_INTERACTION, Optional.empty(), Set.of(), operations);
    }

    private static PublicInteractionParticipationRequest request(ExecutableMerchantModel model,
            StaticSurfaceContribution contribution) {
        return new PublicInteractionParticipationRequest(MERCHANT, RELEASE,
                model.modelIdentifier(), model.version(), contribution);
    }

    private static OpportunityPublicationStateAuthority authority(PublicationLifecycle lifecycle) {
        var authority = mock(OpportunityPublicationStateAuthority.class);
        when(authority.current(MERCHANT, "opportunity-1")).thenReturn(Optional.of(
                new OpportunityPublicationState(MERCHANT, "opportunity-1", "revision-1", lifecycle,
                        lifecycle != PublicationLifecycle.DRAFT
                                ? Optional.of("revision-1") : Optional.empty())));
        return authority;
    }
}
