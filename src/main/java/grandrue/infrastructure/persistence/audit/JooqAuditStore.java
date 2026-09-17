package grandrue.infrastructure.persistence.audit;

import mainstreet.application.MerchantScope;
import grandrue.audit.AuditActionClass;
import grandrue.audit.AuditExecutionScope;
import grandrue.audit.AuditRecord;
import grandrue.audit.AuditStore;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** PostgreSQL/jOOQ append-oriented persistence for governed audit evidence. */
public final class JooqAuditStore implements AuditStore {

    private static final Table<?> AUDIT = DSL.table(DSL.name("audit_record"));
    private static final Field<String> AUDIT_IDENTIFIER =
            DSL.field(DSL.name("audit_identifier"), String.class);
    private static final Field<Instant> OCCURRED_AT =
            DSL.field(DSL.name("occurred_at"), Instant.class);
    private static final Field<String> PRINCIPAL_REFERENCE =
            DSL.field(DSL.name("principal_reference"), String.class);
    private static final Field<String> EXECUTION_SCOPE =
            DSL.field(DSL.name("execution_scope"), String.class);
    private static final Field<String> MERCHANT_IDENTIFIER =
            DSL.field(DSL.name("merchant_identifier"), String.class);
    private static final Field<String> ACTION_CLASS =
            DSL.field(DSL.name("action_class"), String.class);
    private static final Field<String> ACTION_IDENTIFIER =
            DSL.field(DSL.name("action_identifier"), String.class);
    private static final Field<String> SUBJECT_TYPE =
            DSL.field(DSL.name("subject_type"), String.class);
    private static final Field<String> SUBJECT_REFERENCE =
            DSL.field(DSL.name("subject_reference"), String.class);
    private static final Field<String> OUTCOME_IDENTIFIER =
            DSL.field(DSL.name("outcome_identifier"), String.class);
    private static final Field<String> REASON_CATEGORY =
            DSL.field(DSL.name("reason_category"), String.class);
    private static final Field<String> CORRELATION_IDENTIFIER =
            DSL.field(DSL.name("correlation_identifier"), String.class);
    private static final Field<String> CAUSATION_IDENTIFIER =
            DSL.field(DSL.name("causation_identifier"), String.class);
    private static final Field<String> ORIGIN_IDENTIFIER =
            DSL.field(DSL.name("origin_identifier"), String.class);
    private static final Field<String> EVIDENCE_REFERENCE =
            DSL.field(DSL.name("evidence_reference"), String.class);

    private final DSLContext dsl;
    private final TransactionTemplate transactionTemplate;

    public JooqAuditStore(
            DSLContext dsl,
            PlatformTransactionManager transactionManager
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactionTemplate = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
    }

    @Override
    public AuditRecord append(AuditRecord candidate) {
        Objects.requireNonNull(candidate, "candidate");
        AuditRecord result = transactionTemplate.execute(status -> {
            lockAuditIdentity(candidate.auditIdentity());
            Optional<AuditRecord> existing = record(candidate.auditIdentity());
            if (existing.isPresent()) {
                AuditRecord committed = existing.orElseThrow();
                if (!committed.equals(candidate)) {
                    throw new IllegalStateException(
                            "Audit identity already exists with different evidence"
                    );
                }
                return committed;
            }

            dsl.insertInto(AUDIT)
                    .columns(
                            AUDIT_IDENTIFIER,
                            OCCURRED_AT,
                            PRINCIPAL_REFERENCE,
                            EXECUTION_SCOPE,
                            MERCHANT_IDENTIFIER,
                            ACTION_CLASS,
                            ACTION_IDENTIFIER,
                            SUBJECT_TYPE,
                            SUBJECT_REFERENCE,
                            OUTCOME_IDENTIFIER,
                            REASON_CATEGORY,
                            CORRELATION_IDENTIFIER,
                            CAUSATION_IDENTIFIER,
                            ORIGIN_IDENTIFIER,
                            EVIDENCE_REFERENCE
                    )
                    .values(
                            candidate.auditIdentity(),
                            candidate.occurredAt(),
                            candidate.principalReference(),
                            candidate.executionScope().name(),
                            candidate.merchantScope()
                                    .map(MerchantScope::merchantIdentifier)
                                    .orElse(null),
                            candidate.actionClass().name(),
                            candidate.actionIdentifier(),
                            candidate.subjectType().orElse(null),
                            candidate.subjectReference().orElse(null),
                            candidate.outcomeIdentifier(),
                            candidate.reasonCategory().orElse(null),
                            candidate.correlationIdentifier(),
                            candidate.causationIdentifier().orElse(null),
                            candidate.originIdentifier().orElse(null),
                            candidate.evidenceReference().orElse(null)
                    )
                    .execute();
            return candidate;
        });
        return Objects.requireNonNull(result, "Audit append transaction returned no record");
    }

