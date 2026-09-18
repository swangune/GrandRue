package grandrue.publication;

import grandrue.application.MerchantScope;
import mainstreet.semantic.executable.ExecutableMerchantModel;
import mainstreet.surface.PublicInteractionParticipationFact;
import mainstreet.surface.PublicInteractionParticipationRequest;
import mainstreet.surface.PublicInteractionParticipationSource;
import mainstreet.surface.PublicInteractionParticipationSourceRegistration;
import mainstreet.surface.SurfaceAudience;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * I1 source for a bounded set of Opportunity identities under one captured resolved model.
 * Identity hints only bound the owner reads; they are not participation evidence. The explicit
 * registered definition supplies participation semantics and Publication authority establishes
 * each exact subject. Generic S3 separately intersects these facts with serviceable exposed material.
 */
public final class OpportunityEnquiryParticipationSource
        implements PublicInteractionParticipationSource {
    private final OpportunityEnquiryParticipationDefinition definition;
    private final ExecutableMerchantModel model;
    private final MerchantScope merchantScope;
    private final OpportunityPublicationStateAuthority authority;
    private final Set<String> opportunityIdentities;

    public OpportunityEnquiryParticipationSource(
            OpportunityEnquiryParticipationDefinition definition,
            ExecutableMerchantModel model,
            OpportunityPublicationStateAuthority authority,
            Set<String> opportunityIdentities
    ) {
        this.definition = Objects.requireNonNull(definition, "definition");
        this.model = Objects.requireNonNull(model, "model");
        this.merchantScope = new MerchantScope(model.merchantIdentifier());
        this.authority = Objects.requireNonNull(authority, "authority");
        this.opportunityIdentities = Set.copyOf(
                Objects.requireNonNull(opportunityIdentities, "opportunityIdentities"));
        this.opportunityIdentities.forEach(identity -> {
            if (identity.isBlank()) {
                throw new IllegalArgumentException("Opportunity identity must not be blank");
            }
        });
        if (!definition.semanticRegistryReleaseIdentifier().equals(model.semanticRegistryVersion())) {
            throw new IllegalArgumentException(
                    "Participation definition and resolved model require the same semantic release");
        }
    }

    public PublicInteractionParticipationSourceRegistration registration() {
        return new PublicInteractionParticipationSourceRegistration(definition.sourceIdentity(), this);
    }

    @Override
    public Set<PublicInteractionParticipationFact> currentParticipation(
            PublicInteractionParticipationRequest request
    ) {
        Objects.requireNonNull(request, "request");
        requireContext(request);
        if (request.contribution().audience() != SurfaceAudience.PUBLIC
                || !definition.contributionIdentity().equals(request.contribution().identity())
                || !request.contribution().supportedOperationReferences()
                        .contains(definition.operationReference())
                || !model.capabilityIdentifiers().containsAll(Set.of("publication", "enquiry"))
                || model.operationalObject(definition.subjectKind().ownerIdentifier(),
                        definition.subjectKind().instanceKindIdentifier()).isEmpty()
                || model.operation(definition.operationReference()).isEmpty()) {
            return Set.of();
        }

        Set<PublicInteractionParticipationFact> facts = new LinkedHashSet<>();
        for (String identity : opportunityIdentities.stream().sorted().toList()) {
            authority.current(merchantScope, identity).ifPresent(state -> {
                if (!merchantScope.equals(state.merchantScope())
                        || !identity.equals(state.opportunityIdentity())) {
                    throw new IllegalStateException(
                            "Publication participation authority returned another Merchant Scope or subject");
                }
                // The family relationship is independent of lifecycle, publication windows and
                // actionability. It never grants public visibility or permission to submit Enquiry.
                facts.add(new PublicInteractionParticipationFact(
                        merchantScope,
                        definition.semanticRegistryReleaseIdentifier(),
                        model.modelIdentifier(),
                        model.version(),
                        definition.contributionIdentity(),
                        definition.operationReference(),
                        OpportunityPublicExposureReferences.candidate(identity),
                        definition.sourceIdentity(),
                        Optional.empty()
                ));
            });
        }
        return Set.copyOf(facts);
    }

    private void requireContext(PublicInteractionParticipationRequest request) {
        if (!merchantScope.equals(request.merchantScope())
                || !definition.semanticRegistryReleaseIdentifier()
                        .equals(request.semanticRegistryReleaseIdentifier())
                || !model.modelIdentifier().equals(request.resolvedModelIdentifier())
                || model.version() != request.resolvedModelVersion()) {
            throw new IllegalStateException(
                    "Opportunity participation request belongs to another merchant, release or resolved model");
        }
    }
}
