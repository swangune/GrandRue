package grandrue.audit;

import mainstreet.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Minimal append-oriented accountability evidence governed by MS-PROT-064.
 *
 * <p>This record references authoritative subjects and actions but does not own
 * their business state. Values are deliberately bounded identifiers/categories
 * rather than copied request or domain payloads.</p>
 */
public record AuditRecord(
        String auditIdentity,
        Instant occurredAt,
        String principalReference,
        AuditExecutionScope executionScope,
        Optional<MerchantScope> merchantScope,
        AuditActionClass actionClass,
        String actionIdentifier,
        Optional<String> subjectType,
        Optional<String> subjectReference,
        String outcomeIdentifier,
        Optional<String> reasonCategory,
        String correlationIdentifier,
        Optional<String> causationIdentifier,
        Optional<String> originIdentifier,
        Optional<String> evidenceReference
) {
    public AuditRecord {
        require(auditIdentity, "auditIdentity");
        Objects.requireNonNull(occurredAt, "occurredAt");
        require(principalReference, "principalReference");
        Objects.requireNonNull(executionScope, "executionScope");
        merchantScope = Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(actionClass, "actionClass");
        require(actionIdentifier, "actionIdentifier");
        subjectType = normalise(subjectType, "subjectType");
        subjectReference = normalise(subjectReference, "subjectReference");
        require(outcomeIdentifier, "outcomeIdentifier");
        reasonCategory = normalise(reasonCategory, "reasonCategory");
        require(correlationIdentifier, "correlationIdentifier");
        causationIdentifier = normalise(causationIdentifier, "causationIdentifier");
        originIdentifier = normalise(originIdentifier, "originIdentifier");
        evidenceReference = normalise(evidenceReference, "evidenceReference");

        if (executionScope == AuditExecutionScope.MERCHANT && merchantScope.isEmpty()) {
            throw new IllegalArgumentException(
                    "MERCHANT audit execution scope requires Merchant Scope"
            );
        }
        if (executionScope == AuditExecutionScope.PLATFORM && merchantScope.isPresent()) {
            throw new IllegalArgumentException(
                    "PLATFORM audit execution scope must not fabricate Merchant Scope"
            );
        }
        if (subjectType.isPresent() != subjectReference.isPresent()) {
            throw new IllegalArgumentException(
                    "Audit subject type and reference must either both be present or both be absent"
            );
        }
    }

    private static Optional<String> normalise(Optional<String> value, String label) {
        Objects.requireNonNull(value, label);
        value.ifPresent(item -> require(item, label));
        return value;
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
