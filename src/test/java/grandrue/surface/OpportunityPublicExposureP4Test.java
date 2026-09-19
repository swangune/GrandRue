package grandrue.surface;

import grandrue.application.MerchantScope;
import grandrue.publication.OpportunityCalendarDateBoundary;
import grandrue.publication.OpportunityExactInstantBoundary;
import grandrue.publication.OpportunityExposureWindowRequirementEvaluator;
import grandrue.publication.OpportunityPublicExposureCandidateSource;
import grandrue.publication.OpportunityPublicExposureContractPortfolio;
import grandrue.publication.OpportunityPublicExposureEvidence;
import grandrue.publication.OpportunityPublicExposureReadPort;
import grandrue.publication.OpportunityPublicExposureReferences;
import grandrue.publication.OpportunityTemporalBoundary;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OpportunityPublicExposureP4Test {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final String RELEASE = "semantic-registry-1.0";
    private static final String OPPORTUNITY = "opportunity-1";

    @Test
    void registers_exact_public_opportunity_contract_without_absorbing_p5_or_p6() {
        ExposureElementContractRegistrySnapshot registry =
                OpportunityPublicExposureContractPortfolio.forRelease(RELEASE);

        assertEquals(1, registry.contracts().size());

        ExposureElementContract contract = registry.contract(
                        RELEASE,
                        OpportunityPublicExposureReferences.PUBLIC_OPPORTUNITY_REPRESENTATION,
                        SurfaceAudience.PUBLIC
                )
                .orElseThrow();

        assertEquals(
                new ExposureElementContractIdentity(
                        "publication",
                        "public-opportunity-representation"
                ),
                contract.identity()
        );
        assertEquals(
                ExposureMemberIdentitySpecification.instanceQualified(
                        OpportunityPublicExposureReferences.OPPORTUNITY_INSTANCE_KIND
                ),
                contract.memberIdentitySpecification()
        );
        assertEquals(ExposureDecision.EXPOSE, contract.baselineDecision());
        assertEquals(Optional.empty(), contract.merchantChoiceSource());
        assertEquals(
                Set.of(OpportunityPublicExposureReferences.EXPOSURE_WINDOW_REQUIREMENT),
                contract.requirementReferences()
        );
        assertTrue(registry.contracts().stream().noneMatch(candidate ->
                "public-opportunity-actionability".equals(
                        candidate.exposableElementReference().elementIdentifier()
                )
        ));
    }

    @Test
    void exact_instant_window_is_lower_inclusive_and_upper_exclusive() {
        Instant now = Instant.parse("2026-09-05T12:00:00Z");

        assertEquals(
                1,
                resolve(
                        now,
                        evidence(
                                Optional.of(new OpportunityExactInstantBoundary(now)),
                                Optional.of(new OpportunityExactInstantBoundary(
                                        now.plusSeconds(1)
                                ))
                        )
                ).size()
        );

        assertTrue(resolve(
                now,
                evidence(
                        Optional.of(new OpportunityExactInstantBoundary(
                                now.plusSeconds(1)
                        )),
                        Optional.empty()
                )
        ).isEmpty());

        assertTrue(resolve(
                now,
                evidence(
                        Optional.empty(),
                        Optional.of(new OpportunityExactInstantBoundary(now))
                )
        ).isEmpty());

        assertEquals(
                1,
                resolve(
                        now,
                        evidence(Optional.empty(), Optional.empty())
                ).size()
        );
    }

    @Test
    void calendar_date_publish_until_is_inclusive_for_the_recorded_local_date() {
        ZoneId london = ZoneId.of("Europe/London");
        OpportunityTemporalBoundary publishUntil =
                new OpportunityCalendarDateBoundary(
                        LocalDate.of(2026, 9, 5),
                        london
                );

        assertEquals(
                1,
                resolve(
                        Instant.parse("2026-09-05T22:59:59Z"),
                        evidence(Optional.empty(), Optional.of(publishUntil))
                ).size()
        );
        assertTrue(resolve(
                Instant.parse("2026-09-05T23:00:00Z"),
                evidence(Optional.empty(), Optional.of(publishUntil))
        ).isEmpty());
    }

    @Test
    void inability_to_establish_current_publication_evidence_withholds_fail_closed() {
        OpportunityPublicExposureReadPort missing =
                (merchantScope, opportunityIdentity) -> Optional.empty();
        Invocation invocation = invocation(Instant.parse("2026-09-05T12:00:00Z"));

        List<ResolvedExposedElement> resolved = new ExposureResolver().resolve(
                invocation.context(),
                invocation.admission(),
                List.of(OpportunityPublicExposureReferences.candidate(OPPORTUNITY)),
                OpportunityPublicExposureContractPortfolio.forRelease(RELEASE),
                requirements(missing),
                choices()
        );

        assertTrue(resolved.isEmpty());
    }

    private static List<ResolvedExposedElement> resolve(
            Instant evaluatedAt,
            OpportunityPublicExposureEvidence evidence
    ) {
        OpportunityPublicExposureReadPort readPort =
                (merchantScope, opportunityIdentity) -> Optional.of(evidence);
        ExposureCandidateObservation candidate =
                new OpportunityPublicExposureCandidateSource(readPort)
                        .currentCandidate(MERCHANT, OPPORTUNITY)
                        .orElseThrow();
        Invocation invocation = invocation(evaluatedAt);

        return new ExposureResolver().resolve(
                invocation.context(),
                invocation.admission(),
                List.of(candidate),
                OpportunityPublicExposureContractPortfolio.forRelease(RELEASE),
                requirements(readPort),
                choices()
        );
    }

    private static OpportunityPublicExposureEvidence evidence(
            Optional<OpportunityTemporalBoundary> publishFrom,
            Optional<OpportunityTemporalBoundary> publishUntil
    ) {
        return new OpportunityPublicExposureEvidence(
                MERCHANT,
                OPPORTUNITY,
                "revision-1",
                publishFrom,
                publishUntil
        );
    }

    private static ExposureRequirementEvaluatorBindingSnapshot requirements(
            OpportunityPublicExposureReadPort readPort
    ) {
        return new ExposureRequirementEvaluatorBindingSnapshot(
                RELEASE,
                List.of(new ExposureRequirementEvaluatorBinding(
                        OpportunityPublicExposureReferences.EXPOSURE_WINDOW_REQUIREMENT,
                        new OpportunityExposureWindowRequirementEvaluator(readPort)
                ))
        );
    }

    private static MerchantExposureChoiceEvaluatorBindingSnapshot choices() {
        return new MerchantExposureChoiceEvaluatorBindingSnapshot(
                RELEASE,
                List.of()
        );
    }

    private static Invocation invocation(Instant evaluatedAt) {
        EstablishedObservationRequest request =
                new DefaultEstablishedObservationRequest(
                        TestReleases.activeRelease(
                                MERCHANT.merchantIdentifier(),
                                RELEASE
                        ),
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
                                EstablishedObservationRequestDetails
                                        .requestBinding(request)
                        ),
                        evaluatedAt
                );
        return new Invocation(context, admission);
    }

    private record Invocation(
            AudienceObservationContext context,
            AudienceObservationAdmissionResult admission
    ) {
    }
}
