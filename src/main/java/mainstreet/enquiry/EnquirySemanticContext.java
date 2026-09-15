package mainstreet.enquiry;

/** Immutable submission-time semantic context; its presence grants no execution authority. */
public record EnquirySemanticContext(
        String semanticRegistryReleaseIdentifier,
        String resolvedModelIdentifier,
        long resolvedModelVersion
) {
    public EnquirySemanticContext {
        EnquirySubmission.requireIdentifier(semanticRegistryReleaseIdentifier, "semanticRegistryReleaseIdentifier");
        EnquirySubmission.requireIdentifier(resolvedModelIdentifier, "resolvedModelIdentifier");
    }
}
