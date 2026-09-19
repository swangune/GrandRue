package grandrue.publication;

import grandrue.application.MerchantScope;
import grandrue.surface.ProjectionSourceAvailability;
import grandrue.surface.ProjectionSourceCompleteness;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.net.URI;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OpportunityPublicRepresentationP5AdversarialTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final String OPPORTUNITY = "opportunity-1";

    @Test
    void published_binding_change_during_acquisition_fails_closed_without_material() {
        OpportunityPublicationMaterialRevision revisionOne = revision("revision-1", "First");
        OpportunityPublicationMaterialRevision revisionTwo = revision("revision-2", "Second");
        OpportunityPublicationState first = published("revision-1");
        OpportunityPublicationState second = published("revision-2");

        OpportunityPublicationStateAuthority authority = new OpportunityPublicationStateAuthority() {
            private int currentReads;

            @Override
            public OpportunityPublicationState establish(
                    OpportunityPublicationState initialState,
                    OpportunityPublicationMaterialRevision initialRevision
            ) {
                throw new UnsupportedOperationException();
            }

            @Override
            public OpportunityPublicationState compareAndSet(
                    OpportunityPublicationState expectedState,
                    OpportunityPublicationState nextState,
                    Optional<OpportunityPublicationMaterialRevision> newRevision
            ) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Optional<OpportunityPublicationState> current(
                    MerchantScope merchantScope,
                    String opportunityIdentity
            ) {
                if (!MERCHANT.equals(merchantScope) || !OPPORTUNITY.equals(opportunityIdentity)) {
                    return Optional.empty();
                }
                currentReads++;
                return Optional.of(currentReads == 1 ? first : second);
            }

            @Override
            public Optional<OpportunityPublicationMaterialRevision> revision(
                    MerchantScope merchantScope,
                    String opportunityIdentity,
                    String revisionIdentity
            ) {
                if (!MERCHANT.equals(merchantScope) || !OPPORTUNITY.equals(opportunityIdentity)) {
                    return Optional.empty();
                }
                return switch (revisionIdentity) {
                    case "revision-1" -> Optional.of(revisionOne);
                    case "revision-2" -> Optional.of(revisionTwo);
                    default -> Optional.empty();
                };
            }

            @Override
            public List<OpportunityPublicationHistoryEntry> publicationHistory(
                    MerchantScope merchantScope,
                    String opportunityIdentity
            ) {
                return List.of();
            }
        };

        OpportunityPublicRepresentationProjectionObservation observation =
                new AuthorityBackedOpportunityPublicRepresentationProjectionReadPort(authority)
                        .observe(
                                MERCHANT,
                                OPPORTUNITY,
                                Instant.parse("2026-09-05T12:00:00Z")
                        );

        assertTrue(observation.fragment().isEmpty());
        assertEquals(
                ProjectionSourceAvailability.UNAVAILABLE,
                observation.sourceEvidence().availability()
        );
        assertEquals(
                ProjectionSourceCompleteness.MISSING,
                observation.sourceEvidence().completeness()
        );
        assertTrue(observation.sourceEvidence().observedProgressIdentifier().isEmpty());
        assertTrue(observation.sourceEvidence().requiredCurrentProgressIdentifier().isEmpty());
    }

    @Test
    void material_affinity_evidence_carries_identity_only_and_runtime_type_remains_owner_private() {
        Set<String> entryFields = Arrays.stream(
                        OpportunityMaterialAffinityEntry.class.getRecordComponents()
                )
                .map(component -> component.getName())
                .collect(java.util.stream.Collectors.toSet());

        assertEquals(
                Set.of(
                        "candidate",
                        "sourceReference",
                        "expectedPublishedRevisionIdentity"
                ),
                entryFields
        );
        assertFalse(entryFields.contains("representation"));
        assertFalse(entryFields.contains("material"));
        assertFalse(entryFields.contains("title"));

        Class<?> runtimeType = OpportunityMaterialAffinityObservationContributionRegistration
                .runtimeBinding()
                .contractClass();
        assertTrue(Modifier.isFinal(runtimeType.getModifiers()));
        assertFalse(Modifier.isPublic(runtimeType.getModifiers()));
        assertEquals(
                OpportunityMaterialAffinityObservationContribution.class,
                runtimeType
        );
    }

    private static OpportunityPublicationState published(String revisionIdentity) {
        return new OpportunityPublicationState(
                MERCHANT,
                OPPORTUNITY,
                revisionIdentity,
                PublicationLifecycle.PUBLISHED,
                Optional.of(revisionIdentity)
        );
    }

    private static OpportunityPublicationMaterialRevision revision(
            String revisionIdentity,
            String title
    ) {
        return new OpportunityPublicationMaterialRevision(
                MERCHANT,
                OPPORTUNITY,
                revisionIdentity,
                title,
                Optional.of("Description"),
                Optional.of("Eligibility"),
                Optional.of("Provider"),
                Optional.of("Source"),
                List.of(new OpportunityExternalLink(
                        OpportunityExternalLinkRole.OFFICIAL_APPLICATION,
                        URI.create("https://example.com/apply"),
                        Optional.of("Apply")
                )),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );
    }
}
