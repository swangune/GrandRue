package mainstreet.surface;

import grandrue.semantic.executable.ExecutableMerchantModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Composes capability-owned participation facts with currently applicable
 * PUBLIC_INTERACTION contribution semantics and the exact S2-selected public
 * Projection material. It owns no participation, Exposure or execution truth.
 */
public final class PublicInteractionBindingProjector {

    public List<PublicInteractionBinding> project(
            ExecutableMerchantModel model,
            StaticSurfaceContributionCatalogue catalogue,
            BoundedProjectionRead boundedRead,
            PublicCustomerProjectionAssembly assembly,
            PublicInteractionParticipationSourceRegistrySnapshot sourceRegistry
    ) {
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(catalogue, "catalogue");
        Objects.requireNonNull(boundedRead, "boundedRead");
        Objects.requireNonNull(assembly, "assembly");
        Objects.requireNonNull(sourceRegistry, "sourceRegistry");

        validateContext(model, boundedRead, assembly, sourceRegistry);
        Set<ExposureCandidateObservation> selectedSubjects =
                selectedSubjects(boundedRead, assembly);
        if (selectedSubjects.isEmpty()) {
            return List.of();
        }

        List<PublicInteractionParticipationSourceRegistration> registrations =
                sourceRegistry.registrations().stream()
                        .sorted(Comparator
                                .comparing((PublicInteractionParticipationSourceRegistration value) ->
                                        value.identity().ownerIdentifier())
                                .thenComparing(value ->
                                        value.identity().sourceIdentifier()))
                        .toList();
        if (registrations.isEmpty()) {
            return List.of();
        }

        List<StaticSurfaceContribution> interactions =
                applicableInteractions(catalogue);
        Map<ExactParticipationKey, PublicInteractionParticipationSourceIdentity>
                ownership = new HashMap<>();
        List<PublicInteractionBinding> bindings = new ArrayList<>();

        for (StaticSurfaceContribution contribution : interactions) {
            PublicInteractionParticipationRequest request =
                    new PublicInteractionParticipationRequest(
                            boundedRead.merchantScope(),
                            boundedRead.semanticRegistryReleaseIdentifier(),
                            model.modelIdentifier(),
                            model.version(),
                            contribution
                    );
            for (PublicInteractionParticipationSourceRegistration registration
                    : registrations) {
                Set<PublicInteractionParticipationFact> facts =
                        registration.source().currentParticipation(request);
                if (facts == null) {
                    throw structural(
                            "Participation source returned a null result set: "
                                    + registration.identity()
                    );
                }
                for (PublicInteractionParticipationFact fact : facts) {
                    if (fact == null) {
                        throw structural(
                                "Participation source returned a null fact: "
                                        + registration.identity()
                        );
                    }
                    validateFact(request, registration, fact);
                    if (!selectedSubjects.contains(fact.subject())) {
                        continue;
                    }

                    ExactParticipationKey key = new ExactParticipationKey(
                            fact.subject(),
                            fact.contributionIdentity(),
                            fact.operationReference(),
                            fact.participationRoleReference()
                    );
                    PublicInteractionParticipationSourceIdentity previousOwner =
                            ownership.putIfAbsent(
                                    key,
                                    fact.participationSourceIdentity()
                            );
                    if (previousOwner != null
                            && !previousOwner.equals(
                                    fact.participationSourceIdentity()
                            )) {
                        throw structural(
                                "Multiple participation sources claim the same exact Public Interaction fact"
                        );
                    }
                    if (previousOwner == null) {
                        bindings.add(PublicInteractionBinding.from(fact));
                    }
                }
            }
        }

        bindings.sort(bindingComparator());
        return List.copyOf(bindings);
    }

    private static void validateContext(
            ExecutableMerchantModel model,
            BoundedProjectionRead boundedRead,
            PublicCustomerProjectionAssembly assembly,
            PublicInteractionParticipationSourceRegistrySnapshot sourceRegistry
    ) {
        if (boundedRead.binding()
                != assembly.boundedProjectionReadBinding()) {
            throw structural(
                    "Public Interaction assembly does not belong to the exact bounded Projection read"
            );
        }
        if (!model.merchantIdentifier().equals(
                boundedRead.merchantScope().merchantIdentifier()
        )) {
            throw structural(
                    "Resolved merchant model belongs to another Merchant Scope"
            );
        }
        if (!model.semanticRegistryVersion().equals(
                boundedRead.semanticRegistryReleaseIdentifier()
        )) {
            throw structural(
                    "Resolved merchant model belongs to another semantic registry release"
            );
        }
        if (!sourceRegistry.semanticRegistryReleaseIdentifier().equals(
                boundedRead.semanticRegistryReleaseIdentifier()
        )) {
            throw structural(
                    "Participation source registry belongs to another semantic registry release"
            );
        }
    }

