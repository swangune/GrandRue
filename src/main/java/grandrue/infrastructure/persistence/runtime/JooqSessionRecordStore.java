package grandrue.infrastructure.persistence.runtime;

import grandrue.runtime.SessionRecord;
import grandrue.runtime.SessionRecordStore;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * PostgreSQL/jOOQ persistence for ADR-014 server-authoritative Session Records.
 *
 * <p>The adapter persists only a one-way credential verifier. It does not own
 * or persist Merchant Scope, roles, privileges, entitlement, device
 * authorisation or other mutable business authority.</p>
 */
public final class JooqSessionRecordStore implements SessionRecordStore {

    private static final Table<?> SESSION =
            DSL.table(DSL.name("authentication_session_record"));

    private static final Field<String> SESSION_IDENTITY =
            DSL.field(DSL.name("session_identity"), String.class);
    private static final Field<String> IDENTITY_REFERENCE =
            DSL.field(DSL.name("identity_reference"), String.class);
    private static final Field<String> CREDENTIAL_VERIFIER =
            DSL.field(DSL.name("credential_verifier"), String.class);
    private static final Field<Instant> ESTABLISHED_AT =
            DSL.field(DSL.name("established_at"), Instant.class);
    private static final Field<String> AUTHENTICATION_ASSURANCE_REFERENCE =
            DSL.field(DSL.name("authentication_assurance_reference"), String.class);
    private static final Field<String> AUTHENTICATION_METHOD_REFERENCE =
            DSL.field(DSL.name("authentication_method_reference"), String.class);
    private static final Field<Instant> ABSOLUTE_EXPIRY =
            DSL.field(DSL.name("absolute_expiry"), Instant.class);
    private static final Field<Instant> LAST_ACTIVITY_AT =
            DSL.field(DSL.name("last_activity_at"), Instant.class);
    private static final Field<String> SECURITY_GENERATION_REFERENCE =
            DSL.field(DSL.name("security_generation_reference"), String.class);
    private static final Field<Instant> REVOKED_AT =
            DSL.field(DSL.name("revoked_at"), Instant.class);
    private static final Field<String> REVOCATION_REASON =
            DSL.field(DSL.name("revocation_reason"), String.class);

    private final DSLContext dsl;

    public JooqSessionRecordStore(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
    }

    @Override
    public SessionRecord create(SessionRecord candidate) {
        Objects.requireNonNull(candidate, "candidate");

        int inserted = dsl.insertInto(SESSION)
                .columns(
                        SESSION_IDENTITY,
                        IDENTITY_REFERENCE,
                        CREDENTIAL_VERIFIER,
                        ESTABLISHED_AT,
                        AUTHENTICATION_ASSURANCE_REFERENCE,
                        AUTHENTICATION_METHOD_REFERENCE,
                        ABSOLUTE_EXPIRY,
                        LAST_ACTIVITY_AT,
                        SECURITY_GENERATION_REFERENCE,
                        REVOKED_AT,
                        REVOCATION_REASON
                )
                .values(
                        candidate.sessionIdentity(),
                        candidate.identityReference(),
                        candidate.credentialVerifier(),
                        candidate.establishedAt(),
                        candidate.authenticationAssuranceReference(),
                        candidate.authenticationMethodReference(),
                        candidate.absoluteExpiry(),
                        candidate.lastActivityAt(),
                        candidate.securityGenerationReference(),
                        candidate.revokedAt().orElse(null),
                        candidate.revocationReason().orElse(null)
                )
                .onConflictDoNothing()
                .execute();

        if (inserted == 1) {
            return candidate;
        }

        Optional<SessionRecord> existingIdentity = sessionByIdentity(
                candidate.sessionIdentity()
        );
        if (existingIdentity.isPresent()) {
            SessionRecord committed = existingIdentity.orElseThrow();
            if (committed.equals(candidate)) {
                return committed;
            }
            throw new IllegalStateException(
                    "Session identity already exists with different security evidence"
            );
        }

        if (sessionByCredentialVerifier(candidate.credentialVerifier()).isPresent()) {
            throw new IllegalStateException(
                    "Session credential verifier is already bound to another session"
            );
        }

        throw new IllegalStateException(
                "Session record could not be created or resolved after conflict"
        );
    }

