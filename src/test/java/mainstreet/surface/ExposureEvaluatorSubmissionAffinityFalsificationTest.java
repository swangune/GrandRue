package mainstreet.surface;

import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExposureEvaluatorSubmissionAffinityFalsificationTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final String RELEASE = "semantic-registry-1.0";
    private static final Instant EVALUATED_AT = Instant.parse("2026-09-02T12:00:00Z");

    @Test
    void stale_requirement_binding_from_prior_observation_request_is_structural() {
        ExposureCandidateObservation candidate = singleton("profile", "public-display-name");
        ExposureRequirementReference requirement =
                new ExposureRequirementReference("data-protection", "public-readable");
        ExposureElementContract contract = contract(
                candidate.elementReference(),
                SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.singleton(),
                ExposureDecision.EXPOSE,
                Optional.empty(),
                Set.of(requirement)
        );
        RetainingRequirementEvaluator evaluator = new RetainingRequirementEvaluator();
        Invocation first = invocation();
        Invocation second = invocation();

        assertEquals(
                1,
                resolver().resolve(
                        first.context(), first.admission(), List.of(candidate),
                        contracts(RELEASE, contract),
                        requirements(
                                RELEASE,
                                new ExposureRequirementEvaluatorBinding(requirement, evaluator)
                        ),
                        choices(RELEASE)
                ).size()
        );

        assertThrows(
                ExposureResolutionStructuralException.class,
                () -> resolver().resolve(
                        second.context(), second.admission(), List.of(candidate),
                        contracts(RELEASE, contract),
                        requirements(
                                RELEASE,
                                new ExposureRequirementEvaluatorBinding(requirement, evaluator)
                        ),
                        choices(RELEASE)
                )
        );
        assertNotEquals(evaluator.firstBinding, evaluator.secondCurrentBinding);
    }

    @Test
    void stale_binding_from_prior_admission_invocation_of_same_request_is_structural() {
        ExposureCandidateObservation candidate = singleton("profile", "public-display-name");
        ExposureRequirementReference requirement =
                new ExposureRequirementReference("data-protection", "public-readable");
        ExposureElementContract contract = contract(
                candidate.elementReference(),
                SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.singleton(),
                ExposureDecision.EXPOSE,
                Optional.empty(),
                Set.of(requirement)
        );
        RetainingRequirementEvaluator evaluator = new RetainingRequirementEvaluator();
        SameRequestInvocations invocation = sameRequestInvocations();

        assertEquals(
                1,
                resolver().resolve(
                        invocation.context(), invocation.firstAdmission(), List.of(candidate),
                        contracts(RELEASE, contract),
                        requirements(
                                RELEASE,
                                new ExposureRequirementEvaluatorBinding(requirement, evaluator)
                        ),
                        choices(RELEASE)
                ).size()
        );

        assertThrows(
                ExposureResolutionStructuralException.class,
                () -> resolver().resolve(
                        invocation.context(), invocation.secondAdmission(), List.of(candidate),
                        contracts(RELEASE, contract),
                        requirements(
                                RELEASE,
                                new ExposureRequirementEvaluatorBinding(requirement, evaluator)
                        ),
                        choices(RELEASE)
                )
        );
        assertNotEquals(evaluator.firstBinding, evaluator.secondCurrentBinding);
    }

    @Test
    void requirement_binding_is_foreign_to_another_requirement_reference() {
        ExposureCandidateObservation candidate = singleton("profile", "public-display-name");
        ExposureRequirementReference first =
                new ExposureRequirementReference("a-owner", "a-rule");
        ExposureRequirementReference second =
                new ExposureRequirementReference("z-owner", "z-rule");
        ExposureElementContract contract = contract(
                candidate.elementReference(),
                SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.singleton(),
                ExposureDecision.EXPOSE,
                Optional.empty(),
                Set.of(first, second)
        );
        CrossReferenceRequirementEvaluator evaluator =
                new CrossReferenceRequirementEvaluator(first);
        Invocation invocation = invocation();

        assertThrows(
                ExposureResolutionStructuralException.class,
                () -> resolver().resolve(
                        invocation.context(), invocation.admission(), List.of(candidate),
                        contracts(RELEASE, contract),
                        requirements(
                                RELEASE,
                                new ExposureRequirementEvaluatorBinding(first, evaluator),
                                new ExposureRequirementEvaluatorBinding(second, evaluator)
                        ),
                        choices(RELEASE)
                )
        );
        assertNotEquals(evaluator.firstBinding, evaluator.secondCurrentBinding);
    }

    @Test
    void requirement_binding_is_foreign_to_merchant_choice_call() {
        ExposureCandidateObservation candidate = singleton("profile", "public-display-name");
        ExposureRequirementReference requirement =
                new ExposureRequirementReference("data-protection", "public-readable");
        MerchantExposureChoiceSourceReference choice =
                new MerchantExposureChoiceSourceReference(
                        "profile",
                        "public-display-name-choice"
                );
        ExposureElementContract contract = contract(
                candidate.elementReference(),
                SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.singleton(),
                ExposureDecision.WITHHOLD,
                Optional.of(choice),
                Set.of(requirement)
        );
        AtomicReference<ExposureCandidateEvaluationBinding> requirementBinding =
                new AtomicReference<>();
        AtomicReference<ExposureCandidateEvaluationBinding> choiceCurrentBinding =
                new AtomicReference<>();
        ExposureRequirementEvaluator requirementEvaluator =
                (reference, context, submissions) -> {
                    ExposureCandidateEvaluationBinding binding =
                            submissions.getFirst().evaluationBinding();
                    requirementBinding.set(binding);
                    return new ExposureRequirementBatchEvaluation(
                            reference,
                            List.of(new ExposureRequirementCandidateEvaluation(
                                    binding,
                                    ExposureRequirementEvaluationDecision.SATISFIED
                            ))
                    );
                };
        MerchantExposureChoiceEvaluator choiceEvaluator =
                (reference, context, submissions) -> {
                    choiceCurrentBinding.set(
                            submissions.getFirst().evaluationBinding()
                    );
                    return new MerchantExposureChoiceBatchEvaluation(
                            reference,
                            List.of(new MerchantExposureChoiceCandidateEvaluation(
                                    requirementBinding.get(),
                                    MerchantExposureChoiceEvaluationDecision.EXPOSE
                            ))
                    );
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
                                        requirement,
                                        requirementEvaluator
                                )
                        ),
                        choices(
                                RELEASE,
                                new MerchantExposureChoiceEvaluatorBinding(
                                        choice,
                                        choiceEvaluator
                                )
                        )
                )
        );
        assertNotEquals(requirementBinding.get(), choiceCurrentBinding.get());
    }

    @Test
    void reordered_complete_current_binding_results_are_accepted() {
        ExposureCandidateObservation c1 = instance(
                "profile", "public-contact-point", "contact-point", "C1"
        );
        ExposureCandidateObservation c2 = instance(
                "profile", "public-contact-point", "contact-point", "C2"
        );
        ExposureRequirementReference requirement =
                new ExposureRequirementReference("data-protection", "public-readable");
        ExposureElementContract contract = contract(
                c1.elementReference(),
                SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.instanceQualified(
                        new ExposureCandidateInstanceKindReference("profile", "contact-point")
                ),
                ExposureDecision.EXPOSE,
                Optional.empty(),
                Set.of(requirement)
        );
        ExposureRequirementEvaluator reversed =
                (reference, context, submissions) ->
                        new ExposureRequirementBatchEvaluation(
                                reference,
                                List.of(
                                        new ExposureRequirementCandidateEvaluation(
                                                submissions.get(1).evaluationBinding(),
                                                ExposureRequirementEvaluationDecision.SATISFIED
                                        ),
                                        new ExposureRequirementCandidateEvaluation(
                                                submissions.get(0).evaluationBinding(),
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
                        new ExposureRequirementEvaluatorBinding(requirement, reversed)
                ),
                choices(RELEASE)
        );

        assertEquals(2, resolved.size());
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
    void binding_issuance_is_surface_private_and_does_not_contaminate_context_candidate_or_membership() {
        assertFalse(Modifier.isPublic(
                ExposureCandidateEvaluationBindings.class.getModifiers()
        ));
        assertFalse(Modifier.isPublic(
                RuntimeExposureCandidateEvaluationBinding.class.getModifiers()
        ));
        assertEquals(
                Set.of(),
                Arrays.stream(ExposureCandidateEvaluationBinding.class.getMethods())
                        .filter(method -> method.getDeclaringClass()
                                == ExposureCandidateEvaluationBinding.class)
                        .map(java.lang.reflect.Method::getName)
                        .collect(java.util.stream.Collectors.toUnmodifiableSet())
        );
        assertTrue(
                Arrays.stream(OwnerExposureEvaluationContext.class.getMethods())
                        .noneMatch(method -> method.getReturnType()
                                == ExposureCandidateEvaluationBinding.class
                                || Arrays.asList(method.getParameterTypes())
                                .contains(ExposureCandidateEvaluationBinding.class))
        );
        assertTrue(
                Arrays.stream(ExposureCandidateObservation.class.getRecordComponents())
                        .noneMatch(component -> component.getType()
                                == ExposureCandidateEvaluationBinding.class)
        );
        assertTrue(
                Arrays.stream(ExposedElementMembership.class.getRecordComponents())
                        .noneMatch(component -> component.getType()
                                == ExposureCandidateEvaluationBinding.class)
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

    private static Invocation invocation() {
        EstablishedObservationRequest request =
                establishedRequest();
        AudienceObservationContext context =
                new DefaultAudienceObservationContext(
                        request,
                        new PublicObservationSubject()
                );
        AudienceObservationAdmissionResult admission = admitted(request);
        return new Invocation(context, admission);
    }

    private static SameRequestInvocations sameRequestInvocations() {
        EstablishedObservationRequest request = establishedRequest();
        AudienceObservationContext context =
                new DefaultAudienceObservationContext(
                        request,
                        new PublicObservationSubject()
                );
        return new SameRequestInvocations(
                context,
                admitted(request),
                admitted(request)
        );
    }

    private static EstablishedObservationRequest establishedRequest() {
        return new DefaultEstablishedObservationRequest(
                TestReleases.activeRelease(MERCHANT.merchantIdentifier(), RELEASE),
                MERCHANT,
                RELEASE,
                Optional.empty()
        );
    }

    private static AudienceObservationAdmissionResult admitted(
            EstablishedObservationRequest request
    ) {
        return AudienceObservationAdmissionResults.admitted(
                new DefaultAudienceObservationInvocationBinding(
                        EstablishedObservationRequestDetails.requestBinding(request)
                ),
                EVALUATED_AT
        );
    }

    private record Invocation(
            AudienceObservationContext context,
            AudienceObservationAdmissionResult admission
    ) {
    }

    private record SameRequestInvocations(
            AudienceObservationContext context,
            AudienceObservationAdmissionResult firstAdmission,
            AudienceObservationAdmissionResult secondAdmission
    ) {
    }

    private static final class RetainingRequirementEvaluator
            implements ExposureRequirementEvaluator {

        private ExposureCandidateEvaluationBinding firstBinding;
        private ExposureCandidateEvaluationBinding secondCurrentBinding;

        @Override
        public ExposureRequirementBatchEvaluation evaluateBatch(
                ExposureRequirementReference reference,
                OwnerExposureEvaluationContext context,
                List<ExposureCandidateEvaluationSubmission> submissions
        ) {
            ExposureCandidateEvaluationBinding current =
                    submissions.getFirst().evaluationBinding();
            if (firstBinding == null) {
                firstBinding = current;
                return new ExposureRequirementBatchEvaluation(
                        reference,
                        List.of(new ExposureRequirementCandidateEvaluation(
                                current,
                                ExposureRequirementEvaluationDecision.SATISFIED
                        ))
                );
            }
            secondCurrentBinding = current;
            return new ExposureRequirementBatchEvaluation(
                    reference,
                    List.of(new ExposureRequirementCandidateEvaluation(
                            firstBinding,
                            ExposureRequirementEvaluationDecision.SATISFIED
                    ))
            );
        }
    }

    private static final class CrossReferenceRequirementEvaluator
            implements ExposureRequirementEvaluator {

        private final ExposureRequirementReference firstReference;
        private ExposureCandidateEvaluationBinding firstBinding;
        private ExposureCandidateEvaluationBinding secondCurrentBinding;

        private CrossReferenceRequirementEvaluator(
                ExposureRequirementReference firstReference
        ) {
            this.firstReference = firstReference;
        }

        @Override
        public ExposureRequirementBatchEvaluation evaluateBatch(
                ExposureRequirementReference reference,
                OwnerExposureEvaluationContext context,
                List<ExposureCandidateEvaluationSubmission> submissions
        ) {
            ExposureCandidateEvaluationBinding current =
                    submissions.getFirst().evaluationBinding();
            if (reference.equals(firstReference)) {
                firstBinding = current;
                return new ExposureRequirementBatchEvaluation(
                        reference,
                        List.of(new ExposureRequirementCandidateEvaluation(
                                current,
                                ExposureRequirementEvaluationDecision.SATISFIED
                        ))
                );
            }
            secondCurrentBinding = current;
            return new ExposureRequirementBatchEvaluation(
                    reference,
                    List.of(new ExposureRequirementCandidateEvaluation(
                            firstBinding,
                            ExposureRequirementEvaluationDecision.SATISFIED
                    ))
            );
        }
    }
}
