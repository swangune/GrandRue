package mainstreet.commercial;

import java.util.Objects;

/** MS-PROT-056 v1.9, designs/MS-PROT-056 v1.9 — Commercial Catalogue Binding, Publication & Historical Resolution Amendment.md, §12 — Idempotency, concurrency and failure. */
public final class CataloguePublicationException extends IllegalStateException {
    public enum Reason {
        AUTHORISATION_REJECTED, VALIDATION_REJECTED, PREDECESSOR_CONFLICT,
        IDENTITY_CONFLICT, INTEGRITY_FAILURE, TECHNICAL_FAILURE
    }

    private final Reason reason;

    public CataloguePublicationException(Reason reason, String message) {
        super(message);
        this.reason = Objects.requireNonNull(reason, "reason");
    }

    public Reason reason() { return reason; }
}
