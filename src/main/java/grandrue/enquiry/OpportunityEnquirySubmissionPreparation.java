package grandrue.enquiry;

import grandrue.application.MerchantScope;
import grandrue.publication.AuthorityBackedOpportunityPublicExposureReadPort;
import grandrue.publication.OpportunityEnquiryParticipationDefinition;
import grandrue.publication.OpportunityEnquiryParticipationSource;
import grandrue.publication.OpportunityPublicExposureReferences;
import grandrue.publication.OpportunityPublicExposureWindow;
import grandrue.publication.OpportunityPublicationState;
import grandrue.publication.OpportunityPublicationStateAuthority;
import grandrue.publication.OpportunityPublicationSubmissionLock;
import grandrue.publication.PublicationLifecycle;
import mainstreet.semantic.configuration.ConfigurationReleaseActivation;
import grandrue.semantic.executable.ExecutableMerchantModel;
import mainstreet.semantic.registry.OwnedOperationalObjectTypeReference;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import mainstreet.surface.PublicInteractionParticipationRequest;
import mainstreet.surface.StaticSurfaceContribution;
import mainstreet.surface.SurfaceAudience;
import mainstreet.surface.SurfaceContributionIdentity;
import mainstreet.surface.SurfaceContributionKind;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * E3 preparation for merchant-general and the initial Publication Opportunity Enquiry path.
 * The merchant scope comes from a trusted application boundary, never from a browser binding.
 * Captures the current immutable release for this invocation, revalidates the registered
 * interaction and explicit owner participation, and protects Publication currentness until E2
 * commits. Visibility uses the exact P4 portfolio: public representation has EXPOSE default,
 * no merchant choice and the Publication window requirement. It grants no actionability.
 *
 * The required delegate owns remaining Enquiry requirements, trusted CustomerContext association
 * checks and identity generation; no permissive default is supplied. No transport/authentication
 * establishment or universal contact schema is introduced here.
 */
public final class OpportunityEnquirySubmissionPreparation implements EnquirySubmissionPreparation {
    private static final SurfaceContributionIdentity INTERACTION =
            new SurfaceContributionIdentity("enquiry", "send-enquiry");
    private static final OwnedOperationalObjectTypeReference OPPORTUNITY =
            new OwnedOperationalObjectTypeReference("publication", "opportunity");

    private final MerchantScope trustedScope;
    private final ConfigurationReleaseActivation activation;
    private final SemanticRegistrySnapshot registry;
    private final Optional<OpportunityEnquiryParticipationDefinition> definition;
    private final OpportunityPublicationStateAuthority publication;
    private final OpportunityPublicationSubmissionLock publicationLock;
    private final EnquirySubmissionPreparation remainingRequirements;
    private final Clock clock;

