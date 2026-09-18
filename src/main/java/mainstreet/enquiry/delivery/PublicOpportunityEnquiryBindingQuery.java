package mainstreet.enquiry.delivery;

import grandrue.api.*;
import grandrue.enquiry.delivery.PublicOpportunityEnquiryContract;
import mainstreet.enquiry.EnquiryRevisionProvenance;
import mainstreet.publication.*;
import mainstreet.publication.delivery.*;
import mainstreet.semantic.configuration.ConfigurationReleaseActivation;
import mainstreet.semantic.registry.*;
import mainstreet.surface.*;
import java.time.Clock;
import java.util.*;

/** Concrete I2-to-browser handoff; only the exact selected P5 fragment supplies revision meaning. */
public final class PublicOpportunityEnquiryBindingQuery {
    public record BindingResponse(String opportunityIdentity, String binding) { }
    private final PublicOpportunityQuery query;
    private final OpportunityEnquiryParticipationDefinition definition;
    private final OpportunityPublicationStateAuthority owner;
    private final OpportunityEnquiryBindingCodec codec;

    private PublicOpportunityEnquiryBindingQuery(PublicOpportunityQuery query,
            OpportunityEnquiryParticipationDefinition definition, OpportunityPublicationStateAuthority owner,
            OpportunityEnquiryBindingCodec codec) {
        this.query = Objects.requireNonNull(query);
        this.definition = Objects.requireNonNull(definition);
        this.owner = Objects.requireNonNull(owner);
        this.codec = Objects.requireNonNull(codec);
    }

    public static PublicOpportunityEnquiryBindingQuery create(PublicOpportunityRouteScopeAuthority routes,
            ConfigurationReleaseActivation activation, SemanticRegistrySnapshot registry,
            OpportunityPublicationStateAuthority owner, AudienceObservationContextEstablisher contexts,
            AudienceObservationAdmissionEvaluator admission, Clock clock, OpportunityEnquiryBindingCodec codec) {
        return new PublicOpportunityEnquiryBindingQuery(PublicOpportunityQuery.create(routes, activation, registry,
                owner, contexts, admission, clock, PublicOpportunityEnquiryContract.query()),
                OpportunityEnquiryParticipationDefinition.forRelease(registry), owner, codec);
    }

    public Optional<BindingResponse> get(String locator, String opportunity) {
        return query.project(locator, opportunity, (model, catalogue, selected) -> {
            var read = selected.read();
            var expected = OpportunityPublicExposureReferences.candidate(opportunity);
            if (!PublicOpportunityEnquiryContract.query().registration().identity().equals(selected.exposure().contractIdentity())
                    || selected.exposure().surface() != ApiSurfaceClass.PUBLIC
                    || !OpportunityPublicRepresentationProjectionReferences.PROJECTION_CONTRACT.equals(read.contractIdentity())
                    || !OpportunityPublicRepresentationProjectionReferences.READ_USE.equals(read.readUseIdentity())
                    || read.fragments().size() != 1
                    || !(read.fragments().getFirst() instanceof OpportunityPublicRepresentationProjectionFragment fragment)
                    || !expected.equals(fragment.candidateObservation()))
                throw new IllegalStateException("Binding query affinity mismatch");
            var assembly = new PublicCustomerProjectionAssemblyService().assemble(read, selected.serviceability(), selected.exposure());
            var source = new OpportunityEnquiryParticipationSource(definition, model, owner, Set.of(opportunity));
            var bindings = new PublicInteractionBindingProjector().project(model, catalogue, read, assembly,
                    new PublicInteractionParticipationSourceRegistrySnapshot(read.semanticRegistryReleaseIdentifier(),
                            Set.of(source.registration())));
            if (bindings.isEmpty()) return Optional.empty();
            if (bindings.size() != 1 || !bindings.getFirst().subject().equals(expected))
                throw new IllegalStateException("Ambiguous Opportunity enquiry binding");
            var revision = new EnquiryRevisionProvenance(new OwnedOperationalObjectTypeReference("publication", "opportunity"),
                    opportunity, fragment.sourceAffinities().iterator().next().observedProgressIdentifier());
            return Optional.of(new BindingResponse(opportunity, codec.encode(read.merchantScope(), revision)));
        });
    }
}

