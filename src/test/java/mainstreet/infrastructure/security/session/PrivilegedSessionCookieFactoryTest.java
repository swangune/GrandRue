package mainstreet.infrastructure.security.session;

import mainstreet.runtime.OpaqueSessionCredential;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;

import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrivilegedSessionCookieFactoryTest {

    @Test
    void issued_cookie_matches_the_adr_014_host_bound_privileged_session_contract() {
        OpaqueSessionCredential credential = OpaqueSessionCredential.generate(
                new SecureRandom()
        );

        ResponseCookie cookie = new PrivilegedSessionCookieFactory().issue(credential);

        assertEquals("__Host-MS-SESSION", cookie.getName());
        assertEquals(credential.value(), cookie.getValue());
        assertEquals("/", cookie.getPath());
        assertNull(cookie.getDomain());
        assertTrue(cookie.isSecure());
        assertTrue(cookie.isHttpOnly());
        assertEquals("Strict", cookie.getSameSite());
    }
}
