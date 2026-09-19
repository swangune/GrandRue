package grandrue.surface;

import grandrue.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExposureResolverTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final String RELEASE = "semantic-registry-1.0";
    private static final Instant EVALUATED_AT = Instant.parse("2026-09-02T12:00:00Z");

    @Test
    void baseline_expose_produces_positive_membership_but_missing_audience_variant_withholds() {
        ExposureCandidateObservation candidate = singleton("profile", "public-display-name");
        ExposureElementContract publicContract = contract(
                candidate.elementReference(), SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.singleton(), ExposureDecision.EXPOSE,
                Optional.empty(), Set.of()
        );
        Invocation invocation = invocation();

        List<ResolvedExposedElement> exposed = resolver().resolve(
                invocation.context(), invocation.admission(), List.of(candidate),
                contracts(RELEASE, publicContract), requirements(RELEASE), choices(RELEASE)
        );
        assertEquals(1, exposed.size());
        assertEquals(
                new ExposedElementMembership(candidate.elementReference(), Optional.empty()),
                exposed.getFirst().membership()
        );
        assertEquals(publicContract.identity(), exposed.getFirst().contractIdentity());

        ExposureElementContract customerOnly = contract(
                candidate.elementReference(), SurfaceAudience.CUSTOMER,
                ExposureMemberIdentitySpecification.singleton(), ExposureDecision.EXPOSE,
                Optional.empty(), Set.of()
        );
        assertEquals(
                List.of(),
                resolver().resolve(
                        invocation.context(), invocation.admission(), List.of(candidate),
                        contracts(RELEASE, customerOnly), requirements(RELEASE), choices(RELEASE)
                )
        );
    }

    @Test
    void unknown_element_and_invalid_instance_identity_are_structural() {
        ExposureCandidateObservation known = singleton("profile", "public-display-name");
        ExposureCandidateObservation unknown = singleton("profile", "public-tagline");
        Invocation invocation = invocation();
        ExposureElementContract knownContract = contract(
                known.elementReference(), SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.singleton(), ExposureDecision.EXPOSE,
                Optional.empty(), Set.of()
        );

        assertThrows(
                ExposureResolutionStructuralException.class,
                () -> resolver().resolve(
                        invocation.context(), invocation.admission(), List.of(unknown),
                        contracts(RELEASE, knownContract), requirements(RELEASE), choices(RELEASE)
                )
        );

        ExposureCandidateObservation missingInstance =
                singleton("profile", "public-contact-point");
        ExposureElementContract instanceContract = contract(
                missingInstance.elementReference(), SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.instanceQualified(
                        new ExposureCandidateInstanceKindReference("profile", "contact-point")
                ),
                ExposureDecision.EXPOSE, Optional.empty(), Set.of()
        );
        assertThrows(
                ExposureResolutionStructuralException.class,
                () -> resolver().resolve(
                        invocation.context(), invocation.admission(), List.of(missingInstance),
                        contracts(RELEASE, instanceContract), requirements(RELEASE), choices(RELEASE)
                )
        );
    }

    @Test
    void singleton_duplicate_ingress_is_structural_even_with_different_evaluation_instances() {
        ExposableElementReference element =
                new ExposableElementReference("profile", "public-display-name");
        ExposureCandidateObservation first = new ExposureCandidateObservation(
                element,
                Optional.of(new ExposureCandidateInstanceReference(
                        "profile", "display-name-evaluation", "A"
                ))
        );
        ExposureCandidateObservation second = new ExposureCandidateObservation(
                element,
                Optional.of(new ExposureCandidateInstanceReference(
                        "profile", "display-name-evaluation", "B"
                ))
        );
        Invocation invocation = invocation();

        assertThrows(
                ExposureResolutionStructuralException.class,
                () -> resolver().resolve(
                        invocation.context(), invocation.admission(), List.of(first, second),
                        contracts(
                                RELEASE,
                                contract(
                                        element, SurfaceAudience.PUBLIC,
                                        ExposureMemberIdentitySpecification.singleton(),
                                        ExposureDecision.EXPOSE, Optional.empty(), Set.of()
                                )
                        ),
                        requirements(RELEASE), choices(RELEASE)
                )
        );
    }

    @Test
    void distinct_instance_members_survive_and_one_requirement_binding_is_batched_once() {
        ExposureCandidateObservation c1 = instance(
                "profile", "public-contact-point", "contact-point", "C1"
        );
        ExposureCandidateObservation c2 = instance(
                "profile", "public-contact-point", "contact-point", "C2"
        );
        ExposureRequirementReference requirement =
                new ExposureRequirementReference("data-protection", "public-readable");
        CountingRequirementEvaluator evaluator = new CountingRequirementEvaluator();
        ExposureElementContract contract = contract(
                c1.elementReference(), SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.instanceQualified(
                        new ExposureCandidateInstanceKindReference("profile", "contact-point")
                ),
                ExposureDecision.EXPOSE, Optional.empty(), Set.of(requirement)
        );
        Invocation invocation = invocation();

        List<ResolvedExposedElement> resolved = resolver().resolve(
                invocation.context(), invocation.admission(), List.of(c1, c2),
                contracts(RELEASE, contract),
                requirements(
                        RELEASE,
                        new ExposureRequirementEvaluatorBinding(requirement, evaluator)
                ),
                choices(RELEASE)
        );

        assertEquals(2, resolved.size());
        assertEquals(1, evaluator.calls);
        assertEquals(List.of(c1, c2), evaluator.lastCandidates);
        assertEquals("data-protection", evaluator.lastOwner);
        assertEquals(
                Set.of(
                        new ExposedElementMembership(c1.elementReference(), c1.instanceReference()),
                        new ExposedElementMembership(c2.elementReference(), c2.instanceReference())
                ),
                resolved.stream()
                        .map(ResolvedExposedElement::membership)
                        .collect(java.util.stream.Collectors.toUnmodifiableSet())
        );
    }

    @Test
    void failed_requirement_withholds_before_choice_but_choice_expose_can_broaden_baseline() {
        ExposureCandidateObservation candidate = singleton("profile", "public-display-name");
        ExposureRequirementReference requirement =
                new ExposureRequirementReference("data-protection", "public-readable");
        MerchantExposureChoiceSourceReference choice =
                new MerchantExposureChoiceSourceReference("profile", "public-display-name-choice");
        CountingChoiceEvaluator choiceEvaluator = new CountingChoiceEvaluator();
        ExposureElementContract contract = contract(
                candidate.elementReference(), SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.singleton(), ExposureDecision.WITHHOLD,
                Optional.of(choice), Set.of(requirement)
        );
        Invocation invocation = invocation();

        assertEquals(
                List.of(),
                resolver().resolve(
                        invocation.context(), invocation.admission(), List.of(candidate),
                        contracts(RELEASE, contract),
                        requirements(
                                RELEASE,
                                new ExposureRequirementEvaluatorBinding(
                                        requirement,
                                        constantRequirement(
                                                ExposureRequirementEvaluationDecision.UNSATISFIED
                                        )
                                )
                        ),
                        choices(
                                RELEASE,
                                new MerchantExposureChoiceEvaluatorBinding(choice, choiceEvaluator)
                        )
                )
        );
        assertEquals(0, choiceEvaluator.calls);

        List<ResolvedExposedElement> broadened = resolver().resolve(
                invocation.context(), invocation.admission(), List.of(candidate),
                contracts(RELEASE, contract),
                requirements(
                        RELEASE,
                        new ExposureRequirementEvaluatorBinding(
                                requirement,
                                constantRequirement(ExposureRequirementEvaluationDecision.SATISFIED)
                        )
                ),
                choices(
                        RELEASE,
                        new MerchantExposureChoiceEvaluatorBinding(choice, choiceEvaluator)
                )
        );
        assertEquals(1, broadened.size());
        assertEquals(1, choiceEvaluator.calls);
    }

    @Test
    void missing_or_unresolved_evaluator_withholds_without_structural_failure() {
        ExposureCandidateObservation candidate = singleton("profile", "public-display-name");
        ExposureRequirementReference requirement =
                new ExposureRequirementReference("data-protection", "public-readable");
        ExposureElementContract contract = contract(
                candidate.elementReference(), SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.singleton(), ExposureDecision.EXPOSE,
                Optional.empty(), Set.of(requirement)
        );
        Invocation invocation = invocation();

        assertEquals(
                List.of(),
                resolver().resolve(
                        invocation.context(), invocation.admission(), List.of(candidate),
                        contracts(RELEASE, contract), requirements(RELEASE), choices(RELEASE)
                )
        );
        assertEquals(
                List.of(),
                resolver().resolve(
                        invocation.context(), invocation.admission(), List.of(candidate),
                        contracts(RELEASE, contract),
                        requirements(
                                RELEASE,
                                new ExposureRequirementEvaluatorBinding(
                                        requirement,
                                        constantRequirement(ExposureRequirementEvaluationDecision.UNRESOLVED)
                                )
                        ),
                        choices(RELEASE)
                )
        );
    }

    @Test
    void malformed_batch_coverage_is_structural_not_partial_success() {
        ExposureCandidateObservation c1 = instance(
                "profile", "public-contact-point", "contact-point", "C1"
        );
        ExposureCandidateObservation c2 = instance(
                "profile", "public-contact-point", "contact-point", "C2"
        );
        ExposureRequirementReference requirement =
                new ExposureRequirementReference("data-protection", "public-readable");
        ExposureRequirementEvaluator malformed = (reference, context, submissions) ->
                new ExposureRequirementBatchEvaluation(
                        reference,
                        List.of(new ExposureRequirementCandidateEvaluation(
                                submissions.getFirst().evaluationBinding(),
                                ExposureRequirementEvaluationDecision.SATISFIED
                        ))
                );
        ExposureElementContract contract = contract(
                c1.elementReference(), SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.instanceQualified(
                        new ExposureCandidateInstanceKindReference("profile", "contact-point")
                ),
                ExposureDecision.EXPOSE, Optional.empty(), Set.of(requirement)
        );
        Invocation invocation = invocation();

        assertThrows(
                ExposureResolutionStructuralException.class,
                () -> resolver().resolve(
                        invocation.context(), invocation.admission(), List.of(c1, c2),
                        contracts(RELEASE, contract),
                        requirements(
                                RELEASE,
                                new ExposureRequirementEvaluatorBinding(requirement, malformed)
                        ),
                        choices(RELEASE)
                )
        );
    }

    @Test
    void rejected_or_request_mismatched_admission_and_release_mismatch_are_structural() {
        Invocation invocation = invocation();
        ExposureCandidateObservation candidate = singleton("profile", "public-display-name");
        ExposureElementContract contract = contract(
                candidate.elementReference(), SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.singleton(), ExposureDecision.EXPOSE,
                Optional.empty(), Set.of()
        );
        AudienceObservationAdmissionResult rejected =
                AudienceObservationAdmissionResults.rejected(
                        invocation.admission().invocationBinding(),
                        EVALUATED_AT,
                        AudienceObservationAdmissionFailure.UNSUPPORTED_AUDIENCE_MODE
                );

        assertThrows(
                ExposureResolutionStructuralException.class,
                () -> resolver().resolve(
                        invocation.context(), rejected, List.of(candidate),
                        contracts(RELEASE, contract), requirements(RELEASE), choices(RELEASE)
                )
        );
        assertThrows(
                ExposureResolutionStructuralException.class,
                () -> resolver().resolve(
                        invocation.context(), invocation.admission(), List.of(candidate),
                        contracts("semantic-registry-2.0", contract),
                        requirements(RELEASE), choices(RELEASE)
                )
        );

        Invocation other = invocation();
        AudienceObservationAdmissionResult wrongRequestAdmission =
                AudienceObservationAdmissionResults.admitted(
                        other.admission().invocationBinding(), EVALUATED_AT
                );
        assertThrows(
                ExposureResolutionStructuralException.class,
                () -> resolver().resolve(
                        invocation.context(), wrongRequestAdmission, List.of(candidate),
                        contracts(RELEASE, contract), requirements(RELEASE), choices(RELEASE)
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

    private static final class CountingRequirementEvaluator
            implements ExposureRequirementEvaluator {
        private int calls;
        private List<ExposureCandidateObservation> lastCandidates = List.of();
        private String lastOwner;

        @Override
        public ExposureRequirementBatchEvaluation evaluateBatch(
                ExposureRequirementReference reference,
                OwnerExposureEvaluationContext context,
                List<ExposureCandidateEvaluationSubmission> submissions
        ) {
            calls++;
            lastCandidates = submissions.stream()
                    .map(ExposureCandidateEvaluationSubmission::candidate)
                    .toList();
            lastOwner = reference.ownerIdentifier();
            return constantRequirement(ExposureRequirementEvaluationDecision.SATISFIED)
                    .evaluateBatch(reference, context, submissions);
        }
    }

    private static final class CountingChoiceEvaluator
            implements MerchantExposureChoiceEvaluator {
        private int calls;

        @Override
        public MerchantExposureChoiceBatchEvaluation evaluateBatch(
                MerchantExposureChoiceSourceReference reference,
                OwnerExposureEvaluationContext context,
                List<ExposureCandidateEvaluationSubmission> submissions
        ) {
            calls++;
            return new MerchantExposureChoiceBatchEvaluation(
                    reference,
                    submissions.stream()
                            .map(submission -> new MerchantExposureChoiceCandidateEvaluation(
                                    submission.evaluationBinding(),
                                    MerchantExposureChoiceEvaluationDecision.EXPOSE
                            ))
                            .toList()
            );
        }
    }
}
