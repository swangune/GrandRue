package grandrue.enquiry;

import grandrue.application.MerchantScope;
import grandrue.semantic.registry.OwnedOperationalObjectTypeReference;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EnquirySubmissionTest {
    @Test
    void general_submission_preserves_supplied_values_without_inventing_customer_identity() {
        EnquirySubmission submission = submission(Optional.empty(), Optional.empty());
        assertEquals("  Can I apply?\nThank you.  ", submission.question());
        assertEquals(Optional.of("  Alex  "), submission.contact().name());
        assertEquals(Optional.of("Alex@example.test"), submission.contact().email());
        assertEquals(Optional.empty(), submission.customerContextIdentity());
        assertEquals(Optional.empty(), submission.subjectRevision());
    }

    @Test
    void exact_subject_owner_type_identity_and_revision_are_retained_as_evidence() {
        var revision = new EnquiryRevisionProvenance(
                new OwnedOperationalObjectTypeReference("publication", "opportunity"), "O1", "R7");
        var submission = submission(Optional.of(revision), Optional.of("known-customer"));
        assertEquals(revision, submission.subjectRevision().orElseThrow());
        assertEquals(Optional.of("known-customer"), submission.customerContextIdentity());
        assertEquals("release-1", submission.semanticContext().semanticRegistryReleaseIdentifier());
        assertEquals("model-1", submission.semanticContext().resolvedModelIdentifier());
        assertEquals(7, submission.semanticContext().resolvedModelVersion());
    }

    @Test
    void contact_requirements_are_not_invented_by_the_storage_value() {
        assertDoesNotThrow(() -> new EnquirySubmittedContact(
                Optional.empty(), Optional.empty(), Optional.empty()));
        assertThrows(NullPointerException.class,
                () -> new EnquirySubmittedContact(null, Optional.empty(), Optional.empty()));
    }

    @Test
    void incomplete_revision_or_semantic_evidence_is_rejected() {
        var type = new OwnedOperationalObjectTypeReference("publication", "opportunity");
        assertThrows(IllegalArgumentException.class, () -> new EnquiryRevisionProvenance(type, " ", "R7"));
        assertThrows(IllegalArgumentException.class, () -> new EnquiryRevisionProvenance(type, "O1", ""));
        assertThrows(NullPointerException.class, () -> new EnquiryRevisionProvenance(null, "O1", "R7"));
        assertThrows(IllegalArgumentException.class, () -> new EnquirySemanticContext("", "model", 1));
        assertThrows(IllegalArgumentException.class, () -> new EnquirySemanticContext("release", " ", 1));
    }

    @Test
    void submission_requires_identity_question_time_and_explicit_optional_context() {
        var valid = submission(Optional.empty(), Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> new EnquirySubmission(
                valid.merchantScope(), " ", valid.submittedAt(), valid.question(), valid.contact(),
                valid.semanticContext(), Optional.empty(), Optional.empty()));
        assertThrows(IllegalArgumentException.class, () -> new EnquirySubmission(
                valid.merchantScope(), "E1", valid.submittedAt(), " ", valid.contact(),
                valid.semanticContext(), Optional.empty(), Optional.empty()));
        assertThrows(NullPointerException.class, () -> new EnquirySubmission(
                valid.merchantScope(), "E1", null, valid.question(), valid.contact(),
                valid.semanticContext(), Optional.empty(), Optional.empty()));
        assertThrows(NullPointerException.class, () -> submission(null, Optional.empty()));
        assertThrows(IllegalArgumentException.class, () -> submission(Optional.empty(), Optional.of(" ")));
    }

    private EnquirySubmission submission(Optional<EnquiryRevisionProvenance> revision, Optional<String> customer) {
        return new EnquirySubmission(new MerchantScope("merchant-a"), "E1",
                Instant.parse("2026-09-05T12:00:00.123456789Z"), "  Can I apply?\nThank you.  ",
                new EnquirySubmittedContact(Optional.of("  Alex  "), Optional.of("Alex@example.test"),
                        Optional.of("+44 1234 567890")),
                new EnquirySemanticContext("release-1", "model-1", 7), revision, customer);
    }
}
