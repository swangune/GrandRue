package mainstreet.infrastructure.security.webauthn;

import mainstreet.identitysecurity.IdentitySecurityGenerationInitializer;
import mainstreet.infrastructure.persistence.webauthn.JooqWebAuthnAuthenticationSubjectRepository;
import org.springframework.security.web.webauthn.api.Bytes;
import org.springframework.security.web.webauthn.api.ImmutablePublicKeyCredentialUserEntity;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialUserEntity;

import java.security.SecureRandom;
import java.util.Objects;

/**
 * Establishes the Main Street-owned opaque WebAuthn authentication subject for an Identity.
 *
 * <p>The generated handle is authentication infrastructure only. It contains no Main Street
 * Identity identifier, Merchant Scope or business-authority data.</p>
 */
public final class WebAuthnAuthenticationSubjectService {

    private static final int HANDLE_BYTES = 32;

    private final JooqWebAuthnAuthenticationSubjectRepository repository;
    private final IdentitySecurityGenerationInitializer securityInitializer;
    private final SecureRandom secureRandom;

    public WebAuthnAuthenticationSubjectService(
            JooqWebAuthnAuthenticationSubjectRepository repository,
            IdentitySecurityGenerationInitializer securityInitializer
    ) {
        this(repository, securityInitializer, new SecureRandom());
    }

    WebAuthnAuthenticationSubjectService(
            JooqWebAuthnAuthenticationSubjectRepository repository,
            IdentitySecurityGenerationInitializer securityInitializer,
            SecureRandom secureRandom
    ) {
        this.repository = Objects.requireNonNull(repository, "repository");
        this.securityInitializer = Objects.requireNonNull(
                securityInitializer,
                "securityInitializer"
        );
        this.secureRandom = Objects.requireNonNull(secureRandom, "secureRandom");
    }

    public PublicKeyCredentialUserEntity establish(
            String identityReference,
            String username,
            String displayName
    ) {
        requireNonBlank(identityReference, "identityReference");
        requireNonBlank(username, "username");

        PublicKeyCredentialUserEntity existing =
                repository.findByIdentityReference(identityReference);
        if (existing != null) {
            PublicKeyCredentialUserEntity refreshed =
                    ImmutablePublicKeyCredentialUserEntity.builder()
                            .id(existing.getId())
                            .name(username)
                            .displayName(displayName)
                            .build();
            PublicKeyCredentialUserEntity established =
                    repository.establish(identityReference, refreshed);
            securityInitializer.initialize(identityReference);
            return established;
        }

        byte[] handle = new byte[HANDLE_BYTES];
        secureRandom.nextBytes(handle);
        PublicKeyCredentialUserEntity candidate =
                ImmutablePublicKeyCredentialUserEntity.builder()
                        .id(new Bytes(handle))
                        .name(username)
                        .displayName(displayName)
                        .build();
        try {
            PublicKeyCredentialUserEntity established =
                    repository.establish(identityReference, candidate);
            securityInitializer.initialize(identityReference);
            return established;
        } catch (IllegalStateException conflict) {
            PublicKeyCredentialUserEntity concurrentlyEstablished =
                    repository.findByIdentityReference(identityReference);
            if (concurrentlyEstablished == null) {
                throw conflict;
            }
            PublicKeyCredentialUserEntity refreshed =
                    ImmutablePublicKeyCredentialUserEntity.builder()
                            .id(concurrentlyEstablished.getId())
                            .name(username)
                            .displayName(displayName)
                            .build();
            PublicKeyCredentialUserEntity established =
                    repository.establish(identityReference, refreshed);
            securityInitializer.initialize(identityReference);
            return established;
        }
    }

    private static String requireNonBlank(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must be non-blank");
        }
        return value;
    }
}
