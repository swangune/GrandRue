package mainstreet.infrastructure.persistence.identitysecurity;

import mainstreet.audit.AuditActionClass;
import mainstreet.audit.AuditExecutionScope;
import mainstreet.audit.AuditRecord;
import mainstreet.audit.AuditStore;
import mainstreet.identitysecurity.IdentitySecurityGeneration;
import mainstreet.identitysecurity.IdentitySecurityGenerationException;
import mainstreet.identitysecurity.IdentitySecurityGenerationFailureCategory;
import mainstreet.identitysecurity.IdentitySecurityGenerationManagement;
import mainstreet.identitysecurity.IdentitySecurityRotationCommand;
import mainstreet.runtime.SessionRecordStore;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * PostgreSQL/jOOQ Identity Security generation authority under ADR-014 v1.1.
 *
 * <p>Rotation, Identity-wide Session revocation and authentication-security
 * Audit append share one transaction. Generation state contains no credential
 * material or merchant/business authority.</p>
 */
public final class JooqIdentitySecurityGenerationManagement
        implements IdentitySecurityGenerationManagement {

    private static final Table<?> GENERATION =
            DSL.table(DSL.name("identity_security_generation"));
    private static final Field<String> IDENTITY_REFERENCE =
            DSL.field(DSL.name("identity_reference"), String.class);
    private static final Field<String> CURRENT_GENERATION_REFERENCE =
            DSL.field(DSL.name("current_generation_reference"), String.class);
    private static final Field<Long> CONCURRENCY_VERSION =
            DSL.field(DSL.name("concurrency_version"), Long.class);
    private static final Field<Instant> ESTABLISHED_AT =
            DSL.field(DSL.name("established_at"), Instant.class);
    private static final Field<Instant> LAST_ROTATED_AT =
            DSL.field(DSL.name("last_rotated_at"), Instant.class);

    private static final String ROTATION_ACTION =
            "IDENTITY_SECURITY_GENERATION_ROTATED";

    private final DSLContext dsl;
    private final TransactionTemplate transactionTemplate;
    private final SessionRecordStore sessionRecordStore;
    private final AuditStore auditStore;

    public JooqIdentitySecurityGenerationManagement(
            DSLContext dsl,
            PlatformTransactionManager transactionManager,
            SessionRecordStore sessionRecordStore,
            AuditStore auditStore
    ) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
        this.transactionTemplate = new TransactionTemplate(
                Objects.requireNonNull(transactionManager, "transactionManager")
        );
        this.sessionRecordStore = Objects.requireNonNull(
                sessionRecordStore,
                "sessionRecordStore"
        );
        this.auditStore = Objects.requireNonNull(auditStore, "auditStore");
    }

    @Override
    public IdentitySecurityGeneration initialize(
            String identityReference,
            String initialGenerationReference,
            Instant establishedAt
    ) {
        IdentitySecurityGeneration candidate = new IdentitySecurityGeneration(
                identityReference,
                initialGenerationReference,
                1,
                establishedAt,
                Optional.empty()
        );

        IdentitySecurityGeneration result = transactionTemplate.execute(status -> {
            lockIdentity(candidate.identityReference());
            Optional<IdentitySecurityGeneration> existing =
                    state(candidate.identityReference());
            if (existing.isPresent()) {
                return existing.orElseThrow();
            }

            dsl.insertInto(GENERATION)
                    .columns(
                            IDENTITY_REFERENCE,
                            CURRENT_GENERATION_REFERENCE,
                            CONCURRENCY_VERSION,
                            ESTABLISHED_AT,
                            LAST_ROTATED_AT
                    )
                    .values(
                            candidate.identityReference(),
                            candidate.generationReference(),
                            candidate.version(),
                            candidate.establishedAt(),
                            null
                    )
                    .execute();
            return state(candidate.identityReference()).orElseThrow(() ->
                    new IllegalStateException(
                            "Identity security generation was not committed"
                    )
            );
        });
        return Objects.requireNonNull(
                result,
                "Identity security initialization returned no state"
        );
    }

    @Override
    public Optional<IdentitySecurityGeneration> state(String identityReference) {
        require(identityReference, "identityReference");
        Record row = dsl.select(
                        IDENTITY_REFERENCE,
                        CURRENT_GENERATION_REFERENCE,
                        CONCURRENCY_VERSION,
                        ESTABLISHED_AT,
                        LAST_ROTATED_AT
                )
                .from(GENERATION)
                .where(IDENTITY_REFERENCE.eq(identityReference))
                .fetchOne();
        return Optional.ofNullable(row).map(this::toGeneration);
    }

    @Override
    public IdentitySecurityGeneration rotate(
            IdentitySecurityRotationCommand command
    ) {
        Objects.requireNonNull(command, "command");

        IdentitySecurityGeneration result = transactionTemplate.execute(status -> {
            lockIdentity(command.identityReference());
            IdentitySecurityGeneration current = state(command.identityReference())
                    .orElseThrow(() -> failure(
                            IdentitySecurityGenerationFailureCategory.NOT_ESTABLISHED,
                            "Identity security generation is not established"
                    ));

            if (!current.generationReference().equals(
                    command.expectedGenerationReference()
            )) {
                throw failure(
                        IdentitySecurityGenerationFailureCategory.CONFLICT,
                        "Identity security generation changed concurrently"
                );
            }

            long nextVersion = Math.addExact(current.version(), 1);
            int updated = dsl.update(GENERATION)
                    .set(
                            CURRENT_GENERATION_REFERENCE,
                            command.replacementGenerationReference()
                    )
                    .set(CONCURRENCY_VERSION, nextVersion)
                    .set(LAST_ROTATED_AT, command.occurredAt())
                    .where(IDENTITY_REFERENCE.eq(command.identityReference()))
                    .and(CURRENT_GENERATION_REFERENCE.eq(
                            command.expectedGenerationReference()
                    ))
                    .and(CONCURRENCY_VERSION.eq(current.version()))
                    .execute();
            if (updated != 1) {
                throw failure(
                        IdentitySecurityGenerationFailureCategory.CONFLICT,
                        "Identity security generation changed concurrently"
                );
            }

            sessionRecordStore.revokeAllForIdentity(
                    command.identityReference(),
                    command.occurredAt(),
                    sessionRevocationReason(command)
            );
            auditStore.append(rotationAudit(command));

            return state(command.identityReference()).orElseThrow(() ->
                    new IllegalStateException(
                            "Identity security generation disappeared during rotation"
                    )
            );
        });
        return Objects.requireNonNull(
                result,
                "Identity security rotation returned no state"
        );
    }

    private AuditRecord rotationAudit(IdentitySecurityRotationCommand command) {
        return new AuditRecord(
                command.auditIdentity(),
                command.occurredAt(),
                command.principalReference(),
                AuditExecutionScope.PLATFORM,
                Optional.empty(),
                AuditActionClass.AUTHENTICATION_SECURITY,
                ROTATION_ACTION,
                Optional.of("IDENTITY"),
                Optional.of(command.identityReference()),
                "SUCCESS",
                Optional.of(command.reason().name()),
                command.correlationIdentifier(),
                Optional.empty(),
                command.originIdentifier(),
                Optional.of(command.replacementGenerationReference())
        );
    }

    private void lockIdentity(String identityReference) {
        dsl.fetch(
                "select pg_advisory_xact_lock(hashtextextended(cast(? as text), 0))",
                "identity-security|" + identityReference
        );
    }

    private IdentitySecurityGeneration toGeneration(Record row) {
        return new IdentitySecurityGeneration(
                row.get(IDENTITY_REFERENCE),
                row.get(CURRENT_GENERATION_REFERENCE),
                row.get(CONCURRENCY_VERSION),
                row.get(ESTABLISHED_AT),
                Optional.ofNullable(row.get(LAST_ROTATED_AT))
        );
    }

    private static String sessionRevocationReason(
            IdentitySecurityRotationCommand command
    ) {
        return "identity-security-generation-rotated/"
                + command.reason().name();
    }

    private static IdentitySecurityGenerationException failure(
            IdentitySecurityGenerationFailureCategory category,
            String message
    ) {
        return new IdentitySecurityGenerationException(category, message);
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
