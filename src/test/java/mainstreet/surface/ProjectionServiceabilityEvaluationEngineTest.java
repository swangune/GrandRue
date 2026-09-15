package mainstreet.surface;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProjectionServiceabilityEvaluationEngineTest {

    private static final String RELEASE = "semantic-registry-18";
    private static final Instant EVALUATED_AT =
            Instant.parse("2026-08-30T16:00:00Z");

    @Test
    void evaluator_registry_resolves_only_exact_release_category_and_policy() {
        ProjectionPolicyEvaluatorBinding binding = binding(
                ProjectionPolicyCategory.FRESHNESS,
                policy("platform", "presence-freshness")
        );
        ProjectionPolicyEvaluatorRegistrySnapshot registry =
                new ProjectionPolicyEvaluatorRegistrySnapshot(
                        RELEASE,
                        Set.of(binding)
                );

        assertEquals(
                Optional.of(binding),
                registry.binding(
                        RELEASE,
                        ProjectionPolicyCategory.FRESHNESS,
                        binding.policyReference()
                )
        );
        assertFalse(registry.binding(
                "semantic-registry-19",
                ProjectionPolicyCategory.FRESHNESS,
                binding.policyReference()
        ).isPresent());
        assertFalse(registry.binding(
                RELEASE,
                ProjectionPolicyCategory.SERVICEABILITY,
                binding.policyReference()
        ).isPresent());

        ProjectionPolicyEvaluatorBinding conflict =
                new ProjectionPolicyEvaluatorBinding(
                        binding.policyCategory(),
                        binding.policyReference(),
                        new ProjectionPolicyEvaluatorIdentity(
                                "platform",
                                "other-evaluator",
                                "v1"
                        ),
                        context -> ProjectionPolicyAssessment.fullyServiceable()
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> new ProjectionPolicyEvaluatorRegistrySnapshot(
                        RELEASE,
                        Set.of(binding, conflict)
                )
        );
    }

    @Test
    void currentness_is_derived_from_exact_evidence_values() {
        ProjectionSourceDependencyReference source = source(
                "profile",
                "merchant-public-descriptor"
        );
        ProjectionSourceEvidence current = current(source);
        ProjectionSourceEvidence stale = stale(source);
        ProjectionSourceEvidence notApplicable = notApplicable(source);

        assertTrue(current.isCurrent());
        assertFalse(current.isKnownStale());
        assertFalse(stale.isCurrent());
        assertTrue(stale.isKnownStale());
        assertFalse(notApplicable.isCurrent());
        assertTrue(notApplicable.isNotApplicable());

        assertThrows(
                IllegalArgumentException.class,
                () -> new ProjectionSourceEvidence(
                        source,
                        "evidence-invalid",
                        Optional.of("observed"),
                        Optional.empty(),
                        EVALUATED_AT,
                        ProjectionSourceAvailability.NOT_APPLICABLE,
                        ProjectionSourceCompleteness.COMPLETE,
                        ProjectionSourceRevocationState.NOT_APPLICABLE
                )
        );
    }

    @Test
    void evaluation_fails_closed_for_missing_exact_authority_or_evidence() {
        ProjectionContractRegistrySnapshot contracts =
                InitialProjectionContractPortfolio.forRelease(RELEASE);
        ProjectionServiceabilityEvaluationEngine noEvaluators =
                new ProjectionServiceabilityEvaluationEngine(
                        contracts,
                        new ProjectionPolicyEvaluatorRegistrySnapshot(
                                RELEASE,
                                Set.of()
                        )
                );
        ProjectionContractDefinition presence = presence(contracts);

        ProjectionServiceabilityResult unresolvedPolicy =
                noEvaluators.evaluate(request(
                        presence,
                        new ProjectionReadUseIdentity(
                                "platform",
                                "public-merchant-presence"
                        ),
                        currentEvidence(presence)
                ));

        assertEquals(
                ProjectionServiceabilityOutcome.NOT_SERVICEABLE,
                unresolvedPolicy.outcome()
        );
        assertTrue(unresolvedPolicy.reasonCodes().contains(
                ProjectionServiceabilityReasonCode
                        .POLICY_EVALUATOR_UNRESOLVED
        ));
        assertFalse(unresolvedPolicy.unresolvedPolicyReferences().isEmpty());

        ProjectionServiceabilityEvaluationEngine engine = engine(contracts);
        Set<ProjectionSourceEvidence> incomplete = currentEvidence(presence);
        ProjectionSourceEvidence removed = incomplete.iterator().next();
        incomplete = new HashSet<>(incomplete);
        incomplete.remove(removed);

        ProjectionServiceabilityResult evidenceMismatch = engine.evaluate(
                request(
                        presence,
                        new ProjectionReadUseIdentity(
                                "platform",
                                "public-merchant-presence"
                        ),
                        incomplete
                )
        );

        assertEquals(
                ProjectionServiceabilityOutcome.NOT_SERVICEABLE,
                evidenceMismatch.outcome()
        );
        assertTrue(evidenceMismatch.reasonCodes().contains(
                ProjectionServiceabilityReasonCode.EVIDENCE_SET_MISMATCH
        ));

        ProjectionServiceabilityResult wrongRelease = engine.evaluate(
                new ProjectionServiceabilityEvaluationRequest(
                        "semantic-registry-19",
                        presence.identity(),
                        new ProjectionReadUseIdentity(
                                "platform",
                                "public-merchant-presence"
                        ),
                        EVALUATED_AT,
                        currentEvidence(presence)
                )
        );
        assertEquals(
                ProjectionServiceabilityOutcome.NOT_SERVICEABLE,
                wrongRelease.outcome()
        );
        assertTrue(wrongRelease.reasonCodes().contains(
                ProjectionServiceabilityReasonCode
                        .EXACT_CONTRACT_UNRESOLVED
        ));
    }

    @Test
    void merchant_presence_is_full_reduced_or_unserviceable_without_invention() {
        ProjectionContractRegistrySnapshot contracts =
                InitialProjectionContractPortfolio.forRelease(RELEASE);
        ProjectionContractDefinition presence = presence(contracts);
        ProjectionServiceabilityEvaluationEngine engine = engine(contracts);
        ProjectionReadUseIdentity readUse = new ProjectionReadUseIdentity(
                "platform",
                "public-merchant-presence"
        );

        ProjectionServiceabilityResult full = engine.evaluate(request(
                presence,
                readUse,
                currentEvidence(presence)
        ));
        assertEquals(
                ProjectionServiceabilityOutcome.FULLY_SERVICEABLE,
                full.outcome()
        );

        ProjectionSourceDependencyReference hours = source(
                "business-hours",
                "public-business-hours"
        );
        ProjectionServiceabilityResult reduced = engine.evaluate(request(
                presence,
                readUse,
                replace(
                        currentEvidence(presence),
                        unavailable(hours)
                )
        ));
        assertEquals(
                ProjectionServiceabilityOutcome.REDUCED_SERVICEABLE,
                reduced.outcome()
        );
        assertEquals(Set.of(hours), reduced.omittedSourceReferences());
        assertTrue(reduced.reasonCodes().contains(
                ProjectionServiceabilityReasonCode.SOURCE_UNAVAILABLE
        ));

        ProjectionSourceDependencyReference descriptor = source(
                "profile",
                "merchant-public-descriptor"
        );
        ProjectionServiceabilityResult noDescriptor = engine.evaluate(request(
                presence,
                readUse,
                replace(
                        currentEvidence(presence),
                        unavailable(descriptor)
                )
        ));
        assertEquals(
                ProjectionServiceabilityOutcome.NOT_SERVICEABLE,
                noDescriptor.outcome()
        );

        ProjectionSourceDependencyReference location = source(
                "profile",
                "merchant-locations"
        );
        assertEquals(
                ProjectionServiceabilityOutcome.REDUCED_SERVICEABLE,
                engine.evaluate(request(
                        presence,
                        readUse,
                        replace(currentEvidence(presence), stale(location))
                )).outcome()
        );
        assertEquals(
                ProjectionServiceabilityOutcome.REDUCED_SERVICEABLE,
                engine.evaluate(request(
                        presence,
                        readUse,
                        replace(currentEvidence(presence), revoked(location))
                )).outcome()
        );
    }

    @Test
    void calendar_preserves_commitments_but_never_invents_availability() {
        ProjectionContractRegistrySnapshot contracts =
                InitialProjectionContractPortfolio.forRelease(RELEASE);
        ProjectionContractDefinition calendar = calendar(contracts);
        ProjectionServiceabilityEvaluationEngine engine = engine(contracts);
        ProjectionReadUseIdentity committed = new ProjectionReadUseIdentity(
                "calendar",
                "committed-work-overview"
        );
        ProjectionReadUseIdentity availability = new ProjectionReadUseIdentity(
                "calendar",
                "availability-oriented"
        );
        ProjectionSourceDependencyReference external = source(
                "calendar-integration",
                "external-busy-constraints"
        );

        Set<ProjectionSourceEvidence> current = currentEvidence(calendar);
        assertEquals(
                ProjectionServiceabilityOutcome.FULLY_SERVICEABLE,
                engine.evaluate(request(calendar, committed, current)).outcome()
        );
        assertEquals(
                ProjectionServiceabilityOutcome.FULLY_SERVICEABLE,
                engine.evaluate(request(
                        calendar,
                        availability,
                        current
                )).outcome()
        );

        Set<ProjectionSourceEvidence> providerUnavailable = replace(
                current,
                unavailable(external)
        );
        assertEquals(
                ProjectionServiceabilityOutcome.REDUCED_SERVICEABLE,
                engine.evaluate(request(
                        calendar,
                        committed,
                        providerUnavailable
                )).outcome()
        );
        assertEquals(
                ProjectionServiceabilityOutcome.NOT_SERVICEABLE,
                engine.evaluate(request(
                        calendar,
                        availability,
                        providerUnavailable
                )).outcome()
        );

        Set<ProjectionSourceEvidence> integrationDisabled = replace(
                current,
                notApplicable(external)
        );
        assertEquals(
                ProjectionServiceabilityOutcome.FULLY_SERVICEABLE,
                engine.evaluate(request(
                        calendar,
                        availability,
                        integrationDisabled
                )).outcome()
        );
    }

    @Test
    void result_is_deterministic_immutable_and_retains_exact_provenance() {
        ProjectionContractRegistrySnapshot contracts =
                InitialProjectionContractPortfolio.forRelease(RELEASE);
        ProjectionContractDefinition presence = presence(contracts);
        ProjectionServiceabilityEvaluationEngine engine = engine(contracts);
        ProjectionServiceabilityEvaluationRequest request = request(
                presence,
                new ProjectionReadUseIdentity(
                        "platform",
                        "public-merchant-presence"
                ),
                currentEvidence(presence)
        );

        ProjectionServiceabilityResult first = engine.evaluate(request);
        ProjectionServiceabilityResult second = engine.evaluate(request);

        assertEquals(first, second);
        assertEquals(5, first.consumedPolicyEvaluators().size());
        assertEquals(6, first.sourceEvidence().size());
        assertEquals(EVALUATED_AT, first.evaluatedAt());
        assertThrows(
                UnsupportedOperationException.class,
                () -> first.sourceEvidence().clear()
        );
        assertThrows(
                UnsupportedOperationException.class,
                () -> first.consumedPolicyEvaluators().clear()
        );
    }

    private static ProjectionServiceabilityEvaluationEngine engine(
            ProjectionContractRegistrySnapshot contracts
    ) {
        return new ProjectionServiceabilityEvaluationEngine(
                contracts,
                InitialProjectionPolicyEvaluatorPortfolio.forRelease(RELEASE)
        );
    }

    private static ProjectionContractDefinition presence(
            ProjectionContractRegistrySnapshot contracts
    ) {
        return contracts.contract(
                RELEASE,
                new ProjectionContractIdentity(
                        "platform",
                        "merchant-presence"
                )
        ).orElseThrow();
    }

    private static ProjectionContractDefinition calendar(
            ProjectionContractRegistrySnapshot contracts
    ) {
        return contracts.contract(
                RELEASE,
                new ProjectionContractIdentity(
                        "calendar",
                        "merchant-calendar"
                )
        ).orElseThrow();
    }

    private static ProjectionServiceabilityEvaluationRequest request(
            ProjectionContractDefinition contract,
            ProjectionReadUseIdentity readUse,
            Set<ProjectionSourceEvidence> evidence
    ) {
        return new ProjectionServiceabilityEvaluationRequest(
                RELEASE,
                contract.identity(),
                readUse,
                EVALUATED_AT,
                evidence
        );
    }

    private static Set<ProjectionSourceEvidence> currentEvidence(
            ProjectionContractDefinition contract
    ) {
        return contract.authoritativeSourceReferences().stream()
                .map(ProjectionServiceabilityEvaluationEngineTest::current)
                .collect(java.util.stream.Collectors.toUnmodifiableSet());
    }

    private static Set<ProjectionSourceEvidence> replace(
            Set<ProjectionSourceEvidence> evidence,
            ProjectionSourceEvidence replacement
    ) {
        Set<ProjectionSourceEvidence> replaced = new HashSet<>(evidence);
        replaced.removeIf(value -> value.sourceReference().equals(
                replacement.sourceReference()
        ));
        replaced.add(replacement);
        return Set.copyOf(replaced);
    }

    private static ProjectionSourceEvidence current(
            ProjectionSourceDependencyReference source
    ) {
        String progress = "current-" + source.sourceIdentifier();
        return new ProjectionSourceEvidence(
                source,
                "evidence-" + source.ownerIdentifier()
                        + "-" + source.sourceIdentifier(),
                Optional.of(progress),
                Optional.of(progress),
                EVALUATED_AT.minusSeconds(30),
                ProjectionSourceAvailability.AVAILABLE,
                ProjectionSourceCompleteness.COMPLETE,
                ProjectionSourceRevocationState.CLEAR
        );
    }

    private static ProjectionSourceEvidence stale(
            ProjectionSourceDependencyReference source
    ) {
        return new ProjectionSourceEvidence(
                source,
                "evidence-stale-" + source.sourceIdentifier(),
                Optional.of("revision-1"),
                Optional.of("revision-2"),
                EVALUATED_AT.minusSeconds(30),
                ProjectionSourceAvailability.AVAILABLE,
                ProjectionSourceCompleteness.COMPLETE,
                ProjectionSourceRevocationState.CLEAR
        );
    }

    private static ProjectionSourceEvidence unavailable(
            ProjectionSourceDependencyReference source
    ) {
        return new ProjectionSourceEvidence(
                source,
                "evidence-unavailable-" + source.sourceIdentifier(),
                Optional.empty(),
                Optional.empty(),
                EVALUATED_AT.minusSeconds(30),
                ProjectionSourceAvailability.UNAVAILABLE,
                ProjectionSourceCompleteness.MISSING,
                ProjectionSourceRevocationState.NOT_APPLICABLE
        );
    }

    private static ProjectionSourceEvidence revoked(
            ProjectionSourceDependencyReference source
    ) {
        String progress = "current-" + source.sourceIdentifier();
        return new ProjectionSourceEvidence(
                source,
                "evidence-revoked-" + source.sourceIdentifier(),
                Optional.of(progress),
                Optional.of(progress),
                EVALUATED_AT.minusSeconds(30),
                ProjectionSourceAvailability.AVAILABLE,
                ProjectionSourceCompleteness.COMPLETE,
                ProjectionSourceRevocationState.REVOKED
        );
    }

    private static ProjectionSourceEvidence notApplicable(
            ProjectionSourceDependencyReference source
    ) {
        return new ProjectionSourceEvidence(
                source,
                "evidence-not-applicable-" + source.sourceIdentifier(),
                Optional.empty(),
                Optional.empty(),
                EVALUATED_AT.minusSeconds(30),
                ProjectionSourceAvailability.NOT_APPLICABLE,
                ProjectionSourceCompleteness.NOT_APPLICABLE,
                ProjectionSourceRevocationState.NOT_APPLICABLE
        );
    }

    private static ProjectionPolicyEvaluatorBinding binding(
            ProjectionPolicyCategory category,
            ProjectionPolicyReference policyReference
    ) {
        return new ProjectionPolicyEvaluatorBinding(
                category,
                policyReference,
                new ProjectionPolicyEvaluatorIdentity(
                        policyReference.ownerIdentifier(),
                        policyReference.policyIdentifier() + "-evaluator",
                        "v1"
                ),
                context -> ProjectionPolicyAssessment.fullyServiceable()
        );
    }

    private static ProjectionSourceDependencyReference source(
            String owner,
            String identifier
    ) {
        return new ProjectionSourceDependencyReference(owner, identifier);
    }

    private static ProjectionPolicyReference policy(
            String owner,
            String identifier
    ) {
        return new ProjectionPolicyReference(owner, identifier);
    }
}
