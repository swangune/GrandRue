package mainstreet.semantic.configuration;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Coordinates required assessments after resolving immutable validation and source authority.
 * Assessment completion does not establish approval, activation or permission to alter commitments.
 * No completed result escapes if any required assessment fails.
 *
 * Authority: designs/MS-PROT-040 v1.3 — Configuration Validation, Impact Review & Approval Evidence Amendment.md,
 * §§9–11 exact review affinity, business-facing effects and production predicate;
 * designs/MS-PROT-040 — Merchant Configuration Review, Approval, Activation & Change Model.md,
 * v1.0 §§20–24 diff, impact classification and protected historical commitments.
 */
public final class ConfigurationImpactAnalyzer {
    private final ConfigurationRevisionAuthority revisions;
    private final ConfigurationValidationEvidenceAuthority validations;
    private final List<ConfigurationImpactAssessment> assessments;

    public ConfigurationImpactAnalyzer(ConfigurationRevisionAuthority revisions,
                                       ConfigurationValidationEvidenceAuthority validations,
                                       List<ConfigurationImpactAssessment> assessments) {
        this.revisions = Objects.requireNonNull(revisions, "revisions");
        this.validations = Objects.requireNonNull(validations, "validations");
        this.assessments = List.copyOf(assessments);
        if (this.assessments.isEmpty()) {
            throw new IllegalArgumentException("Impact analysis requires applicable assessments");
        }
    }

    public ConfigurationImpactAnalysisResult analyze(MerchantScope scope, String validationEvidenceIdentifier,
                                                     String packageEvidenceIdentifier,
                                                     ResolvedConfigurationPackage resolvedPackage, Instant completedAt) {
        Objects.requireNonNull(scope, "scope");
        Objects.requireNonNull(resolvedPackage, "resolvedPackage");
        Objects.requireNonNull(completedAt, "completedAt");
        var validation = validations.evidence(scope, validationEvidenceIdentifier)
                .orElseThrow(() -> new IllegalArgumentException("Validation evidence does not exist"));
        if (!scope.merchantIdentifier().equals(validation.merchantIdentifier())
                || !validation.validationEvidenceIdentifier().equals(validationEvidenceIdentifier)
                || !validation.resolvedPackageEvidenceIdentifier().equals(packageEvidenceIdentifier)) {
            throw new IllegalArgumentException("Impact request does not match exact validation/package evidence");
        }
        var candidate = revision(scope, validation.configurationRevisionIdentifier());
        var base = candidate.baseConfigurationIdentifier().map(identifier -> revision(scope, identifier));
        var context = new ConfigurationImpactContext(candidate, base, validation, resolvedPackage);
        var effects = new ArrayList<String>();
        var findings = new ArrayList<ConfigurationImpactFinding>();
        for (var assessment : assessments) {
            var contribution = Objects.requireNonNull(assessment.assess(context), "Assessment did not complete");
            effects.addAll(contribution.businessFacingEffects());
            findings.addAll(contribution.findings());
        }
        return new ConfigurationImpactAnalysisResult(validation.merchantIdentifier(),
                validation.configurationRevisionIdentifier(), validation.semanticRegistryReleaseIdentifier(),
                validation.validationEvidenceIdentifier(), validation.resolvedPackageEvidenceIdentifier(),
                effects, findings, completedAt);
    }

    private MerchantConfiguration revision(MerchantScope scope, String identifier) {
        return revisions.configuration(scope, identifier)
                .orElseThrow(() -> new IllegalArgumentException("Exact Configuration Revision does not exist: " + identifier));
    }
}
