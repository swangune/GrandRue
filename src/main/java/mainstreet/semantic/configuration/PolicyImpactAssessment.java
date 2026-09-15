package mainstreet.semantic.configuration;

/**
 * Read-only owner-supplied interpretation of a registered policy change in business terms.
 * Implementations must support the exact before/after releases and obtain applicable
 * commitment facts from their owners. Unsupported semantics must fail assessment.
 * Authority: designs/MS-PROT-047 — Capability Configuration Contract.md, v1.0 §§4–5
 * semantic ownership and separation from presentation;
 * designs/MS-PROT-040 v1.3 — Configuration Validation, Impact Review & Approval Evidence Amendment.md,
 * §10 Business-Facing Review Boundary; §11 Impact-Evidence Production Predicate.
 */
@FunctionalInterface
public interface PolicyImpactAssessment {
    ConfigurationImpactContribution assess(ConfigurationImpactContext context, ResolvedPolicyChange change);
}
