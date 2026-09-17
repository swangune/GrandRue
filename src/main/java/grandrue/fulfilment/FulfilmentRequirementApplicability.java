package grandrue.fulfilment;

/**
 * Bounded deterministic applicability vocabulary for registered Fulfilment
 * Requirements. Runtime facts and arbitrary expressions are deliberately
 * outside this static contract.
 */
public sealed interface FulfilmentRequirementApplicability
        permits AlwaysFulfilmentRequirementApplicability,
        EnumDecisionValueFulfilmentRequirementApplicability {
}