    @Override
    public Optional<SessionRecord> sessionByIdentity(String sessionIdentity) {
        require(sessionIdentity, "sessionIdentity");
        Record row = selectSessionFields()
                .from(SESSION)
                .where(SESSION_IDENTITY.eq(sessionIdentity))
                .fetchOne();
        return Optional.ofNullable(row).map(this::toSessionRecord);
    }

    @Override
    public Optional<SessionRecord> sessionByCredentialVerifier(
            String credentialVerifier
    ) {
        require(credentialVerifier, "credentialVerifier");
        Record row = selectSessionFields()
                .from(SESSION)
                .where(CREDENTIAL_VERIFIER.eq(credentialVerifier))
                .fetchOne();
        return Optional.ofNullable(row).map(this::toSessionRecord);
    }

    @Override
    public SessionRecord revoke(
            String sessionIdentity,
            Instant revokedAt,
            String reason
    ) {
        require(sessionIdentity, "sessionIdentity");
        Objects.requireNonNull(revokedAt, "revokedAt");
        require(reason, "reason");

        SessionRecord current = sessionByIdentity(sessionIdentity)
                .orElseThrow(() -> new IllegalStateException(
                        "Unknown session identity: " + sessionIdentity
                ));
        SessionRecord desired = current.revoke(revokedAt, reason);
        if (current == desired) {
            return current;
        }

        dsl.update(SESSION)
                .set(REVOKED_AT, revokedAt)
                .set(REVOCATION_REASON, reason)
                .where(SESSION_IDENTITY.eq(sessionIdentity))
                .and(REVOKED_AT.isNull())
                .execute();

        SessionRecord committed = sessionByIdentity(sessionIdentity)
                .orElseThrow(() -> new IllegalStateException(
                        "Session disappeared during revocation: " + sessionIdentity
                ));
        if (!committed.equals(desired)) {
            throw new IllegalStateException(
                    "Session was concurrently revoked with different evidence"
            );
        }
        return committed;
    }

    @Override
    public void revokeAllForIdentity(
            String identityReference,
            Instant revokedAt,
            String reason
    ) {
        require(identityReference, "identityReference");
        Objects.requireNonNull(revokedAt, "revokedAt");
        require(reason, "reason");

        dsl.update(SESSION)
                .set(REVOKED_AT, revokedAt)
                .set(REVOCATION_REASON, reason)
                .where(IDENTITY_REFERENCE.eq(identityReference))
                .and(REVOKED_AT.isNull())
                .execute();
    }

    private org.jooq.SelectSelectStep<? extends Record> selectSessionFields() {
        return dsl.select(
                SESSION_IDENTITY,
                IDENTITY_REFERENCE,
                CREDENTIAL_VERIFIER,
                ESTABLISHED_AT,
                AUTHENTICATION_ASSURANCE_REFERENCE,
                AUTHENTICATION_METHOD_REFERENCE,
                ABSOLUTE_EXPIRY,
                LAST_ACTIVITY_AT,
                SECURITY_GENERATION_REFERENCE,
                REVOKED_AT,
                REVOCATION_REASON
        );
    }

    private SessionRecord toSessionRecord(Record row) {
        return new SessionRecord(
                row.get(SESSION_IDENTITY),
                row.get(IDENTITY_REFERENCE),
                row.get(CREDENTIAL_VERIFIER),
                row.get(ESTABLISHED_AT),
                row.get(AUTHENTICATION_ASSURANCE_REFERENCE),
                row.get(AUTHENTICATION_METHOD_REFERENCE),
                row.get(ABSOLUTE_EXPIRY),
                row.get(LAST_ACTIVITY_AT),
                row.get(SECURITY_GENERATION_REFERENCE),
                Optional.ofNullable(row.get(REVOKED_AT)),
                Optional.ofNullable(row.get(REVOCATION_REASON))
        );
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
