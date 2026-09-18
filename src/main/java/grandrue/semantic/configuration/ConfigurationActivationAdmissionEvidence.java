package grandrue.semantic.configuration;

/** Exact release-purpose and stable-serving evidence consumed by activation. */
public record ConfigurationActivationAdmissionEvidence(
        String semanticRegistryReleaseIdentifier,
        String validationAdmissionDecisionIdentifier,
        String businessActivityAdmissionDecisionIdentifier,
        String resolvedPackageEvidenceIdentifier,
        String requirementSetIdentifier,
        String servingGenerationIdentifier,
        long servingGenerationEpoch,
        String packagedBundleContentDigest
) {
    public ConfigurationActivationAdmissionEvidence {
        requireIdentifier(semanticRegistryReleaseIdentifier, "Semantic Registry Release identifier");
        requireIdentifier(validationAdmissionDecisionIdentifier, "Validation admission decision identifier");
        requireIdentifier(businessActivityAdmissionDecisionIdentifier, "Business-activity admission decision identifier");
        requireIdentifier(resolvedPackageEvidenceIdentifier, "Resolved package evidence identifier");
        requireIdentifier(requirementSetIdentifier, "Requirement-set identifier");
        requireIdentifier(servingGenerationIdentifier, "Serving generation identifier");
        if (servingGenerationEpoch < 0) {
            throw new IllegalArgumentException("Serving generation epoch must not be negative");
        }
        requireIdentifier(packagedBundleContentDigest, "Packaged bundle content digest");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
