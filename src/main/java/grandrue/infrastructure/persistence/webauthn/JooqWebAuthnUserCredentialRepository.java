package grandrue.infrastructure.persistence.webauthn;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.security.web.webauthn.api.AuthenticatorTransport;
import org.springframework.security.web.webauthn.api.Bytes;
import org.springframework.security.web.webauthn.api.CredentialRecord;
import org.springframework.security.web.webauthn.api.ImmutableCredentialRecord;
import org.springframework.security.web.webauthn.api.ImmutablePublicKeyCose;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialType;
import org.springframework.security.web.webauthn.management.UserCredentialRepository;

import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Main Street-owned durable persistence adapter for Spring WebAuthn credential records.
 *
 * <p>This table stores authentication credential state only. It does not own Main Street
 * Identity relationships, Merchant Scope, Controller authority, roles or privileges.</p>
 */
public final class JooqWebAuthnUserCredentialRepository implements UserCredentialRepository {

    private static final Table<?> CREDENTIAL = DSL.table(DSL.name("webauthn_credential_record"));

    private static final Field<byte[]> CREDENTIAL_ID =
            DSL.field(DSL.name("credential_id"), byte[].class);
    private static final Field<byte[]> USER_ENTITY_USER_ID =
            DSL.field(DSL.name("user_entity_user_id"), byte[].class);
    private static final Field<String> CREDENTIAL_TYPE =
            DSL.field(DSL.name("credential_type"), String.class);
    private static final Field<byte[]> PUBLIC_KEY_COSE =
            DSL.field(DSL.name("public_key_cose"), byte[].class);
    private static final Field<Long> SIGNATURE_COUNT =
            DSL.field(DSL.name("signature_count"), Long.class);
    private static final Field<Boolean> UV_INITIALIZED =
            DSL.field(DSL.name("uv_initialized"), Boolean.class);
    private static final Field<String[]> TRANSPORTS =
            DSL.field(DSL.name("transports"), String[].class);
    private static final Field<Boolean> BACKUP_ELIGIBLE =
            DSL.field(DSL.name("backup_eligible"), Boolean.class);
    private static final Field<Boolean> BACKUP_STATE =
            DSL.field(DSL.name("backup_state"), Boolean.class);
    private static final Field<byte[]> ATTESTATION_OBJECT =
            DSL.field(DSL.name("attestation_object"), byte[].class);
    private static final Field<byte[]> ATTESTATION_CLIENT_DATA_JSON =
            DSL.field(DSL.name("attestation_client_data_json"), byte[].class);
    private static final Field<Instant> CREATED_AT =
            DSL.field(DSL.name("created_at"), Instant.class);
    private static final Field<Instant> LAST_USED_AT =
            DSL.field(DSL.name("last_used_at"), Instant.class);
    private static final Field<String> LABEL =
            DSL.field(DSL.name("label"), String.class);

    private static final Field<byte[]> STORED_USER_ENTITY_USER_ID =
            DSL.field(DSL.name("webauthn_credential_record", "user_entity_user_id"), byte[].class);
    private static final Field<byte[]> STORED_PUBLIC_KEY_COSE =
            DSL.field(DSL.name("webauthn_credential_record", "public_key_cose"), byte[].class);
    private static final Field<Instant> STORED_CREATED_AT =
            DSL.field(DSL.name("webauthn_credential_record", "created_at"), Instant.class);

    private final DSLContext dsl;

    public JooqWebAuthnUserCredentialRepository(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
    }

    @Override
    public void save(CredentialRecord credentialRecord) {
        Objects.requireNonNull(credentialRecord, "credentialRecord");

        byte[] credentialId = credentialRecord.getCredentialId().getBytes();
        byte[] userId = credentialRecord.getUserEntityUserId().getBytes();
        byte[] publicKey = credentialRecord.getPublicKey().getBytes();

        int affected = dsl.insertInto(CREDENTIAL)
                .columns(
                        CREDENTIAL_ID,
                        USER_ENTITY_USER_ID,
                        CREDENTIAL_TYPE,
                        PUBLIC_KEY_COSE,
                        SIGNATURE_COUNT,
                        UV_INITIALIZED,
                        TRANSPORTS,
                        BACKUP_ELIGIBLE,
                        BACKUP_STATE,
                        ATTESTATION_OBJECT,
                        ATTESTATION_CLIENT_DATA_JSON,
                        CREATED_AT,
                        LAST_USED_AT,
                        LABEL
                )
                .values(
                        credentialId,
                        userId,
                        credentialRecord.getCredentialType() == null
                                ? null
                                : credentialRecord.getCredentialType().getValue(),
                        publicKey,
                        credentialRecord.getSignatureCount(),
                        credentialRecord.isUvInitialized(),
                        transportValues(credentialRecord.getTransports()),
                        credentialRecord.isBackupEligible(),
                        credentialRecord.isBackupState(),
                        nullableBytes(credentialRecord.getAttestationObject()),
                        nullableBytes(credentialRecord.getAttestationClientDataJSON()),
                        credentialRecord.getCreated(),
                        credentialRecord.getLastUsed(),
                        credentialRecord.getLabel()
                )
                .onConflict(CREDENTIAL_ID)
                .doUpdate()
                .set(SIGNATURE_COUNT, credentialRecord.getSignatureCount())
                .set(UV_INITIALIZED, credentialRecord.isUvInitialized())
                .set(BACKUP_STATE, credentialRecord.isBackupState())
                .set(LAST_USED_AT, credentialRecord.getLastUsed())
                .set(LABEL, credentialRecord.getLabel())
                .where(STORED_USER_ENTITY_USER_ID.eq(userId))
                .and(STORED_PUBLIC_KEY_COSE.eq(publicKey))
                .and(STORED_CREATED_AT.eq(credentialRecord.getCreated()))
                .execute();

        if (affected != 1) {
            throw new IllegalStateException(
                    "WebAuthn credential identity already exists with different registration evidence"
            );
        }
    }

