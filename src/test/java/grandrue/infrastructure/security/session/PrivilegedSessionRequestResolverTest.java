package grandrue.infrastructure.security.session;

import jakarta.servlet.http.Cookie;
import grandrue.runtime.AuthenticationException;
import grandrue.runtime.AuthenticationFailureCategory;
import grandrue.runtime.AuthenticationSession;
import grandrue.runtime.SessionCredentialResolver;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class PrivilegedSessionRequestResolverTest {

    @Test
    void exact_privileged_cookie_is_resolved_through_grandrue_session_authority() {
        SessionCredentialResolver sessionCredentialResolver = mock(SessionCredentialResolver.class);
        AuthenticationSession expected = authenticationSession();
        when(sessionCredentialResolver.resolve("opaque-session-credential"))
                .thenReturn(expected);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(
                new Cookie("analytics", "not-authority"),
                new Cookie("__Host-MS-SESSION", "opaque-session-credential")
        );

        AuthenticationSession resolved = new PrivilegedSessionRequestResolver(
                sessionCredentialResolver
        ).resolve(request);

        assertSame(expected, resolved);
        verify(sessionCredentialResolver).resolve("opaque-session-credential");
    }

    @Test
    void missing_privileged_cookie_fails_closed_before_session_authority_is_touched() {
        SessionCredentialResolver sessionCredentialResolver = mock(SessionCredentialResolver.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer header-must-not-become-browser-session");
        request.setParameter("session", "parameter-must-not-become-browser-session");

        AuthenticationException failure = assertThrows(
                AuthenticationException.class,
                () -> new PrivilegedSessionRequestResolver(sessionCredentialResolver)
                        .resolve(request)
        );

        assertEquals(AuthenticationFailureCategory.AUTHENTICATION_FAILED, failure.category());
        verifyNoInteractions(sessionCredentialResolver);
    }

    @Test
    void blank_privileged_cookie_fails_closed_before_session_authority_is_touched() {
        SessionCredentialResolver sessionCredentialResolver = mock(SessionCredentialResolver.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("__Host-MS-SESSION", ""));

        AuthenticationException failure = assertThrows(
                AuthenticationException.class,
                () -> new PrivilegedSessionRequestResolver(sessionCredentialResolver)
                        .resolve(request)
        );

        assertEquals(AuthenticationFailureCategory.AUTHENTICATION_FAILED, failure.category());
        verifyNoInteractions(sessionCredentialResolver);
    }

    @Test
    void ambiguous_duplicate_privileged_cookies_fail_closed() {
        SessionCredentialResolver sessionCredentialResolver = mock(SessionCredentialResolver.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(
                new Cookie("__Host-MS-SESSION", "credential-a"),
                new Cookie("__Host-MS-SESSION", "credential-b")
        );

        AuthenticationException failure = assertThrows(
                AuthenticationException.class,
                () -> new PrivilegedSessionRequestResolver(sessionCredentialResolver)
                        .resolve(request)
        );

        assertEquals(AuthenticationFailureCategory.AUTHENTICATION_FAILED, failure.category());
        verifyNoInteractions(sessionCredentialResolver);
    }

    private static AuthenticationSession authenticationSession() {
        return new AuthenticationSession(
                "session-1",
                "identity-1",
                Instant.parse("2026-08-28T08:00:00Z"),
                Instant.parse("2026-08-28T20:00:00Z"),
                Optional.empty()
        );
    }
}
