package grandrue.commercial;

/**
 * Exact owner-qualified target reference, not a registration or permission.
 * MS-PROT-056 v1.9 — Commercial Catalogue Binding, Publication & Historical Resolution Amendment,
 * §§4–5 — Exact binding requirements; Binding identity and satisfaction.
 */
public record CommercialAccessTarget(String owner, String targetIdentity, String contractRevision) {
    public CommercialAccessTarget {
        requireExactReference(owner, "owner");
        requireExactReference(targetIdentity, "targetIdentity");
        requireExactReference(contractRevision, "contractRevision");
    }

    static void requireExactReference(String value, String field) {
        requireText(value, field);
        if (value.contains("*")) {
            throw new IllegalArgumentException(field + " must not contain a wildcard");
        }
    }

    static void requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
    }
}
