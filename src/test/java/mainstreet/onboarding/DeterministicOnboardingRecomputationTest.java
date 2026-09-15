package mainstreet.onboarding;

import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * MS-PROT-052 / v1.2 deterministic candidate and unresolved-frontier recomputation.
 */
class DeterministicOnboardingRecomputationTest {

    private static final Instant NOW = Instant.parse("2026-08-28T23:30:00Z");
    private static final OnboardingQuestionDefinitionVersion V1 =
            new OnboardingQuestionDefinitionVersion("v1");
    private static final OnboardingQuestionDefinitionVersion V2 =
            new OnboardingQuestionDefinitionVersion("v2");
    private static final OnboardingSemanticSeed SCHEDULING =
            new OnboardingSemanticSeed("mainstreet.semantic", "scheduling");

    @Test
    void current_initial_discovery_answer_proposes_only_registered_candidate_seeds() {
        DeterministicOnboardingRecomputation recomputation =
                new DeterministicOnboardingRecomputation(
                        InitialOnboardingPromptCatalogue.registry()
                );

        OnboardingRecomputation result = recomputation.recompute(
                caseAt("revision-2"),
                List.of(answer(
                        "answer-1",
                        InitialCustomerInteractionDiscoveryQuestion.identity(),
                        V2,
                        Optional.empty(),
                        Set.of("SEND_ENQUIRY", "ARRANGE_APPOINTMENT"),
                        "revision-2"
                ))
        );

        assertEquals(
                Set.of(
                        new OnboardingSemanticSeed(
                                "mainstreet.semantic",
                                "enquiry"
                        ),
                        SCHEDULING
                ),
                result.candidateSemanticSeeds()
        );
        assertEquals(List.of(), result.unresolvedFrontier());
        assertEquals(1, result.candidateEvidence().size());
        assertEquals(List.of(), result.excludedEvidence());
    }

    @Test
    void contradictory_no_additional_interaction_answer_remains_unresolved() {
        OnboardingAnswerEvidenceRevision contradictory = answer(
                "contradictory-answer",
                InitialCustomerInteractionDiscoveryQuestion.identity(),
                V2,
                Optional.empty(),
                Set.of("NOTHING_ELSE_FOR_NOW", "SEND_ENQUIRY"),
                "revision-2"
        );

        OnboardingRecomputation result =
                new DeterministicOnboardingRecomputation(
                        InitialOnboardingPromptCatalogue.registry()
                ).recompute(caseAt("revision-2"), List.of(contradictory));

        assertEquals(Set.of(), result.candidateSemanticSeeds());
        assertEquals(
                List.of(InitialOnboardingPromptCatalogue
                        .initialCustomerInteractionDefinition()),
                result.unresolvedFrontier()
        );
        assertEquals(List.of(contradictory), result.excludedEvidence());
    }

    @Test
    void correction_prunes_stale_branch_answer_from_candidate_influence() {
        OnboardingQuestionIdentity schedulingMode =
                question("scheduling-authority-mode");
        OnboardingPromptRegistry registry = extendInitialRegistry(
                List.of(definition(
                        schedulingMode,
                        OnboardingPromptPriority
                                .MANDATORY_CONFIGURATION_DECISION,
                        Set.of("CUSTOMER_SELECTS", "MERCHANT_CONFIRMS"),
                        OnboardingPromptApplicability.requiringAll(
                                Set.of(SCHEDULING)
                        )
                )),
                List.of()
        );
        DeterministicOnboardingRecomputation recomputation =
                new DeterministicOnboardingRecomputation(registry);

        OnboardingAnswerEvidenceRevision correctedRoot = answer(
                "answer-root-2",
                InitialCustomerInteractionDiscoveryQuestion.identity(),
                V2,
                Optional.empty(),
                Set.of("NOTHING_ELSE_FOR_NOW"),
                "revision-3"
        );
        OnboardingAnswerEvidenceRevision staleBranch = answer(
                "answer-scheduling-1",
                schedulingMode,
                V1,
                Optional.empty(),
                Set.of("CUSTOMER_SELECTS"),
                "revision-2"
        );

        OnboardingRecomputation result = recomputation.recompute(
                caseAt("revision-3"),
                List.of(correctedRoot, staleBranch)
        );

        assertEquals(Set.of(), result.candidateSemanticSeeds());
        assertEquals(List.of(), result.unresolvedFrontier());
        assertEquals(List.of(correctedRoot), result.candidateEvidence());
        assertEquals(List.of(staleBranch), result.excludedEvidence());
    }

