package grandrue.surface;

import grandrue.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BoundedProjectionReadServiceabilityAffinityTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final String RELEASE = "semantic-registry-1.0";
    private static final Instant EVALUATED_AT =
            Instant.parse("2026-09-03T10:00:00Z");
    private static final ProjectionSourceDependencyReference PROFILE_SOURCE =
            new ProjectionSourceDependencyReference(
                    "profile",
                    "merchant-public-descriptor"
            );
    private static final ProjectionContractIdentity CONTRACT =
            new ProjectionContractIdentity("platform", "merchant-presence");
    private static final ProjectionReadUseIdentity READ_USE =
            new ProjectionReadUseIdentity("platform", "public-merchant-presence");
    private static final ExposableElementReference DISPLAY_NAME =
            new ExposableElementReference("profile", "public-display-name");

    @Test
    void bounded_p2_request_is_derived_from_exact_read_and_retains_same_binding_on_success() {
        ProjectionSourceEvidence evidence = evidence(
                "evidence-r8",
                "profile-r8"
        );
        BoundedProjectionRead read = read(
                CONTRACT,
                READ_USE,
                Set.of(evidence)
        );

        ProjectionServiceabilityEvaluationRequest request =
                ProjectionServiceabilityEvaluationRequest.forBoundedRead(
                        read,
                        EVALUATED_AT
                );

        assertEquals(RELEASE, request.semanticRegistryReleaseIdentifier());
        assertEquals(CONTRACT, request.contractIdentity());
        assertEquals(READ_USE, request.readUseIdentity());
        assertEquals(Set.of(evidence), request.sourceEvidence());
        assertSame(
                read.binding(),
                request.boundedReadBinding().orElseThrow()
        );

        ProjectionServiceabilityResult result = engine(CONTRACT).evaluate(request);

        assertEquals(
                ProjectionServiceabilityOutcome.FULLY_SERVICEABLE,
                result.outcome()
        );
        assertEquals(Set.of(evidence), result.sourceEvidence());
        assertSame(
                read.binding(),
                result.boundedReadBinding().orElseThrow()
        );
    }

    @Test
    void fail_closed_p2_result_still_retains_exact_bounded_read_binding() {
        ProjectionContractIdentity missingContract =
                new ProjectionContractIdentity("platform", "missing-projection");
        ProjectionReadUseIdentity missingReadUse =
                new ProjectionReadUseIdentity("platform", "missing-read-use");
        BoundedProjectionRead read = read(
                missingContract,
                missingReadUse,
                Set.of(evidence("evidence-r8", "profile-r8"))
        );

        ProjectionServiceabilityResult result = engine(CONTRACT).evaluate(
                ProjectionServiceabilityEvaluationRequest.forBoundedRead(
                        read,
                        EVALUATED_AT
                )
        );

        assertEquals(
                ProjectionServiceabilityOutcome.NOT_SERVICEABLE,
                result.outcome()
        );
        assertSame(
                read.binding(),
                result.boundedReadBinding().orElseThrow()
        );
    }

    @Test
    void bounded_read_retains_immutable_exact_p2_evidence_set_and_rejects_competing_source_rows() {
        ProjectionSourceEvidence r8 = evidence("evidence-r8", "profile-r8");
        BoundedProjectionRead read = read(CONTRACT, READ_USE, Set.of(r8));

        assertEquals(Set.of(r8), read.sourceEvidence());
        assertThrows(
                UnsupportedOperationException.class,
                () -> read.sourceEvidence().add(
                        evidence("evidence-r9", "profile-r9")
                )
        );

        ProjectionSourceEvidence competing = evidence(
                "competing-evidence-r9",
                "profile-r9"
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> read(CONTRACT, READ_USE, Set.of(r8, competing))
        );
    }

    @Test
    void legacy_unbound_p2_request_does_not_fabricate_bounded_read_affinity() {
        ProjectionServiceabilityEvaluationRequest request =
                new ProjectionServiceabilityEvaluationRequest(
                        RELEASE,
                        CONTRACT,
                        READ_USE,
                        EVALUATED_AT,
                        Set.of(evidence("evidence-r8", "profile-r8"))
                );

        assertFalse(request.boundedReadBinding().isPresent());
        assertFalse(engine(CONTRACT).evaluate(request)
                .boundedReadBinding().isPresent());
    }

    private static BoundedProjectionRead read(
            ProjectionContractIdentity contractIdentity,
            ProjectionReadUseIdentity readUseIdentity,
            Set<ProjectionSourceEvidence> sourceEvidence
    ) {
        return new BoundedProjectionRead(
                observationRequest(),
                contractIdentity,
                readUseIdentity,
                sourceEvidence,
                List.of(new TestFragment(
                        "Bella",
                        new ExposureCandidateObservation(
                                DISPLAY_NAME,
                                Optional.empty()
                        ),
                        Set.of(new ProjectionMaterialSourceAffinity(
                                PROFILE_SOURCE,
                                "profile-r8"
                        ))
                ))
        );
    }

    private static ProjectionServiceabilityEvaluationEngine engine(
            ProjectionContractIdentity registeredContractIdentity
    ) {
        ProjectionPolicyReference freshness = policy(
                "platform",
                "merchant-presence-freshness"
        );
        ProjectionPolicyReference serviceability = policy(
                "platform",
                "merchant-presence-serviceability"
        );
        ProjectionPolicyReference missingEvidence = policy(
                "platform",
                "merchant-presence-missing-evidence"
        );
        ProjectionPolicyReference staleServing = policy(
                "platform",
                "merchant-presence-stale-serving"
        );

        ProjectionContractDefinition contract =
                new ProjectionContractDefinition(
                        registeredContractIdentity,
                        Set.of(ProjectionContractApplicabilityTrigger
                                .REGISTERED_DEPENDENCY),
                        Set.of(PROFILE_SOURCE),
                        ProjectionMaterialisationKind.REQUEST_SCOPED,
                        freshness,
                        Set.of(new ProjectionReadUseContract(
                                READ_USE,
                                serviceability,
                                missingEvidence,
                                staleServing
                        )),
                        Optional.empty(),
                        Optional.empty()
                );

        ProjectionPolicyEvaluatorRegistrySnapshot evaluators =
                new ProjectionPolicyEvaluatorRegistrySnapshot(
                        RELEASE,
                        Set.of(
                                binding(
                                        ProjectionPolicyCategory.FRESHNESS,
                                        freshness
                                ),
                                binding(
                                        ProjectionPolicyCategory.SERVICEABILITY,
                                        serviceability
                                ),
                                binding(
                                        ProjectionPolicyCategory.MISSING_EVIDENCE,
                                        missingEvidence
                                ),
                                binding(
                                        ProjectionPolicyCategory.STALE_SERVING,
                                        staleServing
                                )
                        )
                );

        return new ProjectionServiceabilityEvaluationEngine(
                new ProjectionContractRegistrySnapshot(
                        RELEASE,
                        Set.of(contract)
                ),
                evaluators
        );
    }

    private static ProjectionPolicyEvaluatorBinding binding(
            ProjectionPolicyCategory category,
            ProjectionPolicyReference reference
    ) {
        return new ProjectionPolicyEvaluatorBinding(
                category,
                reference,
                new ProjectionPolicyEvaluatorIdentity(
                        reference.ownerIdentifier(),
                        reference.policyIdentifier() + "-evaluator",
                        "v1"
                ),
                context -> ProjectionPolicyAssessment.fullyServiceable()
        );
    }

    private static ProjectionPolicyReference policy(
            String owner,
            String identifier
    ) {
        return new ProjectionPolicyReference(owner, identifier);
    }

    private static ProjectionSourceEvidence evidence(
            String evidenceIdentifier,
            String progressIdentifier
    ) {
        return new ProjectionSourceEvidence(
                PROFILE_SOURCE,
                evidenceIdentifier,
                Optional.of(progressIdentifier),
                Optional.of(progressIdentifier),
                EVALUATED_AT.minusSeconds(5),
                ProjectionSourceAvailability.AVAILABLE,
                ProjectionSourceCompleteness.COMPLETE,
                ProjectionSourceRevocationState.CLEAR
        );
    }

    private static EstablishedObservationRequest observationRequest() {
        return new DefaultEstablishedObservationRequest(
                TestReleases.activeRelease(
                        MERCHANT.merchantIdentifier(),
                        RELEASE
                ),
                MERCHANT,
                RELEASE,
                Optional.empty()
        );
    }

    private record TestFragment(
            String displayName,
            ExposureCandidateObservation candidateObservation,
            Set<ProjectionMaterialSourceAffinity> sourceAffinities
    ) implements ProjectionMaterialFragment {
        private TestFragment {
            sourceAffinities = Set.copyOf(sourceAffinities);
        }
    }
}
