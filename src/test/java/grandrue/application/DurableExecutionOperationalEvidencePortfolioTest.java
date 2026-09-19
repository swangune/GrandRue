package grandrue.application;

import grandrue.observability.OperationalCorrelationReference;
import grandrue.observability.OperationalEvidenceFamily;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DurableExecutionOperationalEvidencePortfolioTest {

    @Test
    void portfolio_registers_only_the_active_durable_execution_responsibilities() {
        var registry =
                DurableExecutionOperationalEvidencePortfolio.registry();

        assertEquals(
                DurableExecutionOperationalEvidencePortfolio.RELEASE,
                registry.semanticRegistryReleaseIdentifier()
        );
        assertEquals(2, registry.contracts().size());

        var reaction = registry.contract(
                DurableExecutionOperationalEvidencePortfolio.RELEASE,
                DurableExecutionOperationalEvidencePortfolio
                        .EVENT_REACTION_PROGRESSION
        ).orElseThrow();

        assertEquals(
                Set.of(OperationalEvidenceFamily.OPERATIONAL_METRIC),
                reaction.evidenceFamilies()
        );
        assertEquals(
                Set.of(
                        OperationalCorrelationReference.MERCHANT_SCOPE,
                        OperationalCorrelationReference.DOMAIN_EVENT_IDENTITY,
                        OperationalCorrelationReference.EVENT_REACTION_IDENTITY
                ),
                reaction.safeCorrelationReferences()
        );
        assertTrue(
                reaction.owningResponsibilityReference()
                        .contains(
                                StandingFreeEventReactionContract
                                        .IDENTITY
                                        .contractIdentifier()
                        )
        );

        var work = registry.contract(
                DurableExecutionOperationalEvidencePortfolio.RELEASE,
                DurableExecutionOperationalEvidencePortfolio
                        .BACKGROUND_WORK_PROGRESSION
        ).orElseThrow();

        assertEquals(
                Set.of(OperationalEvidenceFamily.OPERATIONAL_METRIC),
                work.evidenceFamilies()
        );
        assertEquals(
                Set.of(
                        OperationalCorrelationReference.MERCHANT_SCOPE,
                        OperationalCorrelationReference
                                .DURABLE_WORK_INSTRUCTION_IDENTITY,
                        OperationalCorrelationReference.WORK_ATTEMPT_IDENTITY
                ),
                work.safeCorrelationReferences()
        );
        assertTrue(
                work.owningResponsibilityReference()
                        .contains(
                                StandingFreeBackgroundWorkContract
                                        .IDENTITY
                                        .contractIdentifier()
                        )
        );
    }

    @Test
    void portfolio_does_not_claim_health_or_business_completion_authority() {
        for (var definition :
                DurableExecutionOperationalEvidencePortfolio
                        .registry()
                        .contracts()) {
            assertTrue(definition.healthInterpretationReference().isEmpty());
            assertTrue(
                    definition.alertingOrEscalationEligibilityReference()
                            .isPresent()
            );
        }
    }
}