    @Test
    void independently_valid_seed_keeps_shared_branch_answer_effective() {
        OnboardingQuestionIdentity independent =
                question("independent-scheduling-discovery");
        OnboardingQuestionIdentity schedulingMode =
                question("scheduling-authority-mode");
        OnboardingPromptDefinition independentRoot = definition(
                independent,
                OnboardingPromptPriority.HIGH_BRANCH_DISCOVERY,
                Set.of("ENABLE"),
                OnboardingPromptApplicability.always()
        );
        OnboardingPromptDefinition scheduling = definition(
                schedulingMode,
                OnboardingPromptPriority.MANDATORY_CONFIGURATION_DECISION,
                Set.of("CUSTOMER_SELECTS"),
                OnboardingPromptApplicability.requiringAll(Set.of(SCHEDULING))
        );
        OnboardingDiscoveryMapping independentMapping =
                new OnboardingDiscoveryMapping(
                        independent,
                        V1,
                        "ENABLE",
                        Set.of(SCHEDULING)
                );
        DeterministicOnboardingRecomputation recomputation =
                new DeterministicOnboardingRecomputation(
                        extendInitialRegistry(
                                List.of(independentRoot, scheduling),
                                List.of(independentMapping)
                        )
                );

        OnboardingRecomputation result = recomputation.recompute(
                caseAt("revision-4"),
                List.of(
                        answer(
                                "answer-root",
                                InitialCustomerInteractionDiscoveryQuestion
                                        .identity(),
                                V2,
                                Optional.empty(),
                                Set.of("NOTHING_ELSE_FOR_NOW"),
                                "revision-2"
                        ),
                        answer(
                                "answer-independent",
                                independent,
                                V1,
                                Optional.empty(),
                                Set.of("ENABLE"),
                                "revision-3"
                        ),
                        answer(
                                "answer-scheduling",
                                schedulingMode,
                                V1,
                                Optional.empty(),
                                Set.of("CUSTOMER_SELECTS"),
                                "revision-4"
                        )
                )
        );

        assertEquals(Set.of(SCHEDULING), result.candidateSemanticSeeds());
        assertEquals(3, result.candidateEvidence().size());
        assertEquals(List.of(), result.excludedEvidence());
        assertEquals(List.of(), result.unresolvedFrontier());
    }

    @Test
    void historical_definition_is_not_silently_reinterpreted() {
        OnboardingAnswerEvidenceRevision historical = answer(
                "historical-v1",
                InitialCustomerInteractionDiscoveryQuestion.identity(),
                V1,
                Optional.empty(),
                Set.of("VIEW_BUSINESS_INFORMATION", "SEND_ENQUIRY"),
                "revision-1"
        );

        OnboardingRecomputation result =
                new DeterministicOnboardingRecomputation(
                        InitialOnboardingPromptCatalogue.registry()
                ).recompute(caseAt("revision-1"), List.of(historical));

        assertEquals(Set.of(), result.candidateSemanticSeeds());
        assertEquals(
                List.of(InitialOnboardingPromptCatalogue
                        .initialCustomerInteractionDefinition()),
                result.unresolvedFrontier()
        );
        assertEquals(List.of(), result.candidateEvidence());
        assertEquals(List.of(historical), result.excludedEvidence());
    }

