package grandrue.infrastructure.security.session;

import mainstreet.runtime.OpaqueSessionCredential;
import org.springframework.http.ResponseCookie;

import java.util.Objects;

/**
 * ADR-014 host-bound cookie contract for privileged human browser sessions.
 *
 * <p>This factory only materialises the accepted transport attributes for an
 * already-issued opaque Main Street session credential. It does not establish
 * authentication, session authority, Merchant Scope or CSRF policy.</p>
 */
public final class PrivilegedSessionCookieFactory {

    static final String COOKIE_NAME = "__Host-MS-SESSION";

    public ResponseCookie issue(OpaqueSessionCredential credential) {
        Objects.requireNonNull(credential, "credential");

        return ResponseCookie.from(COOKIE_NAME, credential.value())
                .path("/")
                .secure(true)
                .httpOnly(true)
                .sameSite("Strict")
                .build();
    }
}
