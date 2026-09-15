package mainstreet.surface;

import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExposureResolverFalsificationTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final String RELEASE = "semantic-registry-1.0";
    private static final Instant EVALUATED_AT = Instant.parse("2026-09-02T12:00:00Z");

    @Test
    void baseline_withhold_without_merchant_choice_cannot_expose() {
        ExposureCandidateObservation candidate = singleton("profile", "public-display-name");
        ExposureElementContract contract = contract(
                candidate.elementReference(),
                SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.singleton(),
                ExposureDecision.WITHHOLD,
                Optional.empty(),
                Set.of()
        );
        Invocation invocation = invocation();

        assertEquals(
                List.of(),
                resolver().resolve(
                        invocation.context(), invocation.admission(), List.of(candidate),
                        contracts(RELEASE, contract), requirements(RELEASE), choices(RELEASE)
                )
        );
    }

    @Test
    void malformed_instance_identity_is_structural_before_audience_mismatch_can_withhold() {
        ExposableElementReference element =
                new ExposableElementReference("profile", "public-contact-point");
        ExposureElementContract customerOnly = contract(
                element,
                SurfaceAudience.CUSTOMER,
                ExposureMemberIdentitySpecification.instanceQualified(
                        new ExposureCandidateInstanceKindReference("profile", "contact-point")
                ),
                ExposureDecision.EXPOSE,
                Optional.empty(),
                Set.of()
        );
        Invocation invocation = invocation();

        ExposureCandidateObservation missingInstance =
                new ExposureCandidateObservation(element, Optional.empty());
        ExposureCandidateObservation wrongKind = new ExposureCandidateObservation(
                element,
                Optional.of(new ExposureCandidateInstanceReference(
                        "profile", "service-area", "C1"
                ))
        );

        for (ExposureCandidateObservation malformed : List.of(missingInstance, wrongKind)) {
            assertThrows(
                    ExposureResolutionStructuralException.class,
                    () -> resolver().resolve(
                            invocation.context(), invocation.admission(), List.of(malformed),
                            contracts(RELEASE, customerOnly),
                            requirements(RELEASE), choices(RELEASE)
                    )
            );
        }

        assertThrows(
                IllegalArgumentException.class,
                () -> new ExposureCandidateObservation(
                        element,
                        Optional.of(new ExposureCandidateInstanceReference(
                                "ordering", "contact-point", "C1"
                        ))
                )
        );
    }

    @Test
    void duplicate_instance_qualified_candidate_membership_is_structural() {
        ExposureCandidateObservation candidate = instance(
                "profile", "public-contact-point", "contact-point", "C1"
        );
        ExposureElementContract contract = contract(
                candidate.elementReference(),
                SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.instanceQualified(
                        new ExposureCandidateInstanceKindReference("profile", "contact-point")
                ),
                ExposureDecision.EXPOSE,
                Optional.empty(),
                Set.of()
        );
        Invocation invocation = invocation();

        assertThrows(
                ExposureResolutionStructuralException.class,
                () -> resolver().resolve(
                        invocation.context(), invocation.admission(),
                        List.of(candidate, candidate),
                        contracts(RELEASE, contract), requirements(RELEASE), choices(RELEASE)
                )
        );
    }

    @Test
    void malformed_requirement_batch_binding_foreign_duplicate_missing_and_exception_are_structural() {
        ExposureCandidateObservation c1 = instance(
                "profile", "public-contact-point", "contact-point", "C1"
        );
        ExposureCandidateObservation c2 = instance(
                "profile", "public-contact-point", "contact-point", "C2"
        );
        ExposureRequirementReference reference =
                new ExposureRequirementReference("data-protection", "public-readable");
        ExposureRequirementReference wrongReference =
                new ExposureRequirementReference("data-protection", "different-rule");
        ExposureElementContract contract = instanceContract(c1, Set.of(reference), Optional.empty());
        Invocation invocation = invocation();

        List<ExposureRequirementEvaluator> malformed = List.of(
                (requested, context, submissions) -> new ExposureRequirementBatchEvaluation(
                        wrongReference,
                        submissions.stream()
                                .map(submission -> new ExposureRequirementCandidateEvaluation(
                                        submission.evaluationBinding(),
                                        ExposureRequirementEvaluationDecision.SATISFIED
                                ))
                                .toList()
                ),
                (requested, context, submissions) -> new ExposureRequirementBatchEvaluation(
                        requested,
                        List.of(
                                new ExposureRequirementCandidateEvaluation(
                                        submissions.getFirst().evaluationBinding(),
                                        ExposureRequirementEvaluationDecision.SATISFIED
                                ),
                                new ExposureRequirementCandidateEvaluation(
                                        ExposureCandidateEvaluationBindings.issue(),
                                        ExposureRequirementEvaluationDecision.SATISFIED
                                )
                        )
                ),
                (requested, context, submissions) -> new ExposureRequirementBatchEvaluation(
                        requested,
                        List.of(
                                new ExposureRequirementCandidateEvaluation(
                                        submissions.getFirst().evaluationBinding(),
                                        ExposureRequirementEvaluationDecision.SATISFIED
                                ),
                                new ExposureRequirementCandidateEvaluation(
                                        submissions.getFirst().evaluationBinding(),
                                        ExposureRequirementEvaluationDecision.SATISFIED
                                )
                        )
                ),
                (requested, context, submissions) -> new ExposureRequirementBatchEvaluation(
                        requested,
                        List.of(new ExposureRequirementCandidateEvaluation(
                                submissions.getFirst().evaluationBinding(),
                                ExposureRequirementEvaluationDecision.SATISFIED
                        ))
                ),
                (requested, context, submissions) -> {
                    throw new IllegalStateException("unexpected evaluator failure");
                }
        );

        for (ExposureRequirementEvaluator evaluator : malformed) {
            assertThrows(
                    ExposureResolutionStructuralException.class,
                    () -> resolver().resolve(
                            invocation.context(), invocation.admission(), List.of(c1, c2),
                            contracts(RELEASE, contract),
                            requirements(
                                    RELEASE,
                                    new ExposureRequirementEvaluatorBinding(reference, evaluator)
                            ),
                            choices(RELEASE)
                    )
            );
        }
    }

    @Test
    void unsatisfied_requirement_cannot_hide_a_structural_failure_in_another_requirement_batch() {
        ExposureCandidateObservation candidate = singleton("profile", "public-display-name");
        ExposureRequirementReference first =
                new ExposureRequirementReference("a-owner", "a-unsatisfied");
        ExposureRequirementReference second =
                new ExposureRequirementReference("z-owner", "z-malformed");
        ExposureElementContract contract = contract(
                candidate.elementReference(),
                SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.singleton(),
                ExposureDecision.EXPOSE,
                Optional.empty(),
                Set.of(first, second)
        );
        ExposureRequirementEvaluator malformed = (reference, context, submissions) -> {
            throw new IllegalStateException("second requirement failed structurally");
        };
        Invocation invocation = invocation();

        assertThrows(
                ExposureResolutionStructuralException.class,
                () -> resolver().resolve(
                        invocation.context(), invocation.admission(), List.of(candidate),
                        contracts(RELEASE, contract),
                        requirements(
                                RELEASE,
                                new ExposureRequirementEvaluatorBinding(
                                        first,
                                        constantRequirement(
                                                ExposureRequirementEvaluationDecision.UNSATISFIED
                                        )
                                ),
                                new ExposureRequirementEvaluatorBinding(second, malformed)
                        ),
                        choices(RELEASE)
                )
        );
    }

    @Test
    void merchant_choice_withhold_unresolved_or_missing_evaluator_all_withhold() {
        ExposureCandidateObservation candidate = singleton("profile", "public-display-name");
        MerchantExposureChoiceSourceReference choice =
                new MerchantExposureChoiceSourceReference(
                        "profile", "public-display-name-choice"
                );
        ExposureElementContract contract = contract(
                candidate.elementReference(),
                SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.singleton(),
                ExposureDecision.EXPOSE,
                Optional.of(choice),
                Set.of()
        );
        Invocation invocation = invocation();

        assertEquals(
                List.of(),
                resolver().resolve(
                        invocation.context(), invocation.admission(), List.of(candidate),
                        contracts(RELEASE, contract), requirements(RELEASE), choices(RELEASE)
                )
        );

        for (MerchantExposureChoiceEvaluationDecision decision : List.of(
                MerchantExposureChoiceEvaluationDecision.WITHHOLD,
                MerchantExposureChoiceEvaluationDecision.UNRESOLVED
        )) {
            assertEquals(
                    List.of(),
                    resolver().resolve(
                            invocation.context(), invocation.admission(), List.of(candidate),
                            contracts(RELEASE, contract), requirements(RELEASE),
                            choices(
                                    RELEASE,
                                    new MerchantExposureChoiceEvaluatorBinding(
                                            choice, constantChoice(decision)
                                    )
                            )
                    )
            );
        }
    }

    @Test
    void malformed_choice_batch_binding_foreign_duplicate_missing_and_exception_are_structural() {
        ExposureCandidateObservation c1 = instance(
                "profile", "public-contact-point", "contact-point", "C1"
        );
        ExposureCandidateObservation c2 = instance(
                "profile", "public-contact-point", "contact-point", "C2"
        );
        MerchantExposureChoiceSourceReference choice =
                new MerchantExposureChoiceSourceReference(
                        "profile", "contact-point-public-exposure"
                );
        MerchantExposureChoiceSourceReference wrongChoice =
                new MerchantExposureChoiceSourceReference(
                        "profile", "merchant-location-public-exposure"
                );
        ExposureElementContract contract = instanceContract(c1, Set.of(), Optional.of(choice));
        Invocation invocation = invocation();

        List<MerchantExposureChoiceEvaluator> malformed = List.of(
                (requested, context, submissions) -> new MerchantExposureChoiceBatchEvaluation(
                        wrongChoice,
                        submissions.stream()
                                .map(submission -> new MerchantExposureChoiceCandidateEvaluation(
                                        submission.evaluationBinding(),
                                        MerchantExposureChoiceEvaluationDecision.EXPOSE
                                ))
                                .toList()
                ),
                (requested, context, submissions) -> new MerchantExposureChoiceBatchEvaluation(
                        requested,
                        List.of(
                                new MerchantExposureChoiceCandidateEvaluation(
                                        submissions.getFirst().evaluationBinding(),
                                        MerchantExposureChoiceEvaluationDecision.EXPOSE
                                ),
                                new MerchantExposureChoiceCandidateEvaluation(
                                        ExposureCandidateEvaluationBindings.issue(),
                                        MerchantExposureChoiceEvaluationDecision.EXPOSE
                                )
                        )
                ),
                (requested, context, submissions) -> new MerchantExposureChoiceBatchEvaluation(
                        requested,
                        List.of(
                                new MerchantExposureChoiceCandidateEvaluation(
                                        submissions.getFirst().evaluationBinding(),
                                        MerchantExposureChoiceEvaluationDecision.EXPOSE
                                ),
                                new MerchantExposureChoiceCandidateEvaluation(
                                        submissions.getFirst().evaluationBinding(),
                                        MerchantExposureChoiceEvaluationDecision.EXPOSE
                                )
                        )
                ),
                (requested, context, submissions) -> new MerchantExposureChoiceBatchEvaluation(
                        requested,
                        List.of(new MerchantExposureChoiceCandidateEvaluation(
                                submissions.getFirst().evaluationBinding(),
                                MerchantExposureChoiceEvaluationDecision.EXPOSE
                        ))
                ),
                (requested, context, submissions) -> {
                    throw new IllegalStateException("unexpected choice evaluator failure");
                }
        );

        for (MerchantExposureChoiceEvaluator evaluator : malformed) {
            assertThrows(
                    ExposureResolutionStructuralException.class,
                    () -> resolver().resolve(
                            invocation.context(), invocation.admission(), List.of(c1, c2),
                            contracts(RELEASE, contract), requirements(RELEASE),
                            choices(
                                    RELEASE,
                                    new MerchantExposureChoiceEvaluatorBinding(choice, evaluator)
                            )
                    )
            );
        }
    }

    @Test
    void requirement_gate_filters_choice_batch_to_only_still_eligible_candidates() {
        ExposureCandidateObservation c1 = instance(
                "profile", "public-contact-point", "contact-point", "C1"
        );
        ExposureCandidateObservation c2 = instance(
                "profile", "public-contact-point", "contact-point", "C2"
        );
        ExposureRequirementReference requirement =
                new ExposureRequirementReference("data-protection", "public-readable");
        MerchantExposureChoiceSourceReference choice =
                new MerchantExposureChoiceSourceReference(
                        "profile", "contact-point-public-exposure"
                );
        ExposureElementContract contract = instanceContract(
                c1, Set.of(requirement), Optional.of(choice)
        );
        RecordingChoiceEvaluator choiceEvaluator = new RecordingChoiceEvaluator();
        ExposureRequirementEvaluator requirementEvaluator = (reference, context, submissions) ->
                new ExposureRequirementBatchEvaluation(
                        reference,
                        List.of(
                                new ExposureRequirementCandidateEvaluation(
                                        submissions.get(0).evaluationBinding(),
                                        ExposureRequirementEvaluationDecision.UNSATISFIED
                                ),
                                new ExposureRequirementCandidateEvaluation(
                                        submissions.get(1).evaluationBinding(),
                                        ExposureRequirementEvaluationDecision.SATISFIED
                                )
                        )
                );
        Invocation invocation = invocation();

        List<ResolvedExposedElement> resolved = resolver().resolve(
                invocation.context(), invocation.admission(), List.of(c1, c2),
                contracts(RELEASE, contract),
                requirements(
                        RELEASE,
                        new ExposureRequirementEvaluatorBinding(requirement, requirementEvaluator)
                ),
                choices(
                        RELEASE,
                        new MerchantExposureChoiceEvaluatorBinding(choice, choiceEvaluator)
                )
        );

        assertEquals(List.of(c2), choiceEvaluator.lastCandidates);
        assertEquals(1, resolved.size());
        assertEquals(
                new ExposedElementMembership(c2.elementReference(), c2.instanceReference()),
                resolved.getFirst().membership()
        );
    }

    @Test
    void every_exact_release_snapshot_must_match_the_established_request_release() {
        ExposureCandidateObservation candidate = singleton("profile", "public-display-name");
        ExposureElementContract contract = contract(
                candidate.elementReference(),
                SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.singleton(),
                ExposureDecision.EXPOSE,
                Optional.empty(),
                Set.of()
        );
        Invocation invocation = invocation();

        assertThrows(
                ExposureResolutionStructuralException.class,
                () -> resolver().resolve(
                        invocation.context(), invocation.admission(), List.of(candidate),
                        contracts(RELEASE, contract),
                        requirements("semantic-registry-2.0"),
                        choices(RELEASE)
                )
        );
        assertThrows(
                ExposureResolutionStructuralException.class,
                () -> resolver().resolve(
                        invocation.context(), invocation.admission(), List.of(candidate),
                        contracts(RELEASE, contract),
                        requirements(RELEASE),
                        choices("semantic-registry-2.0")
                )
        );
    }

    private static ExposureResolver resolver() {
        return new ExposureResolver();
    }

    private static ExposureElementContractRegistrySnapshot contracts(
            String release,
            ExposureElementContract... contracts
    ) {
        return new ExposureElementContractRegistrySnapshot(release, Set.of(contracts));
    }

    private static ExposureRequirementEvaluatorBindingSnapshot requirements(
            String release,
            ExposureRequirementEvaluatorBinding... bindings
    ) {
        return new ExposureRequirementEvaluatorBindingSnapshot(release, List.of(bindings));
    }

    private static MerchantExposureChoiceEvaluatorBindingSnapshot choices(
            String release,
            MerchantExposureChoiceEvaluatorBinding... bindings
    ) {
        return new MerchantExposureChoiceEvaluatorBindingSnapshot(release, List.of(bindings));
    }

    private static ExposureElementContract instanceContract(
            ExposureCandidateObservation candidate,
            Set<ExposureRequirementReference> requirements,
            Optional<MerchantExposureChoiceSourceReference> choice
    ) {
        return contract(
                candidate.elementReference(),
                SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.instanceQualified(
                        new ExposureCandidateInstanceKindReference("profile", "contact-point")
                ),
                ExposureDecision.EXPOSE,
                choice,
                requirements
        );
    }

    private static ExposureElementContract contract(
            ExposableElementReference element,
            SurfaceAudience audience,
            ExposureMemberIdentitySpecification identitySpecification,
            ExposureDecision baseline,
            Optional<MerchantExposureChoiceSourceReference> choice,
            Set<ExposureRequirementReference> requirements
    ) {
        return new ExposureElementContract(
                new ExposureElementContractIdentity(
                        element.ownerIdentifier(),
                        element.elementIdentifier() + "-" + audience.name().toLowerCase()
                ),
                element,
                audience,
                identitySpecification,
                baseline,
                choice,
                requirements
        );
    }

    private static ExposureCandidateObservation singleton(String owner, String element) {
        return new ExposureCandidateObservation(
                new ExposableElementReference(owner, element), Optional.empty()
        );
    }

    private static ExposureCandidateObservation instance(
            String owner,
            String element,
            String kind,
            String instance
    ) {
        return new ExposureCandidateObservation(
                new ExposableElementReference(owner, element),
                Optional.of(new ExposureCandidateInstanceReference(owner, kind, instance))
        );
    }

    private static ExposureRequirementEvaluator constantRequirement(
            ExposureRequirementEvaluationDecision decision
    ) {
        return (reference, context, submissions) ->
                new ExposureRequirementBatchEvaluation(
                        reference,
                        submissions.stream()
                                .map(submission -> new ExposureRequirementCandidateEvaluation(
                                        submission.evaluationBinding(),
                                        decision
                                ))
                                .toList()
                );
    }

    private static MerchantExposureChoiceEvaluator constantChoice(
            MerchantExposureChoiceEvaluationDecision decision
    ) {
        return (reference, context, submissions) ->
                new MerchantExposureChoiceBatchEvaluation(
                        reference,
                        submissions.stream()
                                .map(submission -> new MerchantExposureChoiceCandidateEvaluation(
                                        submission.evaluationBinding(),
                                        decision
                                ))
                                .toList()
                );
    }

    private static Invocation invocation() {
        EstablishedObservationRequest request =
                new DefaultEstablishedObservationRequest(
                        TestReleases.activeRelease(MERCHANT.merchantIdentifier(), RELEASE),
                        MERCHANT,
                        RELEASE,
                        Optional.empty()
                );
        AudienceObservationContext context =
                new DefaultAudienceObservationContext(
                        request,
                        new PublicObservationSubject()
                );
        AudienceObservationAdmissionResult admission =
                AudienceObservationAdmissionResults.admitted(
                        new DefaultAudienceObservationInvocationBinding(
                                EstablishedObservationRequestDetails.requestBinding(request)
                        ),
                        EVALUATED_AT
                );
        return new Invocation(context, admission);
    }

    private record Invocation(
            AudienceObservationContext context,
            AudienceObservationAdmissionResult admission
    ) {
    }

    private static final class RecordingChoiceEvaluator
            implements MerchantExposureChoiceEvaluator {
        private List<ExposureCandidateObservation> lastCandidates = List.of();

        @Override
        public MerchantExposureChoiceBatchEvaluation evaluateBatch(
                MerchantExposureChoiceSourceReference reference,
                OwnerExposureEvaluationContext context,
                List<ExposureCandidateEvaluationSubmission> submissions
        ) {
            lastCandidates = submissions.stream()
                    .map(ExposureCandidateEvaluationSubmission::candidate)
                    .toList();
            return constantChoice(MerchantExposureChoiceEvaluationDecision.EXPOSE)
                    .evaluateBatch(reference, context, submissions);
        }
    }
}