    @Test
    void explicitly_registered_historical_interpretation_remains_usable() {
        OnboardingQuestionIdentity identity = question("compatible-question");
        OnboardingSemanticSeed seed =
                new OnboardingSemanticSeed("mainstreet.semantic", "compatible");
        OnboardingPromptDefinition definition =
                new OnboardingPromptDefinition(
                        new OnboardingPromptKey(identity, Optional.empty()),
                        V2,
                        OnboardingPromptClass.DISCOVERY_QUESTION,
                        OnboardingAnswerForm.MULTI_SELECT,
                        OnboardingPromptPriority.HIGH_BRANCH_DISCOVERY,
                        Map.of(
                                V1, Set.of("LEGACY_YES"),
                                V2, Set.of("YES")
                        ),
                        OnboardingPromptApplicability.always()
                );
        OnboardingPromptRegistry registry = new OnboardingPromptRegistry(
                List.of(definition),
                List.of(new OnboardingDiscoveryMapping(
                        identity,
                        V1,
                        "LEGACY_YES",
                        Set.of(seed)
                ))
        );

        OnboardingRecomputation result =
                new DeterministicOnboardingRecomputation(registry).recompute(
                        caseAt("revision-1"),
                        List.of(answer(
                                "legacy-answer",
                                identity,
                                V1,
                                Optional.empty(),
                                Set.of("LEGACY_YES"),
                                "revision-1"
                        ))
                );

        assertEquals(Set.of(seed), result.candidateSemanticSeeds());
        assertEquals(List.of(), result.unresolvedFrontier());
        assertEquals(1, result.candidateEvidence().size());
    }

    @Test
    void frontier_uses_governing_priority_then_registered_information_gain() {
        OnboardingQuestionIdentity broad = question("broad-discovery");
        OnboardingQuestionIdentity narrow = question("narrow-discovery");
        OnboardingQuestionIdentity mandatory = question("mandatory-root");
        OnboardingSemanticSeed broadSeed =
                new OnboardingSemanticSeed("test", "broad");
        OnboardingSemanticSeed narrowSeed =
                new OnboardingSemanticSeed("test", "narrow");

        OnboardingPromptDefinition broadDefinition = definition(
                broad,
                OnboardingPromptPriority.HIGH_BRANCH_DISCOVERY,
                Set.of("YES"),
                OnboardingPromptApplicability.always()
        );
        OnboardingPromptDefinition narrowDefinition = definition(
                narrow,
                OnboardingPromptPriority.HIGH_BRANCH_DISCOVERY,
                Set.of("YES"),
                OnboardingPromptApplicability.always()
        );
        OnboardingPromptDefinition mandatoryDefinition = definition(
                mandatory,
                OnboardingPromptPriority.MANDATORY_CONFIGURATION_DECISION,
                Set.of("YES"),
                OnboardingPromptApplicability.always()
        );
        OnboardingPromptDefinition broadChildOne = definition(
                question("broad-child-one"),
                OnboardingPromptPriority.REQUIRED_STRUCTURED_DATA,
                Set.of("DONE"),
                OnboardingPromptApplicability.requiringAll(Set.of(broadSeed))
        );
        OnboardingPromptDefinition broadChildTwo = definition(
                question("broad-child-two"),
                OnboardingPromptPriority.REQUIRED_STRUCTURED_DATA,
                Set.of("DONE"),
                OnboardingPromptApplicability.requiringAll(Set.of(broadSeed))
        );
        OnboardingPromptDefinition narrowChild = definition(
                question("narrow-child"),
                OnboardingPromptPriority.REQUIRED_STRUCTURED_DATA,
                Set.of("DONE"),
                OnboardingPromptApplicability.requiringAll(Set.of(narrowSeed))
        );

        OnboardingPromptRegistry registry = new OnboardingPromptRegistry(
                List.of(
                        broadDefinition,
                        narrowDefinition,
                        mandatoryDefinition,
                        broadChildOne,
                        broadChildTwo,
                        narrowChild
                ),
                List.of(
                        new OnboardingDiscoveryMapping(
                                broad,
                                V1,
                                "YES",
                                Set.of(broadSeed)
                        ),
                        new OnboardingDiscoveryMapping(
                                narrow,
                                V1,
                                "YES",
                                Set.of(narrowSeed)
                        )
                )
        );

        OnboardingRecomputation result =
                new DeterministicOnboardingRecomputation(registry)
                        .recompute(caseAt("revision-1"), List.of());

        assertEquals(
                List.of(
                        broadDefinition,
                        narrowDefinition,
                        mandatoryDefinition
                ),
                result.unresolvedFrontier()
        );
        assertEquals(Optional.of(broadDefinition), result.nextPrompt());
    }

