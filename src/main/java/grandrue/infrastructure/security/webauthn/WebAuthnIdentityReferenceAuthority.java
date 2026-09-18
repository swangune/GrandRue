package grandrue.infrastructure.security.webauthn;

import org.springframework.security.web.webauthn.api.PublicKeyCredentialUserEntity;

/**
 * Maps an already-verified Spring WebAuthn user entity to the authoritative
 * GrandRue Identity reference.
 *
 * <p>The WebAuthn user entity's human-readable name/display name is not Main
 * Street semantic Identity authority merely because Spring exposes it. A
 * concrete adapter must resolve the registered credential/user-handle binding
 * to the applicable GrandRue Identity.</p>
 */
@FunctionalInterface
public interface WebAuthnIdentityReferenceAuthority {

    String identityReference(PublicKeyCredentialUserEntity verifiedPrincipal);
}