    private static Set<ExposureCandidateObservation> selectedSubjects(
            BoundedProjectionRead boundedRead,
            PublicCustomerProjectionAssembly assembly
    ) {
        Set<ExposureCandidateObservation> boundedReadSubjects = new HashSet<>();
        for (ProjectionMaterialFragment fragment : boundedRead.fragments()) {
            boundedReadSubjects.add(fragment.candidateObservation());
        }

        Set<ExposureCandidateObservation> selected = new HashSet<>();
        for (ProjectionMaterialFragment fragment : assembly.selectedFragments()) {
            ExposureCandidateObservation subject = fragment.candidateObservation();
            if (!boundedReadSubjects.contains(subject)) {
                throw structural(
                        "S2-selected subject is absent from the exact bounded Projection read"
                );
            }
            if (!selected.add(subject)) {
                throw structural(
                        "S2 assembly contains duplicate selected subject identity"
                );
            }
        }
        return Set.copyOf(selected);
    }

    private static List<StaticSurfaceContribution> applicableInteractions(
            StaticSurfaceContributionCatalogue catalogue
    ) {
        Set<SurfaceContributionIdentity> identities = new HashSet<>();
        List<StaticSurfaceContribution> interactions = new ArrayList<>();
        for (StaticSurfaceContribution contribution : catalogue.contributions()) {
            if (contribution.kind()
                    != SurfaceContributionKind.PUBLIC_INTERACTION) {
                continue;
            }
            if (!identities.add(contribution.identity())) {
                throw structural(
                        "Duplicate applicable PUBLIC_INTERACTION contribution identity: "
                                + contribution.identity()
                );
            }
            interactions.add(contribution);
        }
        interactions.sort(Comparator
                .comparing((StaticSurfaceContribution value) ->
                        value.identity().ownerCapabilityIdentifier())
                .thenComparing(value ->
                        value.identity().contributionIdentifier()));
        return List.copyOf(interactions);
    }

    private static void validateFact(
            PublicInteractionParticipationRequest request,
            PublicInteractionParticipationSourceRegistration registration,
            PublicInteractionParticipationFact fact
    ) {
        if (!registration.identity().equals(
                fact.participationSourceIdentity()
        )) {
            throw structural(
                    "Participation fact launders another source identity"
            );
        }
        if (!request.merchantScope().equals(fact.merchantScope())) {
            throw structural(
                    "Participation fact belongs to another Merchant Scope"
            );
        }
        if (!request.semanticRegistryReleaseIdentifier().equals(
                fact.semanticRegistryReleaseIdentifier()
        )) {
            throw structural(
                    "Participation fact belongs to another semantic registry release"
            );
        }
        if (!request.resolvedModelIdentifier().equals(
                fact.resolvedModelIdentifier()
        ) || request.resolvedModelVersion() != fact.resolvedModelVersion()) {
            throw structural(
                    "Participation fact belongs to another resolved merchant semantic context"
            );
        }
        if (!request.contribution().identity().equals(
                fact.contributionIdentity()
        )) {
            throw structural(
                    "Participation fact claims another Public Interaction contribution"
            );
        }
        if (!request.contribution().supportedOperationReferences().contains(
                fact.operationReference()
        )) {
            throw structural(
                    "Participation fact references an operation not registered by the evaluated contribution"
            );
        }
    }

    private static Comparator<PublicInteractionBinding> bindingComparator() {
        return Comparator
                .comparing((PublicInteractionBinding value) ->
                        value.contributionIdentity().ownerCapabilityIdentifier())
                .thenComparing(value ->
                        value.contributionIdentity().contributionIdentifier())
                .thenComparing(PublicInteractionBinding::operationReference)
                .thenComparing(value ->
                        value.subject().elementReference().ownerIdentifier())
                .thenComparing(value ->
                        value.subject().elementReference().elementIdentifier())
                .thenComparing(value -> value.subject().instanceReference()
                        .map(ExposureCandidateInstanceReference::instanceKindIdentifier)
                        .orElse(""))
                .thenComparing(value -> value.subject().instanceReference()
                        .map(ExposureCandidateInstanceReference::instanceIdentifier)
                        .orElse(""))
                .thenComparing(value ->
                        value.participationRoleReference().orElse(""))
                .thenComparing(value ->
                        value.participationSourceIdentity().ownerIdentifier())
                .thenComparing(value ->
                        value.participationSourceIdentity().sourceIdentifier());
    }

    private static PublicInteractionBindingProjectionStructuralException structural(
            String message
    ) {
        return new PublicInteractionBindingProjectionStructuralException(message);
    }

    private record ExactParticipationKey(
            ExposureCandidateObservation subject,
            SurfaceContributionIdentity contributionIdentity,
            String operationReference,
            Optional<String> participationRoleReference
    ) {
    }
}

/** Structural S3 affinity/ownership contradiction; never a legitimate empty result. */
final class PublicInteractionBindingProjectionStructuralException
        extends RuntimeException {

    PublicInteractionBindingProjectionStructuralException(String message) {
        super(message);
    }
}
