package grandrue.commercial;

import java.util.Objects;

/** Explicit non-fallback outcomes under MS-PROT-056 v1.9,
 * MS-PROT-056 v1.9 — Commercial Catalogue Binding, Publication & Historical Resolution Amendment,
 * §10 — Historical resolution. */
public final class CatalogueResolutionException extends IllegalStateException {
    public enum Reason {
        NOT_ESTABLISHED,
        HISTORY_NOT_COVERED,
        FUTURE_UNSUPPORTED,
        INTEGRITY_FAILURE,
        TECHNICAL_FAILURE
    }

    private final Reason reason;

    public CatalogueResolutionException(Reason reason, String message) {
        super(message);
        this.reason = Objects.requireNonNull(reason, "reason");
    }

    public Reason reason() {
        return reason;
    }
}