    @Override
    public Optional<AuditRecord> record(String auditIdentity) {
        requireIdentifier(auditIdentity, "auditIdentity");
        Record record = selectFields()
                .from(AUDIT)
                .where(AUDIT_IDENTIFIER.eq(auditIdentity))
                .fetchOne();
        return Optional.ofNullable(record).map(this::toAuditRecord);
    }

    @Override
    public List<AuditRecord> merchantRecords(
            MerchantScope merchantScope,
            Instant fromInclusive,
            Instant untilExclusive,
            int limit
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(fromInclusive, "fromInclusive");
        Objects.requireNonNull(untilExclusive, "untilExclusive");
        if (!untilExclusive.isAfter(fromInclusive)) {
            throw new IllegalArgumentException("Audit query end must be after start");
        }
        if (limit < 1) {
            throw new IllegalArgumentException("Audit query limit must be positive");
        }

        return selectFields()
                .from(AUDIT)
                .where(MERCHANT_IDENTIFIER.eq(merchantScope.merchantIdentifier()))
                .and(OCCURRED_AT.ge(fromInclusive))
                .and(OCCURRED_AT.lt(untilExclusive))
                .orderBy(OCCURRED_AT, AUDIT_IDENTIFIER)
                .limit(limit)
                .fetch(this::toAuditRecord);
    }

    private org.jooq.SelectSelectStep<? extends Record> selectFields() {
        return dsl.select(
                AUDIT_IDENTIFIER,
                OCCURRED_AT,
                PRINCIPAL_REFERENCE,
                EXECUTION_SCOPE,
                MERCHANT_IDENTIFIER,
                ACTION_CLASS,
                ACTION_IDENTIFIER,
                SUBJECT_TYPE,
                SUBJECT_REFERENCE,
                OUTCOME_IDENTIFIER,
                REASON_CATEGORY,
                CORRELATION_IDENTIFIER,
                CAUSATION_IDENTIFIER,
                ORIGIN_IDENTIFIER,
                EVIDENCE_REFERENCE
        );
    }

    private AuditRecord toAuditRecord(Record row) {
        String merchantIdentifier = row.get(MERCHANT_IDENTIFIER);
        return new AuditRecord(
                row.get(AUDIT_IDENTIFIER),
                row.get(OCCURRED_AT),
                row.get(PRINCIPAL_REFERENCE),
                AuditExecutionScope.valueOf(row.get(EXECUTION_SCOPE)),
                Optional.ofNullable(merchantIdentifier).map(MerchantScope::new),
                AuditActionClass.valueOf(row.get(ACTION_CLASS)),
                row.get(ACTION_IDENTIFIER),
                Optional.ofNullable(row.get(SUBJECT_TYPE)),
                Optional.ofNullable(row.get(SUBJECT_REFERENCE)),
                row.get(OUTCOME_IDENTIFIER),
                Optional.ofNullable(row.get(REASON_CATEGORY)),
                row.get(CORRELATION_IDENTIFIER),
                Optional.ofNullable(row.get(CAUSATION_IDENTIFIER)),
                Optional.ofNullable(row.get(ORIGIN_IDENTIFIER)),
                Optional.ofNullable(row.get(EVIDENCE_REFERENCE))
        );
    }

    private void lockAuditIdentity(String auditIdentity) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 0))",
                "audit|" + auditIdentity
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
