package grandrue.merchantprofile;

import grandrue.application.MerchantScope;
import grandrue.runtime.SessionRecordStore;
import grandrue.surface.AudienceObservationContextEstablisher;
import grandrue.surface.AudienceObservationContextEstablishmentResult;
import grandrue.surface.BoundedProjectionRead;
import grandrue.surface.ContextualAccessProofRuntimeBindingSnapshot;
import grandrue.surface.EstablishedObservationContribution;
import grandrue.surface.EstablishedObservationRequest;
import grandrue.surface.ExposableElementReference;
import grandrue.surface.ExposureCandidateInstanceReference;
import grandrue.surface.ExposureCandidateObservation;
import grandrue.surface.ObservationContributionConstructionBoundary;
import grandrue.surface.ObservationContributionDefinitionRegistrySnapshot;
import grandrue.surface.ObservationContributionRuntimeBindingSnapshot;
import grandrue.surface.ProjectionContractIdentity;
import grandrue.surface.ProjectionMaterialFragment;
import grandrue.surface.ProjectionMaterialSourceAffinity;
import grandrue.surface.ProjectionReadUseIdentity;
import grandrue.surface.ProjectionSourceAvailability;
import grandrue.surface.ProjectionSourceCompleteness;
import grandrue.surface.ProjectionSourceDependencyReference;
import grandrue.surface.ProjectionSourceEvidence;
import grandrue.surface.ProjectionSourceRevocationState;
import grandrue.surface.TestObservationRequests;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Real Profile-through-E3 registration conformance for BR4. */
class ProfileMaterialAffinityE3RegistrationConformanceTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final ProjectionSourceDependencyReference CONTACT_SOURCE =
            new ProjectionSourceDependencyReference("profile", "contact-points");

    @Test
    void real_profile_registration_and_runtime_binding_are_accepted_by_exact_release_e3() {
        EstablishedObservationRequest request = TestObservationRequests.request(
                MERCHANT,
                "semantic-registry-1.0"
        );
        ExposureCandidateObservation contact = new ExposureCandidateObservation(
                new ExposableElementReference("profile", "public-contact-point"),
                Optional.of(new ExposureCandidateInstanceReference(
                        "profile",
                        "contact-point",
                        "C1"
                ))
        );
        String progress = "contact-revision-8";
        ProjectionSourceEvidence evidence = new ProjectionSourceEvidence(
                CONTACT_SOURCE,
                "evidence-contact-revision-8",
                Optional.of(progress),
                Optional.of(progress),
                Instant.parse("2026-09-03T02:30:00Z"),
                ProjectionSourceAvailability.AVAILABLE,
                ProjectionSourceCompleteness.COMPLETE,
                ProjectionSourceRevocationState.NOT_APPLICABLE
        );
        ProjectionMaterialFragment fragment = new TestFragment(
                contact,
                Set.of(new ProjectionMaterialSourceAffinity(
                        CONTACT_SOURCE,
                        progress
                ))
        );
        BoundedProjectionRead read = new BoundedProjectionRead(
                request,
                new ProjectionContractIdentity("platform", "merchant-presence"),
                new ProjectionReadUseIdentity(
                        "platform",
                        "public-merchant-presence"
                ),
                Set.of(evidence),
                List.of(fragment)
        );

        EstablishedObservationContribution contribution =
                new ObservationContributionConstructionBoundary().construct(
                        request,
                        ProfileMaterialAffinityObservationContributionRegistration
                                .constructor(read)
                );
        AudienceObservationContextEstablisher establisher =
                new AudienceObservationContextEstablisher(
                        emptySessions(),
                        new ContextualAccessProofRuntimeBindingSnapshot(List.of()),
                        new ObservationContributionDefinitionRegistrySnapshot(
                                "semantic-registry-1.0",
                                List.of(
                                        ProfileMaterialAffinityObservationContributionRegistration
                                                .definition()
                                )
                        ),
                        new ObservationContributionRuntimeBindingSnapshot(
                                List.of(
                                        ProfileMaterialAffinityObservationContributionRegistration
                                                .runtimeBinding()
                                )
                        )
                );

        AudienceObservationContextEstablishmentResult result =
                establisher.establishPublic(request, List.of(contribution));

        assertTrue(result.failure().isEmpty());
        assertTrue(result.context().isPresent());
    }

    private static SessionRecordStore emptySessions() {
        return new SessionRecordStore() {
            @Override
            public grandrue.runtime.SessionRecord create(
                    grandrue.runtime.SessionRecord candidate
            ) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Optional<grandrue.runtime.SessionRecord> sessionByIdentity(
                    String sessionIdentity
            ) {
                return Optional.empty();
            }

            @Override
            public Optional<grandrue.runtime.SessionRecord>
            sessionByCredentialVerifier(String credentialVerifier) {
                return Optional.empty();
            }

            @Override
            public grandrue.runtime.SessionRecord revoke(
                    String sessionIdentity,
                    Instant revokedAt,
                    String reason
            ) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void revokeAllForIdentity(
                    String identityReference,
                    Instant revokedAt,
                    String reason
            ) {
                throw new UnsupportedOperationException();
            }
        };
    }

    private record TestFragment(
            ExposureCandidateObservation candidateObservation,
            Set<ProjectionMaterialSourceAffinity> sourceAffinities
    ) implements ProjectionMaterialFragment {
        private TestFragment {
            sourceAffinities = Set.copyOf(sourceAffinities);
        }
    }
}