    public OpportunityEnquirySubmissionPreparation(
            MerchantScope trustedScope,
            ConfigurationReleaseActivation activation,
            SemanticRegistrySnapshot registry,
            Optional<OpportunityEnquiryParticipationDefinition> definition,
            OpportunityPublicationStateAuthority publication,
            OpportunityPublicationSubmissionLock publicationLock,
            EnquirySubmissionPreparation remainingRequirements,
            Clock clock
    ) {
        this.trustedScope = Objects.requireNonNull(trustedScope, "trustedScope");
        this.activation = Objects.requireNonNull(activation, "activation");
        this.registry = Objects.requireNonNull(registry, "registry");
        this.definition = Objects.requireNonNull(definition, "definition");
        this.publication = Objects.requireNonNull(publication, "publication");
        this.publicationLock = Objects.requireNonNull(publicationLock, "publicationLock");
        this.remainingRequirements = Objects.requireNonNull(remainingRequirements, "remainingRequirements");
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    @Override
    public EnquirySubmission prepare(EnquirySubmissionIntent intent) {
        Objects.requireNonNull(intent, "intent");
        require(trustedScope.equals(intent.merchantScope()));
        var active = activation.current(trustedScope.merchantIdentifier()).orElseThrow(
                EnquirySubmissionRevalidationException::new);
        require(trustedScope.merchantIdentifier().equals(active.merchantIdentifier()));
        ExecutableMerchantModel model = active.release().executableModel();
        require(registry.version().equals(model.semanticRegistryVersion()));
        require(model.capabilityIdentifiers().contains("enquiry"));
        require(model.operation("send-enquiry").isPresent());
        require(registry.capability("enquiry").stream().flatMap(c -> c.operations().stream())
                .anyMatch(operation -> operation.identifier().equals("send-enquiry")));

        var interactions = active.release().resolvedPackage().staticSurfaceContributionCatalogue()
                .contributions().stream().filter(c -> INTERACTION.equals(c.identity())
                        && c.audience() == SurfaceAudience.PUBLIC
                        && c.kind() == SurfaceContributionKind.PUBLIC_INTERACTION
                        && c.supportedOperationReferences().contains("send-enquiry")).toList();
        require(interactions.size() == 1);
        // This initial public path has no authority to resolve additional contextual surface
        // gates. Fail closed if a richer registered contribution requires one.
        require(interactions.getFirst().eligibilityContract().requiredPrivilegeIdentifier().isEmpty()
                && !interactions.getFirst().interactionAvailabilityContract().providerReadinessDependent()
                && interactions.getFirst().customerEligibilityRequirement().isEmpty());
        if (intent.subjectRevision().isPresent()) {
            var subject = intent.subjectRevision().orElseThrow();
            require(OPPORTUNITY.equals(subject.subjectType()));
            var locked = publicationLock.lockCurrent(trustedScope, subject.subjectIdentity())
                    .orElseThrow(EnquirySubmissionRevalidationException::new);
            requireExactPublished(subject, locked);
            requireParticipation(subject, model, interactions.getFirst());
            requireExposure(subject, clock.instant());
        }

        EnquirySubmission prepared = Objects.requireNonNull(remainingRequirements.prepare(intent), "prepared");
        require(intent.equals(EnquirySubmissionIntent.from(prepared)));
        Instant submittedAt = clock.instant();
        // Keep time-based eligibility current after potentially slow contextual requirement checks.
        intent.subjectRevision().ifPresent(subject -> {
            requireExactPublished(subject, publication.current(trustedScope, subject.subjectIdentity())
                    .orElseThrow(EnquirySubmissionRevalidationException::new));
            requireExposure(subject, submittedAt);
        });
        return new EnquirySubmission(trustedScope, prepared.enquiryIdentity(), submittedAt,
                intent.question(), intent.contact(),
                new EnquirySemanticContext(registry.version(), model.modelIdentifier(), model.version()),
                intent.subjectRevision(), intent.customerContextIdentity());
    }

    private void requireParticipation(EnquiryRevisionProvenance subject, ExecutableMerchantModel model,
            StaticSurfaceContribution interaction) {
        var sourceDefinition = definition.orElseThrow(EnquirySubmissionRevalidationException::new);
        require(sourceDefinition.semanticRegistryReleaseIdentifier().equals(registry.version()));
        var source = new OpportunityEnquiryParticipationSource(sourceDefinition, model, publication,
                Set.of(subject.subjectIdentity()));
        var facts = source.currentParticipation(new PublicInteractionParticipationRequest(
                trustedScope, registry.version(), model.modelIdentifier(), model.version(), interaction));
        require(facts.stream().anyMatch(fact ->
                fact.subject().equals(OpportunityPublicExposureReferences.candidate(subject.subjectIdentity()))
                        && fact.contributionIdentity().equals(INTERACTION)
                        && fact.operationReference().equals("send-enquiry")
                        && fact.participationSourceIdentity().equals(sourceDefinition.sourceIdentity())));
    }

    private void requireExposure(EnquiryRevisionProvenance subject, Instant time) {
        var evidence = new AuthorityBackedOpportunityPublicExposureReadPort(publication)
                .currentPublishedExposure(trustedScope, subject.subjectIdentity())
                .orElseThrow(EnquirySubmissionRevalidationException::new);
        require(trustedScope.equals(evidence.merchantScope())
                && subject.subjectIdentity().equals(evidence.opportunityIdentity())
                && subject.revisionIdentity().equals(evidence.publishedRevisionIdentity())
                && OpportunityPublicExposureWindow.contains(evidence, time));
    }

    private void requireExactPublished(EnquiryRevisionProvenance subject, OpportunityPublicationState state) {
        require(trustedScope.equals(state.merchantScope())
                && subject.subjectIdentity().equals(state.opportunityIdentity())
                && state.lifecycle() == PublicationLifecycle.PUBLISHED
                && state.publishedRevisionIdentity().filter(subject.revisionIdentity()::equals).isPresent());
    }

    private static void require(boolean satisfied) {
        if (!satisfied) throw new EnquirySubmissionRevalidationException();
    }
}
