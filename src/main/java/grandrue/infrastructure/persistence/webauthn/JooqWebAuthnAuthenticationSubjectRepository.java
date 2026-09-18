package grandrue.infrastructure.persistence.webauthn;

import grandrue.infrastructure.security.webauthn.WebAuthnIdentityReferenceAuthority;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.springframework.security.web.webauthn.api.Bytes;
import org.springframework.security.web.webauthn.api.ImmutablePublicKeyCredentialUserEntity;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialUserEntity;
import org.springframework.security.web.webauthn.management.PublicKeyCredentialUserEntityRepository;

import java.util.Arrays;
import java.util.Objects;

/**
 * GrandRue-owned persistence for the technical WebAuthn authentication subject.
 *
 * <p>The opaque user handle is bound one-to-one to a GrandRue Identity reference,
 * but this repository does not own Merchant Scope, Controller relationships, roles,
 * privileges, entitlements or any other business authority.</p>
 */
public final class JooqWebAuthnAuthenticationSubjectRepository
        implements PublicKeyCredentialUserEntityRepository, WebAuthnIdentityReferenceAuthority {

    private static final Table<?> SUBJECT =
            DSL.table(DSL.name("webauthn_authentication_subject"));
    private static final Field<byte[]> USER_HANDLE =
            DSL.field(DSL.name("user_handle"), byte[].class);
    private static final Field<String> IDENTITY_REFERENCE =
            DSL.field(DSL.name("identity_reference"), String.class);
    private static final Field<String> USERNAME =
            DSL.field(DSL.name("username"), String.class);
    private static final Field<String> DISPLAY_NAME =
            DSL.field(DSL.name("display_name"), String.class);

    private final DSLContext dsl;

    public JooqWebAuthnAuthenticationSubjectRepository(DSLContext dsl) {
        this.dsl = Objects.requireNonNull(dsl, "dsl");
    }

    public PublicKeyCredentialUserEntity establish(
            String identityReference,
            PublicKeyCredentialUserEntity userEntity
    ) {
        String requiredIdentity = requireNonBlank(identityReference, "identityReference");
        PublicKeyCredentialUserEntity requiredEntity = requireValid(userEntity);

        Record existingIdentity = rowByIdentity(requiredIdentity);
        if (existingIdentity != null) {
            byte[] existingHandle = existingIdentity.get(USER_HANDLE);
            if (!Arrays.equals(existingHandle, requiredEntity.getId().getBytes())) {
                throw new IllegalStateException(
                        "GrandRue Identity is already bound to another WebAuthn user handle"
                );
            }
            save(requiredEntity);
            return findById(requiredEntity.getId());
        }

        Record existingHandle = rowById(requiredEntity.getId());
        if (existingHandle != null) {
            throw new IllegalStateException(
                    "WebAuthn user handle is already bound to another GrandRue Identity"
            );
        }

        try {
            int inserted = dsl.insertInto(SUBJECT)
                    .columns(
                            USER_HANDLE,
                            IDENTITY_REFERENCE,
                            USERNAME,
                            DISPLAY_NAME
                    )
                    .values(
                            requiredEntity.getId().getBytes(),
                            requiredIdentity,
                            requiredEntity.getName(),
                            requiredEntity.getDisplayName()
                    )
                    .onConflictDoNothing()
                    .execute();

            if (inserted != 1) {
                Record concurrentIdentity = rowByIdentity(requiredIdentity);
                if (concurrentIdentity != null
                        && Arrays.equals(
                                concurrentIdentity.get(USER_HANDLE),
                                requiredEntity.getId().getBytes()
                        )) {
                    save(requiredEntity);
                    return findById(requiredEntity.getId());
                }
                throw new IllegalStateException(
                        "WebAuthn authentication-subject binding conflicts with existing state"
                );
            }
        } catch (DataAccessException exception) {
            throw new IllegalStateException(
                    "WebAuthn authentication-subject binding conflicts with existing state",
                    exception
            );
        }

        return findById(requiredEntity.getId());
    }

    public PublicKeyCredentialUserEntity findByIdentityReference(String identityReference) {
        String requiredIdentity = requireNonBlank(identityReference, "identityReference");
        Record row = rowByIdentity(requiredIdentity);
        return row == null ? null : toUserEntity(row);
    }

    @Override
    public PublicKeyCredentialUserEntity findById(Bytes id) {
        Objects.requireNonNull(id, "id");
        Record row = rowById(id);
        return row == null ? null : toUserEntity(row);
    }

    @Override
    public PublicKeyCredentialUserEntity findByUsername(String username) {
        String requiredUsername = requireNonBlank(username, "username");
        Record row = dsl.select(
                        USER_HANDLE,
                        IDENTITY_REFERENCE,
                        USERNAME,
                        DISPLAY_NAME
                )
                .from(SUBJECT)
                .where(USERNAME.eq(requiredUsername))
                .fetchOne();
        return row == null ? null : toUserEntity(row);
    }

    @Override
    public void save(PublicKeyCredentialUserEntity userEntity) {
        PublicKeyCredentialUserEntity requiredEntity = requireValid(userEntity);
        try {
            int updated = dsl.update(SUBJECT)
                    .set(USERNAME, requiredEntity.getName())
                    .set(DISPLAY_NAME, requiredEntity.getDisplayName())
                    .where(USER_HANDLE.eq(requiredEntity.getId().getBytes()))
                    .execute();
            if (updated != 1) {
                throw new IllegalStateException(
                        "Spring WebAuthn cannot create a caller-selected GrandRue user handle"
                );
            }
        } catch (DataAccessException exception) {
            throw new IllegalStateException(
                    "WebAuthn human-readable account fields conflict with existing subject state",
                    exception
            );
        }
    }

    @Override
    public void delete(Bytes id) {
        Objects.requireNonNull(id, "id");
        dsl.deleteFrom(SUBJECT)
                .where(USER_HANDLE.eq(id.getBytes()))
                .execute();
    }

    @Override
    public String identityReference(PublicKeyCredentialUserEntity verifiedPrincipal) {
        PublicKeyCredentialUserEntity requiredPrincipal = requireValid(verifiedPrincipal);
        Record row = rowById(requiredPrincipal.getId());
        if (row == null) {
            throw new IllegalStateException(
                    "Verified WebAuthn user handle has no GrandRue Identity binding"
            );
        }
        return row.get(IDENTITY_REFERENCE);
    }

    private Record rowById(Bytes id) {
        return dsl.select(
                        USER_HANDLE,
                        IDENTITY_REFERENCE,
                        USERNAME,
                        DISPLAY_NAME
                )
                .from(SUBJECT)
                .where(USER_HANDLE.eq(id.getBytes()))
                .fetchOne();
    }

    private Record rowByIdentity(String identityReference) {
        return dsl.select(
                        USER_HANDLE,
                        IDENTITY_REFERENCE,
                        USERNAME,
                        DISPLAY_NAME
                )
                .from(SUBJECT)
                .where(IDENTITY_REFERENCE.eq(identityReference))
                .fetchOne();
    }

    private PublicKeyCredentialUserEntity toUserEntity(Record row) {
        return ImmutablePublicKeyCredentialUserEntity.builder()
                .id(new Bytes(row.get(USER_HANDLE)))
                .name(row.get(USERNAME))
                .displayName(row.get(DISPLAY_NAME))
                .build();
    }

    private static PublicKeyCredentialUserEntity requireValid(
            PublicKeyCredentialUserEntity userEntity
    ) {
        Objects.requireNonNull(userEntity, "userEntity");
        Objects.requireNonNull(userEntity.getId(), "userEntity.id");
        byte[] handle = userEntity.getId().getBytes();
        if (handle.length < 1 || handle.length > 64) {
            throw new IllegalArgumentException("WebAuthn user handle must contain 1..64 bytes");
        }
        requireNonBlank(userEntity.getName(), "userEntity.name");
        return userEntity;
    }

    private static String requireNonBlank(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must be non-blank");
        }
        return value;
    }
}