    @Test
    void scoped_prompt_instances_require_distinct_context_answers() {
        OnboardingQuestionIdentity identity = question("location-hours");
        OnboardingPromptDefinition swansea = scopedDefinition(
                identity,
                "location-swansea"
        );
        OnboardingPromptDefinition cardiff = scopedDefinition(
                identity,
                "location-cardiff"
        );
        OnboardingPromptRegistry registry = new OnboardingPromptRegistry(
                List.of(swansea, cardiff),
                List.of()
        );

        OnboardingRecomputation result =
                new DeterministicOnboardingRecomputation(registry).recompute(
                        caseAt("revision-2"),
                        List.of(answer(
                                "swansea-hours",
                                identity,
                                V1,
                                Optional.of("location-swansea"),
                                Set.of("CAPTURED"),
                                "revision-2"
                        ))
                );

        assertEquals(List.of(cardiff), result.unresolvedFrontier());
        assertEquals(1, result.candidateEvidence().size());
    }

    private static OnboardingPromptRegistry extendInitialRegistry(
            List<OnboardingPromptDefinition> definitions,
            List<OnboardingDiscoveryMapping> mappings
    ) {
        OnboardingPromptRegistry initial =
                InitialOnboardingPromptCatalogue.registry();
        return new OnboardingPromptRegistry(
                Stream.concat(
                        initial.promptDefinitions().stream(),
                        definitions.stream()
                ).toList(),
                Stream.concat(
                        initial.discoveryMappings().stream(),
                        mappings.stream()
                ).toList()
        );
    }

    private static OnboardingPromptDefinition definition(
            OnboardingQuestionIdentity identity,
            OnboardingPromptPriority priority,
            Set<String> options,
            OnboardingPromptApplicability applicability
    ) {
        return new OnboardingPromptDefinition(
                new OnboardingPromptKey(identity, Optional.empty()),
                V1,
                OnboardingPromptClass.DISCOVERY_QUESTION,
                OnboardingAnswerForm.MULTI_SELECT,
                priority,
                Map.of(V1, options),
                applicability
        );
    }

    private static OnboardingPromptDefinition scopedDefinition(
            OnboardingQuestionIdentity identity,
            String scope
    ) {
        return new OnboardingPromptDefinition(
                new OnboardingPromptKey(identity, Optional.of(scope)),
                V1,
                OnboardingPromptClass.DATA_CAPTURE_PROMPT,
                OnboardingAnswerForm.MULTI_SELECT,
                OnboardingPromptPriority.REQUIRED_STRUCTURED_DATA,
                Map.of(V1, Set.of("CAPTURED")),
                OnboardingPromptApplicability.always()
        );
    }

    private static OnboardingAnswerEvidenceRevision answer(
            String evidenceIdentifier,
            OnboardingQuestionIdentity questionIdentity,
            OnboardingQuestionDefinitionVersion version,
            Optional<String> context,
            Set<String> options,
            String revision
    ) {
        return new OnboardingAnswerEvidenceRevision(
                evidenceIdentifier,
                new OnboardingCaseIdentity("case-1"),
                new OnboardingCaseRevision(revision),
                Optional.empty(),
                new OnboardingAnswerEvidence(
                        questionIdentity,
                        version,
                        OnboardingAnswerForm.MULTI_SELECT,
                        options,
                        Optional.empty(),
                        context,
                        OnboardingAnswerOrigin.MERCHANT_SELECTED,
                        "identity-42",
                        NOW,
                        Optional.of("answer-provenance-release")
                )
        );
    }

    private static OnboardingCase caseAt(String revision) {
        return new OnboardingCase(
                new OnboardingCaseIdentity("case-1"),
                new MerchantScope("merchant-acme"),
                OnboardingCaseLifecycle.IN_PROGRESS,
                new OnboardingCaseRevision(revision)
        );
    }

    private static OnboardingQuestionIdentity question(String identifier) {
        return new OnboardingQuestionIdentity("onboarding.test", identifier);
    }
}