    @Override
    public CredentialRecord findByCredentialId(Bytes credentialId) {
        Objects.requireNonNull(credentialId, "credentialId");
        Record row = dsl.select(
                        CREDENTIAL_ID,
                        USER_ENTITY_USER_ID,
                        CREDENTIAL_TYPE,
                        PUBLIC_KEY_COSE,
                        SIGNATURE_COUNT,
                        UV_INITIALIZED,
                        TRANSPORTS,
                        BACKUP_ELIGIBLE,
                        BACKUP_STATE,
                        ATTESTATION_OBJECT,
                        ATTESTATION_CLIENT_DATA_JSON,
                        CREATED_AT,
                        LAST_USED_AT,
                        LABEL
                )
                .from(CREDENTIAL)
                .where(CREDENTIAL_ID.eq(credentialId.getBytes()))
                .fetchOne();
        return row == null ? null : toCredentialRecord(row);
    }

    @Override
    public List<CredentialRecord> findByUserId(Bytes userId) {
        Objects.requireNonNull(userId, "userId");
        return dsl.select(
                        CREDENTIAL_ID,
                        USER_ENTITY_USER_ID,
                        CREDENTIAL_TYPE,
                        PUBLIC_KEY_COSE,
                        SIGNATURE_COUNT,
                        UV_INITIALIZED,
                        TRANSPORTS,
                        BACKUP_ELIGIBLE,
                        BACKUP_STATE,
                        ATTESTATION_OBJECT,
                        ATTESTATION_CLIENT_DATA_JSON,
                        CREATED_AT,
                        LAST_USED_AT,
                        LABEL
                )
                .from(CREDENTIAL)
                .where(USER_ENTITY_USER_ID.eq(userId.getBytes()))
                .orderBy(CREATED_AT.asc(), CREDENTIAL_ID.asc())
                .fetch(this::toCredentialRecord);
    }

    @Override
    public void delete(Bytes credentialId) {
        Objects.requireNonNull(credentialId, "credentialId");
        dsl.deleteFrom(CREDENTIAL)
                .where(CREDENTIAL_ID.eq(credentialId.getBytes()))
                .execute();
    }

    private CredentialRecord toCredentialRecord(Record row) {
        var builder = ImmutableCredentialRecord.builder()
                .credentialId(new Bytes(row.get(CREDENTIAL_ID)))
                .userEntityUserId(new Bytes(row.get(USER_ENTITY_USER_ID)))
                .publicKey(new ImmutablePublicKeyCose(row.get(PUBLIC_KEY_COSE)))
                .signatureCount(row.get(SIGNATURE_COUNT))
                .uvInitialized(Boolean.TRUE.equals(row.get(UV_INITIALIZED)))
                .transports(transportSet(row.get(TRANSPORTS)))
                .backupEligible(Boolean.TRUE.equals(row.get(BACKUP_ELIGIBLE)))
                .backupState(Boolean.TRUE.equals(row.get(BACKUP_STATE)))
                .created(row.get(CREATED_AT))
                .lastUsed(row.get(LAST_USED_AT))
                .label(row.get(LABEL));

        String credentialType = row.get(CREDENTIAL_TYPE);
        if (credentialType != null) {
            builder.credentialType(PublicKeyCredentialType.valueOf(credentialType));
        }
        byte[] attestationObject = row.get(ATTESTATION_OBJECT);
        if (attestationObject != null) {
            builder.attestationObject(new Bytes(attestationObject));
        }
        byte[] clientDataJson = row.get(ATTESTATION_CLIENT_DATA_JSON);
        if (clientDataJson != null) {
            builder.attestationClientDataJSON(new Bytes(clientDataJson));
        }
        return builder.build();
    }

    private static String[] transportValues(Set<AuthenticatorTransport> transports) {
        if (transports == null || transports.isEmpty()) {
            return new String[0];
        }
        return transports.stream()
                .map(AuthenticatorTransport::getValue)
                .sorted()
                .toArray(String[]::new);
    }

    private static Set<AuthenticatorTransport> transportSet(String[] transports) {
        if (transports == null || transports.length == 0) {
            return Set.of();
        }
        return Arrays.stream(transports)
                .map(AuthenticatorTransport::valueOf)
                .sorted(Comparator.comparing(AuthenticatorTransport::getValue))
                .collect(Collectors.toUnmodifiableSet());
    }

    private static byte[] nullableBytes(Bytes value) {
        return value == null ? null : value.getBytes();
    }
}
