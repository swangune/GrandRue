package grandrue.application;

import grandrue.observability.OperationalCorrelationReference;
import grandrue.observability.OperationalEvidenceContractDefinition;
import grandrue.observability.OperationalEvidenceContractIdentity;
import grandrue.observability.OperationalEvidenceContractRegistrySnapshot;
import grandrue.observability.OperationalEvidenceFamily;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Current IMP-08C operational-evidence portfolio for the active durable
 * Standing Free Event Reaction and Background Work responsibilities.
 *
 * <p>This is deliberately not the whole Target-19 observability portfolio.</p>
 */
public final class DurableExecutionOperationalEvidencePortfolio {

    public static final String RELEASE =
            "durable-execution-operational-evidence@1";

    public static final OperationalEvidenceContractIdentity
            EVENT_REACTION_PROGRESSION =
            new OperationalEvidenceContractIdentity(
                    "commercial",
                    "standing-free-event-reaction-progression"
            );

    public static final OperationalEvidenceContractIdentity
            BACKGROUND_WORK_PROGRESSION =
            new OperationalEvidenceContractIdentity(
                    "commercial",
                    "standing-free-background-work-progression"
            );

    private static final String DATA_MINIMISATION =
            "MS-PROT-068-v1.1/22:bounded-identifiers-and-classifications";

    private static final String RETENTION =
            "MS-PROT-053:purpose-scoped-operational-evidence-retention";

    private static final Set<String> OPERATIONS_ONLY =
            Set.of("platform-operations-authorised-audience");

    private static final OperationalEvidenceContractDefinition
            EVENT_REACTION_DEFINITION =
            new OperationalEvidenceContractDefinition(
                    EVENT_REACTION_PROGRESSION,
                    reactionResponsibilityReference(),
                    "MS-PROT-068-v1.1/7:durable-event-reaction-progression",
                    Set.of(
                            OperationalEvidenceFamily.OPERATIONAL_METRIC
                    ),
                    Set.of(
                            OperationalCorrelationReference.MERCHANT_SCOPE,
                            OperationalCorrelationReference
                                    .DOMAIN_EVENT_IDENTITY,
                            OperationalCorrelationReference
                                    .EVENT_REACTION_IDENTITY
                    ),
                    Optional.empty(),
                    "MS-PROT-068-v1.1/17:accepted-at-and-current-read",
                    "MS-PROT-068-v1.1/7:pending-reaction-age-and-lag",
                    Optional.of(
                            "MS-PROT-068-v1.1/19:"
                                    + "request-authorised-attention-only"
                    ),
                    DATA_MINIMISATION,
                    RETENTION,
                    OPERATIONS_ONLY
            );

    private static final OperationalEvidenceContractDefinition
            BACKGROUND_WORK_DEFINITION =
            new OperationalEvidenceContractDefinition(
                    BACKGROUND_WORK_PROGRESSION,
                    workResponsibilityReference(),
                    "MS-PROT-068-v1.1/8:durable-background-work-progression",
                    Set.of(
                            OperationalEvidenceFamily.OPERATIONAL_METRIC
                    ),
                    Set.of(
                            OperationalCorrelationReference.MERCHANT_SCOPE,
                            OperationalCorrelationReference
                                    .DURABLE_WORK_INSTRUCTION_IDENTITY,
                            OperationalCorrelationReference
                                    .WORK_ATTEMPT_IDENTITY
                    ),
                    Optional.empty(),
                    "MS-PROT-068-v1.1/17:"
                            + "created-due-next-attempt-and-attempt-time",
                    "MS-PROT-068-v1.1/8:"
                            + "outstanding-age-attempt-and-classification",
                    Optional.of(
                            "MS-PROT-068-v1.1/19:"
                                    + "request-authorised-attention-only"
                    ),
                    DATA_MINIMISATION,
                    RETENTION,
                    OPERATIONS_ONLY
            );

    private DurableExecutionOperationalEvidencePortfolio() {
    }

    public static OperationalEvidenceContractRegistrySnapshot registry() {
        return new OperationalEvidenceContractRegistrySnapshot(
                RELEASE,
                List.of(
                        EVENT_REACTION_DEFINITION,
                        BACKGROUND_WORK_DEFINITION
                )
        );
    }

    private static String reactionResponsibilityReference() {
        return StandingFreeEventReactionContract
                .IDENTITY
                .ownerIdentifier()
                + "/"
                + StandingFreeEventReactionContract
                .IDENTITY
                .contractIdentifier()
                + "@"
                + StandingFreeEventReactionContract
                .AFFINITY
                .semanticRegistryReleaseIdentifier();
    }

    private static String workResponsibilityReference() {
        return StandingFreeBackgroundWorkContract
                .IDENTITY
                .ownerIdentifier()
                + "/"
                + StandingFreeBackgroundWorkContract
                .IDENTITY
                .contractIdentifier()
                + "@"
                + StandingFreeBackgroundWorkContract
                .AFFINITY
                .semanticRegistryReleaseIdentifier();
    }
}
