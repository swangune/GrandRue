package mainstreet.enquiry;

import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EnquirySubmissionIntentTest {
    @Test
    void generated_identity_time_and_resolved_context_do_not_change_supplied_intent() {
        var first = submission("E1", Instant.EPOCH, "release-1");
        var later = submission("E2", Instant.EPOCH.plusSeconds(60), "release-2");
        assertNotEquals(first, later);
        assertEquals(EnquirySubmissionIntent.from(first), EnquirySubmissionIntent.from(later));
    }

    @Test
    void exact_supplied_contact_and_question_are_preserved_for_comparison() {
        var original = EnquirySubmissionIntent.from(submission("E1", Instant.EPOCH, "release-1"));
        assertEquals("  Question  ", original.question());
        assertEquals(Optional.of(" Alex "), original.contact().name());
        assertNotEquals(original, new EnquirySubmissionIntent(original.merchantScope(), "Question",
                original.contact(), original.subjectRevision(), original.customerContextIdentity()));
    }

    @Test
    void invalid_or_implicit_context_is_rejected() {
        var valid = EnquirySubmissionIntent.from(submission("E1", Instant.EPOCH, "release-1"));
        assertThrows(IllegalArgumentException.class, () -> new EnquirySubmissionIntent(
                valid.merchantScope(), " ", valid.contact(), Optional.empty(), Optional.empty()));
        assertThrows(NullPointerException.class, () -> new EnquirySubmissionIntent(
                valid.merchantScope(), valid.question(), valid.contact(), null, Optional.empty()));
        assertThrows(IllegalArgumentException.class, () -> new EnquirySubmissionIntent(
                valid.merchantScope(), valid.question(), valid.contact(), Optional.empty(), Optional.of(" ")));
    }

    private EnquirySubmission submission(String identity, Instant time, String release) {
        return new EnquirySubmission(new MerchantScope("merchant-a"), identity, time, "  Question  ",
                new EnquirySubmittedContact(Optional.of(" Alex "), Optional.empty(), Optional.empty()),
                new EnquirySemanticContext(release, "model-1", 1), Optional.empty(), Optional.empty());
    }
}
