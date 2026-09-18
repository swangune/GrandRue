package grandrue.semantic.configuration;

import java.util.List;

/**
 * Completed contribution; one assessment may have no findings or effects.
 * The combined review must still contain business-facing effects.
 * Authority: designs/MS-PROT-040 v1.3 — Configuration Validation, Impact Review & Approval Evidence Amendment.md,
 * §9 Impact-Review-Evidence Identity and Minimum Affinity; §10 Business-Facing Review Boundary.
 */
public record ConfigurationImpactContribution(
        List<String> businessFacingEffects,
        List<ConfigurationImpactFinding> findings
) {
    public ConfigurationImpactContribution {
        businessFacingEffects = List.copyOf(businessFacingEffects);
        findings = List.copyOf(findings);
        if (businessFacingEffects.stream().anyMatch(String::isBlank)) {
            throw new IllegalArgumentException("Business-facing effects must not be blank");
        }
    }
}
